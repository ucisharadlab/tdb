package edu.uci.ics.perpetual.api.logic;

import edu.uci.ics.perpetual.api.models.user.PostUser;
import edu.uci.ics.perpetual.api.models.user.User;
import edu.uci.ics.perpetual.api.utilities.Res;
import edu.uci.ics.perpetual.api.utilities.Sql;

import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.ArrayList;

public class UserLogic {

    public static Response get (Connection con, String email, int authId,
                                String orderBy, String direction, String orderBy2, String direction2,
                                int limit, int offset) {
        if (!(direction.equals("asc") || direction.equals("desc")) ||
                !(direction2.equals("asc") || direction2.equals("desc")) ||
                limit < 0 || limit > 1000 || offset < 0)
            return Res.bad("direction and direction2 must be 'asc' or 'desc'. "+
                    "limit must be be between 0-1000. offset must be positive.");

        String q = "SELECT * FROM public.user WHERE email LIKE ? AND " +
                   "\"authId\" LIKE ? ORDER BY %s %s, %s %s LIMIT %d OFFSET %d";
        q = String.format(q, orderBy, direction, orderBy2, direction2, offset, limit);
        try {
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, "%" + email + "%");
            ps.setString(2, authId == 0 ? "%" : (authId + ""));
            ResultSet rs = ps.executeQuery();

            ArrayList<User> result = new ArrayList<>();
            while (rs.next())
                result.add(new User(rs.getInt("id"), rs.getInt("authId"), rs.getString("name"),
                        rs.getString("email"), rs.getBoolean("admin")));
            return Res.ok(result);
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response post (Connection con, PostUser req) {

        String q = "INSERT INTO public.user(\"authId\", name, email, admin) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, req.getAuthId());
            ps.setString(2, req.getName());
            ps.setString(3, req.getEmail());
            ps.setBoolean(4, req.getAdmin());
            ps.execute();

            return Res.ok(Sql.getLastInsertId(con));
        }
        catch (SQLIntegrityConstraintViolationException e) {
            try {
                ResultSet rs = con.prepareStatement("SELECT * FROM public.user WHERE \"authId\"="+req.getAuthId())
                    .executeQuery();
                rs.next();
                return Res.ok(new User(rs.getInt("id"), rs.getInt("authId"), rs.getString("name"),
                        rs.getString("email"), rs.getBoolean("admin")));
            }
            catch (SQLException ex) {
                return Res.sqlError(ex);
            }
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response get (Connection con, int userId) {

        try {
            ResultSet rs = con.prepareStatement("SELECT * FROM public.user WHERE id=" + userId).executeQuery();
            if (!rs.next())
                return Res.notFound();
            return Res.ok(new User(rs.getInt("id"), rs.getInt("authId"), rs.getString("name"),
                    rs.getString("email"), rs.getBoolean("admin")));
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response put (Connection con, int userId, PostUser req) {

        String q = "UPDATE public.user SET \"authId\"=?, name=?, email=?, admin=? WHERE id="+userId;
        try {
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, req.getAuthId());
            ps.setString(3, req.getEmail());
            ps.setString(2, req.getName());
            ps.setBoolean(4, req.getAdmin());
            ps.execute();

            return Res.ok(userId);
        }
        catch (SQLIntegrityConstraintViolationException e) {
            try {
                ResultSet rs = con.prepareStatement("SELECT * FROM public.user WHERE \"authId\"="+req.getAuthId())
                    .executeQuery();
                rs.next();
                return Res.ok(new User(rs.getInt("id"), rs.getInt("authId"), rs.getString("name"),
                        rs.getString("email"), rs.getBoolean("admin")));
            }
            catch (SQLException ex) {
                return Res.sqlError(ex);
            }
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }

    public static Response delete (Connection con, int userId) {
        try {
            if (Sql.deleteEntry(con, "user", "id", userId) == 0)
                return Res.notFound();
            return Res.ok("user deleted");
        }
        catch (SQLException e) {
            return Res.sqlError(e);
        }
    }
}
