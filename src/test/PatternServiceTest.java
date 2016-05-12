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
import services.Pattern;
import services.PatternBasic;
import services.Template;
import services.WebServiseError;

public class PatternServiceTest {

	private static final String baseURI = "http://localhost:9000/BP_REST_API/rest";

	private static PatternClient clientPattern = null;
	private static TemplateClient clientTemplate = null;

	@BeforeClass
	public static void setUp() throws Exception {
		System.out.println("setupBeforeClass");
		clientTemplate = new TemplateClient(baseURI);
		clientTemplate.deleteTemplates(MediaType.APPLICATION_JSON);
		clientPattern = new PatternClient(baseURI);
		clientPattern.deletePatterns(MediaType.APPLICATION_JSON);
	}

	@After
	public void setAfter() throws Exception {
		// after each test clean up all
		System.out.println("setupAfterTest");
		clientTemplate.deleteTemplates(MediaType.APPLICATION_JSON);
		clientPattern.deletePatterns(MediaType.APPLICATION_JSON);
	}

	private void testPatternListEmpty(String mediaType) {
		System.out.println("testPatternListEmpty");
		Response response = clientPattern.getPatterns(mediaType);
		assertTrue(response.getStatus() == 200);
		try {
			List<Pattern> patterns = response.readEntity(new GenericType<List<Pattern>>(){});
			assertNotNull(patterns);
			assertTrue(patterns.isEmpty());
			/*
			String patterns = response.readEntity(String.class);
			System.out.println(patterns);
			 */
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaType);
		}
	}

	@Test
	public void testListEmpty() {
		testPatternListEmpty(MediaType.APPLICATION_JSON);
		testPatternListEmpty(MediaType.APPLICATION_XML);
	}


	private void testPatternAddOk(PatternBasic patternBasic, String templateId, String mediaTypeIn, String mediaTypeOut) {
		System.out.println("testPatternAddOk");
		Response response = clientPattern.addPattern(patternBasic, templateId, mediaTypeIn, mediaTypeOut);
		assertTrue(response.getStatus() == 200);
		try {
			Pattern patternNew = response.readEntity(new GenericType<Pattern>(){});
			assertNotNull(patternNew);
//			assertTrue(patternBasic.compareWith(patternBasic));
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeIn + " and " + mediaTypeOut);
		}
	}

	private void testPatternDeleteOk(Pattern pattern, String mediaTypeOut) {
		System.out.println("testPatternDeleteOk");
		Response response = clientPattern.deletePattern(pattern.getId(), mediaTypeOut);
		assertTrue(response.getStatus() == 204);
	}

	private void testPatternGetOk(Pattern pattern, String mediaTypeOut) {
		System.out.println("testPatternGetOk");
		Response response = clientPattern.getPattern(pattern.getId(), mediaTypeOut);
		assertTrue(response.getStatus() == 200);
		try {
			Pattern patternNew = response.readEntity(new GenericType<Pattern>(){});
			assertNotNull(patternNew);
			assertTrue(pattern.compareWith(pattern));
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeOut);
		}
	}

	private void testPatternGetNotFound(String patternId, String mediaTypeOut) {
		System.out.println("testPatternGetNotFound");
		Response response = clientPattern.getPattern(patternId, mediaTypeOut);
		assertTrue(response.getStatus() == 404);
		try {
			WebServiseError error = response.readEntity(new GenericType<WebServiseError>(){});
			assertNotNull(error);
			assertTrue(error.getMessage().equals("Pattern not found"));
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeOut);
		}
	}

	private void testPatternDeleteNotFound(String patternId, String mediaTypeOut) {
		System.out.println("testPatternDeleteNotFound");
		Response response = clientPattern.deletePattern(patternId, mediaTypeOut);
		assertTrue(response.getStatus() == 404);
		try {
			WebServiseError error = response.readEntity(new GenericType<WebServiseError>(){});
			assertNotNull(error);
			assertTrue(error.getMessage().equals("Pattern not found"));
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeOut);
		}
	}

	private void testPatternAddBadRequestDocument(PatternBasic patternBasic, String mediaTypeIn, String mediaTypeOut, String expectedError, int expectedHttpError) {
		System.out.println("testPatternAddBadRequestDocument");
		Response response = clientPattern.addPattern(patternBasic, "", mediaTypeIn, mediaTypeOut);
		assertTrue(response.getStatus() == expectedHttpError);
		try {
			WebServiseError error = response.readEntity(new GenericType<WebServiseError>(){});
			assertNotNull(error);
			assertTrue(error.getMessage().equals(expectedError));
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeIn + " and " + mediaTypeOut);
		}
	}
	
	@Test
	public void testAddPatternBad() {
		// template doesn't exist TODO fix the code
		PatternBasic patternBasic1 = new PatternBasic("TestABC", "qwert");
		testPatternAddBadRequestDocument(patternBasic1, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, "Such template doesn't exist", 500);
		testPatternAddBadRequestDocument(patternBasic1, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML, "Such template doesn't exist", 500);
		
		// empty template
		PatternBasic patternBasic2 = new PatternBasic("TestABC", "");
		testPatternAddBadRequestDocument(patternBasic2, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, "'template_id' has to be specified", 400);
		testPatternAddBadRequestDocument(patternBasic2, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML, "'template_id' has to be specified", 400);

		EngineBpe engine = new EngineBpe(EngineBP.BOONITA7_2, "7908120732971969775", "http://localhost:8080/bonita");
		String id = "1";
		Template template = new Template(id, "Fisrt", "datain1","dataout1","event1s","event1e", null, engine);
		clientTemplate.addTemplate(template, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		// empty name
		PatternBasic patternBasic3 = new PatternBasic("", "1");
		testPatternAddBadRequestDocument(patternBasic3, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, "'name' has to be specified", 400);
		testPatternAddBadRequestDocument(patternBasic3, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML, "'name' has to be specified", 400);
	}

	@Test
	public void testCRDPatternOK() {
		EngineBpe engine = new EngineBpe(EngineBP.BOONITA7_2, "7908120732971969775", "http://localhost:8080/bonita");
		ArrayList <Hole> holes= new ArrayList<Hole>();
		Hole hole1 = new Hole("holename1","as","asd","ad","aasd");
		Hole hole2 = new Hole("holename2","as","asd","ad","aasd");
		holes.add(hole1);
		holes.add(hole2);
		String t_id1 = "1";
		String t_id2 = "2";
		Template template1 = new Template(t_id1, "Fisrt", "datain1","dataout1","event1s","event1e", null, engine);
		Template template2 = new Template(t_id2, "Second", "datain1","dataout1","event1s","event1e", holes, engine);
		/*
		testPatternAddOk(pattern1, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		testPatternAddOk(pattern2, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML);

		testPatternGetOk(pattern1, MediaType.APPLICATION_JSON);
		testPatternGetOk(pattern2, MediaType.APPLICATION_XML);

		testPatternDeleteOk(pattern1, MediaType.APPLICATION_JSON);
		testPatternDeleteOk(pattern2, MediaType.APPLICATION_XML);

		testPatternGetNotFound(id1,MediaType.APPLICATION_JSON);
		testPatternGetNotFound(id1,MediaType.APPLICATION_XML);

		testPatternDeleteNotFound(id1,MediaType.APPLICATION_JSON);
		testPatternDeleteNotFound(id1,MediaType.APPLICATION_XML);*/
	}

}
