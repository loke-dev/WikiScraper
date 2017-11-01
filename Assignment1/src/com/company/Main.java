package com.company;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        String baseURL = "/home/loke/Code/School/2DV515/Assignment1/src/data";

        createStructure();

        scrapeWords(baseURL, "/wiki/Programming_language", "Programming");

        List<Path> linkList = getURL(baseURL, "/Words");

        for (Path link : linkList) {
            scrapeLinks(baseURL, link.toString(), "Programming");
        }

        List<String> newLinks = getListOfURLs(baseURL + "/Links/Programming/Programming_language.txt");

        for (String link : newLinks) {
            scrapeWords(baseURL, link, "Programming");
        }

    }

    private static List<String> getListOfURLs(String fileURL) {
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

    private static List<Path> getURL(String baseURL, String folder) throws IOException {
        return Files.walk(Paths.get(baseURL + folder))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
    }

    private static void writeToFile(String payload, String URL) throws IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(URL), "utf-8"))) {
            writer.write(payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void scrapeLinks(String baseURL, String fileURL, String subFolder) throws IOException {
        String fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.indexOf("."));
        String URL = baseURL + "/Links/" + subFolder + "/" + fileName + ".txt";
        Pattern pattern = Pattern.compile("\\/wiki\\/(\\w*\\(*\\)*\\/*%*\\,*\\!*\\.*\\-*)*");
        Pattern p = Pattern.compile("href=\"(.*?)\"");
        StringBuilder contents = new StringBuilder();
        StringBuilder links = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(fileURL))) {
            String line;
            while ((line = br.readLine()) != null) {
                contents.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Matcher m = p.matcher(contents);

        while (!m.hitEnd()) {
            if (m.find()) {
                String match = contents.substring(m.start(), m.end());
                String temp = match.substring(match.indexOf("\"") + 1, match.lastIndexOf("\""));
                Matcher m2 = pattern.matcher(temp);
                if (m2.find()) {
                    String match2 = temp.substring(m2.start(), m2.end());
                    links.append(match2).append("\n");
                }
            }
        }

        writeToFile(links.toString(), URL);
    }

    private static void scrapeWords(String baseURL, String pageURL, String subFolder) throws IOException {
        String fileName = pageURL.substring(pageURL.lastIndexOf("/") + 1);
        String URL = baseURL + "/Words/" + subFolder + "/" + fileName + ".txt";
        StringBuilder contents = new StringBuilder();
        URL url = new URL("https://en.wikipedia.org" + pageURL);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            contents.append(line).append("/n");
        }
        reader.close();
        writeToFile(contents.toString(), URL);
    }

    private static void createStructure() {
        createRootFolder();
        createSubFolders("Words");
        createSubFolders("Links");
        createCategory("Links", "Programming");
        createCategory("Links", "Games");
        createCategory("Words", "Programming");
        createCategory("Words", "Games");
    }

    private static void createRootFolder() {
        File file = new File("/home/loke/Code/School/2DV515/Assignment1/src/data");
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("directory is not created");
            }
        }
    }

    private static void createSubFolders(String folderName) {
        File File = new File("/home/loke/Code/School/2DV515/Assignment1/src/data/" + folderName);
        if (!File.exists()) {
            if (!File.mkdirs()) {
                System.out.println("failed to create sub directories");
            }
        }
    }

    private static void createCategory(String folderName, String category) {
        File File = new File("/home/loke/Code/School/2DV515/Assignment1/src/data/" + folderName + "/" + category);
        if (!File.exists()) {
            if (File.mkdirs()) {
                System.out.println("failed to create category");
            }
        }
    }
}
