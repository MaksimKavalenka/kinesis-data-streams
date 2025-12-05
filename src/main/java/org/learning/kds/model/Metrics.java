package org.learning.kds.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Metrics {

    @JsonProperty("componentName")
    private String componentName;

    @JsonProperty("fromTimestamp")
    private String fromTimestamp;

    @JsonProperty("toTimestamp")
    private String toTimestamp;

    @JsonProperty("metricName")
    private String metricName;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("minValue")
    private Double minValue;

    @JsonProperty("maxValue")
    private Double maxValue;

    @Override
    public String toString() {
        return "Metrics{" +
               "componentName='" + componentName + '\'' +
               ", fromTimestamp='" + fromTimestamp + '\'' +
               ", toTimestamp=" + toTimestamp + '\'' +
               ", metricName=" + metricName + '\'' +
               ", unit=" + unit + '\'' +
               ", minValue=" + minValue + '\'' +
               ", maxValue=" + maxValue +
               '}';
    }

}
