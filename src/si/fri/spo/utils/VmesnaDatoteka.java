package si.fri.spo.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import si.fri.spo.data.Vrstica;

public class VmesnaDatoteka {
	private static VmesnaDatoteka m_instance;
	
	private static final String IME_DATOTEKE = "/tmp/objektna";
	
	private boolean inMemory;
	private List<Vrstica> vmesnaDatoteka;
	
	private BufferedWriter writer;
	private BufferedReader reader;
	
	private int stPrebranihVrstic;
	
	private VmesnaDatoteka() {
		stPrebranihVrstic = 0;
	}
	
	public static void init(boolean inMemory) throws IOException {
		if(m_instance != null)
			return;
		m_instance = new VmesnaDatoteka();
		
		m_instance.inMemory = inMemory;
		if(!inMemory)
			m_instance.writer = new BufferedWriter(new FileWriter(IME_DATOTEKE));
		else
			m_instance.vmesnaDatoteka = new Vector<Vrstica>();
		
	}
	
	public static VmesnaDatoteka getInstance() {
		if(m_instance == null) {
			m_instance = new VmesnaDatoteka();
		}
		return m_instance;
	}
	
	public void pisi(Vrstica v) throws IOException {
		if(inMemory) {
			vmesnaDatoteka.add(v);
			return;
		}
		
		if(writer == null) {
			close();
			//Odpri z append = true :]
			writer = new BufferedWriter(new FileWriter(IME_DATOTEKE, true));
		}
		//pise v vmesno datoteko
		//System.out.println(v.serialize());
		writer.write(v.serialize());
		writer.newLine();
		//writer.flush();
	}
	
	public Vrstica beri() throws IOException {
		if(inMemory) {
			try {
				return vmesnaDatoteka.get(stPrebranihVrstic++);
			} catch (ArrayIndexOutOfBoundsException e) {
				//Dosegli smo konec datoteke.
				return null;
			}
		}
		
		if(reader == null) {
			close();
			reader = new BufferedReader(new FileReader(IME_DATOTEKE));
		}
		//bere iz vmesne datoteke
		
		String raw =  reader.readLine();
		
		stPrebranihVrstic++;
		return Vrstica.deserialize(raw);
	}
	
	public void close() throws IOException {
		//zapre reader ali writer :].
		if(writer != null) {
			writer.close();
			writer = null;
		}
		if(reader != null) {
			stPrebranihVrstic = 0;
			reader.close();
			reader = null;
		}
	}
}
