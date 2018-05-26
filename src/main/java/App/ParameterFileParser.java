package App;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

public class ParameterFileParser {

    private FileDataAccess _fileDataAccess;
    private String _queryFile;
    private String _docFiles;
    private String _outputFile;
    private RetrievalAlgorithm _retrievalAlgorithm;

    private final String _queryFilePrefix = "queryFile=";
    private final String _docFilesPrefix = "docsFile=";
    private final String _outputFilePrefix = "outputFile=";
    private final String _retrievalAlgorithmPrefix = "retrievalAlgorithm=";

    public ParameterFileParser(FileDataAccess fileDataAccess) {
        _fileDataAccess = fileDataAccess;
    }

    //region Properties' Getters
    public String getQueryFile() {
        return _queryFile;
    }

    public String getDocFiles() {
        return _docFiles;
    }

    public String getOutputFile() {
        return _outputFile;
    }

    public RetrievalAlgorithm getRetrievalAlgorithm() {
        return _retrievalAlgorithm;
    }
    //endregion

    public void LoadContent(String fileName) throws IOException {
        List<String> fileContent = _fileDataAccess.readFileLines(fileName);

        for (String line : fileContent) {
            if (line.startsWith(_queryFilePrefix)){
                _queryFile = line.split(_queryFilePrefix)[1];
            }
            else if (line.startsWith(_docFilesPrefix)){
                _docFiles = line.split(_docFilesPrefix)[1];
            }
            else if (line.startsWith(_outputFilePrefix)){
                _outputFile = line.split(_outputFilePrefix)[1];
            }
            else if (line.startsWith(_retrievalAlgorithmPrefix)){
                String value = line.split(_retrievalAlgorithmPrefix)[1];
                if (value.equals("basic")){
                    _retrievalAlgorithm = RetrievalAlgorithm.Basic;
                } else if (value.equals("improved")){
                    _retrievalAlgorithm = RetrievalAlgorithm.Improved;
                }
                else {
                    _retrievalAlgorithm = RetrievalAlgorithm.Unknown;
                }
            }
        }

        validateParameters();
    }

    private void validateParameters() throws InvalidPropertiesFormatException {
        if (_queryFile == null || _queryFile.equals("")){
            throw new InvalidPropertiesFormatException("QueryFile field was not defined in parameters file.");
        }
        else if (_outputFile == null || _outputFile.equals("")) {
            throw new InvalidPropertiesFormatException("OutputFile field was not defined in parameters file.");
        }
        else if (_docFiles == null || _docFiles.equals("")) {
            throw new InvalidPropertiesFormatException("DocFiles field was not defined in parameters file.");
        }
        else if (_retrievalAlgorithm == RetrievalAlgorithm.Unknown) {
            throw new InvalidPropertiesFormatException("RetrievalAlgorithm field was not defined in parameters file.");
        }
    }
}
