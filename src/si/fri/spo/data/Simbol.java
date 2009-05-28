package si.fri.spo.data;

public class Simbol {
	private Integer vrednost;
	private boolean equ;
	
	public Simbol(Integer v) {
		vrednost = v;
		equ = false;
	}
	
	public Simbol(Integer v, boolean e) {
		vrednost = v;
		equ = e;
	}
	
	public Integer getVrednost() {
		return vrednost;
	}
	public void setVrednost(Integer vrednost) {
		this.vrednost = vrednost;
	}
	public boolean isEqu() {
		return equ;
	}
	public void setEqu(boolean equ) {
		this.equ = equ;
	}
}
