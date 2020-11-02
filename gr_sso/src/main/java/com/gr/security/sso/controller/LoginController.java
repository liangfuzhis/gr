package com.gr.security.sso.controller;

import com.alibaba.fastjson.JSONObject;
import com.gr.security.sso.common.CommProperties;
import com.gr.security.sso.common.ResultJson;
import com.gr.security.sso.entity.ResponseUserToken;
import com.gr.security.sso.entity.SysRole;
import com.gr.security.sso.entity.SysUser;
import com.gr.security.sso.entity.UserDetail;
import com.gr.security.sso.enums.ResultCodeEnums;
import com.gr.security.sso.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Test
 */
@Controller
@RequestMapping("auth")
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private CommProperties commProperties;

    @Autowired
    private LoginService loginService;

    @RequestMapping (value = "/loadLogin")
    public ModelAndView loadLogin(){
        System.out.println(commProperties.getClientId());
        ModelAndView model = new ModelAndView();
        model.setViewName("login");
        return model;
    }

    @RequestMapping (value = "/index")
    public ModelAndView index(){
        System.out.println(commProperties.getClientId());
        ModelAndView model = new ModelAndView();
        model.setViewName("index");
        return model;
    }

    @RequestMapping (value = "/getAuthTest")
    public ResultJson getIndex(HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader(tokenHeader);
        if (token == null) {
            return ResultJson.failure(ResultCodeEnums.UNAUTHORIZED);
        }
        JSONObject obj = new JSONObject();
        response.setHeader("Access-Control-Expose-Headers","Origin,X-Requested-With,Content-Type,Accept,Authorization,token");
        response.setHeader("Authorization",token);
        obj.put("url","http://192.168.31.215:9015/oauth/authorize?response_type=code&client_id=lfz&redirect_uri=http://baidu.com&state=test&scope=app");
        obj.put("token",token);
        return ResultJson.ok(obj);
    }

    /**
     * 第三方授权页面(替换默认的页面)
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/oauth/confirm_access")
    public ModelAndView confirmAccess(HttpServletRequest request) throws Exception {
        log.info("测试自定义授权页面");
        ModelAndView view = new ModelAndView();
        view.setViewName("confirm_access");
        view.addObject("clientId", "255sfsw");
        //authorization
        view.addObject("token", "token234");
        return view;
    }

    /**
     * 登陆成功返回token,登陆之前请先注册账号
     * @param user
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/login")
    public ResultJson<ResponseUserToken> login(HttpServletRequest requests,HttpServletResponse responses, @RequestBody SysUser user){
        responses.setHeader("Access-Control-Expose-Headers","Origin,X-Requested-With,Content-Type,Accept,Authorization,token");
        final ResponseUserToken response = loginService.login(user.getUserName(), user.getPassword());
        log.info(user.getUserName());
        responses.setHeader("Authorization",response.getToken());
        //后期扩展-token存入redis
        return ResultJson.ok(response);
    }

    /**
     * 退出登陆
     * @param request
     * @return
     */
    @GetMapping(value = "/logout")
    public ResultJson logout(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        if (token == null) {
            return ResultJson.failure(ResultCodeEnums.UNAUTHORIZED);
        }
        loginService.logout(token);
        return ResultJson.ok();
    }

    /**
     * 根据token获取用户信息
     * @param request
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/user")
    public ResultJson getUser(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        if (token == null) {
            return ResultJson.failure(ResultCodeEnums.UNAUTHORIZED);
        }
        UserDetail userDetail = loginService.getUserByToken(token);
        return ResultJson.ok(userDetail);
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping(value = "/sign")
    public ResultJson sign(@RequestBody SysUser user) {
        if (StringUtils.isAnyBlank(user.getUserName(), user.getPassword())) {
            return ResultJson.failure(ResultCodeEnums.BAD_REQUEST);
        }
        UserDetail userDetail = new UserDetail(user.getUserName(), user.getPassword(), SysRole.builder().id(1l).build());
        return ResultJson.ok(loginService.register(userDetail));
    }


    /**
     * 刷新token
     * @param request
     * @return
     */
    @GetMapping(value = "refresh")
    public ResultJson refreshAndGetAuthenticationToken(
            HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        ResponseUserToken response = loginService.refresh(token);
        if(response == null) {
            return ResultJson.failure(ResultCodeEnums.BAD_REQUEST, "token无效");
        } else {
            return ResultJson.ok(response);
        }
    }
}
