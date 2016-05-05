package test;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import services.Template;

public class ResttestPattern {

	public static void main(String[] args) {
		// create file !
		Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFilter.class ) );
		WebTarget webTarget0 = client.target("http://localhost:8080/BP_REST_API/rest").path("patterns");
		Invocation.Builder invocationBuilder0 =  webTarget0.request(MediaType.APPLICATION_XML);
		Response response0 = invocationBuilder0.get();

		/*

		WebTarget webTarget = client.target("http://localhost:8080/BP_REST_API/rest").path("templates").path("2");
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_XML);
		Response response = invocationBuilder.get();
		Template template = response.readEntity(Template.class);
		     
		System.out.println(response.getStatus());
		System.out.println(template);
		
		
		template.setId("qweqwe");
		System.out.println("t_new:" + template);
		WebTarget webTarget2 = client.target("http://localhost:8080/BP_REST_API/rest").path("templates");
		Invocation.Builder invocationBuilder2 =  webTarget2.request(MediaType.APPLICATION_XML);
		Response response2 = invocationBuilder2.post(Entity.entity(template, MediaType.APPLICATION_XML));

		System.out.println(response2.getStatus());
		System.out.println(response2.readEntity(Template.class));
		
		template.setData_in("test_data_NEW");
		testUpdateTemplate(client, "qweqwe", template);
		testDeleteTemplate(client, "4564", 404, "Element not found");
		testDeleteTemplate(client, "qweqwe", 204, "");
		*/
	}
	/*
	private static void testDeleteTemplate(Client client, String id, int expectedStatus, String expectedResponse) {
		WebTarget webTarget = client.target("http://localhost:8080/BP_REST_API/rest").path("templates").path(id);
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.TEXT_PLAIN);
		Response response = invocationBuilder.delete();
		String msgtext = response.readEntity(String.class);
		     
		System.out.println(( response.getStatus() == expectedStatus) + " " + "real:" + response.getStatus() +" expected:" + expectedStatus);
		System.out.println((msgtext.toString() == expectedResponse) + " " + "real:'" + msgtext + "' expected:'" + expectedResponse + "'");
		
		WebTarget webTarget2 = client.target("http://localhost:8080/BP_REST_API/rest").path("templates").path(id);
		Invocation.Builder invocationBuilder2 =  webTarget2.request(MediaType.APPLICATION_XML);
		Response response2 = invocationBuilder2.get();

		System.out.println(response2.getStatus() == 404);
	}
	
	private static void testUpdateTemplate(Client client, String id, Template template) {
		int expectedStatus = 200;
		WebTarget webTarget = client.target("http://localhost:8080/BP_REST_API/rest").path("templates").path(id);
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_XML);
		Response response = invocationBuilder.put(Entity.entity(template, MediaType.APPLICATION_XML));
		Template realTemplate = response.readEntity(Template.class);
		     
		System.out.println(( response.getStatus() == expectedStatus) + " " + "real:" + response.getStatus() +" expected:" + expectedStatus);
		System.out.println((realTemplate == template) + " " + "real:'" + realTemplate + "' expected:'" + template + "'");
		
		WebTarget webTarget2 = client.target("http://localhost:8080/BP_REST_API/rest").path("templates").path(id);
		Invocation.Builder invocationBuilder2 =  webTarget2.request(MediaType.APPLICATION_XML);
		Response response2 = invocationBuilder2.get();

		System.out.println(response2.getStatus() == 200);
	}
	*/
}