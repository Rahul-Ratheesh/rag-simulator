package edu.iiitb.controller;

import edu.iiitb.model.SimulatorModel;
import edu.iiitb.view.MainUI;


public class ResourceManager {
	
	public void setDataStructure(int numProcess, int numResource) {
		SimulatorModel.setDataStrucure(numProcess, numResource);
	}
	
	public void setMaximum(int processId, int resourceId, int value) {
		SimulatorModel.setMaximum(processId, resourceId, value);
	}
	public void setAvailable(int resourceId, int value) {
		SimulatorModel.setAvailable(resourceId, value);
	}
	
	
	public String callResourceRequestAlgo(int processId, int resourceId) {
		return SimulatorModel.resourceRequest(processId, resourceId);
	}
	
	public int assignRequests(int processId, int resourceId, int numberOfInstances) {
		return SimulatorModel.assignRequests(processId, resourceId, numberOfInstances);
	}
	
	
	
	public boolean findSafetySeq(int processId, int resourceId) {
		return SimulatorModel.findSafetySeq(processId, resourceId);
	}
	
	public String getSafetySeq() {
		return SimulatorModel.getSafetySeq();
		
	}
	
	public String multishot(int processId, int resourceId){
		return SimulatorModel.multishot(processId, resourceId);
	}

	public String detectDeadlock() {
		return SimulatorModel.detectDeadlock();
	}

	public void recoverSytemFromDeadlock() {
		// TODO Auto-generated method stub
		
		SimulatorModel.recoverSytemFromDeadlock();
		
		
	}
	

}
