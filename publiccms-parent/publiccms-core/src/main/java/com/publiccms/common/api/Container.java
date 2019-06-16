package com.publiccms.common.api;

import java.util.function.Supplier;

public interface Container<T> {
    public Supplier<T> keyFunction();
}