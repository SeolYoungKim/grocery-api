package kimsy.groceryapi.config;

import java.util.Map;
import kimsy.groceryapi.product.domain.ProductType;
import kimsy.groceryapi.product.domain.web_client.FruitWebClient;
import kimsy.groceryapi.product.domain.web_client.GroceryWebClient;
import kimsy.groceryapi.product.domain.GroceryWebClientMapper;
import kimsy.groceryapi.product.domain.web_client.VegetableWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ProductWebClientConfig {
    private final FruitWebClient fruitWebClient;
    private final VegetableWebClient vegetableWebClient;

    @Bean
    public GroceryWebClientMapper groceryWebClientMap() {
        final Map<String, GroceryWebClient> map = Map.of(
                ProductType.FRUIT.productTypeName(), fruitWebClient,
                ProductType.VEGETABLE.productTypeName(), vegetableWebClient);

        return new GroceryWebClientMapper(map);
    }
}
