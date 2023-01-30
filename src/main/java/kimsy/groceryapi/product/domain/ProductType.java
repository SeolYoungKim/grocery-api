package kimsy.groceryapi.product.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ProductType {
    FRUIT("fruit", "/token", "/product", "name"),
    VEGETABLE("vegetable", "/token", "/item", "name");

    private static final Map<String, ProductType> NAME_TYPE_MAP = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(
                    productType -> productType.productTypeName,
                    productType -> productType));

    public static ProductType of(String productTypeName) {
        if (!NAME_TYPE_MAP.containsKey(productTypeName)) {
            throw new IllegalArgumentException("서비스를 지원하지 않는 품목입니다.");
        }

        return NAME_TYPE_MAP.get(productTypeName);
    }

    private final String productTypeName;
    private final String tokenUri;
    private final String productUri;
    private final String priceParamKey;

    ProductType(final String productTypeName, final String tokenUri, final String productUri,
            final String priceParamKey) {
        this.productTypeName = productTypeName;
        this.tokenUri = tokenUri;
        this.productUri = productUri;
        this.priceParamKey = priceParamKey;
    }

    public String productTypeName() {
        return productTypeName;
    }

    public String tokenUri() {
        return tokenUri;
    }

    public String productUri() {
        return productUri;
    }

    public String priceParamKey() {
        return priceParamKey;
    }
}
