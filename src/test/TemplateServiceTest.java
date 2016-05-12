package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import engines.EngineBP;
import engines.EngineBpe;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import services.Hole;
import services.Template;

public class TemplateServiceTest {

	private static final String baseURI = "http://localhost:9000/BP_REST_API/rest";
	
	private static TemplateClient clientTemplate = null;

	@BeforeClass
	public static void setUp() throws Exception {
		System.out.println("setup");
		clientTemplate = new TemplateClient(baseURI);
		clientTemplate.deleteTemplates(MediaType.APPLICATION_JSON);
	}
	
	@After
	public void setAfter() throws Exception {
		System.out.println("setupAfter");
		clientTemplate.deleteTemplates(MediaType.APPLICATION_JSON);
	}
	
	private void testTemplateListEmpty(String mediaType) {
		Response response = clientTemplate.getTemplates(mediaType);
		assertTrue(response.getStatus() == 200);
		try {
			List<Template> templates = response.readEntity(new GenericType<List<Template>>(){});
			assertNotNull(templates);
			assertTrue(templates.isEmpty());
			/*
			String templates = response.readEntity(String.class);
			System.out.println(templates);
			*/
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaType);
		}
	}
	
	@Test
	public void testListEmpty() {
		testTemplateListEmpty(MediaType.APPLICATION_JSON);
		testTemplateListEmpty(MediaType.APPLICATION_XML);
	}
	
	
	private void testTemplateAddOk(Template template, String mediaTypeIn, String mediaTypeOut) {
		Response response = clientTemplate.addTemplate(template, mediaTypeIn, mediaTypeOut);
		assertTrue(response.getStatus() == 200);
		try {
			Template templateNew = response.readEntity(new GenericType<Template>(){});
			assertNotNull(templateNew);
			assertTrue(template.compareWith(template));
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeIn + " and " + mediaTypeOut);
		}
	}
	
	@Test
	public void testAdd() {
		EngineBpe engine = new EngineBpe(EngineBP.BOONITA7_2, "7908120732971969775", "http://localhost:8080/bonita");
		ArrayList <Hole> holes= new ArrayList<Hole>();
		Hole hole1 = new Hole("holename1","as","asd","ad","aasd");
		Hole hole2 = new Hole("holename2","as","asd","ad","aasd");
		holes.add(hole1);
		holes.add(hole2);
		Template template1 = new Template("1", "Fisrt", "datain1","dataout1","event1s","event1e", holes, engine);
		Template template2 = new Template("2", "Second", "datain1","dataout1","event1s","event1e", holes, engine);
		
		testTemplateAddOk(template1, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		testTemplateAddOk(template2, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML);
	}

}
