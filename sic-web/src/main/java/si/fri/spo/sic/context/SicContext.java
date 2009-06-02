package si.fri.spo.sic.context;

import net.sourceforge.stripes.action.ActionBeanContext;

public class SicContext extends ActionBeanContext {
	
	public void storeData(String name, Object value) {
		getRequest().getSession().setAttribute(name, value);
	}
	
	public Object getData(String name) {
		return getRequest().getSession().getAttribute(name);
	}
	
	public void clearData(String name) {
		getRequest().getSession().removeAttribute(name);
	}	
}