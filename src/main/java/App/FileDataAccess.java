package App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileDataAccess {
    public String readFile(String filePath) throws IOException {
        File file = new File(filePath);

        if (!file.exists())
            throw new FileNotFoundException();

        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    public List<String> readFileLines(String filePath) throws IOException {
        File file = new File(filePath);

        if (!file.exists())
            throw new FileNotFoundException();

        return Files.readAllLines(Paths.get(filePath));
    }

    public Dictionary<String, String> parseDocsFile(String filePath) throws IOException {
        return parseFilesToDictionary(filePath, this::getDocId);
    }

    public Dictionary<String, String> parseQueriesFile(String filePath) throws IOException {
        return parseFilesToDictionary(filePath, this::getQueryId);
    }

    public Dictionary<String, String> parseFilesToDictionary(String filePath, Function<String, String> keyParser) throws IOException {
        Dictionary<String, String> result = new Hashtable<>();
        List<String> lines = readFileLines(filePath);

        String currentFileTitle = "";
        String currentFileContent = "";
        for (String line : lines) {
            if (line.startsWith("*")){
                if (!currentFileContent.equals("") && !currentFileTitle.equals("")){
                    result.put(currentFileTitle, currentFileContent.replaceAll("[\r\n]+", " "));
                }
                currentFileTitle = keyParser.apply(line);
                currentFileContent = "";
            }
            else {
                currentFileContent += line + "\n";
            }
        }

        result.put(currentFileTitle, currentFileContent.replaceAll("[\r\n]+", " "));
        return result;

    }

    private String getDocId(String line){
        Pattern pattern = Pattern.compile("\\*TEXT (?<Id>\\d*) .*");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            return matcher.group("Id");
        }

        return "";
    }

    private String getQueryId(String line){
        Pattern pattern = Pattern.compile("\\*FIND[ \\t]+(?<Id>\\d*)");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            return matcher.group("Id");
        }

        return "";
    }
}
