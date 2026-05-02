package com.devops.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HelloDevOps.
 * Jenkins will run these tests automatically in the CI/CD pipeline.
 */
public class HelloDevOpsTest {

    @Test
    void testGreeting() {
        HelloDevOps app = new HelloDevOps();
        assertEquals("Hello from DevOps Pipeline!", app.getGreeting());
    }

    @Test
    void testVersion() {
        HelloDevOps app = new HelloDevOps();
        assertNotNull(app.getVersion());
        assertTrue(app.getVersion().contains("SNAPSHOT"));
    }

    @Test
    void testAdd() {
        HelloDevOps app = new HelloDevOps();
        assertEquals(5, app.add(2, 3));
        assertEquals(0, app.add(0, 0));
        assertEquals(-1, app.add(2, -3));
    }
}
