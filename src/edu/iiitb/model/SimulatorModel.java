package edu.iiitb.model;

import edu.iiitb.view.MainUI;
import edu.iiitb.view.Process;
public class SimulatorModel {

	private static int numProcess;
	private static int numResource;

	private static int maximum[][];
	private static int request[][];
	private static int need[][];
	private static int available[];
	private static int allocation[][];
	private static int resource[];
	private static int safety[];
	private static int work[];
	private static int finish[];

	public static void setDataStrucure(int numProcesses, int numResources) {

		numProcess = numProcesses;
		numResource = numResources;
		
		String result = "Number of processes = " + numProcess + " Number of resources = " + numResource;
		
		maximum = new int[numProcess][numResource];
		request = new int[numProcess][numResource];
		need = new int[numProcess][numResource];
		available = new int[numResource];
		allocation = new int[numProcess][numResource];
		resource = new int[numResource];
		safety = new int[numProcess];
		work = new int[numResource];
		finish = new int[numProcess];
		
		for (int i = 0; i < available.length; i++) {
			available[i] = 1;
		}

		MainUI.statusbar.setText(result);

	}
	
	public static int[][] getMaximumDataStrucure(){
		return maximum;
	}
	
	public static int[][] getRequestDataStrucure(){
		return request;
	}
	
	public static int[][] getNeedDataStrucure(){
		return need;
	}
	
	public static int[] getAvailableDataStrucure(){
		return available;
	}
	
	public static int[][] getAllocationDataStrucure(){
		return allocation;
	}

	public static String setMaximum(int processId, int resourceId, int value) {
		maximum[processId][resourceId] = value;
		need[processId][resourceId] = value;

		return "Success";
	}

	public static String setAvailable(int resourceId, int value) {
		work[resourceId]=value;
		available[resourceId] = value;
		return "Success";

	}

	public static String resourceRequest(int processId, int resourceId) {
		//System.out.println("NEED ::" + need[processId][resourceId]);
		request[processId][resourceId]++;
		//System.out.println("Request:::"+request[processId][resourceId]+ processId+"  " +resourceId);
		//System.out.println("REQ ::" + request[processId][resourceId]);
		if (need[processId][resourceId] == 0)
			return "Request is greater than the need";
		if (available[resourceId] == 0)
			return "Request is greater than the available";
		available[resourceId]--;
		allocation[processId][resourceId]++;
		need[processId][resourceId]--;

		return "Success";

	}
	
	
	
	public static int assignRequests(int processId, int resourceId,
			int numberOfInstances) {
		if (((request[processId][resourceId] + 1) > numberOfInstances))
			return 1;

		request[processId][resourceId]++;
		if (available[resourceId] == 0) {
			need[processId][resourceId] = request[processId][resourceId]
					- allocation[processId][resourceId];
			return 2;
		}

		available[resourceId]--;
		allocation[processId][resourceId]++;
		need[processId][resourceId] = request[processId][resourceId]
				- allocation[processId][resourceId];

		return 3;

	}
	
	public static boolean findSafetySeq(int processId,int resourceId){
		int c = 0;
		int temp = 0;
		int count = 0;
		
		// initialising finish matrix 
		for (int i = 0; i < numProcess; i++) {			
			finish[i] = 0;
		}
		
		for (int i = 0; i <numProcess; i++) {
			for (int z = 0; z <numResource; z++)
				work[z] = work[z] - request[i][z];
		}

		
		for (int i = 0; i <numProcess; i++) {
			for (int x = 0; x<numResource; x++) {  
				//System.out.println("need:" + need[i][x]+" work"+work[x]);
				if (need[i][x] <= work[x])
					temp = 1;
				else {
					temp = 0;
					break;
				}
			}

			if (finish[i] == 0 && temp == 1) {
				for (int j = 0; j < numResource; j++)
					work[j] = work[j] + allocation[i][j];
				finish[i] = ++c;
			} else
				continue;
		}
		
		for (int k = 0; k <numProcess; k++) {
			if (finish[k] == 0)
				count++;
		}
		
		if (count > 0) {
			int fact = findfact(count);

			for (int f = 0; f < fact; f++) {
				for (int k = 0; k <numProcess; k++)
					if (finish[k] == 0) {
						for (int x = 0; x < numResource; x++) {
							if (need[k][x] <= work[x])
								temp = 1;
							else {
								temp = 0;
								break;
							}

						}
						if (temp == 0)
							continue;
						else {
							for (int j = 0; j < numResource; j++)
								work[j] = work[j] + allocation[k][j];
							finish[k] = ++c;
						}
					} else
						continue;

			}
		}
		
		
		for (int i = 0; i < numProcess; i++) {
			if (finish[i] == 0) 
				return false;
		}		
		return true;
	}
	
	public static int[] giveSafetyseq(){
		return finish;		
	}
	
