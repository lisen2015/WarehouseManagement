/*														
 * FileName：SmsPostClient.java						
 *			
 * Description：手机短信、email发送工具客户端
 * 																	
 * History：
 * 版本号 			作者 			日期       			简介
 *  1.0   			chenchen		2015年11月25日		Create	
 */
package com.job.manager.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 手机短信、email发送客户端类
* @ClassName: SmsPostClient 
* @Description: 用于发送手机短信
* @author chenchen
* @date 2015年11月25日 上午11:40:08 
*
 */
public class SmsPostClient {
	private static Logger log=Logger.getLogger(SmsPostClient.class);
	public static ResourceBundle RESOURCE=ResourceBundle.getBundle("config.phone_message");
	//缓存写入错误
	public static final String CACHE_WRITE_ERROR = RESOURCE.getString("cache_write_error");
	//缓存清理错误
	public static final String CACHE_CLEAN_ERROR = RESOURCE.getString("cache_clean_error");
	//未知错误
	public static final String UNKNOWN_ERROR = RESOURCE.getString("unknown_error");
	//数据库执行错误
	public static final String DB_ERROR = RESOURCE.getString("db_error");
	/**
	 * 根据类型发送信息
	 * @param content
	 * @version
	 * 	2015年11月25日		chenchen	create
	 */
	public static  void sendSms(final String content){
		/*
		 * 如果设置不开启信息发送,则直接返回
		 */
		if (!"true".equals(RESOURCE.getString("message_switch"))) {
			return;
		}
		if (!"sendMsg".equals(RESOURCE.getString("message_type"))) {
			return;
		}
		/*
		 * 启动新的线程进行发送
		 */
		new Thread(new Runnable() {
			@Override
			public void run() {
				CloseableHttpClient httpclient=null;
				CloseableHttpResponse response=null;
				try {
					httpclient = HttpClients.createDefault();
					// 构造一个post对象
					HttpPost httpPost = new HttpPost(RESOURCE.getString("message_url"));
//                  HttpPost httpPost = new HttpPost("http://10.10.11.60/msg/newmessage.do");
                    // 添加所需要的post内容
					List<NameValuePair> nvps = new ArrayList<NameValuePair>();
					//设置用户名
					nvps.add(new BasicNameValuePair("userName", RESOURCE.getString("message_user")));
//                  nvps.add(new BasicNameValuePair("userName", "app2"));
					//设置密码
					nvps.add(new BasicNameValuePair("userPassword",RESOURCE.getString("message_pwd")));
//                  nvps.add(new BasicNameValuePair("userPassword", "app1"));
					//设置发送手机号码
					nvps.add(new BasicNameValuePair("phoneNums",RESOURCE.getString("message_phoneNums")));
//                  nvps.add(new BasicNameValuePair("phoneNums","13681907660"));
					//设置内容
                    String localIP="服务器IP:"+InetAddress.getLocalHost()+"|";
					nvps.add(new BasicNameValuePair("content",RESOURCE.getString("message_content")+localIP+content));
//                  nvps.add(new BasicNameValuePair("content", "ssss"));
					/*
					 * 设置产品ID
					 */
					if (StringHelper.isNotEmpty(RESOURCE.getString("message_productId"))) {
						nvps.add(new BasicNameValuePair("productId",RESOURCE.getString("message_productId")));
//                      nvps.add(new BasicNameValuePair("productId", "12"));
					}						
					/*
					 * 设置渠道编号
					 */
					if (StringHelper.isNotEmpty(RESOURCE.getString("message_channelId"))) {
						nvps.add(new BasicNameValuePair("channelId",RESOURCE.getString("message_channelId")));
//                      nvps.add(new BasicNameValuePair("channelId", "77"));
					}	
					//设置发送类型为短信
					nvps.add(new BasicNameValuePair("type",RESOURCE.getString("message_type")));
//                  nvps.add(new BasicNameValuePair("type", "sendMsg"));
                    //设置发送时间
                    nvps.add(new BasicNameValuePair("sendTime",""));
//                    nvps.add(new BasicNameValuePair("sendTime", "2014-12-12 12:11:12"));

                    nvps.add(new BasicNameValuePair("needReport", "1"));
                    //设置
					httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
					response = httpclient.execute(httpPost);
					HttpEntity entity = response.getEntity();
					log.debug("发送短信成功，返回内容："+EntityUtils.toString(entity,"utf-8"));
				} catch (Exception e) {
					log.error("发送短信失败！！！", e);
				}finally{
					try {
						if (response!=null) {
							response.close();
						}
						if (httpclient!=null) {
							httpclient.close();
						}
					} catch (IOException e) {
						log.error("关闭http连接出错",e);
					}
				}
				
			}
		}).run();
		
	}
}
