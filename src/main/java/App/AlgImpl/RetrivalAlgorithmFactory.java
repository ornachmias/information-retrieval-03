package App.AlgImpl;

import App.Model.IRetrivalAlgorithm;
import App.Model.RetrievalAlgorithmType;

public class RetrivalAlgorithmFactory {
    public static IRetrivalAlgorithm GetAlg(RetrievalAlgorithmType alg_type) {
        if (alg_type == RetrievalAlgorithmType.Improved) {
            return new ImprovedAlgorithm();
        }
        else {
            return new BasicAlgorithm();
        }
    }
}