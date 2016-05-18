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

import businessentities.Pattern;
import businessentities.PatternBasic;
import businessentities.PatternHole;
import businessentities.Template;
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
			throw new InternalErrorException(e.getMessage());
		} catch (IOException e) {
			throw new InternalErrorException(e.getMessage());
		}
	}
	
	private List<Pattern> readPatternList () throws InternalErrorException {
		File file = new File(fileName);
		if ( !file.exists() ) {
			throw new InternalErrorException("File '" + fileName + "' doesn't exist");
		}

		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			List<Pattern> patternList = (List<Pattern>) ois.readObject();
			ois.close();
			return patternList;
		} catch (IOException e) {
			throw new InternalErrorException(e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new InternalErrorException(e.getMessage());
		}
	}

	@Override
	public List<Pattern> getAllPatterns() throws InternalErrorException{
		return readPatternList();
	}

	@Override
	public void deleteAllPatterns() throws InternalErrorException {		
		savePatternList(new ArrayList<Pattern>());	
	}

	@Override
	public Pattern getPatternById(String id) throws InternalErrorException{
		List<Pattern> patternList = readPatternList();
		Pattern tmp = new Pattern();
		tmp.setId(id);
		int index = patternList.indexOf(tmp);
		if ( index != -1 ) {
			return patternList.get(index);
		}
		return null;
	}

	@Override
	public Pattern createPattern(PatternBasic patternBasic) throws InternalErrorException  {
		// find a template
		TemplateDAOfile templateDao = new DAOFactory().getTemplateDAO();
		List<Template> templates = templateDao.getAllTemplates();
		Template template = new Template();
		template.setId(patternBasic.getTemplateId());
		int x = templates.indexOf(template);
		if ( x == -1) {
			// TODO change exception
			throw new InternalErrorException("Such template doesn't exist");
		}

		List<Pattern> patternList = readPatternList();
		// check, that name is not used
		for ( Pattern aPattern : patternList ) {
			if ( aPattern.getTemplate().equals(patternBasic.getTemplateId()) && aPattern.getName().equals(patternBasic.getName()) ) {
				// TODO change exception
				throw new InternalErrorException("Such pattern name is already used for the template");
			}
		}

		template = templates.get(x);
		// TODO create a constructor
		Pattern pattern = new Pattern();
		pattern.setName(patternBasic.getName());
		pattern.setTemplate(patternBasic.getTemplateId());
		pattern.setId(PatternDAOfile.getNewId());
		pattern.setHolesFromTemplate(template.getHoles());
		pattern.generateLinks();
		patternList.add(pattern);
		savePatternList(patternList);
		return pattern;
	}

	@Override
	public int deletePattern(String id) throws InternalErrorException {
		List<Pattern> patternList = readPatternList();
		Pattern tmp = new Pattern();
		tmp.setId(id);
		if ( patternList.remove(tmp) ) {
			savePatternList(patternList);
			return 0;
		}
		else {
			return 1;
		}	
	}
	
	@Override
	public PatternHole assignPatternToHole(String patternId, String holeName, String assignedPattern) throws InternalErrorException  {
		List<Pattern> patternList = readPatternList();
		Pattern tmp = new Pattern();
		tmp.setId(patternId);
		int index = patternList.indexOf(tmp);
		if ( index == -1 ) {
			return null;
		}
		Pattern pattern = patternList.get(index);
		if ( pattern.getHoles() == null ) {
			System.out.println("This pattern doesn't have holes");
			return null;
		}
		// let's check its holes
		for ( PatternHole ahole : pattern.getHoles() ) {
			if ( ahole.getName().equals(holeName) ) {
				// we found a hole
				
				// lets check if assigned pattern exists
				tmp.setId(assignedPattern);
				index = patternList.indexOf(tmp);
				if ( index == -1 ) {
					throw new InternalErrorException("assigned pattern id doesn't exist");
				}

				// lets set a new pattern
				ahole.setPatternAssigned(assignedPattern);
				// save it
				savePatternList(patternList);
				return ahole;
			}
		}
		// such hole doesn't exist
		return null;
	}
}