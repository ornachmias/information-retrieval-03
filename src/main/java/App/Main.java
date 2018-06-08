package App;

import App.Logic.AssignmentLogic;
import App.Logic.ConstantThresholdsCheck;
import App.Logic.DynamicThresholdsCheck;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args){
        LogHandler.info("Starting main..");
        CommandLineInterface commandLineInterface = new CommandLineInterface();
        String fileName = commandLineInterface.getFile(args);
        LogHandler.info("Input file=" + fileName);

        if (fileName == null){
            LogHandler.warning("No file detected, printing help to user.");
            commandLineInterface.printHelp();
            return;
        }

        FileDataAccess fileDataAccess = new FileDataAccess();
        ParameterFileParser parameterFileParser = new ParameterFileParser(fileDataAccess);

        try {
            AssignmentLogic assignmentLogic = new AssignmentLogic(fileDataAccess, parameterFileParser);
            LogHandler.info("Running assignment logic..");
            Map<String, List<String>> result = assignmentLogic.run(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}