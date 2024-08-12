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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openrewrite.DocumentExample;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.java;

public class ReactorDeclarativeRecipeTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec
          .parser(JavaParser.fromJavaVersion()
            .classpathFromResources(new InMemoryExecutionContext(), "reactor-core-3.4", "reactive-streams"))
          .recipeFromResources("org.openrewrite.reactive.reactor.UpgradeReactor_3_5");
    }

    @Nested
    class ReactorCurrentContextToContextViewTest {

        @Test
        @DocumentExample
        void currentContextToCurrentView() {
            rewriteRun(
              //language=java
              java(
                """
                  import reactor.core.publisher.MonoSink;
                  import reactor.core.publisher.FluxSink;
                  import reactor.core.publisher.SynchronousSink;

                  class TestClass {
                      void create(MonoSink<String> mono, FluxSink<String> flux, SynchronousSink<String> sync) {
                          mono.currentContext();
                          flux.currentContext();
                          sync.currentContext();
                      }
                  }
                  """,
                """
                  import reactor.core.publisher.MonoSink;
                  import reactor.core.publisher.FluxSink;
                  import reactor.core.publisher.SynchronousSink;

                  class TestClass {
                      void create(MonoSink<String> mono, FluxSink<String> flux, SynchronousSink<String> sync) {
                          mono.contextView();
                          flux.contextView();
                          sync.contextView();
                      }
                  }
                  """
              )
            );
        }
    }

    @Nested
    class ReactorDeferWithContextToDeferContextualTest {

        @Test
        @DocumentExample
        void currentContextToCurrentView() {
            rewriteRun(
              //language=java
              java(
                """
                  import reactor.core.publisher.Mono;
                  import reactor.core.publisher.Flux;

                  class TestClass {
                      void create(String s) {
                          Mono.deferWithContext(ctx -> Mono.just(s));
                          Flux.deferWithContext(ctx -> Flux.just(s));
                      }
                  }
                  """,
                """
                  import reactor.core.publisher.Mono;
                  import reactor.core.publisher.Flux;

                  class TestClass {
                      void create(String s) {
                          Mono.deferContextual(ctx -> Mono.just(s));
                          Flux.deferContextual(ctx -> Flux.just(s));
                      }
                  }
                  """
              )
            );
        }
    }

    @Nested
    class ReactorFirstToFirstWithSignalTest {

        @Test
        @DocumentExample
        void firstToFirstWithSignal() {
            rewriteRun(
              //language=java
              java(
                """
                  import org.reactivestreams.Publisher;
                  import reactor.core.publisher.Flux;
                  import reactor.core.publisher.Mono;

                  class TestClass {
                      void first(Iterable iterable, Mono<String> mono, Publisher<String> publisher) {
                          Flux.first(iterable);
                          Mono.first(iterable);
                          Flux.first(publisher);
                          Mono.first(mono);
                      }
                  }
                  """,
                """
                  import org.reactivestreams.Publisher;
                  import reactor.core.publisher.Flux;
                  import reactor.core.publisher.Mono;

                  class TestClass {
                      void first(Iterable iterable, Mono<String> mono, Publisher<String> publisher) {
                          Flux.firstWithSignal(iterable);
                          Mono.firstWithSignal(iterable);
                          Flux.firstWithSignal(publisher);
                          Mono.firstWithSignal(mono);
                      }
                  }
                  """
              )
            );
        }
    }

    @Nested
    class ReactorFluxLimitRequestToTakeTest {

        @Test
        @DocumentExample
        void limitRequestToTake() {
            rewriteRun(
              //language=java
              java(
                """
                  import reactor.core.publisher.Flux;

                  class TestClass {
                      void getContext(Flux<String> flux, Long limit) {
                          flux.limitRequest(limit);
                      }
                  }
                  """,
                """
                  import reactor.core.publisher.Flux;

                  class TestClass {
                      void getContext(Flux<String> flux, Long limit) {
                          flux.take(limit);
                      }
                  }
                  """
              )
            );
        }
    }

    @Nested
    class ReactorFluxPublishNextToShareNextTest {

        @Test
        @DocumentExample
        void publishNextToShareNext() {
            rewriteRun(
              //language=java
              java(
                """
                  import reactor.core.publisher.Flux;
                  import reactor.core.publisher.Mono;

                  class TestClass {
                      void create(Flux<String> flux) {
                          Mono<String> mono = flux.publishNext();
                      }
                  }
                  """,
                """
                  import reactor.core.publisher.Flux;
                  import reactor.core.publisher.Mono;

                  class TestClass {
                      void create(Flux<String> flux) {
                          Mono<String> mono = flux.shareNext();
                      }
                  }
                  """
              )
            );
        }
    }

    @Nested
    class ReactorSchedulersElasticToBoundedElasticTest {

        @Test
        @DocumentExample
        void elasticToBoundedElastic() {
            rewriteRun(
              //language=java
              java(
                """
                  import reactor.core.scheduler.Schedulers;

                  class TestClass {
                      void elastic() {
                          Schedulers.elastic();
                      }
                  }
                  """,
                """
                  import reactor.core.scheduler.Schedulers;

                  class TestClass {
                      void elastic() {
                          Schedulers.boundedElastic();
                      }
                  }
                  """
              )
            );
        }
    }

    @Nested
    class ReactorSignalGetContextToGetContextViewTest {

        @Test
        @DocumentExample
        void signalGetContextToGetContextView() {
            rewriteRun(
              //language=java
              java(
                """
                  import reactor.core.publisher.Signal;

                  class TestClass {
                      void getContext(Signal<String> signal) {
                          signal.getContext();
                      }
                  }
                  """,
                """
                  import reactor.core.publisher.Signal;

                  class TestClass {
                      void getContext(Signal<String> signal) {
                          signal.getContextView();
                      }
                  }
                  """
              )
            );
        }
    }

}
