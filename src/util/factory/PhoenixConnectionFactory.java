package util.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import util.Cons;
import util.Util;

public class PhoenixConnectionFactory {
    private static PhoenixConnectionFactory connectionFactory = null;

    public Connection getConnection() {
            Connection conn = null;
            try {
            	conn = DriverManager.getConnection(Cons.PHOENIX_CONN_STR);
	        } catch (Exception e) {
	                e.printStackTrace();
	        }
            return conn;
    }

    public static PhoenixConnectionFactory getInstance() {
            if (connectionFactory == null) {
                    connectionFactory = new PhoenixConnectionFactory();
            }
            return connectionFactory;
    }
}