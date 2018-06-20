package org.framestudy.netty.serial;

import java.io.Serializable;
import java.util.Arrays;

public class Req implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String id ;
	private String name ;
	private String requestMessage ;//请求消息
	private String fileName;
	private byte[] attachment;//携带的附件，例如：文件……
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getRequestMessage() {
		return requestMessage;
	}
	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}
	public byte[] getAttachment() {
		return attachment;
	}
	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}
	@Override
	public String toString() {
		return "Req [id=" + id + ", name=" + name + ", requestMessage=" + requestMessage + ", attachment="
				+ Arrays.toString(attachment) + "]";
	}
}
