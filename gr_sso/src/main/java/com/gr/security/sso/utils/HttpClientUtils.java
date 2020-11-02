package com.gr.security.sso.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * httpClient 工具
 * 
 * @author admin
 *
 */

public class HttpClientUtils {

	/**
	 * get请求
	 * 
	 * @return
	 */
	public static String doGet(String url) {
		// 初始化一个httpclient
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// 发送get请求
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse response = httpClient.execute(httpGet);
			/** 请求发送成功，并得到响应 **/
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				/** 读取服务器返回过来的json字符串数据 **/
				// 4.处理结果，这里将结果返回为字符串
				String result = null;
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					result = EntityUtils.toString(entity);
				}
				return result;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("调用get请求出错：" + url);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * post请求(用于key-value格式的参数)
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doPost(String url, Map<String, Object> params) {
		BufferedReader in = null;
		// 初始化一个httpclient
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// 实例化HTTP方法
			HttpPost request = new HttpPost();
			request.setURI(new URI(url));
			// 设置参数
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
				String name = (String) iterator.next();
				String value = String.valueOf(params.get(name));
				nvps.add(new BasicNameValuePair(name, value));
			}
			request.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			CloseableHttpResponse response = httpClient.execute(request);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) { // 请求成功
				in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
				StringBuffer sb = new StringBuffer("");
				String line = "";
				String separator = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + separator);
				}
				in.close();
				return sb.toString();
			} else {// 请求失败
				System.out.println("状态码：" + code);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * post请求（用于请求json格式的参数）
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doPost(String url, String params) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);// 创建httpPost
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-Type", "application/json");
		StringEntity entity = new StringEntity(params, "UTF-8");
		httpPost.setEntity(entity);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			StatusLine status = response.getStatusLine();
			int state = status.getStatusCode();
			if (state == HttpStatus.SC_OK) {
				HttpEntity responseEntity = response.getEntity();
				String jsonString = EntityUtils.toString(responseEntity);
				return jsonString;
			} else {
				System.out.println("请求返回:" + state + "(" + url + ")");
			}
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}



}
