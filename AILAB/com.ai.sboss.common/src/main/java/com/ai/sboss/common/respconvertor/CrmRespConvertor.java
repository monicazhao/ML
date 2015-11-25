package com.ai.sboss.common.respconvertor;

import net.sf.json.JSONObject;

public abstract class CrmRespConvertor {
	/**
	 * Get the CRM result and convert them to response value.
	 * 
	 * @param value
	 * @return
	 */
	public final String convert(String value) {
		if (JSONObject.fromObject(value).containsKey("data"))
			return convert2Resp(JSONObject.fromObject(value).getString("data"));
		else
			return convert(value);
	}

	public abstract String convert2Resp(String array);
}
