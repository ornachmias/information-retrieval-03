package App.AlgImpl;

import App.Model.IRetrievalAlgorithm;
import App.Model.RetrievalAlgorithmType;

public class RetrivalAlgorithmFactory {
    public static IRetrievalAlgorithm GetAlg(RetrievalAlgorithmType alg_type) {
        if (alg_type == RetrievalAlgorithmType.Improved) {
            return new ImprovedAlgorithm();
        }
        else {
            return new BasicAlgorithm();
        }
    }
}