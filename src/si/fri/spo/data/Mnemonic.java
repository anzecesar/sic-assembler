package si.fri.spo.data;

public class Mnemonic {
	public static int BIT_E_3 = 0x1000;
	public static int BIT_P_3 = 0x2000;
	public static int BIT_B_3 = 0x4000;
	public static int BIT_X_3 = 0x8000;
	public static int BIT_I_3 = 0x10000;
	public static int BIT_N_3 = 0x20000;

	public static int BIT_E_4 = 0x100000;
	public static int BIT_P_4 = 0x200000;
	public static int BIT_B_4 = 0x400000;
	public static int BIT_X_4 = 0x800000;
	public static int BIT_I_4 = 0x1000000;
	public static int BIT_N_4 = 0x2000000;
	
	//private String name; - already implemented in the hash table :]
	private int opcode;
	private int format;
	//private byte length;
	
	public Mnemonic(int opcode, int format) {
		this.opcode = opcode;
		this.format = format;
		//this.length = length;
	}
	
	public int getOpcode() {
		return opcode;
	}
	
	public int getShiftedOpCode() {
		if(format == 1)
			return opcode;
		
		if(format == 2)
			return opcode << 8;
		
		return opcode << 16;
		
	}
	
	public void setOpcode(byte optcode) {
		this.opcode = optcode;
	}
	
	public int getFormat() {
		return format;
	}
	
	public void setFormat(byte format) {
		this.format = format;
	}
}
