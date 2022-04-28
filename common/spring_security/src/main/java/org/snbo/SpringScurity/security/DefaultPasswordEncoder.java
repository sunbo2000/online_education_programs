package org.snbo.SpringScurity.security;


import org.snbo.commonutils.MD5Utils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sun.security.provider.MD5;

/**
 * <p>
 * t密码的处理方法类型
 * </p>
 *
 * @author qy
 * @since 2019-11-08
 */
@Component
public class DefaultPasswordEncoder implements PasswordEncoder {

    public DefaultPasswordEncoder() {
        this(-1);
    }

    /**
     * @param strength
     *            the log rounds to use, between 4 and 31
     */
    public DefaultPasswordEncoder(int strength) {

    }

    public String encode(CharSequence rawPassword) {
        return MD5Utils.encrypt(rawPassword.toString());
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(MD5Utils.encrypt(rawPassword.toString()));
    }
}