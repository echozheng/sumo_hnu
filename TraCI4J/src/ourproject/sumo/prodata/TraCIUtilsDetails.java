/**
 * usage: 在TraCIUtils工具类的基础上，细化所取得的对象。
 * 通过用户输入的id数组，返回相应顺序的的组件对象。
 * @author hduser
 * **/

package sumo.prodata;

import java.io.IOException;
import java.util.Iterator;

import it.polito.appeal.traci.SumoTraciConnection;
import it.polito.appeal.traci.TrafficLight;

public class TraCIUtilsDetails {
	
	/**通过id数组，获取相应顺序的TrafficLight组件
	 * @param ids => like {"xx","yy"}
	 * @param conn => SumoTraciConnection object
	 * **/
	public static TrafficLight[] getTrafficLightByIds(String ids[],SumoTraciConnection conn) {
		if(conn.isClosed()) {
			try {
				throw new IOException("the connection to the server is closed already!!!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		TrafficLight lights[] = new TrafficLight[ids.length];
		Iterator<TrafficLight> allLights = TraCIUtils.getTrafficLightIterator(conn);
		while(allLights.hasNext()) {
			TrafficLight trafficLight = allLights.next();
			for(int index = 0;index < ids.length;index++) {
				if(trafficLight.getID().equals(ids[index])) {
					//如果id相匹配
					lights[index] = trafficLight;
					//跳出本层循环
					break;
				}
			}
		}
		return lights;
	}
}
