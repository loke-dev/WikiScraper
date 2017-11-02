package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Scraper {
    private Writer writer;
    private Pattern hrefPattern = Pattern.compile("href=\"(.*?)\"");
    private Pattern validPattern = Pattern.compile("^/wiki/(\\w*\\(*\\)*/*%*,*!*\\.*-*)*");

    Scraper() {
        writer = new Writer();
    }

    void links(String baseURL, String fileURL, String category) throws IOException {
        String fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.lastIndexOf("."));
        String URL = baseURL + "/Links/" + category + "/" + fileName + ".txt";
        StringBuilder contents = new StringBuilder();
        HashSet<String> set = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileURL))) {
            String line;
            while ((line = br.readLine()) != null) {
                contents.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Matcher m = hrefPattern.matcher(contents);

        while (!m.hitEnd()) {
            if (m.find()) {
                String match = contents.substring(m.start(), m.end());
                String temp = match.substring(match.indexOf("\"") + 1, match.lastIndexOf("\""));
                Matcher m2 = validPattern.matcher(temp);
                if (m2.find() && !match.contains(":")) {
                    String match2 = temp.substring(m2.start(), m2.end());
                    set.add(match2);
                }
            }
        }
        writer.setToFile(set, URL);
    }

    void words(String baseURL, String pageURL, String category) throws IOException {
        String fileName = pageURL.substring(pageURL.lastIndexOf("/") + 1);
        String URL = baseURL + "/Words/" + category + "/" + fileName + ".txt";
        StringBuilder contents = new StringBuilder();
        java.net.URL url = new URL("https://en.wikipedia.org" + pageURL);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            contents.append(line).append("\n");
        }
        reader.close();
        writer.stringToFile(contents.toString(), URL);
    }
}
