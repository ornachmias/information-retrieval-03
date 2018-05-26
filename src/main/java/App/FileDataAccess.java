package App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
}
