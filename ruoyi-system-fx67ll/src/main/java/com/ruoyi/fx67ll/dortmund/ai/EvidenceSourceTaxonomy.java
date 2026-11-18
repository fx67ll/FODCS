package com.ruoyi.fx67ll.dortmund.ai;

import java.net.URI;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 证据来源层级与新鲜度分类。
 * <p>
 * 分类只使用供应商无关的URL、来源名称和时间字段；模型不得自行猜测来源层级。
 *
 * @author fx67ll
 * @date 2026-11-17
 */
public final class EvidenceSourceTaxonomy {
    public static final String TIER_OFFICIAL = "OFFICIAL";

    public static final String TIER_MAJOR_MEDIA = "MAJOR_MEDIA";

    public static final String TIER_SPECIALIST = "SPECIALIST";

    public static final String TIER_AGGREGATOR = "AGGREGATOR";

    public static final String TIER_UNKNOWN = "UNKNOWN";

    public static final String FRESHNESS_CURRENT = "CURRENT";

    public static final String FRESHNESS_RECENT = "RECENT";

    public static final String FRESHNESS_STALE = "STALE";

    public static final String FRESHNESS_UNKNOWN = "UNKNOWN";

    private static final Set<String> SOURCE_TIERS = new LinkedHashSet<>();

    private static final Set<String> OFFICIAL_DOMAINS = new LinkedHashSet<>();

    private static final Set<String> MAJOR_MEDIA_DOMAINS = new LinkedHashSet<>();

    private static final Set<String> SPECIALIST_DOMAINS = new LinkedHashSet<>();

    private static final Set<String> AGGREGATOR_DOMAINS = new LinkedHashSet<>();

    static {
        SOURCE_TIERS.add(TIER_OFFICIAL);
        SOURCE_TIERS.add(TIER_MAJOR_MEDIA);
        SOURCE_TIERS.add(TIER_SPECIALIST);
        SOURCE_TIERS.add(TIER_AGGREGATOR);
        SOURCE_TIERS.add(TIER_UNKNOWN);

        OFFICIAL_DOMAINS.add("fifa.com");
        OFFICIAL_DOMAINS.add("uefa.com");
        OFFICIAL_DOMAINS.add("bundesliga.com");
        OFFICIAL_DOMAINS.add("premierleague.com");
        OFFICIAL_DOMAINS.add("laliga.com");
        OFFICIAL_DOMAINS.add("legaseriea.it");
        OFFICIAL_DOMAINS.add("ligue1.com");
        OFFICIAL_DOMAINS.add("jleague.jp");
        OFFICIAL_DOMAINS.add("thefa.com");
        OFFICIAL_DOMAINS.add("dfb.de");
        OFFICIAL_DOMAINS.add("thecfa.cn");

        MAJOR_MEDIA_DOMAINS.add("bbc.com");
        MAJOR_MEDIA_DOMAINS.add("bbc.co.uk");
        MAJOR_MEDIA_DOMAINS.add("espn.com");
        MAJOR_MEDIA_DOMAINS.add("skysports.com");
        MAJOR_MEDIA_DOMAINS.add("theathletic.com");
        MAJOR_MEDIA_DOMAINS.add("reuters.com");
        MAJOR_MEDIA_DOMAINS.add("apnews.com");
        MAJOR_MEDIA_DOMAINS.add("xinhuanet.com");
        MAJOR_MEDIA_DOMAINS.add("people.com.cn");
        MAJOR_MEDIA_DOMAINS.add("cctv.com");
        MAJOR_MEDIA_DOMAINS.add("sina.com.cn");
        MAJOR_MEDIA_DOMAINS.add("163.com");
        MAJOR_MEDIA_DOMAINS.add("qq.com");

        SPECIALIST_DOMAINS.add("transfermarkt.com");
        SPECIALIST_DOMAINS.add("transfermarkt.de");
        SPECIALIST_DOMAINS.add("whoscored.com");
        SPECIALIST_DOMAINS.add("sofascore.com");
        SPECIALIST_DOMAINS.add("flashscore.com");
        SPECIALIST_DOMAINS.add("soccerway.com");
        SPECIALIST_DOMAINS.add("understat.com");
        SPECIALIST_DOMAINS.add("fbref.com");
        SPECIALIST_DOMAINS.add("besoccer.com");
        SPECIALIST_DOMAINS.add("dongqiudi.com");

        AGGREGATOR_DOMAINS.add("news.google.com");
        AGGREGATOR_DOMAINS.add("baidu.com");
        AGGREGATOR_DOMAINS.add("toutiao.com");
        AGGREGATOR_DOMAINS.add("msn.com");
        AGGREGATOR_DOMAINS.add("yahoo.com");
        AGGREGATOR_DOMAINS.add("bing.com");
        AGGREGATOR_DOMAINS.add("so.com");
    }

