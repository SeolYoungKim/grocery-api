package kimsy.groceryapi.product.domain.web_client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kimsy.groceryapi.product.domain.AccessToken;
import kimsy.groceryapi.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@SpringBootTest
class VegetableWebClientTest {
    @Autowired
    private MockVegetableWebClient mockVegetableWebClient;

    @DisplayName("외부 API에 토큰을 요청하고 성공하는 경우 200OK를 반환 받고, AccessToken 타입을 반환한다.")
    @Test
    void getToken() {
        final ResponseEntity<AccessToken> response = mockVegetableWebClient.getToken();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(AccessToken.class);
    }

    @DisplayName("외부 API에 상품 목록 요청을 보내고 성공하는 경우 200OK를 반환 받고, String[] 타입을 반환한다.")
    @Test
    void requestForProducts() {
        final ResponseEntity<String[]> response = mockVegetableWebClient.requestForProducts();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(String[].class);
    }

    @DisplayName("외부 API에 상품 가격 요청을 보내는 경우")
    @Nested
    class RequestForProduct {
        @DisplayName("성공할 경우 200OK를 반환 받고, Product 타입을 반환한다.")
        @Test
        void success() {
            final String productName = "치커리";
            final ResponseEntity<Product> response = mockVegetableWebClient
                    .requestForProduct(productName);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isInstanceOf(Product.class);
        }

        @DisplayName("없는 품목을 요청할 경우 예외를 발생시킨다.")
        @ParameterizedTest(name = "입력={0}")
        @ValueSource(strings = {"사과", "배", "바나나"})
        void failByNotSupportedProductName(String productName) {
            assertThatThrownBy(() -> mockVegetableWebClient.requestForProduct(productName))
                    .isInstanceOf(WebClientResponseException.NotFound.class);
        }

        @DisplayName("null 혹은 빈 값을 요청할 경우 예외를 발생시킨다.")
        @ParameterizedTest(name = "입력={0}")
        @NullAndEmptySource
        void failByNullOrEmptyProductName(String productName) {
            assertThatThrownBy(() -> mockVegetableWebClient.requestForProduct(productName))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}