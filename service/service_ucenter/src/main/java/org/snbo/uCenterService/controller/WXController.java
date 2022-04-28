package org.snbo.uCenterService.controller;

import com.google.gson.Gson;
import io.jsonwebtoken.Jwt;
import org.snbo.commonutils.JwtUtils;
import org.snbo.servicebase.ExceptionHandler.MoguException;
import org.snbo.uCenterService.bean.UcenterMember;
import org.snbo.uCenterService.service.UCenterMemberService;
import org.snbo.uCenterService.util.ConstantWXUtils;
import org.snbo.uCenterService.util.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author sunbo
 * @create 2022-04-09-15:09
 */
@Controller
@RequestMapping("/api/ucenter/wx")
public class WXController {

    @Autowired
    private UCenterMemberService memberService;

    @GetMapping("/callback")
    public String callback(String code, String state) {
        try {
            //得到授权临时票据code
            System.out.println(code);
            //从redis中将state获取出来，和当前传入的state作比较
            //如果一致则放行，如果不一致则抛出异常：非法访问

            //向认证服务器发送请求换取access_token
            String baseAccessTokenUrl =
                    "https://api.weixin.qq.com/sns/oauth2/access_token" +
                            "?appid=%s" +
                            "&secret=%s" +
                            "&code=%s" +
                            "&grant_type=authorization_code";
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantWXUtils.WX_OPEN_APP_ID,
                    ConstantWXUtils.WX_OPEN_APP_SECRET,
                    code);
            //发送请求得到 accessToken 和 openId
            //使用 httpclient 发送请求,得到结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            //accessTokenInfo 是 key-value 形式的字符串,可以将其装换为map
            Gson gson = new Gson();
            HashMap<String, String> mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String accessToken = mapAccessToken.get("access_token");
            String openid = mapAccessToken.get("openid");

            //判断数据库中是否存在该微信用户,不存在添加,存在直接登录
            UcenterMember member = memberService.getMemberByOpenId(openid);
            if (member == null) {
                //拿到accessToken 和 openId,再去请求固定地址,获取扫描人信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
                String userInfo = HttpClientUtils.get(userInfoUrl);
                HashMap<String, String> uerInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = uerInfoMap.get("nickname");
                String headimgurl = uerInfoMap.get("headimgurl");
                //给数据库添加信息
                member = new UcenterMember();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }
            //使用jwt生成token字符串,里面存储id和昵称
            String jwtToken = JwtUtils.getJwtToken(member.getId(),member.getNickname());
            //最后通过路径传递回首页面
            System.out.println(jwtToken);
            return "redirect:http://localhost:3000?token="+jwtToken;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MoguException(20001, "登录失败");
        }

    }

    //生成微信扫描二维码
    @GetMapping("/login")
    public String getWXCode() {
        //请求微信地址
        //%s 为占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        //对 redirect_uri 进行编码
        String redirectUrl = ConstantWXUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format(baseUrl, ConstantWXUtils.WX_OPEN_APP_ID,
                redirectUrl, "atmogu");
        return "redirect:" + url;
    }
}
