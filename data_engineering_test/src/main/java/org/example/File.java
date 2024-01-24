package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.*;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class File {

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

            ///lists to store the needed attributes apd/pid
            List<String> apds = new ArrayList<>();
            List<String> pids = new ArrayList<>();

            //read each line from the file
            while ((line = br.readLine()) != null) {
                ///parse each line as a JSON object
                JsonNode log = objectMapper.readTree(line);
                //get the httpRequest JSON
                JsonNode httpRequest = log.get("httpRequest");
                ///extract requestURL
                if (httpRequest != null && httpRequest.has("requestUrl")) {
                    String requestURL = httpRequest.get("requestUrl").asText();

                    ///extract pid and apd from the requestUrl
                    ///replace or remove illegal characters in the query
                    String santizedUrl = sanitizeURL(requestURL);
                    URL uri = new URL(santizedUrl);
                    //extract the query parameters
                    String query = uri.getQuery();

                    //extract apd and save it to the apds list
                    String apd = extractParameter(query, "apd");
                    apds.add(apd);

                    //extract pid and save it to the pids list
                    String pid = extractParameter(query, "pid");
                    pids.add(pid);
                }
                ///extract remoteIp and request time from the httpRequest
                if (httpRequest != null && httpRequest.has("remoteIp") && httpRequest.has("latency")) {
                    Double requestTime = httpRequest.get("latency").asDouble();
                    String remoteIp = httpRequest.get("remoteIp").asText();
                }

            }
            System.out.println(apds);
            System.out.println(pids);
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


}
