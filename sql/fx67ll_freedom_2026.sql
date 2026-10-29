-- ----------------------------
-- 1、建表说明与删除段（先删旧遗留表，再删并重建本阶段 12 张表）
-- ----------------------------

-- 旧分析 / 评分（只删不建；评分引用分析，分析引用比赛和 AI 模板、模型）
DROP TABLE IF EXISTS `fx67ll_dortmund_match_score`;
DROP TABLE IF EXISTS `fx67ll_dortmund_match_analysis`;

-- 旧 AI 模块（只删不建；模板引用分组、场景、模型，模型引用密钥表）
DROP TABLE IF EXISTS `fx67ll_ai_prompt_template`;
DROP TABLE IF EXISTS `fx67ll_ai_request_log`;
DROP TABLE IF EXISTS `fx67ll_ai_request_daily_log`;
DROP TABLE IF EXISTS `fx67ll_ai_request_monthly_log`;
DROP TABLE IF EXISTS `fx67ll_ai_request_yearly_log`;
DROP TABLE IF EXISTS `fx67ll_ai_request_limit_rule`;
DROP TABLE IF EXISTS `fx67ll_ai_prompt_group`;
DROP TABLE IF EXISTS `fx67ll_ai_prompt_scene`;
DROP TABLE IF EXISTS `fx67ll_ai_prompt_model`;

-- 本阶段表（下面按依赖正序重建）
DROP TABLE IF EXISTS `fx67ll_dortmund_round_plan_rel`;
DROP TABLE IF EXISTS `fx67ll_dortmund_extra_plan_rel`;
DROP TABLE IF EXISTS `fx67ll_dortmund_extra_simulate`;
DROP TABLE IF EXISTS `fx67ll_dortmund_extra_simulate_version`;
DROP TABLE IF EXISTS `fx67ll_dortmund_extra`;
DROP TABLE IF EXISTS `fx67ll_dortmund_plan_match_rel`;
DROP TABLE IF EXISTS `fx67ll_dortmund_plan`;
DROP TABLE IF EXISTS `fx67ll_dortmund_info_point_team_rel`;
DROP TABLE IF EXISTS `fx67ll_dortmund_info_point`;
DROP TABLE IF EXISTS `fx67ll_dortmund_match`;
DROP TABLE IF EXISTS `fx67ll_dortmund_team`;
DROP TABLE IF EXISTS `fx67ll_dortmund_season`;

