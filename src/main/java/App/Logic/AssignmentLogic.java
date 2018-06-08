package App.Logic;

import App.Model.IRetrivalAlgorithm;
import App.Model.*;
import App.Model.Threshold.BasicThreashold;
import App.AlgImpl.RetrivalAlgorithmFactory;
import App.Modules.SearchModule;
import App.FileDataAccess;
import App.Modules.IndexModule;
import App.Modules.TestingModule;
import App.ParameterFileParser;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.store.RAMDirectory;

import java.util.*;

public class AssignmentLogic {

    public AssignmentLogic() {
    }

    protected FileDataAccess _fileDataAccess;
    protected ParameterFileParser _parameterFileParser;

    public AssignmentLogic(FileDataAccess fileDataAccess, ParameterFileParser parameterFileParser) {
        _fileDataAccess = fileDataAccess;
        _parameterFileParser = parameterFileParser;
    }

    public SearchModule IndexDocs(Map<String, String> docs, IRetrivalAlgorithm alg, List<String> stopWords) throws Exception {
        RAMDirectory index = new RAMDirectory();
        IndexModule indexModule = new IndexModule(index, CharArraySet.copy(new HashSet<>(stopWords)), alg);
        indexModule.indexDocs(docs);
        SearchModule searchModule = new SearchModule(index, alg);
        return searchModule;
    }

    public List<String> GetStopWards(Map<String, String> docs) throws Exception {
        BasicThreashold th = new BasicThreashold(0);
        IRetrivalAlgorithm alg = RetrivalAlgorithmFactory.GetAlg(RetrievalAlgorithmType.Basic);
        alg.setThreshold(th);
        // We first index the documents without taking stop words into account
        // so we could complete the assignment with stop words based on the do
        SearchModule searchModule = IndexDocs(docs, alg, new ArrayList<>());
        List<String> stopWords = searchModule.getTopWords(20);
        return stopWords;
    }

    public void MeasureResults(Map<String, List<String>> results) throws Exception {
        String truthFilePath = _parameterFileParser.getTruthFile();
        if (truthFilePath != null) {
            Map truthContent = _fileDataAccess.parseTruthFile(truthFilePath);
            TestingModule tester = new TestingModule(truthContent, results);
            Measurement measurement = tester.TestQueries();
            System.out.println(measurement.GetAverageF());
        }
    }

    public Map<String, List<String>> GetResults(Map<String, String> docs, Map<String, String> queries, SearchModule searchModule) throws Exception {
        Map<String, List<String>> results = new HashMap<>();
        for (String id : queries.keySet()) {
            String query = queries.get(id);
            // We set the size of the page as the total number of docs to avoid
            // paging over the result. Not optimal, but practically it's ok.
            List<String> queryResults = searchModule.queryDocs(query, docs.size());
            results.put(id, queryResults);
        }

        return results;
    }


    public Map<String, List<String>> run(String parametersFileName) throws Exception {
        // Get all relevant parameters
        _parameterFileParser.LoadContent(parametersFileName);
        RetrievalAlgorithmType alg_type = _parameterFileParser.getRetrievalAlgorithm();
        IRetrivalAlgorithm alg = RetrivalAlgorithmFactory.GetAlg(alg_type);

        // Parse the documents
        Map<String, String> docs = _fileDataAccess.parseDocsFile(_parameterFileParser.getDocFiles());

        List<String> stopWords = GetStopWards(docs);
        SearchModule searchModule = IndexDocs(docs, alg, stopWords);

        // Parse queries
        Map<String, String> queries = _fileDataAccess.parseQueriesFile(_parameterFileParser.getQueryFile());

        Map<String, List<String>> results = GetResults(docs, queries, searchModule);
        _fileDataAccess.writeResults(_parameterFileParser.getOutputFile(), results);
        MeasureResults(results);
        return results;
    }
}

