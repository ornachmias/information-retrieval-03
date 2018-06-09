package App.Model;

import App.Model.Threshold.IThreshold;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.index.IndexReader;

public interface IRetrivalAlgorithm {
    ScoreDoc[] getTopResults(ScoreDoc[] hits);
    Analyzer getAnalyzer(CharArraySet preDefinedStopWords);
    IndexWriterConfig getIndexWriterConfig(Analyzer analyzer);
    IndexSearcher getSearcher(IndexReader reader);
    IThreshold getThreshold();
    String getName();
    void setThreshold(IThreshold th);
}