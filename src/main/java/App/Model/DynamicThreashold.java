package App.Model;


import org.apache.lucene.search.ScoreDoc;

public class DynamicThreashold implements Threshold {

    private int _th;

    public DynamicThreashold(int th) {
        _th = th;
    }


    @Override
    public ScoreDoc[] FilterResults(ScoreDoc[] hits) {
        // Calculate ceiling for int
        int a = hits.length;
        int b = _th;
        int n = a / b + ((a % b == 0) ? 0 : 1);
        ScoreDoc[] result = new ScoreDoc[n];
        for (int i = 0; i < n; i++) {
            result[i] = hits[i];
        }
        return result;
    }
}