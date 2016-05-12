package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

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

import services.InstanceBP;

public class InstanceServiceTest {

	private String baseURI = "http://localhost:9000/BP_REST_API/rest";

	@Before
	public void setUp() throws Exception {
		System.out.println("setup");
	}
	
	private void testInstanceList(String mediaType) {
		Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFilter.class ) );
		WebTarget webTarget = client.target(baseURI).path("instances");
		Invocation.Builder invocationBuilder =  webTarget.request(mediaType);
		Response response = invocationBuilder.get();
		assertTrue(response.getStatus() == 200);
		try {
			List<InstanceBP> instances = response.readEntity(new GenericType<List<InstanceBP>>(){});
			assertNotNull(instances);
			/*String instances = response.readEntity(String.class);
			System.out.println(instances);*/
		} catch ( Exception e ) {
			e.printStackTrace();
			fail ("Cannot read instance from " + mediaType);
		}
	}
	
	@Test
	public void test() {
		testInstanceList(MediaType.APPLICATION_JSON);
		testInstanceList(MediaType.APPLICATION_XML);
	}

}
