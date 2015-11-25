package com.sboss.knowledge.camel;

import net.sf.json.JSONObject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public interface IBasicInProcessor extends Processor {
	
	public JSONObject getInputParam(Exchange exchange) throws Exception;
}
