package edu.uci.ics.perpetual.api.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Sql {

    public static int getLastInsertId (Connection con) throws SQLException {
        String query = "SELECT LAST_INSERT_ID() AS id";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        if (!rs.next())
            throw new SQLException();
        return rs.getInt("id");
    }

    public static int deleteEntry (Connection con, String tableName, String idName, int id)
            throws SQLException {
        String query = String.format("DELETE FROM %s WHERE %s=%d", tableName, idName, id);
        PreparedStatement ps = con.prepareStatement(query);
        return ps.executeUpdate();
    }
}
