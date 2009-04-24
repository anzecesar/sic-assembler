package si.fri.spo.exceptions;


public class NapakaPriPrevajanju extends Exception {
	private static final long serialVersionUID = -7790799755528542980L;
	private Exception vzrok;
	private String message;

	public NapakaPriPrevajanju(Exception e) {
		this.vzrok = e;
		this.message = "Napaka pri prevajanju!";
	}
	
	public NapakaPriPrevajanju(Exception e, String message) {
		this.vzrok = e;
		this.message = message;
	}

	public Throwable getCause() {
		return vzrok;
	}

	@Override
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

}