-- ----------------------------
-- 2、赛季管理表（结构保持现状）
-- ----------------------------
CREATE TABLE `fx67ll_dortmund_season` (
  `season_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '赛季唯一标识（主键）',
  `season_code` varchar(23) NOT NULL COMMENT '赛季业务编码（唯一，如2025-2026_Bundesliga）',
  `season_name` varchar(233) NOT NULL COMMENT '赛季业务名称（如2025-2026赛季德甲联赛）',
  `season_start_date` date NOT NULL COMMENT '赛季开始日期',
  `season_end_date` date NOT NULL COMMENT '赛季结束日期',
  `season_status` char(1) NOT NULL DEFAULT '0' COMMENT '赛季状态（字典码：0-进行中，1-已结束，2-未开始）',
  `season_sort` int(4) DEFAULT 0 COMMENT '赛季展示排序（升序排列，数值越小越靠前）',
  `season_remark` varchar(1023) DEFAULT '' COMMENT '赛季业务备注（说明赛事级别、规则等）',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '记录创建者用户名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '记录最后更新者用户名',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '逻辑删除标志（字典码：0-存在，2-已删除）',
  PRIMARY KEY (`season_id`),
  UNIQUE KEY `uk_season_code` (`season_code`) COMMENT '赛季编码唯一索引（防止业务编码重复）',
  KEY `idx_season_status` (`season_status`) COMMENT '赛季状态索引（加速筛选进行中/已结束赛季）',
  KEY `idx_season_sort` (`season_sort`) COMMENT '赛季排序索引（加速前端展示排序）',
  KEY `idx_user_id` (`user_id`) COMMENT '用户ID索引（加速按用户查询赛季）',
  KEY `idx_user_id_status` (`user_id`, `season_status`) COMMENT '用户+状态组合索引（优化按用户筛选进行中赛季）'
) ENGINE=innodb DEFAULT CHARSET=utf8mb4 COMMENT='赛季管理表';

-- ----------------------------
-- 3、球队管理表（结构保持现状）
-- ----------------------------
CREATE TABLE `fx67ll_dortmund_team` (
  `team_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '球队唯一标识（主键）',
  `team_code` varchar(23) NOT NULL COMMENT '球队业务编码（唯一，如DORTMUND、BAYERN）',
  `team_name` varchar(233) NOT NULL COMMENT '球队全称（如多特蒙德足球俱乐部）',
  `team_name_short` varchar(10) NOT NULL COMMENT '球队简称或昵称（如我横、大黄蜂）',
  `team_name_en` varchar(233) NOT NULL COMMENT '球队英文全称（如Borussia Dortmund）',
  `team_logo_url` varchar(1023) NOT NULL COMMENT '球队Logo图片URL地址',
  `team_venue` varchar(233) NOT NULL COMMENT '球队主场（如伊杜纳信号公园球场）',
  `team_country` varchar(23) NOT NULL COMMENT '球队所属国家/地区（如德国、英格兰）',
  `team_tag` varchar(1023) NOT NULL COMMENT '球队标签（如主场龙、客场虫，只做展示）',
  `team_status` char(1) NOT NULL DEFAULT '0' COMMENT '球队状态（字典码：0-启用，1-停用）',
  `team_sort` int(4) DEFAULT 0 COMMENT '球队展示排序（升序排列，数值越小越靠前）',
  `team_remark` varchar(1023) DEFAULT '' COMMENT '球队业务备注',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '记录创建者用户名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '记录最后更新者用户名',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '逻辑删除标志（字典码：0-存在，2-已删除）',
  PRIMARY KEY (`team_id`),
  UNIQUE KEY `uk_team_code` (`team_code`) COMMENT '球队编码唯一索引（防止业务编码重复）',
  KEY `idx_team_status` (`team_status`) COMMENT '球队状态索引（加速筛选启用/停用球队）',
  KEY `idx_team_sort` (`team_sort`) COMMENT '球队排序索引（加速前端展示排序）',
  KEY `idx_team_country` (`team_country`) COMMENT '球队国家索引（加速按国家筛选球队）',
  KEY `idx_user_id` (`user_id`) COMMENT '用户ID索引（加速按用户查询球队）',
  KEY `idx_user_id_country` (`user_id`, `team_country`) COMMENT '用户+国家组合索引（优化按用户筛选某国家球队）'
) ENGINE=innodb DEFAULT CHARSET=utf8mb4 COMMENT='球队管理表';

