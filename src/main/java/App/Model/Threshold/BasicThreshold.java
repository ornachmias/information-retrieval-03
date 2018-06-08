package App.Model.Threshold;

import java.util.Arrays;

import org.apache.lucene.search.ScoreDoc;

public class BasicThreshold implements IThreshold {

    private double _th;

    public BasicThreshold(double th) {
        _th = th;
    }


    @Override
    public ScoreDoc[] getTopResults(ScoreDoc[] hits) {
        ScoreDoc[] result =
                Arrays.stream(hits).filter(x -> x.score > _th).toArray(ScoreDoc[]::new);

        return result;
    }
}