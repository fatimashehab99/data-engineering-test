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
        ///function to the logs.json file
        readingFile();



    }
}