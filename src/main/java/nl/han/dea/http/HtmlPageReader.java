package nl.han.dea.http;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HtmlPageReader {
    public String readFile(String filename) {
        var fullFileName = "pages/".concat(filename);
        try {
            ClassLoader classLoader = getClass().getClassLoader();

            var file = new File(classLoader.getResource(fullFileName).getFile()).toPath();

            var fileAsString = new String(Files.readAllBytes(file));

            return fileAsString;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int calcPageContentLength(String filename){
        var page = readFile(filename);
        var length = page.length();
        return length;
    }
}