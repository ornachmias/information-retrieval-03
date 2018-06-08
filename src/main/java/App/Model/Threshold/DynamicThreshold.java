package App.Model.Threshold;
import java.util.Arrays;

import org.apache.lucene.search.ScoreDoc;

public class DynamicThreshold implements IThreshold {

    private double _th;

    public DynamicThreshold(double th) {
        _th = th;
    }


    @Override
    public ScoreDoc[] getTopResults(ScoreDoc[] hits) {
        if (hits.length == 0) {
            return hits;
        }

        double th  = hits[0].score / _th;
        ScoreDoc[] result =
                Arrays.stream(hits).filter(x -> x.score > th).toArray(ScoreDoc[]::new);

        return result;
    }
}