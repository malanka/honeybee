package services;

import java.util.List;

import services.InstanceBP.InstanceState;

public interface InstanceDAO {

	public List<InstanceBP> getAllInstances(String patternId, InstanceState state) throws InternalErrorException;

	public InstanceBP getInstanceById(String id);

	public InstanceBP createInstance(String patternId) throws InternalErrorException;
}
