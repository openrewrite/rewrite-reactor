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

class ReactorProcessorCacheToSinkTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec
          .parser(JavaParser.fromJavaVersion()
            .classpathFromResources(new InMemoryExecutionContext(), "reactor-core-3.4", "reactive-streams"))
          .recipe(new ReactorProcessorCacheToSinkRecipes());
    }

    @Test
    @DocumentExample
    void processorCacheLast() {
        rewriteRun(
          //language=java
          java(
            """
              import reactor.core.publisher.ReplayProcessor;

              class TestClass {
                  void cache(Object value) {
                      ReplayProcessor.cacheLast();
                      ReplayProcessor.cacheLastOrDefault(value);
                  }
              }
              """,
            """
              import reactor.core.publisher.Sinks;

              class TestClass {
                  void cache(Object value) {
                      Sinks.many().replay().latest();
                      Sinks.many().replay().latestOrDefault(value);
                  }
              }
              """
          )
        );
    }

}
