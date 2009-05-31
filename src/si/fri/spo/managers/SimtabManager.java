package si.fri.spo.managers;

import java.util.HashMap;
import java.util.Map;

import si.fri.spo.data.Simbol;
import si.fri.spo.exceptions.NapakaPriPrevajanju;

public class SimtabManager {
	private static SimtabManager m_instance;
	
	Map<String, Simbol> simTab;
	
	private SimtabManager() {
		simTab = new HashMap<String, Simbol>();
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
		
		//System.out.println("Labela: " + labela + " " + lokSt + " hex " + Integer.toHexString(lokSt));
		
		simTab.put(labela, new Simbol(lokSt));
	}
	
	public void dodajEqu(String labela, Integer vrednost) throws NapakaPriPrevajanju {
		if(simTab.containsKey(labela)) {
			throw new NapakaPriPrevajanju("Napaka: Labela " + labela + " je definirana dvakrat!");
		}
		simTab.put(labela, new Simbol(vrednost, true));
	}
	

	public boolean isLabela(String s) {
		if(simTab.containsKey(s)) 
			return true;
		else 
			return false;
	}
	
	/**
	 * Predvideva, da je programer že klical isLabela pred tem... Sicer obnašanje ni predvidljivo :]
	 * @param s
	 * @return
	 */
	public boolean isEqu(String s) {
		return simTab.get(s).isEqu();
	}
	
	public int getLokSt(String labela) {
		return simTab.get(labela).getVrednost();
	}
	
	public int getVrednostOperanda(String o) throws NapakaPriPrevajanju {
		if(simTab.containsKey(o)) {
			return getLokSt(o);
		} else {
			int vred = 0;
			try {
				vred = Integer.parseInt(o);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				throw new NapakaPriPrevajanju("Napačna vrednost operanda\n");
			}
			return vred;
		}
	}

}
