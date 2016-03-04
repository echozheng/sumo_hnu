package sumo.control;

import it.polito.appeal.traci.SumoTraciConnection;

/**控制工具类，改变sumo仿真的状态
 * @author hduser
 * **/
public interface ControlSumo {
	public void execute(SumoTraciConnection conn,String componentId,Object changeValue);
}
