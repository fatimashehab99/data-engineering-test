package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.example.Request.addRequests;


public class FileController {

    public static void downloadFile(Path destination) {
        //Downloading the JSON logs link
        try {
            URL url = new URL("https://static.cognativex.com/test/logs.json");
            InputStream inputStream = url.openStream();
            ///copy the content of the json log file to the destination file
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File downloaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String extractParameter(String query, String parameterName) {
        String[] parameters = query.split("&");
        for (String parameter : parameters) {
            String[] keyValue = parameter.split("=");
            if (keyValue.length == 2 && keyValue[0].equals(parameterName)) {
                return keyValue[1];
            }
        }
        return null;
    }

    static String sanitizeURL(String url) {
        // Replace or remove illegal characters in the query
        return url.replaceAll("[|]", ""); // Add more characters if needed
    }
//this function is used get the country name from the remoteIp
    static String getCountryNameByIP(String ipAddress) throws IOException, GeoIp2Exception {
        String databaseFile = "src/main/resources/GeoLite2-City.mmdb";
        File database = new File(databaseFile);
        DatabaseReader reader = new DatabaseReader.Builder(database).build();

        InetAddress ip = InetAddress.getByName(ipAddress);
        CityResponse response = reader.city(ip);

        String countryName = response.getCountry().getName();
        return countryName;
    }
///This function is used to get the date from the timestamp
    static String getDate(String timestamp) {

        ///parse the timestamp string
        Instant instant = Instant.parse(timestamp);

        //Convert Instant to local datetime
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));

        ///Extract date from localdatetime
        LocalDate localDate = localDateTime.toLocalDate();

        // Format LocalDate as a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = localDate.format(formatter);
        return formattedDate;
    }


}
