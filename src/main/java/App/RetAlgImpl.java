package App;


import App.Model.*;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.index.IndexReader;

class BasicAlgorithm implements RetrivalAlgInterface {

    Threshold _th;

    public BasicAlgorithm() {

        _th = new BasicThreashold(12);
    }

    public ScoreDoc[] FilterResults(ScoreDoc[] hits) {
        return _th.FilterResults(hits);
    }

    public Analyzer GetAnalyzer(CharArraySet preDefinedStopWords) {
        if (preDefinedStopWords == null) {
            return new StandardAnalyzer();
        } else {
            return new StandardAnalyzer(preDefinedStopWords);
        }
    }

    public IndexWriterConfig GetIndexWriterConfig(Analyzer analyzer) {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setSimilarity(new ClassicSimilarity());
        return config;
    }

    public IndexSearcher GetSearcher(IndexReader reader) {
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(new ClassicSimilarity());
        return searcher;
    }

    public void SetFilter(Threshold th) {
        _th = th;
    }
}


class ImprovedAlgorithm implements RetrivalAlgInterface {
    Threshold _th = new DynamicThreashold(1.4);

    public ScoreDoc[] FilterResults(ScoreDoc[] hits) {
        return _th.FilterResults(hits);
    }

    public Analyzer GetAnalyzer(CharArraySet preDefinedStopWords) {
        if (preDefinedStopWords == null) {
            return new StandardAnalyzer();
        } else {
            return new StandardAnalyzer(preDefinedStopWords);
        }
    }

    public IndexWriterConfig GetIndexWriterConfig(Analyzer analyzer) {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        return config;
    }

    public IndexSearcher GetSearcher(IndexReader reader) {
        IndexSearcher searcher = new IndexSearcher(reader);
        return searcher;
    }

    public void SetFilter(Threshold th) {
        _th = th;
    }
}

public class RetAlgImpl {
    public static RetrivalAlgInterface GetAlg(RetrievalAlgorithm alg_type) {
        if (alg_type == RetrievalAlgorithm.Improved) {
            return new ImprovedAlgorithm();
        } else {
            return new BasicAlgorithm();
        }
    }
}