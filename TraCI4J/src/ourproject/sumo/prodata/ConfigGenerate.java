package sumo.prodata;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

public class ConfigGenerate {
	public static int EDGE_NUMBER = 0;

	public static File createNodeFile(int n) {
		File file = new File("d://SumoText.nod.xml");
		if (file.exists()) {
			file.delete();
		}
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("nodes");
		rootElement.addAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
		rootElement.addAttribute("xsi:noNamespaceSchemaLocation","http://sumo-sim.org/xsd/nodes_file.xsd");

		for (int j = 0; j <= n; j++) {
			for (int i = 0; i <= n; i++) {
				// System.out.println(i * 100 + " " + j * 100);
				Element nodeElement = rootElement.addElement("node");
				nodeElement.addAttribute("id", "n_" + i + "_" + j);
				nodeElement.addAttribute("x", i * 50 + "");
				nodeElement.addAttribute("y", j * 50 + "");
				nodeElement.addAttribute("type", "traffic_light");
			}
		}
		try {
			XMLWriter writer = new XMLWriter(new FileWriter(file));
			writer.write(document);
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return file;
	}

	public static File createEdgeFile(int n) {
		File file = new File("d://SumoText.edg.xml");
		if (file.exists()) {
			file.delete();
		}

		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("edges");
		rootElement.addAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
		rootElement.addAttribute("xsi:noNamespaceSchemaLocation","http://sumo-sim.org/xsd/edges_file.xsd");

		int edgeNumber = 0;
		for (int j = 0; j <= n; j++) {
			for (int i = 0; i < n; i++) {
				// System.out.println(i + "," + j + "-" + (i + 1) + "," + j);
				Element nodeElement = rootElement.addElement("edge");
				nodeElement.addAttribute("id", "e_" + edgeNumber++);
				nodeElement.addAttribute("from", "n_" + i + "_" + j);
				nodeElement.addAttribute("to", "n_" + (i + 1) + "_" + j);

				Element reverseNodeElement = rootElement.addElement("edge");
				reverseNodeElement.addAttribute("id", "e_" + edgeNumber++);
				reverseNodeElement.addAttribute("to", "n_" + i + "_" + j);
				reverseNodeElement.addAttribute("from", "n_" + (i + 1) + "_" + j);
			}
		}

		for (int i = 0; i <= n; i++) {
			for (int j = 0; j < n; j++) {
				// System.out.println(i + "," + j + "-" + i + "," + (j + 1));
				Element nodeElement = rootElement.addElement("edge");
				nodeElement.addAttribute("id", "e_" + edgeNumber++);
				nodeElement.addAttribute("from", "n_" + i + "_" + j);
				nodeElement.addAttribute("to", "n_" + i + "_" + (j + 1));

				Element reverseNodeElement = rootElement.addElement("edge");
				reverseNodeElement.addAttribute("id", "e_" + edgeNumber++);
				reverseNodeElement.addAttribute("to", "n_" + i + "_" + j);
				reverseNodeElement.addAttribute("from", "n_" + i + "_" + (j + 1));
			}
		}
		
		ConfigGenerate.EDGE_NUMBER = edgeNumber;
		try {
			XMLWriter writer = new XMLWriter(new FileWriter(file));
			writer.write(document);
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return file;
	}

	public static File createNetFile(File nodeFile, File edgeFile) {
		File netFile = new File("d://SumoText.net.xml");
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
			ProcessHelper ph = new ProcessHelper(p.getInputStream(),"inputStream");
			ph.start();
			p.waitFor();
			// ph.destroy();

		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}

		return netFile;
	}

	public static File createRoutFile(File netFile, File tripFile) {
		File rouFile = new File("d://SumoText.rou.xml");
		if (rouFile.exists()) {
			rouFile.delete();
		}
		try {
			// duarouter --trip-defs trips.xml --net-file road.net.xml
			// --output-file result.rou.xml
			String cmd = "duarouter -n " + netFile.getAbsolutePath() + " -t "
					+ tripFile.getAbsolutePath() + " -o "
					+ rouFile.getAbsolutePath();

			System.out.println(cmd);
			Process p = Runtime.getRuntime().exec(cmd);

			// p.destroy();
			ProcessHelper ph = new ProcessHelper(p.getInputStream(),
					"inputStream");
			ph.start();
			p.waitFor();
			// zql,2014-04-10
			// ph.destroy();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return rouFile;
	}

	public static File createAdditionFile() {
		File add = new File("d://SumoText.add.xml");
		if (add.exists()) {
			add.delete();
		}
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("sumo-detectors");
		// rootElement.addAttribute("xmlns:xsi",
		// "http://www.w3.org/2001/XMLSchema-instance");
		// rootElement.addAttribute("xsi:noNamespaceSchemaLocation",
		// "http://sumo-sim.org/xsd/additional_file.xsd");

		for (int i = 0; i < EDGE_NUMBER; i++) {
			Element loopElement = rootElement.addElement("e1Detector");
			loopElement.addAttribute("id", "obs" + i);
			loopElement.addAttribute("lane", "e_" + i + "_0");
			loopElement.addAttribute("pos", "20");
			loopElement.addAttribute("freq", "1");
			// loopElement.addAttribute("file", "d://SumoText.detector.xml" );
			loopElement.addAttribute("file", "detector.xml");
		}
		try {
			XMLWriter writer = new XMLWriter(new FileWriter(add));
			writer.write(document);
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return add;

	}

	public static File createSumoCfg() {
		File cfg = new File("d://SumoText.sumocfg");
		if (cfg.exists()) {
			cfg.delete();
		}
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("configuration");
		rootElement.addAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		rootElement.addAttribute("xsi:noNamespaceSchemaLocation",
				"http://sumo-sim.org/xsd/sumoConfiguration.xsd");

		Element inputElement = rootElement.addElement("input");
		Element netElement = inputElement.addElement("net-file");
		netElement.addAttribute("value", "SumoText.net.xml");
		Element routeElement = inputElement.addElement("route-files");
		routeElement.addAttribute("value", "SumoText.rou.xml");
		Element additionalEtlement = inputElement.addElement("additional-files");
		additionalEtlement.addAttribute("value", "SumoText.add.xml");

		try {
			XMLWriter writer = new XMLWriter(new FileWriter(cfg));
			writer.write(document);
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cfg;
	}
}