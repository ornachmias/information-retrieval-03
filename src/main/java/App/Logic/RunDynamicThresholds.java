package App.Logic;

import App.Model.*;
import App.RetAlgImpl;
import App.Modules.SearchModule;

import java.util.*;

class RunDynamicThresholds extends AssignmentLogic {

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

        int threshold = 10;
        Map<String, List<String>> results = new HashMap<>();
        while (threshold < 200){
            alg.SetFilter(new DynamicThreashold(threshold));
            results = GetResults(docs, queries, searchModule);
            _fileDataAccess.writeResults(_parameterFileParser.getOutputFile(), results);
            MeasureResults(results);
            threshold += 1;
            System.out.println(threshold);
        }

        return results;
    }
}