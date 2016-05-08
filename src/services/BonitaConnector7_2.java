package services;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "engine")
public class BonitaConnector7_2 extends Engine implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5489914726421205854L;

	private static final String relativeProcessURI = "/API/bpm/process/";

	private static final String relativeInstanceURI = "/API/bpm/case/";
	
	WS getProcessResource() {
		if ( getProcessId() == null || getBaseURI() == null )
			return null;
		return new WS(getBaseURI() + relativeProcessURI + getProcessId(), "GET", null);
	}

	WS startProcess() {
		if ( getProcessId() == null || getBaseURI() == null )
			return null;
		String requestDocument = "{ \"processDefinitionId\"" + getProcessId() + "}";
		return new WS(getBaseURI() + relativeInstanceURI, "POST", requestDocument);
	}

	@Override
	public String toString() {
		return "BonitaConnector7_2 [processId=" + getProcessId() + ", baseURI=" + getBaseURI() + "]";
	}

	public BonitaConnector7_2() {
		super();
	}
	
	public BonitaConnector7_2(String processId, String baseURI) {
		super(EngineBP.BOONITA7_2, processId, baseURI);
	}

	public BonitaConnector7_2(Engine engine) {
		super(EngineBP.BOONITA7_2, engine.getProcessId(), engine.getBaseURI());
	}
}
