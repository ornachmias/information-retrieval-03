package App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
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

    public Map<String, String> parseDocsFile(String filePath) throws IOException {
        return parseFilesToMap(filePath, this::getDocId);
    }

    public Map<String, String> parseQueriesFile(String filePath) throws IOException {
        return parseFilesToMap(filePath, this::getQueryId);
    }

    public Map<String, List<String>> parseTruthFile(String filePath) throws IOException {
        Map<String, List<String>> result = new Hashtable<>();
        Pattern p = Pattern.compile("-?\\d+");
        List<String> lines = readFileLines(filePath);
        for (String line : lines) {
            Matcher m = p.matcher(line);
            if (!m.find()) {
                continue;
            }
            String query_id = m.group();
            List <String> docs = new LinkedList<>();
            result.put(query_id, docs);
            while (m.find()) {
                docs.add(m.group());
            }
        }
        return result;
    }

    public void writeResults(String filePath, Map<String, List<String>> results) throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        if (file.exists())
            file.delete();

        file.createNewFile();

        FileWriter writer = new FileWriter(file.getAbsolutePath());
        String output = "";
        for (String id : results.keySet()) {
            List<Integer> intList = new ArrayList<>();

            for(String s : results.get(id)) intList.add(Integer.valueOf(s));
            Collections.sort(intList);

            List<String> orderedStringList = new ArrayList<>(intList.size());
            for (Integer i : intList) {
                orderedStringList.add(i.toString());
            }

            output += id + " " + String.join(" ", orderedStringList) + "\n";
        }

        writer.write(output);
        writer.flush();
    }

    private Map<String, String> parseFilesToMap(String filePath, Function<String, String> keyParser) throws IOException {
        Map<String, String> result = new Hashtable<>();
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
