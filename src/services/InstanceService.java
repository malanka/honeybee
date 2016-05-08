package services;


import java.net.HttpURLConnection;
import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import services.InstanceBP.InstanceState;

@Path("/instances")
public class InstanceService {

	private static final String base = "http://BP_REST_API/rest";

	private InstanceDAO instanceDao = new DAOFactory().getInstanceDAO();

	private PatternDAO patternDao = new DAOFactory().getPatternDAO();

	private templateDAO templateDao = new DAOFactory().getTemplateDAO();

	public static String getInstanceURI(String instanceId) {
		return base + "/instances/" + instanceId;
	}
	public static String getInstanceCreationURI(String patternId) {
		return base + "/instances?patternId=" + patternId;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInstances(@QueryParam("state") String stateStr, @QueryParam("patternId") String patternId){
		System.out.println("getInstances");
		InstanceState state = null;
		if ( stateStr != null ) {
			try {
				state = InstanceState.valueOf(stateStr);
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(new WebServiseError(e.getMessage())).build();
			}
		}
		try {
			List<InstanceBP> tmp = instanceDao.getAllInstances(patternId, state);
			GenericEntity<List<InstanceBP>> entity = new GenericEntity<List<InstanceBP>>(tmp) {};
			return Response.ok().entity(entity).build();
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_XML)
	public Response createInstance(@QueryParam("patternId") String patternId){
		System.out.println("createInstance from patternId="+patternId);
		if ( patternId == null ) {
			System.out.println("patternId is null");
			return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(new WebServiseError("Parameter patternId is required")).build();
		}
		try {
			// 1.   Create an instance object
			InstanceBP instance = instanceDao.createInstance(patternId);

			// 2.   Get information about the engine for the instance
			// ASSUMPTION: "engine" part in template is immutable
			Template template = templateDao.getTemplateById(instance.getTemplateId());
			System.out.println("template=" + template);
			if ( !template.isEngineOk() ) {
				throw new InternalErrorException("Engine is not set on the temlate!=" + template);
			}

			// 3.   Start the process instance on the real engine
			// 3.1  Create a webClient
			Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFilter.class ) );
			WS startInstance = template.getEngine().startProcess();
			System.out.println("request=" + startInstance);
			WebTarget webTarget = client.target(startInstance.getUri());
			Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON); // TODO this also should be part of "engine"
			Response response3 = invocationBuilder.post(Entity.json(startInstance.getRequestDocument()));   //this also somehow and method also!!
			// How to print response
			String output = response3.readEntity(String.class);

			System.out.println("responce from BONITA=" + output);
			GenericEntity<InstanceBP> entity = new GenericEntity<InstanceBP>(instance) {};
			return Response.ok(entity).build();
		} catch (InternalErrorException e) {
			System.out.println(e.getMessage());
			return Response.status(500).entity(new WebServiseError(e.getMessage())).build();
		}
	}
/*
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPattern(@PathParam("id") String id) {
		System.out.println("getPattern");
		Pattern pattern = patternDao.getPatternById(id);
		if ( pattern != null ) {
			GenericEntity<Pattern> entity = new GenericEntity<Pattern>(patternDao.getPatternById(id)) {};
			return Response.ok(entity).build();
		}
		else
			return Response.status(404).entity(new WebServiseError("Element not found")).build();
	}


	
	@DELETE
	@Path("{id}")
	//@Produces(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteTemplate(@PathParam("id") String id){
		System.out.println("createTemplates");
		if ( templateDao.deleteTemplate(id) == 0 )
			return Response.noContent().build();
		else
			return Response.status(404).entity("Element not found").build();
		// TODO fix error reporting
	}*/


}
