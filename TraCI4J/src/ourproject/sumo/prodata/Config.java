package sumo.prodata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class Config {
	private static int edgenumber;
	
	public static int getEdgeNumber() {
		return edgenumber;
	}

	private static int nodenumber;

	public static int getNodeNumber() {
		return nodenumber;
	}

	private static int vehiclenumber;

	public static int getVehicleNumber() {
		return vehiclenumber;
	}

	private static int gpsvehicle;

	public static int getGpsVehicle() {
		return gpsvehicle;
	}
	
	private static int gpsvehiclenumber;
	
	public static int getGpsVehicleNumber() {
		return gpsvehiclenumber;
	}
	
	private static int taznumber;
	
	public static int getTazNumber() {
		return taznumber;
	}
	
	//这下面两个文件的作用？？？
	//public  static String FILE_NAME1 = "src/ourProject/iniConfig/SumoText.taz.xml";
	//public static String FILE_NAME2 = "src/ourProject/iniConfig/SumoText.od.xml";

	static {
		try {
			File configFile = new File("src/ourProject/iniConfig/sumoConfig.ini");
			InputStreamReader inputStreamReader = 
						new InputStreamReader(new FileInputStream(configFile), "UTF-8");
			Properties config = new Properties();
			config.load(inputStreamReader);

			nodenumber = Integer.parseInt(config.getProperty("NODENUMBER").trim());
			vehiclenumber = Integer.parseInt(config.getProperty("VEHICLENUMBER").trim());
			gpsvehicle = Integer.parseInt(config.getProperty("GPSVEHICLE").trim());
			gpsvehiclenumber = (vehiclenumber * gpsvehicle) / 100;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static File createNodeFile() {
		File file = new File("src/ourProject/fileGenerator/SumoText.nod.xml");
		if (file.exists()) {
			file.delete();
		}

		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("nodes");
		rootElement.addAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
		rootElement.addAttribute("xsi:noNamespaceSchemaLocation","http://sumo-sim.org/xsd/nodes_file.xsd");
		
		for (int j = 0; j <= nodenumber; j++) {
			for (int i = 0; i <= nodenumber; i++) {
				// System.out.println(i * 100 + " " + j * 100);
				Element nodeElement = rootElement.addElement("node");
				nodeElement.addAttribute("id", "n_" + i + "_" + j);
				nodeElement.addAttribute("x", i * 50 + "");
				nodeElement.addAttribute("y", j * 50 + "");
				nodeElement.addAttribute("type", "traffic_light");
			}
		}
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();//缩减型格式
	        format.setEncoding("UTF-8");//设置编码
			XMLWriter writer = new XMLWriter(new FileWriter(file),format);
			writer.write(document);
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return file;
	}
	
	public static File createEdgeFile() {
		File file = new File("src/ourProject/fileGenerator/SumoText.edg.xml");
		if (file.exists()) {
			file.delete();
		}

		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("edges");
		rootElement.addAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		rootElement.addAttribute("xsi:noNamespaceSchemaLocation",
				"http://sumo-sim.org/xsd/edges_file.xsd");
		int edgeNumber = 0;

		//构建横线的双向edge
		for (int j = 0; j <= nodenumber; j++) {
			for (int i = 0; i < nodenumber; i++) {
				// System.out.println(i + "," + j + "-" + (i + 1) + "," + j);
				Element nodeElement = rootElement.addElement("edge");
				nodeElement.addAttribute("id", "e_" + edgeNumber++);
				nodeElement.addAttribute("from", "n_" + i + "_" + j);
				nodeElement.addAttribute("to", "n_" + (i + 1) + "_" + j);
				
				//与上面方向相反的edge
				Element reverseNodeElement = rootElement.addElement("edge");
				reverseNodeElement.addAttribute("id", "e_" + edgeNumber++);
				reverseNodeElement.addAttribute("from", "n_" + (i + 1) + "_"+ j);
				reverseNodeElement.addAttribute("to", "n_" + i + "_" + j);
			}
		}
		
		//构建纵向的edge
		for (int i = 0; i <= nodenumber; i++) {
			for (int j = 0; j < nodenumber; j++) {
				// System.out.println(i + "," + j + "-" + i + "," + (j + 1));
				Element nodeElement = rootElement.addElement("edge");
				nodeElement.addAttribute("id", "e_" + edgeNumber++);
				nodeElement.addAttribute("from", "n_" + i + "_" + j);
				nodeElement.addAttribute("to", "n_" + i + "_" + (j + 1));

				Element reverseNodeElement = rootElement.addElement("edge");
				reverseNodeElement.addAttribute("id", "e_" + edgeNumber++);
				reverseNodeElement.addAttribute("from", "n_" + i + "_"+ (j + 1));
				reverseNodeElement.addAttribute("to", "n_" + i + "_" + j);
			}
		}

		Config.edgenumber= edgeNumber;

		try {
			OutputFormat format = OutputFormat.createPrettyPrint();//缩减型格式
	        format.setEncoding("UTF-8");//设置编码
			XMLWriter writer = new XMLWriter(new FileWriter(file),format);
			writer.write(document);
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return file;
	}
	
	/**由node文件 与 edge文件，利用sumo的工具 netconvert产生net文件。**/
	public static File createNetFile(File nodeFile, File edgeFile) {
		File netFile = new File("src/ourProject/fileGenerator/SumoText.net.xml");
		if (netFile.exists()) {
			netFile.delete();
		}
		
		try {
			String cmd = "netconvert --node-files "
					+ nodeFile.getAbsolutePath() + " --edge-files "
					+ edgeFile.getAbsolutePath() + " -o "
					+ netFile.getAbsolutePath();

			System.out.println(cmd);

			Process p = Runtime.getRuntime().exec(cmd);
			ProcessHelper processHelper = new ProcessHelper(p.getInputStream(),"inputStream");
			processHelper.start();
			p.waitFor();

		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return netFile;
	}
	
	/**产生车辆的trip 文件**/
	public static File createTripFile() throws IOException{
		File tripFile =   TripGenerate.createTripFile();
		return tripFile;
	}
	
	/**使用sumo自带的工具 duarouter 以trip(定义了车辆的from与to)和net为输入，生成最短路径**/
	public static File createRoutFile(File netFile, File tripFile) {
		File rouFile = new File("src/ourProject/fileGenerator/SumoText.rou.xml");
		if (rouFile.exists()) {
			rouFile.delete();
		}
		
		try {
			// duarouter --trip-defs trips.xml --net-file road.net.xml --output-file result.rou.xml
			String cmd = "duarouter" + " --net-file " + netFile.getAbsolutePath() +
						" --trip-files " + tripFile.getAbsolutePath() +
						" --output-file "+ rouFile.getAbsolutePath();
			//System.out.println(cmd);
			Process process = null;
			try{
				process = Runtime.getRuntime().exec(cmd);
				ProcessHelper processHelper = new ProcessHelper(process.getInputStream(),"inputStream");
				processHelper.start();
				process.waitFor();
			}catch(IOException | InterruptedException ex){
				process.getOutputStream().close();
				process.getInputStream().close();
				process.getErrorStream().close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return rouFile;
	}
	
	/**产生 感应线圈的文件，add.xml**/
	public static File createAdditionFile() {
		File addtionFile = new File("src/ourProject/fileGenerator/SumoText.add.xml");
		if (addtionFile.exists()) {
			addtionFile.delete();
		}
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("additional");
		rootElement.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		rootElement.addAttribute("xsi:noNamespaceSchemaLocation", "http://sumo-sim.org/xsd/additional_file.xsd");
		
		//默认在每条edge的边上 都增加一个InductionLoop
		for (int i = 0; i < edgenumber; i++) {
			Element loopElement = rootElement.addElement("inductionLoop");
			loopElement.addAttribute("id", "obs" + i);
			loopElement.addAttribute("lane", "e_"+i+"_0" );
			loopElement.addAttribute("pos","20" );
			loopElement.addAttribute("freq", "1" );
			loopElement.addAttribute("file", "detector.xml" );
		}
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();	//缩减型格式
	        format.setEncoding("UTF-8");	//设置编码
			XMLWriter writer = new XMLWriter(new FileWriter(addtionFile),format);
			writer.write(document);
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return addtionFile;
	}
		
	/**产生 最后的cfg配置文件**/
	public static File createSumoCfg() {
		File cfg = new File("src/ourProject/fileGenerator/SumoText.sumocfg");
		if (cfg.exists()) {
			cfg.delete();
		}
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("configuration");
		rootElement.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		rootElement.addAttribute("xsi:noNamespaceSchemaLocation", "http://sumo-sim.org/xsd/sumoConfiguration.xsd");

		Element inputElement = rootElement.addElement("input");
		Element netElement = inputElement.addElement("net-file");
		netElement.addAttribute("value", "SumoText.net.xml");
		Element routeElement = inputElement.addElement("route-files");
		routeElement.addAttribute("value", "SumoText.rou.xml");
		Element additionalEtlement = inputElement.addElement("additional-files");
		additionalEtlement.addAttribute("value", "SumoText.add.xml");
		
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();//缩减型格式
	        format.setEncoding("UTF-8");//设置编码
			XMLWriter writer = new XMLWriter(new FileWriter(cfg),format);
			writer.write(document);
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cfg;
	}
}