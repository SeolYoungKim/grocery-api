package kimsy.groceryapi.product.domain.web_client;

import kimsy.groceryapi.product.domain.AccessToken;
import kimsy.groceryapi.product.domain.Product;
import kimsy.groceryapi.product.domain.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class MockFruitWebClient extends MockGroceryWebClient {
    @Autowired
    public MockFruitWebClient(@Value("${api.url.fruit}") String fruitUrl) {
        super(fruitUrl);
    }

    public MockFruitWebClient(final String fruitUrl, final AccessToken accessToken) {
        super(fruitUrl, accessToken);
    }

    @Override
    ResponseEntity<String[]> requestForProducts() {
        return webClient.get()
                .uri(ProductType.FRUIT.productUri())
                .header(HttpHeaders.AUTHORIZATION, accessToken.accessToken())
                .retrieve()
                .toEntity(String[].class)
                .block();
    }

    @Override
    ResponseEntity<AccessToken> getToken() {
        return webClient.get()
                .uri(ProductType.FRUIT.tokenUri())
                .retrieve()
                .toEntity(AccessToken.class)
                .block();
    }

    @Override
    ResponseEntity<Product> requestForProduct(final String productName) {
        validateNullOrEmpty(productName);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ProductType.FRUIT.productUri())
                        .queryParam(ProductType.FRUIT.priceParamKey(), productName)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, accessToken.accessToken())
                .retrieve()
                .toEntity(Product.class)
                .block();
    }
}
