package com.gr.security.sso.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author lfz
 * @since  2020.09.30
 */
@Data
@AllArgsConstructor
public class ResponseUserToken {
    private String token;
    private UserDetail userDetail;
}
