package edu.uci.ics.perpetual.api.utilities;

import edu.uci.ics.perpetual.api.Main;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Res {

    private static void log (String className, String methodName, int statusCode, String message) {
        if (!Main.getEnableLogging()) return;
        Connection con;
        try {                       con = Main.getConPool().requestCon(); }
        catch (SQLException e) {
            //System.out.println("get con error");
            return;
        }
        try {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO responses(uri, statusCode, message) VALUES (?, ?, ?)");
            ps.setString(1, className + ", " + methodName);
            ps.setInt(2, statusCode);
            ps.setString(3, message);
            ps.execute();
            Main.getConPool().releaseCon(con);
        }
        catch (SQLException e) {
            Main.getConPool().releaseCon(con);
        }
    }

    public static Response ok (int id) {
        //if (Math.random() < 0.005) System.gc(); // random garbage collection...

        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        log(caller.getClassName(), caller.getMethodName(), 200, "");

        Map<String, Integer> body = new HashMap<>();
        body.put("id", id);
        return Response.status(Response.Status.OK).entity(body).build();
    }

    public static Response ok (Object body) {
        //if (Math.random() < 0.005) System.gc(); // random garbage collection...

        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        log(caller.getClassName(), caller.getMethodName(), 200, "");

        return Response.status(Response.Status.OK).entity(body).build();
    }

    public static Response nullBody () {
        return bad("Request body cannot be null");
    }

    public static Response bad (String message) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        log(caller.getClassName(), caller.getMethodName(), 400, message);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(message).build();
    }

    public static Response notFound () {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        log(caller.getClassName(), caller.getMethodName(), 404, "");

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public static Response error (Exception e) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        log(caller.getClassName(), caller.getMethodName(), 500, e.getMessage());

        System.out.println("Internal Server Error: " + e.getMessage());
        //e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(e.toString()).build();
    }

    public static Response error (Exception e, String message) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        log(caller.getClassName(), caller.getMethodName(), 500, message);

        System.out.println("Internal Server Error: " + message);
        //e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(message+"\n\n"+e.toString()).build();
    }

    public static Response sqlError (Exception e) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        log(caller.getClassName(), caller.getMethodName(), 500, e.getMessage());

        System.out.println("SQL error: " + e.getMessage());
        //e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("MySQL error\n\n"+e.toString()).build();
    }

    public static Response sqlError (Exception e, String message) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        log(caller.getClassName(), caller.getMethodName(), 500, e.getMessage());

        System.out.println("SQL error: " + message);
        //e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(message+"\n\n"+e.toString()).build();
    }
}
