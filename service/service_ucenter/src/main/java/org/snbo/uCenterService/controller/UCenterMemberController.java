package org.snbo.uCenterService.controller;


import io.swagger.annotations.ApiOperation;
import org.snbo.commonutils.R;
import org.snbo.commonutils.vo.UCenterMember;
import org.snbo.uCenterService.bean.vo.LoginInfoVo;
import org.snbo.uCenterService.bean.vo.LoginVo;
import org.snbo.uCenterService.bean.vo.RegisterVo;
import org.snbo.uCenterService.service.UCenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author snbo
 * @since 2022-04-06
 */
@RestController
@RequestMapping("/eduCenter/member")
public class UCenterMemberController {

    @Autowired
    private UCenterMemberService memberService;


    @PostMapping("/login")
    @ApiOperation(value = "登录验证")
    public R login(@RequestBody LoginVo loginVo) {
        //要求登录后返回token值
        String token = memberService.login(loginVo);
        return R.ok().data("token", token);
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册验证")
    public R register(@RequestBody RegisterVo registerVo) {
        memberService.register(registerVo);
        return R.ok();
    }

    //根据 token 获取用户信息
    @GetMapping("/getInfo")
    public R getMemberInfo(HttpServletRequest request) {
        //从请求里面获取到id,根据id查询到用户信息,将用户信息返回
        LoginInfoVo loginInfo = memberService.getLoginInfo(request);
        return R.ok().data("loginInfo", loginInfo);
    }

    //根据id获取用户信息
    //feign调用
    @GetMapping("/{id}")
    public UCenterMember getMemberInfoById(@PathVariable("id") String id) {
        UCenterMember uCenterMember = new UCenterMember();
        BeanUtils.copyProperties(memberService.getById(id), uCenterMember);
        return uCenterMember;
    }

    //查询某一天注册人数
    @GetMapping("/countRegister/{day}")
    public Integer countRegister(@PathVariable String day) {
        return memberService.countRegister(day);
    }
}

