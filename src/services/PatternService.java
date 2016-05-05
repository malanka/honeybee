package services;


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

@Path("/patterns")
public class PatternService {

	PatternDAO patternDao = new PatternDAO();

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
/*
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Response createTemplate(Template template){
		System.out.println("createTemplate");
		GenericEntity<Template> entity = new GenericEntity<Template>(templateDao.createTemplate(template)) {};
		return Response.ok(entity).build();
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
