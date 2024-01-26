package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.example.DatabaseConnection.getConnection;

public class Request {
    private String countryName;
    private Double requestTime;
    private String date;
    private String websiteDomain;
    private String postId;

    public Request(String countryName, Double requestTime, String date, String websiteDomain, String postId) {
        this.countryName = countryName;
        this.requestTime = requestTime;
        this.date = date;
        this.websiteDomain = websiteDomain;
        this.postId = postId;
    }

    public String getCountryName() {
        return countryName;
    }

    public Double getRequestTime() {
        return requestTime;
    }

    public String getDate() {
        return date;
    }

    public String getWebsiteDomain() {
        return websiteDomain;
    }

    public String getPostId() {
        return postId;
    }

    public static void addRequests(List<Request> requests) throws SQLException {
        ///get the MySql connection
        Connection connection = getConnection();
        try (connection) {
            String query = "INSERT INTO requests (country_name,request_time,request_date,website_domain,post_id) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            for (Request request : requests) {
                preparedStatement.setString(1, request.getCountryName());
                preparedStatement.setDouble(2, request.getRequestTime());
                preparedStatement.setString(3, request.getDate());
                preparedStatement.setString(4, request.getWebsiteDomain());
                preparedStatement.setString(5, request.getPostId());

                //using batch for optimization purpose
                preparedStatement.addBatch();
            }
            int[] batchResult = preparedStatement.executeBatch();
            System.out.println("Data inserted successfully");
            preparedStatement.close();
        } catch (
                SQLException e) {
            System.err.println("Inserting data failed" + e.getMessage());
        }
    }
}
