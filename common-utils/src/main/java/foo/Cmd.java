package foo;

import java.util.Map;

public interface Cmd {
	public void execute(StringBuffer out,StringBuffer content,Map<String, Object> cmd);
}
