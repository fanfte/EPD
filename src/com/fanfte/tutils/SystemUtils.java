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
	
	//写入Properties信息
    public static void WriteProperties(String filePath, String pKey, String pValue) throws IOException {
        Properties pps = new Properties();
        InputStream in = new FileInputStream(filePath);
        //从输入流中读取属性列表（键和元素对） 
        pps.load(in);
        //调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。  
        //强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
        OutputStream out = new FileOutputStream(filePath);
        pps.setProperty(pKey, pValue);
        //以适合使用 load 方法加载到 Properties 表中的格式，  
        //将此 Properties 表中的属性列表（键和元素对）写入输出流  
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
	
	//读取Properties的全部信息
    public static Map<String, String> GetAllProperties(String filePath) throws IOException {
        Properties pps = new Properties();
        InputStream in = new BufferedInputStream(new FileInputStream(filePath));
        pps.load(in);
        Enumeration en = pps.propertyNames(); //得到配置文件的名字
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
           // System.out.println("閺傚洣娆㈡稉宥呯摠閸︼拷:"+ e.toString());  
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
