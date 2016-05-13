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

@Path("/templates")
public class TemplateService {

	private static final String base = "http://localhost:9000/BP_REST_API/rest";
	
	private TemplateDAOfile templateDao;

	public TemplateService() throws InternalErrorException {
		templateDao = new DAOFactory().getTemplateDAO();
	}
	
	public static String getTemplateURI(String templateId){
		return base + "/templates/" + templateId;
	};

	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getTemplates(){
		System.out.println("getTemplates");
		try {
			List<Template> tmp = templateDao.getAllTemplates();
			GenericEntity<List<Template>> entity = new GenericEntity<List<Template>>(tmp) {};
			return Response.ok().entity(entity).build();
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
	}

	// just for testing purposes
	@DELETE
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response deleteAllTemplates(){
		System.out.println("deleteAllTemplates");
		try {
			templateDao.deleteAllTemplates();
			return Response.noContent().build();
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response createTemplate(Template template){
		System.out.println("postTemplates");
		if ( template == null ) {
			return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(new WebServiseError("Request document doesn't fit the expected format")).build();
		}
		if ( template.getEngine() == null || !template.getEngine().isSet() ) {
			return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(new WebServiseError("Property \"engine\" is required to be totally filled")).build();
		}
		// TODO links check, may be remove them
		// TODO check holes, unique names
		try {
			GenericEntity<Template> entity = new GenericEntity<Template>(templateDao.createTemplate(template)) {};
			return Response.ok(entity).build();
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
	}

	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getTemplate(@PathParam("id") String id) {
		System.out.println("getTemplate");
		Template template = null;
		try {
			template = templateDao.getTemplateById(id);
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
		if ( template != null ) {
			GenericEntity<Template> entity = new GenericEntity<Template>(template) {};
			return Response.ok(entity).build();
		}
		else
			return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity(new WebServiseError("Template not found")).build();
	}
	
	@DELETE
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response deleteTemplate(@PathParam("id") String id){
		System.out.println("deleteTemplate");
		try {
			int x = templateDao.deleteTemplate(id);
			if  ( x == 0 ) {
				return Response.noContent().build();
			}
			else {
				return Response.status(HttpURLConnection.HTTP_NOT_FOUND).entity(new WebServiseError("Template not found")).build();
			}
		} catch (InternalErrorException e) {
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
	}
	/*
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
			e.printStackTrace();
			return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(new WebServiseError(e.getMessage())).build();
		}
	}
	*/
}
