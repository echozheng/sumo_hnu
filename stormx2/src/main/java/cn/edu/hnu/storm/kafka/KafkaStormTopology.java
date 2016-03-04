package cn.edu.hnu.storm.kafka;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

public class KafkaStormTopology {
	
	public static void main(String args[]) {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("wordsIn", createKafkaSpout(),2);
		builder.setBolt("wordsGet", new StormKafkaBolt(),2).shuffleGrouping("wordsIn");
		builder.setBolt("wordsCount1", new CountWordsBolt(),4).fieldsGrouping("wordsGet", new Fields("word"));
		builder.setBolt("wordsCount2", new CountWordsOutputBolt()).allGrouping("wordsCount1");
		
		Config conf = new Config();
		conf.setDebug(true);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("testKafkaSpout", conf, builder.createTopology());
		Utils.sleep(1000000);
		cluster.killTopology("testKafkaSpout");
		cluster.shutdown();
	}
	
	//得到kafkaspout
	public static KafkaSpout createKafkaSpout() {
		BrokerHosts zooHosts = new ZkHosts("localhost:2181");
		SpoutConfig spoutConfig = new SpoutConfig(zooHosts,"website-hits", "", "wordsIn");
		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

		//如果出现问题，则不从最初开始获取topic的message
		spoutConfig.forceFromStart = false;
		
		KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
		return kafkaSpout;
	}
}
