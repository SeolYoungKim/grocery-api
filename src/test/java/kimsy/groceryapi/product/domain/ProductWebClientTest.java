package kimsy.groceryapi.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class ProductWebClientTest {
    public static MockWebServer mockWebServer;

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    private final ObjectMapper objectMapper = new ObjectMapper();
    private ProductWebClient productWebClient;

    @BeforeEach
    void setUp() {
        final String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());

        FruitWebClient fruitWebClient = new FruitWebClient(baseUrl);
        VegetableWebClient vegetableWebClient = new VegetableWebClient(baseUrl);

        productWebClient = new ProductWebClient(fruitWebClient, vegetableWebClient);
    }

    @DisplayName("품목이 전달되었을 때")
    @Nested
    class GetProducts {
        private static final String ACCESS_TOKEN = "{ \"accessToken\" : \"token\" }";

        @DisplayName("품목으로 fruit이 전달된 경우 과일 목록을 반환한다.")
        @Test
        void fruitToProductNames() throws JsonProcessingException {
            mockWebServer.enqueue(new MockResponse()
                    .setBody(objectMapper.writeValueAsString(ACCESS_TOKEN))
                    .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

            final String[] expect = {"배", "토마토", "사과", "바나나"};
            mockWebServer.enqueue(new MockResponse()
                    .setBody(objectMapper.writeValueAsString(expect))
                    .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

            final List<String> actual = productWebClient.getProducts(
                    ProductType.FRUIT.productTypeName());


            assertThat(actual).hasSize(4);
            assertThat(actual).contains(expect);
        }

        @DisplayName("품목으로 vegetable이 전달된 경우 채소 목록을 반환한다.")
        @Test
        void vegetableToProductNames() throws JsonProcessingException {
            mockWebServer.enqueue(new MockResponse()
                    .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .addHeader(HttpHeaders.SET_COOKIE, "Authorization=token"));

            final String[] expect = {"치커리", "토마토", "깻잎", "상추"};
            mockWebServer.enqueue(new MockResponse()
                    .setBody(objectMapper.writeValueAsString(expect))
                    .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

            final List<String> actual = productWebClient.getProducts(
                    ProductType.VEGETABLE.productTypeName());


            assertThat(actual).hasSize(4);
            assertThat(actual).contains(expect);
        }

        @DisplayName("지원되지 않는 품목이 전달된 경우 예외를 발생시킨다.")
        @Test
        void failCase() {
            final String notSupportedProductType = "고기";
            assertThatThrownBy(() -> productWebClient.getProducts(notSupportedProductType))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("서비스를 지원하지 않는 품목입니다.");
        }
    }
}