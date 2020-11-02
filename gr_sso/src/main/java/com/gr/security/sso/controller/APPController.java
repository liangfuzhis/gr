package com.gr.security.sso.controller;

import com.alibaba.fastjson.JSONObject;
import com.gr.security.sso.common.ResultJson;
import com.gr.security.sso.entity.ResponseUserToken;
import com.gr.security.sso.entity.SysUser;
import com.gr.security.sso.utils.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * APP
 * 第三方授权登陆
 */
@RestController
@RequestMapping("app")
public class APPController {

    private static final String appId="wx1a12bb8e6f538e90";
    private static final String secret="5a7db0be511342a922c9dd7ede1acc9f";
    //access_Token
    private static  String access_Token ="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    //userinfo
    private static String userInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";


    /**
     * wxAppLogin
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/wxAppLogin")
    public Map<String,String> wxAppLogin(HttpServletRequest request){
        Map<String,String> map = new HashMap<String,String>();
        String code =request.getParameter("code");
        String reStart [] = new String[]{"APPID","SECRET","CODE"};
        String reEnd [] = new String[]{appId,secret,code};
        String   accessToken = StringUtils.replaceEach(access_Token,reStart,reEnd);
        if(StringUtils.isNotBlank(accessToken)){
            String tokenUrl = HttpClientUtils.doGet(accessToken);
            JSONObject obj = JSONObject.parseObject(tokenUrl);
            map.put("access_token",obj.get("access_token").toString());
            map.put("openid",obj.get("openid").toString());
            String reStartUser [] = new String[]{"ACCESS_TOKEN","OPENID"};
            String reEndUser [] = new String[]{obj.get("access_token").toString(),obj.get("openid").toString()};
            String   userInfos = StringUtils.replaceEach(userInfo,reStartUser,reEndUser);
            String userInfoUrl = HttpClientUtils.doGet(userInfos);
            JSONObject userInfosObj = JSONObject.parseObject(userInfoUrl);
            map.put("headimgurl",userInfosObj.get("headimgurl").toString());
            System.out.println(userInfosObj);
            //生成二维码地址
            String token = obj.get("access_token").toString();
            //map =getTicket(map,token);
        }
        return map;
    }

    public Map<String,String> getTicket(Map<String,String> map,String token){
        String tickTokenUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+token;
        Map<String,Object> ticketMap = new HashMap<String,Object>();
        JSONObject tkObj = new JSONObject();
        JSONObject tkObjs = new JSONObject();
        tkObjs.put("scene_str","test");
        tkObj.put("scene",tkObjs);
//        ticketMap.put("expire_seconds","604800");
        ticketMap.put("action_name","QR_STR_SCENE");
        ticketMap.put("action_info",tkObj);
        String  ticketStr = HttpClientUtils.doPost(tickTokenUrl,ticketMap);
        JSONObject ticketObj = JSONObject.parseObject(ticketStr);
        String tickets = ticketObj.get("ticket").toString();
        if(StringUtils.isNotBlank(tickets)){
            String ticketUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+tickets;
            JSONObject ticketsObj = JSONObject.parseObject(ticketUrl);
            map.put("ticket",ticketUrl);
        }
        return map;
    }

}
