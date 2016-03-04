package cn.edu.hnu.storm.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class CountWordsOutputBolt extends BaseRichBolt {
	
	public OutputCollector _collector;
	public static Map<String,Integer> wordsCount = new HashMap<String,Integer>();

	@Override
	public void prepare(Map map, TopologyContext topologycontext,
			OutputCollector outputcollector) {
		// TODO Auto-generated method stub
		this._collector = outputcollector;
	}
	
	@Override
	public void execute(Tuple tuple) {
		// TODO Auto-generated method stub
		String word = tuple.getStringByField("word");
		Integer count = wordsCount.get(word);
		if(count == null) {
			count = 0;
		}
		wordsCount.put(word, count + tuple.getIntegerByField("count"));
		//输出当前计数情况
		outputWordsCount();

		//返回ack
		_collector.ack(tuple);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer outputfieldsdeclarer) {
		// TODO Auto-generated method stub
		//作为终结bolt，不向外发送数据。
	}

	//输出当前单词计数情况
	public void outputWordsCount() {
		Set<String> wordSet = wordsCount.keySet();
		System.out.println();
		for(String word : wordSet) {
			System.out.println("****单词:　"+word+" ,计数为: "+wordsCount.get(word)+" ****");
		}
		System.out.println();
	}
}
