package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import businessentities.Hole;
import businessentities.HoleManipulation;
import businessentities.Pattern;
import businessentities.PatternBasic;
import businessentities.PatternHole;
import businessentities.PatternStatus;
import businessentities.Template;
import engines.EngineBP;
import engines.EngineBpe;
import entityclients.PatternClient;
import entityclients.TemplateClient;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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


	private Pattern testPatternAddOk(PatternBasic patternBasic, Template template, String mediaTypeIn, String mediaTypeOut) {
		System.out.println("testPatternAddOk");
		Response response = clientPattern.addPattern(patternBasic, mediaTypeIn, mediaTypeOut);
		assertTrue(response.getStatus() == 200);
		try {
			Pattern patternNew = response.readEntity(new GenericType<Pattern>(){});
			assertNotNull(patternNew);
			assertTrue(patternNew.getName().equals(patternBasic.getName()));
			assertTrue(patternNew.getTemplate().equals(patternBasic.getTemplateId()));
			if ( template.getHoles() == null || template.getHoles().isEmpty() ) {
				assertTrue(patternNew.getStatus().equals(PatternStatus.READY));
			}
			else {
				assertTrue(patternNew.getStatus().equals(PatternStatus.WIP));
			}
			// TODO pattern links
			assertTrue (patternNew.getHoles() == null);
			if ( template.getHoles() == null ) {
				Response rPatternHoles = clientPattern.getHoles(patternNew.getId(), mediaTypeOut);
				assertTrue (rPatternHoles.getStatus() == 200 );
				List<PatternHole> patternHoles = rPatternHoles.readEntity(new GenericType<List<PatternHole>>(){});
				assertTrue (patternHoles.isEmpty());
			} else {
				// Holes are not returned for the pattern, need a special request
				Response rPatternHoles = clientPattern.getHoles(patternNew.getId(), mediaTypeOut);
				assertTrue (rPatternHoles.getStatus() == 200 );
				List<PatternHole> patternHoles = rPatternHoles.readEntity(new GenericType<List<PatternHole>>(){});
				for (PatternHole patternHole : patternHoles ) {
					// TODO pattern hole links
					assertNull(patternHole.getPatternAssigned());
					assertTrue(patternHole.getPatternParent().equals(patternNew.getId()));
					int x = 0;
					for ( Hole templateHole : template.getHoles() ) {
						if  (patternHole.compareWith(templateHole) ) {
							x++;
						}
					}
					assertTrue(x == 1);
				}
			}
			return patternNew;
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeIn + " and " + mediaTypeOut);
			return null;
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
			assertTrue(pattern.compareWith(patternNew));
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
		Response response = clientPattern.addPattern(patternBasic, mediaTypeIn, mediaTypeOut);
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

		// empty name
		EngineBpe engine = new EngineBpe(EngineBP.TESTCONNECTOR, "7908120732971969775", "http://localhost:8080/bonita");
		String id = "1";
		Template template = new Template(id, "Fisrt", "datain1","dataout1","event1s","event1e", null, engine);
		clientTemplate.addTemplate(template, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		PatternBasic patternBasic3 = new PatternBasic("", "1");
		testPatternAddBadRequestDocument(patternBasic3, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, "'name' has to be specified", 400);
		testPatternAddBadRequestDocument(patternBasic3, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML, "'name' has to be specified", 400);
	}

	@Test
	public void testCRDPatternOK() {
		EngineBpe engine = new EngineBpe(EngineBP.TESTCONNECTOR, "7908120732971969775", "http://localhost:8080/bonita");
		ArrayList <Hole> holes= new ArrayList<Hole>();
		Hole hole1 = new Hole("holename1","as","asd","ad","aasd");
		Hole hole2 = new Hole("holename2","as","asd","ad","aasd");
		holes.add(hole1);
		holes.add(hole2);
		String t_id1 = "1";
		String t_id2 = "2";
		Template template1 = new Template(t_id1, "Fisrt", "datain1","dataout1","event1s","event1e", null, engine);
		Template template2 = new Template(t_id2, "Second", "datain1","dataout1","event1s","event1e", holes, engine);
		clientTemplate.addTemplate(template1, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		clientTemplate.addTemplate(template2, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);

		PatternBasic patternBasic1 = new PatternBasic("FirstPattern", template1.getId());
		Pattern pattern1 = testPatternAddOk(patternBasic1, template1, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		PatternBasic patternBasic11 = new PatternBasic("FirstPattern1", template1.getId());
		testPatternAddOk(patternBasic11, template1, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML);
		PatternBasic patternBasic2= new PatternBasic("SecondPattern", template2.getId());
		Pattern pattern2 = testPatternAddOk(patternBasic2, template2, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML);
		PatternBasic patternBasic22= new PatternBasic("SecondPattern2", template2.getId());
		testPatternAddOk(patternBasic22, template2, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		
		testPatternGetOk(pattern1, MediaType.APPLICATION_JSON);
		testPatternGetOk(pattern2, MediaType.APPLICATION_XML);

		testPatternDeleteOk(pattern1, MediaType.APPLICATION_JSON);
		testPatternDeleteOk(pattern2, MediaType.APPLICATION_XML);

		testPatternGetNotFound(pattern1.getId(), MediaType.APPLICATION_JSON);
		testPatternGetNotFound(pattern2.getId(), MediaType.APPLICATION_XML);

		testPatternDeleteNotFound(pattern1.getId(), MediaType.APPLICATION_JSON);
		testPatternDeleteNotFound(pattern2.getId(), MediaType.APPLICATION_XML);
	}


	@Test
	public void testAssignPatternHole() {
		EngineBpe engine = new EngineBpe(EngineBP.TESTCONNECTOR, "7908120732971969775", "http://localhost:8080/bonita");
		ArrayList <Hole> holes= new ArrayList<Hole>();
		Hole hole1 = new Hole("holename1","as","asd","ad","aasd");
		Hole hole2 = new Hole("holename2","as","asd","ad","aasd");
		holes.add(hole1);
		holes.add(hole2);
		Template template = new Template("1", "Second", "datain1","dataout1","event1s","event1e", holes, engine);
		clientTemplate.addTemplate(template, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);

		PatternBasic patternBasic1 = new PatternBasic("FirstPattern", template.getId());
		Pattern pattern1 = testPatternAddOk(patternBasic1, template, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		testPatternGetOk(pattern1, MediaType.APPLICATION_JSON);

		PatternBasic patternBasic2 = new PatternBasic("SecondPattern", template.getId());
		Pattern pattern2 = testPatternAddOk(patternBasic2, template, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		testPatternGetOk(pattern2, MediaType.APPLICATION_JSON);

		HoleManipulation holeManipulation = new HoleManipulation(pattern2.getId(), null);
		testAssignHoleOk(pattern1, holeManipulation, hole1.getName(), MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		PatternHole e = testAssignHoleOk(pattern1, holeManipulation, hole1.getName(), MediaType.APPLICATION_XML, MediaType.APPLICATION_XML);

		testPatternHoleOne (e, MediaType.APPLICATION_JSON);
		testPatternHoleOne (e, MediaType.APPLICATION_XML);
		// status of the pattern didn't change as assign only one hole
		testPatternGetOk(pattern1, MediaType.APPLICATION_JSON);
		testPatternGetOk(pattern1, MediaType.APPLICATION_XML);
		
		
		HoleManipulation holeManipulation2 = new HoleManipulation(pattern2.getId(), null);
		testAssignHoleOk(pattern1, holeManipulation, hole2.getName(), MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		PatternHole e2 = testAssignHoleOk(pattern1, holeManipulation2, hole2.getName(), MediaType.APPLICATION_XML, MediaType.APPLICATION_XML);

		testPatternHoleOne (e2, MediaType.APPLICATION_JSON);
		testPatternHoleOne (e2, MediaType.APPLICATION_XML);
		// status of the pattern changed as all holes were assigned
		pattern1.setStatus(PatternStatus.READY);
		testPatternGetOk(pattern1, MediaType.APPLICATION_JSON);
		testPatternGetOk(pattern1, MediaType.APPLICATION_XML);

	}

	private void testPatternHoleOne(PatternHole patternHole, String mediaTypeOut) {
		System.out.println("testPatternHoleOne");
		Response response = clientPattern.getOneHole(patternHole.getPatternParent(), patternHole.getName(), mediaTypeOut);
		assertTrue(response.getStatus() == 200);
		try {
			PatternHole patternHole_new = response.readEntity(new GenericType<PatternHole>(){});
			assertNotNull(patternHole_new);
			assertTrue(patternHole_new.equals(patternHole));
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeOut);
		}
	}

	private PatternHole testAssignHoleOk(Pattern pattern, HoleManipulation holeManipulation, String holeName, String mediaTypeIn, String mediaTypeOut) {
		System.out.println("testAssignHoleOk");
		Response response = clientPattern.assignPattern(pattern.getId(), holeName, holeManipulation, mediaTypeIn, mediaTypeOut);
		assertTrue(response.getStatus() == 200);
		try {
			PatternHole patternHole = response.readEntity(new GenericType<PatternHole>(){});
			assertNotNull(patternHole);
			assertTrue(patternHole.getName().equals(holeName));
			assertTrue(patternHole.getPatternAssigned().equals(holeManipulation.getAssigned_pattern_id()));
			return patternHole;
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeOut);
		}
		return null;
	}


	@Test
	public void testGetPatternHolesWithHoles() {
		// set up expected input data template with holes
		EngineBpe engine = new EngineBpe(EngineBP.TESTCONNECTOR, "7908120732971969775", "http://localhost:8080/bonita");
		ArrayList <Hole> holes= new ArrayList<Hole>();
		Hole hole1 = new Hole("holename1","as","asd","ad","aasd");
		Hole hole2 = new Hole("holename2","as","asd","ad","aasd");
		holes.add(hole1);
		holes.add(hole2);
		Template template = new Template("1", "Second", "datain1","dataout1","event1s","event1e", holes, engine);
		Response response = clientTemplate.addTemplate(template, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		assertTrue(response.getStatus() == 200);

		PatternBasic patternBasic1 = new PatternBasic("FirstPattern", template.getId());
		response = clientPattern.addPattern(patternBasic1, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		assertTrue(response.getStatus() == 200);
		Pattern pattern = response.readEntity(Pattern.class);

		// test the result
		testPatternHoleList (pattern, MediaType.APPLICATION_JSON, template.getHoles());
		testPatternHoleList (pattern, MediaType.APPLICATION_XML, template.getHoles());
	}

	@Test
	public void testGetPatternHolesWithOutHoles() {
		// set up expected input data template without holes
		EngineBpe engine = new EngineBpe(EngineBP.TESTCONNECTOR, "7908120732971969775", "http://localhost:8080/bonita");
		Template template = new Template("1", "Second", "datain1","dataout1","event1s","event1e", null, engine);
		Response response = clientTemplate.addTemplate(template, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		assertTrue(response.getStatus() == 200);

		PatternBasic patternBasic1 = new PatternBasic("FirstPattern", template.getId());
		response = clientPattern.addPattern(patternBasic1, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		assertTrue(response.getStatus() == 200);
		Pattern pattern = response.readEntity(Pattern.class);

		// test the result
		testPatternHoleList (pattern, MediaType.APPLICATION_JSON, template.getHoles());
		testPatternHoleList (pattern, MediaType.APPLICATION_XML, template.getHoles());
	}

	private void testPatternHoleList(Pattern pattern, String mediaTypeOut, List<Hole> templateHoles) {
		System.out.println("testPatternHoleList");
		Response response = clientPattern.getHoles(pattern.getId(), mediaTypeOut);
		assertTrue(response.getStatus() == 200);
		try {
			List<PatternHole> patternHoles = response.readEntity(new GenericType<List<PatternHole>>(){});
			assertNotNull(patternHoles);
			if ( templateHoles == null || templateHoles.isEmpty() ) {
				assertTrue(patternHoles.isEmpty());
			}
			else {
				assertTrue(patternHoles.size() == templateHoles.size());
				for (PatternHole patternHole : patternHoles ) {
					// TODO pattern hole links
					assertNull(patternHole.getPatternAssigned());
					assertTrue(patternHole.getPatternParent().equals(pattern.getId()));
					int x = 0;
					for ( Hole templateHole : templateHoles ) {
						if  (patternHole.compareWith(templateHole) ) {
							x++;
						}
					}
					assertTrue(x == 1);
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeOut);
		}
	}

	@Test
	public void testGetPatternHole() {
		// set up expected input data template with holes
		EngineBpe engine = new EngineBpe(EngineBP.TESTCONNECTOR, "7908120732971969775", "http://localhost:8080/bonita");
		ArrayList <Hole> holes= new ArrayList<Hole>();
		Hole hole1 = new Hole("holename1","as","asd","ad","aasd");
		Hole hole2 = new Hole("holename2","as","asd","ad","aasd");
		holes.add(hole1);
		holes.add(hole2);
		Template template = new Template("1", "Second", "datain1","dataout1","event1s","event1e", holes, engine);
		Response response = clientTemplate.addTemplate(template, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		assertTrue(response.getStatus() == 200);

		PatternBasic patternBasic1 = new PatternBasic("FirstPattern", template.getId());
		response = clientPattern.addPattern(patternBasic1, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		assertTrue(response.getStatus() == 200);
		Pattern pattern = response.readEntity(Pattern.class);

		// test the result OK
		testPatternHole (pattern, hole1.getName(), MediaType.APPLICATION_JSON, template.getHoles());
		testPatternHole (pattern, hole2.getName(), MediaType.APPLICATION_XML, template.getHoles());
		testPatternHole (pattern, "ASD", MediaType.APPLICATION_JSON, template.getHoles());
		testPatternHole (pattern, "ASD", MediaType.APPLICATION_XML, template.getHoles());
	}

	private void testPatternHole(Pattern pattern, String holeName, String mediaTypeOut, List<Hole> templateHoles) {
		System.out.println("testPatternHole");
		Response response = clientPattern.getOneHole(pattern.getId(), holeName, mediaTypeOut);
		List<Hole> templateHoles_copy = new ArrayList<Hole>();
		if ( templateHoles != null ) {
			templateHoles_copy.addAll(templateHoles);
			templateHoles_copy.removeIf(s-> ( ! (s.getName().equals(holeName)) ));
		}
		// not Ok case
		if ( templateHoles_copy.isEmpty() ) {
			assertTrue(response.getStatus() == 404);
			try {
				WebServiseError error = response.readEntity(new GenericType<WebServiseError>(){});
				assertNotNull(error);
				return;
			} catch ( Exception e ) {
				e.printStackTrace();
				fail ("Cannot read for " + mediaTypeOut);
			}
		}
		// Ok case
		assertTrue(response.getStatus() == 200);
		try {
			PatternHole patternHole = response.readEntity(new GenericType<PatternHole>(){});
			assertNull(patternHole.getPatternAssigned());
			assertTrue(patternHole.getPatternParent().equals(pattern.getId()));
			assertTrue (patternHole.compareWith(templateHoles_copy.get(0)));
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeOut);
		}
	}

}