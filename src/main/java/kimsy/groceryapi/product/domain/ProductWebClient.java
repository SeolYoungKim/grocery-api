package kimsy.groceryapi.product.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Component
public class ProductWebClient {
    private final FruitWebClient fruitClient;
    private final VegetableWebClient vegetableWebClient;

    public Products getProducts(final String productType) {
        if (ProductType.isFruit(productType)) {
            return fruitClient.getProducts();

        } else if (ProductType.isVegetable(productType)) {
            return vegetableWebClient.getProducts();
        }

        throw new IllegalArgumentException("서비스를 지원하지 않는 품목입니다.");
    }

    public Product getProduct(final String productType, final String productName) {
        validate(productName);

        if (ProductType.isFruit(productType)) {
            return fruitClient.getPrice(productName);

        } else if (ProductType.isVegetable(productType)) {
            return vegetableWebClient.getPrice(productName);
        }

        throw new IllegalArgumentException("서비스를 지원하지 않는 품목입니다.");
    }

    private void validate(final String productName) {
        if (!StringUtils.hasText(productName)) {
            throw new IllegalArgumentException("품목명을 입력해 주세요.");
        }
    }
}
