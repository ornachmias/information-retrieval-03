package App;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.omg.Dynamic.Parameter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class ParameterFileParserTest {

    @Test
    public void LoadContent_ValidContent_PropertiesInitialized() throws IOException {
        // Arrange
        List<String> validFileContent = new ArrayList<>();
        validFileContent.add("queryFile=data/queries.txt");
        validFileContent.add("docsFile=data/docs.txt");
        validFileContent.add("outputFile=out/output.txt");
        validFileContent.add("retrievalAlgorithm=basic");

        FileDataAccess fileDataAccess = Mockito.mock(FileDataAccess.class);
        when(fileDataAccess.readFileLines(anyString())).thenReturn(validFileContent);
        ParameterFileParser parameterFileParser = new ParameterFileParser(fileDataAccess);

        // Act
        parameterFileParser.LoadContent("parameters.txt");

        // Assert
        Assertions.assertEquals("data/queries.txt", parameterFileParser.getQueryFile());
        Assertions.assertEquals("data/docs.txt", parameterFileParser.getDocFiles());
        Assertions.assertEquals("out/output.txt", parameterFileParser.getOutputFile());
        Assertions.assertEquals(RetrievalAlgorithm.Basic, parameterFileParser.getRetrievalAlgorithm());
    }
}
