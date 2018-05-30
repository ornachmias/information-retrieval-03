package App;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Dictionary;

public class FileDataAccessTest {
    @Test
    public void parseQueriesFile_MultipleQueries_ReturnDictionary() throws IOException {
        // Arrange
        String path = getFilePathFromResources("TestQueriesFile");
        FileDataAccess fileDataAccess = new FileDataAccess();

        // Act
        Dictionary<String, String> result = fileDataAccess.parseQueriesFile(path);

        // Assert
        Assertions.assertEquals(3, result.size());
        Assertions.assertNotNull(result.get("14"));
        Assertions.assertNotNull(result.get("34"));
        Assertions.assertNotNull(result.get("2"));
    }

    @Test
    public void parseDocsFile_MultipleDocs_ReturnDictionary() throws IOException {
        // Arrange
        String path = getFilePathFromResources("TestDocsFile");
        FileDataAccess fileDataAccess = new FileDataAccess();

        // Act
        Dictionary<String, String> result = fileDataAccess.parseDocsFile(path);

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertNotNull(result.get("11"));
        Assertions.assertNotNull(result.get("146"));
    }

    public String getFileContentFromResources(String relativePath) throws IOException {
        URL url = Thread.currentThread()
                        .getContextClassLoader()
                        .getResource(relativePath);

        File file = new File(url.getPath());
        return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
    }

    public String getFilePathFromResources(String relativePath){
        URL url = Thread.currentThread()
                .getContextClassLoader()
                .getResource(relativePath);

        File file = new File(url.getPath());
        return file.getAbsolutePath();
    }
}
