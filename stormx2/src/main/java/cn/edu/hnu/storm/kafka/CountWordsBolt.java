package cn.edu.hnu.storm.kafka;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class CountWordsBolt extends BaseRichBolt {
	
	public OutputCollector _collector;

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		// TODO Auto-generated method stub
		this._collector = arg2;
	}
	
	@Override
	public void execute(Tuple tuple) {
		// TODO Auto-generated method stub
		String word = tuple.getString(0);
		_collector.emit(new Values(word, 1));
		_collector.ack(tuple);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer outputFiledsDeclarer) {
		// TODO Auto-generated method stub
		outputFiledsDeclarer.declare(new Fields("word","count"));
	}
}
