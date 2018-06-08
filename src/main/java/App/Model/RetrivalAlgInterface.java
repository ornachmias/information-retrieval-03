package App.Model;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.index.IndexReader;

public interface RetrivalAlgInterface {
    ScoreDoc[] FilterResults(ScoreDoc[] hits);
    Analyzer GetAnalyzer(CharArraySet preDefinedStopWords);
    IndexWriterConfig GetIndexWriterConfig(Analyzer analyzer);
    IndexSearcher GetSearcher(IndexReader reader);
    void SetFilter(Threshold th);
}