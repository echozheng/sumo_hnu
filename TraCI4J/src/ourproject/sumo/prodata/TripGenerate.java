package sumo.prodata;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dtools.ini.BasicIniFile;
import org.dtools.ini.BasicIniSection;
import org.dtools.ini.IniFile;
import org.dtools.ini.IniFileReader;
import org.dtools.ini.IniItem;
import org.dtools.ini.IniSection;

public class TripGenerate {
	public static File createTripFile() throws IOException {
		//trips-file
		File tripFile = new File("src/ourProject/fileGenerator/SumoText.trips.xml");
		if (tripFile.exists()) {
			tripFile.delete();
		}
		IniFile inifile = new BasicIniFile();
		File file = new File("src/ourProject/iniConfig/vehicletrip.ini");
		IniFileReader read = new IniFileReader(inifile, file);
		read.read();
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("trips");
		
		//设定车辆的出发时间
		int departTime = 0;

		//对车的三个目标，按照20:30:50的概率随机抽取。
		for(int i = 0; i < Config.getVehicleNumber(); i++) {
			 int r = (int)(Math.random() * 100);
			 int weight;
			 if(r < 20) {
				 weight = 0;
			 } else if(r < 50) {
				 weight = 1;
			 } else {
				 weight = 2;
			 }
			 Element tripElement = rootElement.addElement("trip");
			 //getSection()这个方法厉害啊。
			 IniSection iniSection= inifile.getSection(i);  
			 
			 //得到from的内容
			 IniItem fromItem = iniSection.getItem("from"); 
			 String from = fromItem.getValue();
			 
			 //得到to的内容
			 IniItem toItem = iniSection.getItem("to"+ weight); 
			 String to = toItem.getValue(); 
			 
			 //组成一条trip
			 tripElement.addAttribute("depart", "" + departTime++);
			 tripElement.addAttribute("from", "e_" + from);
			 tripElement.addAttribute("to", "e_" + to);
		}
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();//缩减型格式
			format.setEncoding("UTF-8");//设置编码
			XMLWriter writer = new XMLWriter(new FileWriter(tripFile),format);
			writer.write(document);
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return tripFile;
	 }
	
	/**根据edge的数目(根据node计算,如果node的个数为n，则产生的edge数目为：[n*(2*n -2)] * 2 )
	来产生某个具体车辆的 出发地 与 目的地。格式如下：
	*[VehicleId0]
	*from=12
	*to0=37
	*weight0=20
	*to1=86
	*weight1=30
	*to2=37
	*weight2=50
	***/
	public static void  createTripFormTo(int nodeNumber,int vehicleNumber) {
		File tripIni = new File("src/ourProject/iniConfig/vehicletrip.ini");
		if (tripIni.exists()) {
			tripIni.delete();
		}
		int edgeNumber = 4 *(nodeNumber * nodeNumber - nodeNumber);
		BasicIniFile basicIniFile = new BasicIniFile();
		IniFileWriter fileWriter = new IniFileWriter(basicIniFile, tripIni);
		try {
			for(int index = 0;index < vehicleNumber;index++) {
				//编号
				IniSection section = new BasicIniSection("VehicleId"+index);

				//from
				int form = (int) (Math.random() * edgeNumber);
				IniItem fromItem = new IniItem("from");
				fromItem.setValue(form);
				section.addItem(fromItem);
				
				//to1
				int to0 = (int) (Math.random() * edgeNumber);
				IniItem to0Item = new IniItem("to0");
				to0Item.setValue(to0+"\n"+"weight=20");
				section.addItem(to0Item);
				
				//to2
				int to1 = (int) (Math.random() * edgeNumber);
				IniItem to1Item = new IniItem("to1");
				to1Item.setValue(to1+"\n"+"weight=30");
				section.addItem(to1Item);
				
				//to3
				int to2 = (int) (Math.random() * edgeNumber);
				IniItem to2Item = new IniItem("to2");
				to2Item.setValue(to2+"\n"+"weight=30"+"\n");
				section.addItem(to2Item);
				basicIniFile.addSection(section);
			}
			fileWriter.write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
