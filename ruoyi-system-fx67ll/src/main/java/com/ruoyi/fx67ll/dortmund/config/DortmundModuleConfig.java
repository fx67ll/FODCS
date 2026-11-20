package com.ruoyi.fx67ll.dortmund.config;

import java.math.BigDecimal;

/**
 * Dortmund 模块业务配置基线。
 * <p>
 * 这里集中维护由业务方案定义、且不存放在字典表中的固定值。默认值面向生产使用并保留必要冗余；
 * 字典编码、数据库表结构、供应商地址/密钥、纯视觉样式和框架常量不在此维护。
 * <p>
 * 修改分析池、候选腿数、超时、Token、租约、轮询或文本长度时，应同步评估请求校验、AI 提示词、
 * 前端选择器和页面轮询；23 场分析池与单候选最多 8 腿是两个独立边界，禁止复用同一常量。
 *
 * @author fx67ll
 * @date 2026-11-20
 */
public final class DortmundModuleConfig {
    private DortmundModuleConfig() {
    }

    /**
     * 回报组合业务边界与候选数量。
     */
    public static final class Return {
        /** 一次分析至少选择的比赛数。 */
        public static final int MIN_ANALYSIS_MATCHES = 1;
        /** 一次分析允许选择的比赛池上限；只代表可选空间，不代表串关腿数。 */
        public static final int MAX_ANALYSIS_MATCHES = 23;
        /** 单条候选至少包含的比赛数。 */
        public static final int MIN_COMBINATION_MATCHES = 1;
        /** 单条候选最多包含的比赛数，对应八串一限制。 */
        public static final int MAX_COMBINATION_MATCHES = 8;
        /** 自动 AI 响应的解析保护上限，高于最终落库数量以避免模型轻微超量直接丢弃整批结果。 */
        public static final int MAX_AUTO_PARSED_CANDIDATES = 50;
        /** 单个批次最终保存并展示的候选数量。 */
        public static final int MAX_PERSISTED_CANDIDATES = 10;
        /** 人工 AI 1.0 契约允许的候选数量。 */
        public static final int MAX_MANUAL_CANDIDATES = 10;
        /** 批量采用接口一次允许提交的组合数量。 */
        public static final int MAX_BATCH_ADOPT_COUNT = 100;
        /** 客户端幂等请求号落库长度。 */
        public static final int MAX_CLIENT_REQUEST_ID_LENGTH = 64;
        /** 置信度取值范围上限。 */
        public static final int MAX_CONFIDENCE = 100;
        /** 证据权重影响的下界。 */
        public static final int MIN_WEIGHT_IMPACT = -100;
        /** 证据权重影响的上界。 */
        public static final int MAX_WEIGHT_IMPACT = 100;
        /** 批次快照结构版本。 */
        public static final String SNAPSHOT_VERSION = "2.2";
    }

    /**
     * 自动 AI 模型调用与输出预算。
     */
    public static final class Ai {
        /** 最终分析输出 Token；为 23 场上下文、10 条候选和完整证据解释预留空间。 */
        public static final int ANALYSIS_MAX_TOKENS = 32768;
        /** 证据规划输出 Token；覆盖最多 23 条有界查询。 */
        public static final int PLANNER_MAX_TOKENS = 12288;
        /** 最终分析阶段预算（秒）。 */
        public static final int ANALYSIS_TIMEOUT_SECONDS = 240;
        /** 最终分析阶段预算（毫秒），供调用契约直接使用。 */
        public static final long ANALYSIS_TIMEOUT_MILLIS = ANALYSIS_TIMEOUT_SECONDS * 1000L;
        /** 证据规划阶段预算（秒）。 */
        public static final int PLAN_TIMEOUT_SECONDS = 90;
        /** 证据规划阶段预算（毫秒），供调用契约直接使用。 */
        public static final long PLAN_TIMEOUT_MILLIS = PLAN_TIMEOUT_SECONDS * 1000L;
        /** 模型网关连接超时（秒）。 */
        public static final int CONNECT_TIMEOUT_SECONDS = 10;
        /** 部署默认读取超时（秒）；调用阶段预算优先。 */
        public static final int DEFAULT_READ_TIMEOUT_SECONDS = 240;
        /** 最终分析温度，低温度降低契约漂移。 */
        public static final double ANALYSIS_TEMPERATURE = 0.2D;
        /** 规划温度，进一步降低查询计划漂移。 */
        public static final double PLANNER_TEMPERATURE = 0.1D;
        /** 按读取超时缓存的客户端上限，防止动态预算造成无界增长。 */
        public static final int MAX_CACHED_REST_TEMPLATES = 8;
        /** AI 调用引用长度，与数据库 ai_*_run_ref varchar(128) 对齐。 */
        public static final int MAX_AI_RUN_REF_LENGTH = 128;
        /** 异常日志响应正文上限。 */
        public static final int MAX_LOG_RESPONSE_CHARS = 500;
    }

