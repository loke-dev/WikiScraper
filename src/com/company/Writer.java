package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

class Writer {
    List<String> getListOfURLs(String fileURL) throws IOException {
        return Files.lines(Paths.get(fileURL)).collect(Collectors.toList());
    }

    List<Path> getURL(String baseURL, String folder) throws IOException {
        return Files.walk(Paths.get(baseURL + folder))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
    }

    void setToFile(HashSet set, String URL) throws IOException {
        Path path = Paths.get(URL);
        byte[] strToBytes = setToString(set).getBytes();
        Files.write(path, strToBytes);
    }

    void stringToFile(String payload, String URL) throws IOException {
        Path path = Paths.get(URL);
        byte[] strToBytes = payload.getBytes();
        Files.write(path, strToBytes);
    }

    private String setToString(HashSet set) {
        StringBuilder links = new StringBuilder();
        for (Object s : set) {
            links.append(s.toString()).append("\n");
        }
        return links.toString();
    }
}
