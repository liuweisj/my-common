package foo;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValueUtil {

	static final String pkg = "core";
	static final String cmd = "for";

	public Object getVal(Object obj, String key) throws Exception {
		if (obj instanceof Integer || obj instanceof Boolean
				|| obj instanceof Float || obj instanceof Double
				|| obj instanceof Character || obj instanceof Byte) {
			throw new Exception("base data type no property by " + key);

		}

		return key;
	}
	
	public static void main(String[] args) {
		
		Cmd _for = new Cmd() {
			@Override
			public void execute(StringBuffer out, StringBuffer content,
					Map<String, Object> data) {
				String val = data.get("value").toString();
				String var = data.get("var").toString();
				Object resource = data.get("data");
				Map<String, Object> scope = new HashMap<String, Object>();
				Object obj = VoUtil.getValue(resource,val);
				Object[] varTar = null;;
				if(obj instanceof List){
					List lst = (List)obj;
					varTar = new Object[lst.size()];
					for (int i = 0; i < lst.size(); i++) {
						varTar[i] = lst.get(i);
					}
				}else if(obj.getClass().isArray()){
					varTar = new Object[Array.getLength(obj)];
					for (int i = 0; i < Array.getLength(obj); i++) {
						varTar[i] = Array.get(obj,i);
					}
				}else{
					System.out.println("for 标签只能迭代数组 或者 List");
				}
				
				
				
				if(out.length()==0)out.append(content);
				StringBuffer bulid = new StringBuffer();
				System.out.println("run for:"+data);
			}
		};
		
		CmdContent.getInstance().reg("for",_for);
		
		String a = "awefawefawefwe<core:for value=\"bb\" var=\"a\">"
				+ "awefwae wefawef" + "</core:for>awefawefawef";
		StringBuffer out = new StringBuffer();
		StringBuffer content = new StringBuffer(a);
		Map<String, Object> cmd = readCmd(out,content,null);
		CmdContent.getInstance().execute(out,content, cmd);
		
	}

	
	/**
	 * 
	 * @description:解析为命令行
	 * @create     :Jan 29, 2013
	 * @author     :liuwei
	 * @param content
	 * @param data
	 * @return
	 * @return     :Map<String,String>
	 */
	static public Map<String, Object> readCmd(StringBuffer out,StringBuffer content, Map<Object, Object> data) {
		// \\[0-9a-zA-Z_\\s]*\\
		String regex = "<"+pkg+"\\:"+cmd+"[^>]*";
		String regexEnd = "</core:for[^>]*";
		Pattern pattern = Pattern.compile(regex);
		Matcher mt = pattern.matcher(content);

		int start = 0;
		int end = 0;
		int endLen = 0;
		int startLen = 0;
		String cmdStr = null;
		String cmd = null;
		String sourceCmd = null;
		
		if (mt.find()) {
			cmdStr = mt.group();
			start = mt.start();
			startLen = cmdStr.length();
		}

		pattern = Pattern.compile(regexEnd);
		mt = pattern.matcher(content);

		if (mt.find()) {
			String s = mt.group();
			end = mt.start();
			endLen = s.length();
		}
		cmdStr = cmdStr.replace("<" + pkg + ":", "");
		sourceCmd = content.substring(start, end + endLen+1); 
		cmd = getCmd(cmdStr);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cmd", cmd);
		map.put("data", data);
		map.put("sourceContent",content);
		map.put("sourceCmd",sourceCmd);
		map.put("cmdBody",content.substring(start+startLen+1, end));
		map.put("out",out);
		map.put("params",getParams(cmdStr,cmd));
		
		return map;
	}
	
	static public String getCmd(String cmds){
		int start = cmds.indexOf(" ");
		String rst = cmds.substring(0,start);
		return rst;
	}
	
	static public Map<String, String> getParams(String cmds,String cmd){
		cmds = cmds.replace(cmd,"");
		String regex = "[a-zA-Z_\\s]*=[\\s]*[\"'][0-9a-zA-Z_\\s]*[\"']";
		Map<String, String> params = new HashMap<String, String>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(cmds);
		while(matcher.find()){
			String kvStr = matcher.group();
			String[] kv = kvStr.split("=");
			params.put(kv[0].trim(),kv[1].replace("\"","").replace("'","").trim());
		}
		return params;
	}
	
	class CmdData{
		private String cmd;
		private Map data;
		private String sourceContent;
		private String sourceCmd;
		private String cmdBody;
		private StringBuffer out;
		private StringBuffer content;
		private Map<String,String> param;
		public String getCmd() {
			return cmd;
		}
		public void setCmd(String cmd) {
			this.cmd = cmd;
		}
		public Map getData() {
			return data;
		}
		public void setData(Map data) {
			this.data = data;
		}
		public String getSourceContent() {
			return sourceContent;
		}
		public void setSourceContent(String sourceContent) {
			this.sourceContent = sourceContent;
		}
		public String getSourceCmd() {
			return sourceCmd;
		}
		public void setSourceCmd(String sourceCmd) {
			this.sourceCmd = sourceCmd;
		}
		public String getCmdBody() {
			return cmdBody;
		}
		public void setCmdBody(String cmdBody) {
			this.cmdBody = cmdBody;
		}
		public StringBuffer getOut() {
			return out;
		}
		public void setOut(StringBuffer out) {
			this.out = out;
		}
		public StringBuffer getContent() {
			return content;
		}
		public void setContent(StringBuffer content) {
			this.content = content;
		}
		public Map<String, String> getParam() {
			return param;
		}
		public void setParam(Map<String, String> param) {
			this.param = param;
		}
	}
}
