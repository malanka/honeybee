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

import businessentities.InstanceBP;
import businessentities.InstanceBasic;
import businessentities.InstanceHole;
import businessentities.InstanceManipulation;
import businessentities.InstanceState;
import businessentities.Template;
import engines.GeneralCase;
import entityclients.InstanceClient;
import serviceerrors.InternalErrorException;

@Path("/instances")
public class InstanceService {

	private static final String base = "http://localhost:9000/BP_REST_API/rest";

	private InstanceDAO instanceDao = null;

	private templateDAO templateDao = null;

	public InstanceService () throws InternalErrorException {
		templateDao = new DAOFactory().getTemplateDAO();
		instanceDao = new DAOFactory().getInstanceDAO();
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
				e.printStackTrace();
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
	public Response createInstance(InstanceBasic instanceBasic) {
		System.out.println("createInstance");
		if ( instanceBasic == null ) {
			return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(new WebServiseError("Request document doesn't fit the expected format")).build();
		}
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

			GeneralCase generalCase = template.getEngine().generateInstance(getInstanceURI(instance.getInstanceId()));
			instance.setBpeId(generalCase.getId());
			instance.setStartDate(generalCase.getStartDate());
			GenericEntity<InstanceBP> entity = new GenericEntity<InstanceBP>(instance) {};
			return Response.ok(entity).build();
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
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
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
	}
	
	@PUT
	@Path("{id}")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getInstance(@PathParam("id") String id, InstanceManipulation action) {
		System.out.println("put instanceManipulation:" + action);
		if ( action == null ) {
			return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(new WebServiseError("Request document doesn't fit the expected format")).build();
		}
		InstanceBP instance = null;
		try {
			instance = instanceDao.putInstanceById(id, action.getState());
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
		if ( instance == null ) {
			return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity(new WebServiseError("Instance not found")).build();
		}
		GenericEntity<InstanceBP> entity = new GenericEntity<InstanceBP>(instance) {};
		return Response.ok(entity).build();	
	}

	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getInstance(@PathParam("id") String id) {
		System.out.println("getInstance");
		InstanceBP instance = null;
		try {
			instance = instanceDao.getInstanceById(id);
			System.out.println(instance);
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
		if ( instance == null ) {
			return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity(new WebServiseError("Instance not found")).build();
		}
		GenericEntity<InstanceBP> entity = new GenericEntity<InstanceBP>(instance) {};
		return Response.ok(entity).build();
	}

	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("{id}/holes/{holename}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response startHole(@PathParam("id") String instanceId, @PathParam("holename") String holeName) {
		System.out.println("startHole");
		InstanceBP instance = null;
		try {
			instance = instanceDao.getInstanceById(instanceId);
			System.out.println(instance);
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
		if ( instance == null ) {
			return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity(new WebServiseError("Instance not found")).build();
		}
		for ( InstanceHole ahole : instance.getHoles() ) {
			if ( ahole.getName().equals(holeName) ) {
				// we found a hole
				if ( ahole.getPatternAssigned() == null ) {
					System.out.println("pattern is not assigned");
					return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError("Pattern for the hole is not assigned")).build();
				}
				InstanceBasic instanceBasic = new InstanceBasic(ahole.getPatternAssigned());
				Response response = this.createInstance(instanceBasic);
				if ( response.getStatus() != HttpURLConnection.HTTP_OK ) {
					// instance cannot be created
					System.out.println("Instance was not created");
					return response;
				}
				InstanceBP instanceNew = (InstanceBP) response.getEntity();
				ahole.setInstanceId(instanceNew.getInstanceId());
				GenericEntity<InstanceBP> entity = new GenericEntity<InstanceBP>(instance) {};
				return Response.ok(entity).build();
			}
		}
		// we didn't found such hole
		System.out.println("Specified hole doesn't exist");
		return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(new WebServiseError("Instance hole not found")).build();		
	}

}
