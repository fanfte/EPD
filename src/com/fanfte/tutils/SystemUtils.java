package com.fanfte.tutils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.codehaus.jettison.json.JSONObject;


public class SystemUtils {
	
	private static Properties pro;
	public static final String		DATA_FILE = ".opts";
	public static final String PROP_FILE = "SeqNo.opts";
	private static void saveOpts(JSONObject obj) {
		String fileDir = System.getProperty("user.dir")  ;
		File file = new File(fileDir, DATA_FILE);
		FileOutputStream fos = null;
		try {
			 fos =new FileOutputStream(file);
			 Properties pro = new Properties();
			 Iterator it = obj.keys();  
	            while (it.hasNext()) {  
	            	 String key = (String) it.next();  
	                 String value = obj.getString(key); 
	            	pro.setProperty(key, value);
	            }
			 pro.store(fos," ");  
		     fos.close(); 
		} catch (Exception e) {
			
		} finally {
			try {
				if (null != fos) fos.close();
			} catch (IOException e) {
				
			}
		}
	}
	
	//д��Properties��Ϣ
    public static void WriteProperties(String filePath, String pKey, String pValue) throws IOException {
        Properties pps = new Properties();
        InputStream in = new FileInputStream(filePath);
        //���������ж�ȡ�����б�����Ԫ�ضԣ� 
        pps.load(in);
        //���� Hashtable �ķ��� put��ʹ�� getProperty �����ṩ�����ԡ�  
        //ǿ��Ҫ��Ϊ���Եļ���ֵʹ���ַ���������ֵ�� Hashtable ���� put �Ľ����
        OutputStream out = new FileOutputStream(filePath);
        pps.setProperty(pKey, pValue);
        //���ʺ�ʹ�� load �������ص� Properties ���еĸ�ʽ��  
        //���� Properties ���е������б�����Ԫ�ضԣ�д�������  
        pps.store(out, "Update " + pKey + " name");
    }
	
	public static String readProperty(String filePath, String key) {
		String value = "";
		Properties pps = new Properties();
	    try {
	        InputStream in = new BufferedInputStream (new FileInputStream(filePath));  
	        pps.load(in);
	        value = pps.getProperty(key);
	        System.out.println(key + " - " + value);
	     }catch (IOException e) {
	         e.printStackTrace();
	     }
	    return value;
	}
	
	//��ȡProperties��ȫ����Ϣ
    public static Map<String, String> GetAllProperties(String filePath) throws IOException {
        Properties pps = new Properties();
        InputStream in = new BufferedInputStream(new FileInputStream(filePath));
        pps.load(in);
        Enumeration en = pps.propertyNames(); //�õ������ļ�������
        Map<String, String> map = new HashMap<String, String>();
        while(en.hasMoreElements()) {
            String strKey = (String) en.nextElement();
            String strValue = pps.getProperty(strKey);
            System.out.println(strKey + "=" + strValue);
            map.put(strKey, strValue);
        }
        return map;
    }
	
	public static void writeProperty(String key, String value) {
		String fileDir = System.getProperty("user.dir")  ;
		System.out.println(fileDir);
		File file = new File(fileDir, PROP_FILE);
		FileOutputStream fos = null;
		try {
			 fos =new FileOutputStream(file);
			 Properties pro = new Properties();
	         pro.setProperty(key, value);
			 pro.store(fos," ");  
		     fos.close(); 
		} catch (Exception e) {
			
		} finally {
			try {
				if (null != fos) fos.close();
			} catch (IOException e) {
				
			}
		}
	}
	
	public static Properties getOptsPro(){  
		if (null != pro) return pro;
		Reader fis = null;
        try {  
        	String file = System.getProperty("user.dir") + File.separator + DATA_FILE;
        	System.out.println("file " + file);
        	fis = new FileReader(file);
            pro = new Properties();  
            pro.load(fis);
            String username = pro.getProperty("username", null);
			String password = pro.getProperty("password", null);
			
			if(isEmpty(username) || isEmpty(password)) {
				return null;
			}
            return pro;
            
        } catch (Exception e) {  
           // System.out.println("鏂囦欢涓嶅瓨鍦�:"+ e.toString());  
            return null;
        }   finally {
        	if (null !=fis) {
        		try {
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        	
        }
    }  
	
	/**
	 * will trim the string
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
	    if (null == s)
	        return true;
	    if (s.length() == 0)
	        return true;
	    if (s.trim().length() == 0)
	        return true;
	    return false;
	}
}
