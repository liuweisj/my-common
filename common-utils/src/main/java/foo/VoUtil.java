package foo;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VoUtil {
	
	static public Object getValue(Object obj,String propertys){
		String[] propertyAry = propertys.split("\\.");
		Object tmp = obj;
		for (String string : propertyAry) {
			if(string.indexOf("[")!=-1){
				String[] pros = getProperty(string);
				for (String string2 : pros) {
					tmp = get(tmp,string2);
				}
			}else{
				tmp = get(tmp, string);
			}
		}
		return tmp;
	}
	
	static private Object get(Object obj,String property){
		
		String[] pros = getProperty(property);
		Object tmp = obj;
		
		if(obj==null||property==null)return null;
		if(obj instanceof Map){
			Map map = (Map)obj;
			String key = getIdxProperty(property);
			if(property.indexOf("[")!=-1){
				key = getIdxProperty(property);
			}
			return map.get(key);
		} else if(obj instanceof List){
			List lst = (List)obj;
			return lst.get(Integer.parseInt(getIdxProperty(property)));
		}else if(obj.getClass().isArray()){
			return Array.get(obj,Integer.parseInt(getIdxProperty(property)));
		}else if(isBaseDataType(obj.getClass())){
			return String.valueOf(obj);
		}else{
			try {
				Method method = obj.getClass().getDeclaredMethod(getPropertyGetMethodName(property));
				return method.invoke(obj);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static String getPropertyGetMethodName(String property){
		return "get"+property.substring(0, 1).toUpperCase()+property.substring(1);
	}
	
	private static String getIdxProperty(String key){
		String regex = "[\\[\\]'\"]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(key);
		return key.replaceAll(regex,"");
	}
	
	private static String[] getProperty(String key){
		if(key==null)return null;
		
		String regex = "[\\[][0-9a-zA-Z']*[\\]]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(key);
		StringBuffer buf = new StringBuffer();
		while(matcher.find()){
			String s = matcher.group();
			buf.append(s+",");
		}
		int start = key.indexOf("[");
		if(start>0)
			buf.insert(0,key.substring(0,start)+",");
		else
			buf.append(key);
		
		return buf.toString().split(",");
	}
	public static void main(String[] args) {
//		String[] a = getProperty("aaa[0][1]");
//		System.out.println("size:"+a.length);
//		for (String string : a) {
//			System.out.println(string);
//		}
//		String regex = "[^\\[][\"']?[0-9a-zA-Z]*[\"']?[^\\]]";
//		String regex = "[^\\[][0-9a-zA-Z]+[^\\]]";
//		System.out.println(regex);
//		Pattern pattern = Pattern.compile(regex);
//		Matcher matcher = pattern.matcher("[0] [12] [\"21\"] ['ab']");
//		while(matcher.find()){
//			System.out.println(matcher.group());
//		}
		
//		int[] ary = new int[10];
//		System.out.println(ary.getClass().isArray());
//		Map<String, Object > map = new HashMap<String, Object>();
//		map.put("aa",ary);
//		System.out.println(Array.get(map.get("aa"), 0));
//		System.out.println(getIdxProperty("[1]"));
		User user = new User();
		user.setName("liuwei");
		user.setPwd("123");
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("user",user);
		data.put("test", "123");
		data.put("sex", 12);
		data.put("array", new String[]{"aa","bb","cc"});
		System.out.println(getValue(data,"sex"));
		System.out.println(getValue(data,"user.name"));
		System.out.println(getValue(data,"array[1]"));
	}
	
	
	/**  
	  * 判断一个类是否为基本数据类型。  
	  * @param clazz 要判断的类。  
	  * @return true 表示为基本数据类型。  
	  */  
	 private static boolean isBaseDataType(Class clazz) {   
	     return  
	     (   
	         clazz.equals(String.class) ||   
	         clazz.equals(Integer.class)||   
	         clazz.equals(Byte.class) ||   
	         clazz.equals(Long.class) ||   
	         clazz.equals(Double.class) ||   
	         clazz.equals(Float.class) ||   
	         clazz.equals(Character.class) ||   
	         clazz.equals(Short.class) ||   
	         clazz.equals(BigDecimal.class) ||   
	         clazz.equals(BigInteger.class) ||   
	         clazz.equals(Boolean.class) ||   
	         clazz.equals(Date.class) ||   
	         clazz.isPrimitive()   
	     );   
	 }   
}
class User{
	private String name;
	private String pwd;
	private Map<String, String> map;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	
}