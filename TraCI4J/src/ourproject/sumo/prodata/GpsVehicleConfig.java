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

public class GpsVehicleConfig {
	
	 static Random rad = new Random();
	 
	private static int gpsvehiclenumber = Config.getGpsVehicleNumber();
	private static String gpsvehicleid = "gpsvehicleid";
	
	public static File writeGpsVehilceConfig() throws IOException {
		
		File gpsvehicleconfig = new File("src/ourProject/iniConfig/SumoText.gpsv.ini");
		if(gpsvehicleconfig.exists()) {
			gpsvehicleconfig.delete();
		}
		
		int id;
		IniFile iniFile = new BasicIniFile();
		IniSection dataSection = new BasicIniSection("gpsvehicleids");
		iniFile.addSection(dataSection);
		
		for(int i = 0; i < gpsvehiclenumber; i ++) {
			id = Math.abs(rad.nextInt()) % Config.getVehicleNumber();
			IniItem gpsInitem = new IniItem(gpsvehicleid + i);
			gpsInitem.setValue(String.valueOf(id));
			dataSection.addItem(gpsInitem);
		}
		
		IniFileWriter niFileWriter = new IniFileWriter(iniFile, gpsvehicleconfig); 
		niFileWriter.write();
		
		return gpsvehicleconfig;
	}
}
