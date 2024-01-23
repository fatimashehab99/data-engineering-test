package org.example;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.example.File.downloadingFile;
import static org.example.File.readingFile;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {

        //downloading the logs.json file if not exists
        Path destination = Path.of("src/main/resources/logs.json");
        if (!Files.exists(destination)) {
            downloadingFile(destination);
        }
        readingFile();

        ///parsing json
//        String json = "{\"name\":\"fatima\",\"age\":25}";
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            person personn = objectMapper.readValue(json, person.class);
//            System.out.println("Name:" + personn.getName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        String filePath = "src/main/resources/logs.json";
//
//        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            // List to store severity values
//            List<String> severities = new ArrayList<>();
//
//            // Read each line from the file
//            while ((line = br.readLine()) != null) {
//                // Parse each line as a JSON object
//                JsonNode log = objectMapper.readTree(line);
//
//
//                // Navigate to the "jsonPayload" field, which contains another JSON object
//                JsonNode jsonPayload = log.get("httpRequest");
//
//                // Extract "severity" from the nested JSON object
//                if (jsonPayload != null && jsonPayload.has("remoteIp")) {
//                    String severity = jsonPayload.get("remoteIp").asText();
//                    severities.add(severity);
//                }
//                ;
//            }
//
//            // Print or further process the stored severities
//            System.out.println("Severities: " + severities);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}