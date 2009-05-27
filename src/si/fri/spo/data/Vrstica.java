package si.fri.spo.data;

import si.fri.spo.utils.MnetabManager;
import si.fri.spo.utils.Utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Vrstica {
	
	private String mnemonik;
	private String labela;
	private String operand;
	private Integer lokSt;
	private boolean extended = false;
	private boolean posrednoNaslavljanje = false;
	
	private String objektnaKoda;
	
	private boolean veljavna = true; //zaradi pravilnega oštevilčenja vrstic.
	
	public boolean hasMnemonik() {
		if(mnemonik == null)
			return false;
		return true;
	}
	
	public String getMnemonik() {
		return mnemonik;
	}
	public void setMnemonik(String mnemonik) {
		if(mnemonik.startsWith("+")) {
			extended = true;
			mnemonik = mnemonik.substring(1);
		}
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
		if(operand.startsWith("@")) {
			posrednoNaslavljanje = true;
			operand = operand.substring(1);
		}
		this.operand = operand;
	}
	
	@Override
	public String toString() {
		if(!veljavna)
			return "Vrstica je komentar/prazna.";
		String vrstica = "";
		if(labela != null)
			vrstica += "(l)" + labela + " ";
		vrstica += "(m)" + mnemonik + " ";
		if(operand != null)
			vrstica += "(o)" + operand;
		if(extended)
			vrstica += " [extended]";
		if(posrednoNaslavljanje)
			vrstica += " [posredno naslovljen]";
		
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
		
		return l + " " + mnemonik + " " + o + " " + extended + " " + posrednoNaslavljanje + " " + lokSt
		+ " " + veljavna;
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
		
		v.posrednoNaslavljanje = Boolean.getBoolean(stolpci[4]);
		
		v.setLokSt(Integer.parseInt(stolpci[5]));
		
		v.setVeljavna(Boolean.getBoolean(stolpci[6]));
		
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

	public boolean isPosrednoNaslavljanje() {
		return posrednoNaslavljanje;
	}

	public boolean isVeljavna() {
		return veljavna;
	}

	public void setVeljavna(boolean veljavna) {
		this.veljavna = veljavna;
	}

	public String getObjektnaKoda() {
		return objektnaKoda;
	}
	
	public void setObjektnaKoda(String objektnaKoda) {
		this.objektnaKoda = objektnaKoda;
	}

	public void setObjektnaKoda(int objektnaKoda) {
		int format = MnetabManager.getInstance().getFormat(mnemonik);
		switch( format ) {
			case 1:
				this.objektnaKoda = Utils.razsiri(objektnaKoda, 2);
			break;
			case 2:
				this.objektnaKoda = Utils.razsiri(objektnaKoda, 4);
			break;
			case 3:
				if(!extended)
					this.objektnaKoda = Utils.razsiri(objektnaKoda, 6);
				else
					this.objektnaKoda = Utils.razsiri(objektnaKoda, 8);
			break;
		}
	}
	
	public int getNaslov() {
		int format = MnetabManager.getInstance().getFormat(mnemonik);
		int naslov = 0;
		switch( format ) {
			case 1:
				naslov = lokSt - 1;
			break;
			case 2:
				naslov = lokSt - 2;
			break;
			case 3:
				if(!extended)
					naslov = lokSt - 3;
				else
					naslov = lokSt - 4;
			break;
		}
		return naslov;
	}
}
