package com.tyiti.easycommerce.util.xml.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="CSubmitState", namespace="http://tempuri.org/")
@XmlAccessorType(XmlAccessType.FIELD)
public class CSubmitState extends XmlEntity {
	@XmlElement(name="State")
	public int State;
	@XmlElement(name="MsgID")
	public int MsgID;
	@XmlElement(name="MsgState")
	public String MsgState;
	@XmlElement(name="Reserve")
	public int Reserve;

	public int getState() {
		return State;
	}
	public void setState(int state) {
		State = state;
	}

	public int getMsgID() {
		return MsgID;
	}
	public void setMsgID(int msgID) {
		MsgID = msgID;
	}

	public String getMsgState() {
		return MsgState;
	}
	public void setMsgState(String msgState) {
		MsgState = msgState;
	}
	
	public int getReserve() {
		return Reserve;
	}
	public void setReserve(int reserve) {
		Reserve = reserve;
	}
}