    private EvidenceSourceTaxonomy() {
    }

    /**
     * 判断来源层级是否属于稳定枚举。
     */
    public static boolean isValidTier(String tier) {
        return tier != null && SOURCE_TIERS.contains(tier.trim().toUpperCase(Locale.ROOT));
    }

    /**
     * 规范化来源层级；未知值统一降级为UNKNOWN。
     */
    public static String normalizeTier(String tier) {
        if (!isValidTier(tier)) {
            return TIER_UNKNOWN;
        }
        return tier.trim().toUpperCase(Locale.ROOT);
    }

    /**
     * 根据URL域名和来源名称推断来源层级。
     * <p>
     * 未命中配置时返回UNKNOWN，不根据正文内容猜测可信度。
     */
    public static String classifySourceTier(String sourceUrl, String sourceName) {
        String host = extractHost(sourceUrl);
        if (host == null) {
            return TIER_UNKNOWN;
        }
        if (host.endsWith(".gov") || host.endsWith(".gov.cn") || host.endsWith(".edu")
                || host.endsWith(".edu.cn") || matchesDomain(host, OFFICIAL_DOMAINS)) {
            return TIER_OFFICIAL;
        }
        if (matchesDomain(host, MAJOR_MEDIA_DOMAINS)) {
            return TIER_MAJOR_MEDIA;
        }
        if (matchesDomain(host, SPECIALIST_DOMAINS)) {
            return TIER_SPECIALIST;
        }
        if (matchesDomain(host, AGGREGATOR_DOMAINS)) {
            return TIER_AGGREGATOR;
        }
        return TIER_UNKNOWN;
    }

    /**
     * 根据证据时间和业务快照截止时间计算新鲜度。
     */
    public static String classifyFreshness(Date publishedTime, Date retrievedTime, Date dataAsOf) {
        if (dataAsOf == null) {
            return FRESHNESS_UNKNOWN;
        }
        Date reference = publishedTime != null ? publishedTime : retrievedTime;
        if (reference == null) {
            return FRESHNESS_UNKNOWN;
        }
        long ageMillis = dataAsOf.getTime() - reference.getTime();
        if (ageMillis < -TimeUnit.DAYS.toMillis(1)) {
            return FRESHNESS_UNKNOWN;
        }
        if (ageMillis <= TimeUnit.DAYS.toMillis(7)) {
            return FRESHNESS_CURRENT;
        }
        if (ageMillis <= TimeUnit.DAYS.toMillis(30)) {
            return FRESHNESS_RECENT;
        }
        return FRESHNESS_STALE;
    }

    private static String extractHost(String sourceUrl) {
        if (sourceUrl == null || sourceUrl.trim().isEmpty()) {
            return null;
        }
        try {
            URI uri = URI.create(sourceUrl.trim());
            String host = uri.getHost();
            return host == null ? null : host.toLowerCase(Locale.ROOT);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static boolean matchesDomain(String host, Set<String> domains) {
        for (String domain : domains) {
            if (host.equals(domain) || host.endsWith("." + domain)) {
                return true;
            }
        }
        return false;
    }
}
