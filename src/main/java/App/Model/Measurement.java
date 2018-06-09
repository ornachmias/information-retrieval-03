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
        int precision_samples = 0;
        int recall_samples = 0;

        for (SingleMeasurement mes : _measurements.values()) {
            total_f += mes.GetF();
            double precision = mes.GetPrecision();
            double recall = mes.GetRecall();
            if (precision != Double.MAX_VALUE) {
                total_p += precision;
                precision_samples+=1;
            }

            if (recall != Double.MAX_VALUE) {
                total_r += recall;
                recall_samples+=1;
            }
        }
        _f = total_f / new Double(_measurements.size());
        _p = total_p / precision_samples;
        _r = total_r / recall_samples;
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
