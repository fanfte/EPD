package com.fanfte.datatransfer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.fanfte.global.GlobalConstants;
import com.fanfte.model.StartResutData;
import com.fanfte.tutils.ConnectUtils;
import com.fanfte.tutils.HospitalUtils;
import com.fanfte.tutils.HttpUtils;
import com.fanfte.tutils.JsonUtil;
import com.fanfte.tutils.JsonUtils;
import com.fanfte.tutils.SystemUtils;
import com.fanfte.tutils.WriteCSV;

public class DataHandlerClient extends Thread{
	
	private DataHandler dh;
	private String from;
	private Integer DATA_LENGTH =GlobalConstants.DATA_LENGTH ;
	public static ArrayList<Double> datas = new ArrayList<Double>();
	public static ArrayList<Double> averageWeight = new ArrayList<Double>();
	public Double averageSpeed = 0d;
	public double speed = 0d;
	private Double oldWeight = 0d;
	private Double newWeight = 0d;
	private Double diff = 0d;
	private ThreadLocal<Integer> countLocal = new ThreadLocal<Integer>() {
		protected Integer initialValue() {return 0;};
	};
	private Double remainingTime = 0d;
	private Double bottleWeight = 54.0d;
	private ThreadLocal<Integer> startFlag = new ThreadLocal<Integer>() {
		protected Integer initialValue() {return 0;};
	}; 
	private Double startWeight = 0d;
	private ThreadLocal<String> mac = new ThreadLocal<String>();
	private ThreadLocal<String> ip = new ThreadLocal<String>();
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private ThreadLocal<Integer> stopFlag = new ThreadLocal<Integer>() {
		protected Integer initialValue() {return 0;};
	};
	private HeartBeatThread hbt;
	private static String body;
	public final byte[] byteLock = new byte[1];
	public final ReentrantLock lock = new ReentrantLock();
	private Double oldTimeTag = 0d;
	private Double newTimeTag = 0d;
	private float startTimeTag = 0;
	private String timeTag = "";
	private List<Double> timeArray = new ArrayList<Double>();
	private Double timePerData = 0d;
	private double fittingTimeRemaining = 0d;
	private double fittingSpeed = 0d;
	private double spentWeight = 0d;
	private double remainingtime2 = 0d;
	
	public float getStartTimeTag() {
		return startTimeTag;
	}

	public void setStartTimeTag(float startTimeTag) {
		this.startTimeTag = startTimeTag;
	}

	public String getTimeTag() {
		return timeTag;
	}

	public void setTimeTag(String timeTag) {
		this.timeTag = timeTag;
	}

	public Double getOldTimeTag() {
		return oldTimeTag;
	}

	public void setOldTimeTag(Double oldTimeTag) {
		this.oldTimeTag = oldTimeTag;
	}

	public Double getNewTimeTag() {
		return newTimeTag;
	}

	public void setNewTimeTag(Double newTimeTag) {
		this.newTimeTag = newTimeTag;
	}

	public Integer getStartFlag() {
		return startFlag.get();
	}

	public void setStartFlag(Integer startFlag) {
		this.startFlag.set(startFlag);;
	}

	public Integer getCountLocal() {
		return countLocal.get();
	}

	public void setCountLocal(Integer countLocal) {
		this.countLocal.set(countLocal);
	}

	public byte[] getByteLock() {
		return byteLock;
	}

	public ReentrantLock getLock() {
		return lock;
	}

	public static String getBody() {
		return body;
	}

	public void setBody(String body) {
		DataHandlerClient.body = body;
	}

	public Integer getDATA_LENGTH() {
		return DATA_LENGTH;
	}

	public void setDATA_LENGTH(Integer dATA_LENGTH) {
		DATA_LENGTH = dATA_LENGTH;
	}

	public static ArrayList<Double> getDatas() {
		return datas;
	}

	public static void setDatas(ArrayList<Double> datas) {
		DataHandlerClient.datas = datas;
	}

