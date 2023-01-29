package kimsy.groceryapi.product.domain.web_client;

import kimsy.groceryapi.product.domain.AccessToken;
import kimsy.groceryapi.product.domain.Product;
import kimsy.groceryapi.product.domain.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class FruitWebClient extends GroceryWebClient {
    @Autowired
    public FruitWebClient(@Value("${api.url.fruit}") String fruitUrl) {
        super(fruitUrl);
    }

    public FruitWebClient(final String fruitUrl, final AccessToken accessToken) {
        super(fruitUrl, accessToken);
    }

    @Override
    String[] requestForProducts() {
        return webClient.get()
                .uri(ProductType.FRUIT.productUri())
                .header(HttpHeaders.AUTHORIZATION, accessToken.accessToken())
                .retrieve()
                .bodyToMono(String[].class)
                .block();
    }

    @Override
    AccessToken getToken() {
        return webClient.get()
                .uri(ProductType.FRUIT.tokenUri())
                .retrieve()
                .bodyToMono(AccessToken.class)
                .block();
    }

    @Override
    Product requestForProduct(final String productName) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ProductType.FRUIT.productUri())
                        .queryParam(ProductType.FRUIT.priceParamKey(), productName)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, accessToken.accessToken())
                .retrieve()
                .bodyToMono(Product.class)
                .block();
    }
}
