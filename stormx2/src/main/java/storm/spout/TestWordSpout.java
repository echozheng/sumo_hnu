package storm.spout;

import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class TestWordSpout extends BaseRichSpout {
	
	SpoutOutputCollector _collector;

	public void open(Map conf, TopologyContext context,SpoutOutputCollector collector) {
		// TODO Auto-generated method stub
		_collector = collector;
	}

	/**
	 * @usage
	 * 	nextTuple()方法 发送一个新的Tuple到 Topology.
	 * **/
	public void nextTuple() {
		// TODO Auto-generated method stub
		Utils.sleep(100);
		final String[] words = new String[]{"nathan","mike","jackson","golda","bertels"};
		final Random rand = new Random();
		final String word = words[rand.nextInt(words.length)];
		_collector.emit(new Values(word));
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("word"));
	}
	
}
