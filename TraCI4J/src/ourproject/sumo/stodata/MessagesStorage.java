package sumo.stodata;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import sumo.outputmodel.MessageOnLoop;
import it.polito.appeal.traci.InductionLoop;

public class MessagesStorage {
	
	final String INSERT_SQL1 = "insert into c_induction_loop(c_id,c_lane,c_position) values(?,?,?)";
	//final String INSERT_SQL2 = "insert into c_message_onloop(c_idstr,c_vehicle_number,c_mean_speed) values(?,?,?)";
	final String INSERT_SQL2 = "insert into c_message_onloop(c_idstr,c_vehicle_number,c_mean_speed,c_step_time) values(?,?,?,?)";
	public final Connection jdbcConn = JDBCConnection.getConnection();
	public PreparedStatement ptsm = null;
	
	//存储inductionLoop的基本信息
	public void saveInductionLoop(Collection<InductionLoop> loopCollection){
		try{
			jdbcConn.setAutoCommit(false);	//取消掉自动提交
			ptsm = jdbcConn.prepareStatement(INSERT_SQL1);
			Iterator<InductionLoop> loops = loopCollection.iterator();
			InductionLoop loop = null;
			while(loops.hasNext()) {
				loop = loops.next();
				String id = loop.getID();
				String lane = loop.getLane().getID();
				//System.out.println("the loop's position: "+loop.getReadQuery(InductionLoop.Variable.POSITION));
				//System.out.println("the loop's position: "+loop.getPosition().toString());
				//ReadObjectVarQuery<?> position = loop.getReadQuery(InductionLoop.Variable.POSITION)
				ptsm.setString(1, id);
				ptsm.setString(2, lane);
				//测试运行
				ptsm.setString(3, "20;Test");
				//批量操作
				ptsm.addBatch();
			}
			//一次性向mysql服务器提交 一个step中产生的所有信息。
			ptsm.executeBatch();
			jdbcConn.commit();
		}catch(Exception ex){
			ex.printStackTrace();
			try {
				ptsm.clearBatch();
				jdbcConn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}finally{
			if(ptsm != null){
				try {
					ptsm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}//finally
	}
	
	//存储inductionLoop所获得的信息
	public void saveMessagesOnLoop(Collection<InductionLoop> loopCollection, int step_time){
		try{
			jdbcConn.setAutoCommit(false);	//取消掉自动提交
			ptsm = jdbcConn.prepareStatement(INSERT_SQL2);
			Iterator<InductionLoop> loops = loopCollection.iterator();
			InductionLoop loop = null;
			//创建一个对象 来 存储。 --- 没多大用处
			MessageOnLoop messageOnLoop = new MessageOnLoop();
			while(loops.hasNext()){
				loop = loops.next();
				if(loop.getVehicleNumber() < 1){
					//只存储 有车辆记录的信息
					continue;
				}
				messageOnLoop.setIdstr(loop.getID());
				messageOnLoop.setVehicleNumber(loop.getVehicleNumber());
				messageOnLoop.setMeanSpeed(loop.getMeanSpeed());
				ptsm.setString(1, messageOnLoop.getIdstr());
				ptsm.setInt(2, messageOnLoop.getVehicleNumber());
				ptsm.setDouble(3, messageOnLoop.getMeanSpeed());
				ptsm.setInt(4, step_time);
				//批量操作
				ptsm.addBatch();
			}
			//一次性向mysql服务器提交 一个step中产生的所有信息。
			ptsm.executeBatch();
			jdbcConn.commit();
		}catch(Exception ex){
			ex.printStackTrace();
			try {
				ptsm.clearBatch();
				jdbcConn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}finally{
			if(ptsm != null){
				try {
					ptsm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}//finally
	}
}