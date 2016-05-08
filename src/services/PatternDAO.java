package services;

import java.util.List;

public interface PatternDAO {
	
	public List<Pattern> getAllPatterns();

	public Pattern getPatternById(String id);

	public Pattern createPattern(String templateId, PatternBasic patternBasic) throws InternalErrorException;
}