-- ----------------------------
-- 4、比赛记录表（本阶段校准：唯一索引、分析次数默认值、赛果空值、状态注释）
-- ----------------------------
CREATE TABLE `fx67ll_dortmund_match` (
  `match_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '比赛唯一标识（主键）',
  `season_id` bigint(20) NOT NULL COMMENT '所属赛季ID（外键，关联fx67ll_dortmund_season.season_id）',
  `home_team_id` bigint(20) NOT NULL COMMENT '主队球队ID（外键，关联fx67ll_dortmund_team.team_id）',
  `away_team_id` bigint(20) NOT NULL COMMENT '客队球队ID（外键，关联fx67ll_dortmund_team.team_id）',
  `match_time` datetime NOT NULL COMMENT '比赛开球时间',
  `match_venue` varchar(233) DEFAULT '' COMMENT '比赛举办场地名称',
  `match_status` char(1) NOT NULL DEFAULT '0' COMMENT '比赛状态（字典码：0-正常，1-关闭，沿用sys_notice_status，表示资料状态；默认0）',
  `analysis_count` int(11) unsigned NOT NULL DEFAULT 0 COMMENT 'AI分析次数（统计该比赛已生成的分析报告数量，只由服务端维护）',
  `match_result` char(1) DEFAULT NULL COMMENT '比赛结果（字典码：0-主队胜，1-平局，2-客队胜；未产生赛果时为空）',
  `match_remark` varchar(1023) DEFAULT '' COMMENT '比赛业务备注（如轮次、特殊说明）',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '记录创建者用户名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '记录最后更新者用户名',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '逻辑删除标志（字典码：0-存在，2-已删除）',
  PRIMARY KEY (`match_id`),
  UNIQUE KEY `uk_season_home_away_time` (`season_id`, `home_team_id`, `away_team_id`, `match_time`) COMMENT '赛季+主队+客队+开赛时间唯一索引（重复时改选已有比赛；前缀同时覆盖按赛季查询和赛季外键）',
  KEY `idx_home_team_id` (`home_team_id`) COMMENT '主队ID索引（主队外键和主队搜索）',
  KEY `idx_away_team_id` (`away_team_id`) COMMENT '客队ID索引（客队外键和客队搜索）',
  KEY `idx_match_time` (`match_time`) COMMENT '开赛时间索引（总览按时间范围查询和倒序）',
  KEY `idx_season_match_time` (`season_id`, `match_time`) COMMENT '赛季+时间组合索引（某赛季比赛按时间排序）',
  KEY `idx_status_match_time` (`match_status`, `match_time`) COMMENT '状态+时间组合索引（选择器只取正常比赛并按时间排序）',
  KEY `idx_user_id` (`user_id`) COMMENT '用户ID索引（数据范围过滤）',
  FOREIGN KEY (`season_id`) REFERENCES `fx67ll_dortmund_season`(`season_id`) ON DELETE RESTRICT,
  FOREIGN KEY (`home_team_id`) REFERENCES `fx67ll_dortmund_team`(`team_id`) ON DELETE RESTRICT,
  FOREIGN KEY (`away_team_id`) REFERENCES `fx67ll_dortmund_team`(`team_id`) ON DELETE RESTRICT
) ENGINE=innodb DEFAULT CHARSET=utf8mb4 COMMENT='比赛记录表';

-- ----------------------------
-- 5、统一信息点表
-- ----------------------------
CREATE TABLE `fx67ll_dortmund_info_point` (
  `info_point_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '信息点唯一标识（主键）',
  `info_point_type` char(1) NOT NULL COMMENT '信息点类型（T-赛季球队信息点，M-比赛临时信息点；创建后不可修改）',
  `season_id` bigint(20) DEFAULT NULL COMMENT 'T类型所属赛季ID（外键，关联fx67ll_dortmund_season.season_id；创建后不可修改；M类型为空）',
  `match_id` bigint(20) DEFAULT NULL COMMENT 'M类型所属比赛ID（外键，关联fx67ll_dortmund_match.match_id；创建后不可修改；T类型为空）',
  `point_title` varchar(233) NOT NULL COMMENT '标题',
  `point_content` mediumtext NOT NULL COMMENT '客观信息内容',
  `source_name` varchar(233) NOT NULL COMMENT '信息来源名称',
  `source_url` varchar(1023) DEFAULT NULL COMMENT '来源地址',
  `source_publish_time` datetime DEFAULT NULL COMMENT '来源发布时间',
  `collect_time` datetime NOT NULL COMMENT '采集或录入时间',
  `effective_start_time` datetime DEFAULT NULL COMMENT '生效时间（空表示该端不限制）',
  `effective_end_time` datetime DEFAULT NULL COMMENT '失效时间（空表示该端不限制）',
  `point_status` char(1) NOT NULL DEFAULT '0' COMMENT '信息点状态（0-启用，1-停用，复用sys_normal_disable）',
  `allow_public` char(1) NOT NULL DEFAULT '0' COMMENT '是否可公开（0-不可公开，1-可公开，字典fx67ll_dortmund_allow_public）',
  `point_remark` varchar(1023) DEFAULT NULL COMMENT '备注',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID（服务端写入）',
  `create_by` varchar(64) DEFAULT '' COMMENT '记录创建者用户名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '记录最后更新者用户名',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
  PRIMARY KEY (`info_point_id`),
  KEY `idx_info_point_type` (`info_point_type`) COMMENT '信息点类型索引',
  KEY `idx_season_id` (`season_id`) COMMENT '赛季ID索引',
  KEY `idx_match_id` (`match_id`) COMMENT '比赛ID索引',
  KEY `idx_point_status` (`point_status`) COMMENT '信息点状态索引',
  KEY `idx_user_id` (`user_id`) COMMENT '用户ID索引',
  KEY `idx_user_id_type` (`user_id`, `info_point_type`) COMMENT '用户+类型组合索引',
  FOREIGN KEY (`season_id`) REFERENCES `fx67ll_dortmund_season`(`season_id`) ON DELETE RESTRICT,
  FOREIGN KEY (`match_id`) REFERENCES `fx67ll_dortmund_match`(`match_id`) ON DELETE RESTRICT
) ENGINE=innodb DEFAULT CHARSET=utf8mb4 COMMENT='统一信息点表';

