package services;

import java.net.HttpURLConnection;
import java.util.ArrayList;
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

import businessentities.HoleManipulation;
import businessentities.Pattern;
import businessentities.PatternBasic;
import businessentities.PatternHole;
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
		if ( pattern == null ) {
			return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity(new WebServiseError("Pattern not found")).build();
		}
		GenericEntity<Pattern> entity = new GenericEntity<Pattern>(pattern) {};
		return Response.ok(entity).build();
	}

	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response createPattern(PatternBasic patternBasic){
		System.out.println("createPattern");
		if ( patternBasic == null ) {
			return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(new WebServiseError("Request document doesn't fit the expected format")).build();
		}
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

	@POST
	@Path("{id}/holes/{holename}")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response assignPatternToHole(@PathParam("id") String patternId, @PathParam("holename") String holeName, HoleManipulation holeManipulation) {
		System.out.println("assignPatternToHole");
		PatternHole patternHole = null;
		try {
			patternHole = patternDao.assignPatternToHole(patternId, holeName, holeManipulation.getAssigned_pattern_id());
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
		if ( patternHole == null ) {
			return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity(new WebServiseError("Pattern or hole not found")).build();
		}
		GenericEntity<PatternHole> entity = new GenericEntity<PatternHole>(patternHole) {};
		return Response.ok().entity(entity).build();
	}

	@GET
	@Path("{id}/holes")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getHoleList(@PathParam("id") String patternId) {
		System.out.println("getHoleList");
		Pattern pattern = null;
		try {
			pattern = patternDao.getPatternById(patternId);
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
		if ( pattern == null ) {
			return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity(new WebServiseError("Pattern not found")).build();
		}
		if ( pattern.getHoles() == null ) {
			GenericEntity<List<PatternHole>> entity = new GenericEntity<List<PatternHole>>(new ArrayList<PatternHole>()) {};
			return Response.ok(entity).build();
		}
		GenericEntity<List<PatternHole>> entity = new GenericEntity<List<PatternHole>>(pattern.getHoles()) {};
		return Response.ok(entity).build();
	}

	@GET
	@Path("{id}/holes/{holename}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getOneHole(@PathParam("id") String patternId, @PathParam("holename") String holeName) {
		System.out.println("getOneHole");
		Pattern pattern = null;
		try {
			pattern = patternDao.getPatternById(patternId);
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
		if ( pattern == null ) {
			return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity(new WebServiseError("Pattern not found")).build();
		}  
		List<PatternHole> patternHoles = pattern.getHoles();
		if ( patternHoles == null ) {
			return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity(new WebServiseError("Pattern hole not found")).build();
		}
		patternHoles.removeIf(s-> (!(s.getName().equals(holeName))));
		if ( patternHoles.isEmpty() ) {
			return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity(new WebServiseError("Pattern hole not found")).build();
		}
		// as name should be unique, in the list only one hole can survive
		GenericEntity<PatternHole> entity = new GenericEntity<PatternHole>(patternHoles.get(0)) {};
		return Response.ok(entity).build();
	}
}