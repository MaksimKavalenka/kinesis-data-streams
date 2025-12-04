package org.learning.kds;

import lombok.RequiredArgsConstructor;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.kinesis.source.KinesisStreamsSource;
import org.apache.flink.connector.kinesis.source.enumerator.assigner.ShardAssignerFactory;
import org.apache.flink.formats.json.JsonDeserializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.learning.kds.config.KinesisSourceConfig;
import org.learning.kds.model.Metrics;
import org.learning.kds.utils.JsonEncoder;
import org.learning.kds.utils.JsonEncoder.JsonMapperFactory;
import org.learning.kds.utils.MetricsAggregator;

import java.time.Duration;
import java.time.ZoneOffset;

import static org.apache.flink.connector.kinesis.source.config.KinesisSourceConfigOptions.InitialPosition;
import static org.apache.flink.connector.kinesis.source.config.KinesisSourceConfigOptions.STREAM_INITIAL_POSITION;

@RequiredArgsConstructor
public class StreamingApplication {

    private final KinesisSourceConfig sourceConfig;

    public static void main(String[] args) throws Exception {
        new StreamingApplication(null).run();
    }

    public JobID run() throws Exception {
        Configuration sourceConfig = new Configuration();
        sourceConfig.set(STREAM_INITIAL_POSITION, InitialPosition.LATEST);

        KinesisStreamsSource<Metrics> streamsSource = KinesisStreamsSource.<Metrics>builder()
                .setStreamArn("arn:aws:kinesis:us-east-1:000000000000:stream/metrics-stream")
                .setSourceConfig(sourceConfig)
                .setDeserializationSchema(new JsonDeserializationSchema<>(Metrics.class))
                .setKinesisShardAssigner(ShardAssignerFactory.uniformShardAssigner())
                .build();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        WatermarkStrategy<Metrics> watermarkStrategy = WatermarkStrategy
                .<Metrics>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                .withIdleness(Duration.ofMinutes(1))
                .withTimestampAssigner((metrics, timestamp) ->
                        metrics.getFromTimestamp().toEpochSecond(ZoneOffset.UTC)
                );

        JsonMapperFactory jsonMapperFactory = () -> new ObjectMapper().registerModule(new JavaTimeModule());
        JsonEncoder jsonEncoder = new JsonEncoder(jsonMapperFactory);

        env.fromSource(streamsSource, watermarkStrategy, "Kinesis Metrics Stream", TypeInformation.of(Metrics.class))
                .keyBy(Metrics::getComponentName)
                .window(TumblingProcessingTimeWindows.of(Duration.ofMinutes(5)))
                .aggregate(new MetricsAggregator())
                .map(jsonEncoder::encode)
                .returns(TypeInformation.of(String.class))
                .print();

        return env.executeAsync().getJobID();
    }

}
