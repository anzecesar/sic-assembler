package si.fri.spo.data;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Vrstica {
	
	private String mnemonik;
	private String labela;
	private String operand;
	private Integer lokSt;
	private boolean extended = false;
	
	public boolean hasMnemonik() {
		if(mnemonik == null)
			return false;
		return true;
	}
	
	public String getMnemonik() {
		return mnemonik;
	}
	public void setMnemonik(String mnemonik) {
		this.mnemonik = mnemonik;
	}
	public String getLabela() {
		return labela;
	}
	public void setLabela(String labela) {
		this.labela = labela;
	}
	public String getOperand() {
		return operand;
	}
	public void setOperand(String operand) {
		if(operand.startsWith("+")) {
			extended = true;
			//odstrani +:
			operand = operand.substring(1);
		}
		this.operand = operand;
	}
	
	public String toString() {
		String vrstica = "";
		if(labela != null)
			vrstica += "(l)" + labela + " ";
		vrstica += "(m)" + mnemonik + " ";
		if(operand != null)
			vrstica += "(o)" + operand;
		if(extended)
			vrstica += " [extended]";
		
		if(lokSt != null)
			vrstica += "	(s)" + Integer.toHexString(lokSt);
			
		return vrstica;
	}
	
	public String serialize() {
		//Sestavi vrstico v primerno obliko za zapis v vmesno datoteko
		String l = labela;
		if(l == null)
			l = "N";
		
		String o = operand;
		if(o == null)
			o = "N";
		
		//Tu so lahko kaki zlobni zanki, kar nam ni vsec, zato enkodiramo...
		o = Base64.encode(o.getBytes());
		
		return l + " " + mnemonik + " " + o + " " + extended + " " + lokSt;
	}
	
	public static Vrstica deserialize(String prebranaVrstica) {
		if(prebranaVrstica == null)
			return null;
		
		String[] stolpci = prebranaVrstica.split("\\s+");
		Vrstica v = new Vrstica();
		
		if(!"N".equals(stolpci[0]))
			v.setLabela(stolpci[0]);
		
		v.setMnemonik(stolpci[1]);
		
		//Dekodiranje operanda
		stolpci[2] = new String(Base64.decode(stolpci[2]));
		if(!"N".equals(stolpci[2]))
			v.setOperand(stolpci[2]);
		
		v.setExtended(Boolean.getBoolean(stolpci[3]));
		
		v.setLokSt(Integer.parseInt(stolpci[4]));
		
		return v;
	}

	public int getLokSt() {
		return lokSt;
	}

	public void setLokSt(int lokSt) {
		this.lokSt = lokSt;
	}

	public boolean isExtended() {
		return extended;
	}

	public void setExtended(boolean extended) {
		this.extended = extended;
	}
}
