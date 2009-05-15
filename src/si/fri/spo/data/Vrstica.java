package si.fri.spo.data;

public class Vrstica {
	
	private String mnemonik;
	private String labela;
	private String operand;
	private int lokSt;
	
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
		this.operand = operand;
	}
	
	public String toString() {
		String vrstica = "";
		if(labela != null)
			vrstica += "(l)" + labela + " ";
		vrstica += "(m)" + mnemonik + " ";
		if(operand != null)
			vrstica += "(o)" + operand;
			
		return vrstica;
	}

	public int getLokSt() {
		return lokSt;
	}

	public void setLokSt(int lokSt) {
		this.lokSt = lokSt;
	}
}
