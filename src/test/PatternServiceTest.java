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
import services.Template;
import services.WebServiseError;

public class PatternServiceTest {

	private static final String baseURI = "http://localhost:9000/BP_REST_API/rest";
	
	private static PatternClient clientPattern = null;

	@BeforeClass
	public static void setUp() throws Exception {
		System.out.println("setupBeforeClass");
		clientPattern = new PatternClient(baseURI);
		clientPattern.deletePatterns(MediaType.APPLICATION_JSON);
	}
	
	@After
	public void setAfter() throws Exception {
		// after each test clean up all
		System.out.println("setupAfterTest");
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
	
	
	private void testPatternAddOk(Pattern pattern, String mediaTypeIn, String mediaTypeOut) {
		System.out.println("testPatternAddOk");
		Response response = clientPattern.addPattern(pattern, mediaTypeIn, mediaTypeOut);
		assertTrue(response.getStatus() == 200);
		try {
			Pattern patternNew = response.readEntity(new GenericType<Pattern>(){});
			assertNotNull(patternNew);
			assertTrue(pattern.compareWith(pattern));
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