    /**
     * 受控证据检索边界。
     */
    public static final class Retrieval {
        /** 单次规划允许的最大查询数。与分析比赛池独立配置，便于分别收紧检索成本。 */
        public static final int MAX_QUERIES = 23;
        /** 单条查询检索结果上限，为重复、空正文和低质量来源预留筛除空间。 */
        public static final int MAX_RESULTS_PER_QUERY = 8;
        /** 单条检索默认读取超时（秒）。 */
        public static final int DEFAULT_TIMEOUT_SECONDS = 30;
        /** 读取超时下限（秒），避免误配过小值中断正常检索。 */
        public static final int MIN_TIMEOUT_SECONDS = 10;
        /** 检索网关连接超时（秒）。 */
        public static final int CONNECT_TIMEOUT_SECONDS = 10;
        /** 检索响应字符上限，用于阻止异常超大响应。 */
        public static final int MAX_RESPONSE_CHARS = 4 * 1024 * 1024;
        /** 检索新鲜度允许的最大回溯天数。 */
        public static final int MAX_RECENCY_DAYS = 365;
        /** 检索区域标签最大长度。 */
        public static final int MAX_REGION_LENGTH = 32;
        /** 单条检索查询文本最大长度。 */
        public static final int MAX_QUERY_LENGTH = 260;
        /** 检索目的编码最大长度。 */
        public static final int MAX_PURPOSE_LENGTH = 64;
        /** 检索理由最大长度。 */
        public static final int MAX_REASON_LENGTH = 500;
        /** locale 标签最大长度。 */
        public static final int MAX_LOCALE_LENGTH = 16;
        /** 规划摘要最大长度。 */
        public static final int MAX_PLANNING_SUMMARY_LENGTH = 2000;
    }

    /**
     * 异步 worker、租约、重试与图片清理。
     */
    public static final class Worker {
        /** 最低租约：规划 90 秒 + 23 次串行检索 + 分析 240 秒 + 收尾 180 秒。 */
        public static final int MIN_LEASE_SECONDS = 1200;
        /** 默认租约，在最低串行预算外再保留 300 秒收敛余量。 */
        public static final int DEFAULT_LEASE_SECONDS = 1500;
        /** 租约中为落库、状态写入、网络抖动和时钟偏差预留的余量（秒）。 */
        public static final int LEASE_MARGIN_SECONDS = 180;
        /** 单个批次最大尝试次数。 */
        public static final int MAX_ATTEMPTS = 3;
        /** 失败后的重试间隔（秒）。 */
        public static final int RETRY_DELAY_SECONDS = 60;
        /** 待执行批次扫描间隔（毫秒）。 */
        public static final long DISPATCH_INTERVAL_MILLIS = 15000L;
        /** 调度器首次执行延迟（毫秒）。 */
        public static final long DISPATCH_INITIAL_DELAY_MILLIS = 5000L;
        /** 单次扫描最多领取的批次数。 */
        public static final int DISPATCH_BATCH_SIZE = 5;
        /** 临时图片保留小时数。 */
        public static final int IMAGE_TEMP_HOURS = 24;
        /** 孤儿图片单批清理数量。 */
        public static final int IMAGE_CLEANUP_BATCH_SIZE = 50;
        /** 图片删除失败重试上限。 */
        public static final int IMAGE_DELETE_RETRY_LIMIT = 5;
        /** 孤儿图片扫描间隔（毫秒）。 */
        public static final long IMAGE_CLEANUP_INTERVAL_MILLIS = 600000L;
        /** 孤儿图片首次扫描延迟（毫秒）。 */
        public static final long IMAGE_CLEANUP_INITIAL_DELAY_MILLIS = 60000L;
    }

