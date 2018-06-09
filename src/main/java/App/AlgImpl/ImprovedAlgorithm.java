package App.AlgImpl;

import App.Model.IRetrivalAlgorithm;
import App.Model.Threshold.DynamicThreshold;
import App.Model.Threshold.IThreshold;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;

public class ImprovedAlgorithm implements IRetrivalAlgorithm {
    IThreshold _th = new DynamicThreshold(1.4);

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

