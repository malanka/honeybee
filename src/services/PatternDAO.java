package services;

import java.util.List;

import serviceerrors.InternalErrorException;

public interface PatternDAO {
	
	public List<Pattern> getAllPatterns() throws InternalErrorException;

	public Pattern getPatternById(String id) throws InternalErrorException;

	public Pattern createPattern(PatternBasic patternBasic) throws InternalErrorException;
	
	public void deleteAllPatterns() throws InternalErrorException ;

	int deletePattern(String id) throws InternalErrorException;
}
