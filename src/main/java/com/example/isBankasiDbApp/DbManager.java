package com.example.isBankasiDbApp;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Component
public class DbManager {
    String sql = ""; //will be set to queries
    Connection c = null;
    Statement stmt = null;
    static final String DBNAME = "rates";
    static final String url = "jdbc:postgresql://localhost:5432/";

    public DbManager() { //sets up the database and initializes values that exists
        renewTable();

    }

    private void renewTable() {
        try {
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DBNAME,
                    "postgres", "postgres");
            stmt = c.createStatement();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getErrorCode());
            System.err.println(e.getCause());
            System.err.println(e.getStackTrace());
            System.err.println(e.getClass().getName());
            System.err.println(e.getSQLState());
            sql = "CREATE DATABASE " + DBNAME;
            try {

                c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "postgres", "postgres");
                stmt = c.createStatement();
                stmt.execute(sql);
                c.close();
                c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + DBNAME,
                        "postgres", "postgres");
                stmt = c.createStatement();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();

            }


        }
        Currency[] data = DataFetcher.fetch();
        sql = "CREATE TABLE IF NOT EXISTS rates (BUY real, SELL real, EBUY real, ESELL real, CODE TEXT)";
        try {
            stmt.executeUpdate(sql);
            sql = "TRUNCATE TABLE rates";
            stmt.executeUpdate(sql);
            for (Currency currency : data) {
                /*sql = "INSERT INTO rates VALUES ("+ currency.getRate_buy() + ", " + currency.getRate_sell() + ", " + currency.getEffective_rate_buy()
                + ", " + currency.getEffective_rate_sell() + ", " + currency.getCode() + " )";
                stmt.executeUpdate(sql);*/
                String sql = "INSERT INTO rates (BUY, SELL, EBUY, ESELL, CODE) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = c.prepareStatement(sql);

// Set the values for parameters
                pstmt.setDouble(1, currency.getRate_buy());
                pstmt.setDouble(2, currency.getRate_sell());
                pstmt.setDouble(3, currency.getEffective_rate_buy());
                pstmt.setDouble(4, currency.getEffective_rate_sell());
                pstmt.setString(5, currency.getCode());

// Execute the statement
                pstmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());

            throw new RuntimeException(e);
        }
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    private void updateTable() {
        Currency[] data = DataFetcher.fetch();
        for (Currency currency : data) {
            if (getRate(currency.getCode()) == null) {
                add(currency);
            } else {
                update(currency);
            }
        }

    }

    private ArrayList<Currency> temporaryGenerator() { //exists to test because we can't fetch data
        ArrayList<Currency> rates = new ArrayList<>();
        rates.add(new Currency(34.0f, 33.0f, 34.6f, 33.2f, "USD"));
        rates.add(new Currency(37.0f, 36.0f, 37.5f, 36.4f, "EUR"));

        rates.add(new Currency(39.0f, 38.0f, 39.5f, 38.1f, "GBP"));
        return rates;
    }


    Currency[] getAllRates() {//returns all exchange rates on DB

        sql = "SELECT * FROM rates";
        ArrayList<Currency> rates = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                rates.add(new Currency(rs.getFloat("BUY"), rs.getFloat("SELL"),
                        rs.getFloat("EBUY"), rs.getFloat("ESELL"), rs.getString("CODE")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return rates.toArray(new Currency[rates.size()]);
    }

    Currency getRate(String code) {
        sql = "SELECT * FROM rates WHERE CODE = ?";
        Currency answer;
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                if (rs.next()) throw new IllegalArgumentException("No rate with the code:" + code);
            }
            answer = new Currency(
                    rs.getFloat("buy"),
                    rs.getFloat("sell"),
                    rs.getFloat("ebuy"),
                    rs.getFloat("esell"),
                    rs.getString("code"));

            if (rs.next()) throw new IllegalArgumentException("Mulitple rates with the code:" + code);


        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Could not find element:" + code);
            answer = null;
        }
        return answer;
    }

    void remove(String code) {
        sql = "DELETE FROM rates WHERE CODE = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {

            pstmt.setString(1, code);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Could not remove element:" + code);
        }
    }

    void update(float buy, float sell, float ebuy, float esell, String code) { //sets the rate with given code to given values (post or update)
        sql = "UPDATE rates" +
                "    SET BUY = ?,  SELL = ?, EBUY = ?, ESELL = ?" +
                "    WHERE CODE = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setFloat(1, buy);
            pstmt.setFloat(2, sell);
            pstmt.setFloat(3, ebuy);
            pstmt.setFloat(4, esell);
            pstmt.setString(5, code);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Could not update element:" + code);
        }
    }

    void update(Currency curr) {
        update(curr.getRate_buy(), curr.getRate_sell(),
                curr.getEffective_rate_buy(), curr.getEffective_rate_sell(),
                curr.getCode());
    }

    void add(float buy, float sell, float ebuy, float esell, String code) { //adds column with the given data to DB
        sql = "INSERT INTO rates VALUES (? , ? , ? , ? , ?)";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setFloat(1, buy);
            pstmt.setFloat(2, sell);
            pstmt.setFloat(3, ebuy);
            pstmt.setFloat(4, esell);
            pstmt.setString(5, code);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Could not add element:" + code);
        }
    }

    void add(Currency curr) {
        add(curr.getRate_buy(), curr.getRate_sell(),
                curr.getEffective_rate_buy(), curr.getEffective_rate_sell(),
                curr.getCode());
    }

    static User getUser(String username) {
        UserDao dao = null;
        User u = null;
        try (Connection con = DriverManager.getConnection(url + DBNAME,"postgres","postgres")) {

            dao = new UserDao(con);
            u = dao.getUserWithUsername(username);
        } catch (SQLException e) {
            System.err.println("adafafa");
            throw new RuntimeException(e);
        }

        return u;
    }

}
