package server;

import org.apache.log4j.PropertyConfigurator;
import org.restlet.Component;
import org.restlet.data.Protocol;

import util.Cons;
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
        Cons.ROOT = util.getStrFromPropertiesFile(Cons.APP_PROPERTIES, "root");
        Cons.SLOW_RESPONSE_TIME = Long.parseLong(util.getStrFromPropertiesFile(Cons.APP_PROPERTIES, "slow_response_time"));
        
        Cons.PHOENIX_CONN_STR = util.getStrFromPropertiesFile(Cons.PHOENIX_PROPERTIES, "phoenix_conn_str");
        
        Cons.S_BLACK_LIST_QUERY = util.getSetStrFromPropertiesFile(Cons.BLACKLIST_PROPERTIES, "query");
        
        Cons.REDIS_HOST = util.getStrFromPropertiesFile(Cons.REDIS_PROPERTIES, "host");
        Cons.REDIS_PORT = Integer.parseInt(util.getStrFromPropertiesFile(Cons.REDIS_PROPERTIES, "port"));
        Cons.REDIS_DB = Integer.parseInt(util.getStrFromPropertiesFile(Cons.REDIS_PROPERTIES, "db"));
    }
}
