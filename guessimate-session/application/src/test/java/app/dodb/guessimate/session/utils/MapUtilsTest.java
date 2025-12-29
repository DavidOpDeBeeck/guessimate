package app.dodb.guessimate.session.utils;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MapUtilsTest {

    @Test
    void transform() {
        var transformed = MapUtils.transform(Map.of(1, 2), Object::toString, Object::toString);

        assertThat(transformed)
            .containsExactly(Map.entry("1", "2"));
    }
}