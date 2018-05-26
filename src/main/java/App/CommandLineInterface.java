package App;

import java.io.File;

public class CommandLineInterface {
    public String getFile(String[] args){
        if (args == null || args.length == 0)
            return null;

        if (!new File(args[0]).exists())
            return null;

        return args[0];
    }

    public void printParameters(ParameterFileParser parameterFileParser){
        System.out.println("Input parameters:");
        System.out.println("DocFiles=" + parameterFileParser.getDocFiles());
        System.out.println("OutputFile=" + parameterFileParser.getOutputFile());
        System.out.println("QueryFile=" + parameterFileParser.getQueryFile());
        System.out.println("RetrievalAlgorithm=" + parameterFileParser.getRetrievalAlgorithm());
    }

    public void printHelp(){
        System.out.println("No input file was detected.\n" +
                "It is possible that no arguments were inserted " +
                "or the file does not exists in the current directory.\n" +
                "USAGE: java.exe -jar <JAR-FILE> <PARAMETERS-FILE>");
    }
}
