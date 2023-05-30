package com.lab.task3;

public class Runner {
    private final String rootFolderPath = "C:\\Users\\vbardin\\Desktop\\PCT\\Labs\\Lab04\\src\\main\\resources\\test-dir";

    public void run() {
        var fileProcessor = new NodeProcessor();
        fileProcessor.createDescriptors(rootFolderPath);
        fileProcessor.printDescriptors();
    }
}
