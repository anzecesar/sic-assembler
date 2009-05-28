package si.fri.spo.data;

import si.fri.spo.utils.Utils;

public class Literal {
	private int vrednost;
	private int dolzina;
	private int lokacija;
	
	//ker v prvem passu preverjava za LTORG ukaze, je treba shraniti za drugi prehod kje naj dumpa literale
	private int ltorgDump;
	
	public Literal() {
		ltorgDump = -1;
		lokacija = -1;
	}

	public String getVrednost() {
		return Utils.razsiri(vrednost, dolzina * 2);
	}

	public void setVrednost(int vrednost) {
		this.vrednost = vrednost;
	}

	public int getDolzina() {
		return dolzina;
	}

	public void setDolzina(int dolzina) {
		this.dolzina = dolzina;
	}

	public int getLokacija() {
		return lokacija;
	}

	public void setLokacija(int lokacija) {
		this.lokacija = lokacija;
	}

	public int getLtorgDump() {
		return ltorgDump;
	}

	public void setLtorgDump(int ltorgDump) {
		this.ltorgDump = ltorgDump;
	}
	
	
}
