package kimsy.groceryapi.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import kimsy.groceryapi.product.MockWebServerTest;
import kimsy.groceryapi.product.domain.web_client.FruitWebClient;
import kimsy.groceryapi.product.domain.web_client.VegetableWebClient;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class GroceryWebClientMapperTest extends MockWebServerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private GroceryWebClientMapper groceryWebClientMapper;

    @BeforeEach
    void setUp() {
        final String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        final AccessToken token = new AccessToken("token");
        final FruitWebClient fruitWebClient = new FruitWebClient(baseUrl, token);
        final VegetableWebClient vegetableWebClient = new VegetableWebClient(baseUrl, token);

        groceryWebClientMapper = new GroceryWebClientMapper(Map.of(
                ProductType.FRUIT.productTypeName(), fruitWebClient,
                ProductType.VEGETABLE.productTypeName(), vegetableWebClient));
    }


    @DisplayName("청과물 분류 별 전체 품목을 조회하는 기능")
    @Nested
    class GetProducts {

        @DisplayName("ProductType으로 fruit이 전달된 경우 과일 목록을 반환한다.")
        @Test
        void fruitToProductNames() throws JsonProcessingException {
            final String[] expect = {"배", "토마토", "사과", "바나나"};
            mockWebServer.enqueue(new MockResponse()
                    .setBody(objectMapper.writeValueAsString(expect))
                    .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

            final List<String> actual = groceryWebClientMapper.getProducts(
                    ProductType.FRUIT.productTypeName()).values();

            assertThat(actual).hasSize(expect.length);
            assertThat(actual).contains(expect);
        }

        @DisplayName("ProductType으로 vegetable이 전달된 경우 채소 목록을 반환한다.")
        @Test
        void vegetableToProductNames() throws JsonProcessingException {
            final String[] expect = {"치커리", "토마토", "깻잎", "상추"};
            mockWebServer.enqueue(new MockResponse()
                    .setBody(objectMapper.writeValueAsString(expect))
                    .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

            final List<String> actual = groceryWebClientMapper.getProducts(
                    ProductType.VEGETABLE.productTypeName()).values();


            assertThat(actual).hasSize(expect.length);
            assertThat(actual).contains(expect);
        }

        @DisplayName("지원되지 않는 품목이 전달된 경우 예외를 발생시킨다.")
        @Test
        void failCase() {
            final String notSupportedProductType = "고기";
            assertThatThrownBy(() -> groceryWebClientMapper.getProducts(notSupportedProductType))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("서비스를 지원하지 않는 품목입니다.");
        }
    }

    @DisplayName("청과물 개별 품목의 가격 조회기능")
    @Nested
    class GetProduct {
        @DisplayName("productType=fruit, productName=배 가 전달된 경우 배의 가격을 반환한다.")
        @Test
        void searchFruitPair() throws JsonProcessingException {
            final String productName = "배";
            final int productPrice = 3000;
            mockWebServer.enqueue(new MockResponse()
                    .setBody(objectMapper.writeValueAsString(Map.of(
                            "name", productName,
                            "price", productPrice)))
                    .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

            final Product product = groceryWebClientMapper.getProduct(
                    ProductType.FRUIT.productTypeName(), productName);

            assertThat(product.name()).isEqualTo(productName);
            assertThat(product.price()).isEqualTo(productPrice);
        }

        @DisplayName("productType=vegetable, productName=깻잎 이 전달된 경우 깻잎의 가격을 반환한다.")
        @Test
        void searchVegetableSesame() throws JsonProcessingException {
            final String productName = "깻잎";
            final int productPrice = 1500;
            mockWebServer.enqueue(new MockResponse()
                    .setBody(objectMapper.writeValueAsString(Map.of(
                            "name", productName,
                            "price", productPrice)))
                    .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

            final Product product = groceryWebClientMapper.getProduct(
                    ProductType.VEGETABLE.productTypeName(), productName);

            assertThat(product.name()).isEqualTo(productName);
            assertThat(product.price()).isEqualTo(productPrice);
        }

        @DisplayName("지원되지 않는 품목이 전달된 경우 예외를 발생시킨다.")
        @Test
        void failCase() {
            final String notSupportedType = "고기";
            final String notSupportedName = "한우";
            assertThatThrownBy(() -> groceryWebClientMapper.getProduct(notSupportedType, notSupportedName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("서비스를 지원하지 않는 품목입니다.");
        }
    }
}