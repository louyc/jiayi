package com.lifelight.common.tools.util;

import java.util.UUID;



public class UUIDUtil {
	/**
	 * 返回去除连接符-的UUID
	 * 
	 * @return
	 */
	public static String uuid() {
		String uuid = uuid2();
		return uuid.replaceAll("-", "");
	}
	
	/**
	 * 返回原生UUID
	 * 
	 * @return
	 */
	public static String uuid2() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 返回原始UUID中指定序号的一组字符串。
	 * 
	 * @param index
	 *            有效索引序号[0,1,2,3,4]。
	 * @return
	 */
	public static String uuid(int index) {
		String[] uuids = uuid().split("-");
		return uuids[index];
	}

	public static void main(String[] args){
		System.out.println(uuid2());
		System.out.println(uuid());
	}
}
