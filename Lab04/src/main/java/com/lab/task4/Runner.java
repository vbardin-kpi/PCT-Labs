package com.lab.task4;

import java.util.ArrayList;

public class Runner {
    private final String rootFolderPath = "C:\\Users\\vbardin\\Desktop\\PCT\\Labs\\Lab04\\src\\main\\resources\\test-dir";

    public void run() {
        var keywords = new ArrayList<String>();
        keywords.add("test");

        FilterableNodeProcessor fileProcessor = new FilterableNodeProcessor();
        fileProcessor.createDescriptors(rootFolderPath);
        fileProcessor.printFilteredDescriptors(keywords);
    }
}
