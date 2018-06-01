package App;

import java.util.List;

class SingleMeasurement {
    private List<String> _false_negatives;
    private List<String> _true_positives;
    private List<String> _false_positives;

    public SingleMeasurement(List<String> false_negatives, List<String> true_positives, List<String> false_positives) {
        _false_negatives = false_negatives;
        _true_positives = true_positives;
        _false_positives = false_positives;
    }

    public Double GetPrecision() {

        if (_true_positives.size() + _false_positives.size() == 0) {
            return Double.MAX_VALUE;
        }
        return _true_positives.size() / new Double((_true_positives.size() + _false_positives.size() ));
    }

    public Double GetRecall() {
        if (_true_positives.size() + _false_negatives.size() == 0) {
            return Double.MAX_VALUE;
        }
        return _true_positives.size() / new Double((_true_positives.size() + _false_negatives.size()));
    }

    public Double GetF() {
        Double ALPHA = 0.5;
        Double f  = 1/(ALPHA* ((1/this.GetPrecision()) + (1/this.GetRecall())));
        return f;
    }

}

