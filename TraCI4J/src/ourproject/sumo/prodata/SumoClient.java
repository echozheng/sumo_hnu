package sumo.prodata;
import java.net.*;
import java.io.*;

public class SumoClient {

	 public static void main(String[] args) { 
		Socket sclient = null;
		InputStream insclient = null;
		OutputStream outsclient = null;
		//int edgenum = new Random();
		
		String st = null;
	    try { 
	           sclient = new Socket(InetAddress.getLocalHost(), 1234);
	           outsclient = sclient.getOutputStream(); 
	           DataOutputStream doutsclient = new DataOutputStream(outsclient); 
	           doutsclient.writeUTF("I want to connect with you!");
	           insclient = sclient.getInputStream();
	           DataInputStream dinsclient = new DataInputStream(insclient);
	           st = dinsclient.readUTF();
	           System.out.println(st);
	           insclient.close(); 
	           outsclient.close(); 	           
	           sclient.close(); 
	    }catch (IOException e) {
	    	e.printStackTrace();
	    } 
	 } 
}
