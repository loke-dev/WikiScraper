package com.company;

import java.io.File;

class Builder {
    void main() {
        createRootFolder();
        createSubFolders("Words");
        createSubFolders("Links");
        createCategory("Links", "Programming");
        createCategory("Links", "Games");
        createCategory("Words", "Programming");
        createCategory("Words", "Games");
    }

    private void createRootFolder() {
        File file = new File("/home/loke/Code/School/2DV515/Assignment1/src/data");
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("directory is not created");
            }
        }
    }

    private void createSubFolders(String folderName) {
        File File = new File("/home/loke/Code/School/2DV515/Assignment1/src/data/" + folderName);
        if (!File.exists()) {
            if (!File.mkdirs()) {
                System.out.println("failed to create sub directories");
            }
        }
    }

    private void createCategory(String folderName, String category) {
        File File = new File("/home/loke/Code/School/2DV515/Assignment1/src/data/" + folderName + "/" + category);
        if (!File.exists()) {
            if (File.mkdirs()) {
                System.out.println("failed to create category");
            }
        }
    }
}
