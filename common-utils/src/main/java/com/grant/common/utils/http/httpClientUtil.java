package com.grant.common.utils.http;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class httpClientUtil {
	
	protected HttpClient client;
	
	public byte[] get(String url) throws HttpException, IOException{
		return get(url,"UTF-8");
	}
	
	public byte[] get(String url,String charset) throws HttpException, IOException{
		GetMethod method = new GetMethod(url);
		method.setRequestHeader("Content-Type","text/html;charset="+charset);
		int state = client.executeMethod(method);
		if(state!=200)return null;
		return method.getResponseBody();
	}

	public byte[] post(String url,Map<String,String> data) throws HttpException, IOException{
		return post(url,"utf-8",data);
	}
	
	public byte[] post(String url ,String charset,Map<String, String> data) throws HttpException, IOException{
		PostMethod method = new PostMethod(url);
		method.setRequestHeader("Content-Type","text/html;charset="+charset);
		if(data!=null){
			Set<String> set = data.keySet();
			NameValuePair[] nvps = new NameValuePair[set.size()];
			int i = 0;
			for (String string : set) {
				nvps[i] = new NameValuePair(string,data.get(string));
				i++;
			}
			method.setRequestBody(nvps);
		}
		if(client.executeMethod(method)!=200)return null;
		return method.getResponseBody();
	}
}
