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
import reactor.core.publisher.ReplayProcessor;
import reactor.core.publisher.Sinks;

@RecipeDescriptor(
        name = "Replace various `Processor.cache` calls with their `Sinks` equivalent",
        description = "As of 3.5 Processors are deprecated and Sinks are preferred."
)
public class ReactorProcessorCacheToSink {

    @RecipeDescriptor(
            name = "Replace `ReplayProcessor.cacheLast()` with `Sinks.many().replay().latest()`",
            description = "As of 3.5 ReplayProcessor is deprecated and Sinks are preferred"
    )
    public static class ReplayProcessorCacheToSink {
        @BeforeTemplate
        void createBefore() {
            ReplayProcessor.cacheLast();
        }

        @AfterTemplate
        void sinkAfter() {
            Sinks.many().replay().latest();
        }
    }

    @RecipeDescriptor(
            name = "Replace `ReplayProcessor.cacheLast()` with `Sinks.many().replay().latest()`",
            description = "As of 3.5 ReplayProcessor is deprecated and Sinks are preferred"
    )
    public static class ReplayProcessorCacheDefaultToSink {
        @BeforeTemplate
        void createBefore(Object value) {
            ReplayProcessor.cacheLastOrDefault(value);
        }

        @AfterTemplate
        void sinkAfter(Object value) {
            Sinks.many().replay().latestOrDefault(value);
        }
    }

}
