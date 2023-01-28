package kimsy.groceryapi.product.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class VegetableWebClient extends GroceryWebClient {
    @Autowired
    public VegetableWebClient(@Value("${api.url.vegetable}") String vegetableUrl) {
        super(vegetableUrl);
    }

    public VegetableWebClient(final String vegetableUrl, final AccessToken accessToken) {
        super(vegetableUrl, accessToken);
    }

    @Override
    String[] requestForProducts() {
        return webClient.get()
                .uri(ProductType.VEGETABLE.productUri())
                .header(HttpHeaders.AUTHORIZATION, accessToken.accessToken())
                .retrieve()
                .bodyToMono(String[].class)
                .block();
    }

    @Override
    AccessToken getToken() {
        return webClient.get()
                .uri(ProductType.VEGETABLE.tokenUri())
                .exchangeToMono(response -> {
                    final ResponseCookie responseCookie = response.cookies()
                            .getFirst(HttpHeaders.AUTHORIZATION);

                    if (responseCookie == null) {
                        throw new IllegalStateException("정보를 가져오는 데 실패했습니다.");
                    }

                    return Mono.fromSupplier(() -> new AccessToken(responseCookie.getValue()));
                }).block();
    }

    @Override
    Product requestForProduct(final String productName) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ProductType.VEGETABLE.productUri())
                        .queryParam("name", productName)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, accessToken.accessToken())
                .retrieve()
                .bodyToMono(Product.class)
                .block();
    }
}
