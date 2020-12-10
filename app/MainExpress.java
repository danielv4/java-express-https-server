// javac MainExpress.java
// jar cvfe main.jar MainExpress *.class
// java -jar main.jar
// javac MainExpress.java && jar cvfe main.jar MainExpress *.class && java -jar main.jar


// keytool -genkey -keyalg RSA -alias selfsigned -keystore test.jks -storepass passphrase -validity 360 -keysize 2048

import java.lang.reflect.Method;
import java.io.*;
import java.lang.Thread;
import java.util.ArrayList;

public class MainExpress {
	
	public void calledFrom() {
		
		System.out.println(">> calledFrom");
	}
	
    public void onHttpResponse(ExpressObject obj, MainExpress main) throws Exception {
	
		System.out.println(obj.url);
		System.out.println(obj.method);
		
		if(obj.url != null) {
		
			if(obj.url.equals("/page2") == true) {
				
				obj.writeStatusCode(200);
				obj.writeHead("Content-Type", "text/html");
				obj.write("<html><body><div>page Okkkk</div></body></html>");
				obj.end();		
			}
			else {
				
				obj.writeStatusCode(200);
				obj.writeHead("Content-Type", "application/json");
				obj.write("{\"status\":\"null\"}");
				obj.end();		
			}
		}
    }

    public static void main(String[] args) throws Exception {
		
        Express server = new Express();
		
		server.httpSSLEngineOn("test.jks", "passphrase");
		
		int peerLimit = 100;
		
		server.httpListen("0.0.0.0", 8080, peerLimit);
		
		server.httpSetResponseCallback("onHttpResponse");
		
		while(true) {
			
			server.httpPeerConnection();
		}
    }
}