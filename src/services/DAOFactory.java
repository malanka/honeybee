package services;

final public class DAOFactory {

	/*
	 Returns all DAO instances.
	 */
	TemplateDAOfile getTemplateDAO(){
		String fileName = "Templates11.dat";
		return new TemplateDAOfile(fileName);
	}

	PatternDAOfile getPatternDAO(){
		String fileName = "Patterns11.dat";
		return new PatternDAOfile(fileName);
	}

}