package org.springframework.core;

import java.io.IOException;

import org.springframework.lang.Nullable;

/**
 * NestedIOException spring6
 * 
 */
public class NestedIOException extends IOException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    static {
        NestedExceptionUtils.class.getName();
    }

    /**
     * Construct a {@code NestedIOException} with the specified detail message.
     * 
     * @param msg
     *            the detail message
     */
    public NestedIOException(String msg) {
        super(msg);
    }

    /**
     * Construct a {@code NestedIOException} with the specified detail message
     * and nested exception.
     * 
     * @param msg
     *            the detail message
     * @param cause
     *            the nested exception
     */
    public NestedIOException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    /**
     * Return the detail message, including the message from the nested
     * exception if there is one.
     */
    @Override
    @Nullable
    public String getMessage() {
        if (getCause() == null) {
            return super.getMessage();
        }
        StringBuilder sb = new StringBuilder(64);
        if (super.getMessage() != null) {
            sb.append(super.getMessage()).append("; ");
        }
        sb.append("nested exception is ").append(getCause());
        return sb.toString();
    }
}
