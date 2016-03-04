/**
 * 实现对交通灯的控制
 * **/
package it.polito.appeal.traci.examples;

import java.io.IOException;
import java.util.Iterator;

import sumo.prodata.TraCIUtils;
import sumo.prodata.TraCIUtilsDetails;
import it.polito.appeal.traci.InductionLoop;
import it.polito.appeal.traci.Logic;
import it.polito.appeal.traci.Phase;
import it.polito.appeal.traci.Program;
import it.polito.appeal.traci.SumoTraciConnection;
import it.polito.appeal.traci.TLState;
import it.polito.appeal.traci.TrafficLight;

public class TrafficProgram {
	//保存的交通灯program
	protected static Program preserveProgram;
	//保存的交通灯 phase index
	protected static int preserveIndex;
	//保存交通灯断掉的时间
	protected static int preserveTime = 0;
	//program id => 交通灯每次调用 changeCompleteProgramDefinition时候，所搭配的programId要唯一！！！
	protected static int programId = 12;
	
	public static void main(String args[]) {
		SumoTraciConnection conn = new SumoTraciConnection(
				"test/resources/sumo_maps/traffic_control/traffic_control.sumocfg",12345);
		try {
			//运行sumo
			conn.runServer();
			
			//如果上行或者下行的4个感应线圈，感应到有车辆通过，就把上下行的交通灯设置成绿色，
			//绿灯时间为5秒。
			int totalTime = 120; //通过直接跑sumo来获得。
			while((totalTime--) >= 0) {
				conn.nextSimStep();
				Iterator<InductionLoop> loops = TraCIUtils.getInductionLoopIterator(conn);
				boolean carPassedOrNot = haveVehiclePassed(loops,120-totalTime);
				if(carPassedOrNot) {
					//有车通过，则改变当前方向的交通灯状态。
					TrafficLight light  = TraCIUtilsDetails.getTrafficLightByIds(new String []{"nodeA"}, conn)[0];
					System.out.println(light.toString());
					preserveTime = conn.getCurrentSimTime();
					System.out.println("now the preserve time is : " + preserveTime);
					changeTrafficLightState(light);
				}
				if(((conn.getCurrentSimTime() - preserveTime - 6000) == 0) && (preserveTime != 0)) {
					//过了5秒之后，就把交通灯的状态 恢复
					System.out.println("recovering the traffic light");
					TrafficLight light2  = TraCIUtilsDetails.getTrafficLightByIds(new String []{"nodeA"}, conn)[0];
					recoverTrafficLightState(light2);
				}
				System.out.println("--------------------------------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				//把conn.close()写在finally中
				conn.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	//判断目前是否有车通过
	public static boolean haveVehiclePassed(Iterator<InductionLoop> loops,int time) {
		System.out.println("当前时间为 : "+time);
		while(loops.hasNext()) {
			try {
				InductionLoop loop = loops.next();
				//!!!!!getVehicleNumber()这个方法的缺点是，下一个时刻的车与上一个时刻的车 可能相同。
				int passedVehicles = loop.getVehicleNumber();
				//System.out.println("当前loop "+loop.getID()+" 通过的车数量为 => " + passedVehicles);
				if(passedVehicles > 0) {
					return true;
				}
				/*for(Vehicle vehicle : loop.getLastStepVehicles()) {
					System.out.println("目前通过的车辆id为： "+vehicle.getID());
				}*/
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//System.out.println("--------------------------------------");
		return false;
	}
	
	/**改变交通灯状态**/
	public static void changeTrafficLightState(TrafficLight light) {
		//预先定义好交通灯状态
		//TLState tlState = new TLState("rGGrrrrrrrGGrrrrrr");
		//Phase tlPhase = new Phase(6,tlState);
		
		//light.queryReadCurrentProgram().get() 这个方法得到的是 交通灯所对应的program id。
		//System.out.println(light.queryReadCurrentProgram().get().toString());
		
		//int duration = light.getDefaultCurrentPhaseDuration();
		//System.out.println("duration =: "+duration);
		
		try {
			//先保存好原来的program
			preserveProgram = light.queryReadCompleteDefinition().get();
			preserveIndex = preserveProgram.getLogics()[0].getCurrentPhaseIndex();
			
			TLState tlState = new TLState("rGGrrrrrrrGGrrrrrr");
			Phase tlPhase[] = {new Phase(5,tlState)};
			Logic tlLogic = new Logic(String.valueOf(programId++), 0, tlPhase);
			light.changeCompleteProgramDefinition(tlLogic);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("改变了 交通灯了。。。。。");
	}
	
	/**改变交通灯状态,方法重载**/
	public static void recoverTrafficLightState(TrafficLight light) {
		try {
			//System.out.println("preserveIndex = " + preserveIndex);
			//System.out.println("logic.length : " + preserveProgram.getLogics().length);
			//System.out.println("logic.content : " + preserveProgram.getLogics()[0].getPhases()[0].getState().toString());
			Logic tlLogic = new Logic(String.valueOf(programId++),0,preserveProgram.getLogics()[0].getPhases());
			light.changeCompleteProgramDefinition(tlLogic);
			light.changePhaseDuration(preserveIndex);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("恢复了 交通灯了。。。。。");
	}
}
