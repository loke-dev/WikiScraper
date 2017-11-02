package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class IOController {
    List<String> getListOfURLs(String fileURL) {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileURL))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    List<Path> getURL(String baseURL, String folder) throws IOException {
        return Files.walk(Paths.get(baseURL + folder))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
    }

    void writeToFile(String payload, String URL) throws IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(URL), "utf-8"))) {
            writer.write(payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
