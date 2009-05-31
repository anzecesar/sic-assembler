package si.fri.spo.managers;

import java.util.List;
import java.util.Vector;


public class ModTabManager {
private static ModTabManager m_instance;
	
	private List<String> modTab; 
	
	private ModTabManager() {
		modTab = new Vector<String>();
	}
	
	public static ModTabManager getInstance() {
		if(m_instance == null)
			m_instance = new ModTabManager();
		
		return m_instance;
	}
	
	public void addMod(String mod) {
		modTab.add(mod);
	}
	
	public String getAll() {
		String modifikacije = "";
		for (String mod : modTab) {
			modifikacije += mod + "\n";
		}
		return modifikacije;
	}

}
