package App.Logic;

import App.*;
import App.AlgImpl.RetrivalAlgorithmFactory;
import App.Model.*;
import App.Model.Threshold.BasicThreshold;
import App.Modules.SearchModule;
import App.Modules.IndexModule;
import App.Modules.TestingModule;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.store.RAMDirectory;
import java.io.FileOutputStream;
import java.io.File;
import java.util.*;
import java.io.PrintWriter;

public class AssignmentLogic {

    public AssignmentLogic() {
    }

    protected FileDataAccess _fileDataAccess;
    protected ParameterFileParser _parameterFileParser;
    PrintWriter _csvPrinter = null;

    public AssignmentLogic(FileDataAccess fileDataAccess, ParameterFileParser parameterFileParser) {
        _fileDataAccess = fileDataAccess;
        _parameterFileParser = parameterFileParser;
    }

    public SearchModule indexDocs(Map<String, String> docs, IRetrivalAlgorithm alg, List<String> stopWords) throws Exception {
        RAMDirectory index = new RAMDirectory();
        IndexModule indexModule = new IndexModule(index, CharArraySet.copy(new HashSet<>(stopWords)), alg);
        indexModule.indexDocs(docs);
        SearchModule searchModule = new SearchModule(index, alg);
        return searchModule;
    }

    public List<String> getStopWords(Map<String, String> docs) throws Exception {
        BasicThreshold th = new BasicThreshold(0);
        IRetrivalAlgorithm alg = RetrivalAlgorithmFactory.GetAlg(RetrievalAlgorithmType.Basic);
        alg.setThreshold(th);
        // We first index the documents without taking stop words into account
        // so we could complete the assignment with stop words based on the do
        SearchModule searchModule = indexDocs(docs, alg, new ArrayList<>());
        List<String> stopWords = searchModule.getTopWords(20);
        return stopWords;
    }

    public void measureResults(Map<String, List<String>> results) throws Exception {
    private void printToCSV(String line) throws Exception{
        if (_csvPrinter == null) {
            File f = new File("out/results.csv");
            if (!f.exists()) {
                f.createNewFile();
                FileOutputStream fop = new FileOutputStream(f, true);
                PrintWriter out = new PrintWriter(fop);
                out.println("Algorithm, ThresholdName, ThresholdValue, AverageF, AveragePrecision, AverageRecall");
                 _csvPrinter = out;
            }
            else{
                FileOutputStream fop = new FileOutputStream(f, true);
                _csvPrinter = new PrintWriter(fop);
            }
        }

        _csvPrinter.println(line);
        _csvPrinter.flush();
    }

    public void writeMeasurements(Measurement measurement, IRetrivalAlgorithm alg) throws Exception{
        String line = String.format("%1$s, %2$s, %3$s, %4$f, %5$f, %6$f",
                alg.getName(),
                alg.getThreshold().getName(),
                alg.getThreshold().getValue(),
                measurement.GetAverageF(),
                measurement.GetAveragePrecision(),
                measurement.GetAverageRecall()
        );
        printToCSV(line);
    }

    public void MeasureResults(Map<String, List<String>> results, IRetrivalAlgorithm alg) throws Exception {
        String truthFilePath = _parameterFileParser.getTruthFile();
        if (truthFilePath != null) {
            Map truthContent = _fileDataAccess.parseTruthFile(truthFilePath);
            TestingModule tester = new TestingModule(truthContent, results);
            Measurement measurement = tester.TestQueries();
            LogHandler.info(String.format("Algorithm=%1$s, ThresholdName=%2$s, ThresholdValue=%3$s, AverageF=%4$f, AveragePrecision=%5$f, AverageRecall=%6$f",
                    alg.getName(),
                    alg.getThreshold().getName(),
                    alg.getThreshold().getValue(),
                    measurement.GetAverageF(),
                    measurement.GetAveragePrecision(),
                    measurement.GetAverageRecall()));
            writeMeasurements(measurement, alg);

        }
    }

    public Map<String, List<String>> getResults(Map<String, String> docs, Map<String, String> queries, SearchModule searchModule) throws Exception {
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

        List<String> stopWords = getStopWords(docs);
        SearchModule searchModule = indexDocs(docs, alg, stopWords);

        // Parse queries
        Map<String, String> queries = _fileDataAccess.parseQueriesFile(_parameterFileParser.getQueryFile());

        Map<String, List<String>> results = getResults(docs, queries, searchModule);
        _fileDataAccess.writeResults(_parameterFileParser.getOutputFile(), results);
        MeasureResults(results, alg);
        measureResults(results);
        return results;
    }
}