-- ----------------------------
-- 6、信息点球队关联表
-- ----------------------------
CREATE TABLE `fx67ll_dortmund_info_point_team_rel` (
  `relation_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联唯一标识（主键）',
  `info_point_id` bigint(20) NOT NULL COMMENT '信息点ID（必须指向T类型，由服务层校验；外键关联fx67ll_dortmund_info_point.info_point_id）',
  `team_id` bigint(20) NOT NULL COMMENT '球队ID（外键，关联fx67ll_dortmund_team.team_id）',
  `relation_status` char(1) NOT NULL DEFAULT '0' COMMENT '关联状态（0-有效，1-停用，字典fx67ll_dortmund_relation_status）',
  `create_by` varchar(64) DEFAULT '' COMMENT '记录创建者用户名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '记录最后更新者用户名',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
  PRIMARY KEY (`relation_id`),
  UNIQUE KEY `uk_info_point_team` (`info_point_id`, `team_id`) COMMENT '同一信息点与球队只保留一行（停用行同样占用）',
  KEY `idx_team_id` (`team_id`) COMMENT '球队ID索引（按球队反查信息点）',
  FOREIGN KEY (`info_point_id`) REFERENCES `fx67ll_dortmund_info_point`(`info_point_id`) ON DELETE RESTRICT,
  FOREIGN KEY (`team_id`) REFERENCES `fx67ll_dortmund_team`(`team_id`) ON DELETE RESTRICT
) ENGINE=innodb DEFAULT CHARSET=utf8mb4 COMMENT='信息点球队关联表';

-- ----------------------------
-- 7、方案计划表
-- ----------------------------
CREATE TABLE `fx67ll_dortmund_plan` (
  `plan_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '方案计划唯一标识（主键）',
  `plan_content` varchar(1023) NOT NULL COMMENT '正文',
  `plan_rate` decimal(10,4) DEFAULT NULL COMMENT '录入倍率（空表示凭证没有该项；有值时必须大于0，由服务层校验）',
  `plan_amount` decimal(14,2) DEFAULT NULL COMMENT '录入金额（空表示凭证没有该项；有值时必须大于等于0，由服务层校验）',
  `plan_remark` varchar(1023) DEFAULT NULL COMMENT '其他说明',
  `plan_image_object_keys` varchar(2047) DEFAULT NULL COMMENT '最多两个相对对象键，逗号分隔；不保存公开URL或绝对路径',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID（服务端写入）',
  `create_by` varchar(64) DEFAULT '' COMMENT '记录创建者用户名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '记录最后更新者用户名',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '逻辑删除标志（0-存在，2-已删除）',
  PRIMARY KEY (`plan_id`),
  KEY `idx_user_id` (`user_id`) COMMENT '按用户查询'
) ENGINE=innodb DEFAULT CHARSET=utf8mb4 COMMENT='方案计划表';

