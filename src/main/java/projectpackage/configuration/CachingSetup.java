package projectpackage.configuration;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.stereotype.Component;

import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.TouchedExpiryPolicy;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lenovo on 23.04.2017.
 */
@Component
public class CachingSetup implements JCacheManagerCustomizer {
    @Override
    public void customize(javax.cache.CacheManager cacheManager) {
        cacheManager.createCache("rooms", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 10))).setStoreByValue(false).setStatisticsEnabled(false));
    }
}
