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

import org.junit.jupiter.api.Test;
import org.openrewrite.DocumentExample;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.java;

class ReactorProcessorCreateToSinkTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec
          .parser(JavaParser.fromJavaVersion()
            .classpathFromResources(new InMemoryExecutionContext(), "reactor-core-3.4", "reactive-streams"))
          .recipe(new ReactorProcessorCreateToSinkRecipes());
    }

    @Test
    @DocumentExample
    void processorCreation() {
        rewriteRun(
          //language=java
          java(
            """
              import reactor.core.publisher.DirectProcessor;
              import reactor.core.publisher.EmitterProcessor;
              import reactor.core.publisher.MonoProcessor;
              import reactor.core.publisher.ReplayProcessor;
              import reactor.core.publisher.UnicastProcessor;

              class TestClass {
                  void create() {
                      MonoProcessor.create();
                      ReplayProcessor.create();
                      DirectProcessor.create();
                      EmitterProcessor.create();
                      UnicastProcessor.create();
                  }
              }
              """,
            """
              import reactor.core.publisher.*;

              class TestClass {
                  void create() {
                      Sinks.one();
                      Sinks.many().replay().all();
                      Sinks.many().multicast().directBestEffort();
                      Sinks.many().multicast().onBackpressureBuffer();
                      Sinks.many().unicast().onBackpressureBuffer();
                  }
              }
              """
          )
        );
    }

    @Test
    void processorCreationInt() {
        rewriteRun(
          //language=java
          java(
            """
              import reactor.core.publisher.EmitterProcessor;
              import reactor.core.publisher.ReplayProcessor;

              class TestClass {
                  void create(Integer integer) {
                      ReplayProcessor.create(integer);
                      EmitterProcessor.create(integer);
                  }
              }
              """,
            """
              import reactor.core.publisher.Sinks;

              class TestClass {
                  void create(Integer integer) {
                      Sinks.many().replay().limit(integer);
                      Sinks.many().multicast().onBackpressureBuffer(integer);
                  }
              }
              """
          )
        );
    }

    @Test
    void processorCreationIntLiteralBoolean() {
        rewriteRun(
          //language=java
          java(
            """
              import reactor.core.publisher.ReplayProcessor;

              class TestClass {
                  void create(Integer integer) {
                      ReplayProcessor.create(integer, false);
                      ReplayProcessor.create(integer, true);
                  }
              }
              """,
            """
              import reactor.core.publisher.Sinks;

              class TestClass {
                  void create(Integer integer) {
                      Sinks.many().replay().limit(integer);
                      Sinks.many().replay().all(integer);
                  }
              }
              """
          )
        );
    }

    @Test
    void processorCreationBoolean() {
        rewriteRun(
          //language=java
          java(
            """
              import reactor.core.publisher.EmitterProcessor;

              class TestClass {
                  void create(Boolean bool) {
                      EmitterProcessor.create(bool);
                  }
              }
              """,
            """
              import reactor.core.publisher.Sinks;
              import reactor.util.concurrent.Queues;

              class TestClass {
                  void create(Boolean bool) {
                      Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, bool);
                  }
              }
              """
          )
        );
    }

    @Test
    void processorCreationIntBoolean() {
        rewriteRun(
          //language=java
          java(
            """
              import reactor.core.publisher.EmitterProcessor;

              class TestClass {
                  void create(Integer integer, Boolean bool) {
                      EmitterProcessor.create(integer, bool);
                  }
              }
              """,
            """
              import reactor.core.publisher.Sinks;

              class TestClass {
                  void create(Integer integer, Boolean bool) {
                      Sinks.many().multicast().onBackpressureBuffer(integer, bool);
                  }
              }
              """
          )
        );
    }

    @Test
    void processorCreationQueue() {
        rewriteRun(
          //language=java
          java(
            """
              import java.util.Queue;
              import java.util.function.Consumer;
              import reactor.core.Disposable;
              import reactor.core.publisher.UnicastProcessor;

              class TestClass {
                  void create(Queue queue, Consumer consumer, Disposable disposable) {
                      UnicastProcessor.create(queue);
                      UnicastProcessor.create(queue, consumer, disposable);
                      UnicastProcessor.create(queue, disposable);
                  }
              }
              """,
            """
              import java.util.Queue;
              import java.util.function.Consumer;
              import reactor.core.Disposable;
              import reactor.core.publisher.Sinks;

              class TestClass {
                  void create(Queue queue, Consumer consumer, Disposable disposable) {
                      Sinks.many().unicast().onBackpressureBuffer(queue);
                      Sinks.many().unicast().onBackpressureBuffer(queue, disposable);
                      Sinks.many().unicast().onBackpressureBuffer(queue, disposable);
                  }
              }
              """
          )
        );
    }

    @Test
    void processorCreationTimeoutAndSize() {
        rewriteRun(
          //language=java
          java(
            """
              import java.time.Duration;
              import reactor.core.publisher.ReplayProcessor;
              import reactor.core.scheduler.Scheduler;

              class TestClass {
                  void create(Integer integer, Duration duration, Scheduler scheduler) {
                      ReplayProcessor.createTimeout(duration);
                      ReplayProcessor.createTimeout(duration, scheduler);
                      ReplayProcessor.createSizeAndTimeout(integer, duration);
                      ReplayProcessor.createSizeAndTimeout(integer, duration, scheduler);
                  }
              }
              """,
            """
              import java.time.Duration;
              import reactor.core.publisher.Sinks;
              import reactor.core.scheduler.Scheduler;

              class TestClass {
                  void create(Integer integer, Duration duration, Scheduler scheduler) {
                      Sinks.many().replay().limit(duration);
                      Sinks.many().replay().limit(duration, scheduler);
                      Sinks.many().replay().limit(integer, duration);
                      Sinks.many().replay().limit(integer, duration, scheduler);
                  }
              }
              """
          )
        );
    }
}
