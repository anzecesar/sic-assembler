package si.fri.spo.data;

public class fileLine {
	
	private String mnemonik;
	private String labela;
	private String operand;
	private boolean comment;
	
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
	public boolean isComment() {
		return comment;
	}
	public void setComment(boolean comment) {
		this.comment = comment;
	}

}
