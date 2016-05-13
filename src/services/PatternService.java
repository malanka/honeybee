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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import serviceerrors.InternalErrorException;

@Path("/patterns")
public class PatternService {

	private static final String base = "http://localhost:9000/BP_REST_API/rest";

	private PatternDAO patternDao;

	public PatternService() throws InternalErrorException {
		patternDao = new DAOFactory().getPatternDAO();
	}
	
	public static String getPatternHolesURI(String patternId) {
		return base + "/patterns/" + patternId + "/holes";
	}

	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getPatterns(){
		System.out.println("getPatterns");
		try {
			List<Pattern> tmp = patternDao.getAllPatterns();
			GenericEntity<List<Pattern>> entity = new GenericEntity<List<Pattern>>(tmp) {};
			return Response.ok().entity(entity).build();
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
	}

	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getPattern(@PathParam("id") String id) {
		System.out.println("getPattern");
		Pattern pattern = null;
		try {
			pattern = patternDao.getPatternById(id);
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
		if ( pattern != null ) {
			GenericEntity<Pattern> entity = new GenericEntity<Pattern>(pattern) {};
			return Response.ok(entity).build();
		}
		else
			return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity(new WebServiseError("Pattern not found")).build();
	}

	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response createPattern(PatternBasic patternBasic){
		System.out.println("createPattern");
		try {
			patternBasic.checkIt();
		} catch (NotReadyException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(new WebServiseError(e.getMessage())).build();
		}
		try {
			Pattern pattern = patternDao.createPattern(patternBasic);
			GenericEntity<Pattern> entity = new GenericEntity<Pattern>(pattern) {};
			System.out.println("Seems fine"+pattern);
			return Response.ok(entity).build();
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
	}
	
	// just for testing purposes
	@DELETE
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response deleteAllPatterns(){
		System.out.println("deleteALLPatterns");
		try {
			patternDao.deleteAllPatterns();
			return Response.noContent().build();
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
	}

	@DELETE
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response deletePattern(@PathParam("id") String id){
		System.out.println("deletePattern");
		try {
			int x = patternDao.deletePattern(id);
			if  ( x == 0 ) {
				return Response.noContent().build();
			}
			else {
				return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity(new WebServiseError("Pattern not found")).build();
			}
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
	}

}
