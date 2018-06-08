package App.Model;
import org.apache.lucene.search.ScoreDoc;

public interface IThreshold {
    ScoreDoc[] getTopResults(ScoreDoc[] hits);
}