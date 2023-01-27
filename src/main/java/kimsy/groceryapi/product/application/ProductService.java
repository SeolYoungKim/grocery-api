package kimsy.groceryapi.product.application;

import java.util.List;
import kimsy.groceryapi.product.application.dto.ProductsResponse;
import kimsy.groceryapi.product.domain.ProductWebClient;
import kimsy.groceryapi.product.presentation.dto.ProductsSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductWebClient productWebClient;

    public ProductsResponse getProducts(final ProductsSearchRequest productsSearchRequest) {
        final List<String> product = productWebClient.getProducts(productsSearchRequest.productType());
        return new ProductsResponse(product);
    }
}
