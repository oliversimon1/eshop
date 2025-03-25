package com.oliver.eshop.domain.utils;

import java.util.function.Consumer;

public final class NullUtils {

    private NullUtils() {}

    public static <T> void ifNotNull(T value, Consumer<T> action) {
        if (value != null) {
            action.accept(value);
        }
    }
}
