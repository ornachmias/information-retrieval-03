package App.AlgImpl;

import App.Model.IRetrivalAlgorithm;
import App.Model.Threshold.BasicThreshold;
import App.Model.Threshold.IThreshold;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.ClassicSimilarity;

public class BasicAlgorithm implements IRetrivalAlgorithm {
    IThreshold _th;

    public BasicAlgorithm() {

        _th = new BasicThreshold(0.96);
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
}
