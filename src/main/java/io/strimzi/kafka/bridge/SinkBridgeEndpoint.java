/*
 * Copyright 2016 Red Hat Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.strimzi.kafka.bridge;

import io.strimzi.kafka.bridge.config.BridgeConfigProperties;
import io.strimzi.kafka.bridge.tracker.OffsetTracker;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for sink bridge endpoints
 *
 * @param <K>   type of Kafka message key
 * @param <V>   type of Kafka message payload
 */
public abstract class SinkBridgeEndpoint<K, V> implements BridgeEndpoint {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected Vertx vertx;

    protected BridgeConfigProperties bridgeConfigProperties;

    private Handler<BridgeEndpoint> closeHandler;

    // used for tracking partitions and related offset for AT_LEAST_ONCE QoS delivery
    protected OffsetTracker offsetTracker;

    /**
     * Constructor
     *
     * @param vertx	Vert.x instance
     * @param bridgeConfigProperties	Bridge configuration
     */
    public SinkBridgeEndpoint(Vertx vertx, BridgeConfigProperties bridgeConfigProperties) {
        this.vertx = vertx;
        this.bridgeConfigProperties = bridgeConfigProperties;
    }

    @Override
    public BridgeEndpoint closeHandler(Handler<BridgeEndpoint> endpointCloseHandler) {
        this.closeHandler = endpointCloseHandler;
        return this;
    }

    /**
     * Raise close event
     */
    protected void handleClose() {

        if (this.closeHandler != null) {
            this.closeHandler.handle(this);
        }
    }
}
