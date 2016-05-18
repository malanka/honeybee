package services;

import java.util.List;

import businessentities.Pattern;
import businessentities.PatternBasic;
import businessentities.PatternHole;
import serviceerrors.InternalErrorException;

public interface PatternDAO {
	
	public List<Pattern> getAllPatterns() throws InternalErrorException;

	public Pattern getPatternById(String id) throws InternalErrorException;

	public Pattern createPattern(PatternBasic patternBasic) throws InternalErrorException;
	
	public void deleteAllPatterns() throws InternalErrorException ;

	public int deletePattern(String id) throws InternalErrorException;

	public PatternHole assignPatternToHole(String patternId, String holeName, String assignedPattern)
			throws InternalErrorException;
}