-- ----------------------------
-- 8、方案计划与比赛关系表
-- ----------------------------
CREATE TABLE `fx67ll_dortmund_plan_match_rel` (
  `rel_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关系唯一标识（主键）',
  `plan_id` bigint(20) NOT NULL COMMENT '方案计划ID（外键，关联fx67ll_dortmund_plan.plan_id）',
  `match_id` bigint(20) NOT NULL COMMENT '比赛ID（外键，关联fx67ll_dortmund_match.match_id）',
  `create_by` varchar(64) DEFAULT '' COMMENT '记录创建者用户名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '记录最后更新者用户名',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
  PRIMARY KEY (`rel_id`),
  UNIQUE KEY `uk_plan_match` (`plan_id`, `match_id`) COMMENT '同一方案计划与比赛只保留一行',
  KEY `idx_match_id` (`match_id`) COMMENT '按比赛反查方案计划',
  FOREIGN KEY (`plan_id`) REFERENCES `fx67ll_dortmund_plan`(`plan_id`) ON DELETE RESTRICT,
  FOREIGN KEY (`match_id`) REFERENCES `fx67ll_dortmund_match`(`match_id`) ON DELETE RESTRICT
) ENGINE=innodb DEFAULT CHARSET=utf8mb4 COMMENT='方案计划与比赛关系表';

-- ----------------------------
-- 9、外快盈亏表（第 6 周，按历史字段原样重建）
-- ----------------------------
CREATE TABLE `fx67ll_dortmund_extra` (
  `extra_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '外快记录主键',
  `extra_money` varchar(23) DEFAULT '0' COMMENT '当前外快总金额',
  `is_win` char(1) DEFAULT 'N' COMMENT '是否盈利（Y是 N否）',
  `win_money` varchar(23) DEFAULT '0' COMMENT '外快盈亏金额',
  `seed_money` varchar(23) DEFAULT '0' COMMENT '当前投入本金',
  `save_money` varchar(23) DEFAULT '0' COMMENT '已经落袋为安的盈利金额',
  `target_money` varchar(23) DEFAULT '0' COMMENT '目标金额',
  `extra_remark` varchar(1023) DEFAULT '' COMMENT '外快盈亏备注',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `user_id` bigint(20) COMMENT '用户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '记录创建者用户名',
  `create_time` datetime COMMENT '记录创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '记录更新者',
  `update_time` datetime COMMENT '记录更新时间',
  PRIMARY KEY (`extra_id`)
) ENGINE=innodb DEFAULT CHARSET=utf8mb4 COMMENT='外快盈亏记录表';

-- ----------------------------
-- 10、外快盈亏与方案计划关系表（第 6 周）
-- ----------------------------
CREATE TABLE `fx67ll_dortmund_extra_plan_rel` (
  `rel_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关系唯一标识（主键）',
  `extra_id` bigint(20) NOT NULL COMMENT '外快盈亏ID（外键，关联fx67ll_dortmund_extra.extra_id）',
  `plan_id` bigint(20) NOT NULL COMMENT '方案计划ID（外键，关联fx67ll_dortmund_plan.plan_id）',
  `create_by` varchar(64) DEFAULT '' COMMENT '记录创建者用户名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '记录最后更新者用户名',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
  PRIMARY KEY (`rel_id`),
  UNIQUE KEY `uk_extra_plan` (`extra_id`, `plan_id`) COMMENT '同一外快盈亏与方案计划只保留一行',
  KEY `idx_plan_id` (`plan_id`) COMMENT '按方案计划反查外快盈亏（引用检查）',
  FOREIGN KEY (`extra_id`) REFERENCES `fx67ll_dortmund_extra`(`extra_id`) ON DELETE RESTRICT,
  FOREIGN KEY (`plan_id`) REFERENCES `fx67ll_dortmund_plan`(`plan_id`) ON DELETE RESTRICT
) ENGINE=innodb DEFAULT CHARSET=utf8mb4 COMMENT='外快盈亏与方案计划关系表';

-- ----------------------------
-- 11、数值模拟版本表（第 8 周，按历史字段原样重建）
-- ----------------------------
CREATE TABLE `fx67ll_dortmund_extra_simulate_version` (
  `version_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '数值模拟版本记录主键',
  `version_no` int(11) DEFAULT 1 COMMENT '版本序号（同一用户内递增）',
  `version_name` varchar(64) DEFAULT '' COMMENT '版本名称',
  `sim_mode` varchar(8) DEFAULT 'full' COMMENT '模拟模式（full全量滚动 half半量滚动）',
  `initial_value` varchar(23) DEFAULT '0' COMMENT '初始数值',
  `coefficient` varchar(23) DEFAULT '0' COMMENT '每轮系数',
  `target_value` varchar(23) DEFAULT '0' COMMENT '目标数值',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `invalid_flag` char(1) DEFAULT '0' COMMENT '作废标志（0代表正常 1代表已作废，作废后不在列表中展示）',
  `user_id` bigint(20) COMMENT '用户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '记录创建者用户名',
  `create_time` datetime COMMENT '记录创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '记录更新者',
  `update_time` datetime COMMENT '记录更新时间',
  PRIMARY KEY (`version_id`)
) ENGINE=innodb DEFAULT CHARSET=utf8mb4 COMMENT='外快盈亏数值模拟版本记录表';

