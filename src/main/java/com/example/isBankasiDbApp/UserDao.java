package com.example.isBankasiDbApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Objects;



public class UserDao implements AutoCloseable {
    private final Connection con;
    private boolean open;

    public UserDao(Connection con) {
        this.con = Objects.requireNonNull(con, "Connection must not be null");
        open = true;
    }

    public User getUser(Long id) {
        if (!open) {
            throw new IllegalStateException("Connection already closed.");
        }
        User user = null;
        try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM users  WHERE userid= ?")) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));

            } else {
                throw new NoSuchElementException();

            }
            if (rs.next()) {
                throw new IllegalArgumentException("More than one Entity matches id");
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            System.err.println("Could not get entry with id:" + id);
        }
        return user;
    }

    public User getUserWithUsername(String username) {
        if (!open) {
            throw new IllegalStateException("Connection already closed.");
        }
        User user = null;
        //beware that usernames don't match with one another
        try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM users  WHERE username= ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                user = new User();
                user.setId(rs.getLong("userid"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));

            } else {
                throw new NoSuchElementException();

            }
            if (rs.next()) {
                throw new IllegalArgumentException("More than one Entity matches username");
            }
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            System.err.println("Could not get entry with id:" + username);
        }
        return user;
    }

    @Override
    public void close() throws SQLException {
        if (open) {
            con.close();
            open = false;
        }
    }
}