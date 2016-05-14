package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import businessentities.Hole;
import businessentities.Template;
import engines.EngineBP;
import engines.EngineBpe;
import entityclients.TemplateClient;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import services.WebServiseError;

public class TemplateServiceTest {

	private static final String baseURI = "http://localhost:9000/BP_REST_API/rest";
	
	private static TemplateClient clientTemplate = null;

	@BeforeClass
	public static void setUp() throws Exception {
		System.out.println("setupBeforeClass");
		clientTemplate = new TemplateClient(baseURI);
		clientTemplate.deleteTemplates(MediaType.APPLICATION_JSON);
	}
	
	@After
	public void setAfter() throws Exception {
		// after each test clean up all
		System.out.println("setupAfterTest");
		clientTemplate.deleteTemplates(MediaType.APPLICATION_JSON);
	}
	
	private void testTemplateListEmpty(String mediaType) {
		System.out.println("testTemplateListEmpty");
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
		System.out.println("testTemplateAddOk");
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
	
	private void testTemplateDeleteOk(Template template, String mediaTypeOut) {
		System.out.println("testTemplateDeleteOk");
		Response response = clientTemplate.deleteTemplate(template.getId(), mediaTypeOut);
		assertTrue(response.getStatus() == 204);
	}
	
	private void testTemplateGetOk(Template template, String mediaTypeOut) {
		System.out.println("testTemplateGetOk");
		Response response = clientTemplate.getTemplate(template.getId(), mediaTypeOut);
		assertTrue(response.getStatus() == 200);
		try {
			Template templateNew = response.readEntity(new GenericType<Template>(){});
			assertNotNull(templateNew);
			assertTrue(template.compareWith(template));
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeOut);
		}
	}
	
	private void testTemplateGetNotFound(String templateId, String mediaTypeOut) {
		System.out.println("testTemplateGetNotFound");
		Response response = clientTemplate.getTemplate(templateId, mediaTypeOut);
		assertTrue(response.getStatus() == 404);
		try {
			WebServiseError error = response.readEntity(new GenericType<WebServiseError>(){});
			assertNotNull(error);
			assertTrue(error.getMessage().equals("Template not found"));
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeOut);
		}
	}

	private void testTemplateDeleteNotFound(String templateId, String mediaTypeOut) {
		System.out.println("testTemplateDeleteNotFound");
		Response response = clientTemplate.deleteTemplate(templateId, mediaTypeOut);
		assertTrue(response.getStatus() == 404);
		try {
			WebServiseError error = response.readEntity(new GenericType<WebServiseError>(){});
			assertNotNull(error);
			assertTrue(error.getMessage().equals("Template not found"));
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeOut);
		}
	}
	@Test
	public void testCRDTemplateOK() {
		EngineBpe engine = new EngineBpe(EngineBP.BOONITA7_2, "7908120732971969775", "http://localhost:8080/bonita");
		ArrayList <Hole> holes= new ArrayList<Hole>();
		Hole hole1 = new Hole("holename1","as","asd","ad","aasd");
		Hole hole2 = new Hole("holename2","as","asd","ad","aasd");
		holes.add(hole1);
		holes.add(hole2);
		Template template1 = new Template("id1", "Fisrt", "datain1","dataout1","event1s","event1e", holes, engine);
		Template template2 = new Template("id2", "Second", "datain1","dataout1","event1s","event1e", holes, engine);

		testTemplateAddOk(template1, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		testTemplateAddOk(template2, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML);

		testTemplateGetOk(template1, MediaType.APPLICATION_JSON);
		testTemplateGetOk(template2, MediaType.APPLICATION_XML);

		testTemplateDeleteOk(template1, MediaType.APPLICATION_JSON);
		testTemplateDeleteOk(template2, MediaType.APPLICATION_XML);

		testTemplateGetNotFound(template1.getId(), MediaType.APPLICATION_JSON);
		testTemplateGetNotFound(template2.getId(), MediaType.APPLICATION_XML);

		testTemplateDeleteNotFound(template1.getId(), MediaType.APPLICATION_JSON);
		testTemplateDeleteNotFound(template2.getId(), MediaType.APPLICATION_XML);
	}

}
