package services;

final public class DAOFactory {

	/*
	 Returns all DAO instances.
	 */
	TemplateDAOfile getTemplateDAO(){
		String fileName = "Templates15.dat";
		return new TemplateDAOfile(fileName);
	}

	PatternDAOfile getPatternDAO(){
		String fileName = "Patterns15.dat";
		return new PatternDAOfile(fileName);
	}

	InstanceDAOfile getInstanceDAO(){
		String fileName = "Instance15.dat";
		return new InstanceDAOfile(fileName);
	}
}