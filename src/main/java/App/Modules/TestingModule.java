package App.Modules;

import App.Model.Measurement;
import App.Model.SingleMeasurement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;

public class TestingModule {
    private Map<String, List<String>> _truth;
    private Map<String, List<String>> _results;

    public TestingModule(Map<String, List<String>> truth, Map<String, List<String>> results) {
        _truth = truth;
        _results = results;
    }

    private SingleMeasurement GetResultsForQuery(String queryId){
        List <String> ground_truth_docs = _truth.get(queryId);
        List <String> found_docs = _results.get(queryId);
        List <String> false_negatives = ground_truth_docs
                .stream()
                .filter(doc -> !found_docs.contains(doc))
                .collect(Collectors.toList());
        List <String> true_positives = found_docs
                .stream()
                .filter(doc -> ground_truth_docs.contains(doc))
                .collect(Collectors.toList());
        List <String> false_positives = found_docs
                .stream()
                .filter(doc -> !ground_truth_docs.contains(doc))
                .collect(Collectors.toList());

        return new SingleMeasurement(false_negatives, true_positives, false_positives);
    }

    public Measurement TestQueries() {
        Map<String, SingleMeasurement> measurements = new HashMap<>();
        List <String> query_ids = new ArrayList<>(_truth.keySet());
        for (String id : query_ids) {
            SingleMeasurement mes = GetResultsForQuery(id);
            measurements.put(id, mes);
        }
        return new Measurement(measurements);
    }
}