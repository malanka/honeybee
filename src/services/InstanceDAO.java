package services;

import java.util.List;

import serviceerrors.InternalErrorException;
import services.InstanceBP.InstanceState;

public interface InstanceDAO {

	public List<InstanceBP> getAllInstances(String patternId, InstanceState state) throws InternalErrorException;

	public InstanceBP getInstanceById(String id) throws InternalErrorException;

	public InstanceBP createInstance(String patternId) throws InternalErrorException;
	
	public void deleteAllInstances() throws InternalErrorException;
}
