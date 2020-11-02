package com.gr.security.sso.config;

import com.gr.security.sso.common.ClientProperties;
import com.gr.security.sso.common.ClientSystem;
import com.gr.security.sso.service.impl.CustomUserDetailsServiceImpl;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;


/**
 * 认证服务
 * @author lfz
 * @date 2020.10.11
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    public AuthorizationServerConfig(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Resource
    private ClientSystem clientSystem;
    //设置token有效期
    private static final int  accessToken_Seconds = 7 * 24 * 60 * 60;
    //设置refreshToken有效期
    private static final int  refreshToken_Seconds = 7 * 24 * 60 * 60;

    /**
     * 定义token的存储方式
     */
    @Bean
    public TokenStore tokenStore() {
        //使用redis存储token
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        //设置redis token存储中的前缀
        redisTokenStore.setPrefix("auth-token:");
        return redisTokenStore;
    }

    /**
     * 定义令牌端点上的安全性约束
     */
    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        // "/oauth/token_key"断点默认不允许访问
        oauthServer.tokenKeyAccess("permitAll()");
        // "/oauth/check_token"端点默认不允许访问
        oauthServer.checkTokenAccess("isAuthenticated()");
        // 允许通过form提交客户端认证信息(client_id,client_secret),默认为basic方式认证
        oauthServer.allowFormAuthenticationForClients();

    }


    /**
     * 定义客户端详细信息服务
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        if (ArrayUtils.isNotEmpty(clientSystem.getClients())) {
            for (ClientProperties config : clientSystem.getClients()) {
                builder
                        //设置客户端和密码
                        .withClient(config.getClientId()).secret(config.getClientSecret())
                        .accessTokenValiditySeconds(accessToken_Seconds)
                        .refreshTokenValiditySeconds(refreshToken_Seconds)
                        //认证方式(authorization_code)
                        .authorizedGrantTypes("refresh_token", "authorization_code", "password").autoApprove(false)
                        //授权域(自定义授权页面[all无效果])
                        .scopes("app", "write")
                        .redirectUris("http://192.168.31.215:9015/auth/oauth/confirm_access"); //回调地址
            }
        }

    }

    /**
     * 定义授权和令牌以及令牌服务
     */
    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //替换默认页面
                .pathMapping("/oauth/confirm_access","/auth/custom/confirm_access")
                //指定认证管理器
                .authenticationManager(authenticationManager)
                //用户账号密码认证
                .userDetailsService(userDetailsService)
                .reuseRefreshTokens(false)
                //.tokenServices(tokenService())
                .tokenStore(tokenStore());
    }

    @Bean
    public DefaultTokenServices tokenService() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        //配置token存储
        tokenServices.setTokenStore(tokenStore());
        //开启支持refresh_token，此处如果之前没有配置，启动服务后再配置重启服务，可能会导致不返回token的问题，解决方式：清除redis对应token存储
        tokenServices.setSupportRefreshToken(true);
        //复用refresh_token
        tokenServices.setReuseRefreshToken(true);
        //token有效期，设置12小时
        tokenServices.setAccessTokenValiditySeconds(accessToken_Seconds);
        //refresh_token有效期，设置一周
        tokenServices.setRefreshTokenValiditySeconds(refreshToken_Seconds);
        return tokenServices;
    }

}
