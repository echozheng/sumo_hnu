package sumo.prodata;
import it.polito.appeal.traci.InductionLoop;
import it.polito.appeal.traci.SumoTraciConnection;
import it.polito.appeal.traci.Vehicle;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;


public class GetSumoMessage implements Runnable {

	public SumoTraciConnection conn = null;

	public boolean start = false;

	DecimalFormat format = new DecimalFormat("#.00");
	Random r = new Random();

	Thread thread;
	private int n;
	private int c;

	File cfg;

	Object lock = new Object();

	public GetSumoMessage() {

	}

	public void config(int n, int c) {

		File nodeFile = ConfigGenerate.createNodeFile(n);
		File edgeFile = ConfigGenerate.createEdgeFile(n);
		File netFile = ConfigGenerate.createNetFile(nodeFile, edgeFile);
	//	File tripFile = ConfigGenerate.createTripFile(c);
	//	File rouFile = ConfigGenerate.createRoutFile(netFile, tripFile);
		File additionFile = ConfigGenerate.createAdditionFile();
		cfg = ConfigGenerate.createSumoCfg();
		this.n = n;
		this.c = c;
	}

	public void start() {

		//@param :1=> config 2=>random seed
		conn = new SumoTraciConnection(cfg.getAbsolutePath(), 12345 );
		try {
			conn.runServer();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		start = true;
	}

	public void stop() {
		// thread.stop();
		start = false;
		try {
			conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int getN() {

		return this.n;
	}

	public int getnum_cars() {
 
		return this.c;
	}

	public Site getsite(String id) {

		synchronized (lock) {
			try {
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
						if (aVehicle.getID().equals(id)) {
							Site site = new Site(loop.getLane().getID(),format.format(0.7 + (r.nextDouble() * 0.3)));
							return site;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String[] args) {
		GetSumoMessage gm = new GetSumoMessage();

		Thread thread = new Thread(gm);
		thread.start();
		gm.config(4, 100);
		gm.start();

		System.out.println(gm.getN());

		while (true) {
			Site info = gm.getsite("0");
			if (info != null) {
				System.out.println(info);
			}
		}
		// gm.stop();
	}

	public void showinfo() {
		synchronized (lock) {
			try {
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
						//System.out.println(conn.getCurrentSimStep() + ","
						System.out.println(conn.getCurrentSimTime() + ","
								+ loop.getLane().getID() + "," + loop.getID()
								+ "," + aVehicle.getID() + ",0"
								+ format.format(0.7 + (r.nextDouble() * 0.3)));
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		while (true) {
			if (start == true) {
				try {
					synchronized (lock) {
						conn.nextSimStep();
					}
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class Site {
	String street;
	String d;

	@Override
	public String toString() {
		return "street:" + street + " distance" + d;
	}
	
	public Site(String str, String d) {
		this.street = str;
		this.d = d;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}
}
