package com.ai.sboss.common.impl;

import net.sf.json.JSONObject;

import org.apache.camel.Exchange;
import org.apache.camel.Message;

import com.ai.sboss.common.interfaces.IBasicInProcessor;
import com.ai.sboss.common.interfaces.IJsonParser;

public class SbossJettyInProcessor implements IBasicInProcessor {
	
	private IJsonParser jsonParser = null;
	SbossJettyInProcessor(IJsonParser parser) {
		this.jsonParser = parser;
	}
	
	SbossJettyInProcessor() {
		this.jsonParser = null;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		JSONObject properIn = getInputParam(exchange);
		exchange.getIn().setBody(properIn.toString());
	}

	@Override
	public final JSONObject getInputParam(Exchange exchange) {
		Message message = exchange.getIn();
		String params = "";
		//Get input paraments from different http request type
		if ("POST".equals(message.getHeader(Exchange.HTTP_METHOD))) {
			params = message.getBody(String.class);
		} else if ("GET".equals(message.getHeader(Exchange.HTTP_METHOD))) {
			params = message.getHeader(Exchange.HTTP_QUERY, String.class);
		}
		
		//peel useful paraments
		if (!JSONObject.fromObject(params).containsKey("data")) {
			throw new IllegalArgumentException();
		}
		JSONObject ret = JSONObject.fromObject(params).getJSONObject("data");
		if (jsonParser != null) {
			ret = this.jsonParser.parser(ret);
		}
		return ret;
	}


}
