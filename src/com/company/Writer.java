package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

class Writer {
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

    void setToFile(HashSet set, String URL) throws IOException {
        String output = setToString(set);
        try (java.io.Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(URL), "utf-8"))) {
            writer.write(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void stringToFile(String payload, String URL) throws IOException {
        try (java.io.Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(URL), "utf-8"))) {
            writer.write(payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String setToString(HashSet set) {
        StringBuilder links = new StringBuilder();
        for (Object s : set) {
            links.append(s.toString()).append("\n");
        }
        return links.toString();
    }
}
