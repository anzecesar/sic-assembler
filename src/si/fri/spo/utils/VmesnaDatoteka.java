package si.fri.spo.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import si.fri.spo.data.Vrstica;

public class VmesnaDatoteka {
	private static VmesnaDatoteka m_instance;
	
	private static final String IME_DATOTEKE = "objektna";
	
	private BufferedWriter writer;
	private BufferedReader reader;
	
	private int stPrebranihVrstic;
	
	private VmesnaDatoteka() {
		stPrebranihVrstic = 0;
	}
	
	public static void init() throws IOException {
		if(m_instance != null)
			return;
		m_instance = new VmesnaDatoteka();
		
		m_instance.writer = new BufferedWriter(new FileWriter(IME_DATOTEKE));
	}
	
	public static VmesnaDatoteka getInstance() {
		if(m_instance == null) {
			m_instance = new VmesnaDatoteka();
		}
		return m_instance;
	}
	
	public void pisi(Vrstica v) throws IOException {
		if(writer == null) {
			if(reader != null) {
				reader.close();
				reader = null;
			}
			//Odpri z append = true :]
			writer = new BufferedWriter(new FileWriter(IME_DATOTEKE, true));
		}
		//pise v vmesno datoteko
		writer.append(v.serialize());
	}
	
	public Vrstica beri() throws IOException {
		if(reader == null) {
			if(writer != null) {
				writer.close();
				writer = null;
			}
			reader = new BufferedReader(new FileReader(IME_DATOTEKE));
		}
		//bere iz vmesne datoteke
		
		String raw =  reader.readLine();
		
		stPrebranihVrstic++;
		return Vrstica.deserialize(raw);
	}
	
	public void reset() {
		//postavi kazalec v datoteki na zacetek
	}
	
	protected void finalize() throws Throwable {
		//Destruktor :]
		writer.close();
		
		super.finalize();
	}
}
