package com.gr.security.sso.service.impl;

import com.gr.security.sso.common.ResultJson;
import com.gr.security.sso.entity.ResponseUserToken;
import com.gr.security.sso.entity.SysRole;
import com.gr.security.sso.entity.UserDetail;
import com.gr.security.sso.enums.ResultCodeEnums;
import com.gr.security.sso.exception.CustomException;
import com.gr.security.sso.mapper.LoginMapper;
import com.gr.security.sso.service.LoginService;
import com.gr.security.sso.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtTokenUtil;
    private final LoginMapper loginMapper;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    public LoginServiceImpl(AuthenticationManager authenticationManager,
                            @Qualifier("customUserDetailsService") UserDetailsService userDetailsService,
                            JwtUtils jwtTokenUtil,
                            LoginMapper loginMapper) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.loginMapper = loginMapper;
    }

    @Override
    public UserDetail register(UserDetail userDetail) {
        final String username = userDetail.getUsername();
        UserDetail uerDetail = loginMapper.findByUsername(username);
        System.out.println(uerDetail);
        if(loginMapper.findByUsername(username)!=null) {
            throw new CustomException(ResultJson.failure(ResultCodeEnums.BAD_REQUEST, "用户已存在"));
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = userDetail.getPassword();
        userDetail.setPassword(encoder.encode(rawPassword));
        userDetail.setLastPasswordResetDate(new Date());
        loginMapper.insert(userDetail);
        long roleId = userDetail.getSysRole().getId();
        SysRole role = loginMapper.findRoleById(roleId);
        userDetail.setSysRole(role);
        loginMapper.insertRole(userDetail.getId(), roleId);
        return userDetail;
    }

    @Override
    public ResponseUserToken login(String username, String password) {
        //用户验证
        final Authentication authentication = authenticate(username, password);
        //存储认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //生成token
        final UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        final String token = jwtTokenUtil.generateAccessToken(userDetail);
        //存储token-Redis
        jwtTokenUtil.putToken(username, token);
        return new ResponseUserToken(token, userDetail);
    }

    @Override
    public void logout(String token) {
        token = token.substring(tokenHead.length());
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        jwtTokenUtil.deleteToken(userName);
    }

    @Override
    public ResponseUserToken refresh(String oldToken) {
        String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserDetail userDetail = (UserDetail) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, userDetail.getLastPasswordResetDate())){
            token =  jwtTokenUtil.refreshToken(token);
            return new ResponseUserToken(token, userDetail);
        }
        return null;
    }

    @Override
    public UserDetail getUserByToken(String token) {
        token = token.substring(tokenHead.length());
        return jwtTokenUtil.getUserFromToken(token);
    }

    private Authentication authenticate(String username, String password) {
        try {
            //该方法会去调用userDetailsService.loadUserByUsername()去验证用户名和密码，如果正确，则存储该用户名密码到“security 的 context中”
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException | BadCredentialsException e) {
            throw new CustomException(ResultJson.failure(ResultCodeEnums.LOGIN_ERROR, e.getMessage()));
        }
    }
}
