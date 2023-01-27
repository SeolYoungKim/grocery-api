package kimsy.groceryapi.product.domain;

public class AccessToken {
    private String accessToken;

    public AccessToken() {
    }

    public AccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String accessToken() {
        return accessToken;
    }

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }
}
