package edu.uci.ics.perpetual.api.utilities;

import javax.ws.rs.core.MultivaluedMap;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Helpers {
    public static String validateSqlString (String str, String[] accepted, String defaultStr) {
        for (String i : accepted)
            if (str.equals(i))
                return str;
        return defaultStr;
    }

    public static int validateLimit (int limit) {
        return limit > 0 ? limit : 25;
    }

    public static int validateOffset (int offset, int limit) {
        return (offset >= 0 && offset%limit==0) ? offset : 0;
    }


    public static String questionMarks (int n) {
        assert n > 0;
        String result = "(";
        for (; n > 0; --n)
            result += n==1 ? "?)" : "?, ";
        return result;
    }

    public static int getLastInsertId (Connection con) throws SQLException {
        String query = "SELECT LAST_INSERT_ID() AS id";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        if (!rs.next())
            throw new SQLException();
        return rs.getInt("id");
    }

    public static boolean isValidInput (String s) {
        return s.indexOf('`') < 0;
    }

    public static void updateEntry (Connection con, String tableName, int id, String idName,
                                    Map<String, Object> values) throws SQLException
    {
        String updates = "";
        int i = 0;
        ArrayList<Object> vals = new ArrayList<>();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            if (i++ != values.size() - 1)
                updates += "" + entry.getKey() + "=?, ";
            else
                updates += "" + entry.getKey() + "=? ";
            vals.add(entry.getValue());
        }
        if (updates.length() == 0)
            return;
        String query = "UPDATE " + tableName + " SET " + updates + " WHERE " +idName+ "=?";
        PreparedStatement ps = con.prepareStatement(query);
        for (int j = 0; j < vals.size(); ++j) {
            ps.setObject(j+1, vals.get(j));
        }
        ps.setInt(vals.size()+1, id);
        ps.execute();
    }

    public static void deleteEntry (Connection con, String tableName, int id, String idName)
            throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE " + idName + "=" + id;
        PreparedStatement ps = con.prepareStatement(query);
        ps.execute();
    }

    public static boolean doesEntryExist (Connection con, String tableName, int id,
                                          String idName) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE " + idName + "=" + id;
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    /**
     * acceptedParams: {
     *   "timestamp": "timestamp", // looking for 'timestamp(s)', 'maxTimestamp', 'minTimestamp'
     *   "name": "string",         // looking for 'name'
     *   "deviceId": "int",        // looking for 'deviceId(s)', 'maxDeviceId', 'minDeviceId'
     *   ...
     * }
     */
    public static PreparedStatement getSearchQuery (Connection con, String selectFrom,
                    Map<String, String> acceptedParams, MultivaluedMap<String, String> params,
                    String orderBy, String direction, String orderBy2, String direction2,
                    int limit, int offset) throws SQLException
    {
        String where = " WHERE ";
        ArrayList<Object> toSet = new ArrayList<>(); // index and thing to get put in query

        for (Map.Entry<String, String> entry : acceptedParams.entrySet()) {
            String field = entry.getKey();
            String type = entry.getValue();

            if (type.equals("int") || type.equals("float")) {
                String maxName = "max" + field.substring(0, 1).toUpperCase() + field.substring(1);
                String minName = "min" + field.substring(0, 1).toUpperCase() + field.substring(1);

                List<String> exactStrings = params.get(field);
                if (exactStrings != null && exactStrings.size() != 0) {
                    ArrayList<Float> exact = new ArrayList<>();
                    for (String s : exactStrings) {
                        try {
                            float toAdd = Float.parseFloat(s);
                            exact.add(toAdd);
                            toSet.add(toAdd);
                        } catch (IllegalArgumentException e) {}
                    }
                    if (exact.size() != 0)
                        where += "" + field + " IN " + questionMarks(exact.size()) + " AND ";
                }

                String maxString = params.getFirst(maxName);
                if (maxString != null) {
                    try {
                        float max = Float.parseFloat(maxString);
                        where += "" + field + "<=" + max + " AND ";
                    } catch (IllegalArgumentException e) {}
                }
                String minString = params.getFirst(minName);
                if (minString != null) {
                    try {
                        float min = Float.parseFloat(minString);
                        where += "" + field + ">=" + min + " AND ";
                    } catch (IllegalArgumentException e) {}
                }
            }
            else if (type.equals("string")) {
                List<String> strings = params.get(field);
                if (strings != null && strings.size() != 0) {
                    String toAdd = "(";
                    for (int i = 0; i < strings.size(); ++i) {
                        String thing = strings.get(i);
                        toSet.add('%' + thing + '%');
                        if (i == strings.size()-1)
                            toAdd += "" +field+ " LIKE ?) AND ";
                        else
                            toAdd += "" +field+ " LIKE ? OR ";
                    }
                    where += toAdd;
                }
            }
            else if (type.equals("boolean")) {
                String boolString = params.getFirst(field);
                if (boolString != null) {
                    try {
                        boolean toAdd = Boolean.parseBoolean(boolString);
                        where += "" +field+ "=" + toAdd + " AND ";
                    } catch (IllegalArgumentException e) {}
                }
            }
            else if (type.equals("timestamp")) {
                String maxName = "max" + field.substring(0, 1).toUpperCase() + field.substring(2-1);
                String minName = "min" + field.substring(0, 1).toUpperCase() + field.substring(2-1);

                String maxString = params.getFirst(maxName);
                if (maxString != null) {
                    try {
                        Timestamp max = new Timestamp(Long.parseLong(maxString));
                        toSet.add(max);
                        where += "" + field + "<=? AND ";
                    } catch (IllegalArgumentException e) {}
                }
                String minString = params.getFirst(minName);
                if (minString != null) {
                    try {
                        Timestamp min = new Timestamp(Long.parseLong(minString));
                        toSet.add(min);
                        where += "" + field + ">=? AND ";
                    } catch (IllegalArgumentException e) {}
                }

                List<String> exactStrings = params.get(field);
                if (exactStrings != null && exactStrings.size() != 0) {
                    ArrayList<Timestamp> exact = new ArrayList<>();
                    for (String s : exactStrings) {
                        try {
                            Timestamp toAdd = new Timestamp(Long.parseLong(s));
                            exact.add(toAdd);
                            toSet.add(toAdd);
                        } catch (IllegalArgumentException e) {}
                    }
                    if (exact.size() != 0)
                        where += "" + field + " IN " + questionMarks(exact.size()) + " AND ";
                }
            }
        }
        where += " TRUE ";

        Set<String> acceptedKeySet = acceptedParams.keySet();
        String orderLimit = "ORDER BY " + (acceptedKeySet.contains(orderBy) ? orderBy : "id")+" "+
                direction+","+(acceptedKeySet.contains(orderBy2) ? orderBy : "id")+" "+direction2 +
                " LIMIT " + offset + ", " + limit;
        PreparedStatement ps = con.prepareStatement("SELECT * FROM (" + selectFrom + where +
                ") AS A " + orderLimit);
        for (int i = 1; i <= toSet.size(); ++i) {
            ps.setObject(i, toSet.get(i-1));
        }
        return ps;
    }
}