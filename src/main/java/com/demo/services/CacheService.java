package com.demo.services;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface CacheService {

    <K, V> Map<K, V> fetchMapFromCache(String key, Class<K> keyClass, Class<V> valueClass);

    <K, V> void addMapToCache(String key, Map<K, V> cacheMap);

    void addCacheExpireTime(String key, final long timeout, final TimeUnit unit);

    <T> List<T> fetchListFromCache(String key, Class<T> type);

    <T> void addListToCache(String key, List<T> cacheList);

}
