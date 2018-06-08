package App.Model;
import java.util.Arrays;

import org.apache.lucene.search.ScoreDoc;

public class DynamicThreashold implements Threshold {

    private double _th;

    public DynamicThreashold(double th) {
        _th = th;
    }


    @Override
    public ScoreDoc[] FilterResults(ScoreDoc[] hits) {
        if (hits.length == 0) {
            return hits;
        }

        double th  = hits[0].score / _th;
        ScoreDoc[] result =
                Arrays.stream(hits).filter(x -> x.score > th).toArray(ScoreDoc[]::new);

        return result;
    }
}