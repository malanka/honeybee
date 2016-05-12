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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import engines.EngineBpe;
import engines.EngineBP;
import services.Hole;
import services.PatternBasic;
import services.Template;
import services.WS;
import services.Pattern;

public class ResttestPattern {

	public static void main(String[] args) {
		// create file !
		Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFilter.class ) );
		WebTarget webTarget0 = client.target("http://localhost:9000/BP_REST_API/rest").path("patterns");
		Invocation.Builder invocationBuilder0 =  webTarget0.request(MediaType.APPLICATION_JSON);
		Response response0 = invocationBuilder0.get();
		System.out.println(response0.getStatus() == 200);

		List<Template> list = PrepareTemplates(client);
		for (Template aTemplate : list) {
			testCreatePatternWithExistingTemplate(client, aTemplate.getId(), "Pattern"+aTemplate.getId());
		}
	}
	
	private static List<Template> PrepareTemplates(Client client) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd_HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));

		ArrayList <Hole> holes= new ArrayList<Hole>();
		Hole hole1 = new Hole("holename1","as","asd","ad","aasd");
		Hole hole2 = new Hole("holename2","as","asd","ad","aasd");
		holes.add(hole1);
		holes.add(hole2);
		EngineBpe engine1 = new EngineBpe(EngineBP.BOONITA7_2,"1234561", "somebaseuri");
		EngineBpe engine2 = new EngineBpe(EngineBP.BOONITA7_2,"1234562", "somebaseuri");
		Template template1 = new Template(dateFormat.format(date)+"1", "FirstTemplate"+dateFormat.format(date), "data_inQQ", "data_outQQ", "event_inQQ", "even_outOO", null, engine1);
		Template template2 = new Template(dateFormat.format(date)+"2", "SecondTemplate"+dateFormat.format(date), "data_inQQ", "data_outQQ", "event_inQQ", "even_outOO", holes,engine2);

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
		return list;
	}
	
	private static void testCreatePatternWithExistingTemplate(Client client, String templateId, String patternName) {		
		PatternBasic patternBasic = new PatternBasic(patternName);
		
		ObjectMapper mapper = new ObjectMapper();
		//Object to JSON in String
		try {
			String jsonInString = mapper.writeValueAsString(patternBasic);
			System.out.println(jsonInString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		WebTarget webTarget3 = client.target("http://localhost:9000/BP_REST_API/rest").path("patterns").queryParam("templateId", templateId);
		Invocation.Builder invocationBuilder3 =  webTarget3.request(MediaType.APPLICATION_XML);
		Response response3 = invocationBuilder3.post(Entity.entity(patternBasic, MediaType.APPLICATION_XML));
		     
		System.out.println(response3.getStatus() == 200);
		Pattern pattern = response3.readEntity(Pattern.class);
		System.out.println(pattern);
		
	}
}