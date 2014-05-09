//package foo;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.dom4j.DocumentException;
//import org.dom4j.Node;
//
//import com.sshtools.j2ssh.util.Hash;
//
//
//
///**
// * @description:
// * 
// * @author     :liuwei
// * @create     :Jan 23, 2013
// *
// * 上海创乐人企业发展有限公司
// */
//public class DynamicTemplate2 extends XmlTemplate{
//	private final String root = "happy";
//	private final String thisEle = "dynamics";
//	private final String thisItem = "dynamic";
//	private final String title = "title";
//	private final String content = "content";
//	
//	public DynamicTemplate2(String file) throws DocumentException {
//		super(file);
//	}
//	
//	/**
//	 * 
//	 * @description:根据模块id 和 类型id 查出标题模板
//	 * @create     :Jan 23, 2013
//	 * @author     :liuwei
//	 * @param mid
//	 * @param type
//	 * @return
//	 * @return     :String
//	 */
//	public String getTmpltTitleByMid(Long mid,Long type){
//		Node node = selectSingle("/"+root+"/"+thisEle+"/"+thisItem+"[@mid="+mid+"]/"+title+"[@type="+type+"]");
//		if(node==null)return null;
//		return node.getText();
//	}
//	
//	/**
//	 * 
//	 * @description:根据模块id 和 类型id 查出内容模板
//	 * @create     :Jan 23, 2013
//	 * @author     :liuwei
//	 * @param mid
//	 * @param tid
//	 * @return
//	 * @return     :String
//	 */
//	public String getTmpltContentByMid(Long mid,Long tid){
//		Node node = selectSingle("/"+root+"/"+thisEle+"/"+thisItem+"[@mid="+mid+"]/"+content+"[@tid="+tid+"]");
//		if(node==null)return null;
//		return node.getText();
//	}
//	
//	/**
//	 * 
//	 * @description:根据模块id 和 类型id 查出内容模板
//	 * @create     :Jan 23, 2013
//	 * @author     :liuwei
//	 * @param mid
//	 * @param tid
//	 * @return
//	 * @return     :String
//	 */
//	public String getTmpltContentByMid(Long mid,String type){
//		Node node = selectSingle("/"+root+"/"+thisEle+"/"+thisItem+"[@mid="+mid+"]/"+content+"[@type='"+type+"']");
//		if(node==null)return null;
//		return node.getText();
//	}
//	
//	/**
//	 * 
//	 * @description:根据数据生成目标文本
//	 * @create     :Jan 22, 2013
//	 * @author     :liuwei
//	 * @param template
//	 * @param data
//	 * @return
//	 * @return     :String
//	 */
//	public String bulidTemplate(String template,Map<String, Object> data){
//		if(template==null||data==null)return null;
//		SimpleTextTemplateUtil util = new SimpleTextTemplateUtil(template);
//		return util.bulidTemplate(data);
//	}
//	
//	public static void main(String[] args) throws DocumentException {
//		DynamicTemplate2 t2 = new DynamicTemplate2("D:/tmpXml.xml");
////		System.out.println(t2.getTmpltContentByMid(1L, "text"));
////		System.out.println(t2.getTmpltTitleByMid(1L, 1L));
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("name", "liuwei");
//		System.out.println(t2.bulidTemplate(t2.getTmpltTitleByMid(1L, 1L),map));
//	}
//}
