package sumo.prodata;

import it.polito.appeal.traci.SumoTraciConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import sumo.stodata.JDBCConnection;
import sumo.stodata.MessagesStorage;

public class MyMainFrame {
	
	private static class Holder {
		static MyMainFrame instance = new MyMainFrame();
	}
	
	public static MyMainFrame getInstance() {
		return MyMainFrame.Holder.instance;
	}
	
	public static MessagesStorage messagesStorage;
	
	public MyMainFrame(){
		try {
			messagesStorage = new MessagesStorage();
			init();
			
			//关闭数据库的连接
			JDBCConnection.closeConnectionToMysql();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void init() throws IOException{
		//利用sumo工具 产生相应的配置文件
		File nodeFile = Config.createNodeFile();
		File edgeFile = Config.createEdgeFile();
		File netFile = Config.createNetFile(nodeFile, edgeFile);
		
		File tripFile = Config.createTripFile();	
		File rouFile = Config.createRoutFile(netFile, tripFile);
		
		File addFile = Config.createAdditionFile();
		File cfg = Config.createSumoCfg();
		
		//疑问：这些gps车辆的作用在哪儿呢？sumo中也不支持直接定位的吧。
		//File  gpsvehilefile = GpsVehicleConfig.writeGpsVehilceConfig();
		/*File gpsvehilefile = new File("src/ourProject/iniConfig/SumoText.gpsv.ini");
		InputStreamReader inputstreamReader = new InputStreamReader(new FileInputStream(gpsvehilefile));
		Properties gvepp = new Properties();
		gvepp.load(inputstreamReader);
		int gbsvehid[] = new int[Config.getGpsVehicleNumber()];
		for(int i = 0;i < gbsvehid.length;i++) {
		    gbsvehid[i] = Integer.parseInt(gvepp.getProperty("gpsvehicleid"+i).trim());
		}*/
		
		//**运行仿真系统**//
		SumoTraciConnection conn = new SumoTraciConnection("src/ourProject/fileGenerator/SumoText.sumocfg", 12345);
		int time = 100;
		boolean flag = true;
		try{
			conn.runServer();
			while((time--) > 0){
				conn.nextSimStep();
				//System.out.println("the step time from SumoTraciConnection: "+conn.getCurrentSimStep());
				if(flag){
					//获取 线圈的基本信息，只执行一次即可。
					messagesStorage.saveInductionLoop(conn.getInductionLoopRepository().getAll().values());
					flag = false;
				}
				messagesStorage.saveMessagesOnLoop(conn.getInductionLoopRepository().getAll().values(),conn.getCurrentSimTime());
				System.out.println("that 1 step time messages have been storaged--------------------");
				System.out.println();
			}
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MyMainFrame.getInstance();
	}
	
}
