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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/patterns")
public class PatternService {

	private PatternDAOfile patternDao = new DAOFactory().getPatternDAO();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPatterns(){
		System.out.println("getPatterns");
		List<Pattern> tmp = patternDao.getAllPatterns();
		GenericEntity<List<Pattern>> entity = new GenericEntity<List<Pattern>>(tmp) {};
		return Response.ok().entity(entity).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getPattern(@PathParam("id") String id) {
		System.out.println("getPattern");
		Pattern pattern = patternDao.getPatternById(id);
		if ( pattern != null ) {
			GenericEntity<Pattern> entity = new GenericEntity<Pattern>(patternDao.getPatternById(id)) {};
			return Response.ok(entity).build();
		}
		else
			return Response.status(404).entity("<error> Element not found </error>").build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Response createPattern(@QueryParam("templateId") String templateId, PatternBasic patternBasic){
		System.out.println("createPattern");
		if ( templateId == null ) {
			return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity("<error>parameter templateId is required/error>").build();
		}
		try {
			Pattern pattern = patternDao.createPattern(templateId, patternBasic);
			GenericEntity<Pattern> entity = new GenericEntity<Pattern>(pattern) {};
			System.out.println("Seems fine");
			return Response.ok(entity).build();
		} catch (InternalErrorException e) {
			System.out.println(e.getMessage());
			return Response.status(500).entity("<error>" + e.getMessage() + "</error>").build();
		}
	}
	/*
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
