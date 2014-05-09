package foo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  
 * @description:文本模板解析工具
 * 
 * @author     :liuwei
 * @create     :Jan 22, 2013
 *
 * 上海创乐人企业发展有限公司
 */
public class SimpleTextTemplateUtil {
	
	private String template;//文本模板
	private Map<String,String> propertys;
	private final String regex = "\\$\\{[0-9a-zA-Z_\\s]*\\}";	
	
	public SimpleTextTemplateUtil(String template) {
		this.template = template;
		init();
	}
	
	private void init(){
		initPropertys();
	}

	/**
	 * 
	 * @description:初始化文字模板属性
	 * @create     :Jan 22, 2013
	 * @author     :liuwei
	 * @return     :void
	 */
	private void initPropertys(){
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(template);
		while(matcher.find()){
			String group = matcher.group();
			addProperty(group);
		}
	}
	
	private void addProperty(String srcProperty){
		if(propertys==null)propertys = new HashMap<String,String>();
		propertys.put(srcProperty,trimProperty(srcProperty));
	}
	
	public Set<String> getSrcPropertys(){
		Set<String> set = new HashSet<String>(propertys.size());
		set.addAll(propertys.keySet());
		return set;
	}
	
	public Set<String> getPropertys(){
		Set<String> set = new HashSet<String>(propertys.size());
		set.addAll(propertys.values());
		return set;
	}
	
	private String trimProperty(String srcProperty){
		srcProperty = srcProperty.replace("${","");
		srcProperty = srcProperty.replace("}","");
		return srcProperty.trim();
	}
	
	public void reloadPropertys(){
		initPropertys();
	}
	
	/**
	 * 
	 * @description:根据提供的数据生成 目标文本
	 * @create     :Jan 22, 2013
	 * @author     :liuwei
	 * @param data
	 * @return
	 * @return     :String
	 */
	public String bulidTemplate(Map<String, Object> data){
		if(data==null||this.template==null)return null;
		StringBuffer rst = new StringBuffer(template);
		Set<String> set = getSrcPropertys();
		for (String string : set) {
			String a = propertys.get(string);
			Object val = data.get(propertys.get(string));
			String valStr = "";
			if(val!=null){
				valStr = val.toString();
			}
			int index = rst.indexOf(string);
			rst.replace(index,index+string.length(),valStr);
		}
		
		return rst.toString();
	}
	
	public static void main(String[] args) {
		
		Map<String, String> m2 = new HashMap<String, String>();
		
		StringBuffer buf = new StringBuffer("<a>你好 ${name}  你的用户名是 ${ name}  密码是 ${ pwd} id:${id }</a>");
		SimpleTextTemplateUtil exp = new SimpleTextTemplateUtil(buf.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "liuwei");
		map.put("pwd", "123");
		System.out.println(exp.bulidTemplate(map));
	}
}
