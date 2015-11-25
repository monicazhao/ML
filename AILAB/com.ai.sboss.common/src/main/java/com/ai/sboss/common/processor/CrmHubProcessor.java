package com.ai.sboss.common.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.log4j.Logger;

public abstract class CrmHubProcessor implements Processor {
	protected Logger logger = Logger.getLogger(this.getClass());
	private static final String HUB_FORMAT_QUERY = "servicecode=<SERVICE_DODE>&WEB_HUB_PARAMS={\"data\":<PARAM_JSON>,\"header\":{\"Content-Type\":\"application/json\"}}";

	public void process(Exchange exchange) throws Exception {
		String params = HUB_FORMAT_QUERY.replace("<SERVICE_DODE>", getServiceCode()).replace("<PARAM_JSON>", convert2Req(exchange));
		logger.info("CRM system query parameters:  " + params);
		exchange.getIn().setHeader(Exchange.HTTP_QUERY, envalidURI(params));
		// exchange.getIn().setHeader(Exchange.HTTP_PATH, "");
	}

	/**
	 * This method will be invoked by processor
	 * 
	 * @param value
	 * @param exchange
	 * @return
	 */
	private String convert2Req(Exchange exchange) {
		Message message = exchange.getIn();
		StringBuilder params = new StringBuilder("{");
		if ("POST".equals(message.getHeader(Exchange.HTTP_METHOD))) {
			params.append(convert2Req(message.getBody(String.class)));
		} else if ("GET".equals(message.getHeader(Exchange.HTTP_METHOD))) {
			params.append(convert2Req(message.getHeader(Exchange.HTTP_QUERY, String.class)));
		}
		params.append("}");
		logger.info("request parameter convert to: " + params);
		return params.toString();
	}

	private String envalidURI(String value) {
		value = value.replaceAll("\\[", "%5B").replaceAll("\\]", "%5D");
		return value;
	}

	/**
	 * Get the CRM DNA code from configure file via request parameter
	 * "servicecode" form request parameters.
	 * 
	 * @return
	 */
	public abstract String getServiceCode();

	/**
	 * Get the request parameters and convert them to CRM parameters.
	 * 
	 * @param value
	 * @return
	 */
	public abstract String convert2Req(String value);

}
