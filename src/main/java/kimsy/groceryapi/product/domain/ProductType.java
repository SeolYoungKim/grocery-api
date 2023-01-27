package kimsy.groceryapi.product.domain;

public enum ProductType {
    FRUIT("fruit", "/token", "/product"),
    VEGETABLE("vegetable", "/token", "/item");

    public static boolean isFruit(final String productType) {
        return FRUIT.productTypeName.equals(productType);
    }

    public static boolean isVegetable(final String productType) {
        return VEGETABLE.productTypeName.equals(productType);
    }

    private final String productTypeName;
    private final String tokenUri;
    private final String productUri;

    ProductType(final String productTypeName, final String tokenUri, final String productUri) {
        this.productTypeName = productTypeName;
        this.tokenUri = tokenUri;
        this.productUri = productUri;
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
}
