package App.Model;

import App.Model.Threshold.IThreshold;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.index.IndexReader;

public interface IRetrievalAlgorithm {
    /**
     * Filter the results for the query based on the threshold
     * @param hits Result from search for the query
     * @return Filtered results based on the threshold
     */
    ScoreDoc[] getTopResults(ScoreDoc[] hits);

    /**
     * Initialize the analyzer fit for the algorithm
     * @param preDefinedStopWords Set of predefined stop words. null if we don't want any stop words
     * @return Analyzer object for the current algorithm based on the implementation.
     */
    Analyzer getAnalyzer(CharArraySet preDefinedStopWords);

    /**
     * Get index writer based on the similarity intended for this algorithm
     * @param analyzer Pre-set analyzer fit to this algorithm.
     * @return IndexWriter allowing indexing for this algorithm structure
     */
    IndexWriterConfig getIndexWriterConfig(Analyzer analyzer);


    /**
     * Get index writer based on the similarity intended for this algorithm
     * @param reader Reader for the indexed data
     * @return Searcher allowing us to run queries on the index
     */
    IndexSearcher getSearcher(IndexReader reader);

    /**
     * Get the threshold object that was set for this algorithm
     * @return
     */
    IThreshold getThreshold();

    /**
     * Set the threshold object for this algorithm
     * @param th Threshold object
     */
    void setThreshold(IThreshold th);

    /**
     * Get the name for this algorithm (for output)
     * @return Pretty string to represent the algorithm
     */
    String getName();

}