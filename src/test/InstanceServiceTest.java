package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import businessentities.Hole;
import businessentities.HoleManipulation;
import businessentities.InstanceBP;
import businessentities.InstanceBasic;
import businessentities.InstanceHole;
import businessentities.InstanceManipulation;
import businessentities.InstanceState;
import businessentities.Pattern;
import businessentities.PatternBasic;
import businessentities.PatternHole;
import businessentities.Template;
import engines.EngineBP;
import engines.EngineBpe;
import entityclients.InstanceClient;
import entityclients.PatternClient;
import entityclients.TemplateClient;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import services.WebServiseError;

public class InstanceServiceTest {

	private static String baseURI = "http://localhost:9000/BP_REST_API/rest";

	private static PatternClient clientPattern = null;
	private static TemplateClient clientTemplate = null;
	private static InstanceClient clientInstance = null;

	@BeforeClass
	public static void setUp() throws Exception {
		System.out.println("setupBeforeClass");
		clientTemplate = new TemplateClient(baseURI);
		clientTemplate.deleteTemplates(MediaType.APPLICATION_JSON);
		clientPattern = new PatternClient(baseURI);
		clientPattern.deletePatterns(MediaType.APPLICATION_JSON);
		clientInstance = new InstanceClient(baseURI);
		clientInstance.deleteInstances(MediaType.APPLICATION_JSON);
	}

	@After
	public void setAfter() throws Exception {
		// after each test clean up all
		System.out.println("setupAfterTest");
		clientTemplate.deleteTemplates(MediaType.APPLICATION_JSON);
		clientPattern.deletePatterns(MediaType.APPLICATION_JSON);
		clientInstance.deleteInstances(MediaType.APPLICATION_JSON);
	}

