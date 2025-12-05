package org.learning.kds.utils;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.learning.kds.model.Metrics;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.BiFunction;

import static org.learning.kds.utils.DateConverter.convert;

public class MetricsAggregator implements AggregateFunction<Metrics, Metrics, Metrics> {

    @Override
    public Metrics createAccumulator() {
        return new Metrics();
    }

    @Override
    public Metrics add(Metrics metrics, Metrics accumulator) {
        Double minValue = Optional.ofNullable(accumulator.getMinValue())
                .map(value -> Math.min(metrics.getMinValue(), value))
                .orElseGet(metrics::getMinValue);

        Double maxValue = Optional.ofNullable(accumulator.getMaxValue())
                .map(value -> Math.max(metrics.getMaxValue(), value))
                .orElseGet(metrics::getMaxValue);

        String fromTimestamp = Optional.ofNullable(accumulator.getFromTimestamp())
                .filter(value -> compareDates(metrics.getFromTimestamp(), value, LocalDateTime::isAfter))
                .orElseGet(metrics::getFromTimestamp);

        String toTimestamp = Optional.ofNullable(accumulator.getToTimestamp())
                .filter(value -> compareDates(metrics.getToTimestamp(), value, LocalDateTime::isBefore))
                .orElseGet(metrics::getToTimestamp);

        accumulator.setComponentName(metrics.getComponentName());
        accumulator.setMetricName(metrics.getMetricName());
        accumulator.setUnit(metrics.getUnit());

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

    private Boolean compareDates(
            String date1,
            String date2,
            BiFunction<LocalDateTime, LocalDateTime, Boolean> function
    ) {
        return function.apply(convert(date1), convert(date2));
    }

}
