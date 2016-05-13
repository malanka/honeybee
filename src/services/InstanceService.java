package services;


import java.net.HttpURLConnection;
import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import engines.GeneralCase;

import serviceerrors.InternalErrorException;

@Path("/instances")
public class InstanceService {

	private static final String base = "http://BP_REST_API/rest";

	private InstanceDAO instanceDao = new DAOFactory().getInstanceDAO();

	private PatternDAO patternDao = new DAOFactory().getPatternDAO();

	private templateDAO templateDao ;

	public InstanceService () throws InternalErrorException {
		templateDao = new DAOFactory().getTemplateDAO();
	}

	public static String getInstanceURI(String instanceId) {
		return base + "/instances/" + instanceId;
	}
	public static String getInstanceCreationURI(String patternId) {
		return base + "/instances?patternId=" + patternId;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
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
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response createInstance(InstanceBasic instanceBasic){
		System.out.println("createInstance");
		try {
			instanceBasic.checkIt();
		} catch (NotReadyException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(new WebServiseError(e.getMessage())).build();
		}
		try {
			// 1.   Create an instance object
			InstanceBP instance = instanceDao.createInstance(instanceBasic.getPatternId());

			// 2.   Get information about the engine for the instance
			// ASSUMPTION: "engine" part in template is immutable
			Template template = templateDao.getTemplateById(instance.getTemplateId());
			if ( template == null || !template.isEngineOk() ) {
				throw new InternalErrorException("Engine is not set on the template=" + template);
			}

			GeneralCase generalCase = template.getEngine().generateInstance(instance.getInstanceId());
			instance.setBpeId(generalCase.getId());
			instance.setStartDate(generalCase.getStartDate());
			GenericEntity<InstanceBP> entity = new GenericEntity<InstanceBP>(instance) {};
			return Response.ok(entity).build();
		} catch (InternalErrorException e) {
			System.out.println(e.getMessage());
			return Response.status(500).entity(new WebServiseError(e.getMessage())).build();
		}
	}
	
	// just for testing purposes
	@DELETE
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response deleteAllInstances(){
		System.out.println("deleteALLInstances");
		try {
			instanceDao.deleteAllInstances();
			return Response.noContent().build();
		} catch (InternalErrorException e) {
			return Response.status(500).entity(new WebServiseError(e.getMessage())).build();
		}
	}
	
	@PUT
	@Path("{id}")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getPattern(@PathParam("id") String id, InstanceManipulation action) {
		System.out.println("put instanceFINISHED");
		InstanceBP instance = null;
		try {
			instance = instanceDao.putInstanceById(id, action.getState());
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
		if ( instance != null ) {
			GenericEntity<InstanceBP> entity = new GenericEntity<InstanceBP>(instance) {};
			return Response.ok(entity).build();
		}
		else
			return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity(new WebServiseError("Instance not found")).build();
	}

	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getInstance(@PathParam("id") String id) {
		System.out.println("getInstance");
		InstanceBP instance = null;
		try {
			instance = instanceDao.getInstanceById(id);
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
		if ( instance != null ) {
			GenericEntity<InstanceBP> entity = new GenericEntity<InstanceBP>(instance) {};
			return Response.ok(entity).build();
		}
		else
			return Response.status(404).entity(new WebServiseError("Instance not found")).build();
	}

/*
	
	@DELETE
	@Path("{id}")
	//@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
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
