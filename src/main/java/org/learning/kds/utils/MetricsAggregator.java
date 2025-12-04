package org.learning.kds.utils;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.learning.kds.model.Metrics;

import java.time.LocalDateTime;

public class MetricsAggregator implements AggregateFunction<Metrics, Metrics, Metrics> {

    @Override
    public Metrics createAccumulator() {
        return new Metrics();
    }

    @Override
    public Metrics add(Metrics value, Metrics accumulator) {
        Double minValue = Math.min(value.getMinValue(), accumulator.getMinValue());
        Double maxValue = Math.max(value.getMaxValue(), accumulator.getMaxValue());

        LocalDateTime fromTimestamp = value.getFromTimestamp().isBefore(accumulator.getFromTimestamp())
                ? value.getFromTimestamp()
                : accumulator.getFromTimestamp();

        LocalDateTime toTimestamp = value.getToTimestamp().isBefore(accumulator.getToTimestamp())
                ? accumulator.getToTimestamp()
                : value.getToTimestamp();

        accumulator.setMinValue(minValue);
        accumulator.setMaxValue(maxValue);
        accumulator.setFromTimestamp(fromTimestamp);
        accumulator.setToTimestamp(toTimestamp);

        return accumulator;
    }

    @Override
    public Metrics getResult(Metrics accumulator) {
        return accumulator;
    }

    @Override
    public Metrics merge(Metrics a, Metrics b) {
        return add(a, b);
    }

}
