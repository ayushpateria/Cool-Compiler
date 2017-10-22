package cool;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class IRClass {
	public String name;
	public String parent = null;
	public HashMap <String, AST.attr> alist;
	public HashMap <String, AST.method> mlist;	
	public HashMap <String, Integer> attrOffset;
	public HashMap <String, String> IRname;
	
	
	IRClass(String nm, String pr, HashMap<String, AST.attr> al, HashMap<String, AST.method> ml) {
		name = new String(nm);
		if(pr != null) parent = new String(pr);
		alist = new HashMap <String, AST.attr>();
		alist.putAll(al);
		mlist = new HashMap <String, AST.method>();
		mlist.putAll(ml);
		
		attrOffset = new HashMap <String, Integer>();
		int attroff = 0;
		for (Entry<String, AST.attr> entry : alist.entrySet()) {
			attrOffset.put(entry.getKey(), attroff);
			attroff++;
		}

		IRname = new HashMap <String, String>();
		HashMap<String, String> irname = new HashMap<String, String>();
		for (Entry<String, AST.method> entry : mlist.entrySet()) {
			String m_irname = "@"+name+"_"+entry.getKey();
			IRname.put(entry.getKey(), m_irname);
		}
		
	}
}