package si.fri.spo.utils;

import java.util.HashMap;
import java.util.Map;

import si.fri.spo.exceptions.NapakaPriPrevajanju;

public class SimtabManager {
	private static SimtabManager m_instance;
	
	Map<String, Integer> simTab;
	
	private SimtabManager() {
		simTab = new HashMap<String, Integer>();
	}
	
	public static SimtabManager getInstance() {
		if(m_instance == null)
			m_instance = new SimtabManager();
		
		return m_instance;
	}
	
	/**
	 * Doda labelo v tabelo simtab
	 * @param labela
	 * @param lokSt
	 * @throws NapakaPriPrevajanju Napaka, če je dodana labela že vsebovana.
	 */
	public void dodajLabelo(String labela, Integer lokSt) throws NapakaPriPrevajanju {
		if(simTab.containsKey(labela)) {
			throw new NapakaPriPrevajanju("Napaka: Labela " + labela + " je definirana dvakrat!");
		}
		
		simTab.put(labela, lokSt);
	}

	public boolean isLabela(String s) {
		if(simTab.containsKey(s)) 
			return true;
		else 
			return false;
	}
	
	public int getLokSt(String labela) {
		return simTab.get(labela);
	}

}