    /**
     * 证据链、解释文本和数据库安全长度。
     */
    public static final class Evidence {
        /** 单条候选允许引用的证据因子上限。 */
        public static final int MAX_FACTORS = 60;
        /** 单场选择允许引用的支持证据上限。 */
        public static final int MAX_SUPPORT_REFS = 60;
        /** 单场选择允许引用的反向证据上限。 */
        public static final int MAX_COUNTER_REFS = 60;
        /** 证据正文进入 Prompt 前的字符上限。 */
        public static final int MAX_CONTENT_LENGTH = 4000;
        /** 单个证据观察文本上限。 */
        public static final int MAX_OBSERVATION_LENGTH = 2000;
        /** 单场权重推导文本上限。 */
        public static final int MAX_WEIGHT_RATIONALE_LENGTH = 8000;
        /** 候选风险提示上限，为 varchar(2047) 保留安全余量。 */
        public static final int MAX_RISK_NOTES_LENGTH = 2000;
        /** 冲突组编码最大长度。 */
        public static final int MAX_CONFLICT_GROUP_LENGTH = 64;
        /** 证据类型、来源引用和计划目的编码等通用编码长度。 */
        public static final int MAX_CODE_LENGTH = 64;
        /** 证据因子编码最大长度。 */
        public static final int MAX_FACTOR_CODE_LENGTH = 32;
        /** 证据计划查询文本落库长度。 */
        public static final int MAX_PLAN_QUERY_LENGTH = 512;
        /** 受控异常摘要写入数据库的长度。 */
        public static final int MAX_ERROR_MESSAGE_LENGTH = 500;
        /** 组合标题长度，与 combination_title varchar(233) 对齐。 */
        public static final int MAX_COMBINATION_TITLE_LENGTH = 233;
        /** 组合理由长度，与 combination_reason varchar(2047) 对齐。 */
        public static final int MAX_COMBINATION_REASON_LENGTH = 2047;
        /** 逐场选择理由长度，与 selection_reason varchar(1023) 对齐。 */
        public static final int MAX_SELECTION_REASON_LENGTH = 1023;
        /** 证据来源名称长度，与 source_name varchar(233) 对齐。 */
        public static final int MAX_SOURCE_NAME_LENGTH = 233;
        /** 证据来源地址长度，与 source_url varchar(1023) 对齐。 */
        public static final int MAX_SOURCE_URL_LENGTH = 1023;
        /** 证据标题上限。 */
        public static final int MAX_TITLE_LENGTH = 512;
        /** 批次备注和受控错误摘要长度，与 varchar(1023) 对齐。 */
        public static final int MAX_GENERATION_MESSAGE_LENGTH = 1023;
    }

