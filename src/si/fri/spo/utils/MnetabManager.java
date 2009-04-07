package si.fri.spo.utils;

import java.util.HashMap;
import java.util.Map;

import si.fri.spo.data.Mnemonic;

public class MnetabManager {
	private MnetabManager m_manager;
	
	private Map<String,Mnemonic> mnetab;
	
	private MnetabManager() {
		
	}
	
	public MnetabManager getInstance() {
		if(m_manager == null)
			m_manager = new MnetabManager();
		return m_manager;
	}
	
	public void makeTable() {
		mnetab = new HashMap<String, Mnemonic>();
	}
}
