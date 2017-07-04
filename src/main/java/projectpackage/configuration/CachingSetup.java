package projectpackage.configuration;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.stereotype.Component;

import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.TouchedExpiryPolicy;
import java.util.concurrent.TimeUnit;

@Component
public class CachingSetup implements JCacheManagerCustomizer {
    @Override
    public void customize(javax.cache.CacheManager cacheManager) {
        cacheManager.createCache("userList", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 20))).setStoreByValue(false).setStatisticsEnabled(false));
        cacheManager.createCache("blockList", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 20))).setStoreByValue(false).setStatisticsEnabled(false));
        cacheManager.createCache("complimentaryList", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 20))).setStoreByValue(false).setStatisticsEnabled(false));
        cacheManager.createCache("journalRecordList", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 20))).setStoreByValue(false).setStatisticsEnabled(false));
        cacheManager.createCache("maintenanceList", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 20))).setStoreByValue(false).setStatisticsEnabled(false));
        cacheManager.createCache("modificationHistoryList", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 20))).setStoreByValue(false).setStatisticsEnabled(false));
        cacheManager.createCache("notificationList", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 20))).setStoreByValue(false).setStatisticsEnabled(false));
        cacheManager.createCache("notificationTypeList", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 20))).setStoreByValue(false).setStatisticsEnabled(false));
        cacheManager.createCache("orderList", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 20))).setStoreByValue(false).setStatisticsEnabled(false));
        cacheManager.createCache("phoneList", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 20))).setStoreByValue(false).setStatisticsEnabled(false));
        cacheManager.createCache("priceList", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 20))).setStoreByValue(false).setStatisticsEnabled(false));
        cacheManager.createCache("rateList", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 20))).setStoreByValue(false).setStatisticsEnabled(false));
        cacheManager.createCache("roleList", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 60))).setStoreByValue(false).setStatisticsEnabled(false));
        cacheManager.createCache("roomList", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 60))).setStoreByValue(false).setStatisticsEnabled(false));
        cacheManager.createCache("simpleRoomList", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 60))).setStoreByValue(false).setStatisticsEnabled(false));
        cacheManager.createCache("roomTypeList", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 60))).setStoreByValue(false).setStatisticsEnabled(false));
        cacheManager.createCache("simpleRoomTypeList", new MutableConfiguration<>().setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 60))).setStoreByValue(false).setStatisticsEnabled(false));
    }
}
