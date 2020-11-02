package com.gr.security.sso.entity;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * UserDetails SSO重写
 * @author : lfz
 * createAt: 2018/9/14
 */
public class UserDetail implements UserDetails {

    private @Getter @Setter long id;
    private @Getter @Setter String username;
    private @Getter @Setter String password;
    private @Getter @Setter SysUser sysUser;
    private @Getter @Setter SysRole sysRole;
    private @Getter @Setter Date lastPasswordResetDate;

    public UserDetail(long id, String username, SysUser role, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.sysUser = role;
    }

    public UserDetail(String username, String password, SysUser role) {
        this.username = username;
        this.password = password;
        this.sysUser = role;
    }

    public UserDetail(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public UserDetail(String name, String password, SysRole sysRole) {
        this.username = name;
        this.password = password;
        this.sysRole = sysRole;
    }

    public UserDetail(long userId, String username, SysRole role, String password) {
        this.id = userId;
        this.username = username;
        this.password = password;
        this.sysRole=role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(sysRole.getName()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true ;
    }
}
