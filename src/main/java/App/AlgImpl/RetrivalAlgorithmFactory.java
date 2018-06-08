package App;


import App.AlgImpl.BasicAlgorithm;
import App.Model.IRetrivalAlgorithm;
import App.AlgImpl.ImprovedAlgorithm;
import App.Model.*;

public class RetrivalAlgorithmFactory {
    public static IRetrivalAlgorithm GetAlg(RetrievalAlgorithm alg_type) {
        if (alg_type == RetrievalAlgorithm.Improved) {
            return new ImprovedAlgorithm();
        }
        else {
            return new BasicAlgorithm();
        }
    }
}