    /**
     * 业务输入、导入和通用内容边界。
     */
    public static final class Content {
        /** 补充业务目标条数上限。 */
        public static final int MAX_SUPPLEMENTARY_OBJECTIVES = 8;
        /** 单条补充业务目标长度上限。 */
        public static final int MAX_SUPPLEMENTARY_OBJECTIVE_LENGTH = 240;
        /** 补充业务目标合计长度上限。 */
        public static final int MAX_SUPPLEMENTARY_OBJECTIVES_TOTAL_LENGTH = 1600;
        /** 人工 AI 结果导入上限（字节）。 */
        public static final int MAX_MANUAL_RESPONSE_BYTES = 8 * 1024 * 1024;
        /** 单笔业务金额上限。 */
        public static final BigDecimal MAX_BUSINESS_AMOUNT = new BigDecimal("999999999999.99");
        /** 组合倍率上限。 */
        public static final BigDecimal MAX_COMBINATION_ODDS = new BigDecimal("999999999999.999999");
        /** 方案计划正文上限，与 varchar(1023) 对齐。 */
        public static final int MAX_PLAN_CONTENT_LENGTH = 1023;
        /** 方案计划说明上限，与 varchar(1023) 对齐。 */
        public static final int MAX_PLAN_REMARK_LENGTH = 1023;
        /** 方案计划倍率上限。 */
        public static final BigDecimal MAX_PLAN_RATE = new BigDecimal("999999999999999999.999999");
        /** 方案计划金额上限。 */
        public static final BigDecimal MAX_PLAN_AMOUNT = new BigDecimal("9999999999999999999999.99");
        /** 外快合并单次输入上限。 */
        public static final int MAX_EXTRA_MERGE_INPUT_COUNT = 23;
        /** 外快合并至少需要选择的记录数，保证合并后的金额序列具有业务意义。 */
        public static final int MIN_EXTRA_MERGE_COUNT = 2;
        /** 外快记录说明上限。 */
        public static final int MAX_EXTRA_REMARK_LENGTH = 1023;
        /** 倍率记录说明上限。 */
        public static final int MAX_ODDS_REMARK_LENGTH = 1023;
        /** 每个回报组合最多图片数。 */
        public static final int MAX_RETURN_IMAGES = 3;
        /** 单张回报组合图片上限（字节）。 */
        public static final int MAX_RETURN_IMAGE_BYTES = 4 * 1024 * 1024;
        /** 孤儿图片批量删除一次允许提交的图片数。 */
        public static final int MAX_ORPHAN_IMAGE_DELETE_COUNT = 100;
        /** 孤儿图片查询最大时间跨度（天）。 */
        public static final int MAX_ORPHAN_IMAGE_QUERY_DAYS = 366;
        /** 异步失败摘要长度，避免把供应商完整响应写入批次备注。 */
        public static final int MAX_FAILURE_SUMMARY_LENGTH = 900;
        /** 图片原始文件名落库上限，与 image_original_name 字段容量对齐。 */
        public static final int MAX_IMAGE_ORIGINAL_NAME_LENGTH = 255;
        /** 图片 MIME 类型落库上限，与 image_content_type 字段容量对齐。 */
        public static final int MAX_IMAGE_CONTENT_TYPE_LENGTH = 128;
        /** 图片对象键长度上限，防止异常路径绕过对象存储约束。 */
        public static final int MAX_IMAGE_OBJECT_KEY_LENGTH = 512;
        /** 上传对象键随机后缀长度。 */
        public static final int IMAGE_KEY_RANDOM_LENGTH = 7;
        /** Worker 实例随机后缀长度；只用于区分调度实例，不承载业务语义。 */
        public static final int WORKER_ID_RANDOM_LENGTH = 12;
        /** 证据权重理由的最小有效长度，避免模型用空泛短语规避解释义务。 */
        public static final int MIN_WEIGHT_RATIONALE_LENGTH = 20;
    }

