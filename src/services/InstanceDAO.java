package services;

import java.util.List;

import businessentities.InstanceBP;
import businessentities.InstanceHole;
import businessentities.InstanceState;
import serviceerrors.InternalErrorException;

public interface InstanceDAO {

	public List<InstanceBP> getAllInstances(String patternId, InstanceState state) throws InternalErrorException;

	public InstanceBP getInstanceById(String id) throws InternalErrorException;

	public InstanceBP createInstance(String patternId) throws InternalErrorException;
	
	public void deleteAllInstances() throws InternalErrorException;

	public InstanceBP changeInstanceStateById(String id, InstanceState instanceState) throws InternalErrorException;

	public InstanceBP updateInstance(InstanceBP instance) throws InternalErrorException;

	public InstanceHole assignPatternToHole(String instanceId, String holeName, String assigned_pattern_id) throws InternalErrorException;
}
