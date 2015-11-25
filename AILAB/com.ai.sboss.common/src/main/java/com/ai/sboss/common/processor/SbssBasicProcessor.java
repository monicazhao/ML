package com.ai.sboss.common.processor;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.camel.Exchange;
import org.apache.camel.Message;

public abstract class SbssBasicProcessor {
	
	private int crmCode;
	
	public int getCrmCode() {
		return crmCode;
	}

	public void setCrmCode(int crmCode) {
		this.crmCode = crmCode;
	}

	private final static String RETFMT = "{\"data\":<DATA>,\"desc\":{\"result_code\":\"<RET_CODE>\",\"result_msg\":\"<RET_MSG>\",\"data_mode\":\"<DATA_MODE>\",\"digest\":\"<DIGEST>\"}}";
	
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
/*		if (!JSONObject.fromObject(params).containsKey("data")) {
			throw new IllegalArgumentException();
		}
		JSONObject ret = JSONObject.fromObject(params).getJSONObject("data");*/
		JSONObject ret = JSONObject.fromObject(params);
		return ret;
	}
	
	public final String convert2requst(String data) {
		return RETFMT.replace("<DATA>", data).replace("<RET_CODE>", "0")
				.replace("<RET_MSG>", "").replace("<DATA_MODE>", "0")
				.replace("<DIGEST>", "");
	}
	
	public final String getCrmResult(Exchange exchange) {
		JSONObject crmRet = JSONObject.fromObject(exchange.getIn().getBody());
		setCrmCode(Integer.parseInt(crmRet.getString("hub_code")));
		JSONObject ret = null;
		try {
			ret = crmRet.getJSONObject("data");
		} catch (JSONException e) {
			setCrmCode(0);
			return "";
		}
		return ret.toString();
	}
	
}
