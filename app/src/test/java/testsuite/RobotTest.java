package testsuite;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.PrintStream;

public class RobotTest {

    private Robot robot;
    private ServiceRobot serviceRobot;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        robot = new Robot(50, 100, 2);
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
        outContent.reset();
    }

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    // Robot tests

    @Test
    public void testRobotInitialization() {
        assertEquals(50, robot.getBatteryLevel());
        assertEquals(100, robot.getMaxLevel());
        assertEquals(2, robot.getChargingRate());
    }

    @Test
    public void testPerformTaskSuccessful() {
        assertTrue(robot.performTask(30));
        assertEquals(20, robot.getBatteryLevel());
    }

    @Test
    public void testPerformTaskInsufficientBattery() {
        assertFalse(robot.performTask(60));
        assertEquals(50, robot.getBatteryLevel());
    }

    @Test
    public void testTimeToCharge() {
        assertEquals(100, robot.timeToCharge());
    }

    @Test
    public void testCharge() {
        robot.charge();
        assertEquals(100, robot.getBatteryLevel());
    }