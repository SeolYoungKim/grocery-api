package kimsy.groceryapi.product.domain;

import org.springframework.stereotype.Component;

@Component
public class ProductWebClient {
    private final FruitWebClient fruitClient;
    private final VegetableWebClient vegetableWebClient;

    public ProductWebClient(final FruitWebClient fruitClient,
            final VegetableWebClient vegetableWebClient) {
        this.fruitClient = fruitClient;
        this.vegetableWebClient = vegetableWebClient;
    }

    public Products getProducts(final String productType) {
        if (ProductType.isFruit(productType)) {
            return fruitClient.getProducts();

        } else if (ProductType.isVegetable(productType)) {
            return vegetableWebClient.getProducts();
        }

        throw new IllegalArgumentException("서비스를 지원하지 않는 품목입니다.");
    }
}
