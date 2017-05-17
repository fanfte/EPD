package com.fanfte.datatransfer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import com.fanfte.global.GlobalConstants;
import com.fanfte.model.StartResutData;
import com.fanfte.tutils.ConnectUtils;
import com.fanfte.tutils.HospitalUtils;
import com.fanfte.tutils.HttpUtils;
import com.fanfte.tutils.JsonUtil;
import com.fanfte.tutils.JsonUtils;
import com.fanfte.tutils.SystemUtils;
import com.fanfte.tutils.WriteCSV;

public class DataTransfer {
	public volatile static Map<String, Boolean> isRunning = new HashMap<>();
	public static Integer DATA_LENGTH = 5;
	public static ArrayList<Double> datas = new ArrayList<Double>();
	public static ArrayList<Double> averageWeight = new ArrayList<Double>();
	public static Double averageSpeed = 0d;
	private static Double oldWeight = 0d;
	private static Double newWeight = 0d;
	private static Double diff = 0d;
	private static Integer count = 0;
	private static Double remainingTime = 0d;
	private static Double bottleWeight = 54.0d;
	private static Integer startFlag = 0; 
	private static Double startWeight = 0d;
	private static String mac;
	private static String ip;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Integer stopFlag = 0;
	private static HeartBeatThread hbt;
	private static ArrayList<String> froms = new ArrayList<String>();
	private static Map<String, DataHandlerClient> threadMap = new HashMap<String, DataHandlerClient>();
	static{ 
	    try{
	        Class.forName("org.jivesoftware.smack.ReconnectionManager");
	    }catch(Exception e){
		e.printStackTrace();
	    }
	}
	public static void main(String[] args) {
		
		startTransfer();
	}
	
	public static void startTransfer() {
		Properties pro = SystemUtils.getOptsPro();
		GlobalConstants.username = pro.getProperty("username", null);
		GlobalConstants.password = pro.getProperty("password", null);
		GlobalConstants.domain = pro.getProperty("domain", null);
		GlobalConstants.port = Integer.parseInt(pro.getProperty("port", null));
		GlobalConstants.serverName = pro.getProperty("serverName", null);
		xmppConnect();
	}
	
