package si.fri.spo.data;

public class Mnemonic {
	//private String name; - already implemented in the hash table :]
	private int optcode;
	private int format;
	//private byte length;
	
	public Mnemonic(int optcode, int format) {
		this.optcode = optcode;
		this.format = format;
		//this.length = length;
	}
	
	public int getOptcode() {
		return optcode;
	}
	public void setOptcode(byte optcode) {
		this.optcode = optcode;
	}
	public int getFormat() {
		return format;
	}
	public void setFormat(byte format) {
		this.format = format;
	}
/*
	public byte getLength() {
		return length;
	}
	public void setLength(byte length) {
		this.length = length;
	}
*/	
}
