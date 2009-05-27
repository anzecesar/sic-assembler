package si.fri.spo.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ObjektnaDatoteka {
	private static final int DOLZINA_ZAPISA = 60;
	private static final String IME_DATOTEKE = "program.obj";
	private static ObjektnaDatoteka m_instance;
	
	private BufferedWriter writer;
	
	private String zacetek;
	private StringBuffer vsebina;
	
	private ObjektnaDatoteka() {
		vsebina = new StringBuffer();
	}
	
	public static ObjektnaDatoteka getInstance() {
		if(m_instance == null)
			m_instance = new ObjektnaDatoteka();
		
		return m_instance;
	}
	
	public static ObjektnaDatoteka init() throws IOException {
		if(m_instance != null)
			return m_instance;
		
		m_instance = new ObjektnaDatoteka();
		m_instance.writer = new BufferedWriter(new FileWriter(IME_DATOTEKE));
		
		return m_instance;
	}
	
	public void pisi(String what, int naslov) throws IOException {		
		if(vsebina.length() == 0) {
			zacetek = "T" + razsiri(naslov, 6);
		}
		System.out.println("w " + what);
		if(vsebina.length() + what.length() > DOLZINA_ZAPISA) {
			flush(naslov);
		}
		
		vsebina.append(what);
	}
	
	private void doPisi(String what) throws IOException {		
		if(writer == null) {
			//Odpri z append = false :]
			writer = new BufferedWriter(new FileWriter(IME_DATOTEKE, false));
		}
		writer.write(what);
	}
	
	public void newLine() throws IOException {
		writer.newLine();
	}
	
	public void pisiZaglavje(int dolzina, String imePrograma, int zacetniNaslov) throws IOException {
		doPisi("H" + imePrograma + " " + razsiri(zacetniNaslov, 6) + razsiri(dolzina, 6));
		newLine();
	}
	
	public void pisiKonecOP(int zacetniNaslov) throws IOException {
		if(vsebina.length() > 0) {
			flush();
		}
		doPisi(ModTabManager.getInstance().getAll());
		
		doPisi("E" + razsiri(zacetniNaslov, 6));
		
		newLine();
	}
	
	private void flush() throws IOException {
		flush(-1);
	}
	
	public void flush(int naslov) throws IOException {
		if(vsebina.length() == 0)
			return;
		
		zacetek += razsiri(vsebina.length()/2, 2);
		doPisi(zacetek + vsebina);
		newLine();
		
		vsebina.setLength(0);
		
		zacetek = "T" + razsiri(naslov, 6);
	}
	
	public void close() throws IOException {
		//zapre reader ali writer :].
		if(writer != null) {
			writer.close();
			writer = null;
		}
	}
	
	private String razsiri(int stevilka, int size) {
		return Utils.razsiri(stevilka, size);
	}
}
