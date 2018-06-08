package App;

import App.Logic.ConstantThresholdsCheck;
import App.Model.RetrievalAlgorithm;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class RunConstantThresholdsTest {
    @Test
    public void run_tryMultipleThreshold_getAllResults() throws Exception {
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

        when(parameterFileParser.getRetrievalAlgorithm()).thenReturn(RetrievalAlgorithm.Basic);

        ConstantThresholdsCheck runConstantThresholds = new ConstantThresholdsCheck(fileDataAccess, parameterFileParser);

        // Act
        runConstantThresholds.run("dummy");
    }
}
