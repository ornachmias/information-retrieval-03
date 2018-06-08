package App.Model;

import java.util.Map;

public class Measurement {
    private Map<String, SingleMeasurement> _measurements;
    double _f = 0;
    double _p = 0;
    double _r = 0;

    public Measurement(Map<String, SingleMeasurement> measurements) {
        _measurements = measurements;
        DoCalculations();
    }


    private void DoCalculations()

    {
        Double total_f = 0.0;
        Double total_p = 0.0;
        Double total_r = 0.0;
        Double average_f;
        Double average_p;
        Double average_r;

        for (SingleMeasurement mes : _measurements.values()) {
            total_f += mes.GetF();
            total_p += mes.GetPrecision();
            total_r += mes.GetRecall();
        }
        _f = total_f / new Double(_measurements.size());
        _p = total_p / new Double(_measurements.size());
        _r = total_r / new Double(_measurements.size());
    }

    public double GetAverageF() {
        return _f;
    }

    public double GetAveragePrecision() {
        return _p;
    }

    public double GetAverageRecall() {
        return _r;
    }
}
