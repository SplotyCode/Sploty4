package me.david.sploty4.storage;

import java.io.File;

public class StorageHelper {


    public File getSplotyFolder(){
        String os = System.getProperty("os.name").toLowerCase();
        if(os.equals("linux") || os.equals("unix")) return new File(System.getProperty("user.home"), ".Sploty/");
        if(os.contains("mac") || os.contains("osx")) return new File(System.getProperty("user.home"), "Library/Application Support/Sploty/");
        if(os.contains("win")) new File(System.getenv("AppData"));
        System.err.println("Count not detect Data Folder for '" + os + "'! Use /Sploty/ for datafolder");
        return new File("/Sploty/");
    }
}
