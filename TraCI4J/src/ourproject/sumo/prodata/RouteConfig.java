package sumo.prodata;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.dtools.ini.BasicIniFile;
import org.dtools.ini.BasicIniSection;
import org.dtools.ini.IniFile;
import org.dtools.ini.IniFileWriter;
import org.dtools.ini.IniItem;
import org.dtools.ini.IniSection;

public class RouteConfig {
	
	File file=new File("src/ourProject/vehicletrip.ini");  
     
	
	private int vehicles = Config.getVehicleNumber();
	private String vehicleid = "VehicleId";
	Random rad = new Random();
	public void writeRouCongig() throws IOException{
		IniFile iniFile = new BasicIniFile();
		int from = 0;
		int to = 0;   
		//int weight =0;
		IniItem fromIniItem;  
		IniItem toIniItem;
		IniItem weightIniItem;
		
		for(int i = 0; i < vehicles; i++){
			int j = 0;
			from = Math.abs(rad.nextInt()%100);
			
			IniSection dataSection = new BasicIniSection(vehicleid+i);
			iniFile.addSection(dataSection);
			
			fromIniItem = new IniItem("from");
			fromIniItem.setValue(String.valueOf(from));
			dataSection.addItem(fromIniItem);  
			
			toIniItem = new IniItem("to"+j);
			to = Math.abs(rad.nextInt()%100);
			toIniItem.setValue(String.valueOf(to));
			dataSection.addItem(toIniItem);		
			weightIniItem = new IniItem("weight"+j);
			weightIniItem.setValue(String.valueOf(20));
			dataSection.addItem(weightIniItem);
			j++;
			
			toIniItem = new IniItem("to"+j);
			to = Math.abs(rad.nextInt()%100);
			toIniItem.setValue(String.valueOf(to));
			dataSection.addItem(toIniItem);		
		    weightIniItem = new IniItem("weight"+j);
			weightIniItem.setValue(String.valueOf(30));
			dataSection.addItem(weightIniItem);
			j++;
			
			toIniItem = new IniItem("to"+j);
			to = Math.abs(rad.nextInt()%100);
			toIniItem.setValue(String.valueOf(to));
			dataSection.addItem(toIniItem);		
			weightIniItem = new IniItem("weight"+j);
			weightIniItem.setValue(String.valueOf(50));
			dataSection.addItem(weightIniItem);
			
			System.out.println(i);
		}
		
		IniFileWriter niFileWriter=new IniFileWriter(iniFile, file); 
		niFileWriter.write();
	}
}

