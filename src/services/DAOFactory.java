package services;

import serviceerrors.InternalErrorException;

public final class DAOFactory {

	/*
	 Returns all DAO instances.
	 */
	TemplateDAOfile getTemplateDAO() throws InternalErrorException{
		String fileName = "Templates18.dat";
		return new TemplateDAOfile(fileName);
	}

	PatternDAO getPatternDAO() throws InternalErrorException {
		String fileName = "Patterns18.dat";
		return new PatternDAOfile(fileName);
	}

	InstanceDAO getInstanceDAO() throws InternalErrorException{
		String fileName = "Instance18.dat";
		return new InstanceDAOfile(fileName);
	}
}