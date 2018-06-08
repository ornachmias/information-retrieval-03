package App;

import App.Logic.RunConstantThresholds;
import App.Logic.RunDynamicThresholds;
import App.Model.RetrievalAlgorithm;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class RunDynamicThresholdsTests {
    @Test
    public void run_changeDynamicThreshold_getAllResults() throws Exception {
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

        RunDynamicThresholds runDynamicThresholds = new RunDynamicThresholds(fileDataAccess, parameterFileParser);

        // Act
        runDynamicThresholds.run("dummy");
    }
}
