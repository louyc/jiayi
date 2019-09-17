package com.lifelight.api.vo;

import java.io.Serializable;

public class SmsVO implements Serializable{

	private String phoneNum;
	
	private String telephoneMsg;

	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getTelephoneMsg() {
		return telephoneMsg;
	}

	public void setTelephoneMsg(String telephoneMsg) {
		this.telephoneMsg = telephoneMsg;
	}

	
}
