package App;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

public class FileDataAccessTest {
    @Test
    public void parseQueriesFile_MultipleQueries_ReturnDictionary() throws IOException {
        // Arrange
        String path = TestHelper.getFilePathFromResources("TestQueriesFile");
        FileDataAccess fileDataAccess = new FileDataAccess();

        // Act
        Map<String, String> result = fileDataAccess.parseQueriesFile(path);

        // Assert
        Assertions.assertEquals(3, result.size());
        Assertions.assertNotNull(result.get("14"));
        Assertions.assertNotNull(result.get("34"));
        Assertions.assertNotNull(result.get("2"));
    }

    @Test
    public void parseDocsFile_MultipleDocs_ReturnDictionary() throws IOException {
        // Arrange
        String path = TestHelper.getFilePathFromResources("TestDocsFile");
        FileDataAccess fileDataAccess = new FileDataAccess();

        // Act
        Map<String, String> result = fileDataAccess.parseDocsFile(path);

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertNotNull(result.get("11"));
        Assertions.assertNotNull(result.get("146"));
    }
}
