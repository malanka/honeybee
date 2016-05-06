package test;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import services.Hole;
import services.PatternBasic;
import services.Template;
import services.WS;
import services.Pattern;

public class ResttestPattern {

	public static void main(String[] args) {
		// create file !
		Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFilter.class ) );
		WebTarget webTarget0 = client.target("http://localhost:8080/BP_REST_API/rest").path("patterns");
		Invocation.Builder invocationBuilder0 =  webTarget0.request(MediaType.APPLICATION_JSON);
		Response response0 = invocationBuilder0.get();
		System.out.println(response0.getStatus() == 200);

		PrepareTemplates(client);
		testCreatePatternWithExistingTemplate(client, "1", "Pattern1");
		testCreatePatternWithExistingTemplate(client, "2", "Pattern2");
/*		
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

	
	private static void PrepareTemplates(Client client) {
		WS ws1 = new WS("SOME_URI1","POST");
		WS ws2 = new WS("SOME_URI2","POST");
		ArrayList <Hole> holes= new ArrayList<Hole>();
		Hole hole1 = new Hole("holename1","as","asd","ad","aasd");
		Hole hole2 = new Hole("holename2","as","asd","ad","aasd");
		holes.add(hole1);
		holes.add(hole2);
		Template template1 = new Template("1", "FirstTemplate", "data_inQQ", "data_outQQ", "event_inQQ", "even_outOO", null, ws1);
		Template template2 = new Template("2", "SecondTemplate", "data_inQQ", "data_outQQ", "event_inQQ", "even_outOO", holes, ws2);

		WebTarget webTarget1 = client.target("http://localhost:8080/BP_REST_API/rest").path("templates");
		Invocation.Builder invocationBuilder1 =  webTarget1.request(MediaType.APPLICATION_XML);
		Response response1 = invocationBuilder1.post(Entity.entity(template1, MediaType.APPLICATION_XML));
		
		System.out.println(response1.getStatus() == 200);
		
		WebTarget webTarget2 = client.target("http://localhost:8080/BP_REST_API/rest").path("templates");
		Invocation.Builder invocationBuilder2 =  webTarget2.request(MediaType.APPLICATION_XML);
		Response response2 = invocationBuilder2.post(Entity.entity(template2, MediaType.APPLICATION_XML));
		
		System.out.println(response2.getStatus() == 200);
	}
	
	private static void testCreatePatternWithExistingTemplate(Client client, String templateId, String patternName) {


		
		PatternBasic patternBasic = new PatternBasic(patternName);
		WebTarget webTarget3 = client.target("http://localhost:8080/BP_REST_API/rest").path("patterns").queryParam("templateId", templateId);
		Invocation.Builder invocationBuilder3 =  webTarget3.request(MediaType.APPLICATION_XML);
		Response response3 = invocationBuilder3.post(Entity.entity(patternBasic, MediaType.APPLICATION_XML));

		     
		System.out.println(response3.getStatus() == 200);
		Pattern pattern = response3.readEntity(Pattern.class);
		System.out.println(pattern);
		
	}
}