    /**
     * 数值字段精度与业务上限。
     * <p>前端 DORTMUND_NUMERIC 与本组配置一一对应，避免页面提示、表单校验和数据库精度漂移。</p>
     */
    public static final class Numeric {
        /** 让分值整数位上限。 */
        public static final int HANDICAP_INTEGER_DIGITS = 6;
        /** 让分值小数位上限。 */
        public static final int HANDICAP_FRACTION_DIGITS = 2;
        /** 让分值绝对上限。 */
        public static final BigDecimal MAX_HANDICAP_VALUE = new BigDecimal("999999.99");
        /** 单类倍率整数位上限。 */
        public static final int ODDS_INTEGER_DIGITS = 6;
        /** 单类倍率小数位上限。 */
        public static final int ODDS_FRACTION_DIGITS = 4;
        /** 单类倍率上限。 */
        public static final BigDecimal MAX_ODDS_VALUE = new BigDecimal("999999.9999");
        /** 方案倍率整数位上限。 */
        public static final int PLAN_RATE_INTEGER_DIGITS = 18;
        /** 方案倍率小数位上限。 */
        public static final int PLAN_RATE_FRACTION_DIGITS = 6;
        /** 方案金额整数位上限。 */
        public static final int PLAN_AMOUNT_INTEGER_DIGITS = 22;
        /** 方案金额小数位上限。 */
        public static final int PLAN_AMOUNT_FRACTION_DIGITS = 2;
        /** 方案摘要计算倍率的展示精度，不影响原始倍率精度。 */
        public static final int CALCULATED_RATE_FRACTION_DIGITS = 4;
        /** 模拟数值整数位上限。 */
        public static final int SIMULATION_INTEGER_DIGITS = 20;
        /** 模拟数值小数位上限。 */
        public static final int SIMULATION_FRACTION_DIGITS = 2;
        /** 模拟数值文本长度，与数据库 varchar(23) 对齐。 */
        public static final int SIMULATION_TEXT_LENGTH = 23;
        /** 回报组合倍率落库小数位。 */
        public static final int RETURN_COMBINED_ODDS_FRACTION_DIGITS = 6;
        /** 预计回报与偏差金额小数位。 */
        public static final int RETURN_AMOUNT_FRACTION_DIGITS = 2;
        /** 回报组合输入金额允许的小数位。 */
        public static final int BUSINESS_AMOUNT_FRACTION_DIGITS = 2;
        /** 外快非负金额整数位上限。 */
        public static final int EXTRA_UNSIGNED_INTEGER_DIGITS = 20;
        /** 外快可正可负金额整数位上限。 */
        public static final int EXTRA_SIGNED_INTEGER_DIGITS = 19;
        /** 外快金额小数位上限。 */
        public static final int EXTRA_FRACTION_DIGITS = 2;
    }

    /** 数值模拟默认参数与轮次边界；只描述业务规则，不保存用户本次录入值。 */
    public static final class Simulation {
        public static final int FIRST_ROUND_NO = 1;
        public static final int MIN_ROUND_NO = Integer.MIN_VALUE;
        public static final int MAX_ROUND_NO = Integer.MAX_VALUE;
        public static final BigDecimal MIN_VALUE = new BigDecimal("0.01");
        public static final BigDecimal COEFFICIENT_EXCLUSIVE_MIN = BigDecimal.ONE;
        public static final int HALF_MODE_DIVISOR = 2;
        public static final int MAX_VERSION_CREATE_ATTEMPTS = 3;
    }

    /** 日期与时间换算常量，用于查询边界和租约计算，避免魔法数字散落。 */
    public static final class Time {
        public static final long MILLIS_PER_HOUR = 3600_000L;
        public static final int DATE_ONLY_LENGTH = 10;
        public static final String START_OF_DAY = " 00:00:00";
        public static final String END_OF_DAY = " 23:59:59";
    }

    /** 倍率权重编码与取值范围；权重只参与 AI 解释，不直接改变金额公式。 */
    public static final class Weight {
        public static final int SCALE = 4;
        public static final int UNITS_PER_ONE = 10000;
        public static final int MIN_UNITS = 0;
        public static final int MAX_UNITS = UNITS_PER_ONE;
        public static final BigDecimal MIN_WEIGHT = new BigDecimal("0.0000");
        public static final BigDecimal DEFAULT_WEIGHT = new BigDecimal("1.0000");
    }

    /**
     * 数据库文本字段长度基线。
     * <p>前端 DORTMUND_LIMITS 与后端校验均引用同一口径，修改表结构时只改这里并同步迁移脚本。</p>
     */
    public static final class Field {
        public static final int SEASON_CODE = 23;
        public static final int SEASON_NAME = 233;
        public static final int SEASON_REMARK = 1023;
        public static final int TEAM_CODE = 23;
        public static final int TEAM_NAME = 233;
        public static final int TEAM_NAME_SHORT = 10;
        public static final int TEAM_NAME_EN = 233;
        public static final int TEAM_LOGO_URL = 1023;
        public static final int TEAM_VENUE = 233;
        public static final int TEAM_COUNTRY = 23;
        public static final int TEAM_TAG = 1023;
        public static final int TEAM_REMARK = 1023;
        public static final int MATCH_CODE = 23;
        public static final int MATCH_VENUE = 233;
        public static final int MATCH_REMARK = 1023;
        public static final int INFO_TITLE = 233;
        public static final int INFO_CONTENT = 1023;
        public static final int INFO_SOURCE_NAME = 233;
        public static final int INFO_SOURCE_URL = 1023;
        public static final int INFO_REMARK = 1023;
        public static final int ODDS_CODE = 23;
        public static final int ODDS_REMARK = 1023;
        public static final int PLAN_CONTENT = 1023;
        public static final int PLAN_REMARK = 1023;
        public static final int EXTRA_REMARK = 1023;
        public static final int EXTRA_MONEY = 23;
        public static final int SIMULATE_VERSION_NAME = 64;
        public static final int SIMULATE_VALUE_TEXT = 23;
    }

