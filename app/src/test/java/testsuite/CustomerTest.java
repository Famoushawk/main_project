package testsuite;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import org.junit.After;
import org.junit.Before;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class CustomerTest {

    private Customer customer;
    private Set<String> preferences;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void initialize(){
        preferences = new HashSet<>(Arrays.asList("Sports", "Technology"));
        customer = new Customer(preferences, 3);
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        outContent.reset();
    }

    @Test
    public void testCustomerFieldsExistence() {
        Set<String> expectedFields = new HashSet<>(Arrays.asList("preferences", "advertisements", "maxAdvs"));
        Set<String> actualFields = new HashSet<>();
        Arrays.stream(Customer.class.getDeclaredFields()).forEach(f -> actualFields.add(f.getName()));
        assertEquals(expectedFields, actualFields);
    }

    @Test
    public void testCustomerConstructor(){
        assertEquals(preferences, customer.getPreferences());
        assertTrue(customer.getAdvertisements().isEmpty());
    }

    @Test
    public void testAddAdvertisementWhenListIsFull() {
        customer.addAdvertisement("ad1");
        customer.addAdvertisement("ad2");
        customer.addAdvertisement("ad3");
        assertFalse(customer.addAdvertisement("ad4"));
        assertEquals(3, customer.getAdvertisements().size());
    }

    @Test
    public void testAddAdvertisementWhenListHasSpace() {
        assertTrue(customer.addAdvertisement("ad1"));
        assertEquals(1, customer.getAdvertisements().size());
    }

    @Test
    public void testReadAdvertisementsMoreThanSize() {
        customer.addAdvertisement("ad1");
        customer.addAdvertisement("ad2");
        customer.addAdvertisement("ad3");
        customer.readAdvertisements(10);
        String expectedOutput = "ad3\nad2\nad1\n";
        assertEquals(expectedOutput, outContent.toString().replaceAll("\\r\\n?", "\n"));
        assertTrue(customer.getAdvertisements().isEmpty());
    }

    @Test
    public void testReadAdvertisementsEqualToSize() {
        customer.addAdvertisement("ad1");
        customer.addAdvertisement("ad2");
        customer.readAdvertisements(2);
        String expectedOutput = "ad2\nad1\n";
        assertEquals(expectedOutput, outContent.toString().replaceAll("\\r\\n?", "\n"));
        assertTrue(customer.getAdvertisements().isEmpty());
    }

    @Test
    public void testAdvertisingPlatformConstructor() {
        Set<String> forbiddenWords = new HashSet<>(Arrays.asList("badword", "spam"));
        AdvertisingPlatform platform = new AdvertisingPlatform(forbiddenWords);
        assertTrue(platform.getSubscribers().isEmpty());
        assertEquals(forbiddenWords, platform.getForbiddenWords());
    }

    @Test
    public void testAddCustomerSuccess() {
        AdvertisingPlatform platform = new AdvertisingPlatform(new HashSet<>());
        platform.addCustomer(customer);
        assertTrue(platform.getSubscribers().contains(customer));
        assertEquals("customer is added!", outContent.toString().trim());
    }

    @Test
    public void testAddCustomerAlreadyExists() {
        AdvertisingPlatform platform = new AdvertisingPlatform(new HashSet<>());
        platform.addCustomer(customer);
        outContent.reset();
        platform.addCustomer(customer);
        assertEquals(1, Collections.frequency(platform.getSubscribers(), customer));
        assertEquals("customer already exists!", outContent.toString().trim());
    }

    @Test
    public void testCheckValidityAdvertisementValid() {
        AdvertisingPlatform platform = new AdvertisingPlatform(new HashSet<>(Arrays.asList("badword")));
        assertTrue(platform.checkValidity("Great product! Buy now."));
    }

    @Test
    public void testCheckValidityAdvertisementTooLong() {
        AdvertisingPlatform platform = new AdvertisingPlatform(new HashSet<>());
        String advertisement = String.join(" ", Collections.nCopies(21, "word"));
        assertFalse(platform.checkValidity(advertisement));
    }

    @Test
    public void testCheckValidityAdvertisementWithForbiddenWord() {
        AdvertisingPlatform platform = new AdvertisingPlatform(new HashSet<>(Arrays.asList("badword")));
        assertFalse(platform.checkValidity("Check this badword product!"));
    }
}