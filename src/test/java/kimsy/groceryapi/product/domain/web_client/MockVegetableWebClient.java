package kimsy.groceryapi.product.domain.web_client;

import kimsy.groceryapi.product.domain.AccessToken;
import kimsy.groceryapi.product.domain.Product;
import kimsy.groceryapi.product.domain.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MockVegetableWebClient extends MockGroceryWebClient {
    @Autowired
    public MockVegetableWebClient(@Value("${api.url.vegetable}") String vegetableUrl) {
        super(vegetableUrl);
    }

    public MockVegetableWebClient(final String vegetableUrl, final AccessToken accessToken) {
        super(vegetableUrl, accessToken);
    }

    @Override
    ResponseEntity<String[]> requestForProducts() {
        return webClient.get()
                .uri(ProductType.VEGETABLE.productUri())
                .header(HttpHeaders.AUTHORIZATION, accessToken.accessToken())
                .retrieve()
                .toEntity(String[].class)
                .block();
    }

    @Override
    ResponseEntity<AccessToken> getToken() {
        return webClient.get()
                .uri(ProductType.VEGETABLE.tokenUri())
                .exchangeToMono(response -> {
                    final ResponseCookie responseCookie = response.cookies()
                            .getFirst(HttpHeaders.AUTHORIZATION);

                    if (responseCookie == null) {
                        throw new IllegalStateException("정보를 가져오는 데 실패했습니다.");
                    }
                    return Mono.just(new ResponseEntity<>(
                            new AccessToken(responseCookie.getValue()), response.statusCode()));
                }).block();
    }

    @Override
    ResponseEntity<Product> requestForProduct(final String productName) {
        validateNullOrEmpty(productName);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ProductType.VEGETABLE.productUri())
                        .queryParam(ProductType.VEGETABLE.priceParamKey(), productName)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, accessToken.accessToken())
                .retrieve()
                .toEntity(Product.class)
                .block();
    }
}
