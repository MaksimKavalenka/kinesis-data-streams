package org.learning.kds.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Metrics {

    @JsonProperty("componentName")
    private String componentName;

    @JsonProperty("fromTimestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private LocalDateTime fromTimestamp;

    @JsonProperty("maxValue")
    private Double maxValue;

    @JsonProperty("metricName")
    private String metricName;

    @JsonProperty("minValue")
    private Double minValue;

    @JsonProperty("toTimestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private LocalDateTime toTimestamp;

    @JsonProperty("unit")
    private String unit;

    @Override
    public String toString() {
        return "Metrics{" +
               "componentName='" + componentName + '\'' +
               ", fromTimestamp='" + fromTimestamp + '\'' +
               ", maxValue=" + maxValue + '\'' +
               ", metricName=" + metricName + '\'' +
               ", minValue=" + minValue + '\'' +
               ", toTimestamp=" + toTimestamp + '\'' +
               ", unit=" + unit +
               '}';
    }

}
