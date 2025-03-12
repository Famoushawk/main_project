package testsuite;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.PrintStream;

public class ServiceRobotTest {

    private ServiceRobot serviceRobot;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @Before
    public void setUp() {
        serviceRobot = new ServiceRobot(50, 100, 2);
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
        outContent.reset();
    }

    @Test
    public void testServiceRobotInitialization() {
        assertTrue(serviceRobot.getTaskInfo().isEmpty());
        assertEquals(50, serviceRobot.getBatteryLevel());
    }

    @Test
    public void testDefineTaskValidInput() {
        String input = "Clean windows\n50\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        serviceRobot.defineTask();
        assertEquals(Integer.valueOf(50), serviceRobot.getTaskInfo().get("Clean windows"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefineTaskInvalidEnergyNegative() {
        String input = "Clean windows\n-10\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        serviceRobot.defineTask();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefineTaskInvalidEnergyTooHigh() {
        String input = "Clean windows\n150\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        serviceRobot.defineTask();
    }

    @Test
    public void testChargeOverride() {
        serviceRobot = new ServiceRobot(10, 100, 1);
        serviceRobot.charge();
        assertEquals("Time to charge is 90 minutes", outContent.toString().trim());
        assertEquals(100, serviceRobot.getBatteryLevel());
    }
}