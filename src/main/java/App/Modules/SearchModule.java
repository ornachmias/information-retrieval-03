package App.Modules;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.misc.HighFreqTerms;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchModule {
    private RAMDirectory _index;
    private Analyzer _analyzer;
    private Similarity _similarity;
    private float _scoreThreshold = (float) 0.96;
    private int _dynamicThreshold = 100;

    public SearchModule(RAMDirectory index, Similarity similarity) {
        _analyzer = new StandardAnalyzer();
        _similarity = similarity;
        _index = index;
    }

    public SearchModule(RAMDirectory index, Similarity similarity, float threshold, int dynamicThreshold) {
        _analyzer = new StopAnalyzer();
        _similarity = similarity;
        _index = index;
        _scoreThreshold = threshold;
        _dynamicThreshold = dynamicThreshold;
    }

    public List<String> queryDocs(String queryString, int hitsPerPage) throws ParseException, IOException {
        QueryParser queryParser = new QueryParser("content", _analyzer);
        queryParser.setSplitOnWhitespace(true);
        Query q = queryParser.parse(queryString.trim());
        IndexReader reader = DirectoryReader.open(_index);
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(_similarity);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        searcher.search(q, collector);
        ScoreDoc[] hits = getTopResultsByConstThreshold(collector.topDocs().scoreDocs);

        List<String> result = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            Document doc = searcher.doc(hit.doc);
            result.add(doc.get("id"));
        }

        reader.close();
        return result;
    }

    public List<String> getTopWords(int numberOfTopWords) throws Exception {
        List<String> result = new ArrayList<>();
        IndexReader reader = DirectoryReader.open(_index);
        TermStats[] commonTerms = HighFreqTerms.getHighFreqTerms(reader, numberOfTopWords, "content", new HighFreqTerms.TotalTermFreqComparator());
        for (TermStats commonTerm : commonTerms) {
            result.add(commonTerm.termtext.utf8ToString());
        }

        reader.close();
        return result;
    }

    private ScoreDoc[] getTopResultsDynamically(ScoreDoc[] hits){
        // Calculate ceiling for int
        int a = hits.length;
        int b = _dynamicThreshold;
        int n = a / b + ((a % b == 0) ? 0 : 1);
        ScoreDoc[] result = new ScoreDoc[n];
        for(int i = 0; i < n; i++) {
            result[i] = hits[i];
        }
        return result;
    }

    @Deprecated
    private ScoreDoc[] getTopResultsByConstThreshold(ScoreDoc[] hits){
        ScoreDoc[] result =
                Arrays.stream(hits).filter(x-> x.score > _scoreThreshold).toArray(ScoreDoc[]::new);

        return result;
    }
}
