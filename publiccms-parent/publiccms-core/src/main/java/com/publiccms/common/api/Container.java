package com.publiccms.common.api;

import java.util.function.Supplier;

public interface Container<T> {
    Supplier<T> keyFunction();
}