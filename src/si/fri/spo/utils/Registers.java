package si.fri.spo.utils;

import java.util.HashMap;
import java.util.Map;

public class Registers {
	private Registers m_registers;
	
	private Map<String, Integer> registers;
	
	private Registers() {
		registers = new HashMap<String, Integer>();
		
		//Init registers table :]
		registers.put("A", 0x0);
		registers.put("X", 0x1);
		registers.put("L", 0x2);
		registers.put("PC", 0x8);
		registers.put("SW", 0x9);
		registers.put("B", 0x3);
		registers.put("S", 0x4);
		registers.put("T", 0x5);
		registers.put("F", 0x6);
	}
	
	public Registers getInstance() {
		if(m_registers == null) {
			m_registers = new Registers();
		}
		return m_registers;
	}

	public Map<String, Integer> getRegisters() {
		return registers;
	}
}
