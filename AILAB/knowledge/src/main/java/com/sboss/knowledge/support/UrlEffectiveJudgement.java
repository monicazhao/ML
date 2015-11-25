package com.sboss.knowledge.support;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlEffectiveJudgement {
	public boolean isEffective(URL url) {

		try {
			InputStream in = url.openStream();
			System.out.println("连接可用");
			return true;
		} catch (Exception e1) {
			System.out.println("连接打不开!");
			url = null;
			return false;
		}
	}

	public boolean isUrlEffective(String urlStr) {
		String httpProtocolStr = "http://";
		String httpsProtocolStr = "https://";
		Pattern pattern = Pattern.compile("^http://.*|^https://.*");
		Matcher protocalMatcher = pattern.matcher(urlStr);
		URL url = null;
		URL urlHttp = null;
		URL urlHttps = null;

		int counts = 0;
		String succ = null;
/*		if (url == null || url.toString().length() <= 0) {
			return false;
		}*/
		HttpURLConnection connection, connectionHttp, connectionHttps;
		
		int state = -1, stateHttp = -1, stateHttps = -1;
		//System.out.println(state + "\n" + stateHttp + "\n" + stateHttps);
		if (counts == 0) {
			try {
				if(protocalMatcher.matches()){
					url = new URL(urlStr);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
					state = connection.getResponseCode();
					//System.out.println(state);
					if(state == 200){
						return true;
					}
				}else{
					urlHttp = new URL(httpProtocolStr + urlStr);
					urlHttps = new URL(httpsProtocolStr + urlStr);
					//System.out.println(urlHttp.toString() + "\n" + urlHttps.toString());
					connectionHttp = (HttpURLConnection) urlHttp.openConnection();
					connectionHttp.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
					stateHttp = connectionHttp.getResponseCode();
					if(stateHttp == 200){
						return true;
					}
					
					connectionHttps = (HttpURLConnection) urlHttps.openConnection();
					
					connectionHttps.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
					stateHttps = connectionHttps.getResponseCode();
					if(stateHttps == 200){
						return true;
					}
				}			
/*				if (state == 200 || stateHttp == 200 || stateHttps == 200) {
					succ = connection.getURL().toString();
					System.out.println("succ-->" + succ);
					return true;
				}*/
				//break;
			} catch (Exception ex) {
				counts++;
				//continue;
			}
		}
		return false;
	}
	public static void main(String [] args) {
		UrlEffectiveJudgement jj = new UrlEffectiveJudgement();
		System.out.println("test");
		System.out.println(jj.isUrlEffective("http://blog.csdn.net/w20101114/article/details/8201474"));
		
	}
}
