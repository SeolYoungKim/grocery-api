package kimsy.groceryapi.product.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessToken {
    private String accessToken;

    public AccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    String accessToken() {
        return accessToken;
    }

    void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }
}
