package App.Model;

import java.util.Arrays;

import org.apache.lucene.search.ScoreDoc;

public class BasicThreashold implements Threshold {

    private double _th;

    public BasicThreashold(double th) {
        _th = th;
    }


    @Override
    public ScoreDoc[] FilterResults(ScoreDoc[] hits) {
        ScoreDoc[] result =
                Arrays.stream(hits).filter(x -> x.score > _th).toArray(ScoreDoc[]::new);

        return result;
    }
}