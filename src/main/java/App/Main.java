package App;

import java.io.IOException;

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
            parameterFileParser.LoadContent(fileName);
            commandLineInterface.printParameters(parameterFileParser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
