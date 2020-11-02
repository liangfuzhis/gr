package com.gr.security.sso.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 角色
 */
@Data
@Builder
public class SysRole implements Serializable {

    @Id
    @GeneratedValue
    private @Getter @Setter Long id;
    private @Getter @Setter  String name;
}
