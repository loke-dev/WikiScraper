package com.company;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

public class Main {
    private static String baseURL;

    public static void main(String[] args) throws IOException {
        baseURL = "./src/data";

        //Build the file structure
        Builder builder = new Builder();
        builder.main();

        //Initialize the scraping for selected category and with starting URL
        initScrape("Programming", "/wiki/Programming_language");
    }
    private static void initScrape(String category, String startURL) throws IOException {
        Writer writer = new Writer();
        Scraper Scrape = new Scraper();

        //Scrape the initial page to get links (Level 0)
        Scrape.words(baseURL, startURL, "Programming");

        //Fetch links from initial page (Level 0)
        List<Path> linkList = writer.getURL(baseURL, "/Words");
        for (Path link : linkList) {
            Scrape.links(baseURL, link.toString(), category);
        }

        //Scrape the pages that came from level 0 scrape (Level 1)
        List<String> newLinks = writer.getListOfURLs(baseURL + "/Links/Programming/Programming_language.txt");
        int index = 1;
        for (String link : newLinks) {
            Scrape.words(baseURL, link, "Programming");
            System.out.println("Scraping " + (index++) + " of " + newLinks.size());
        }
    }
}
