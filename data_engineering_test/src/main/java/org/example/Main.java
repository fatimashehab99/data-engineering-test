package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import java.io.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.example.FileController.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException, GeoIp2Exception {

        //downloading the logs.json file if not exists
        Path destination = Path.of("src/main/resources/logs.json");
        if (!Files.exists(destination)) {
            downloadingFile(destination);
        }

        ///reading logs.json file and extract the needed attributes
        readingFile();


    }
}