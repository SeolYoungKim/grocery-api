package kimsy.groceryapi.product.domain.web_client;

import kimsy.groceryapi.product.domain.AccessToken;
import kimsy.groceryapi.product.domain.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;


public abstract class MockGroceryWebClient {
    protected final WebClient webClient;
    protected AccessToken accessToken;

    public MockGroceryWebClient(final String url) {
        this.webClient = WebClient.builder().baseUrl(url).build();
        this.accessToken = getToken().getBody();
    }

    public MockGroceryWebClient(final String url, final AccessToken accessToken) {
        this.webClient = WebClient.builder().baseUrl(url).build();
        this.accessToken = accessToken;
    }

    abstract ResponseEntity<AccessToken> getToken();
    abstract ResponseEntity<String[]> requestForProducts();
    abstract ResponseEntity<Product> requestForProduct(String productName);

    protected void validateNullOrEmpty(final String productName) {
        if (!StringUtils.hasText(productName)) {
            throw new IllegalArgumentException("품목명을 입력해 주세요.");
        }
    }
}
