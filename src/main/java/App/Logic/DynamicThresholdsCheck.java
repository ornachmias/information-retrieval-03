package App.Logic;

import App.AlgImpl.RetrivalAlgorithmFactory;
import App.FileDataAccess;
import App.Model.*;
import App.Model.Threshold.DynamicThreshold;
import App.ParameterFileParser;
import App.Modules.SearchModule;

import java.util.*;

public class DynamicThresholdsCheck extends AssignmentLogic {
    double _start_window = 0.1;
    double _end_window = 2;
    RetrievalAlgorithmType _alg_type = RetrievalAlgorithmType.Unknown;
    public DynamicThresholdsCheck(FileDataAccess fileDataAccess, ParameterFileParser parameterFileParser) {
        super(fileDataAccess,parameterFileParser);
    }

    public DynamicThresholdsCheck(FileDataAccess fileDataAccess,
                                   ParameterFileParser parameterFileParser,
                                   RetrievalAlgorithmType alg_type,
                                   double start_window,
                                   double end_window) {

        super(fileDataAccess,parameterFileParser);
        _alg_type = alg_type;
        _start_window = start_window;
        _end_window = end_window;
    }

    /**
     * Run the assignment logic with different threhsold parameters
     * @param parametersFileName Input parameter file defined by the user
     * @return Map of queries ids to found documents
     * @throws Exception
     */
    public Map<String, List<String>> run(String parametersFileName) throws Exception {
        // Get all relevant parameters
        _parameterFileParser.LoadContent(parametersFileName);


        if (_alg_type == RetrievalAlgorithmType.Unknown) {
            _alg_type = _parameterFileParser.getRetrievalAlgorithm();
        }

        IRetrievalAlgorithm alg = RetrivalAlgorithmFactory.GetAlg(_alg_type);

        // Parse the documents
        Map<String, String> docs = _fileDataAccess.parseDocsFile(_parameterFileParser.getDocFiles());

        List<String> stopWords = getStopWords(docs);
        SearchModule searchModule = indexDocs(docs, alg, stopWords);

        // Parse queries
        Map<String, String> queries = _fileDataAccess.parseQueriesFile(_parameterFileParser.getQueryFile());

        double threshold = _start_window;
        Map<String, List<String>> results = new HashMap<>();
        while (threshold < _end_window){
            alg.setThreshold(new DynamicThreshold(threshold));
            results = getResults(docs, queries, searchModule);
            _fileDataAccess.writeResults(_parameterFileParser.getOutputFile(), results);
            measureResults(results, alg);
            threshold += 0.005;
        }

        return results;
    }
}