package com.lifelight.api.vo;

import com.lifelight.api.entity.Message;

public class MessageVO extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7104170327363295538L;
	private String messageToName;
	private String countNum;
	
	public String getCountNum() {
		return countNum;
	}

	public void setCountNum(String countNum) {
		this.countNum = countNum;
	}

	public String getMessageToName() {
		return messageToName;
	}

	public void setMessageToName(String messageToName) {
		this.messageToName = messageToName;
	}
	
	
}