-- ----------------------------
-- 12、数值模拟轮次表（第 8 周，按历史字段原样重建）
-- ----------------------------
CREATE TABLE `fx67ll_dortmund_extra_simulate` (
  `sim_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '数值模拟轮次记录主键',
  `version_id` bigint(20) NOT NULL COMMENT '所属模拟版本主键',
  `sim_mode` varchar(8) DEFAULT 'full' COMMENT '模拟模式（full全量滚动 half半量滚动）',
  `round_no` int(11) DEFAULT 1 COMMENT '轮次序号（版本内序号，新版本从1开始）',
  `start_value` varchar(23) DEFAULT '0' COMMENT '本轮开始数值',
  `join_value` varchar(23) DEFAULT '0' COMMENT '本轮参与滚动数值',
  `is_hit` char(1) DEFAULT 'N' COMMENT '本轮是否达成（Y是 N否）',
  `return_value` varchar(23) DEFAULT '0' COMMENT '本轮实际回收数值（不必然等于参与数值乘系数，支持部分回本）',
  `end_value` varchar(23) DEFAULT '0' COMMENT '本轮结束数值',
  `round_change` varchar(23) DEFAULT '0' COMMENT '本轮数值变动倍数',
  `coefficient` varchar(23) DEFAULT '0' COMMENT '本轮使用系数',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `user_id` bigint(20) COMMENT '用户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '记录创建者用户名',
  `create_time` datetime COMMENT '记录创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '记录更新者',
  `update_time` datetime COMMENT '记录更新时间',
  PRIMARY KEY (`sim_id`),
  KEY `idx_simulate_version_id` (`version_id`) COMMENT '版本ID索引（加速按版本查询轮次记录）'
) ENGINE=innodb DEFAULT CHARSET=utf8mb4 COMMENT='外快盈亏数值模拟轮次记录表';

-- ----------------------------
-- 13、数值轮次与方案计划关系表（第 8 周）
-- ----------------------------
CREATE TABLE `fx67ll_dortmund_round_plan_rel` (
  `rel_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关系唯一标识（主键）',
  `round_id` bigint(20) NOT NULL COMMENT '数值模拟轮次ID（外键，关联fx67ll_dortmund_extra_simulate.sim_id）',
  `plan_id` bigint(20) NOT NULL COMMENT '方案计划ID（外键，关联fx67ll_dortmund_plan.plan_id）',
  `create_by` varchar(64) DEFAULT '' COMMENT '记录创建者用户名',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '记录最后更新者用户名',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
  PRIMARY KEY (`rel_id`),
  UNIQUE KEY `uk_round_plan` (`round_id`, `plan_id`) COMMENT '同一轮次与方案计划只保留一行',
  KEY `idx_plan_id` (`plan_id`) COMMENT '按方案计划反查轮次（引用检查）',
  FOREIGN KEY (`round_id`) REFERENCES `fx67ll_dortmund_extra_simulate`(`sim_id`) ON DELETE RESTRICT,
  FOREIGN KEY (`plan_id`) REFERENCES `fx67ll_dortmund_plan`(`plan_id`) ON DELETE RESTRICT
) ENGINE=innodb DEFAULT CHARSET=utf8mb4 COMMENT='数值轮次与方案计划关系表';