	public ArrayList<Double> getAverageWeight() {
		return averageWeight;
	}

	public void setAverageWeight(ArrayList<Double> averageWeight) {
		DataHandlerClient.averageWeight = averageWeight;
	}

	public Double getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(Double averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	public Double getOldWeight() {
		return oldWeight;
	}

	public void setOldWeight(Double oldWeight) {
		this.oldWeight = oldWeight;
	}

	public Double getNewWeight() {
		return newWeight;
	}

	public void setNewWeight(Double newWeight) {
		this.newWeight = newWeight;
	}

	public Double getDiff() {
		return diff;
	}

	public  void setDiff(Double diff) {
		this.diff = diff;
	}

	public Double getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(Double remainingTime) {
		this.remainingTime = remainingTime;
	}

	public Double getBottleWeight() {
		return bottleWeight;
	}

	public void setBottleWeight(Double bottleWeight) {
		this.bottleWeight = bottleWeight;
	}


	public Double getStartWeight() {
		return startWeight;
	}

	public void setStartWeight(Double startWeight) {
		this.startWeight = startWeight;
	}

	public String getMac() {
		return mac.get();
	}

	public void setMac(String mac) {
		this.mac.set(mac);
	}

	public String getIp() {
		return ip.get();
	}

	public void setIp(String ip) {
		this.ip.set(ip);
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static void setSdf(SimpleDateFormat sdf) {
		DataHandlerClient.sdf = sdf;
	}

	public Integer getStopFlag() {
		return stopFlag.get();
	}

	public void setStopFlag(Integer stopFlag) {
		this.stopFlag.set(stopFlag);
	}

	public HeartBeatThread getHbt() {
		return hbt;
	}

	public  void setHbt(HeartBeatThread hbt) {
		this.hbt = hbt;
	}

	public DataHandler getDh() {
		return dh;
	}

	public void setDh(DataHandler dh) {
		this.dh = dh;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	DataHandlerClient(String from) {
		Thread.currentThread().setName(from);
		this.from = from;
		this.dh = new DataHandler();
	}
	
	public void run() {
		while(body != null && !body.equals("")) {
//			lock.lock();
		synchronized (byteLock) {
			System.out.println("body " + body + Thread.currentThread().getName());
//			System.out.println("countlocal " + getCountLocal() + " " + body + Thread.currentThread().getName());
//			System.out.println("thread name " + Thread.currentThread().getName());
			if(body != null && !body.equals("")) {
				if(body.contains("START")) {
					startFlag.set(1);
					this.countLocal.set(-1);
					stopFlag.set(0);
					System.out.println("start ");
				}else if(!body.contains("MAC")){
					String substring = body.substring(body.indexOf("=") + 1);
					newWeight = Double.parseDouble(body.substring(body.indexOf("=") + 1, body.indexOf(",") - 1));
					if(body.contains("time_tag") && !body.contains("STOP")) {
						timeTag = body.substring(body.indexOf("time_tag") + 9);
					}
					if(oldTimeTag == 0) {
						oldTimeTag = Double.parseDouble(timeTag);
					}else {
						oldTimeTag = newTimeTag;
						newTimeTag = Double.parseDouble(timeTag);
						timePerData = (newTimeTag - oldTimeTag) / 1000;
					}
//					System.out.println(" weight " + newWeight +" " +  Thread.currentThread().getName());
					if(oldWeight != newWeight) {
						diff = newWeight - oldWeight;
						if(diff > 50) {
							startWeight = oldWeight;
						}
						oldWeight = newWeight;
					}
					if(oldWeight == 0) {
						oldWeight = Double.parseDouble(body.substring(body.indexOf("=")));
					}
				}
				
				//开始输液
				if(getStartFlag() == 1) {
					this.countLocal.set(getCountLocal() + 1);
					if(timeArray.size() != GlobalConstants.DATA_LENGTH) {
						timeArray.add((newTimeTag - oldTimeTag) / 1000);
					}
					spentWeight = startWeight - newWeight;
//					System.out.println("count " + count + " " + Thread.currentThread().getName());
//					System.out.println("countLocal " + getCountLocal() + " " + Thread.currentThread().getName());
					//开始输液操作
					if(getCountLocal() == 1 ) {
						System.out.println("start1 ");
						startWeight = newWeight;
						String result = HospitalUtils.startInfusion(JsonUtils.makeInfusionStartJson(mac.get(), ip.get(), "", ""));
						System.out.println("result " + result);
						String data = HospitalUtils.getJsonData(result);
						System.out.println("data " + data);
						StartResutData jsonData = (StartResutData) JsonUtil.getInstance().json2Obj(data.toLowerCase(), 
													StartResutData.class);
						if(jsonData != null && !jsonData.equals("")) {
							SystemUtils.writeProperty(jsonData.getMac(), jsonData.getSequenceno());
							if((hbt != null && hbt.isThreadAllDone())) {
								hbt.setThreadAllDone(false);
							}else {
								hbt = new HeartBeatThread(jsonData.getMac(), ip.get(), jsonData.getSequenceno());
								hbt.start();
							}
						}
					}
					//发送总时间给终端
					if(getCountLocal() == GlobalConstants.DATA_LENGTH + 120) {
						ConnectUtils.sendDeviceMessage( from + "@" + GlobalConstants.serverName,
								                      "total time:" +  (int)Math.abs(remainingtime2) + "#" + sdf.format(new Date()).toString());
						System.out.println("total time" + Math.abs(remainingTime));
						System.out.println("total time ");
					}
					//datas[count] = newWeight;
					if(getCountLocal() > 0) {
						if(Math.abs(diff) > 10) {
							datas.add(oldWeight - averageSpeed);
						} else {
							datas.add(newWeight);
						}
						if(getCountLocal() >= DATA_LENGTH) {
							double sum = 0d;
							for(int i=0;i<DATA_LENGTH;i++) {
								sum += datas.get(getCountLocal() - DATA_LENGTH + i); 
							}
							//平均重量
							double average = sum / DATA_LENGTH;
							if(averageWeight.size() < (DATA_LENGTH + 1)/2) {
								for(int i=0;i<(DATA_LENGTH + 1)/2 - 1;i++) {
									averageWeight.add(0d);
								}
								averageWeight.add(average);
							} else {
								averageWeight.add(average);
							}
							
//							for(int i=0;i<DATA_LENGTH;i++) {
//								sum += datas.get(getCountLocal() - DATA_LENGTH + i); 
//							}
							System.out.println("datas length " + datas.size() + " average length " + averageWeight.size() + " localcount " + getCountLocal());
							if(averageWeight.size() > GlobalConstants.DATA_LENGTH && getCountLocal() > GlobalConstants.DATA_LENGTH) {
								double tmpA=0d, tmpB=0d;
								for(int i=0; i<GlobalConstants.DATA_LENGTH; i++) {
									tmpA = tmpA + Math.abs(datas.get(getCountLocal() - DATA_LENGTH + i) -
											averageWeight.get(getCountLocal() - (DATA_LENGTH + 1)/2)) 
									       * Math.abs((getCountLocal() - DATA_LENGTH + i - (getCountLocal() - (DATA_LENGTH + 1)/2)));
									tmpB = tmpB + (getCountLocal() - DATA_LENGTH + i - (getCountLocal() - (DATA_LENGTH + 1)/2))   //两个数据间隔一秒
											    * (getCountLocal() - DATA_LENGTH + i - (getCountLocal() - (DATA_LENGTH + 1)/2));  //平方和
								}
								fittingSpeed = tmpA/tmpB;
								if(fittingSpeed != 0) {
									fittingTimeRemaining = (newWeight - bottleWeight) / fittingSpeed;
									System.out.println("fitting speed " + fittingSpeed);
									System.out.println("fitting remaining " + fittingTimeRemaining);
								}
							}
							//不取平均速度
							speed = spentWeight / (newTimeTag / 1000);
							//平均速度
//							System.out.println("startWeight " + startWeight);
							averageSpeed = (startWeight - averageWeight.get(getCountLocal() - (DATA_LENGTH + 1)/2))/
									       (getCountLocal() - (DATA_LENGTH + 1)/2);
							System.out.println("average_speed " + averageSpeed);
							System.out.println("speed " + speed);
							System.out.println("remain time 2 " + remainingtime2);
							if(speed != 0) {
								remainingtime2 = (newWeight - bottleWeight) / speed; 
								ConnectUtils.sendDeviceMessage( from + "@" + GlobalConstants.serverName, 
																"remaining time:" + (int) Math.abs(remainingtime2) + "#" + sdf.format(new Date()).toString());
							}
							if(averageSpeed != 0) {
								remainingTime = (newWeight - bottleWeight) / averageSpeed;
								System.out.println("remaining_time " + remainingTime + Thread.currentThread().getName());
//								ConnectUtils.sendDeviceMessage( from + "@" + GlobalConstants.serverName, 
//																"remaining time:" + (int) Math.abs(remainingTime) + "#" + sdf.format(new Date()).toString());
							}
						}
					}
				}
				String s = "";
				//初始化信息
				if(body.contains("MAC")) {
					try {
						JSONObject jo = new JSONObject(body);
						mac.set(jo.getString("MAC"));
						ip.set(jo.getString("IP"));
						System.out.println("mac " + mac + " ip " + ip);
						jo.put("FROM", from);
						//mac地址在文件中不存在则保存
	//					if(!FileUtils.isMacExists(mac)) {
	//						FileUtils.writeUserData(jo.toString());
							//对服务器发送注册请求
	//						s = HttpUtils.sendJsonPost(GlobalConstants.CONNECT_URL + "/device", JsonUtils.makeDeviceJson(mac, ip, ""));
							s = HttpUtils.postDevice(JsonUtils.makeDeviceJson(mac.get(), ip.get(), ""));
							System.out.println("post result  " + s);
							String result = HospitalUtils.getJsonData(s);
	//					} 
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				List<String> dataAdd = new ArrayList<String>();
				if(speed != 0 && remainingtime2 != 0) {
					dataAdd.add(speed + "");
					dataAdd.add(remainingtime2 + "");
					if(hbt.getSeqNo() != null && hbt.getSeqNo() != "") {
						dataAdd.add(hbt.getSeqNo());
					}
					dataAdd.add(sdf.format(new Date()).toString());
				}else {
					dataAdd.add("averageSpeed");
					dataAdd.add("remainingTime");
				    dataAdd.add("SeqNo");
				}
				WriteCSV.processBody(body, from,dataAdd, timeTag);
				if(Math.abs(remainingTime) < 100 && Math.abs(remainingTime) != 0 && getStartFlag() == 1&& getCountLocal() > 0) {
					stopFlag.set(1);
				}
				
				//删除滴定设备
				if(body.toLowerCase().contains("stop")) {
					System.out.println("delete seqNo " + hbt.getSeqNo());
					String result = HttpUtils.delete(JsonUtils.makeEndJson(mac.get(), "2", "2", hbt.getSeqNo()));
					if(hbt!= null) {
						hbt.setThreadAllDone(true);
					}
					System.out.println("delete result  " + result);
				}
				
				//删除滴定设备
//				if(getStopFlag() == 1 && getStartFlag() == 1) {
////					System.out.println("delete seqNo " + hbt.getSeqNo());
//					String result = HttpUtils.delete(JsonUtils.makeEndJson(mac.get(), "2", "2", hbt.getSeqNo()));
////					System.out.println("delete result  " + result);
//					stopFlag.set(1);
//					startFlag.set(0);
//				}
			}
			try {
				byteLock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//			lock.unlock();
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} 
		}
	}
}
