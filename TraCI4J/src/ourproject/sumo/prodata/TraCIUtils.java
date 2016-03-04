/*   
    Copyright (C) 2016 ApPeAL Group, Politecnico di Torino

    This file is part of TraCI4J.

    TraCI4J is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TraCI4J is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TraCI4J.  If not, see <http://www.gnu.org/licenses/>.
*/

package sumo.prodata;

import java.util.Iterator;

import it.polito.appeal.traci.Edge;
import it.polito.appeal.traci.InductionLoop;
import it.polito.appeal.traci.Lane;
import it.polito.appeal.traci.Route;
import it.polito.appeal.traci.SumoTraciConnection;
import it.polito.appeal.traci.TrafficLight;
import it.polito.appeal.traci.Vehicle;
import it.polito.appeal.traci.VehicleType;

//常用工具类，所有的方法都是静态的，可直接调用。
public class TraCIUtils {
	
	//仿真前进5秒
	public static void advance5Sec(SumoTraciConnection conn) {
		try {
			conn.nextSimStep();
			conn.nextSimStep();
			conn.nextSimStep();
			conn.nextSimStep();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//仿真前进100秒
	public static void advance100Sec(SumoTraciConnection conn) {
		try {
			for(int index = 0;index < 100;index++) {
				conn.nextSimStep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**获取当前时刻仿真系统中的所有车辆的 迭代器
	 * @param SumoTraciConnection conn => the object which connect the sumo
	 * @author hduser
	 * **/
	public static Iterator<Vehicle> getVehicleIterator(SumoTraciConnection conn) {
		if(!conn.isClosed()) {
			try {
				return conn.getVehicleRepository().getAll().values().iterator();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**获取当前时刻 仿真系统中所有车辆类型的 迭代器
	 * @param SumoTraciConnection conn => the object which connect the sumo
	 * @author hduser
	 * */
	public static Iterator<VehicleType> getVehicleTypeIterator(SumoTraciConnection conn) {
		if(!conn.isClosed()) {
			try {
				return conn.getVehicleTypeRepository().getAll().values().iterator();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**获取当前时刻 仿真系统中所有路由的 迭代器
	 * @param SumoTraciConnection conn => the object which connect the sumo
	 * @author hduser
	 * */
	public static Iterator<Route> getRouteIterator(SumoTraciConnection conn) {
		if(!conn.isClosed()) {
			try {
				return conn.getRouteRepository().getAll().values().iterator();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**获取当前时刻 仿真系统中所有车道的 迭代器
	 * @param SumoTraciConnection conn => the object which connect the sumo
	 * @author hduser
	 * */
	public static Iterator<Lane> getLaneIterator(SumoTraciConnection conn) {
		if(!conn.isClosed()) {
			try {
				return conn.getLaneRepository().getAll().values().iterator();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**获取当前时刻 仿真系统中所有交通灯的 迭代器
	 * @param SumoTraciConnection conn => the object which connect the sumo
	 * @author hduser
	 * */
	public static Iterator<TrafficLight> getTrafficLightIterator(SumoTraciConnection conn) {
		if(!conn.isClosed()) {
			try {
				return conn.getTrafficLightRepository().getAll().values().iterator();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**获取当前时刻 仿真系统中所有edge的 迭代器
	 * @param SumoTraciConnection conn => the object which connect the sumo
	 * @author hduser
	 * */
	public static Iterator<Edge> getEdgeIterator(SumoTraciConnection conn) {
		if(!conn.isClosed()) {
			try {
				return conn.getEdgeRepository().getAll().values().iterator();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**获取当前时刻 仿真系统中所有Induction Loop的 迭代器
	 * @param SumoTraciConnection conn => the object which connect the sumo
	 * @author hduser
	 * */
	public static Iterator<InductionLoop> getInductionLoopIterator(SumoTraciConnection conn) {
		if(!conn.isClosed()) {
			try {
				return conn.getInductionLoopRepository().getAll().values().iterator();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
