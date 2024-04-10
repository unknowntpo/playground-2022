package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

@Testcontainers
class MixedLifecycleTests {

  // will be shared between test methods
  @Container
  private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer();

  // will be started before and stopped after each test method
  @Container
  private PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer()
      .withDatabaseName("foo")
      .withUsername("foo")
      .withPassword("secret");

  @Test
  void test() {
    assertThat(MY_SQL_CONTAINER.isRunning()).isTrue();
    assertThat(postgresqlContainer.isRunning()).isTrue();
    assertTrue(1+1 == 2);
  }
}
