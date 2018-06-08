package App;

import App.Model.RetrivalAlgInterface;
import App.Modules.IndexModule;
import App.Modules.SearchModule;
import App.Model.RetrievalAlgorithm;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.RAMDirectory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class IndexSearchIntegrationTests {
    @Test
    public void IndexDocuments_SearchDocument_DocumentsReturned() throws IOException, ParseException {
        // Arrange
        RetrivalAlgInterface alg = RetAlgImpl.GetAlg(RetrievalAlgorithm.Basic);
        RAMDirectory index = new RAMDirectory();
        IndexModule indexModule = new IndexModule(index, null, alg);
        SearchModule searchModule = new SearchModule(index, alg);
        FileDataAccess fileDataAccess = new FileDataAccess();

        String filePath = TestHelper.getFilePathFromResources("TestDocsFile");
        Map<String, String> documents = fileDataAccess.parseDocsFile(filePath);

        // Act
        indexModule.indexDocs(documents);
        List<String> result = searchModule.queryDocs("SO",10);

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertNotNull(result.contains("146"));
        Assertions.assertNotNull(result.contains("11"));
    }

    @Test
    public void IndexDocuments_SearchDocument_DocumentReturned() throws IOException, ParseException {
        // Arrange
        RetrivalAlgInterface alg = RetAlgImpl.GetAlg(RetrievalAlgorithm.Basic);
        RAMDirectory index = new RAMDirectory();
        IndexModule indexModule = new IndexModule(index, null,alg);
        SearchModule searchModule = new SearchModule(index, alg);
        FileDataAccess fileDataAccess = new FileDataAccess();

        String filePath = TestHelper.getFilePathFromResources("TestDocsFile");
        Map<String, String> documents = fileDataAccess.parseDocsFile(filePath);

        // Act
        indexModule.indexDocs(documents);
        List<String> result = searchModule.queryDocs("VERBRUGGHE",10);

        // Assert
        Assertions.assertEquals(1, result.size());
        Assertions.assertNotNull(result.contains("11"));
    }

    @Test
    public void IndexDocuments_TopWords_Return5MostUsedWords() throws Exception {
        // Arrange
        RetrivalAlgInterface alg = RetAlgImpl.GetAlg(RetrievalAlgorithm.Basic);
        RAMDirectory index = new RAMDirectory();
        IndexModule indexModule = new IndexModule(index, CharArraySet.EMPTY_SET, alg);
        SearchModule searchModule = new SearchModule(index, alg);
        FileDataAccess fileDataAccess = new FileDataAccess();

        String filePath = TestHelper.getFilePathFromResources("TestDocsFile");
        Map<String, String> documents = fileDataAccess.parseDocsFile(filePath);

        // Act
        indexModule.indexDocs(documents);
        List<String> result = searchModule.getTopWords(5);

        // Assert
        Assertions.assertEquals(5, result.size());
    }
}
