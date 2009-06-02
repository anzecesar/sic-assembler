package si.fri.spo.sic.action;

import si.fri.spo.sic.context.SicContext;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.util.Log;

public class AbstractBean implements ActionBean {
	protected static final Log logger = Log.getInstance(AbstractBean.class);
	
	private SicContext context;
	
	public SicContext getContext() {
		return this.context;
	}

	public void setContext(ActionBeanContext context) {
		this.context = (SicContext) context;
	}

}