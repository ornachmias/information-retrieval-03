package App.AlgImpl;

import App.Model.IRetrievalAlgorithm;
import App.Model.Threshold.DynamicThreshold;
import App.Model.Threshold.IThreshold;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;

// Comments on the methods can be found in the base interface
public class ImprovedAlgorithm implements IRetrievalAlgorithm {

    IThreshold _th;

    /**
     * Initialize the algorithm with threshold parameters
     */
    public ImprovedAlgorithm() {
        _th = new DynamicThreshold(1.425);
    }

    public ScoreDoc[] getTopResults(ScoreDoc[] hits) {
        return _th.getTopResults(hits);
    }

    public Analyzer getAnalyzer(CharArraySet preDefinedStopWords) {
        if (preDefinedStopWords == null) {
            return new StandardAnalyzer();
        } else {
            return new StandardAnalyzer(preDefinedStopWords);
        }
    }

    public IndexWriterConfig getIndexWriterConfig(Analyzer analyzer) {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        return config;
    }

    public IndexSearcher getSearcher(IndexReader reader) {
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }

    public void setThreshold(IThreshold th) {
        _th = th;
    }

    public String getName() {
        return "Improved";
    }

    public IThreshold getThreshold() {
        return _th;
    }

}

