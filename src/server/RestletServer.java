package server;

import org.apache.log4j.PropertyConfigurator;
import org.restlet.Component;
import org.restlet.data.Protocol;

import util.Util;
import application.QueryServiceApplication;

public class RestletServer {
    public static void main(String[] args) throws Exception {
    	ClassLoader classLoader = Thread.currentThread().getContextClassLoader(); 
    	PropertyConfigurator.configure(classLoader.getResource("log4j.properties")); 
    	Util util = new Util();
    	
        // Create a new Component.  
        Component component = new Component();  
  
        // Add a new HTTP server listening on port 8182.  
        component.getServers().add(Protocol.HTTP, 9090);  
  
        // Attach application.  
        component.getDefaultHost().attach("", new QueryServiceApplication());  
  
        // Start the component.  
        component.start();
        
        // read conf
        util.readPhoenixConf();
        util.readRedisConf();
    }
}
