
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





class ExpressThread extends Thread {

    public ExpressThread(SSLSocket socket, String s1) throws Exception {

		// must clear all files and .class before running

        Class[] parameterTypes = new Class[2];
		parameterTypes[0] = ExpressObject.class;
		parameterTypes[1] = MainExpress.class;
        Method callback = MainExpress.class.getMethod(s1, parameterTypes);
		
		
		ExpressObject obj = new ExpressObject();
		obj.socket = socket;
		obj.initRequest();


        Object[] parameters = new Object[2];
        parameters[0] = obj;
		parameters[1] = new MainExpress();


        callback.invoke(new MainExpress(), parameters);
    }

    @Override
    public void run() {

    }
}












