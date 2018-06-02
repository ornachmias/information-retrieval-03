package App;

import App.Logic.AssignmentLogic;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args){
        CommandLineInterface commandLineInterface = new CommandLineInterface();
        String fileName = commandLineInterface.getFile(args);

        if (fileName == null){
            commandLineInterface.printHelp();
            return;
        }

        FileDataAccess fileDataAccess = new FileDataAccess();
        ParameterFileParser parameterFileParser = new ParameterFileParser(fileDataAccess);

        try {
            AssignmentLogic assignmentLogic = new AssignmentLogic(fileDataAccess, parameterFileParser);
            Map<String, List<String>> result = assignmentLogic.run(fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}