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

public class FileController {

    public static void downloadingFile(Path destination) {
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

    public static void readingFile() throws FileNotFoundException {
        try (BufferedReader br = new BufferedReader(new BufferedReader(new FileReader("src/main/resources/logs.json")))) {
            String line;
            ObjectMapper objectMapper = new ObjectMapper();

            //read each line from the file
            while ((line = br.readLine()) != null) {
                ///parse each line as a JSON object
                JsonNode log = objectMapper.readTree(line);
                //get the httpRequest JSON
                JsonNode httpRequest = log.get("httpRequest");
                //get the timestamp
                String timestamp = log.get("timestamp").asText();
                System.out.println("Parsed Date: " + getDate(timestamp));
                if (httpRequest != null) {
                    ///extract remoteIp and request time from the httpRequest
                    if (httpRequest.has("remoteIp") && httpRequest.has("latency")) {
                        Double requestTime = httpRequest.get("latency").asDouble();
                        String remoteIp = httpRequest.get("remoteIp").asText();
                        System.out.println("country_name: " + getCountryNameByIP(remoteIp));
                        System.out.println("request_time: " + requestTime);
                    }

                    ///extract requestURL
                    if (httpRequest.has("requestUrl")) {
                        String requestURL = httpRequest.get("requestUrl").asText();
                        ///extract pid and apd from the requestUrl
                        ///replace or remove illegal characters in the query
                        String santizedUrl = sanitizeURL(requestURL);
                        URL uri = new URL(santizedUrl);
                        //extract the query parameters
                        String query = uri.getQuery();
                        //extract apd and save it to the apds list
                        String apd = extractParameter(query, "apd");
                        //extract pid and save it to the pids list
                        String pid = extractParameter(query, "pid");
                        System.out.println("apd: " + apd);
                        System.out.println("pid: " + pid);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String extractParameter(String query, String parameterName) {
        String[] parameters = query.split("&");
        for (String parameter : parameters) {
            String[] keyValue = parameter.split("=");
            if (keyValue.length == 2 && keyValue[0].equals(parameterName)) {
                return keyValue[1];
            }
        }
        return null;
    }

    private static String sanitizeURL(String url) {
        // Replace or remove illegal characters in the query
        return url.replaceAll("[|]", ""); // Add more characters if needed
    }

    private static String getCountryNameByIP(String ipAddress) throws IOException, GeoIp2Exception {
        String databaseFile = "src/main/resources/GeoLite2-City.mmdb";
        File database = new File(databaseFile);
        DatabaseReader reader = new DatabaseReader.Builder(database).build();

        InetAddress ip = InetAddress.getByName(ipAddress);
        CityResponse response = reader.city(ip);

        String countryName = response.getCountry().getName();
        return countryName;
    }

    private static String getDate(String timestamp) {

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
