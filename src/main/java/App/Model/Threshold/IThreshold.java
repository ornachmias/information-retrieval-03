package App.Model.Threshold;
import org.apache.lucene.search.ScoreDoc;

public interface IThreshold {
    ScoreDoc[] getTopResults(ScoreDoc[] hits);
    String getName();
    Double getValue();
}