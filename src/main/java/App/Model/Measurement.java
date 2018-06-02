package App.Model;

import java.util.Map;

public class Measurement {
    private Map<String, SingleMeasurement> _measurements;

    public Measurement(Map<String, SingleMeasurement> measurements) {
        _measurements = measurements;
    }

    public Double GetAverageF() {
        Double total_f = 0.0;
        Double average_f;

        for (SingleMeasurement mes : _measurements.values()) {
            total_f += mes.GetF();
        }
        average_f = total_f / new Double(_measurements.size());
        return average_f;
    }
}