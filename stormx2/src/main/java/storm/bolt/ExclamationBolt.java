package storm.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class ExclamationBolt extends BaseRichBolt{

	private static final long serialVersionUID = 1L;
	OutputCollector _collector;
	public void prepare(Map stormConf, TopologyContext context,OutputCollector collector) {
		// TODO Auto-generated method stub
		this._collector = collector;
	}

	/**
	 * @usage
	 * 	Bolt的execute()方法，在接收信息之后，Bolt会调用这个方法。
	 * 用户可以在其中执行自己希望的操作。
	 * */
	public void execute(Tuple input) {
		// TODO Auto-generated method stub
		this._collector.emit(input,new Values(input.getString(0) + "!!!"));
		System.out.println("send out the word : " + input.getString(0));
		this._collector.ack(input);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("word"));
	}
}
