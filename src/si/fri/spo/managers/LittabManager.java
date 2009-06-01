package si.fri.spo.managers;

import java.util.HashMap;
import java.util.Map;

import si.fri.spo.data.Literal;
import si.fri.spo.utils.Utils;

public class LittabManager {

	private static LittabManager m_manager;
	private int ltorgIter = 0;
	 
	private Map<String, Literal> littab;
	
	private LittabManager() {
		littab = new HashMap<String, Literal>();
	}
	
	public static LittabManager getInstance() {
		if(m_manager == null)
			m_manager = new LittabManager();
		return m_manager;
	}
	
	public void dodajLiteral(String ime, String vrednost) {
		Literal l = new Literal();
		l.setDolzina(Utils.getStBajtov(vrednost));
		l.setVrednost(Utils.pretvoriOperand(vrednost));
		littab.put(vrednost, l);
	}
	
	public int naslovLiterala(String ime) {
		return littab.get(ime).getLokacija();
	}
	
	public int obdelajLtorg(int lokSt) {
		for (Literal l : littab.values()) {
			if(l.getLokacija() == -1) {
				l.setLokacija(lokSt);
				lokSt += l.getDolzina();
				l.setLtorgDump(ltorgIter);
			}
		}
		ltorgIter++;
		return lokSt;
	}
	
	/**
	 * Obvezen klic pred drugim prehodom! :]
	 */
	public void resetirajStevec() {
		ltorgIter = 0;
	}
	
	public String flushLtorg() {
		String ret = "";
		
		for (Literal l : littab.values()) {
			if(l.getLtorgDump() == ltorgIter) {
				//Če litral pripada ukazu, ki je bil najden že v prvem prehodu...
				ret += l.getVrednost();
			}
		}
		ltorgIter++;
		return ret;
	}
	
	public void clear() {
		littab.clear();
	}
}
