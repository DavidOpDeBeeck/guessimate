package app.dodb.guessimate.session.utils;

import java.util.Map;
import java.util.function.Function;

import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;

public class MapUtils {

    public static <K1, V1, K2, V2> Map<K2, V2> transform(Map<K1, V1> initial, Function<K1, K2> keyTransformer, Function<V1, V2> valueTransformer) {
        return initial.entrySet().stream()
            .map(entry -> entry(keyTransformer.apply(entry.getKey()), valueTransformer.apply(entry.getValue())))
            .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
