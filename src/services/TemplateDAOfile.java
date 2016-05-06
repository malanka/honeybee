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

public class TemplateDAOfile implements templateDAO{
	private String fileName = null;

	public TemplateDAOfile(String fileName) {
		super();
		this.fileName = fileName;
		File file = new File(fileName);
		if ( !file.exists() ) {
			saveTemplateList(new ArrayList<Template>());
		}
	}

	public List<Template> getAllTemplates(){
		List<Template> templateList = null;
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				WS ws1 = new WS("SOME_URI1","POST");
				WS ws2 = new WS("SOME_URI2","POST");
				ArrayList <Hole> holes= new ArrayList<Hole>();
				Hole hole1 = new Hole("holename1","as","asd","ad","aasd");
				Hole hole2 = new Hole("holename1","as","asd","ad","aasd");
				holes.add(hole1);
				holes.add(hole2);
				Template template1 = new Template("1", "FirstTemplate", "data_inQQ", "data_outQQ", "event_inQQ", "even_outOO", null, ws1);
				Template template2 = new Template("2", "SecondTemplate", "data_inQQ", "data_outQQ", "event_inQQ", "even_outOO", holes, ws2);
				templateList = new ArrayList<Template>();
				templateList.add(template1);
				templateList.add(template2);
				saveTemplateList(templateList);		
			}
			else{
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				templateList = (List<Template>) ois.readObject();
				ois.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
		return templateList;
	}

	private void saveTemplateList(List<Template> templateList){
		try {
			File file = new File(fileName);
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(templateList);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Template getTemplateById(String id){
		List<Template> templateList = null;
		Template template = null;
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				return template;	
			}
			else{
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				templateList = (List<Template>) ois.readObject();
				ois.close();
			}
			Template tmp = new Template();
			tmp.setId(id);
			int index = templateList.indexOf(tmp);
			if ( index != -1 )
				template = templateList.get(index);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
		return template;
	}

	public Template createTemplate(Template template) {
		List<Template> templateList = null;
		try {
			File file = new File(fileName);
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				templateList = (List<Template>) ois.readObject();
				ois.close();
			}
			// TODO check, the name of the template to be unique
			System.out.println("OldListsize="+templateList.size());
			templateList.add(template);
			saveTemplateList(templateList);
			System.out.println("NewListsize="+templateList.size());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
		return template;
	}

	public int deleteTemplate(String id) {
		List<Template> templateList = null;
		try {
			File file = new File(fileName);
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				templateList = (List<Template>) ois.readObject();
				ois.close();
			}
			Template tmp = new Template();
			tmp.setId(id);
			if ( templateList.remove(tmp) ) {
				saveTemplateList(templateList);
				return 0;
			}
			else
				return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return -1;
		}	
	}

	public Template updateTemplateById(String id, Template newRepresentation) throws InternalErrorException {
		File file = new File(fileName);
		if ( !file.exists() )
			throw new InternalErrorException("File '" + fileName + "' doesn't exist");

		List<Template> templateList = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			templateList = (List<Template>) ois.readObject();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new InternalErrorException(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException(e.getMessage());
		}
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
}