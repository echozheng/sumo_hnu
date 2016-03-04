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

package sumo.control;

import java.util.Iterator;

import it.polito.appeal.traci.ChangeLightsStateQuery;
import it.polito.appeal.traci.SumoTraciConnection;
import it.polito.appeal.traci.TLState;
import it.polito.appeal.traci.TrafficLight;
import sumo.prodata.TraCIUtils;

public class ChangeTrafficLightsStateQuery implements ControlSumo {

	@Override
	public void execute(SumoTraciConnection conn,String componentId, Object changeValue) {
		try {
			TrafficLight light = getTrafficLight(conn,componentId);
			ChangeLightsStateQuery lightChangeQuery = light.queryChangeLightsState();
			lightChangeQuery.setValue((TLState) changeValue);
			lightChangeQuery.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TrafficLight getTrafficLight(SumoTraciConnection conn,String componentId) {
		Iterator<TrafficLight> lights = TraCIUtils.getTrafficLightIterator(conn);
		while(lights.hasNext()) {
			TrafficLight light = lights.next();
			//LaneListQuery lightControlLanes = light.queryReadControlledLanes();
			//light.getReadQuery(Variable.CONTROLLED_LINKS);
			if(light.getID().equals(componentId)) {
				//找到符合的组件，直接返回。
				return light;
			}
		}
		return null;
	}

}
