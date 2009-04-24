package si.fri.spo.assembler;

import java.util.List;
import java.util.Vector;

import si.fri.spo.data.Vrstica;

public class PrebranaDatoteka {
	private List<Vrstica> datoteka;
	
	public PrebranaDatoteka() {
		datoteka = new Vector<Vrstica>();
	}
	
	public void dodajVrstico(Vrstica vrstica) {
		if(vrstica != null)
			datoteka.add(vrstica);
	}
	
	public List<Vrstica> getVrstice() {
		return datoteka;
	}
}
