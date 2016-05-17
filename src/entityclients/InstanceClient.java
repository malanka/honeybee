package entityclients;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import businessentities.InstanceBasic;
import businessentities.InstanceManipulation;

public class InstanceClient {

	public static final String RESOURCE_PATH = "/instances";

	private final String uri;

	private final Client client = ClientBuilder.newClient(new ClientConfig().register( LoggingFilter.class ) );

	public InstanceClient(String uri) {
		super();
		this.uri = uri;
	}

	public Response getInstances(String mediaTypeOut) {
		WebTarget webTarget = client.target(uri).path(RESOURCE_PATH);
		Invocation.Builder invocationBuilder =  webTarget.request(mediaTypeOut);
		return invocationBuilder.get();
	}
	
	public Response deleteInstances(String mediaTypeOut) {
		WebTarget webTarget = client.target(uri).path(RESOURCE_PATH);
		Invocation.Builder invocationBuilder = webTarget.request(mediaTypeOut);
		return invocationBuilder.delete();
	}
	
	public Response addInstance(InstanceBasic instanceBasic, String mediaTypeIn, String mediaTypeOut) {
		WebTarget webTarget = client.target(uri).path(RESOURCE_PATH);
		Invocation.Builder invocationBuilder = webTarget.request(mediaTypeOut);
		return invocationBuilder.post(Entity.entity(instanceBasic, mediaTypeIn));
	}
	
	public Response getInstance(String instanceId, String mediaTypeOut) {
		WebTarget webTarget = client.target(uri).path(RESOURCE_PATH).path(instanceId);
		Invocation.Builder invocationBuilder = webTarget.request(mediaTypeOut);
		return invocationBuilder.get();
	}
	
	public Response deleteInstance(String instanceId, String mediaTypeOut) {
		WebTarget webTarget = client.target(uri).path(RESOURCE_PATH).path(instanceId);
		Invocation.Builder invocationBuilder = webTarget.request(mediaTypeOut);
		return invocationBuilder.delete();
	}

	public Response doAction(String instanceId, InstanceManipulation instanceManipulation, String mediaTypeIn, String mediaTypeOut) {
		WebTarget webTarget = client.target(uri).path(RESOURCE_PATH).path(instanceId);
		Invocation.Builder invocationBuilder = webTarget.request(mediaTypeOut);
		return invocationBuilder.put(Entity.entity(instanceManipulation, mediaTypeIn));
	}
	
	public Response startHole(String instanceId, String holeName, String mediaTypeIn, String mediaTypeOut) {
		WebTarget webTarget = client.target(uri).path(RESOURCE_PATH).path(instanceId).path("holes").path(holeName);
		Invocation.Builder invocationBuilder = webTarget.request(mediaTypeOut);
		return invocationBuilder.post(Entity.entity(null, mediaTypeIn));
	}
}
