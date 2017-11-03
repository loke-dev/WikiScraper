package com.company;

import java.io.*;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

public class Main {
    private static int MAX_COUNTER;
    private static String baseURL;
    private static HashSet<String> visited = new HashSet<>();

    public static void main(String[] args) throws IOException {
        baseURL = "./src/data";
        MAX_COUNTER = 1000;

        //Build the file structure
        Builder builder = new Builder();
        builder.structure("Programming");
        builder.structure("Games");

        //Initialize the scraping for selected category and with starting URL
        initScrape("Programming", "/wiki/Programming_language");
    }
    private static void initScrape(String category, String startURL) throws IOException {
        Writer writer = new Writer();
        Scraper Scrape = new Scraper();

        //Scrape the initial page to get links (Level 0)
        Scrape.html(baseURL, startURL, "Programming");

        //Fetch links from initial page (Level 0)
        List<Path> linkList = writer.getURL(baseURL, "/HTML");
        for (Path link : linkList) {
            Scrape.links(baseURL, link.toString(), category);
        }

        //Scrape the pages that came from level 0 scrape (Level 1)
        List<String> newLinks = writer.getListOfURLs(baseURL + "/Links/Programming/Programming_language.txt");
        for (String link : newLinks) {
            if (!visited.contains(link) && visited.size() < MAX_COUNTER) {
                Scrape.html(baseURL, link, "Programming");
                System.out.println("Scraping page " + visited.size() + " of " + MAX_COUNTER + " (Level 1)");
                visited.add(link);
            }
        }

        //Scrape the pages that came from level 1 scrape (Level 2)
        List<Path> last = writer.getURL(baseURL, "/HTML");
        for (Path p : last) {
            String URL = p.toString().replace("HTML", "Links");
            List<String> lastLinks = writer.getListOfURLs(URL);
            for (String link : lastLinks) {
                if (!visited.contains(link) && visited.size() < MAX_COUNTER) {
                    Scrape.html(baseURL, link, "Programming");
                    System.out.println("Scraping page " + visited.size() + " of " + MAX_COUNTER  + " (Level 2)");
                    visited.add(link);
                }
            }
        }
    }
}
