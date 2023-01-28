package kimsy.groceryapi.product.domain;

class AccessToken {
    private String accessToken;

    AccessToken() {
    }

    AccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    String accessToken() {
        return accessToken;
    }

    void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }
}
