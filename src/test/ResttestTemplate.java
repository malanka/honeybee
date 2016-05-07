package test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import services.Engine;
import services.EngineBP;
import services.Hole;
import services.Template;
import services.WS;

public class ResttestTemplate {

	public static void main(String[] args) {
		// create a webClient
		Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFilter.class ) );

		DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd_HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		
		ArrayList <Hole> holes= new ArrayList<Hole>();
		Hole hole1 = new Hole("holename1","as","asd","ad","aasd");
		Hole hole2 = new Hole("holename2","as","asd","ad","aasd");
		holes.add(hole1);
		holes.add(hole2);
		Engine engine1 = new Engine(EngineBP.BOONITA7_2,"1234561", "somebaseuri");
		Engine engine2 = new Engine(EngineBP.BOONITA7_2,"1234562", "somebaseuri");
		Template template1 = new Template(dateFormat.format(date)+"1", "FirstTemplate"+dateFormat.format(date), "data_inQQ", "data_outQQ", "event_inQQ", "even_outOO", null, engine1);
		Template template2 = new Template(dateFormat.format(date)+"2", "SecondTemplate"+dateFormat.format(date), "data_inQQ", "data_outQQ", "event_inQQ", "even_outOO", holes, engine2);

		ObjectMapper mapper = new ObjectMapper();
		//Object to JSON in String
		try {
			String jsonInString = mapper.writeValueAsString(template1);
			System.out.println(jsonInString);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		testCreateTemplateOK(client,template1);
		testCreateTemplateOK(client,template2);
	
		template1.setData_in("test_data_NEW");
		testUpdateTemplateOK(client, template1.getId(), template1);
		testDeleteTemplateOK(client, template1.getId());
		testDeleteTemplateNotOK(client, "4564");
	}
	
	private static void testCreateTemplateOK(Client client, Template template) {
		WebTarget webTarget1 = client.target("http://localhost:8080/BP_REST_API/rest").path("templates");
		Invocation.Builder invocationBuilder1 =  webTarget1.request(MediaType.APPLICATION_XML);
		Response response1 = invocationBuilder1.post(Entity.entity(template, MediaType.APPLICATION_XML));
		
		System.out.println(( response1.getStatus() == 200) + " " + "real:" + response1.getStatus() +" expected:" + 200);
		Template newTemplate = response1.readEntity(Template.class);
		System.out.println("newId="+newTemplate.getId());
		
		WebTarget webTarget2 = client.target("http://localhost:8080/BP_REST_API/rest").path("templates").path(newTemplate.getId());
		Invocation.Builder invocationBuilder2 =  webTarget2.request(MediaType.APPLICATION_JSON);
		Response response2 = invocationBuilder2.get();

		System.out.println(( response2.getStatus() == 200) + " " + "real:" + response2.getStatus() +" expected:" + 200);
	}
	
	private static void testDeleteTemplateOK(Client client, String id) {
		WebTarget webTarget = client.target("http://localhost:8080/BP_REST_API/rest").path("templates").path(id);
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_XML);
		Response response = invocationBuilder.delete();
		     
		System.out.println(( response.getStatus() == 204) + " " + "real:" + response.getStatus() +" expected:" + 204);
		
		WebTarget webTarget2 = client.target("http://localhost:8080/BP_REST_API/rest").path("templates").path(id);
		Invocation.Builder invocationBuilder2 =  webTarget2.request(MediaType.APPLICATION_JSON);
		Response response2 = invocationBuilder2.get();

		System.out.println(( response2.getStatus() == 404) + " " + "real:" + response2.getStatus() +" expected:" + 404);
	}
	
	private static void testDeleteTemplateNotOK(Client client, String id) {
		WebTarget webTarget = client.target("http://localhost:8080/BP_REST_API/rest").path("templates").path(id);
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_XML);
		Response response = invocationBuilder.delete();
		     
		System.out.println(( response.getStatus() == 404) + " " + "real:" + response.getStatus() +" expected:" + 404);
	}
	
	private static void testUpdateTemplateOK(Client client, String id, Template template) {
		int expectedStatus = 200;
		WebTarget webTarget = client.target("http://localhost:8080/BP_REST_API/rest").path("templates").path(id);
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_XML);
		Response response = invocationBuilder.put(Entity.entity(template, MediaType.APPLICATION_XML));

		     
		System.out.println(( response.getStatus() == expectedStatus) + " " + "real:" + response.getStatus() +" expected:" + expectedStatus);
		Template realTemplate = response.readEntity(Template.class);
		System.out.println(realTemplate.equals(template) + " " + "real:'" + realTemplate + "' expected:'" + template + "'");
		
		WebTarget webTarget2 = client.target("http://localhost:8080/BP_REST_API/rest").path("templates").path(id);
		Invocation.Builder invocationBuilder2 =  webTarget2.request(MediaType.APPLICATION_JSON);
		Response response2 = invocationBuilder2.get();

		System.out.println(response2.getStatus() == 200);
	}
	
}