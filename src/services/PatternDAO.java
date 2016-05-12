package services;

import java.util.List;

import serviceerrors.InternalErrorException;

public interface PatternDAO {
	
	public List<Pattern> getAllPatterns();

	public Pattern getPatternById(String id);

	public Pattern createPattern(String templateId, PatternBasic patternBasic) throws InternalErrorException;
	
	public void deleteAllPatterns() throws InternalErrorException ;
}
