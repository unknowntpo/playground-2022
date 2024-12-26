package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultMethodInterfaceImplTest {
  @Test
  void shouldNotBeTheSameArray() {
    assertTrue(DefaultMethodInterfaceImpl.EMPTY_ARRAY.equals(DefaultMethodInterfaceImpl.EMPTY_ARRAY));
  }
}
