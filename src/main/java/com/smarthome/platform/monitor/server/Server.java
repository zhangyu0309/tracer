package com.smarthome.platform.monitor.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.smarthome.core.util.ByteUtil;
import com.smarthome.platform.monitor.bean.GpsInfo;
import com.smarthome.platform.monitor.common.Constant;
import com.smarthome.platform.monitor.dao.HostJDBCDao;
import com.sun.org.apache.bcel.internal.generic.RETURN;

public class Server implements Runnable {
	private static Logger logger = Logger.getLogger(Server.class.getName());
	private static String id = "";
	private final Socket _s;
	private boolean started = false;//定时获取GPS数据的任务是否开始
	Timer timer = new Timer();
	byte[] reqDate = null;
	long lastReceiveTime;// 上次收到数据包的时间
	InputStream in = null;
	OutputStream os = null;
	public Server(Socket s) {
		_s = s;
		lastReceiveTime = System.currentTimeMillis();
	}

	public void run() {
		try {
			in = _s.getInputStream();
			os = _s.getOutputStream();
			while (true) {
				if (System.currentTimeMillis() - lastReceiveTime > Constant.SOCKET_WAIT_TIME) {
					logger.info("超时断开:" + (id == null ? "" : id));
					if (id != null && !id.equals("")) {
						HostJDBCDao.offline(id);
					}
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (in.available() > 0) {
					lastReceiveTime = System.currentTimeMillis();
					// 处理客户端发来的数据
					byte[] bytes = new byte[in.available()];
					in.read(bytes);
					logger.info("receice:" + new String(bytes).trim());
					logger.info("receice length:" + bytes.length);
					if (bytes.length < 88) {
						logger.info("报文长度异常,丢弃");
						throw new Exception("无效数据丢弃");
					}
					bytes = HexToByte(new String(bytes).trim());
					if (bytes[0] != 0x41 || bytes[1] != 0x41){
						logger.info("无效数据丢弃");
						throw new Exception("无效数据丢弃");
					}
					logger.info("receice-1:" + new String(bytes).trim());
					for (byte b : bytes) {
						System.out.print(ByteUtil.toHex(b));
						System.out.print("|");
					}
					System.out.println();
					byte[] response = MessageHandle(bytes);
					if (response != null) {
						String reString = bytesToHexString(response);
						int trytime = 0;
						do {// 为防止发送失败，最多重试6次
							try {
								trytime++;
								os.write(reString.getBytes());
								logger.info("response:" + reString);
								break;
							} catch (Exception e) {
								logger.error(e.getMessage(), e);
								e.printStackTrace();
								Thread.sleep(1000);
							}
						} while (trytime < 6);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} finally {
			try {
				if (!_s.isClosed()) {
					_s.close();
				}
				if (timer != null){
					timer.cancel();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 对socket收到的报文进行处理
	 * 
	 * @param msg
	 *            收到报文
	 * @return 响应报文
	 */
	private byte[] MessageHandle(byte[] msg) {
		logger.info("HANDLE MESSAGE:" + new String(msg));
		if (msg.length < 88) {
			logger.info("报文长度异常");
			return null;
		}
		if (id == null || id.equals("")) {
			// 为获取ID，通常为第一次通信
			byte[] idbyte = new byte[Constant.ID_LENGTH];
			System.arraycopy(msg, Constant.START1_LENGTH
					+ Constant.START2_LENGTH + Constant.CMD_LENGTH
					+ Constant.DATALENGTH_LENGTH
					+ Constant.EXTENDDATALENGTH_LENGTH + Constant.DATA_LENGTH,
					idbyte, 0, Constant.ID_LENGTH);
			StringBuilder idsb = new StringBuilder();
			for (byte b : idbyte) {
				idsb.append(ByteUtil.toHex(b));
			}
			id = idsb.toString();
		}

		byte cmd = msg[Constant.START1_LENGTH + Constant.START2_LENGTH];
		int dataLength = 64;// msg[Constant.START1_LENGTH +
							// Constant.START2_LENGTH +
							// Constant.CMD_LENGTH];//数据长度
		byte[] extentDataLengthBytes = new byte[Constant.EXTENDDATALENGTH_LENGTH];
		System.arraycopy(msg, Constant.START1_LENGTH + Constant.START2_LENGTH
				+ Constant.CMD_LENGTH + Constant.DATALENGTH_LENGTH,
				extentDataLengthBytes, 0, Constant.EXTENDDATALENGTH_LENGTH);
		int extentDataLength = msg.length - 88;// bytes2int(extentDataLengthBytes);//扩展数据长度
		logger.info("命令字：" + Byte.toString(cmd) + "数据长度：" + dataLength
				+ ",扩展长度：" + extentDataLength);
		switch (cmd) {
		case Constant.CMD_HAND_SHAKE:
			msg[2] = Constant.CMD_REQ_DATA;
			if (!started){
				reqDate = msg;
				TimerTask task = new TimerTask() {  
			        @Override  
			        public void run() {
			        	if (reqDate != null && os != null){
			        		String reString = bytesToHexString(reqDate);
							int trytime = 0;
							do {// 为防止发送失败，最多重试6次
								try {
									trytime++;
									os.write(reString.getBytes());
									logger.info("请求GPS数据:" + reString);
									break;
								} catch (Exception e) {
									logger.error(e.getMessage(), e);
									e.printStackTrace();
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e1) {}
								}
							} while (trytime < 6);
			        	}
			        }  
			    };  
			    timer.scheduleAtFixedRate(task, 0, 60000);
			    started = true;
			}
			//return msg;
			return null;
		case Constant.CMD_HAND_SHAKE_ACK:

			break;
		case Constant.CMD_SYNC_TIME:

			break;
		case Constant.CMD_SYNC_TIME_ACK:

			break;
		case Constant.CMD_HEART_BEAT:
			HostJDBCDao.heartbeat(id);
			break;
		case Constant.CMD_HEART_BEAT_ACK:

			break;
		case Constant.CMD_REQ_DATA:

			break;
		case Constant.CMD_REQ_DATA_ACK:
			// System.arraycopy(msg, Constant.START1_LENGTH
			// + Constant.START2_LENGTH + Constant.CMD_LENGTH
			// + Constant.DATALENGTH_LENGTH
			// + Constant.EXTENDDATALENGTH_LENGTH , gpsbytes, 0, dataLength);
			if (extentDataLength > 0) {
				byte[] gpsbytes = new byte[extentDataLength];
				System.arraycopy(msg, 88, gpsbytes, 0, extentDataLength);
				String gps = new String(gpsbytes);
				logger.info("完整gps信息：" + gps);

				GpsInfo gi = new GpsInfo();
				gi.setDeviceid(id);
				boolean getGprmc = false;// 是否已解析GPS_GPRMC信息
				boolean getGpgga = false;// 是否已解析GPS_GPGGA信息
				for (String tmpLine : gps.split("\n")) {
					if (tmpLine.startsWith(Constant.GPS_GPRMC)) {
						String[] gprmcInfo = tmpLine.split(",");
						gi.setTime(gprmcInfo[1]);
						gi.setState(gprmcInfo[2]);
						gi.setLatitude(gprmcInfo[3]);
						gi.setNs(gprmcInfo[4]);
						gi.setLongitude(gprmcInfo[5]);
						gi.setEw(gprmcInfo[6]);
						gi.setSpeed(gprmcInfo[7]);
						gi.setDirection(gprmcInfo[8]);
						gi.setDate(gprmcInfo[9]);
						gi.setAngle(gprmcInfo[10]);
						gi.setAngledirection(gprmcInfo[11]);
						getGprmc = true;
					} else if (tmpLine.startsWith(Constant.GPS_GPGGA)) {
						String[] gpggaInfo = tmpLine.split(",");
						gi.setSatellites(gpggaInfo[7]);
						gi.setElevation(gpggaInfo[9]);
						getGpgga = true;
					}
					if (getGpgga && getGprmc) {
						break;
					}
				}
				if (gi.getState().equalsIgnoreCase("V")) {
					logger.info("定位数据无效，设备ID：" + id);
					break;
				}
				SimpleDateFormat targetFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				gi.setTime("20" + gi.getDate().substring(4) + "-"
						+ gi.getDate().substring(2, 4) + "-"
						+ gi.getDate().substring(0, 2) + " "
						+ gi.getTime().substring(0, 2) + ":"
						+ gi.getTime().substring(2, 4) + ":"
						+ gi.getTime().substring(4, 6));
				Date date = null;
				try {
					date = targetFormat.parse(gi.getTime());
					//北京时间加8小时
					date = new Date(date.getTime() + 8 * 3600 *1000);
					gi.setTime(targetFormat.format(date));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				HostJDBCDao.savePosition(gi);
			} else {
				logger.info("GPS数据为空");
			}

			break;
		case Constant.CMD_SET_DEV:

			break;
		case Constant.CMD_SET_DEV_ACK:

			break;
		case Constant.CMD_ACK:

			break;
		case Constant.CMD_END:

			break;
		default:
			break;
		}
		return null;
	}

	/**
	 * 把16进制字符串 转换成字节数组 只转前88字节
	 * 
	 * @param bArray
	 * @return
	 */
	private static byte[] HexToByte(String hexString)// 字串符转换成16进制byte[]
	{
		int len = (hexString.length() - 88);// 前88字节转换
		byte[] result = new byte[len];
		char[] achar = hexString.substring(0, 176).toCharArray();
		System.out.println(achar.length);
		for (int i = 0; i < 88; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		if (len > 88) {
			System.arraycopy(hexString.substring(88 * 2).getBytes(), 0, result,
					88, len - 88);
		}

		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/**
	 * 把字节数组转换成16进制字符串
	 * 
	 * @param bArray
	 * @return
	 */
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}

		return sb.toString();
	}
}
