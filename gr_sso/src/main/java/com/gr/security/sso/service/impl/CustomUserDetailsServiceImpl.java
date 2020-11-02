package com.gr.security.sso.service.impl;

import com.gr.security.sso.entity.SysRole;
import com.gr.security.sso.entity.UserDetail;
import com.gr.security.sso.mapper.LoginMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 登陆身份认证
 * @author: lfz
 * @since  2020.09.30
 */
@Component(value="customUserDetailsService")
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final LoginMapper loginMapper;

    public CustomUserDetailsServiceImpl(LoginMapper authMapper) {
        this.loginMapper = authMapper;
    }

    @Override
    public UserDetail loadUserByUsername(String name) throws UsernameNotFoundException {
        UserDetail userDetail = loginMapper.findByUsername(name);
        if (userDetail == null) {
            throw new UsernameNotFoundException(String.format("No userDetail found with username '%s'.", name));
        }
        SysRole role = loginMapper.findRoleByUserId(userDetail.getId());
        userDetail.setSysRole(role);
        return userDetail;
    }
}
