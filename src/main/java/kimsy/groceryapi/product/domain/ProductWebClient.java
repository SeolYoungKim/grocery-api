package kimsy.groceryapi.product.domain;

import java.util.List;
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

    public List<String> getProducts(final String productType) {
        if (ProductType.isFruit(productType)) {
            return fruitClient.getProductNames();

        } else if (ProductType.isVegetable(productType)) {
            return vegetableWebClient.getProductNames();
        }

        throw new IllegalArgumentException("서비스를 지원하지 않는 품목입니다.");
    }


}
