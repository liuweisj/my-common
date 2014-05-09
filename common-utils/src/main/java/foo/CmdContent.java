package foo;

import java.util.HashMap;
import java.util.Map;

public class CmdContent {
	static private CmdContent instance = new CmdContent();
	
	static public CmdContent getInstance(){
		return instance;
	}
	
	private CmdContent() {
	}
	
	private Map<String,Cmd> cmds = new HashMap<String, Cmd>();
	
	public void reg(String cmdStr,Cmd cmd){
		cmds.put(cmdStr, cmd);
	}
	
	public void execute(StringBuffer out,StringBuffer content,Map<String,Object> cmd){
		Cmd cmdImp = cmds.get(cmd.get("cmd"));
		if(cmdImp==null){
			System.out.println("not cmd '"+cmd.get("cmd")+"'");
			return;
		}
		cmdImp.execute(out,content,cmd);
	}
	
	public static void main(String[] args) {
		
	}
}
