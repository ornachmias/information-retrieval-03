package App.Modules;

import App.Model.RetrivalAlgInterface;
import org.apache.lucene.analysis.Analyzer;
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
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchModule {
    private RAMDirectory _index;
    private RetrivalAlgInterface _alg;

    public SearchModule(RAMDirectory index, RetrivalAlgInterface alg) {
        _alg = alg;
        _index = index;
    }

    public List<String> queryDocs(String queryString, int hitsPerPage) throws ParseException, IOException {
        Analyzer analyzer = _alg.GetAnalyzer(null);
        QueryParser queryParser = new QueryParser("content", analyzer);
        queryParser.setSplitOnWhitespace(true);
        Query q = queryParser.parse(queryString.trim());
        IndexReader reader = DirectoryReader.open(_index);
        IndexSearcher searcher = _alg.GetSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        ScoreDoc[] filtered_hits = _alg.FilterResults(hits);
        List<String> docs = new ArrayList<>();
        for (ScoreDoc hit : filtered_hits) {
            Document doc = searcher.doc(hit.doc);
            docs.add(doc.get("id"));
        }

        reader.close();
        return docs;
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
}