	private void testInstanceListEmpty(String mediaType) {
		System.out.println("testInstanceListEmpty");
		Response response = clientInstance.getInstances(mediaType);
		assertTrue(response.getStatus() == 200);
		try {
			List<InstanceBP> instances = response.readEntity(new GenericType<List<InstanceBP>>(){});
			assertNotNull(instances);
			assertTrue(instances.isEmpty());
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaType);
		}
	}

	@Test
	public void testListEmpty() {
		testInstanceListEmpty(MediaType.APPLICATION_JSON);
		testInstanceListEmpty(MediaType.APPLICATION_XML);
	}

	private InstanceBP testInstanceAddOk(InstanceBasic instanceBasic, String mediaTypeIn, String mediaTypeOut, Pattern pattern) {
		System.out.println("testInstanceAddOk");
		Response response = clientInstance.addInstance(instanceBasic, mediaTypeIn, mediaTypeOut);
		assertTrue(response.getStatus() == 200);
		try {
			InstanceBP instanceNew = response.readEntity(new GenericType<InstanceBP>(){});
			assertNotNull(instanceNew);
			System.out.println(instanceNew);
			assertTrue(instanceNew.getPatternId().equals(instanceBasic.getPatternId()));
			assertTrue(instanceNew.getState().equals(InstanceState.RUNNING));
			// TODO instance links
			if ( pattern.getHoles() == null ) {
				assertTrue (instanceNew.getHoles() == null || instanceNew.getHoles().isEmpty());
			} else {
				for (InstanceHole instanceHole : instanceNew.getHoles() ) {
					// TODO instance hole links
					int x = 0;
					for ( PatternHole patternHole : pattern.getHoles() ) {
						if  (instanceHole.compareWith(patternHole) ) {
							x++;
							assertNull(instanceHole.getInstanceId());
							assertTrue(instanceHole.compareWith(patternHole));
						}
					}
					assertTrue(x == 1);
				}
			}
			return instanceNew;
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeIn + " and " + mediaTypeOut);
			return null;
		}
	}

	private void testInstanceGetOk(InstanceBP instance, String mediaTypeOut) {
		System.out.println("testInstanceGetOk");
		Response response = clientInstance.getInstance(instance.getInstanceId(), mediaTypeOut);
		assertTrue(response.getStatus() == 200);
		try {
			InstanceBP instanceNew = response.readEntity(new GenericType<InstanceBP>(){});
			assertNotNull(instanceNew);
//			assertTrue(instance.compareWith(instance));
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeOut);
		}
	}
	/*
	private void testInstanceGetNotFound(String instanceId, String mediaTypeOut) {
		System.out.println("testInstanceGetNotFound");
		Response response = clientInstance.getInstance(instanceId, mediaTypeOut);
		assertTrue(response.getStatus() == 404);
		try {
			WebServiseError error = response.readEntity(new GenericType<WebServiseError>(){});
			assertNotNull(error);
			assertTrue(error.getMessage().equals("Instance not found"));
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeOut);
		}
	}
	 */

	private void testInstanceAddBadRequestDocument(InstanceBasic instanceBasic, String mediaTypeIn, String mediaTypeOut, String expectedError, int expectedHttpError) {
		System.out.println("testInstanceAddBadRequestDocument");
		Response response = clientInstance.addInstance(instanceBasic, mediaTypeIn, mediaTypeOut);
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
	public void testAddInstanceBad() {
		// pattern doesn't exist TODO fix the code
		InstanceBasic instanceBasic1 = new InstanceBasic("nonsense");
		testInstanceAddBadRequestDocument(instanceBasic1, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, "Such pattern doesn't exist", 500);
		testInstanceAddBadRequestDocument(instanceBasic1, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML, "Such pattern doesn't exist", 500);

		// empty pattern
		InstanceBasic instanceBasic2 = new InstanceBasic("");
		testInstanceAddBadRequestDocument(instanceBasic2, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, "'pattern_id' has to be specified", 400);
		testInstanceAddBadRequestDocument(instanceBasic2, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML, "'pattern_id' has to be specified", 400);
	}

	@Test
	public void testCRDInstanceOKwithoutHoles() {
		// create template
		EngineBpe engine = new EngineBpe(EngineBP.TESTCONNECTOR, "8991886837299789007", "http://localhost:8080/bonita");
		Template template = new Template("1", "Fisrt", "datain1","dataout1","event1s","event1e", null, engine);
		clientTemplate.addTemplate(template, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		// create pattern
		PatternBasic patternBasic = new PatternBasic("FirstPattern", template.getId());
		Response response = clientPattern.addPattern(patternBasic, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		Pattern pattern = response.readEntity(new GenericType<Pattern>(){});

		// test instance creation
		InstanceBasic instanceBasic = new InstanceBasic(pattern.getId());
		InstanceBP instance1 = testInstanceAddOk(instanceBasic, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, pattern);
		InstanceBP instance2 = testInstanceAddOk(instanceBasic, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML, pattern);

		testInstanceGetOk(instance1, MediaType.APPLICATION_JSON);
		testInstanceGetOk(instance2, MediaType.APPLICATION_XML);

		InstanceManipulation instanceManipulation = new InstanceManipulation(InstanceState.TERMINATED);
		testInstancePutOk(instance1,instanceManipulation, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		/*
		testPatternDeleteOk(instance1, MediaType.APPLICATION_JSON);
		testPatternDeleteOk(instance2, MediaType.APPLICATION_XML);

		testPatternGetNotFound(instance1.getId(), MediaType.APPLICATION_JSON);
		testPatternGetNotFound(instance2.getId(), MediaType.APPLICATION_XML);

		testPatternDeleteNotFound(instance1.getId(), MediaType.APPLICATION_JSON);
		testPatternDeleteNotFound(instance2.getId(), MediaType.APPLICATION_XML);
		 */
	}

	private InstanceBP testInstancePutOk(InstanceBP instance, InstanceManipulation instanceManipulation, String mediaTypeIn, String mediaTypeOut) {
		System.out.println("testInstancePutOk");
		Response response = clientInstance.doAction(instance.getInstanceId(), instanceManipulation, mediaTypeIn, mediaTypeOut);
		assertTrue(response.getStatus() == 200);
		try {
			InstanceBP instanceNew = response.readEntity(new GenericType<InstanceBP>(){});
			assertNotNull(instanceNew);
			System.out.println(instanceNew);
			assertTrue(instanceNew.getState().equals(instanceManipulation.getState()));
			/*if ( template.getHoles() == null || template.getHoles().isEmpty() ) {
				assertTrue(instanceNew.getStatus().equals(InstanceStatus.READY));
			}
			else {
				assertTrue(instanceNew.getStatus().equals(InstanceStatus.WIP));
			}
			// TODO instance links
			if ( template.getHoles() == null ) {
				assertTrue (instanceNew.getHoles() == null || instanceNew.getHoles().isEmpty());
			} else {
				for (InstanceHole instanceHole : instanceNew.getHoles() ) {
					// TODO instance hole links
					assertNull(instanceHole.getInstanceAssigned());
					assertTrue(instanceHole.getInstanceParent().equals(instanceNew.getId()));
					int x = 0;
					for ( Hole templateHole : template.getHoles() ) {
						if  (instanceHole.compareWith(templateHole) ) {
							x++;
						}
					}
					assertTrue(x == 1);
				}
			}*/
			return instanceNew;
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeIn + " and " + mediaTypeOut);
			return null;
		}

	}

	@Test
	public void testCRDInstanceOKwithHoles() {
		// create template
		EngineBpe engine = new EngineBpe(EngineBP.TESTCONNECTOR, "8991886837299789007", "http://localhost:8080/bonita");
		ArrayList <Hole> holes= new ArrayList<Hole>();
		Hole hole1 = new Hole("holename1","as","asd","ad","aasd");
		Hole hole2 = new Hole("holename2","as","asd","ad","aasd");
		holes.add(hole1);
		holes.add(hole2);
		Template template = new Template("1", "Fisrt", "datain1","dataout1","event1s","event1e", holes, engine);
		assertNotNull(template);
		Response response = clientTemplate.addTemplate(template, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		System.out.println(response.readEntity(String.class));

		// create pattern
		PatternBasic patternBasic = new PatternBasic("FirstPattern", template.getId());
		response = clientPattern.addPattern(patternBasic, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		Pattern pattern = response.readEntity(new GenericType<Pattern>(){});

		// test instance creation
		InstanceBasic instanceBasic = new InstanceBasic(pattern.getId());
		InstanceBP instance1 = testInstanceAddOk(instanceBasic, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, pattern);
		InstanceBP instance2 = testInstanceAddOk(instanceBasic, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML, pattern);

		testInstanceGetOk(instance1, MediaType.APPLICATION_JSON);
		testInstanceGetOk(instance2, MediaType.APPLICATION_XML);

		InstanceManipulation instanceManipulation = new InstanceManipulation(InstanceState.TERMINATED);
		testInstancePutOk(instance1,instanceManipulation, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		/*
		testPatternDeleteOk(instance1, MediaType.APPLICATION_JSON);
		testPatternDeleteOk(instance2, MediaType.APPLICATION_XML);

		testPatternGetNotFound(instance1.getId(), MediaType.APPLICATION_JSON);
		testPatternGetNotFound(instance2.getId(), MediaType.APPLICATION_XML);

		testPatternDeleteNotFound(instance1.getId(), MediaType.APPLICATION_JSON);
		testPatternDeleteNotFound(instance2.getId(), MediaType.APPLICATION_XML);
		 */
	}

	@Test
	public void testHoleStart() {
		// create template with holes
		EngineBpe engine1 = new EngineBpe(EngineBP.TESTCONNECTOR, "8991886837299789007", "http://localhost:8080/bonita");
		ArrayList <Hole> holes1= new ArrayList<Hole>();
		Hole hole11 = new Hole("A","as","asd","ad","aasd");
		holes1.add(hole11);
		Template template1 = new Template("1", "Fisrt", "datain1","dataout1","event1s","event1e", holes1, engine1);
		assertNotNull(template1);
		Response response = clientTemplate.addTemplate(template1, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		assertTrue(response.getStatus() == 200);
		System.out.println(response.readEntity(String.class));

		// create template without holes
		EngineBpe engine2 = new EngineBpe(EngineBP.TESTCONNECTOR, "8991886837299789007", "http://localhost:8080/bonita");
		Template template2 = new Template("2", "Second", "datain1","dataout1","event1s","event1e", null, engine2);
		assertNotNull(template2);
		response = clientTemplate.addTemplate(template2, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		assertTrue(response.getStatus() == 200);
		System.out.println(response.readEntity(String.class));

		// create pattern for template without holes
		PatternBasic patternBasic = new PatternBasic("FirstPattern", template2.getId());
		assertNotNull(patternBasic);
		response = clientPattern.addPattern(patternBasic, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		assertTrue(response.getStatus() == 200);
		Pattern pattern_filler = response.readEntity(new GenericType<Pattern>(){});		

		// create pattern for template with holes
		patternBasic = new PatternBasic("SecondPattern", template1.getId());
		assertNotNull(patternBasic);
		response = clientPattern.addPattern(patternBasic, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		assertTrue(response.getStatus() == 200);
		Pattern pattern_to_fill = response.readEntity(new GenericType<Pattern>(){});

		// Assign pattern_filler to hole A in the pattern_to_fill
		HoleManipulation holeManipulation = new HoleManipulation(pattern_filler.getId(), null);
		assertNotNull(holeManipulation);
		response = clientPattern.assignPattern(pattern_to_fill.getId(), hole11.getName(), holeManipulation, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		assertTrue(response.getStatus() == 200);
		PatternHole e = response.readEntity(PatternHole.class);
		ArrayList<PatternHole> newHoles = new ArrayList<PatternHole>();
		newHoles.add(e);
		pattern_to_fill.setHoles(newHoles);

		// start an instance for pattern with holes
		InstanceBasic instanceBasic = new InstanceBasic(pattern_to_fill.getId());
		InstanceBP instance1 = testInstanceAddOk(instanceBasic, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, pattern_to_fill);
		InstanceBP instance2 = testInstanceAddOk(instanceBasic, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML, pattern_to_fill);

		// signal instance to start a hole
		response = clientInstance.startHole(instance1.getInstanceId(), hole11.getName(), MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		assertTrue(response.getStatus() == 200);
		InstanceHole instanceHole1 = response.readEntity(InstanceHole.class);

		response = clientInstance.startHole(instance2.getInstanceId(), hole11.getName(), MediaType.APPLICATION_XML, MediaType.APPLICATION_XML);
		assertTrue(response.getStatus() == 200);
		InstanceHole instanceHole2 = response.readEntity(InstanceHole.class);

		testHoleStarted (instance1.getInstanceId(), hole11.getName(), MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, instanceHole1);
		testHoleStarted (instance2.getInstanceId(), hole11.getName(), MediaType.APPLICATION_XML, MediaType.APPLICATION_XML, instanceHole2);
	}

	private void testHoleStarted(String instanceId, String holeName, String mediaTypeIn, String mediaTypeOut, InstanceHole expectedHole) {
		Response response = clientInstance.getHole(instanceId, holeName, mediaTypeIn, mediaTypeOut);
		assertTrue(response.getStatus() == 200);
		try {
			InstanceHole instanceHoleNew = response.readEntity(InstanceHole.class);
			assertTrue (instanceHoleNew.equals(expectedHole));
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeIn + " and " + mediaTypeOut);
		}
	}
}