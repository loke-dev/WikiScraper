package com.company;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

public class Main {
    private static String baseURL;

    public static void main(String[] args) throws IOException {
        baseURL = "/home/loke/Code/School/2DV515/Assignment1/src/data";
        initScrape("Programming", "/wiki/Programming_language");
    }
    private static void initScrape(String category, String startURL) throws IOException {
        Builder builder = new Builder();
        Writer writer = new Writer();
        Scraper Scrape = new Scraper();

        //Build the file structure
        builder.main();

        //Scrape the initial page to get links (Level 0)
        Scrape.words(baseURL, startURL, "Programming");


        List<Path> linkList = writer.getURL(baseURL, "/Words");

        for (Path link : linkList) {
            Scrape.links(baseURL, link.toString(), category);
        }

        List<String> newLinks = writer.getListOfURLs(baseURL + "/Links/Programming/Programming_language.txt");

        int index = 1;
        for (String link : newLinks) {
            Scrape.words(baseURL, link, "Programming");
            System.out.println("Scraping " + (index++) + " of " + newLinks.size());
        }
    }
}
