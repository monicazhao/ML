package com.sboss.knowledge.camel;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.stereotype.Component;

@Component("generalJettyInProcessor")
public class GeneralJettyInProcessor implements IBasicInProcessor {

	@Override
	public void process(Exchange exchange) throws Exception {
		JSONObject properIn = getInputParam(exchange);
		exchange.getIn().setBody(properIn.toString());
		exchange.getIn().setHeader(Exchange.HTTP_METHOD, "POST");
	}

	// 通用Jetty输入参数解析函数，负责将Get/Post方法传入的参数放入Message的Body中
	@Override
	public JSONObject getInputParam(Exchange exchange) {
		Message message = exchange.getIn();
		String params = "";
		// 如果条件成立，说明是客户端使用的是POST方式的HTTP请求，应该从body中取出参数
		if ("POST".equals(message.getHeader(Exchange.HTTP_METHOD))) {
			params = message.getBody(String.class);
		}
		// 如果条件成立，说明客户端使用的GET方式的HTTP请求，应该从head取出参数
		else if ("GET".equals(message.getHeader(Exchange.HTTP_METHOD))) {
			params = message.getHeader(Exchange.HTTP_QUERY, String.class);
		}
		// 其他情况抛出异常
		else {
			throw new IllegalArgumentException("没有发现兼容的HTTP请求类型");
		}

		//判断参数是否合法
		JSONObject inputJsonObject = null;
		try {
			inputJsonObject = JSONObject.fromObject(params);
		} catch (JSONException e) {
			throw new IllegalArgumentException("Json格式不合法"+e.toString());
		}
		//if (!inputJsonObject.containsKey("data") || !inputJsonObject.containsKey("desc")) {
		if (!inputJsonObject.containsKey("data")) {
			throw new IllegalArgumentException("输入Json格式不规范");
		}
		
		JSONObject ret = inputJsonObject.getJSONObject("data");
		return ret;
	}

}
