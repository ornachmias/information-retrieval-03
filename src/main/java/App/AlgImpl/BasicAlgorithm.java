package App.AlgImpl;

import App.Model.IRetrievalAlgorithm;
import App.Model.Threshold.BasicThreshold;
import App.Model.Threshold.IThreshold;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.ClassicSimilarity;

// Comments on the methods can be found in the base interface
public class BasicAlgorithm implements IRetrievalAlgorithm {
    IThreshold _th;

    /**
     * Initialize the algorithm with threshold parameters
     */
    public BasicAlgorithm() {

        _th = new BasicThreshold(0.99);
    }

    public ScoreDoc[] getTopResults(ScoreDoc[] hits) {
        return _th.getTopResults(hits);
    }

    public Analyzer getAnalyzer(CharArraySet preDefinedStopWords) {
        if (preDefinedStopWords == null) {
            return new StopAnalyzer();
        } else {
            return new StopAnalyzer(preDefinedStopWords);
        }
    }

    public IndexWriterConfig getIndexWriterConfig(Analyzer analyzer) {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setSimilarity(new ClassicSimilarity());
        return config;
    }

    public IndexSearcher getSearcher(IndexReader reader) {
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(new ClassicSimilarity());
        return searcher;
    }

    public void setThreshold(IThreshold th) {
        _th = th;
    }


    public String getName() {
        return "Basic";
    }

    public IThreshold getThreshold() {
        return _th;
    }
}