    /** 球队排序字段范围，与 team_sort 列和前端数字输入保持一致。 */
    public static final class Team {
        public static final int SORT_MIN = 0;
        public static final int SORT_MAX = 1023;
        public static final int SORT_DEFAULT = 0;
        /** 球队标签兼容中文逗号和英文逗号，列表、选项与 AI 上下文必须使用同一拆分规则。 */
        public static final String TAG_SPLIT_REGEX = "[,，]";
    }

    /** 赛季排序字段范围，与 season_sort 列和前端数字输入保持一致。 */
    public static final class Season {
        public static final int SORT_MIN = 0;
        public static final int SORT_MAX = 1023;
        public static final int SORT_DEFAULT = 0;
    }

    /**
     * 用户组合偏好。四项预设作为快捷模板，四项受控维度支持组合选择；
     * 偏好只影响 AI 的组合结构与解释，不改变金额公式、odds、证据效力或候选硬边界。
     */
    public static final class Preference {
        public static final String LOW_RISK_SINGLE_HIGH_STAKE = "LOW_RISK_SINGLE_HIGH_STAKE";
        public static final String LOW_RISK_SINGLE_SHORT_PARLAY = "LOW_RISK_SINGLE_SHORT_PARLAY";
        public static final String HIGH_RISK_LOW_STAKE_LONG_PARLAY = "HIGH_RISK_LOW_STAKE_LONG_PARLAY";
        public static final String BALANCED = "BALANCED";
        public static final String CUSTOM = "CUSTOM";
        public static final String DEFAULT_CODE = BALANCED;
        public static final String[] CODES = {
                LOW_RISK_SINGLE_HIGH_STAKE, LOW_RISK_SINGLE_SHORT_PARLAY,
                HIGH_RISK_LOW_STAKE_LONG_PARLAY, BALANCED, CUSTOM
        };

        /** 风险水平维度。 */
        public static final String RISK_LOW = "LOW";
        public static final String RISK_BALANCED = "BALANCED";
        public static final String RISK_HIGH = "HIGH";
        public static final String[] RISK_LEVELS = {RISK_LOW, RISK_BALANCED, RISK_HIGH};

        /** 组合结构维度；范围只作为AI结构提示，单候选硬边界仍为1至8场。 */
        public static final String STRUCTURE_SINGLE = "SINGLE";
        public static final String STRUCTURE_SHORT = "SHORT";
        public static final String STRUCTURE_BALANCED = "BALANCED";
        public static final String STRUCTURE_LONG = "LONG";
        public static final String[] STRUCTURES = {
                STRUCTURE_SINGLE, STRUCTURE_SHORT, STRUCTURE_BALANCED, STRUCTURE_LONG
        };

        /** 投入意图维度；实际投入始终以用户inputAmount为准。 */
        public static final String STAKE_CONSERVATIVE = "CONSERVATIVE";
        public static final String STAKE_BALANCED = "BALANCED";
        public static final String STAKE_AGGRESSIVE = "AGGRESSIVE";
        public static final String[] STAKE_INTENTS = {STAKE_CONSERVATIVE, STAKE_BALANCED, STAKE_AGGRESSIVE};

        /** 回报意图维度；不得解释为保本或收益承诺。 */
        public static final String RETURN_TARGET_SEEKING = "TARGET_SEEKING";
        public static final String RETURN_BALANCED = "BALANCED";
        public static final String RETURN_MAX_RETURN = "MAX_RETURN";
        public static final String[] RETURN_INTENTS = {RETURN_TARGET_SEEKING, RETURN_BALANCED, RETURN_MAX_RETURN};

