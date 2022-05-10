package org.snbo.uCenterService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.snbo.commonutils.JwtUtils;
import org.snbo.commonutils.MD5Utils;
import org.snbo.commonutils.TimeUtils;
import org.snbo.servicebase.ExceptionHandler.MoguException;
import org.snbo.uCenterService.bean.UcenterMember;
import org.snbo.uCenterService.bean.vo.LoginInfoVo;
import org.snbo.uCenterService.bean.vo.LoginVo;
import org.snbo.uCenterService.bean.vo.RegisterVo;
import org.snbo.uCenterService.mapper.UcenterMemberMapper;
import org.snbo.uCenterService.service.UCenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author snbo
 * @since 2022-04-06
 */
@Service
public class UCenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UCenterMemberService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //登录方法
    @Override
    public String login(LoginVo loginVo) {
        //登录记录放进redis里
        String loginCountName = TimeUtils.getNowLoginCountName();
        String loginCount = redisTemplate.opsForValue().get(loginCountName);
        if (StringUtils.isEmpty(loginCount)) {
            redisTemplate.opsForValue().set(loginCountName, "1", TimeUtils.getInvalidTime(), TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(loginCountName, String.valueOf(Integer.parseInt(loginCount) + 1), 0);
        }

        //获取登录手机号和密码
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //手机号和密码不能为空,前后端都判断
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new MoguException(20001, "登录失败");
        }

        //根据手机号码获取出用户再进行后面的判断
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        UcenterMember theMember = baseMapper.selectOne(wrapper);

        if (theMember == null) {
            throw new MoguException(20001, "账号或密码错误");
        }
        //判断密码是否正确
        //md5加密后比较
        if (!MD5Utils.encrypt(password).equals(theMember.getPassword())) {
            throw new MoguException(20001, "账号或密码错误");
        }

        //判断用户状态
        if (theMember.getIsDisabled()) {
            throw new MoguException(20001, "登录失败");
        }
        //验证成功
        //生成token字符串
        return JwtUtils.getJwtToken(theMember.getId(), theMember.getNickname());
    }

    @Override
    public void register(RegisterVo registerVo) {
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String nickname = registerVo.getNickname();
        String code = registerVo.getCode();
        //判断非空
        if (StringUtils.isEmpty(nickname) || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password) || StringUtils.isEmpty(code)) {
            throw new MoguException(20001, "注册失败");
        }

        //判断验证码
        //从redis中取出数据
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(redisCode)) {
            throw new MoguException(20001, "验证码错误,请检查后输入");
        }
        //判断是否已经注册过
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new MoguException(20001, "该号码已注册账号");
        }

        //都无误,添加到数据库
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5Utils.encrypt(password));
        //默认头像
        member.setAvatar("https://edu-mogu.oss-cn-chengdu.aliyuncs.com/%E9%BB%98%E8%AE%A4%E5%AE%87%E8%88%AA%E5%91%98%E5%A4%B4%E5%83%8F.png");
        baseMapper.insert(member);
    }


    /**
     * 根据id获取是否登录信息
     */
    @Override
    public LoginInfoVo getLoginInfo(HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)) {
            throw new MoguException(28004, "未登录");
        }
        UcenterMember member = baseMapper.selectById(memberId);
        LoginInfoVo loginInfoVo = new LoginInfoVo();
        BeanUtils.copyProperties(member, loginInfoVo);
        return loginInfoVo;
    }

    @Override
    public UcenterMember getMemberByOpenId(String openId) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openId);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public Integer countRegister(String day) {
        return baseMapper.countRegister(day);
    }
}
