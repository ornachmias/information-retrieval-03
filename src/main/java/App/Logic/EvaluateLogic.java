package App.Logic;

import App.LogHandler;
import App.CommandLineInterface;
import App.FileDataAccess;
import App.ParameterFileParser;
import App.Model.RetrievalAlgorithmType;


public class EvaluateLogic {
    public static void main(String[] args) {
        LogHandler.info("Starting main..");
        CommandLineInterface commandLineInterface = new CommandLineInterface();
        String fileName = commandLineInterface.getFile(args);
        LogHandler.info("Input file=" + fileName);

        if (fileName == null) {
            LogHandler.warning("No file detected, printing help to user.");
            commandLineInterface.printHelp();
            return;
        }

        FileDataAccess fileDataAccess = new FileDataAccess();
        ParameterFileParser parameterFileParser = new ParameterFileParser(fileDataAccess);

        try {
            new ConstantThresholdsCheck(fileDataAccess, parameterFileParser, RetrievalAlgorithmType.Basic, 0, 1,0.01).run(fileName);
            new DynamicThresholdsCheck(fileDataAccess, parameterFileParser, RetrievalAlgorithmType.Basic, 1, 2).run(fileName);
            new ConstantThresholdsCheck(fileDataAccess, parameterFileParser, RetrievalAlgorithmType.Improved, 0, 18,0.5).run(fileName);
            new DynamicThresholdsCheck(fileDataAccess, parameterFileParser, RetrievalAlgorithmType.Improved, 1, 2).run(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}