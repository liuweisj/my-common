//package foo;
//
//import java.io.File;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.Node;
//import org.dom4j.io.SAXReader;
//
///**
// * @description:
// * 
// * @author     :liuwei
// * @create     :Jan 22, 2013
// *
// * 上海创乐人企业发展有限公司
// */
//public class XmlTemplate {
//	
//	private SAXReader reader;
//	private Document doc;
//	
//	public XmlTemplate(String file) throws DocumentException {
//		load(file);
//	}
//	
//	public void load(String file) throws DocumentException{
//		this.load(new File(file));
//	}
//	
//	private void load(File xmlFile) throws DocumentException{
//		this.reader = new SAXReader();
//		this.doc = reader.read(xmlFile);
//	}
//	
//	/**
//	 * 
//	 * @description:根据xpath查询节点
//	 * @create     :Jan 22, 2013
//	 * @author     :liuwei
//	 * @param path
//	 * @return
//	 * @return     :List<Node>
//	 */
//	public List<Node> select(String path){
//		return doc.selectNodes(path);
//	}
//	
//	/**
//	 * 
//	 * @description:根据xpath查询唯一节点
//	 * @create     :Jan 22, 2013
//	 * @author     :liuwei
//	 * @param path
//	 * @return
//	 * @return     :Node
//	 */
//	public Node selectSingle(String path){
//		return doc.selectSingleNode(path);
//	}
//	
//	/**
//	 * 
//	 * @description:根据path 和属性集合查询节点
//	 * @create     :Jan 22, 2013
//	 * @author     :liuwei
//	 * @param path
//	 * @param map
//	 * @return
//	 * @return     :String[]
//	 */
//	public String[] selectTmpltByPropertys(String path,Map<String, String> map){
//		if(map==null)return null;
//		Set<String> set = map.keySet();
//		StringBuffer xpath = new StringBuffer(path);
//		if(!map.isEmpty())xpath.append("[");
//		String and = " and ";
//		
//		for (String string : set) {
//			xpath.append("@"+string+"="+map.get(string));
//			xpath.append(and);
//		}
//		
//		if(xpath.length()>5&&and.equals(xpath.substring(xpath.length()-and.length(),xpath.length()))){
//			xpath.replace(xpath.length()-and.length(),xpath.length(),"");
//		}
//		if(!map.isEmpty())xpath.append("]");
//		
//		List<Node> lst = this.select(xpath.toString());
//		if(lst==null)return null;
//		String[] rst = new String[lst.size()];
//		for (int i = 0; i < rst.length; i++) {
//			rst[i] = lst.get(i).getText();
//		}
//		return rst;
//	}
//	
//	public static void main(String[] args) throws DocumentException {
//		XmlTemplate xml = new XmlTemplate("D:/tmpXml.xml");
//		List<Node> nodes = xml.select("/dynamics/dynamic[@name='diary' and @type='text']");
//		System.out.println(nodes.size());
//	}
//}
