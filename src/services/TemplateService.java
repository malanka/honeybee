package services;


import java.net.HttpURLConnection;
import java.util.ArrayList;
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

	private static final String base = "http://BP_REST_API/rest";
	
	private TemplateDAOfile templateDao = new DAOFactory().getTemplateDAO();

	public static String getTemplateURI(String templateId){
		return base + "/templates/" + templateId;
	};

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTemplates(){
		System.out.println("getTemplates");
		List<Template> tmp = templateDao.getAllTemplates();
		GenericEntity<List<Template>> entity = new GenericEntity<List<Template>>(tmp) {};
		return Response.ok().entity(entity).build();
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
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTemplate(@PathParam("id") String id) {
		System.out.println("getTemplate");
		Template template = templateDao.getTemplateById(id);
		if ( template != null ) {
			GenericEntity<Template> entity = new GenericEntity<Template>(templateDao.getTemplateById(id)) {};
			return Response.ok(entity).build();
		}
		else
			return Response.status(404).entity(new WebServiseError("Element not found")).build();
	}
	
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Response deleteTemplate(@PathParam("id") String id){
		System.out.println("deleteTemplate");
		if ( templateDao.deleteTemplate(id) == 0 ) {
			return Response.noContent().build();
		}
		else
			return Response.status(404).entity(new WebServiseError("Element not found")).build();
		// TODO fix error reporting
	}
	
	@PUT
	@Path("{templateId}")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public Response updateTemplate(@PathParam("templateId") String templateId, Template template) {
		System.out.println("putTemplate");
		try {
			Template templateNew = templateDao.updateTemplateById(templateId,template);
			GenericEntity<Template> entity = new GenericEntity<Template>(templateNew) {};
			System.out.println("Seems fine");
			return Response.ok(entity).build();
		} catch (InternalErrorException e) {
			System.out.println(e.getMessage());
			return Response.status(500).entity(new WebServiseError(e.getMessage())).build();
		}
	}
}
