package com.ai.sboss.common.interfaces;

import org.apache.camel.Processor;

public interface IBasicOutProcessor extends Processor {
	public String convert2requst(String data) throws Exception;
}
