package App.Model;
import org.apache.lucene.search.ScoreDoc;

public interface Threshold{
    ScoreDoc[] FilterResults(ScoreDoc[] hits);
}