package org.example.concurrency;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.example.concurrency.Csp.Result;

import java.util.List;

class CspTest {

    @Test
    void testCsp() {
        int numJobs = 10;
        var csp = new Csp(numJobs);
        csp.start();
        List<Result> results = csp.getResults();
        assertEquals(10, results.size());
    }
}