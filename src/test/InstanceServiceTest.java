package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import engines.EngineBP;
import engines.EngineBpe;
import entityclients.InstanceClient;
import entityclients.PatternClient;
import entityclients.TemplateClient;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import services.Hole;
import services.InstanceBP;
import services.InstanceBasic;
import services.Template;
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
//		clientTemplate.deleteTemplates(MediaType.APPLICATION_JSON);
		clientPattern = new PatternClient(baseURI);
//		clientPattern.deletePatterns(MediaType.APPLICATION_JSON);
		clientInstance = new InstanceClient(baseURI);
//		clientInstance.deleteInstances(MediaType.APPLICATION_JSON);
	}

//	@After
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
			/*
			String instances = response.readEntity(String.class);
			System.out.println(instances);
			 */
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaType);
		}
	}

//	@Test
	public void testListEmpty() {
		testInstanceListEmpty(MediaType.APPLICATION_JSON);
		testInstanceListEmpty(MediaType.APPLICATION_XML);
	}

/*
	private Instance testInstanceAddOk(InstanceBasic instanceBasic, Template template, String mediaTypeIn, String mediaTypeOut) {
		System.out.println("testInstanceAddOk");
		Response response = clientInstance.addInstance(instanceBasic, mediaTypeIn, mediaTypeOut);
		assertTrue(response.getStatus() == 200);
		try {
			Instance instanceNew = response.readEntity(new GenericType<Instance>(){});
			assertNotNull(instanceNew);
			assertTrue(instanceNew.getName().equals(instanceBasic.getName()));
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
			}
			return instanceNew;
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeIn + " and " + mediaTypeOut);
			return null;
		}
	}

	private void testInstanceDeleteOk(Instance instance, String mediaTypeOut) {
		System.out.println("testInstanceDeleteOk");
		Response response = clientInstance.deleteInstance(instance.getId(), mediaTypeOut);
		assertTrue(response.getStatus() == 204);
	}

	private void testInstanceGetOk(Instance instance, String mediaTypeOut) {
		System.out.println("testInstanceGetOk");
		Response response = clientInstance.getInstance(instance.getId(), mediaTypeOut);
		assertTrue(response.getStatus() == 200);
		try {
			Instance instanceNew = response.readEntity(new GenericType<Instance>(){});
			assertNotNull(instanceNew);
			assertTrue(instance.compareWith(instance));
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read for " + mediaTypeOut);
		}
	}

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
		InstanceBasic instanceBasic = new InstanceBasic("nonsense");
		testInstanceAddBadRequestDocument(instanceBasic, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, "Such pattern doesn't exist", 500);
		testInstanceAddBadRequestDocument(instanceBasic, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML, "Such pattern doesn't exist", 500);
/*
		// empty template
		InstanceBasic instanceBasic2 = new InstanceBasic("TestABC", "");
		testInstanceAddBadRequestDocument(instanceBasic2, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, "'template_id' has to be specified", 400);
		testInstanceAddBadRequestDocument(instanceBasic2, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML, "'template_id' has to be specified", 400);

		// empty name
		EngineBpe engine = new EngineBpe(EngineBP.BOONITA7_2, "7908120732971969775", "http://localhost:8080/bonita");
		String id = "1";
		Template template = new Template(id, "Fisrt", "datain1","dataout1","event1s","event1e", null, engine);
		clientTemplate.addTemplate(template, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		InstanceBasic instanceBasic3 = new InstanceBasic("", "1");
		testInstanceAddBadRequestDocument(instanceBasic3, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON, "'name' has to be specified", 400);
		testInstanceAddBadRequestDocument(instanceBasic3, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML, "'name' has to be specified", 400);
*/
	}
/*
	@Test
	public void testCRDInstanceOK() {
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
		clientTemplate.addTemplate(template1, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		clientTemplate.addTemplate(template2, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);

		PatternBasic instanceBasic1 = new PatternBasic("FirstPattern", template1.getId());
		Pattern instance1 = testPatternAddOk(instanceBasic1, template1, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON);
		PatternBasic instanceBasic2= new PatternBasic("SecondPattern", template2.getId());
		Pattern instance2= testPatternAddOk(instanceBasic2, template2, MediaType.APPLICATION_XML, MediaType.APPLICATION_XML);

		testPatternGetOk(instance1, MediaType.APPLICATION_JSON);
		testPatternGetOk(instance2, MediaType.APPLICATION_XML);
		
		testPatternDeleteOk(instance1, MediaType.APPLICATION_JSON);
		testPatternDeleteOk(instance2, MediaType.APPLICATION_XML);

		testPatternGetNotFound(instance1.getId(), MediaType.APPLICATION_JSON);
		testPatternGetNotFound(instance2.getId(), MediaType.APPLICATION_XML);

		testPatternDeleteNotFound(instance1.getId(), MediaType.APPLICATION_JSON);
		testPatternDeleteNotFound(instance2.getId(), MediaType.APPLICATION_XML);
	}*/

}