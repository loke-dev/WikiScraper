package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Scraper {
    private IOController IO;
    Scraper() {
        IO = new IOController();
    }
    void links(String baseURL, String fileURL, String subFolder) throws IOException {
        String fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.indexOf("."));
        String URL = baseURL + "/Links/" + subFolder + "/" + fileName + ".txt";
        Pattern pattern = Pattern.compile("^\\/wiki\\/(\\w*\\(*\\)*\\/*%*\\,*\\!*\\.*\\-*)*");
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
                if (m2.find() && !match.contains(":")) {
                    String match2 = temp.substring(m2.start(), m2.end());
                    links.append(match2).append("\n");
                }
            }
        }

        IO.writeToFile(links.toString(), URL);
    }

    void words(String baseURL, String pageURL, String subFolder) throws IOException {
        String fileName = pageURL.substring(pageURL.lastIndexOf("/") + 1);
        String URL = baseURL + "/Words/" + subFolder + "/" + fileName + ".txt";
        StringBuilder contents = new StringBuilder();
        java.net.URL url = new URL("https://en.wikipedia.org" + pageURL);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            contents.append(line).append("/n");
        }
        reader.close();
        IO.writeToFile(contents.toString(), URL);
    }
}
