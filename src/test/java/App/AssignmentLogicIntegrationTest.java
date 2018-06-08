package App;

import App.Logic.AssignmentLogic;
import App.Model.RetrievalAlgorithmType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

public class AssignmentLogicIntegrationTest {
    @Test
    public void run_MultipleDocs_SingleQuery_BasicAlgorithm_ReturnResults() throws Exception {
        // Arrange
        FileDataAccess fileDataAccess = new FileDataAccess();
        ParameterFileParser parameterFileParser = Mockito.mock((ParameterFileParser.class));
        when(parameterFileParser.getDocFiles())
                .thenReturn(TestHelper.getFilePathFromResources("TestSet01/data/docs.txt"));

        when(parameterFileParser.getQueryFile())
                .thenReturn(TestHelper.getFilePathFromResources("TestSet01/data/queries.txt"));

        when(parameterFileParser.getQueryFile())
                .thenReturn(TestHelper.getFilePathFromResources("TestSet01/data/queries.txt"));

        when(parameterFileParser.getOutputFile()).thenReturn("out/output.txt");

        when(parameterFileParser.getRetrievalAlgorithm()).thenReturn(RetrievalAlgorithmType.Basic);

        AssignmentLogic assignmentLogic = new AssignmentLogic(fileDataAccess, parameterFileParser);

        // Act
        Map<String, List<String>> result = assignmentLogic.run("dummy");

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertNotNull(result.get("42"));
        Assertions.assertNotNull(result.get("43"));
        Assertions.assertEquals(2, result.get("42").size());
        Assertions.assertEquals(2, result.get("43").size());
        Assertions.assertTrue(result.get("42").contains("47"));
        Assertions.assertTrue(result.get("42").contains("48"));
        Assertions.assertTrue(result.get("43").contains("329"));
        Assertions.assertTrue(result.get("43").contains("341"));
    }
}
