package engines;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;


import businessentities.WS;
import serviceerrors.InternalErrorException;

@XmlRootElement(name = "engine")
public class TestConnector extends EngineBpe implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9058491532660910478L;


	@Override
	public WS getProcessResource() {
		return new WS("test", "GET", null);
	}

	@Override
	public String toString() {
		return "TestConnector";
	}

	@Override
	public GeneralCase generateInstance (String instanceUrl) throws InternalErrorException {
		return new GeneralCase(new Date(), "12456", "OK");	
	}

	public TestConnector() {
		super();
	}

	public TestConnector(String processId, String baseURI) {
		super(EngineBP.TESTCONNECTOR, "someeprocessid", "somebaseURL");
	}

	public TestConnector(EngineBpe engine) {
		super(EngineBP.TESTCONNECTOR, engine.getProcessId(), engine.getBaseURI());
	}

}
