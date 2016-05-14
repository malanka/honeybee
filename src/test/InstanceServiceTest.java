package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import businessentities.InstanceBP;
import businessentities.InstanceBasic;
import businessentities.InstanceManipulation;
import businessentities.InstanceState;
import businessentities.Pattern;
import businessentities.PatternBasic;
import businessentities.Template;
import engines.EngineBP;
import engines.EngineBpe;
import entityclients.InstanceClient;
import entityclients.PatternClient;
import entityclients.TemplateClient;

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
//			String instances = response.readEntity(String.class);
//			System.out.println(instances);
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

	private InstanceBP testInstanceAddOk(InstanceBasic instanceBasic, String mediaTypeIn, String mediaTypeOut) {
		System.out.println("testInstanceAddOk");
		Response response = clientInstance.addInstance(instanceBasic, mediaTypeIn, mediaTypeOut);
		assertTrue(response.getStatus() == 200);
		try {
			InstanceBP instanceNew = response.readEntity(new GenericType<InstanceBP>(){});
			assertNotNull(instanceNew);
			System.out.println(instanceNew);
			/*
			assertTrue(instanceNew.getTemplate().equals(instanceBasic.getTemplateId()));
			if ( template.getHoles() == null || template.getHoles().isEmpty() ) {
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
/*
	private void testInstanceDeleteOk(Instance instance, String mediaTypeOut) {
		System.out.println("testInstanceDeleteOk");
		Response response = clientInstance.deleteInstance(instance.getId(), mediaTypeOut);
		assertTrue(response.getStatus() == 204);
	}
*/
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

	private void testInstanceDeleteNotFound(String instanceId, String mediaTypeOut) {
		System.out.println("testInstanceDeleteNotFound");
		Response response = clientInstance.deleteInstance(instanceId, mediaTypeOut);
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
		EngineBpe engine = new EngineBpe(EngineBP.BOONITA7_2, "8991886837299789007", "http://localhost:8080/bonita");
		Template template = new Template("1", "Fisrt", "datain1","dataout1","event1s","event1e", null, engine);
		clientTemplate.addTemplate(template, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		clientTemplate.addTemplate(template, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		// create pattern
		PatternBasic patternBasic = new PatternBasic("FirstPattern", template.getId());
		Response response = clientPattern.addPattern(patternBasic, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		Pattern pattern = response.readEntity(new GenericType<Pattern>(){});
		
		// test instance creation
		InstanceBasic instanceBasic = new InstanceBasic(pattern.getId());
		InstanceBP instance1 = testInstanceAddOk(instanceBasic, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		InstanceBP instance2 = testInstanceAddOk(instanceBasic, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML);

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

	private InstanceBP testInstancePutOk(InstanceBP instance1, InstanceManipulation instanceManipulation, String mediaTypeIn, String mediaTypeOut) {
		System.out.println("testInstancePutOk");
		Response response = clientInstance.doAction(instance1.getInstanceId(), instanceManipulation, mediaTypeIn, mediaTypeOut);
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

}