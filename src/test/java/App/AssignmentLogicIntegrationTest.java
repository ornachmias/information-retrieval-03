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

        when(parameterFileParser.getOutputFile()).thenReturn("out/output.txt");

        when(parameterFileParser.getRetrievalAlgorithm()).thenReturn(RetrievalAlgorithmType.Basic);

        AssignmentLogic assignmentLogic = new AssignmentLogic(fileDataAccess, parameterFileParser);

        // Act
        Map<String, List<String>> result = assignmentLogic.run("dummy", null);

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

    @Test
    public void run_MultipleDocs_RunBatchQueries_BasicAlgorithm_WriteLogWithDifferentScores() throws Exception {
        // Arrange
        FileDataAccess fileDataAccess = new FileDataAccess();
        ParameterFileParser parameterFileParser = Mockito.mock((ParameterFileParser.class));
        when(parameterFileParser.getDocFiles())
                .thenReturn(TestHelper.getFilePathFromResources("TestSet02/data/docs.txt"));

        when(parameterFileParser.getQueryFile())
                .thenReturn(TestHelper.getFilePathFromResources("TestSet02/data/queries.txt"));

        when(parameterFileParser.getTruthFile())
                .thenReturn(TestHelper.getFilePathFromResources("TestSet02/truth.txt"));

        when(parameterFileParser.getOutputFile()).thenReturn("out/output.txt");

        when(parameterFileParser.getRetrievalAlgorithm()).thenReturn(RetrievalAlgorithmType.Basic);

        AssignmentLogic assignmentLogic = new AssignmentLogic(fileDataAccess, parameterFileParser);

        // Act
        Map<String, List<String>> result1 = assignmentLogic.run("dummy", 20);
        Map<String, List<String>> result2 = assignmentLogic.run("dummy", 20);
        Map<String, List<String>> result3 = assignmentLogic.run("dummy", 20);
        Map<String, List<String>> result4 = assignmentLogic.run("dummy", 20);
        Map<String, List<String>> result5 = assignmentLogic.run("dummy", 20);
    }

    @Test
    public void run_MultipleDocs_RunAllQueries_BasicAlgorithm_WriteOutputFile() throws Exception {
        // Arrange
        FileDataAccess fileDataAccess = new FileDataAccess();
        ParameterFileParser parameterFileParser = Mockito.mock((ParameterFileParser.class));
        when(parameterFileParser.getDocFiles())
                .thenReturn(TestHelper.getFilePathFromResources("TestSet02/data/docs.txt"));

        when(parameterFileParser.getQueryFile())
                .thenReturn(TestHelper.getFilePathFromResources("TestSet02/data/queries.txt"));

        when(parameterFileParser.getTruthFile())
                .thenReturn(TestHelper.getFilePathFromResources("TestSet02/truth.txt"));

        when(parameterFileParser.getOutputFile()).thenReturn("out/output.txt");

        when(parameterFileParser.getRetrievalAlgorithm()).thenReturn(RetrievalAlgorithmType.Basic);

        AssignmentLogic assignmentLogic = new AssignmentLogic(fileDataAccess, parameterFileParser);

        // Act
        Map<String, List<String>> result1 = assignmentLogic.run("dummy", null);
    }

    @Test
    public void run_MultipleDocs_RunBatchQueries_ImprovedAlgorithm_WriteOutputFile() throws Exception {
        // Arrange
        FileDataAccess fileDataAccess = new FileDataAccess();
        ParameterFileParser parameterFileParser = Mockito.mock((ParameterFileParser.class));
        when(parameterFileParser.getDocFiles())
                .thenReturn(TestHelper.getFilePathFromResources("TestSet02/data/docs.txt"));

        when(parameterFileParser.getQueryFile())
                .thenReturn(TestHelper.getFilePathFromResources("TestSet02/data/queries.txt"));

        when(parameterFileParser.getTruthFile())
                .thenReturn(TestHelper.getFilePathFromResources("TestSet02/truth.txt"));

        when(parameterFileParser.getOutputFile()).thenReturn("out/output.txt");

        when(parameterFileParser.getRetrievalAlgorithm()).thenReturn(RetrievalAlgorithmType.Improved);

        AssignmentLogic assignmentLogic = new AssignmentLogic(fileDataAccess, parameterFileParser);

        // Act
        Map<String, List<String>> result1 = assignmentLogic.run("dummy", 20);
        Map<String, List<String>> result2 = assignmentLogic.run("dummy", 20);
        Map<String, List<String>> result3 = assignmentLogic.run("dummy", 20);
        Map<String, List<String>> result4 = assignmentLogic.run("dummy", 20);
        Map<String, List<String>> result5 = assignmentLogic.run("dummy", 20);
    }
}
