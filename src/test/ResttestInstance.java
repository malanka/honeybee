package test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import engines.EngineBpe;
import engines.EngineBP;
import services.Hole;
import services.PatternBasic;
import services.Template;
import services.Pattern;

public class ResttestInstance {

	public static void main(String[] args) {
		// create a webClient
		Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFilter.class ) );
		// prepare template and patterns
		List<Pattern> list = PreparePatterns(client);
		for (Pattern aPattern : list) {
			testCreateInstanceWithExistingPattern(client, aPattern.getId());
		}
	}
	
	private static List<Pattern> PreparePatterns(Client client) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd_HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		
		ArrayList <Hole> holes= new ArrayList<Hole>();
		Hole hole1 = new Hole("holename1","as","asd","ad","aasd");
		Hole hole2 = new Hole("holename2","as","asd","ad","aasd");
		holes.add(hole1);
		holes.add(hole2);
		EngineBpe engine1 = new EngineBpe(EngineBP.BOONITA7_2, "7908120732971969775", "http://localhost:8080/bonita");
		EngineBpe engine2 = new EngineBpe(EngineBP.BOONITA7_2, "7908120732971969775", "http://localhost:8080/bonita");
		Template template1 = new Template(dateFormat.format(date)+"1", "FirstTemplate"+dateFormat.format(date), "data_inQQ", "data_outQQ", "event_inQQ", "even_outOO", null, engine1);
		Template template2 = new Template(dateFormat.format(date)+"2", "SecondTemplate"+dateFormat.format(date), "data_inQQ", "data_outQQ", "event_inQQ", "even_outOO", holes, engine2);

		WebTarget webTarget1 = client.target("http://localhost:9000/BP_REST_API/rest").path("templates");
		Invocation.Builder invocationBuilder1 =  webTarget1.request(MediaType.APPLICATION_XML);
		Response response1 = invocationBuilder1.post(Entity.entity(template1, MediaType.APPLICATION_XML));
		
		System.out.println(response1.getStatus() == 200);
		
		WebTarget webTarget2 = client.target("http://localhost:9000/BP_REST_API/rest").path("templates");
		Invocation.Builder invocationBuilder2 =  webTarget2.request(MediaType.APPLICATION_XML);
		Response response2 = invocationBuilder2.post(Entity.entity(template2, MediaType.APPLICATION_XML));
		
		System.out.println(response2.getStatus() == 200);
		List<Template> list = new ArrayList<Template>();
		list.add(template1);
		list.add(template2);
		
		PatternBasic patternBasic = new PatternBasic("patternName"+dateFormat.format(date), template1.getId());
		WebTarget webTarget3 = client.target("http://localhost:9000/BP_REST_API/rest").path("patterns");
		Invocation.Builder invocationBuilder3 =  webTarget3.request(MediaType.APPLICATION_XML);
		Response response3 = invocationBuilder3.post(Entity.entity(patternBasic, MediaType.APPLICATION_XML));
		     	     
		System.out.println(response3.getStatus() == 200);
		Pattern pattern1 = response3.readEntity(Pattern.class);
		System.out.println(pattern1);
		
		List<Pattern> listPatterns = new ArrayList<Pattern>();
		listPatterns.add(pattern1);
		return listPatterns;
	}
	
	private static void testCreateInstanceWithExistingPattern(Client client, String patternId) {
		System.out.println("patternId" + patternId);
		WebTarget webTarget = client.target("http://localhost:9000/BP_REST_API/rest").path("instances").queryParam("patternId", patternId);
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_XML);
		Entity<Object> entity = Entity.entity(null, "application/x-ample");
		Response response3 = invocationBuilder.post(entity);
		     
		System.out.println(response3.getStatus() == 200);
		// How to print response
		String output = response3.readEntity(String.class);
		System.out.println(output);
		//InstanceBP instance = response3.readEntity(InstanceBP.class);
		//System.out.println(instance);
		
	}
}