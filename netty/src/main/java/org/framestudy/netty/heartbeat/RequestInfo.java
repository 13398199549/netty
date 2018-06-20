package org.framestudy.netty.heartbeat;

import java.io.Serializable;

public class RequestInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String heartInfo ;
	//.. other field
	public String getHeartInfo() {
		return heartInfo;
	}
	public void setHeartInfo(String heartInfo) {
		this.heartInfo = heartInfo;
	}
	@Override
	public String toString() {
		return "RequestInfo [heartInfo=" + heartInfo + "]";
	}
}
