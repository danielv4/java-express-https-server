
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


public class Express {
	
	public String responseCallback;
	public SSLContext ctx;
	public SSLServerSocket serverSocket;
	
	
    public void httpSSLEngineOn(String s1, String s2) throws Exception {
        
		// ssl
		KeyStore store = KeyStore.getInstance("JKS");
		store.load(new FileInputStream(s1), s2.toCharArray());


		// key manager
		KeyManagerFactory keyManager = KeyManagerFactory.getInstance("SunX509");
		keyManager.init(store, "passphrase".toCharArray());
		KeyManager[] w1 = keyManager.getKeyManagers();


		// trust manager
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
		trustManagerFactory.init(store);
		TrustManager[] w2 = trustManagerFactory.getTrustManagers();


		// initialize
		SSLContext ctxG = SSLContext.getInstance("TLSv1");
		ctxG.init(w1,  w2, null);

		
		// set
		this.ctx = ctxG;
    }
	
	
    public void httpListen(String s1, int s2, int s3) throws Exception {
        
		// create server socket factory
		SSLServerSocketFactory sslFactory = this.ctx.getServerSocketFactory();

		// create server socket
		InetAddress addr = InetAddress.getByName(s1);
		serverSocket = (SSLServerSocket)sslFactory.createServerSocket(s2, s3, addr);
		
    }
	
	
    public void httpSetResponseCallback(String s1) throws Exception {
        
		this.responseCallback = s1;
    }
	
	
    public void httpPeerConnection() throws Exception {
        
		//
		SSLSocket clientSocket = (SSLSocket)serverSocket.accept();
		
		// 
		new ExpressThread(clientSocket, this.responseCallback).start();	
    }
}

















