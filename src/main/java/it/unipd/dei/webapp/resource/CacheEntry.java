
package it.unipd.dei.webapp.resource;

public final class CacheEntry<T> {
    private final T data;
    private final long cacheTime;
    private final long expirationTime;

    public CacheEntry(T data, long cacheTime, long expirationTime) {
        this.data = data;
        this.cacheTime = cacheTime;
        this.expirationTime = expirationTime;
    }

    public T getData() {
        return data;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public boolean isCacheExpired() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - this.getCacheTime();
        return elapsedTime > this.expirationTime;
    }
}
