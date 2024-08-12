/*
 * Copyright 2024 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.reactive.reactor;

import com.google.errorprone.refaster.annotation.AfterTemplate;
import com.google.errorprone.refaster.annotation.BeforeTemplate;
import org.openrewrite.java.template.RecipeDescriptor;
import reactor.core.Disposable;
import reactor.core.publisher.*;
import reactor.core.scheduler.Scheduler;
import reactor.util.concurrent.Queues;

import java.time.Duration;
import java.util.Queue;
import java.util.function.Consumer;

@RecipeDescriptor(
        name = "Replace various `Processor.create` calls with their `Sinks` equivalent",
        description = "As of 3.5 Processors are deprecated and Sinks are preferred."
)
public class ReactorProcessorCreateToSink {

    @RecipeDescriptor(
            name = "Replace `MonoProcessor.create()` with `Sinks.one()`",
            description = "As of 3.5 MonoProcessor is deprecated and Sinks are preferred"
    )
    public static class MonoProcessorCreateToSink {
        @BeforeTemplate
        void createBefore() {
            MonoProcessor.create();
        }

        @AfterTemplate
        void sinkAfter() {
            Sinks.one();
        }
    }

    @RecipeDescriptor(
            name = "Replace `ReplayProcessor.create()` with `Sinks.many().replay().all()`",
            description = "As of 3.5 ReplayProcessor is deprecated and Sinks are preferred"
    )
    public static class ReplayProcessorCreateToSink {
        @BeforeTemplate
        void createBefore() {
            ReplayProcessor.create();
        }

        @AfterTemplate
        void sinkAfter() {
            Sinks.many().replay().all();
        }
    }

    @RecipeDescriptor(
            name = "Replace `ReplayProcessor.create(int)` with `Sinks.many().replay().limit(int)`",
            description = "As of 3.5 ReplayProcessor is deprecated and Sinks are preferred"
    )
    public static class ReplayProcessorCreateIntToSink {
        @BeforeTemplate
        void createBefore(Integer integer) {
            ReplayProcessor.create(integer);
        }

        @AfterTemplate
        void sinkAfter(Integer integer) {
            Sinks.many().replay().limit(integer);
        }
    }

    @RecipeDescriptor(
            name = "Replace `ReplayProcessor.create(int, false)` with `Sinks.many().replay().limit(int)`",
            description = "As of 3.5 ReplayProcessor is deprecated and Sinks are preferred"
    )
    public static class ReplayProcessorCreateIntLiteralFalseToSink {
        @BeforeTemplate
        void createBefore(Integer integer) {
            ReplayProcessor.create(integer, false);
        }

        @AfterTemplate
        void sinkAfter(Integer integer) {
            Sinks.many().replay().limit(integer);
        }
    }

    @RecipeDescriptor(
            name = "Replace `ReplayProcessor.create(int, true)` with `Sinks.many().replay().all(int)`",
            description = "As of 3.5 ReplayProcessor is deprecated and Sinks are preferred"
    )
    public static class ReplayProcessorCreateIntLiteralTrueToSink {
        @BeforeTemplate
        void createBefore(Integer integer) {
            ReplayProcessor.create(integer, true);
        }

        @AfterTemplate
        void sinkAfter(Integer integer) {
            Sinks.many().replay().all(integer);
        }
    }

    @RecipeDescriptor(
            name = "Replace `ReplayProcessor.createSizeAndTimeout(int, Duration)` with `Sinks.many().replay().limit(int, duration)`",
            description = "As of 3.5 ReplayProcessor is deprecated and Sinks are preferred"
    )
    public static class ReplayProcessorCreateSizeAndTimeoutToSink {
        @BeforeTemplate
        void createBefore(Integer integer, Duration duration) {
            ReplayProcessor.createSizeAndTimeout(integer, duration);
        }

        @AfterTemplate
        void sinkAfter(Integer integer, Duration duration) {
            Sinks.many().replay().limit(integer, duration);
        }
    }

    @RecipeDescriptor(
            name = "Replace `ReplayProcessor.createSizeAndTimeout(int, Duration, Scheduler)` with `Sinks.many().replay().limit(int, Duration, Scheduler)`",
            description = "As of 3.5 ReplayProcessor is deprecated and Sinks are preferred"
    )
    public static class ReplayProcessorCreateSizeAndTimeoutSchedulerToSink {
        @BeforeTemplate
        void createBefore(Integer integer, Duration duration, Scheduler scheduler) {
            ReplayProcessor.createSizeAndTimeout(integer, duration, scheduler);
        }

        @AfterTemplate
        void sinkAfter(Integer integer, Duration duration, Scheduler scheduler) {
            Sinks.many().replay().limit(integer, duration, scheduler);
        }
    }

    @RecipeDescriptor(
            name = "Replace `ReplayProcessor.createTimeout(Duration)` with `Sinks.many().replay().limit(duration)`",
            description = "As of 3.5 ReplayProcessor is deprecated and Sinks are preferred"
    )
    public static class ReplayProcessorCreateTimeoutToSink {
        @BeforeTemplate
        void createBefore(Duration duration) {
            ReplayProcessor.createTimeout(duration);
        }

        @AfterTemplate
        void sinkAfter(Duration duration) {
            Sinks.many().replay().limit(duration);
        }
    }

    @RecipeDescriptor(
            name = "Replace `ReplayProcessor.createTimeout(Duration, Scheduler)` with `Sinks.many().replay().limit(Duration, Scheduler)`",
            description = "As of 3.5 ReplayProcessor is deprecated and Sinks are preferred"
    )
    public static class ReplayProcessorCreateTimeoutSchedulerToSink {
        @BeforeTemplate
        void createBefore(Duration duration, Scheduler scheduler) {
            ReplayProcessor.createTimeout(duration, scheduler);
        }

        @AfterTemplate
        void sinkAfter(Duration duration, Scheduler scheduler) {
            Sinks.many().replay().limit(duration, scheduler);
        }
    }

    @RecipeDescriptor(
            name = "Replace `DirectProcessor.create()` with `Sinks.many().multicast().directBestEffort()`",
            description = "As of 3.5 DirectProcessor is deprecated and Sinks are preferred"
    )
    public static class DirectProcessorCreateToSink {
        @BeforeTemplate
        void createBefore() {
            DirectProcessor.create();
        }

        @AfterTemplate
        void sinkAfter() {
            Sinks.many().multicast().directBestEffort();
        }
    }

    @RecipeDescriptor(
            name = "Replace `EmitterProcessor.create()` with `Sinks.many().multicast().onBackpressureBuffer()`",
            description = "As of 3.5 EmitterProcessor is deprecated and Sinks are preferred"
    )
    public static class EmitterProcessorCreateToSink {
        @BeforeTemplate
        void createBefore() {
            EmitterProcessor.create();
        }

        @AfterTemplate
        void sinkAfter() {
            Sinks.many().multicast().onBackpressureBuffer();
        }
    }

    @RecipeDescriptor(
            name = "Replace `EmitterProcessor.create(Boolean)` with `Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, Boolean)`",
            description = "As of 3.5 EmitterProcessor is deprecated and Sinks are preferred"
    )
    public static class EmitterProcessorCreateBooleanToSink {
        @BeforeTemplate
        void createBefore(Boolean bool) {
            EmitterProcessor.create(bool);
        }

        @AfterTemplate
        void sinkAfter(Boolean bool) {
            Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, bool);
        }
    }

    @RecipeDescriptor(
            name = "Replace `EmitterProcessor.create(int)` with `Sinks.many().multicast().onBackpressureBuffer(int)`",
            description = "As of 3.5 EmitterProcessor is deprecated and Sinks are preferred"
    )
    public static class EmitterProcessorCreateIntToSink {
        @BeforeTemplate
        void createBefore(Integer integer) {
            EmitterProcessor.create(integer);
        }

        @AfterTemplate
        void sinkAfter(Integer integer) {
            Sinks.many().multicast().onBackpressureBuffer(integer);
        }
    }

    @RecipeDescriptor(
            name = "Replace `EmitterProcessor.create(int, Boolean)` with `Sinks.many().multicast().onBackpressureBuffer(int, Boolean)`",
            description = "As of 3.5 EmitterProcessor is deprecated and Sinks are preferred"
    )
    public static class EmitterProcessorCreateIntBooleanToSink {
        @BeforeTemplate
        void createBefore(Integer integer, Boolean bool) {
            EmitterProcessor.create(integer, bool);
        }

        @AfterTemplate
        void sinkAfter(Integer integer, Boolean bool) {
            Sinks.many().multicast().onBackpressureBuffer(integer, bool);
        }
    }

    @RecipeDescriptor(
            name = "Replace `UnicastProcessor.create()` with `Sinks.many().unicast().onBackpressureBuffer()`",
            description = "As of 3.5 UnicastProcessor is deprecated and Sinks are preferred"
    )
    public static class UnicastProcessorCreateToSink {
        @BeforeTemplate
        void createBefore() {
            UnicastProcessor.create();
        }

        @AfterTemplate
        void sinkAfter() {
            Sinks.many().unicast().onBackpressureBuffer();
        }
    }

    @RecipeDescriptor(
            name = "Replace `UnicastProcessor.create(Queue)` with `Sinks.many().unicast().onBackpressureBuffer(Queue)`",
            description = "As of 3.5 UnicastProcessor is deprecated and Sinks are preferred"
    )
    public static class UnicastProcessorCreateQueueToSink {
        @BeforeTemplate
        void createBefore(Queue queue) {
            UnicastProcessor.create(queue);
        }

        @AfterTemplate
        void sinkAfter(Queue queue) {
            Sinks.many().unicast().onBackpressureBuffer(queue);
        }
    }

    @RecipeDescriptor(
            name = "Replace `UnicastProcessor.create(Queue, Disposable)` with `Sinks.many().unicast().onBackpressureBuffer(Queue, Disposable)`",
            description = "As of 3.5 UnicastProcessor is deprecated and Sinks are preferred"
    )
    public static class UnicastProcessorCreateQueueDisposableToSink {
        @BeforeTemplate
        void createBefore(Queue queue, Disposable disp) {
            UnicastProcessor.create(queue, disp);
        }

        @AfterTemplate
        void sinkAfter(Queue queue, Disposable disp) {
            Sinks.many().unicast().onBackpressureBuffer(queue, disp);
        }
    }

    @RecipeDescriptor(
            name = "Replace `UnicastProcessor.create(Queue, Consumer, Disposable)` with `Sinks.many().unicast().onBackpressureBuffer(Queue, Disposable)`",
            description = "As of 3.5 UnicastProcessor is deprecated and Sinks are preferred"
    )
    public static class UnicastProcessorCreateQueueConsumerDisposableToSink {
        @BeforeTemplate
        void createBefore(Queue queue, Consumer cons, Disposable disp) {
            UnicastProcessor.create(queue, cons, disp);
        }

        @AfterTemplate
        void sinkAfter(Queue queue, Consumer cons, Disposable disp) {
            Sinks.many().unicast().onBackpressureBuffer(queue, disp);
        }
    }

}
