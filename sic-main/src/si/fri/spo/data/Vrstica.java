package si.fri.spo.data;

import si.fri.spo.managers.MnetabManager;
import si.fri.spo.utils.Utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Vrstica {
	
	private String mnemonik;
	private String labela;
	private String operand;
	private Integer lokSt;
	private boolean extended = false;
	private boolean posrednoNaslavljanje = false;
	private boolean operandJeLiteral = false;
	
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
		} else if(operand.startsWith("=")) {
			operandJeLiteral = true;
			operand = operand.substring(1);
		}
		this.operand = operand;
	}
	
	@Override
	public String toString() {
		if(!veljavna)
			return ".komentar";
		String vrstica = "";
		
		vrstica += Integer.toHexString(getNaslov()) + " ";
		
		if(labela != null)
			vrstica += labela + " ";
		
		if(extended)
			vrstica += "+";
		
		vrstica += mnemonik + " ";
		
		if(posrednoNaslavljanje)
			vrstica += "@";
		
		if(operand != null)
			vrstica += operand + " ";
		
		if(objektnaKoda != null)
			vrstica += objektnaKoda;
		
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
		+ " " + veljavna + " " + operandJeLiteral;
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
		
		v.setExtended(Boolean.parseBoolean(stolpci[3]));
		
		v.posrednoNaslavljanje = Boolean.parseBoolean(stolpci[4]);
		
		try {
			v.setLokSt(Integer.parseInt(stolpci[5]));
		} catch (NumberFormatException e) {
			//Če se je zgodilo to, je v datoteki null ...
		}
		
		v.veljavna = Boolean.parseBoolean(stolpci[6]);
		
		v.operandJeLiteral = Boolean.parseBoolean(stolpci[7]);
		
		//System.out.println("deserialize: " + prebranaVrstica + " >> " + v.toString());
		
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

	public boolean isOperandJeLiteral() {
		return operandJeLiteral;
	}
}
