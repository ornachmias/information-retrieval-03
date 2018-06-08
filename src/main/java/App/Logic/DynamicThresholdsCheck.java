package App.Logic;

import App.FileDataAccess;
import App.LogHandler;
import App.Model.*;
import App.ParameterFileParser;
import App.RetAlgImpl;
import App.Modules.SearchModule;

import java.util.*;

public class DynamicThresholdsCheck extends AssignmentLogic {

    public DynamicThresholdsCheck(FileDataAccess fileDataAccess, ParameterFileParser parameterFileParser) {
        super(fileDataAccess,parameterFileParser);
    }

    public Map<String, List<String>> run(String parametersFileName) throws Exception {
        // Get all relevant parameters
        _parameterFileParser.LoadContent(parametersFileName);
        RetrievalAlgorithm alg_type = _parameterFileParser.getRetrievalAlgorithm();
        RetrivalAlgInterface alg = RetAlgImpl.GetAlg(alg_type);

        // Parse the documents
        Map<String, String> docs = _fileDataAccess.parseDocsFile(_parameterFileParser.getDocFiles());

        List<String> stopWords = GetStopWards(docs);
        SearchModule searchModule = IndexDocs(docs, alg, stopWords);

        // Parse queries
        Map<String, String> queries = _fileDataAccess.parseQueriesFile(_parameterFileParser.getQueryFile());

        double threshold = 1.0;
        Map<String, List<String>> results = new HashMap<>();
        while (threshold < 2){
            alg.SetFilter(new DynamicThreashold(threshold));
            results = GetResults(docs, queries, searchModule);
            _fileDataAccess.writeResults(_parameterFileParser.getOutputFile(), results);
            threshold += 0.005;
        }

        return results;
    }
}