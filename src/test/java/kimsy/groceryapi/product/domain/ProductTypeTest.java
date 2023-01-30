package kimsy.groceryapi.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTypeTest {
    @DisplayName("지원되는 품목이 입력될 경우 그에 맞는 ProductType을 반환한다.")
    @ParameterizedTest(name = "입력={0}, {1}")
    @MethodSource("successCaseArguments")
    void success(String productTypeName, ProductType productType) {
        assertThat(ProductType.of(productTypeName)).isEqualTo(productType);
    }

    static Stream<Arguments> successCaseArguments() {
        return Stream.of(
                Arguments.of("fruit", ProductType.FRUIT),
                Arguments.of("vegetable", ProductType.VEGETABLE));
    }

    @DisplayName("지원되지 않는 품목이 입력될 경우 예외를 발생시킨다.")
    @ParameterizedTest(name = "입력={0}")
    @ValueSource(strings = {"meat", "fruitBox", "vegetableBall"})
    void failByNotSupportedProductType(String productTypeName) {
        assertThatThrownBy(() -> ProductType.of(productTypeName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("서비스를 지원하지 않는 품목입니다.");
    }

    @DisplayName("품목이 빈값이거나 null일 경우 예외를 발생시킨다.")
    @ParameterizedTest(name = "입력={0}")
    @NullAndEmptySource
    void failByNullOrEmpty(String productTypeName) {
        assertThatThrownBy(() -> ProductType.of(productTypeName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("서비스를 지원하지 않는 품목입니다.");
    }
}