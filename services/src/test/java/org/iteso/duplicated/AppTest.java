/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.iteso.duplicated;

import org.iteso.duplicated.model.TestApp;
import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {
    @Test public void testAppHasAGreeting() {
        TestApp classUnderTest = new TestApp();
        assertNotNull("app should have a greeting", classUnderTest.getGreeting());
    }
}
