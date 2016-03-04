/**
 * Class:MessageOnLoop
 * Usage:sumo每隔 1s就会向 detector.xml输出所有 inductive loop线圈 所得到的消息。
 * 在 此程序中，使用SumoTraciConnection这个类，在sumo每进行一个 step，收集此地图中 
 * 所有inductionLoop 所得到的消息。
 * */
package sumo.outputmodel;

public class MessageOnLoop {
	//自增主键
	private long id;
	//线圈名称
	private String idstr;
	//在one step内通过的车数量
	private int vehicleNumber;
	//在one step内通过的车的平均速度
	private double meanSpeed;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getIdstr() {
		return idstr;
	}
	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}
	public int getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(int vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public double getMeanSpeed() {
		return meanSpeed;
	}
	public void setMeanSpeed(double meanSpeed) {
		this.meanSpeed = meanSpeed;
	}
}
