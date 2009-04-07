package si.fri.spo.data;

public class Mnemonic {
	//private String name; - already implemented in the hash table :]
	private byte optcode;
	private byte format;
	private byte length;
	
	public Mnemonic(byte optcode, byte format, byte length) {
		this.optcode = optcode;
		this.format = format;
		this.length = length;
	}
	
	public byte getOptcode() {
		return optcode;
	}
	public void setOptcode(byte optcode) {
		this.optcode = optcode;
	}
	public byte getFormat() {
		return format;
	}
	public void setFormat(byte format) {
		this.format = format;
	}
	public byte getLength() {
		return length;
	}
	public void setLength(byte length) {
		this.length = length;
	}
	
}
