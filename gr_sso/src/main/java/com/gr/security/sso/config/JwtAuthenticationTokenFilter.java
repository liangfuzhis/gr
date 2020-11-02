package com.gr.security.sso.config;

import com.gr.security.sso.entity.UserDetail;
import com.gr.security.sso.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token校验
 * @author: lfz
 * @since  2020.09.30
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.header}")
    private String token_header;

    @Resource
    private JwtUtils jwtUtils;

    /**
     * 后期优化从Redis中提取token的有效期
     * 第三方授权
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        //从redis中过滤
        String auth_token = request.getHeader(this.token_header);
        final String auth_token_start = "Bearer ";
        if (StringUtils.isNotEmpty(auth_token) && auth_token.startsWith(auth_token_start)) {
            auth_token = auth_token.substring(auth_token_start.length());
            String username = jwtUtils.getUsernameFromToken(auth_token);
            logger.info(String.format("Checking authentication for userDetail %s.", username));
//            if (jwtUtils.containToken(username, auth_token) && username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (StringUtils.isNotBlank(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetail userDetail = jwtUtils.getUserFromToken(auth_token);
                if (jwtUtils.validateToken(auth_token, userDetail)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    logger.info(String.format("Authenticated userDetail %s, setting security context", username));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } else {
            setCorsResponse(request, response);
            //由于使用前后分离的方式，发现每次请求之前都会发送一个OPTIONS来测试服务器是否支持跨域
            // security jwt拦截，所以这里发现这个请求之后直接返回
            if (request.getMethod().equals("OPTIONS")) {
                response.setStatus(200);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    public void setCorsResponse(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept,authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}
