
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.Thread;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyStore;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.io.*;


public class ExpressObject {

	public SSLSocket socket;
	
	public SSLSession session;
	
	public String url;
	
	public String method;
	
	public ArrayList<String> request;
	
	public byte[] raw;
	
	public BufferedReader bufferedReader;
	
	public PrintWriter printWriter;
	
	public StringBuilder response;


    public void setUrl() throws Exception {

		for (int m = 0; m < this.request.size(); m ++) {
			
			if(m == 0) { 

				String item = this.request.get(m);
				
				String[] list = item.split(" ");
			
				this.url = list[1];
			}
		}
    }
	
	
    public void setMethod() throws Exception {

		for (int m = 0; m < this.request.size(); m ++) {
			
			if(m == 0) { 

				String item = this.request.get(m);
				
				String[] list = item.split(" ");
			
				this.method = list[0];
			}
		}
    }
	
	
	public void initRequest() throws Exception {
	
		this.socket.setEnabledCipherSuites(this.socket.getSupportedCipherSuites());

		try {
			
			this.request = new ArrayList<String>();
			this.response = new StringBuilder();
			
			// start handshake
			this.socket.startHandshake();

            // get session
			this.session = this.socket.getSession();

			// handling application content
			InputStream inputStream = this.socket.getInputStream();
			OutputStream outputStream = this.socket.getOutputStream();

			// get reader & writer
			this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			this.printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
			
			// read request
			String line = null;
			while((line = this.bufferedReader.readLine()) != null) {
  
				if(line.trim().isEmpty()) {
					break;
				}
				
				this.request.add(line);
			}
			
			// parse request
			this.setUrl();
			this.setMethod();
			
		} 
		catch (Exception ex) {
			
			//ex.printStackTrace();
        }
	}
	
	public String httpGetServerTime() {
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatQ = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US); 
		formatQ.setTimeZone(TimeZone.getTimeZone("GMT"));
		return formatQ.format(cal.getTime());
	}
	
	
	public void writeStatusCode(int s1) throws Exception {

		if(s1 == 200) {
			
			this.response.append("HTTP/1.1 200\r\n");
			this.response.append("Date: " + this.httpGetServerTime() + "\r\n");
			this.response.append("Server: JavaWebServer/1.0 \r\n");
			this.response.append("Last-Modified: " + this.httpGetServerTime() + "\r\n");
			this.response.append("Accept-Ranges: bytes \r\n");
			this.response.append("Connection: close \r\n");
		}
	}


    public void writeHead(String s1, String s2) throws Exception {
        
		this.response.append(s1 + ":" + s2 + "\r\n");
    }
	
	
    public void write(String s1) throws Exception {
        
		int sizeM = s1.length();
		String lenM = String.valueOf(sizeM);
		this.response.append("Content-Length: " + lenM + "\r\n\r\n" + s1 + "\r\n");
    }


    public void end() throws Exception {
        
		try {
 
			// write data
			this.printWriter.print(this.response.toString());
			this.printWriter.flush();
			
			// close all
			this.printWriter.close();
			this.bufferedReader.close();
			this.socket.close();
		} 
		catch (Exception ex) {
			
			//ex.printStackTrace();
		}
    }
}


















