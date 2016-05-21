package entityclients;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import businessentities.HoleManipulation;
import businessentities.PatternBasic;

public class PatternClient {

	public static final String RESOURCE_PATH = "patterns";

	public static final String RESOURCE_HILES_PATH = "holes";

	private final String uri;

	private final Client client = ClientBuilder.newClient(new ClientConfig().register( LoggingFilter.class ) );

	public PatternClient(String uri) {
		super();
		this.uri = uri;
	}

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

	public Response assignPattern(String patternId, String holeName, HoleManipulation holeManipulation, String mediaTypeIn, String mediaTypeOut) {
		WebTarget webTarget = client.target(uri).path(RESOURCE_PATH).path(patternId).path(RESOURCE_HILES_PATH).path(holeName);
		Invocation.Builder invocationBuilder = webTarget.request(mediaTypeOut);
		return invocationBuilder.post(Entity.entity(holeManipulation, mediaTypeIn));
	}

	public Response getHoleList(String patternId, String mediaTypeOut) {
		WebTarget webTarget = client.target(uri).path(RESOURCE_PATH).path(patternId).path(RESOURCE_HILES_PATH);
		Invocation.Builder invocationBuilder = webTarget.request(mediaTypeOut);
		return invocationBuilder.get();
	}

	public Response getOneHole(String patternId, String holeName, String mediaTypeOut) {
		WebTarget webTarget = client.target(uri).path(RESOURCE_PATH).path(patternId).path(RESOURCE_HILES_PATH).path(holeName);
		Invocation.Builder invocationBuilder = webTarget.request(mediaTypeOut);
		return invocationBuilder.get();
	}
}