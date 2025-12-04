package org.learning.kds.config;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class KinesisSourceConfig {

    @NonNull
    private final String secretKey;

    @NonNull
    private final String accessKey;

    @NonNull
    private final String streamsEndpointUrl;

    @NonNull
    private final String region;

    @NonNull
    private final String streamName;

}