        public static final String DEFAULT_RISK_LEVEL = RISK_BALANCED;
        public static final String DEFAULT_STRUCTURE = STRUCTURE_BALANCED;
        public static final String DEFAULT_STAKE_INTENT = STAKE_BALANCED;
        public static final String DEFAULT_RETURN_INTENT = RETURN_BALANCED;
    }

    /**
     * 批次、证据、图片和选择类型的业务编码。
     */
    public static final class Contract {
        public static final String STATUS_PROCESSING = "0";
        public static final String STATUS_SUCCESS = "1";
        public static final String STATUS_FAILED = "2";
        public static final String STATUS_MANUAL_PENDING = "3";
        public static final String RESULT_SOURCE_AUTO = "A";
        public static final String RESULT_SOURCE_MANUAL = "M";
        public static final String EVIDENCE_STATUS_NOT_APPLICABLE = "N";
        public static final String EVIDENCE_STATUS_PLANNING = "P";
        public static final String EVIDENCE_STATUS_READY = "R";
        public static final String EVIDENCE_STATUS_FAILED = "F";
        public static final String IMAGE_TEMP = "T";
        public static final String IMAGE_BOUND = "B";
        public static final String IMAGE_DELETE_PENDING = "D";
        public static final String IMAGE_QUARANTINED = "Q";
        public static final String AI_OPERATION_PLAN = "PLAN";
        public static final String AI_OPERATION_ANALYZE = "ANALYZE";
        public static final String YES = "Y";
        public static final String NO = "N";
        public static final String[] SELECTION_TYPES = {"HW", "DR", "AW", "HH", "HD", "HA"};
        public static final String[] RETURN_SORT_FIELDS = {
                "combinedOdds", "estimatedReturn", "deviationAmount", "matchCount",
                "reviewTime", "aiConfidence", "createTime"
        };
    }

    /**
     * 球队主场和标签的内置兜底目录。
     * <p>
     * 数据库中的导入值会与这里合并；内置项保持既定顺序和标签颜色，数据库新增值追加在后，
     * 从而避免第三方导入数据缺失时选项为空，也不会覆盖已存在的自定义值。
     */
    public static final class TeamCatalog {
        /** {标签, 值}；主场不参与标签配色。 */
        public static final String[][] VENUE_OPTIONS = {
                {"伊度纳信号公园球场", "伊度纳信号公园球场"},
                {"老特拉福德球场", "老特拉福德球场"},
                {"安菲尔德球场", "安菲尔德球场"},
                {"酋长球场", "酋长球场"},
                {"伊蒂哈德球场", "伊蒂哈德球场"},
                {"斯坦福桥球场", "斯坦福桥球场"},
                {"托特纳姆热刺球场", "托特纳姆热刺球场"},
                {"伯纳乌球场", "伯纳乌球场"},
                {"诺坎普球场", "诺坎普球场"},
                {"万达大都会球场", "万达大都会球场"},
                {"圣马梅斯球场", "圣马梅斯球场"},
                {"安联球场", "安联球场"},
                {"圣西罗球场", "圣西罗球场"},
                {"朱塞佩·梅阿查球场", "朱塞佩·梅阿查球场"},
                {"罗马奥林匹克球场", "罗马奥林匹克球场"},
                {"拉齐奥奥林匹克球场", "拉齐奥奥林匹克球场"},
                {"王子公园球场", "王子公园球场"},
                {"韦洛德罗姆球场", "韦洛德罗姆球场"},
                {"安盟球场", "安盟球场"},
                {"路易二世体育场", "路易二世体育场"}
        };

