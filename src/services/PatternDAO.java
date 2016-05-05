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

import javax.xml.bind.annotation.XmlElement;

public class PatternDAO {
	private static final String fileName = "Pattern4.dat";

	public List<Pattern> getAllPatterns(){
		List<Pattern> patternList = null;
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				ArrayList <PatternHole> holes= new ArrayList<PatternHole>();
				PatternHole hole1 = new PatternHole("holename1","asd","sdf","sdf","sdf","pattenrparetnURI",null,null);
				PatternHole hole2 = new PatternHole("holename2","asd","sdf","sdf","sdf","pattenrparetnURI",null,null);
				holes.add(hole1);
				holes.add(hole2);
				Pattern pattern1 = new Pattern("1","pattern1", "URI template11", holes);
				Pattern pattern2 = new Pattern("2","pattern1", "URI template11", holes);
				patternList = new ArrayList<Pattern>();
				patternList.add(pattern1);
				patternList.add(pattern2);
				savePatternList(patternList);
			}
			else{
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				patternList = (List<Pattern>) ois.readObject();
				ois.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return patternList;
	}

	private void savePatternList(List<Pattern> templateList){
		try {
			File file = new File(fileName);
			FileOutputStream fos;

			fos = new FileOutputStream(file);

			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(templateList);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Pattern getPatternById(String id){
		List<Pattern> patternList = null;
		Pattern pattern = null;
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				return pattern;	
			}
			else{
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);
				patternList = (List<Pattern>) ois.readObject();
				ois.close();
			}
			Pattern tmp = new Pattern();
			tmp.setId(id);
			int index = patternList.indexOf(tmp);
			if ( index != -1 )
				pattern = patternList.get(index);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
		return pattern;
	}
/*
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
			templateList.add(template);
			saveTemplateList(templateList);
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
	}*/
}