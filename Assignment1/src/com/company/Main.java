package com.company;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

public class Main {
    private static String baseURL;

    public static void main(String[] args) throws IOException {
        baseURL = "/home/loke/Code/School/2DV515/Assignment1/src/data";
        initScrape();
    }
    private static void initScrape() throws IOException {
        Builder builder = new Builder();
        IOController IO = new IOController();
        Scraper Scrape = new Scraper();
        builder.main();

        Scrape.words(baseURL, "/wiki/Programming_language", "Programming");

        List<Path> linkList = IO.getURL(baseURL, "/Words");

        for (Path link : linkList) {
            Scrape.links(baseURL, link.toString(), "Programming");
        }

        List<String> newLinks = IO.getListOfURLs(baseURL + "/Links/Programming/Programming_language.txt");

        int index = 1;
        for (String link : newLinks) {
            Scrape.words(baseURL, link, "Programming");
            System.out.println("Scraping " + (index++) + " of " + newLinks.size());
        }
    }
}
