package cn.edu.hnu.storm.kafka;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class StormKafkaBolt extends BaseRichBolt {

	private static final long serialVersionUID = 1L;
	OutputCollector _collector;
	
	@Override
	public void prepare(Map map, TopologyContext topologycontext,
			OutputCollector outputcollector) {
		// TODO Auto-generated method stub
		this._collector = outputcollector;
	}

	@Override
	public void execute(Tuple tuple) {
		// TODO Auto-generated method stub
		//System.out.println("从kafka producer 发送过来的数据是 : "+tuple.getString(0)+" **********$$$$$$$");
		//向bolt发送数据
		_collector.emit(new Values(tuple.getString(0)));
		//调用ack方法，告知前面的 组件 tuple已经收到，防止重复发送。
		this._collector.ack(tuple);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer outputfieldsdeclarer) {
		// TODO Auto-generated method stub
		outputfieldsdeclarer.declare(new Fields("word"));
	}
}
