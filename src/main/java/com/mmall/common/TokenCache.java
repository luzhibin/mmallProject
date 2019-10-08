package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**java日志框架
 * log4j
 * commons-logging
 * jdk-logging
 * slf4j
 * logback
 * log4j2
 * */
//存放Token缓存
public class TokenCache {

    //声明日志,slf4j的日志记录器

    //todo
    private static final Logger logger = LoggerFactory.getLogger(TokenCache.class);

    public static final String TOKEN_PREFIX = "token_";

    /** guava本地缓存：由于本地缓存是将计算结果缓存到内存中，所以我们往往需要设置一个最大容量来防止出现内存溢出的情况。
     * 这个容量可以是缓存对象的数量，也可以是一个具体的内存大小。在Guava中仅支持设置缓存对象的数量。
     * 以下使用Guava cache构建本地缓存
     * 当缓存的最大数量/容量逼近或超过我们所设置的最大值时，Guava就会使用LRU算法对之前的缓存进行回收。
     * 采用算法：LRU算法(最近最少使用)，核心思想：“如果数据最近被访问过，那么将来被访问的几率也更高”
     * */
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder()
            .initialCapacity(1000)  //初始化缓存，1000表示缓存的初始化容量
            .maximumSize(10000)     //设置缓存的最大容量
            .expireAfterAccess(12, TimeUnit.HOURS)  //设置缓存在写入12小时后失效
            .build(new CacheLoader<String, String>() {      //放入缓存
        //该方法是默认的数据加载实现，当调用get取值的时候，如果key没有对应的值，就调用这个load方法进行加载
        @Override
        public String load(String s) throws Exception {
            return "null";
        }
    });

    public static void setKey(String key,String value){
        localCache.put(key,value);
    }
    public static String getKey(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value)){
                return null;
            }
            return value;
        }catch (Exception e){
            logger.error("localCache get error",e);
        }
        return null;
    }
}
