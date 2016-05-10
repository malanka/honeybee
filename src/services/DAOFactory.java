package services;

final public class DAOFactory {

	/*
	 Returns all DAO instances.
	 */
	TemplateDAOfile getTemplateDAO(){
		String fileName = "Templates18.dat";
		return new TemplateDAOfile(fileName);
	}

	PatternDAO getPatternDAO(){
		String fileName = "Patterns18.dat";
		return new PatternDAOfile(fileName);
	}

	InstanceDAO getInstanceDAO(){
		String fileName = "Instance18.dat";
		return new InstanceDAOfile(fileName);
	}
}