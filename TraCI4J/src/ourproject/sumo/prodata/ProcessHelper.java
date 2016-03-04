package sumo.prodata;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessHelper extends Thread {
	public InputStream is;
	public String type;

	public ProcessHelper(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;

			while ((line = br.readLine()) != null) {
				System.out.println(type + ">" + line);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}