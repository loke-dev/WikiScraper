package com.company;

import java.io.File;

class Builder {
    Builder() {
        createRootFolder();
        createSubFolders("HTML");
        createSubFolders("Links");
        createSubFolders("Words");
        createSubFolders("Text");
    }

    void structure(String category) {
        createCategory("Links", category);
        createCategory("HTML", category);
        createCategory("Words", category);
        createCategory("Text", category);
    }

    private void createRootFolder() {
        File file = new File("./src/data");
        if (!file.exists()) {
            if (!file.mkdir()) {
                System.out.println("directory is not created");
            }
        }
    }

    private void createSubFolders(String folderName) {
        File File = new File("./src/data/" + folderName);
        if (!File.exists()) {
            if (!File.mkdirs()) {
                System.out.println("failed to create sub directories");
            }
        }
    }

    private void createCategory(String folderName, String category) {
        File File = new File("./src/data/" + folderName + "/" + category);
        if (!File.exists()) {
            if (!File.mkdirs()) {
                System.out.println("failed to create category");
            }
        }
    }
}
