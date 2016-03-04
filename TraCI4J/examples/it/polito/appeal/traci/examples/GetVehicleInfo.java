/*   
    Copyright (C) 2013 ApPeAL Group, Politecnico di Torino

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

package it.polito.appeal.traci.examples;

import it.polito.appeal.traci.AddVehicleQuery;
import it.polito.appeal.traci.ChangeRouteQuery;
import it.polito.appeal.traci.Edge;
import it.polito.appeal.traci.Lane;
import it.polito.appeal.traci.Route;
import it.polito.appeal.traci.SumoTraciConnection;
import it.polito.appeal.traci.Vehicle;
import it.polito.appeal.traci.VehicleType;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import sumo.prodata.TraCIUtils;

/**
 * This code runs an instance of SUMO, queries the map bounds and does ten
 * simulation steps. For each step, it prints which and how many vehicles are
 * active.
 * <p>
 * The specified configuration file is relative to the TraCI4J package's base
 * directory.
 * 
 * @author Enrico Gueli &lt;enrico.gueli@polito.it&gt;
 * 
 */
public class GetVehicleInfo {

	/** main method */
	public static void main(String[] args) {
		
		SumoTraciConnection conn = new SumoTraciConnection("test/resources/sumo_maps/box1l/test.sumo.cfg",12345);
		try {
			//运行sumo
			conn.runServer();
			
			//添加车辆
			TraCIUtils.advance5Sec(conn);
			addAVehicle(conn);
			
			//改变车辆路由
			TraCIUtils.advance5Sec(conn);
			changeVehicleRoute(conn);
			
			TraCIUtils.advance5Sec(conn);
			TraCIUtils.advance100Sec(conn);
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**向路网中添加一辆车**/
	public static void addAVehicle(SumoTraciConnection conn) {
		/***在第五步时进行下列操作***/
		Iterator<Vehicle> vehicles = TraCIUtils.getVehicleIterator(conn);
		Iterator<VehicleType> vehicleTypes = TraCIUtils.getVehicleTypeIterator(conn);
		Iterator<Route> routes = TraCIUtils.getRouteIterator(conn);
		Iterator<Lane> lanes = TraCIUtils.getLaneIterator(conn);
		
		//输出当前所有车辆的路由信息
		while(vehicles.hasNext()) {
			Vehicle aVehicle = vehicles.next();
			try {
				System.out.println("Vehicle " + aVehicle+ " will traverse these edges: " + aVehicle.getCurrentRoute());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//往当前路网中 添加两个车辆，分别使用route0
		Route route0 = routes.hasNext()?routes.next():null;
		VehicleType vehicleType0 = vehicleTypes.hasNext()?vehicleTypes.next():null;
		Lane lane0 = null;
		while(lanes.hasNext()) {
			lane0 = lanes.next();
			if(lane0.getID().equals("0_0")){
				break;
			}
		}
		
		//添加一个车辆
		try {
			AddVehicleQuery addVehicleQuery =  conn.queryAddVehicle();
			//setVehicleData方法中 departureTime的时间以ms为单位
			addVehicleQuery.setVehicleData("zql_add1",vehicleType0,route0, lane0, 6000, 10.0, 0.0);
			addVehicleQuery.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//过了5步之后，打印出车辆信息看看
		TraCIUtils.advance5Sec(conn);
		Iterator<Vehicle> vehicles2 = TraCIUtils.getVehicleIterator(conn);
		while(vehicles2.hasNext()) {
			Vehicle aVehicle = vehicles2.next();
			System.out.println("Vehicle " + aVehicle+ " and its ID is: " + aVehicle.getID());
		}
	}
	
	/**改变一个车辆的路由**/
	public static void changeVehicleRoute(SumoTraciConnection conn) {
		Iterator<Vehicle> vehicles = TraCIUtils.getVehicleIterator(conn);
		//车的路由从 [0,1,2,3] => [0,3,2,1]
		Vehicle vehicle = vehicles.hasNext()?vehicles.next():null;
		ChangeRouteQuery changeRouteQuery = vehicle.queryChangeRoute();
		
		//new edge list
		List<Edge> edgeList = new LinkedList<Edge>(); 
		Iterator<Edge> edges = TraCIUtils.getEdgeIterator(conn);
		String edgeID[] = {"0","1","2","3"};
		//得到新route的edge列表
		edgeList = getInterestedEdges(edges,edgeID);
		changeRouteQuery.setValue(edgeList);
		try {
			changeRouteQuery.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**返回感兴趣的edge列表**/
	public static List<Edge> getInterestedEdges(Iterator<Edge> edges,String edgeID[]) {
		List<Edge> allEdges = new LinkedList<Edge>();
		List<Edge> routeEdges = new LinkedList<Edge>();
		Edge edgeArray[] = new Edge[4];
		
		//获取全部的edge
		while(edges.hasNext()) {
			allEdges.add(edges.next());
		}
		
		for(int index = 0;index < 4;index++) {
			for(Edge edge : allEdges) {
				//如果ID号相符,则存入数组中。
				if(edge.getID().equals(edgeID[index])) {
					edgeArray[index] = edge;
					//跳出本轮内循环
					continue;
				}
			}
		}
		
		routeEdges.add(edgeArray[0]);
		routeEdges.add(edgeArray[1]);
		System.out.println("改变之后的车的路由为 : "+routeEdges.toString());
		return routeEdges;
	}
}
