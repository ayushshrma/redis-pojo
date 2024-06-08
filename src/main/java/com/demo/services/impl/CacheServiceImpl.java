package com.demo.services.impl;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

import com.demo.services.CacheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public <K, V> Map<K, V> fetchMapFromCache(String key, Class<K> keyClass, Class<V> valueClass) {
        Map<Object, Object> mapOfObjects = redisTemplate.opsForHash().entries(key);
        return mapOfObjects.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> objectMapper.convertValue(entry.getKey(), keyClass),
                        entry -> objectMapper.convertValue(entry.getValue(), valueClass)));
    }

    @Override
    public <K, V> void addMapToCache(String key, Map<K, V> cacheMap) {
        redisTemplate.opsForHash().putAll(key, cacheMap);
    }

    @Override
    public void addCacheExpireTime(String key, final long timeout, final TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public <T> List<T> fetchListFromCache(String key, Class<T> type) {
        List<Object> listOfObject = redisTemplate.opsForList().range(key, 0, -1);
        if (listOfObject == null) {
            return emptyList();
        }
        return listOfObject.stream()
                .map(obj -> objectMapper.convertValue(obj, type))
                .collect(toList());
    }

    @Override
    public <T> void addListToCache(String key, List<T> cacheList) {
        redisTemplate.opsForList().rightPushAll(key, cacheList);
    }

}
