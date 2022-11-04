import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

import static ru.netology.geo.GeoServiceImpl.MOSCOW_IP;

@ExtendWith(MockitoExtension.class)

public class MockitoTests {
    private static final String IP_ADDRESS_HEADER = "x-real-ip";
    final String RU_IP = "172.0.32.11";
    final String US_IP = "96.44.183.149";
    private MessageSenderImpl messageSenderImpl;
    private Map<String, String> headers;
    Location locationUs;
    Location locationRu;

    @Mock
    private GeoService geoService;
    @Mock
    private LocalizationService localizationService;

    @BeforeEach
    void setUp() {
        geoService = Mockito.mock(GeoService.class);
        localizationService = Mockito.mock(LocalizationService.class);
        messageSenderImpl = new MessageSenderImpl(geoService, localizationService);
        headers = new HashMap<>();
        headers.put(IP_ADDRESS_HEADER, RU_IP);
        locationRu = new Location("Moscow", Country.RUSSIA, "Lenina", 15);
        locationUs = new Location("New York", Country.USA, "10th Avenue", 32);
    }

    @Test
    @DisplayName("return Russian IP")
    void return_RU_IP_send() {
        System.out.println("Testing RU IP:");
        Mockito.when(geoService.byIp(MOSCOW_IP)).thenReturn(locationRu);
        Mockito.when(localizationService.locale(Country.RUSSIA)).thenReturn("Добро пожаловать");
        String actualResult = messageSenderImpl.send(headers);
        String expectedResult = "Добро пожаловать";
        System.out.println("\n" + "Expected: " + actualResult + "\n");
        Assertions.assertEquals(expectedResult, actualResult);
    }
    @Test
    @DisplayName("return USA IP")
    void return_USA_IP_send() {
        System.out.println("Testing USA IP:");
        Mockito.when(geoService.byIp(US_IP)).thenReturn(locationUs);
        Mockito.when(localizationService.locale(Country.USA)).thenReturn("Welcome");
        headers.put(IP_ADDRESS_HEADER, US_IP);
        String actualResult = messageSenderImpl.send(headers);
        String expectedResult = "Welcome";
        System.out.println("\n" + "Expected: " + actualResult + "\n");
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("Check Location By IP")
    void Location_byIp() {
        Mockito.when(geoService.byIp(MOSCOW_IP)).thenReturn(locationRu);
        Location actualResult = geoService.byIp(MOSCOW_IP);
        Location expectedResult = locationRu;
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("Check Localization")
    void locale() {
        System.out.println("Testing Localization:");
        Mockito.when(localizationService.locale(Country.GERMANY)).thenReturn("Welcome");
        String actualResult = localizationService.locale(Country.GERMANY);
        String expectedResult = "Welcome";
        System.out.println("Expected: " + expectedResult + "\n" + "Actual: " + actualResult + "\n");
        Assertions.assertEquals(expectedResult, actualResult);
    }
}
