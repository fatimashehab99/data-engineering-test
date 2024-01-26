package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import java.io.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.example.FileController.*;
import static org.example.Request.addRequests;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException, GeoIp2Exception, SQLException {

        //downloading the logs.json file if not exists
        Path destination = Path.of("src/main/resources/logs.json");
        if (!Files.exists(destination)) {
            downloadingFile(destination);
        }
        ///initializing request arrayList for saving the extracted data
        List<Request> requests = new ArrayList<>();
        ///reading json lines from logs.json
        try (BufferedReader br = new BufferedReader(new BufferedReader(new FileReader("src/main/resources/logs.json")))) {
            String line;
            ///helps in reading json
            ObjectMapper objectMapper = new ObjectMapper();

            //read each line from the file
            while ((line = br.readLine()) != null) {
                ///parse each line as a JSON object
                JsonNode log = objectMapper.readTree(line);
                //get the httpRequest JSON
                JsonNode httpRequest = log.get("httpRequest");
                //get the timestamp
                String timestamp = log.get("timestamp").asText();
                ///initializing the needed variables
                String remoteIp = null;
                Double requestTime = null;
                String apd = null;
                String pid = null;
                if (httpRequest != null) {
                    ///extract remoteIp and request time from the httpRequest
                    if (httpRequest.has("remoteIp") && httpRequest.has("latency")) {
                        requestTime = httpRequest.get("latency").asDouble();
                        remoteIp = httpRequest.get("remoteIp").asText();
                    }
                    ///extract requestURL
                    if (httpRequest.has("requestUrl")) {
                        String requestURL = httpRequest.get("requestUrl").asText();
                        ///replace or remove illegal characters in the query
                        String santizedUrl = sanitizeURL(requestURL);
                        URL uri = new URL(santizedUrl);
                        //extract the query parameters
                        String query = uri.getQuery();
                        ///extract pid and apd from the requestUrl
                        apd = extractParameter(query, "apd");
                        //extract pid and save it to the pids list
                        pid = extractParameter(query, "pid");
//                        System.out.println("apd: " + apd);
                    }
                }
                //create a request object that contains the needed data to add later to the database
                Request request = new Request(getCountryNameByIP(remoteIp), requestTime, getDate(timestamp), apd, pid);
                //adding the Request objects to an array list
                requests.add(request);
            }
            ///Add requests(needed data) to the database
            addRequests(requests);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}