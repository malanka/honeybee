package services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import services.InstanceBP.InstanceState;

public class InstanceDAOfile implements InstanceDAO{

	private static int instanceId = 1;

	private String fileName = null;

	public InstanceDAOfile(String fileName) {
		super();
		this.fileName = fileName;
		File file = new File(fileName);
		if ( !file.exists() ) {
			saveInstanceList(new ArrayList<InstanceBP>());
		}
	}

	private static String getNewId() {
		String id = "" + instanceId;
		instanceId++;
		return id;
	}

	private void saveInstanceList(List<InstanceBP> instanceList){
		try {
			File file = new File(fileName);
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(instanceList);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<InstanceBP> getAllInstances(String patternId, InstanceState state) throws InternalErrorException {
		File file = new File(fileName);
		if ( !file.exists() )
			throw new InternalErrorException("File '" + fileName + "' doesn't exists");
		
		List<InstanceBP> instanceList = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			instanceList = (List<InstanceBP>) ois.readObject();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new InternalErrorException(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException(e.getMessage());
		}
		if ( ( state == null) && ( patternId == null ) ) {
			// no filters were applied
			return instanceList;
		}
		instanceList.removeIf(s-> (s.getState()!=state && !s.getPatternId().equals(patternId)));
		return instanceList;
	}

	@Override
	public InstanceBP createInstance(String patternId) throws InternalErrorException {
		File file = new File(fileName);
		if ( !file.exists() )
			throw new InternalErrorException("File '" + fileName + "' doesn't exist");

		List<InstanceBP> instanceList = null;
		try{
			// read all instances
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			instanceList = (List<InstanceBP>) ois.readObject();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new InternalErrorException(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException(e.getMessage());
		}
		// find a pattern
		PatternDAO patternDao = new DAOFactory().getPatternDAO();
		List<Pattern> patterns = patternDao.getAllPatterns();
		Pattern pattern = new Pattern();
		pattern.setId(patternId);
		int x = patterns.indexOf(pattern);
		if ( x == -1) {
			// TODO change exception
			throw new InternalErrorException("Such pattern doesn't exist");
		}
		pattern = patterns.get(x);
		InstanceBP instance = new InstanceBP(getNewId(), null, null, patternId, pattern.getHoles(), pattern.getTemplate());
		instanceList.add(instance);
		saveInstanceList(instanceList);
		
		ObjectMapper mapper = new ObjectMapper();
		//Object to JSON in String
		try {
			String jsonInString = mapper.writeValueAsString(instance);
			System.out.println("Created instanceJSON=" + jsonInString);
			System.out.println("Created instanceString=" + instance);
			String xmlString = "";
		    try {
		        JAXBContext context = JAXBContext.newInstance(InstanceBP.class);
		        Marshaller m = context.createMarshaller();

		        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML

		        StringWriter sw = new StringWriter();
		        m.marshal(instance, sw);
		        xmlString = sw.toString();
		        System.out.println("Created instanceXML=" + xmlString);
		    } catch (JAXBException e) {
		        e.printStackTrace();
		    }
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return instance;
	}
/*
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

	public Pattern createPattern(String templateId, PatternBasic patternBasic) throws InternalErrorException  {

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



	@Override
	public InstanceBP getInstanceById(String id) {
		// TODO Auto-generated method stub
		return null;
	}


}
