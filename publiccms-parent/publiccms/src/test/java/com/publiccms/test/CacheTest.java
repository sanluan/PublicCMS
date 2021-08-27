package com.publiccms.test;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;

@DisplayName("cache test case")
public class CacheTest {
    private static ExecutorService pool = Executors.newFixedThreadPool(10);

    @Test
    @DisplayName("generate category childIds test case")
    public void main() {
        try {
            CacheEntityFactory bean = new CacheEntityFactory("config/cache.properties");
            CacheEntity<String, String> wordCache = bean.createCacheEntity("word");
            pool.execute(new TestTask(wordCache));
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}

class TestTask implements Runnable {
    CacheEntity<String, String> wordCache;

    public TestTask(CacheEntity<String, String> wordCache) {
        this.wordCache = wordCache;
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    @Override
    public void run() {
        for (int i = 1; i <= 100000; i++) {
            List<String> aa = wordCache.put(getRandomString(20), getRandomString(20));
            if (null != aa) {
                System.out.println(aa.size());
            }
        }
    }

}