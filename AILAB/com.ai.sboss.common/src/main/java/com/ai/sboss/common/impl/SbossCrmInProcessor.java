package com.ai.sboss.common.impl;

import net.sf.json.JSONObject;

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;

import com.ai.sboss.common.interfaces.IBasicInProcessor;
import com.ai.sboss.common.interfaces.IJsonParser;

public class SbossCrmInProcessor implements IBasicInProcessor {
	
	protected Logger logger = Logger.getLogger(this.getClass());
	private static final String HUB_FORMAT_QUERY = "servicecode=<SERVICE_DODE>&WEB_HUB_PARAMS={\"data\":<PARAM_JSON>,\"header\":{\"Content-Type\":\"application/json\"}}";
	
	private String serviceCode = null;
	private IJsonParser jsonParser = null;
	
	SbossCrmInProcessor(String serviceCode, IJsonParser parser) {
		if (serviceCode != null) {
			this.serviceCode = serviceCode;
			this.jsonParser = parser;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		JSONObject properInput = getInputParam(exchange);
		String query = HUB_FORMAT_QUERY.replace("<SERVICE_DODE>", this.serviceCode).replace("<PARAM_JSON>", properInput.toString());
		exchange.getIn().setHeader(Exchange.HTTP_QUERY, query);
	}

	@Override
	public JSONObject getInputParam(Exchange exchange) {
		JSONObject ret = JSONObject.fromObject(exchange.getIn().getBody(String.class));
		if (jsonParser != null) {
			ret = this.jsonParser.parser(ret);
		}
		return ret;
	}

}
