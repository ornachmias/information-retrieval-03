package App.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.List;

public class Measurement {
    private Map<String, SingleMeasurement> _measurements;
    double _average_f = 0;
    double _average_p = 0;
    double _average_r = 0;


    public Measurement(Map<String, SingleMeasurement> measurements) {
        _measurements = measurements;
        DoCalculations();
    }


    private void DoCalculations()

    {
        Double total_f = 0.0;
        Double total_p = 0.0;
        Double total_r = 0.0;
        int precision_samples = 0;
        int recall_samples = 0;

        for (SingleMeasurement mes : _measurements.values()) {
            total_f += mes.GetF();
            double precision = mes.GetPrecision();
            double recall = mes.GetRecall();
            if (precision != Double.MAX_VALUE) {
                total_p += precision;
                precision_samples += 1;
            }

            if (recall != Double.MAX_VALUE) {
                total_r += recall;
                recall_samples += 1;
            }
        }
        _average_f = total_f / new Double(_measurements.size());
        _average_p = total_p / precision_samples;
        _average_r = total_r / recall_samples;
    }

    public double GetAverageF() {
        return _average_f;
    }

    public double GetAveragePrecision() {
        return _average_p;
    }

    public double GetAverageRecall() {
        return _average_r;
    }

    public double GetMedianF() {
        int idx = (int)Math.ceil(_measurements.size()/2-1.0);
        List <SingleMeasurement> measurements = new ArrayList<>(_measurements.values());
        Collections.sort(measurements);
        return measurements.get(idx).GetF();
    }

    public double GetMedianPrecision() {
        int idx = (int)Math.ceil(_measurements.size()/2-1.0);
        List <SingleMeasurement> measurements = new ArrayList<>(_measurements.values());
        Collections.sort(measurements);
        return measurements.get(idx).GetPrecision();
    }

    public double GetMedianRecall() {
        int idx = (int)Math.ceil(_measurements.size()/2-1.0);
        List <SingleMeasurement> measurements = new ArrayList<>(_measurements.values());
        Collections.sort(measurements);
        return measurements.get(idx).GetRecall();
    }
}