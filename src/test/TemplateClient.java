package test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import services.Template;

public class TemplateClient {

	public static final String RESOURCE_PATH = "/templates";

	private final String uri;

	private final Client client = ClientBuilder.newClient(new ClientConfig().register( LoggingFilter.class ) );

	public TemplateClient(String uri) {
		super();
		this.uri = uri;
	}

	public Response getTemplates(String mediaType) {
		WebTarget webTarget = client.target(uri).path(RESOURCE_PATH);
		Invocation.Builder invocationBuilder =  webTarget.request(mediaType);
		return invocationBuilder.get();
	}
	
	public Response deleteTemplates(String mediaType) {
		WebTarget webTarget = client.target(uri).path(RESOURCE_PATH);
		Invocation.Builder invocationBuilder = webTarget.request(mediaType);
		return invocationBuilder.delete();
	}
	
	public Response addTemplate(Template template, String mediaTypeIn, String mediaTypeOut) {
		WebTarget webTarget = client.target(uri).path(RESOURCE_PATH);
		Invocation.Builder invocationBuilder = webTarget.request(mediaTypeOut);
		return invocationBuilder.post(Entity.entity(template, mediaTypeIn));
	}
}
