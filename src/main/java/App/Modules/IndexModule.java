package App.Modules;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IndexModule {
    private RAMDirectory _index;
    private IndexWriterConfig _config;
    private Similarity _similarity;

    public IndexModule(RAMDirectory index, CharArraySet preDefinedStopWords, Similarity similarity) throws IOException {
        StandardAnalyzer analyzer = new StandardAnalyzer(preDefinedStopWords);

        if (preDefinedStopWords == null)
            analyzer = new StandardAnalyzer();

        _similarity = similarity;
        _config = new IndexWriterConfig(analyzer);
        _config.setSimilarity(_similarity);
        _index = index;
    }

    public void indexDocs(Map<String,String> docs) throws IOException {
        List<Document> documents = new ArrayList<>();

        for (String id : docs.keySet()) {
            Document doc = new Document();
            doc.add(new TextField("id", id, Field.Store.YES));
            doc.add(new TextField("content", docs.get(id), Field.Store.YES));
            documents.add(doc);
        }
        IndexWriter indexWriter = new IndexWriter(_index, _config);
        indexWriter.addDocuments(documents);
        indexWriter.close();
    }
}
