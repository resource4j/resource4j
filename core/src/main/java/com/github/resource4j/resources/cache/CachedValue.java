package com.github.resource4j.resources.cache;

public final class CachedValue<T> {

    private final T value;

    private final boolean missing;

    public static <T> CachedValue<T> cached(T value) {
        return new CachedValue<>(value);
    }

    public static <T> CachedValue<T> missingValue(Class<T> type) {
        return new CachedValue<>();
    }

    protected CachedValue() {
        this.value = null;
        this.missing = true;
    }

    protected CachedValue(T value) {
        this.value = value;
        this.missing = false;
    }

    public T get() {
        return value;
    }

    public boolean isMissing() {
        return missing;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (missing ? 1231 : 1237);
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CachedValue<?> other = (CachedValue<?>) obj;
        if (missing != other.missing) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.missing ? "<missing>" : (this.value == null ? "<null>" : String.valueOf(this.value));
    }

}
