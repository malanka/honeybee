package services;


import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/templates")
public class TemplateService {

	TemplateDAO templateDao = new TemplateDAO();

	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<Template> getTemplates(){
		System.out.println("getTemplates");
		return templateDao.getAllTemplates();
	}

	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Response createTemplate(Template template){
		// TODO links check, may be remove them
		// TODO check holes, unique names
		System.out.println("postTemplates");
		GenericEntity<Template> entity = new GenericEntity<Template>(templateDao.createTemplate(template)) {};
		return Response.ok(entity).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getTemplate(@PathParam("id") String id) {
		System.out.println("getTemplate");
		Template template = templateDao.getTemplateById(id);
		if ( template != null ) {
			GenericEntity<Template> entity = new GenericEntity<Template>(templateDao.getTemplateById(id)) {};
			return Response.ok(entity).build();
		}
		else
			return Response.status(404).entity("<error> Element not found </error>").build();
	}
	
	@DELETE
	@Path("{id}")
	//@Produces(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteTemplate(@PathParam("id") String id){
		System.out.println("deleteTemplate");
		if ( templateDao.deleteTemplate(id) == 0 )
			return Response.noContent().build();
		else
			return Response.status(404).entity("Element not found").build();
		// TODO fix error reporting
	}
	
	@PUT
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public Response updateTemplate(@PathParam("id") String id, Template template) {
		System.out.println("putTemplate");
		int rv = templateDao.updateTemplateById(id, template);
		switch (rv) {
			case 0:
				GenericEntity<Template> entity = new GenericEntity<Template>(template) {};
				return Response.ok(entity).build();
			case 1:
				return Response.status(404).entity("<error>Element not found<error/>").build();
			case -1:
				return Response.status(500).entity("<error> internal </error>").build();
		}
		return Response.status(500).entity("<error> internal </error>").build();
	}
}
