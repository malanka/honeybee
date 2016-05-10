package engines;

import java.io.Serializable;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import serviceerrors.InternalErrorException;
import services.WS;

@XmlRootElement(name = "engine")
public class BonitaConnector7_2 extends Engine implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5489914726421205854L;

	private static final String relativeProcessURI = "/API/bpm/process/";

	private static final String relativeInstanceURI = "/API/bpm/case/";

	private static final String loginURI = "/loginservice";

	@Override
	public WS getProcessResource() {
		if ( getProcessId() == null || getBaseURI() == null )
			return null;
		return new WS(getBaseURI() + relativeProcessURI + getProcessId(), "GET", null);
	}

	private WS startProcess(String instanceId) {
		if ( getProcessId() == null || getBaseURI() == null )
			return null;
		String requestDocument = "{ \"processDefinitionId\":" + getProcessId() + ",\"variables\":[{\"name\":\"callerid\", \"value\":\"" + instanceId + "\"}]}";
		return new WS(getBaseURI() + relativeInstanceURI, "POST", requestDocument);
	}

	@Override
	public String toString() {
		return "BonitaConnector7_2 [processId=" + getProcessId() + ", baseURI=" + getBaseURI() + "]";
	}
	@Override
	public GeneralCase generateInstance (String instanceId) throws InternalErrorException {
		Map<String, NewCookie> cookies = authorize();
		if ( cookies == null ) {
			throw new InternalErrorException ("Cookies are missing");
		}
		BonitaCase bonitaCase = createCase(instanceId, cookies);
		if ( bonitaCase == null ) {
			throw new InternalErrorException ("Insttance was not created");
		}
		// TODO logout service
		return new GeneralCase(bonitaCase.startDateCorrect(), bonitaCase.getId(), bonitaCase.getState());		
	}

	private BonitaCase createCase(String instanceId, Map<String, NewCookie> cookies) throws InternalErrorException {
		Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFilter.class ) );
		WS startInstance = this.startProcess(instanceId);
		WebTarget webTarget = client.target(startInstance.getUri());
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		for (Map.Entry<String, NewCookie> entry : cookies.entrySet() ) {
			invocationBuilder = invocationBuilder.cookie(entry.getValue());
		}
		Response response = invocationBuilder.post(Entity.json(startInstance.getRequestDocument()));
		if ( response.getStatus() != 200 ) {
			throw new InternalErrorException (response.readEntity(String.class));
		}
		return response.readEntity(BonitaCase.class);
	}

	private Map<String, NewCookie> authorize() throws InternalErrorException {
		/* How cookie should look like:
		 * 
		 * JSESSIONID=86D31D2C4E8ECAE96715FE438D5196D1; X-Bonita-API-Token=80379964-771a-44e8-ba1f-825003a4f4ca; BOS_Locale=en
		 */
		Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFilter.class ) );
		WebTarget webTargetAuth = client.target( getBaseURI() + loginURI);
		Invocation.Builder invocationBuilderAuth =  webTargetAuth.request();
		MultivaluedMap<String, String> authMap = new MultivaluedHashMap<>();
		// All three values are mandatory
		// For now login+passwd are just hard coded
		authMap.add("username", "alena");
		authMap.add("password", "11111");
		authMap.add("redirect", "false");
		Response responseAuth = invocationBuilderAuth.post(Entity.entity(authMap, MediaType.APPLICATION_FORM_URLENCODED));
		String responseText = responseAuth.readEntity(String.class);
		if ( !responseText.isEmpty() ) {
			throw new InternalErrorException("Cannot authenticate");
		}
		return responseAuth.getCookies();
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
