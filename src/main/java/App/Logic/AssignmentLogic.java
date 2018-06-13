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
    protected FileDataAccess _fileDataAccess;
    protected ParameterFileParser _parameterFileParser;
    private PrintWriter _csvPrinter = null;

    public AssignmentLogic(FileDataAccess fileDataAccess, ParameterFileParser parameterFileParser) {
        _fileDataAccess = fileDataAccess;
        _parameterFileParser = parameterFileParser;
    }

    /**
     * Run the logic of the assignment
     * @param parametersFileName Parameter file path based on user's input
     * @param batchSize Size of random batch to run from the queries, null if all the queries
     * @return Map of queries ids to found documents
     * @throws Exception
     */
    public Map<String, List<String>> run(String parametersFileName, Integer batchSize) throws Exception {
        // Get all relevant parameters
        _parameterFileParser.LoadContent(parametersFileName);
        RetrievalAlgorithmType alg_type = _parameterFileParser.getRetrievalAlgorithm();
        IRetrievalAlgorithm alg = RetrivalAlgorithmFactory.GetAlg(alg_type);

        // Parse the documents
        Map<String, String> docs = _fileDataAccess.parseDocsFile(_parameterFileParser.getDocFiles());

        List<String> stopWords = getStopWords(docs);
        SearchModule searchModule = indexDocs(docs, alg, stopWords);

        // Parse queries
        Map<String, String> queries = _fileDataAccess.parseQueriesFile(_parameterFileParser.getQueryFile());

        // If we decided to use batches - replace the queries with
        // randomly selected queries from the set
        if (batchSize != null){
            List<String> keys = new ArrayList<>(queries.keySet());
            while (keys.size() > batchSize){
                keys.remove(randomWithRange(0, keys.size() - 1));
            }

            Map<String, String> filteredQueries = new HashMap<>();
            Map<String, String> finalQueries = queries;
            keys.forEach(x-> filteredQueries.put(x, finalQueries.get(x)));
            queries = filteredQueries;
        }

        // Run the queries
        Map<String, List<String>> results = getResults(docs, queries, searchModule);

        // Write results
        _fileDataAccess.writeResults(_parameterFileParser.getOutputFile(), results);

        // Measure results based on ground truth and write measurements
        measureResults(results, alg);

        return results;
    }

    /**
     * Index the documents
     * @param docs Map of the document id to the document content
     * @param alg Algorithm we choose the index the documents by
     * @param stopWords Set of stopwords to include while indexing
     * @return Search module that match the parameters for this index
     * @throws Exception
     */
    public SearchModule indexDocs(Map<String, String> docs, IRetrievalAlgorithm alg, List<String> stopWords) throws Exception {
        RAMDirectory index = new RAMDirectory();
        IndexModule indexModule = new IndexModule(index, CharArraySet.copy(new HashSet<>(stopWords)), alg);
        indexModule.indexDocs(docs);
        SearchModule searchModule = new SearchModule(index, alg);
        return searchModule;
    }

    /**
     * Index all of the data without any predefined stopwords and query the top 20 words used in the documents
     * @param docs Map of the document id to the document content
     * @return List of the top 20 words used in the documents
     * @throws Exception
     */
    public List<String> getStopWords(Map<String, String> docs) throws Exception {
        BasicThreshold th = new BasicThreshold(0);
        IRetrievalAlgorithm alg = RetrivalAlgorithmFactory.GetAlg(RetrievalAlgorithmType.Basic);
        alg.setThreshold(th);
        // We first index the documents without taking stop words into account
        // so we could complete the assignment with stop words based on the do
        SearchModule searchModule = indexDocs(docs, alg, new ArrayList<>());
        List<String> stopWords = searchModule.getTopWords(20);
        return stopWords;
    }

    /**
     * Write all of the measurements to a CSV file
     * @param measurement Measurement for the result
     * @param alg Used algorithm
     * @throws Exception
     */
    public void writeMeasurements(Measurement measurement, IRetrievalAlgorithm alg) throws Exception{
        String line = String.format("%1$s, %2$s, %3$s, %4$f, %5$f, %6$f, %7$f, %8$f, %9$f",
                alg.getName(),
                alg.getThreshold().getName(),
                alg.getThreshold().getValue(),
                measurement.GetAverageF(),
                measurement.GetMedianF(),
                measurement.GetAveragePrecision(),
                measurement.GetMedianPrecision(),
                measurement.GetAverageRecall(),
                measurement.GetMedianRecall()
        );
        printToCSV(line);
    }

    /**
     * Write a single measurement result to a CSV file with all of the parameters
     * @param line Line in CSV format
     * @throws Exception
     */
    private void printToCSV(String line) throws Exception{
        if (_csvPrinter == null) {
            File f = new File("out/results.csv");
            if (!f.exists()) {
                f.createNewFile();
                FileOutputStream fop = new FileOutputStream(f, true);
                PrintWriter out = new PrintWriter(fop);
                out.println("Algorithm, ThresholdName, ThresholdValue, AverageF, MedianF, AveragePrecision, MedianPrecision, AverageRecall, MedianRecall");
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

    /**
     * Compare the results to the truth file and write results
     * @param results Map of query id to matching documents
     * @param alg Used algorithm
     * @throws Exception
     */
    public void measureResults(Map<String, List<String>> results, IRetrievalAlgorithm alg) throws Exception {
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

    /**
     * Run the queries
     * @param docs Map of docs from id to document content
     * @param queries Map of queries from query id to query content
     * @param searchModule The search module to run the queries on
     * @return Map of queries id to found documents
     * @throws Exception
     */
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

    int randomWithRange(int min, int max){
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }
}