        /** {标签文本, 值, Element UI标签颜色}。 */
        public static final String[][] TAG_OPTIONS = {
                {"我横", "我横", "warning"},
                {"主队", "主队", "info"},
                {"窝里横", "窝里横", "warning"},
                {"客队", "客队", "info"},
                {"德比战", "德比战", "primary"},
                {"恩怨局", "恩怨局", "danger"},
                {"主场龙", "主场龙", "success"},
                {"客场虫", "客场虫", "danger"},
                {"客场龙", "客场龙", "success"},
                {"主场软", "主场软", "danger"},
                {"血脉压制", "血脉压制", "primary"},
                {"苦主克星", "苦主克星", "primary"},
                {"平局大师", "平局大师", "info"},
                {"杯赛大师", "杯赛大师", "info"},
                {"杯赛拉胯", "杯赛拉胯", "danger"},
                {"二队出战", "二队出战", "warning"},
                {"轮换为主", "轮换为主", "warning"},
                {"全主力", "全主力", "success"},
                {"攻势足球", "攻势足球", "success"},
                {"防守反击", "防守反击", "primary"},
                {"传控流", "传控流", "info"},
                {"摆大巴", "摆大巴", "warning"},
                {"高空轰炸", "高空轰炸", "primary"},
                {"低位防守", "低位防守", "info"},
                {"高位逼抢", "高位逼抢", "primary"},
                {"边路冲击", "边路冲击", "success"},
                {"大开大合", "大开大合", "warning"},
                {"稳扎稳打", "稳扎稳打", "success"},
                {"状态火热", "状态火热", "success"},
                {"状态低迷", "状态低迷", "danger"},
                {"起伏不定", "起伏不定", "warning"},
                {"连胜势头", "连胜势头", "success"},
                {"连败颓势", "连败颓势", "danger"},
                {"连平走势", "连平走势", "info"},
                {"绝杀专业户", "绝杀专业户", "success"},
                {"被逆转之王", "被逆转之王", "danger"},
                {"补时丢球", "补时丢球", "danger"},
                {"顺风稳", "顺风稳", "success"},
                {"逆风硬", "逆风硬", "success"},
                {"顺风浪", "顺风浪", "warning"},
                {"青年近卫军", "青年近卫军", "info"},
                {"老牌劲旅", "老牌劲旅", "primary"},
                {"升班马", "升班马", "warning"},
                {"降班马", "降班马", "warning"},
                {"保级队", "保级队", "danger"},
                {"保级狂魔", "保级狂魔", "warning"},
                {"争冠热门", "争冠热门", "success"},
                {"争冠梯队", "争冠梯队", "primary"},
                {"欧战资格", "欧战资格", "primary"},
                {"中游混子", "中游混子", "info"},
                {"土豪新军", "土豪新军", "primary"},
                {"铁血之师", "铁血之师", "success"},
                {"玻璃军团", "玻璃军团", "danger"},
                {"点球大战专家", "点球大战专家", "primary"},
                {"边路飞翼", "边路飞翼", "success"},
                {"中场绞肉机", "中场绞肉机", "warning"},
                {"中场大师", "中场大师", "primary"},
                {"定位球高手", "定位球高手", "primary"},
                {"定位球漏勺", "定位球漏勺", "danger"},
                {"门将开挂", "门将开挂", "success"},
                {"门将卧底", "门将卧底", "danger"},
                {"锋线哑火", "锋线哑火", "danger"},
                {"锋线狂飙", "锋线狂飙", "success"},
                {"后防稳如狗", "后防稳如狗", "success"},
                {"后防漏勺", "后防漏勺", "danger"},
                {"头球强势", "头球强势", "primary"},
                {"防空困难", "防空困难", "danger"},
                {"吃牌大户", "吃牌大户", "warning"},
                {"犯规凶悍", "犯规凶悍", "warning"},
                {"伤病满营", "伤病满营", "danger"},
                {"核心伤缺", "核心伤缺", "danger"},
                {"核心回归", "核心回归", "success"},
                {"赛程密集", "赛程密集", "warning"},
                {"多线作战", "多线作战", "warning"},
                {"以逸待劳", "以逸待劳", "success"},
                {"客场玄学", "客场玄学", "warning"},
                {"主场玄学", "主场玄学", "warning"},
                {"红牌体质", "红牌体质", "danger"},
                {"点球体质", "点球体质", "primary"},
                {"送点大户", "送点大户", "danger"}
        };
    }
}
