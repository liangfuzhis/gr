package com.gr.security.sso.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * 用户
 */
@Data
@Builder
public class SysUser implements Serializable {

    private @Getter @Setter String userName;
    private @Getter @Setter String password;
}
