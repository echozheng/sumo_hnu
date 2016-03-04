package storm.topology;

import storm.spout.TestWordSpout;
import storm.bolt.ExclamationBolt;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;


public class ExclamationTopology {
	public static void main(String[] args) throws Exception{
		TopologyBuilder builder = new TopologyBuilder();
		
		builder.setSpout("word", new TestWordSpout(),10);
		builder.setBolt("exclaim1", new ExclamationBolt(),3).shuffleGrouping("word");
		builder.setBolt("exclaim2", new ExclamationBolt(),2).shuffleGrouping("exclaim1");
		
		Config conf = new Config();
		conf.setDebug(true);
		
		if(args != null && args.length > 0) {
			conf.setNumWorkers(3);
			StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
		} else {
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("test", conf, builder.createTopology());
			Utils.sleep(10000);
			cluster.killTopology("test");
			cluster.shutdown();
		}
	}
}
