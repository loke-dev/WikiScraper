package com.company;

import java.io.*;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        //Build the file structure
        Builder builder = new Builder();
        builder.structure("Programming");
        builder.structure("Games");

        //Init the scraper
        Scraper prog = new Scraper();
        Scraper game = new Scraper();
        prog.main("Programming", "/wiki/Programming_language");
        game.main("Games", "/wiki/Video_game");
    }
}
