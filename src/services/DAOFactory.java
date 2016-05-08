package services;

final public class DAOFactory {

	/*
	 Returns all DAO instances.
	 */
	TemplateDAOfile getTemplateDAO(){
		String fileName = "Templates16.dat";
		return new TemplateDAOfile(fileName);
	}

	PatternDAO getPatternDAO(){
		String fileName = "Patterns16.dat";
		return new PatternDAOfile(fileName);
	}

	InstanceDAO getInstanceDAO(){
		String fileName = "Instance16.dat";
		return new InstanceDAOfile(fileName);
	}
}