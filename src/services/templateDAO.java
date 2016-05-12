package services;

import java.util.List;

import serviceerrors.InternalErrorException;

public interface templateDAO {
	
	public List<Template> getAllTemplates() throws InternalErrorException;

	public Template getTemplateById(String id);

	public Template createTemplate(Template template);

	public int deleteTemplate(String id);

	public Template updateTemplateById(String id, Template newRepresentation) throws InternalErrorException;
	
	public void deleteAllTemplates() throws InternalErrorException;
}