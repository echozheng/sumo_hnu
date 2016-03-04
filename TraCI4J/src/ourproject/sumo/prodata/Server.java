package sumo.prodata;
import java.io.*;
import java.net.*;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import it.polito.appeal.traci.InductionLoop;
import it.polito.appeal.traci.SumoTraciConnection;
import it.polito.appeal.traci.Vehicle;

public class Server {
	@SuppressWarnings("resource")
	public static void main(String[] args){
		ServerSocket sserver = null;
		InputStream inserver = null;
		OutputStream outserver = null;
		
		Random r = new Random();
		int n = r.nextInt(10);
		int c = r.nextInt(100);
		int time = 50000;
		File nodeFile = ConfigGenerate.createNodeFile(n);
		File edgeFile = ConfigGenerate.createEdgeFile(n);
		File netFile = ConfigGenerate.createNetFile(nodeFile, edgeFile);
	//	File tripFile = ConfigGenerate.createTripFile(c);
	//	File rouFile = ConfigGenerate.createRoutFile(netFile, tripFile);
		File additionFile = ConfigGenerate.createAdditionFile();
		File cfg = ConfigGenerate.createSumoCfg();
		
	    try{
	    	sserver = new ServerSocket(1234);
	    }catch(IOException e){
		     e.printStackTrace();
		     System.exit(0);
	    }	
	    
	    while(true){
	    	try {
				Socket s = sserver.accept();
				inserver = s.getInputStream();
				DataInputStream dinserver = new DataInputStream(inserver);
				System.out.println(dinserver.readUTF());
				
				outserver = s.getOutputStream();
				DataOutputStream doutserver = new DataOutputStream(outserver);
				doutserver.writeUTF("ok!");
				
				SumoTraciConnection conn = new SumoTraciConnection(cfg
						.getAbsolutePath(), // config
						12345 // random seed
				);
				
				conn.runServer();
				while (time-- > 0) {
					conn.nextSimStep();
					Collection<InductionLoop> loops = conn.getInductionLoopRepository().getAll().values();
					Iterator<InductionLoop> loopIt = loops.iterator();
					while (loopIt.hasNext()) {
						InductionLoop loop = loopIt.next();
						Collection<Vehicle> vehicles = loop.getLastStepVehicles();
						if (vehicles.size() == 0) {
							continue;
						}
						Iterator<Vehicle> veIt = vehicles.iterator();
						while (veIt.hasNext()) {
							Vehicle aVehicle = veIt.next();
							if (aVehicle.getID().equals("0")) {
								System.out.println(conn.getCurrentSimTime()
										+ ","
										+ loop.getLane().getID()
										+ ","
										+ loop.getID()
										+ ","
										+ aVehicle.getID()
										+ ",0");
							}
						}
					}
				}
				conn.close();
				inserver.close();
				outserver.close();
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    }
	}	
}