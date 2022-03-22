package edu.uci.ics.perpetual.api.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

public class ConnectionPool {
    private LinkedList<Connection> connections;
    private String url;
    private String username;
    private String password;

    public ConnectionPool (int numCons, String driver, String url, String username, String password)
            throws ClassNotFoundException, SQLException
    {
        connections = new LinkedList<>();
        this.url = url;
        this.username = username;
        this.password = password;

        Class.forName(driver);
        for (int i = 0; i < numCons; ++i) {
            System.out.println("Creating connection #" + (i + 1));
            Connection con = createConnection();
            connections.add(con);
        }
    }

    public synchronized Connection alterPool (boolean isRequestCon, Connection con) throws SQLException {
        //System.out.println("altering pool, " + isRequestCon);
        if (isRequestCon) {
            if (connections.size() >= 100)
                throw new SQLException("SQL connection pool has exceeded connection limit.");

            if (connections.isEmpty()) {
                //System.out.println("no available connections, creating 1 more.");
                return createConnection();
            }
            else {
                //System.out.println("available connection found. curr #: " + (connections.size() - 1));
                return connections.removeFirst();
            }
        }
        else {
            //System.out.println("released connection. curr #: " + (connections.size() + 1));
            connections.add(con);
            return null;
        }
    }

    public Connection requestCon() throws SQLException {
        return alterPool(true, null);
    }

    public void releaseCon (Connection con) {
        try {
            alterPool(false, con);
        }
        catch (SQLException e) {
            System.out.println("releaseCon SQLException");
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
