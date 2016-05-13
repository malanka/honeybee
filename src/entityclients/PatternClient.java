package entityclients;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import services.Pattern;
import services.PatternBasic;

public class PatternClient {

	public static final String RESOURCE_PATH = "/patterns";

	private final String uri;

	private final Client client = ClientBuilder.newClient(new ClientConfig().register( LoggingFilter.class ) );

	public PatternClient(String uri) {
		super();
		this.uri = uri;
	}

	// Correct URI
	public Response getPatterns(String mediaTypeOut) {
		WebTarget webTarget = client.target(uri).path(RESOURCE_PATH);
		Invocation.Builder invocationBuilder =  webTarget.request(mediaTypeOut);
		return invocationBuilder.get();
	}
	
	public Response deletePatterns(String mediaTypeOut) {
		WebTarget webTarget = client.target(uri).path(RESOURCE_PATH);
		Invocation.Builder invocationBuilder = webTarget.request(mediaTypeOut);
		return invocationBuilder.delete();
	}
	
	public Response addPattern(PatternBasic patternBasic, String mediaTypeIn, String mediaTypeOut) {
		WebTarget webTarget = client.target(uri).path(RESOURCE_PATH);
		Invocation.Builder invocationBuilder = webTarget.request(mediaTypeOut);
		return invocationBuilder.post(Entity.entity(patternBasic, mediaTypeIn));
	}
	
	public Response getPattern(String patternId, String mediaTypeOut) {
		WebTarget webTarget = client.target(uri).path(RESOURCE_PATH).path(patternId);
		Invocation.Builder invocationBuilder = webTarget.request(mediaTypeOut);
		return invocationBuilder.get();
	}
	
	public Response deletePattern(String patternId, String mediaTypeOut) {
		WebTarget webTarget = client.target(uri).path(RESOURCE_PATH).path(patternId);
		Invocation.Builder invocationBuilder = webTarget.request(mediaTypeOut);
		return invocationBuilder.delete();
	}

}
