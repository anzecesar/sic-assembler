package si.fri.spo.sic.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import si.fri.spo.assembler.Assembler;
import si.fri.spo.exceptions.NapakaPriPrevajanju;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;

@UrlBinding("/compile")
public class CompileBean extends AbstractBean {
	@Validate(on="compile", required=true)
	private FileBean sicSrc;
	
	@DefaultHandler
	public Resolution view() {
		return new ForwardResolution("/WEB-INF/pages/compile.jsp");
	}
	
	public Resolution assemble() {
		Assembler a = new Assembler(true);
		try {
			a.assemble(sicSrc.getReader());
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NapakaPriPrevajanju e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new StreamingResolution("text", a.getObj());
	}

	public FileBean getSicSrc() {
		return sicSrc;
	}

	public void setSicSrc(FileBean sicSrc) {
		this.sicSrc = sicSrc;
	}
}
