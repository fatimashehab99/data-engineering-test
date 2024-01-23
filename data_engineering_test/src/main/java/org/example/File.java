package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
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

            ///lists to store the needed attributes
            List<String> requestURLs = new ArrayList<>();
            //read each line from the file
            while ((line = br.readLine()) != null) {
                ///parse each line as a JSON object
                JsonNode log = objectMapper.readTree(line);
                //get the httpRequest JSON
                JsonNode httpRequest = log.get("httpRequest");
                ///extract requestURL
                if (httpRequest != null && httpRequest.has("requestUrl")) {
                    String requestURL = httpRequest.get("requestUrl").asText();
                    requestURLs.add(requestURL);
                }
                ///show the results
                System.out.println("Request URL: " + requestURLs);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
