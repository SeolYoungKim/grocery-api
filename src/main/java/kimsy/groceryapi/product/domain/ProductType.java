package kimsy.groceryapi.product.domain;

public enum ProductType {
    FRUIT("fruit", "/token", "/product", "name"),
    VEGETABLE("vegetable", "/token", "/item", "name");

    public static boolean isFruit(final String productType) {
        return FRUIT.productTypeName.equals(productType);
    }

    public static boolean isVegetable(final String productType) {
        return VEGETABLE.productTypeName.equals(productType);
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
