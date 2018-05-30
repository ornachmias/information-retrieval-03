package App;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchModule {
    private RAMDirectory _index;
    private StandardAnalyzer _analyzer;

    public SearchModule(RAMDirectory index) {
        _analyzer = new StandardAnalyzer();
        _index = index;
    }

    public List<String> queryDocs(String queryString, int hitsPerPage) throws ParseException, IOException {
        Query q = new QueryParser("content", _analyzer).parse(queryString);
        IndexReader reader = DirectoryReader.open(_index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        List<String> result = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            Document doc = searcher.doc(hit.doc);
            result.add(doc.get("id"));
        }

        return result;
    }
}
