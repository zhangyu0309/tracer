package com.smarthome.platform.monitor.common;

import org.apache.log4j.Logger;

public class Constant {
	public static Logger logger = Logger.getLogger(Constant.class);
	/**
	 * 服务启动时 是否允许socket监听
	 */
	public static final boolean IS_RUN_SOCKET = true;
	/**
	 * 监听端口uuuuuuu
	 */
	public static final int LISTEN_PORT = 5000;
	
	/**
	 * socket未收到心跳，超时关闭的时间，毫秒
	 */
	public static final int SOCKET_WAIT_TIME = 180000;
	
	//命令字
	public static final byte CMD_HAND_SHAKE = 0x50;
	
	public static final byte CMD_HAND_SHAKE_ACK = 0x51;
	
	public static final byte CMD_SYNC_TIME = 0x52;
	
	public static final byte CMD_SYNC_TIME_ACK = 0x53;
	
	public static final byte CMD_HEART_BEAT = 0x54;
	
	public static final byte CMD_HEART_BEAT_ACK = 0x55;
	
	public static final byte CMD_REQ_DATA = 0x56;
	
	public static final byte CMD_REQ_DATA_ACK = 0x57;
	
	public static final byte CMD_SET_DEV = 0x58;
	
	public static final byte CMD_SET_DEV_ACK = 0x59;
	
	public static final byte CMD_ACK = 0x60;
	
	public static final byte CMD_END = 0x61;
			
	//数据包各部分长度
	public static final int START1_LENGTH = 1;
	
	public static final int START2_LENGTH = 1;
	
	public static final int CMD_LENGTH = 1;
	
	public static final int DATALENGTH_LENGTH = 1;
	
	public static final int EXTENDDATALENGTH_LENGTH = 4;
	
	public static final int DATA_LENGTH = 64;
	
	public static final int ID_LENGTH = 8;
	
	public static final int RESERVE1_LENGTH = 1;
	
	public static final int CHECKSUM_LENGTH = 1;
	
	public static final int CRC_LENGTH = 2;
	
	public static final int RESERVE2_LENGTH = 4;
	
	/**
	 * GPS信息，可解析出经纬度，速度等信息
	 */
	public static final String GPS_GPRMC = "$GPRMC";
	
	/**
	 * GPS信息，可解析出海拔高度信息
	 */
	public static final String GPS_GPGGA = "$GPGGA";
	
	
}
