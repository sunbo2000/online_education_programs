package org.snbo.uCenterService.service;

import org.snbo.uCenterService.bean.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import org.snbo.uCenterService.bean.vo.LoginInfoVo;
import org.snbo.uCenterService.bean.vo.LoginVo;
import org.snbo.uCenterService.bean.vo.RegisterVo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author snbo
 * @since 2022-04-06
 */
public interface UCenterMemberService extends IService<UcenterMember> {
    String login(LoginVo loginVo);

    void register(RegisterVo registerVo);

    LoginInfoVo getLoginInfo(HttpServletRequest request);

    UcenterMember getMemberByOpenId(String openId);

    Integer countRegister(String day);
}
