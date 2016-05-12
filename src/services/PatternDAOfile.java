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

public class PatternDAOfile implements PatternDAO {

	private static int patternId = 1;

	private String fileName = null;

	public PatternDAOfile(String fileName) throws InternalErrorException {
		super();
		this.fileName = fileName;
		File file = new File(fileName);
		if ( !file.exists() ) {
			savePatternList(new ArrayList<Pattern>());
		}
		// TODO need a start id point!
	}

	private static String getNewId() {
		String id = "" + patternId;
		patternId++;
		return id;
	}

	private void savePatternList(List<Pattern> patternList) throws InternalErrorException{
		try {
			File file = new File(fileName);
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(patternList);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new InternalErrorException(e.getMessage());
		}
	}
	
	private List<Pattern> readPatternList () throws InternalErrorException {
		File file = new File(fileName);
		if ( !file.exists() )
			throw new InternalErrorException("File '" + fileName + "' doesn't exist");
		
		List<Pattern> patternList = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			patternList = (List<Pattern>) ois.readObject();
			ois.close();
			return patternList;
		} catch (IOException e) {
			e.printStackTrace();
			throw new InternalErrorException(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException(e.getMessage());
		}
	}

	@Override
	public List<Pattern> getAllPatterns() throws InternalErrorException{
		return readPatternList();
	}

	@Override
	public void deleteAllPatterns() throws InternalErrorException {		
		File file = new File(fileName);
		if ( !file.exists() )
			throw new InternalErrorException("File '" + fileName + "' doesn't exist");
		savePatternList(new ArrayList<Pattern>());	
	}

	@Override
	public Pattern getPatternById(String id) throws InternalErrorException{
		List<Pattern> patternList = readPatternList();
		Pattern pattern = null;
		Pattern tmp = new Pattern();
		tmp.setId(id);
		int index = patternList.indexOf(tmp);
		if ( index != -1 )
			pattern = patternList.get(index);
		return pattern;
	}

	@Override
	public Pattern createPattern(String templateId, PatternBasic patternBasic) throws InternalErrorException  {
		File file = new File(fileName);
		if ( !file.exists() )
			throw new InternalErrorException("File '" + fileName + "' doesn't exist");

		List<Pattern> patternList = null;
		try{
			// read all patterns
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			patternList = (List<Pattern>) ois.readObject();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new InternalErrorException(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException(e.getMessage());
		}
		// check, that name is not used
		for ( Pattern aPattern : patternList ) {
			if ( aPattern.getTemplate().equals(templateId) && aPattern.getName().equals(patternBasic.getName()) ) {
				// TODO change exception
				throw new InternalErrorException("Such pattern name is already used");
			}
		}
		// find a template
		TemplateDAOfile templateDao = new DAOFactory().getTemplateDAO();
		List<Template> templates = templateDao.getAllTemplates();
		Template template = new Template();
		template.setId(templateId);
		int x = templates.indexOf(template);
		if ( x == -1) {
			// TODO change exception
			throw new InternalErrorException("Such template doesn't exist");
		}
		template = templates.get(x);
		// TODO create a constructor
		Pattern pattern = new Pattern();
		pattern.setName(patternBasic.getName());
		pattern.setTemplate(templateId);
		pattern.setId(PatternDAOfile.getNewId());
		pattern.setHolesFromTemplate(template.getHoles());
		pattern.generateLinks();
		patternList.add(pattern);
		savePatternList(patternList);
		return pattern;
	}
	/*
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
	}*/
}