package services;

import java.util.List;

public interface templateDAO {
	
	public List<Template> getAllTemplates();

	public Template getTemplateById(String id);

	public Template createTemplate(Template template);

	public int deleteTemplate(String id);

	public int updateTemplateById(String id, Template newRepresentation);
}