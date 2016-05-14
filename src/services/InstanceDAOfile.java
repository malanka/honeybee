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

import businessentities.InstanceBP;
import businessentities.InstanceState;
import businessentities.Pattern;
import serviceerrors.InternalErrorException;

public class InstanceDAOfile implements InstanceDAO{

	private static int instanceId = 1;

	private String fileName = null;

	public InstanceDAOfile(String fileName) throws InternalErrorException {
		super();
		this.fileName = fileName;
		File file = new File(fileName);
		if ( !file.exists() ) {
			saveInstanceList(new ArrayList<InstanceBP>());
		}
		// TODO id
	}

	private static String getNewId() {
		String id = "" + instanceId;
		instanceId++;
		return id;
	}

	private void saveInstanceList(List<InstanceBP> instanceList) throws InternalErrorException{
		try {
			File file = new File(fileName);
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(instanceList);
			oos.close();
		} catch (FileNotFoundException e) {
			throw new InternalErrorException(e.getMessage());
		} catch (IOException e) {
			throw new InternalErrorException(e.getMessage());
		}
	}

	private List<InstanceBP> readInstanceList () throws InternalErrorException {
		File file = new File(fileName);
		if ( !file.exists() )
			throw new InternalErrorException("File '" + fileName + "' doesn't exist");
		
		List<InstanceBP> instanceList = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			instanceList = (List<InstanceBP>) ois.readObject();
			ois.close();
			return instanceList;
		} catch (IOException e) {
			throw new InternalErrorException(e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new InternalErrorException(e.getMessage());
		}
	}

	@Override
	public void deleteAllInstances() throws InternalErrorException {		
		saveInstanceList(new ArrayList<InstanceBP>());	
	}
	
	@Override
	public List<InstanceBP> getAllInstances(String patternId, InstanceState state) throws InternalErrorException {
		List<InstanceBP> instanceList = readInstanceList();
		if ( ( state == null) && ( patternId == null ) ) {
			// no filters were applied
			return instanceList;
		}
		instanceList.removeIf(s-> (s.getState()!=state && !s.getPatternId().equals(patternId)));
		return instanceList;
	}

	@Override
	public InstanceBP createInstance(String patternId) throws InternalErrorException {
		List<InstanceBP> instanceList = readInstanceList();
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
		/*
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
		}*/
		return instance;
	}

	@Override
	public InstanceBP getInstanceById(String id) throws InternalErrorException{
		List<InstanceBP> instanceList = readInstanceList();
		InstanceBP tmp = new InstanceBP();
		tmp.setInstanceId(id);
		int index = instanceList.indexOf(tmp);
		if ( index != -1 ) {
			return instanceList.get(index);
		}
		return null;
	}

	@Override
	public InstanceBP putInstanceById(String id, InstanceState state) throws InternalErrorException {
		List<InstanceBP> instanceList = readInstanceList();
		InstanceBP tmp = new InstanceBP();
		tmp.setInstanceId(id);
		int index = instanceList.indexOf(tmp);
		if ( index != -1 ) {
			InstanceBP instance= instanceList.get(index);
			instance.setState(state);
			saveInstanceList(instanceList);
			return instance;
		}
		return null;
	}


}
