package App;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestHelper {
    public static String getFileContentFromResources(String relativePath) throws IOException {
        URL url = Thread.currentThread()
                .getContextClassLoader()
                .getResource(relativePath);

        File file = new File(url.getPath());
        return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
    }

    public static String getFilePathFromResources(String relativePath){
        URL url = Thread.currentThread()
                .getContextClassLoader()
                .getResource(relativePath);

        File file = new File(url.getPath());
        return file.getAbsolutePath();
    }
}
