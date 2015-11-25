package com.sboss.knowledge.exception;

/**
 * 业务处理异常。这个异常负责在大多数场景中告知上层调用者处理过程中出现了问题。
 * 其中该异常中有一个ResponseCode枚举，描述了具体的处理问题
 * @author yinwenjie
 *
 */
public class BizException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -592173718294268531L;
	
	/**
	 * 异常响应编码 
	 */
	private ResponseCode responseCode;
	
	/**
	 * @param mess
	 * @param responseCode
	 */
	public BizException(String mess , ResponseCode responseCode) {
		super(mess);
		this.responseCode = responseCode;
	}

	public ResponseCode getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(ResponseCode responseCode) {
		this.responseCode = responseCode;
	}
}