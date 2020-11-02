package com.gr.security.sso.service;

import com.gr.security.sso.entity.ResponseUserToken;
import com.gr.security.sso.entity.UserDetail;

/**
 * 登陆服务
 * @anthor lfz
 * @since  2020.09.30
 */
public interface LoginService {

    /**
     * 注册用户
     * @param userDetail
     * @return
     */
    UserDetail register(UserDetail userDetail);

    /**
     * 登陆
     * @param username
     * @param password
     * @return
     */
    ResponseUserToken login(String username, String password);

    /**
     * 登出
     * @param token
     */
    void logout(String token);

    /**
     * 刷新Token
     * @param oldToken
     * @return
     */
    ResponseUserToken refresh(String oldToken);

    /**
     * 根据Token获取用户信息
     * @param token
     * @return
     */
    UserDetail getUserByToken(String token);
}