-- ----------------------------
-- 14、字典类型（复用字典自 fx67ll_ry_2023.sql 原样迁入，沿用原字典主键 18、19；新增字典从 20 起分配）
-- ----------------------------

-- 比赛标准化评分记录管理枚举项插入值
insert into sys_dict_type values(18, '比赛结果类型',  'fx67ll_match_result',    '0', 'admin', sysdate(), '', null, '比赛结果类型列表');

-- 比赛/赛季等通用管理枚举项插入值
insert into sys_dict_type values(19, '我横状态类型',  'fx67ll_dortmund_status',    '0', 'admin', sysdate(), '', null, '我横状态类型');

-- 统一信息点管理枚举项插入值（第 4 周）
insert into sys_dict_type values(20, '信息点类型',          'fx67ll_dortmund_info_point_type',    '0', 'admin', sysdate(), '', null, '信息点类型列表');
insert into sys_dict_type values(21, '是否可公开',          'fx67ll_dortmund_allow_public',       '0', 'admin', sysdate(), '', null, '是否可公开列表');
insert into sys_dict_type values(22, '信息点球队关联状态',  'fx67ll_dortmund_relation_status',    '0', 'admin', sysdate(), '', null, '信息点球队关联状态列表');

-- ----------------------------
-- 15、字典数据（复用字典自 fx67ll_ry_2023.sql 原样迁入，沿用原字典编码 56 到 61；新增字典数据从 62 起分配）
-- ----------------------------

-- 比赛标准化评分记录管理枚举项插入值
insert into sys_dict_data values(56, 1,  '胜',       '0',       'fx67ll_match_result',         '',   '',     'N', '0', 'admin', sysdate(), '', null, '胜');
insert into sys_dict_data values(57, 2,  '平',       '1',       'fx67ll_match_result',         '',   '',     'N', '0', 'admin', sysdate(), '', null, '平');
insert into sys_dict_data values(58, 3,  '负',       '2',       'fx67ll_match_result',         '',   '',     'N', '0', 'admin', sysdate(), '', null, '负');

-- 比赛/赛季等通用管理枚举项插入值
insert into sys_dict_data values(59, 1,  '进行中',   '0',       'fx67ll_dortmund_status',         '',   '',     'N', '0', 'admin', sysdate(), '', null, '进行中');
insert into sys_dict_data values(60, 2,  '已结束',   '1',       'fx67ll_dortmund_status',         '',   '',     'N', '0', 'admin', sysdate(), '', null, '已结束');
insert into sys_dict_data values(61, 3,  '未开始',   '2',       'fx67ll_dortmund_status',         '',   '',     'N', '0', 'admin', sysdate(), '', null, '未开始');

-- 统一信息点类型枚举项插入值（第 4 周）
insert into sys_dict_data values(62, 1,  '赛季球队信息点',  'T',  'fx67ll_dortmund_info_point_type',    '',   '',     'N', '0', 'admin', sysdate(), '', null, '赛季球队信息点');
insert into sys_dict_data values(63, 2,  '比赛临时信息点',  'M',  'fx67ll_dortmund_info_point_type',    '',   '',     'N', '0', 'admin', sysdate(), '', null, '比赛临时信息点');

-- 统一信息点是否可公开枚举项插入值（第 4 周）
insert into sys_dict_data values(64, 1,  '不可公开',        '0',  'fx67ll_dortmund_allow_public',       '',   '',     'N', '0', 'admin', sysdate(), '', null, '不可公开');
insert into sys_dict_data values(65, 2,  '可公开',          '1',  'fx67ll_dortmund_allow_public',       '',   '',     'N', '0', 'admin', sysdate(), '', null, '可公开');

-- 信息点球队关联状态枚举项插入值（第 4 周）
insert into sys_dict_data values(66, 1,  '有效',            '0',  'fx67ll_dortmund_relation_status',    '',   '',     'N', '0', 'admin', sysdate(), '', null, '有效');
insert into sys_dict_data values(67, 2,  '停用',            '1',  'fx67ll_dortmund_relation_status',    '',   '',     'N', '0', 'admin', sysdate(), '', null, '停用');