	public static String getSafetySeq() {
		String seq = ""; 
		for (int index = 1; index <= finish.length; index++) {
			int process = 0;
			while (process < finish.length) {
				if (finish[process] == index) {
					seq = seq + "P"+process+ " ";
					break;
				}
				else
					process++;
			}
		}
		return seq;
	}
	
	public static int findfact(int f){
		if (f <= 1)
			return 1;
		else
			return f * findfact(f - 1);

	}
	public static String multishot(int processId, int resourceId){
		int flag=0;
		String status="";
		request[processId][resourceId]++;
		if (need[processId][resourceId] == 0)
		{
			status="invalid1";
			return status;
		}
		
		for(int i=0;i<processId;i++){
			for(int j=0;j<numResource;j++){
				//System.out.println(maximum[i][j]+" "+allocation[i][j]);
			if(maximum[i][j]!=allocation[i][j]){
				flag=1;
				status="invalid2";
				//System.out.println("1st condition");
				return status;
			}
			}
		}
		if(flag==0)
		{
			if(available[resourceId]>=1){
			
				status="available";
				available[resourceId]--;
				//System.out.println(available[resourceId]);
				allocation[processId][resourceId]++;
				need[processId][resourceId]--;
			}
			else
			{
				status="pending";
			}
		}
		return status;
	}

	
	/**
	 * This method will detect whether the system has entered a deadlock state,
	 * if no deadlock the will return the safety sequence else will return an error message
	 */
	public static String detectDeadlock() {

		int t = 0;
		int count, count1;
		int flag;
		count = 0;
		count1 = 0;
		int[] order = new int[numProcess];
		int[] flags = new int[numProcess];
		int[][] temp_need= new int[numProcess][numResource];
		int[] temp_avail=new int[numResource];
		
		for(int i=0;i<numProcess;i++)
		{
			for(int j=0;j<numResource;j++)
			{
				temp_need[i][j]=need[i][j];
			}
		}
		
		for(int i=0;i<numResource;i++)
		{
			temp_avail[i]=available[i];
		}

		for (int i = 0; i < numProcess; i++)
			flags[i] = -1;

		while (!(count > numProcess || count1++ > numProcess)) {
			count1++;
			for (int i = 0; i < numProcess; i++) {
				if (flags[i] == -1) // if flags[i]==-1 means process is not inserted
				{
					flag = 0;
					for (int j = 0; j < numResource; j++) {
						if (temp_need[i][j] > temp_avail[j]) {
							flag = 1; // check whether each resource is
										// satisfying given condition
						}

					}
					if (flag == 0) {
						for (int j = 0; j < numResource; j++) {
							temp_avail[j] = temp_avail[j] + allocation[i][j]; 
						}
						count++;
						order[t++] = i; // executed process’s order is stored
						flags[i] = 1; // flags[i]=1 marks process executed
					}

				}
			}
		}

		if (count < numProcess)
			return "System is in Deadlock";

		String result = "Safe Sequence is ";
		for (int i = 0; i < numProcess; i++) {
			result += "P" + order[i] + " ";
		}

		return result;

	}

	public static void recoverSytemFromDeadlock() {
		
		String result="";
		//int i=0;
		while(detectDeadlock().equals("System is in Deadlock"))
		{
			result+=" P"+(numProcess-1);
			MainUI.removeProcess();
		    removeOneProcessFromSystem();
		    MainUI.ui.repaint();
		}
		MainUI.statusbar.setText("Pre-empting process"+result);
		
	}

	private static void removeOneProcessFromSystem() {
		
		
		
		MainUI.processList.remove(MainUI.processList.size()-1);
		numProcess--;
		int[][] temp_maximum = new int[numProcess][numResource];
		int[][] temp_request = new int[numProcess][numResource];
		int[][] temp_need = new int[numProcess][numResource];
		//int[] temp_available = new int[numResource];
		int[][] temp_allocation = new int[numProcess][numResource];
		//int[] temp_resource = new int[numResource];
		int[] temp_safety = new int[numProcess];
		//int[] temp_work = new int[numResource];
		int[] temp_finish = new int[numProcess];
		
		for(int i=0;i<numResource;i++)
		{
			available[i]+=allocation[numProcess][i];
		}
		
		for(int i=0;i<numProcess;i++)
		{
			for(int j=0;j<numResource;j++)
			{
				temp_maximum[i][j]=maximum[i][j];
				temp_request[i][j]=request[i][j];
				temp_need[i][j]=need[i][j];
				temp_allocation[i][j]=allocation[i][j];
				
			}
		}
		maximum=temp_maximum;
		request=temp_request;
		need=temp_need;
		allocation=temp_allocation;
		
		
		/*
		for(int i=0;i<numResource;i++)
		{
			
			temp_available[i]=available[i];
			temp_resource[i]=resource[i];
			temp_work[i]=work[i];
			
		}*/
		
		
		for(int i=0;i<numProcess;i++)
		{
			
			temp_safety[i]=safety[i];
			temp_finish[i]=finish[i];
						
		}
		
		safety=temp_safety;
		finish=temp_finish;
		
	}
	


}
 
