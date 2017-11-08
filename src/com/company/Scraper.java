package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Source;

class Scraper {
    private Writer writer;
    private String baseURL;
    private int MAX_COUNTER;
    private HashSet<String> visited;
    private Pattern wordPattern = Pattern.compile("\\w+");
    private Pattern hrefPattern = Pattern.compile("href=\"(.*?)\"");
    private Pattern validPattern = Pattern.compile("^/wiki/(\\w*\\(*\\)*/*%*,*!*\\.*-*)*");

    Scraper() {
        visited = new HashSet<>();
        writer = new Writer();
    }

    void main(String category, String startURL) throws IOException {
        baseURL = "./src/data";
        MAX_COUNTER = 500;
        initScrape(category, startURL);
    }

    private void initScrape(String category, String startURL) throws IOException {
        String fileName = startURL.substring(startURL.lastIndexOf("/") + 1);
        String linkURL = baseURL + "/Links/" + category + "/" + fileName + ".txt";
        Writer writer = new Writer();
        Scraper Scrape = new Scraper();

        //Scrape the initial page to get links (Level 0)
        Scrape.html(baseURL, startURL, category);
        System.out.println("Scraping initial page (Level 0)");

        //Scrape the pages that came from level 0 scrape (Level 1)
        List<String> newLinks = writer.getListOfURLs(linkURL);
        for (String link : newLinks) {
            if (!visited.contains(link) && (visited.size() + 1) <= MAX_COUNTER) {
                Scrape.html(baseURL, link, category);
                System.out.println("Scraping page " + visited.size() + " of " + MAX_COUNTER + " (Level 1)");
                visited.add(link);
            }
        }
        //Scrape the pages that came from level 1 scrape (Level 2)
        List<Path> last = writer.getURL(baseURL, "/HTML");
        for (Path p : last) {
            String URL = p.toString().replace("./src/data/HTML/", "./src/data/Links/");
            List<String> lastLinks = writer.getListOfURLs(URL);
            for (String link : lastLinks) {
                if (!visited.contains(link) && (visited.size() + 1) <= MAX_COUNTER) {
                    Scrape.html(baseURL, link, category);
                    System.out.println("Scraping page " + visited.size() + " of " + MAX_COUNTER  + " (Level 2)");
                    visited.add(link);
                }
            }
        }
    }

    private void extractLinks(String payload, String baseURL, String fileURL, String category) throws IOException {
        HashSet<String> set = new HashSet<>();
        String fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.lastIndexOf("."));
        String URL = baseURL + "/Links/" + category + "/" + fileName + ".txt";

        Matcher m = hrefPattern.matcher(payload);

        while (!m.hitEnd()) {
            if (m.find()) {
                String match = payload.substring(m.start(), m.end());
                String substring = match.substring(match.indexOf("\"") + 1, match.lastIndexOf("\""));
                Matcher m2 = validPattern.matcher(substring);
                if (m2.find() && !match.contains(":")) {
                    String match2 = substring.substring(m2.start(), m2.end());
                    set.add(match2);
                }
            }
        }
        writer.setToFile(set, URL);
    }

    private void extractWords(String payload, String baseURL, String fileURL, String category) throws IOException {
        String fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.lastIndexOf("."));
        String URL = baseURL + "/Words/" + category + "/" + fileName + ".txt";
        Source src = new Source(payload);
        Renderer rend = new Renderer(src);

        writer.stringToFile(rend.toString(), URL);
    }

    private void extractText(String payload, String baseURL, String fileURL, String category) throws IOException {
        String fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.lastIndexOf("."));
        String URL = baseURL + "/Text/" + category + "/" + fileName + ".txt";
        StringBuilder contents = new StringBuilder();

        Matcher m = wordPattern.matcher(payload);

        while (!m.hitEnd()) {
            if (m.find()) {
                String match = payload.substring(m.start(), m.end());
                if (m.find()) {
                    contents.append(match).append(" ");
                }
            }
        }
        writer.stringToFile(contents.toString(), URL);
    }

//    void links(String baseURL, String fileURL, String category) throws IOException {
//        String fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.lastIndexOf("."));
//        String URL = baseURL + "/Links/" + category + "/" + fileName + ".txt";
//        StringBuilder contents = new StringBuilder();
//        HashSet<String> set = new HashSet<>();
//
//        try (BufferedReader br = new BufferedReader(new FileReader(fileURL))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                contents.append(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Matcher m = hrefPattern.matcher(contents);
//
//        while (!m.hitEnd()) {
//            if (m.find()) {
//                String match = contents.substring(m.start(), m.end());
//                String temp = match.substring(match.indexOf("\"") + 1, match.lastIndexOf("\""));
//                Matcher m2 = validPattern.matcher(temp);
//                if (m2.find() && !match.contains(":")) {
//                    String match2 = temp.substring(m2.start(), m2.end());
//                    set.add(match2);
//                }
//            }
//        }
//        writer.setToFile(set, URL);
//    }

//    void words(String baseURL, String pageURL, String category) throws IOException {
//        String fileName = pageURL.substring(pageURL.lastIndexOf("/") + 1);
//        String URL = baseURL + "/Words/" + category + "/" + fileName + ".txt";
//        StringBuilder contents = new StringBuilder();
//        java.net.URL url = new URL("https://en.wikipedia.org" + pageURL);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
//        String line;
//        while ((line = reader.readLine()) != null) {
//            contents.append(line).append("\n");
//        }
//        reader.close();
//        writer.stringToFile(contents.toString(), URL);
//    }

    private void html(String baseURL, String pageURL, String category) throws IOException {
        String fileName = pageURL.substring(pageURL.lastIndexOf("/") + 1);
        String URL = baseURL + "/HTML/" + category + "/" + fileName + ".txt";
        StringBuilder contents = new StringBuilder();
        java.net.URL url = new URL("https://en.wikipedia.org" + pageURL);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            contents.append(line).append("\n");
        }
        reader.close();
        extractLinks(contents.toString(), baseURL, URL, category);
        extractWords(contents.toString(), baseURL, URL, category);
        extractText(contents.toString(), baseURL, URL, category);
        writer.stringToFile(contents.toString(), URL);
    }
}