	public static void xmppReconnect() {
		ConnectUtils.getConnection().addConnectionListener(new ConnectionListener() {
			SimpleDateFormat sdf = new SimpleDateFormat();

			// 当网络断线了，重新连接上服务器触发的事件
			public void reconnectionSuccessful() {
				System.out.println("reconnectionSuccessful");
				JOptionPane.showMessageDialog(null, "server reconnectionSuccessful" + sdf.format(new Date()), "",
						JOptionPane.WARNING_MESSAGE);
			}

			// 重新连接失败
			public void reconnectionFailed(Exception arg0) {
				System.out.println("reconnectionFailed");
			}

			// 重新连接的动作正在进行的动作，里面的参数arg0是一个倒计时的数字，如果连接失败的次数增多，数字会越来越大，开始的时候是14
			public void reconnectingIn(int arg0) {
				System.out.println("time: " + arg0);
				System.out.println("reconnectingIn");
			}

			// 这里就是网络不正常断线激发的事件
			public void connectionClosedOnError(Exception arg0) {
				System.out.println("connectionClosedOnError");
				JOptionPane.showMessageDialog(null, "server connectionClosedOnError" + sdf.format(new Date()), "",
						JOptionPane.WARNING_MESSAGE);
				//xmppConnect();
			}

			// 这里是正常关闭连接的事件
			public void connectionClosed() {
				System.out.println("connectionClosed");
			}
		});
		new Thread() {
			public void run() {
				while (true) {
					try {
						sleep(3 * 1000);
						if(ConnectUtils.getConnection() == null || !ConnectUtils.getConnection().isConnected()) {
							System.out.println("disconnected");
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	public static void xmppConnect() {
		
    	try {
			ConnectUtils.openConnection(GlobalConstants.serverName, GlobalConstants.port);;
		} catch (IllegalStateException e) {
			JOptionPane.showMessageDialog(null, "已经连上服务器", "", JOptionPane.WARNING_MESSAGE);
		} 
    	
    	try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			
		boolean isLogin = ConnectUtils.loginUser(GlobalConstants.username, GlobalConstants.password);
		
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(isLogin) {
			JOptionPane.showMessageDialog(null, "连接服务器成功", "", JOptionPane.WARNING_MESSAGE);
			
			Roster roster = ConnectUtils.getConnection().getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			String s = null;
			for(RosterEntry entry: entries){
				System.out.println("---" + entry.getName());
				s = s + entry.getName() + " ";
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Roster rostera = ConnectUtils.getConnection().getRoster();
			
			// 添加花名册监听器，监听好友状态的改变
			rostera.addRosterListener(new RosterListener() {

				@Override
				public void entriesAdded(Collection<String> invites) {
					System.out.println("entriesAdded" + invites);
					for (Iterator iter = invites.iterator(); iter.hasNext();) {  
                        String fromUserJids = (String)iter.next();  
                        System.out.println(fromUserJids + "请求添加好友");  
                        ConnectUtils.addFriend(fromUserJids.substring(0, fromUserJids.indexOf("@")), "");
                  }   
				}

				@Override
				public void entriesUpdated(Collection<String> invites) {
					System.out.println("entriesUpdated" + invites);
					for (Iterator iter = invites.iterator(); iter.hasNext();) {  
                        String fromUserJids = (String)iter.next();  
                        System.out.println("同意添加的好友是："+fromUserJids);  
                      }  
				}

				@Override
				public void entriesDeleted(Collection<String> addresses) {
					System.out.println("entriesDeleted");
				}

				@Override
				public void presenceChanged(Presence presence) {
					System.out.println("presenceChanged - >" + presence.getStatus());
				}

			});
			
			//ConnectUtils.processOfflineMessage();
			
			ChatManager chatManager = ConnectUtils.getConnection().getChatManager();
			chatManager.addChatListener(new ChatManagerListener() {
				
				@Override
				public void chatCreated(Chat chat, boolean arg1) {
					chat.addMessageListener(new MessageListener() {

						@Override
						public void processMessage(Chat chat, Message message) {
							String body = message.getBody().toString();
//							System.out.println("GlobalConstants.domain " + GlobalConstants.domain);
//							System.out.println("body main " + body);
							String s = "";
							String from = message.getFrom().substring(0,message.getFrom().indexOf("@"));
							ConnectUtils.sendDeviceMessage( from + "@" + GlobalConstants.serverName, "ack:ok#");
							if(!froms.contains(from) && !threadMap.containsKey(from)) {
								froms.add(from);
							} 
							
							if(froms.contains(from)){
								if(!threadMap.containsKey(from)) {
									DataHandlerClient dhc = new DataHandlerClient(from);
									threadMap.put(from, dhc);
									dhc.start();
								}
								if(threadMap.containsKey(from)){
									DataHandlerClient client = threadMap.get(from);
									synchronized (client.byteLock) {
										client.setBody(body);
										client.byteLock.notify();
									}
								}
							}
//							ConnectUtils.sendDeviceMessage( from + "@" + GlobalConstants.serverName, "ack:ok#" + sdf.format(new Date()));
							
//							System.out.println(from + "@" + GlobalConstants.domain);
							
							//开始滴定
/*							if(body.contains("START")) {
								startFlag = 1;
								count = -1;
								stopFlag = 0;
								System.out.println("start ");
							} else if(!body.contains("MAC")){
								String substring = body.substring(body.indexOf("=") + 1);
								newWeight = Double.parseDouble(body.substring(body.indexOf("=") + 1, body.indexOf(",") - 1));
								System.out.println(" weight " + newWeight);
								if(oldWeight != newWeight) {
									diff = newWeight - oldWeight;
									if(diff > 50) {
										startWeight = oldWeight;
									}
									oldWeight = newWeight;
								}
								//判断开始
//								if((newWeight - startWeight) > 50 && startWeight > 0) {
//									startCount ++;
//									if(startCount > 15) {
//										startFlag = 1;
//										count = -1;
//										startCount = 0;
//									}
//								}
								if(oldWeight == 0) {
									oldWeight = Double.parseDouble(body.substring(body.indexOf("=")));
								}
							}
							
							//开始输液
							if(startFlag == 1) {
								
								count ++;
								if(count == 1 ) {
									startWeight = newWeight;
									String result = HospitalUtils.startInfusion(JsonUtils.makeInfusionStartJson(mac, ip, "", ""));
									System.out.println("result " + result);
									String data = HospitalUtils.getJsonData(result);
									System.out.println("data " + data);
									StartResutData jsonData = (StartResutData) JsonUtil.getInstance().json2Obj(data.toLowerCase(), 
																StartResutData.class);
									hbt = new HeartBeatThread(jsonData.getMac(), ip, jsonData.getSequenceno());
									hbt.start();
								}
								//datas[count] = newWeight;
								if(count > 0) {
									if(Math.abs(diff) > 10) {
										datas.add(oldWeight - averageSpeed);
									} else {
										datas.add(newWeight);
									}
									if(count >= DATA_LENGTH) {
										double sum = 0d;
										for(int i=0;i<DATA_LENGTH;i++) {
											sum += datas.get(count - DATA_LENGTH + i); 
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
	//									averageWeight[count-DATA_LENGTH + (DATA_LENGTH + 1)/2] = average;
										//平均速度
//										System.out.println("startWeight " + startWeight);
										averageSpeed = (startWeight - averageWeight.get(count - (DATA_LENGTH + 1)/2))/
												       (count - (DATA_LENGTH + 1)/2);
										System.out.println("average_speed " + averageSpeed);
										if(averageSpeed != 0) {
											remainingTime = (newWeight - bottleWeight) / averageSpeed;
											System.out.println("remaining_time " + remainingTime);
										}
									}
								}
							}
							
							//初始化信息
							if(body.contains("MAC")) {
								try {
									JSONObject jo = new JSONObject(body);
									mac =  jo.getString("MAC");
									ip = jo.getString("IP");
									System.out.println("mac " + mac + " ip " + ip);
									jo.put("FROM", from);
									//mac地址在文件中不存在则保存
//									if(!FileUtils.isMacExists(mac)) {
//										FileUtils.writeUserData(jo.toString());
										//对服务器发送注册请求
//										s = HttpUtils.sendJsonPost(GlobalConstants.CONNECT_URL + "/device", JsonUtils.makeDeviceJson(mac, ip, ""));
										s = HttpUtils.postDevice(JsonUtils.makeDeviceJson(mac, ip, ""));
										System.out.println("post result  " + s);
										String result = HospitalUtils.getJsonData(s);
//									} 
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							List<String> dataAdd = new ArrayList<String>();
							if(averageSpeed != 0 && remainingTime != 0) {
								dataAdd.add(averageSpeed + "");
								dataAdd.add(remainingTime + "");
								if(hbt.getSeqNo() != null && hbt.getSeqNo() != "") {
									dataAdd.add(hbt.getSeqNo());
								}
							}else {
								dataAdd.add("averageSpeed");
								dataAdd.add("remainingTime");
							    dataAdd.add("SeqNo");
							}
							WriteCSV.processBody(body, from,dataAdd);
							ConnectUtils.sendDeviceMessage( from + "@" + GlobalConstants.serverName, "ack:ok#" + sdf.format(new Date()));
							
							if(remainingTime < 100 && remainingTime != 0 && count > 0) {
								stopFlag = 1;
							}
							if(stopFlag == 1) {
								System.out.println("delete seqNo " + hbt.getSeqNo());
								String result = HttpUtils.delete(JsonUtils.makeEndJson(mac, "2", "2", hbt.getSeqNo()));
								System.out.println("delete result  " + result);
								stopFlag = 0;
							}*/
						}
					});
				}
			});
		}
		xmppReconnect();
	}
}

