package services;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import serviceerrors.InternalErrorException;

public class TemplateDAOfile implements templateDAO{

	private String fileName = null;

	public TemplateDAOfile(String fileName) throws InternalErrorException {
		super();
		this.fileName = fileName;
		File file = new File(fileName);
		if ( !file.exists() ) {
			saveTemplateList(new ArrayList<Template>());
		}
		// TODO: id
	}

	private void saveTemplateList(List<Template> templateList) throws InternalErrorException{
		try {
			File file = new File(fileName);
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(templateList);
			oos.close();
		} catch (FileNotFoundException e) {
			throw new InternalErrorException(e.getMessage());
		} catch (IOException e) {
			throw new InternalErrorException(e.getMessage());
		}
	}

	private List<Template> readTemplateList () throws InternalErrorException {
		File file = new File(fileName);
		if ( !file.exists() )
			throw new InternalErrorException("File '" + fileName + "' doesn't exist");
		
		List<Template> templateList = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			templateList = (List<Template>) ois.readObject();
			ois.close();
			return templateList;
		} catch (IOException e) {
			throw new InternalErrorException(e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new InternalErrorException(e.getMessage());
		}
	}

	@Override
	public List<Template> getAllTemplates() throws InternalErrorException {
		return readTemplateList();		
	}

	@Override
	public void deleteAllTemplates() throws InternalErrorException {
		File file = new File(fileName);
		if ( !file.exists() )
			throw new InternalErrorException("File '" + fileName + "' doesn't exist");
		saveTemplateList(new ArrayList<Template>());	
	}	

	@Override
	public Template getTemplateById(String id) throws InternalErrorException{
		List<Template> templateList = readTemplateList();
		Template tmp = new Template();
		tmp.setId(id);
		int index = templateList.indexOf(tmp);
		if ( index != -1 ) {
			return templateList.get(index);
		}
		return null;
	}

	@Override
	public Template createTemplate(Template template) throws InternalErrorException {
		List<Template> templateList = readTemplateList();
		// TODO check, the name of the template to be unique
		templateList.add(template);
		saveTemplateList(templateList);
		return template;
		
	}

	@Override
	public int deleteTemplate(String id) throws InternalErrorException {
		List<Template> templateList = readTemplateList();
		Template tmp = new Template();
		tmp.setId(id);
		if ( templateList.remove(tmp) ) {
			saveTemplateList(templateList);
			return 0;
		}
		else {
			return 1;
		}
	}
/*
	@Override
	public Template updateTemplateById(String id, Template newRepresentation) throws InternalErrorException {
		List<Template> templateList = readTemplateList();
		for ( Template aTemplate : templateList ) {
			if ( !aTemplate.getId().equals(id) && aTemplate.getName().equals(newRepresentation.getName()) ) {
				throw new InternalErrorException("Such template name is already used");
			}
		}
		Template tmp = new Template();
		tmp.setId(id);
		int index = templateList.indexOf(tmp);
		if ( index == -1 ) {
			throw new InternalErrorException("Such template doesn't exist");
		}
		templateList.set(index, newRepresentation);
		saveTemplateList(templateList);
		return newRepresentation;
	}
	*/
}