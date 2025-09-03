-- ----------------------------
-- 1、号码日志记录表
-- ----------------------------
drop table if exists fx67ll_lottery_log;
create table fx67ll_lottery_log (
  lottery_id           bigint(20)      not null auto_increment    comment '号码日志主键',
  date_code            varchar(10)     default ''                 comment '彩票期号',
  record_number        varchar(1023)   default ''                 comment '当日购买号码',
  chase_number         varchar(1023)   default ''                 comment '当日固定追号',
  winning_number       varchar(44)     default ''                 comment '当日中奖号码',
  is_win               char(1)         default 'N'                comment '是否中奖（Y是 N否）',
  winning_price        varchar(10)     default '0'                comment '中奖金额',
  number_type          int(1)                                     comment '当日购买的彩票类型（1大乐透 2双色球）',
  week_type            int(1)                                     comment '星期几的购买记录（1周一 2周二 3周三 4周四 5周五 6周六 7周日）',
  has_more_purchases   char(1)         default 'N'                comment '是否有追加购买（Y是 N否）',
  del_flag             char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  user_id              bigint(20)                                 comment '用户ID',
  create_by            varchar(64)     default ''                 comment '记录创建者',
  create_time 	       datetime                                   comment '记录创建时间',
  update_by            varchar(64)     default ''                 comment '记录更新者',
  update_time          datetime                                   comment '记录更新时间',
  primary key (lottery_id)
) engine=innodb auto_increment=1 comment = '号码日志记录表';


-- ----------------------------
-- 1-1、统计所有记录的中奖金额之和 
-- ----------------------------
SELECT SUM(CAST(winning_price AS SIGNED)) AS total_winning_amount
FROM fx67ll_lottery_log
WHERE is_win = 'Y';


-- ----------------------------
-- 1-2、统计所有记录的中奖金额之和，并分别统计大乐透、双色球以及总计三个维度
-- ----------------------------
SELECT 
  number_type,
  SUM(CAST(winning_price AS SIGNED)) AS total_winning_amount
FROM 
  fx67ll_lottery_log
WHERE 
  is_win = 'Y'
GROUP BY 
  number_type
WITH ROLLUP;


-- ----------------------------
-- 1-3、统计所有记录的中奖金额之和，并分别统计大乐透、双色球以及总计三个维度，并且需要转换为表头显示
-- ----------------------------
SELECT 
  SUM(CASE WHEN number_type = 1 AND is_win = 'Y' THEN CAST(winning_price AS SIGNED) ELSE 0 END) AS 大乐透,
  SUM(CASE WHEN number_type = 2 AND is_win = 'Y' THEN CAST(winning_price AS SIGNED) ELSE 0 END) AS 双色球,
  SUM(CASE WHEN is_win = 'Y' THEN CAST(winning_price AS SIGNED) ELSE 0 END) AS 总计
FROM 
  fx67ll_lottery_log;
 
 
-- ----------------------------
-- 1-4、统计所有记录的中奖金额之和，并分别统计大乐透、双色球以及总计三个维度
-- 除了统计数据之和，还要统计总共购买了多少注，多少注中奖，以及中奖总金额
-- ----------------------------
 SELECT
  CASE 
    WHEN number_type = 1 THEN '大乐透'
    WHEN number_type = 2 THEN '双色球'
    ELSE '总计'
  END AS lottery_type,
  COUNT(*) AS total_tickets,
  SUM(CASE WHEN is_win = 'Y' THEN 1 ELSE 0 END) AS winning_tickets,
  SUM(CASE WHEN is_win = 'Y' THEN CAST(winning_price AS SIGNED) ELSE 0 END) AS total_winning_amount
FROM
  fx67ll_lottery_log
GROUP BY
  number_type
WITH ROLLUP;
 

-- ----------------------------
-- 1-5-1、统计所有记录的中奖金额之和，并分别统计大乐透、双色球以及总计三个维度
-- 除了统计数据之和，还要统计总共购买了多少期，多少注号码，多少期中奖，以及中奖总金额
-- ----------------------------
SELECT
  CASE
    WHEN number_type = 1 THEN '大乐透'
    WHEN number_type = 2 THEN '双色球'
    ELSE '总计'
  END AS lottery_type,
  COUNT(*) AS total_tickets,
  (COUNT(IF(chase_number IS NOT NULL AND chase_number <> '', 1, NULL) + SUM(LENGTH(record_number) - LENGTH(REPLACE(record_number, '/', '')) + 1)) AS total_numbers,
  SUM(CASE WHEN is_win = 'Y' THEN 1 ELSE 0 END) AS winning_tickets,
  SUM(CASE WHEN is_win = 'Y' THEN CAST(winning_price AS SIGNED) ELSE 0 END) AS total_winning_amount
FROM
  fx67ll_lottery_log
GROUP BY
  number_type WITH ROLLUP;


-- ----------------------------
-- 1-5-2、新增类型统计
-- ----------------------------
SELECT
  CASE
    WHEN number_type = 1 THEN '大乐透'
    WHEN number_type = 2 THEN '双色球'
    WHEN number_type = 3 THEN '排列三'
    WHEN number_type = 4 THEN '排列五'
    WHEN number_type = 5 THEN '七星彩'
    ELSE '总计'
  END AS lottery_type,
  COUNT(*) AS total_tickets,
  -- 追号数量 + 单号码记录总数
  COUNT(IF(chase_number IS NOT NULL AND chase_number <> '', 1, NULL)) 
  + SUM(LENGTH(record_number) - LENGTH(REPLACE(record_number, '/', '')) + 1) 
    AS total_numbers,
  SUM(CASE WHEN is_win = 'Y' THEN 1 ELSE 0 END) AS winning_tickets,
  SUM(CASE WHEN is_win = 'Y' THEN CAST(winning_price AS SIGNED) ELSE 0 END) AS total_winning_amount
FROM
  fx67ll_lottery_log
GROUP BY
  number_type WITH ROLLUP;
 
 
-- ----------------------------
-- 1-6、给号码日志记录表添加新的索引，根据实际需要使用单个索引或组合索引
-- 两种方式生成索引均可，顺便记录查询索引和删除索引的方式
-- ----------------------------
SHOW INDEX FROM `fx67ll_lottery_log`;
ALTER TABLE fx67ll_lottery_log ADD COLUMN create_date DATE AS (DATE(create_time)) STORED;
ALTER TABLE fx67ll_lottery_log ADD INDEX idx_create_date(create_date);
CREATE INDEX idx_group_lottery ON fx67ll_lottery_log(user_id, create_date DESC);
DROP INDEX idx_create_date ON fx67ll_lottery_log;


-- ----------------------------
-- 2、固定追号配置表
-- ----------------------------
drop table if exists fx67ll_lottery_chase;
create table fx67ll_lottery_chase (
  chase_id          bigint(20)      not null auto_increment    comment '固定追号主键',
  chase_number      varchar(1023)   default ''                 comment '每日固定追号',
  number_type       int(1)                                     comment '固定追号的彩票类型（1大乐透 2双色球）',
  week_type         int(1)                                     comment '星期几的固定追号（1周一 2周二 3周三 4周四 5周五 6周六 7周日）',
  sort              int                                        comment '排序',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  user_id           bigint(20)                                 comment '用户ID',
  create_by         varchar(64)     default ''                 comment '记录创建者',
  create_time 	    datetime                                   comment '记录创建时间',
  update_by         varchar(64)     default ''                 comment '记录更新者',
  update_time       datetime                                   comment '记录更新时间',
  primary key (chase_id)
) engine=innodb auto_increment=1 comment = '固定追号配置表';


-- ----------------------------
-- 3、个人彩票生成配置表
-- ----------------------------
drop table if exists fx67ll_lottery_setting;
create table fx67ll_lottery_setting (
  setting_id        bigint(20)        not null auto_increment    comment '生成配置主键',
  lottery_setting   varchar(19999)    default ''                 comment '个人彩票生成配置',
  del_flag          char(1)           default '0'                comment '删除标志（0代表存在 2代表删除）',
  user_id           bigint(20)                                   comment '用户ID',
  create_by         varchar(64)       default ''                 comment '记录创建者',
  create_time 	    datetime                                     comment '记录创建时间',
  update_by         varchar(64)       default ''                 comment '记录更新者',
  update_time       datetime                                     comment '记录更新时间',
  primary key (setting_id)
) engine=innodb auto_increment=1 comment = '个人彩票生成配置表';


-- ----------------------------
-- 4、外快盈亏记录表
-- ----------------------------
drop table if exists fx67ll_dortmund_extra;
create table fx67ll_dortmund_extra (
  extra_id          bigint(20)        not null auto_increment    comment '外快记录主键',
  extra_money       varchar(23)       default '0'                comment '当前外快总金额',
  is_win            char(1)           default 'N'                comment '是否盈利（Y是 N否）',
  win_money         varchar(23)       default '0'                comment '外快盈亏金额',
  seed_money        varchar(23)       default '0'                comment '当前投入本金',
  save_money        varchar(23)       default '0'                comment '已经落袋为安的盈利金额',
  target_money      varchar(23)       default '0'                comment '目标金额',
  extra_remark      varchar(1023)     default ''                 comment '外快盈亏备注',
  del_flag          char(1)           default '0'                comment '删除标志（0代表存在 2代表删除）',
  user_id           bigint(20)                                   comment '用户ID',
  create_by         varchar(64)       default ''                 comment '记录创建者',
  create_time 	    datetime                                     comment '记录创建时间',
  update_by         varchar(64)       default ''                 comment '记录更新者',
  update_time       datetime                                     comment '记录更新时间',
  primary key (extra_id)
) engine=innodb auto_increment=1 comment = '外快盈亏记录表';

ALTER TABLE fx67ll_dortmund_extra
ADD COLUMN save_money varchar(23) DEFAULT '0' COMMENT '已经落袋为安的盈利金额';

-- ----------------------------
-- 5、秘钥配置表
-- ----------------------------
drop table if exists fx67ll_secret_key;
create TABLE fx67ll_secret_key (
  secret_id         bigint(20)       not null auto_increment    comment '秘钥主键',
  secret_key        varchar(1023)                               comment '秘钥键',
  secret_value      varchar(1023)                               comment '秘钥值',
  primary key (secret_id)
) engine=innodb auto_increment=1 comment = '秘钥配置表';


-- ----------------------------
-- 6、打卡记录表
-- ----------------------------
drop table if exists fx67ll_punch_log;
create table fx67ll_punch_log (
  punch_id             bigint(20)      not null auto_increment    comment '打卡记录主键',
  punch_type           char(1)                                    comment '打卡类型（1代表上班 2代表下班）',
  punch_remark         varchar(1023)   default ''                 comment '打卡记录备注',
  del_flag             char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  user_id              bigint(20)                                 comment '用户ID',
  create_by            varchar(64)     default ''                 comment '记录创建者',
  create_time 	       datetime                                   comment '记录创建时间',
  update_by            varchar(64)     default ''                 comment '记录更新者',
  update_time          datetime                                   comment '记录更新时间',
  primary key (punch_id)
) engine=innodb auto_increment=1 comment = '打卡记录表';


-- ----------------------------
-- 6-1、统计每个用户当月的工作总时长
-- ----------------------------
SELECT
	fx67ll_punch_log_result.punch_user,
	fx67ll_punch_log_result.punch_month,
	SUM(TIME_TO_SEC(TIMEDIFF( fx67ll_punch_log_result.max_punch_time, fx67ll_punch_log_result.min_punch_time ))) / 3600 AS total_work_hours,
	SUM(TIMESTAMPDIFF(MINUTE, fx67ll_punch_log_result.min_punch_time, fx67ll_punch_log_result.max_punch_time )) AS total_work_minutes,
	SUM(TIME_TO_SEC(TIMEDIFF( fx67ll_punch_log_result.max_punch_time, fx67ll_punch_log_result.min_punch_time ))) AS total_work_seconds,
	COUNT(*) AS total_punch_days,
	SUM(CASE WHEN max_punch_time IS NOT NULL AND min_punch_time IS NOT NULL THEN 1 ELSE 0 END) AS total_work_days,
	SUM(TIME_TO_SEC(TIMEDIFF( fx67ll_punch_log_result.max_punch_time, fx67ll_punch_log_result.min_punch_time ))) / 3600 / SUM(CASE WHEN max_punch_time IS NOT NULL AND min_punch_time IS NOT NULL THEN 1 ELSE 0 END) AS work_hours_per_day
FROM
	(
	SELECT
		update_by AS punch_user,
		DATE_FORMAT(update_time, '%Y-%m') AS punch_month,
		DATE(update_time) AS punch_day,
        MAX(CASE WHEN punch_type = 2 THEN update_time ELSE NULL END) AS max_punch_time,
        MIN(CASE WHEN punch_type = 1 THEN update_time ELSE NULL END) AS min_punch_time
	FROM
		fx67ll_punch_log
	GROUP BY
		punch_user,
		punch_month,
		punch_day
	) 
AS fx67ll_punch_log_result
GROUP BY
	punch_user,
	punch_month
ORDER BY
	punch_month
DESC;


-- ----------------------------
-- 6-2、统计每个用户当月的只打了一次卡的缺卡记录
-- ----------------------------
SELECT
	fx67ll_punch_log_result.punch_user AS punch_user,
	fx67ll_punch_log_result.punch_month AS punch_month,
	fx67ll_punch_log_result.punch_day AS punch_day,
	IF(fx67ll_punch_log_result.punch_type = '2',
	'上班缺卡',
	'下班缺卡') AS lost_punch_type
FROM
	(
	SELECT
		punch_type AS punch_type,
		update_by AS punch_user,
		DATE_FORMAT(update_time, '%Y-%m') AS punch_month,
		DATE(update_time) AS punch_day,
		IF(punch_type = '2',
		MAX(update_time),
		MIN(update_time)) AS punch_time
	FROM
		fx67ll_punch_log
	GROUP BY
		punch_type,
		punch_user,
		punch_month,
		punch_day
	) 
AS fx67ll_punch_log_result
GROUP BY 
	punch_user,
	punch_day
HAVING
	COUNT(CASE WHEN punch_type = '1' THEN 1 END) = 0
	OR COUNT(CASE WHEN punch_type = '2' THEN 1 END) = 0;



-- ----------------------------
-- 7、极简面试题记录表
-- ----------------------------
drop table if exists fx67ll_interview_minimal_log;
drop table if exists fx67ll_interview_minimal_option;
drop table if exists fx67ll_interview_minimal_collection;
drop table if exists fx67ll_interview_minimal_disagree;

-- 建表，面试题表
create table fx67ll_interview_minimal_log (
  interview_minimal_id                   bigint(20)       not null auto_increment    comment '面试题记录主键',
  subject_type                           char(1)          not null                   comment '面试题学科类型（1代表前端 2代表后端 3代表大数据）',
  question_type                          char(1)          not null                   comment '面试题问题类型（1代表单选题 2代表多选题 3代表解答题）',
  question_title                         varchar(1023)    not null default ''        comment '面试题问题描述',
  answer_content                         varchar(4444)    default ''                 comment '面试题答案详解',
  answer_big_text                        text                                        comment '面试题答案详解，用于存储复杂数据，尽量不启用',
  interview_minimal_remark               varchar(1023)    default ''                 comment '面试题备注',
  top_sort                               int(11)                                     comment '个人创建的置顶排序，为空不置顶，有值则按顺序从小到大排序',
  is_share                               char(1)          not null default '0'       comment '分享标志（0代表私密题目 1代表公开题目）',
  audit_status                           char(1)          not null default '0'       comment '审核状态（公开题需审核：0代表待审核 1代表审核通过 2代表审核拒绝',
  del_flag                               char(1)          not null default '0'       comment '删除标志（0代表存在 2代表删除）',
  user_id                                bigint(20)       not null                   comment '用户ID',
  create_by                              varchar(64)      default ''                 comment '记录创建者',
  create_time 	                         datetime                                    comment '记录创建时间',
  update_by                              varchar(64)      default ''                 comment '记录更新者',
  update_time                            datetime                                    comment '记录更新时间',
  primary key (interview_minimal_id),
  -- 新增核心索引：覆盖高频查询场景
  index idx_user_del_top (user_id, del_flag, top_sort),  -- 查用户的题（带置顶排序）
  index idx_share_audit_del (is_share, audit_status, del_flag),  -- 查公开+审核通过的题
  index idx_subject_type (subject_type, question_type, is_share, del_flag)  -- 按学科+题型筛选
) engine=innodb auto_increment=1 default charset=utf8mb4 comment = '极简面试题记录表';

-- 建表，选项表
create table fx67ll_interview_minimal_option (
  interview_minimal_option_id            bigint(20)       not null auto_increment    comment '面试题选项主键',
  interview_minimal_id                   bigint(20)       not null                   comment '关联面试题记录主键（外键）',
  option_label         char(2)          not null                   comment '面试题选项标识（如A、B、C、D、E）',
  option_content       varchar(1023)    not null                   comment '面试题选项内容',
  is_correct                             tinyint(1)       not null default '0'       comment '是否正确选项（0代表错误 1代表正确）',
  create_by                              varchar(64)      default ''                 comment '记录创建者',
  create_time                            datetime                                    comment '创建时间',
  update_by                              varchar(64)      default ''                 comment '记录更新者',
  update_time                            datetime                                    comment '记录更新时间',
  primary key (interview_minimal_option_id),
  -- 外键：删除题目时同步删除选项（符合物理删除逻辑）
  foreign key fk_question_option (interview_minimal_id) references fx67ll_interview_minimal_log (interview_minimal_id) on delete cascade,
  -- 索引：通过题目ID快速关联选项
  index idx_question_id (interview_minimal_id)
) engine=innodb auto_increment=1 default charset=utf8mb4 comment = '极简面试题选项表';

-- 建表，收藏表
create table fx67ll_interview_minimal_collection (
  interview_minimal_collection_id        bigint(20)       not null auto_increment    comment '收藏面试题记录主键',
  interview_minimal_id                   bigint(20)       not null                   comment '关联面试题记录主键（外键）',
  top_sort                               int(11)                                     comment '收藏的置顶排序，为空不置顶，有值则按顺序从小到大排序',
  user_id                                bigint(20)       not null                   comment '收藏用户ID',
  create_time                            datetime                                    comment '收藏面试题时间',
  primary key (interview_minimal_collection_id),
  -- 联合唯一：防止用户重复收藏同一题目
  unique key uk_user_question (user_id, interview_minimal_id),
  -- 外键：删除题目/用户时，自动删除收藏记录（符合物理删除逻辑）
  foreign key fk_collection_question (interview_minimal_id) references fx67ll_interview_minimal_log (interview_minimal_id) on delete cascade,
  foreign key fk_collection_user (user_id) references sys_user (user_id) on delete cascade,
  -- 优化索引：支持“用户的收藏+置顶排序+收藏时间”查询
  index idx_user_collection_top (user_id, top_sort, create_time desc)
) engine=innodb auto_increment=1 default charset=utf8mb4 comment = '极简面试题收藏表';

-- 建表，异议反馈表
create table fx67ll_interview_minimal_disagree (
  interview_minimal_disagree_id          bigint(20)       not null auto_increment    comment '异议记录主键',
  interview_minimal_id                   bigint(20)       not null                   comment '关联面试题ID（外键，指向需异议的题目）',
  interview_minimal_disagree_reason      varchar(1023)    not null                   comment '不认同理由（核心字段，说明为何不认同答案，限制2000字内）',
  user_id                                bigint(20)       not null                   comment '异议用户ID（外键，记录谁提出的异议）',
  create_time                            datetime                                    comment '异议提交时间（自动生成，无需手动插入）',
  primary key (interview_minimal_disagree_id),
  -- 联合唯一约束：同一用户对同一道题，只能提交1次异议（避免重复反馈）
  unique key uk_user_question (user_id, interview_minimal_id),
  -- 外键级联删除：题目删除/用户删除时，自动删除对应的异议记录（物理删除，符合极简清理逻辑）
  foreign key fk_disagree_question (interview_minimal_id) references fx67ll_interview_minimal_log (interview_minimal_id) on delete cascade,
  foreign key fk_disagree_user (user_id) references sys_user (user_id) on delete cascade,
  -- 仅保留2个必要索引，覆盖高频查询场景
  index idx_question_disagree (interview_minimal_id),  -- 快速查询某道题的所有异议
  index idx_user_disagree (user_id, create_time desc)  -- 快速查询用户自己提交的异议（按时间倒序）
) engine=innodb auto_increment=1 default charset=utf8mb4 comment = '极简面试题异议反馈表';

-- 查询
select * from fx67ll_interview_minimal_log;
select * from fx67ll_interview_minimal_option;
select * from fx67ll_interview_minimal_collection;
select * from fx67ll_interview_minimal_disagree;



-- ----------------------------
-- 8、号码日志记录表第二期
-- ----------------------------
-- 给 fx67ll_lottery_log 新增插入枚举类型
insert into sys_dict_data values(44, 3,  '排列三',   '3',       'fx67ll_lottery_type',    '',   '',     'N', '0', 'admin', sysdate(), '', null, '排列三');
insert into sys_dict_data values(45, 4,  '排列五',   '4',       'fx67ll_lottery_type',    '',   '',     'N', '0', 'admin', sysdate(), '', null, '排列五');
insert into sys_dict_data values(46, 5,  '七星彩',   '5',       'fx67ll_lottery_type',    '',   '',     'N', '0', 'admin', sysdate(), '', null, '七星彩');

-- 给 fx67ll_lottery_log 添加新的索引
ALTER TABLE fx67ll_lottery_log 
ADD COLUMN create_date DATE AS (DATE(create_time)) STORED,
ADD INDEX idx_create_date(create_date);
CREATE INDEX idx_group_lottery ON fx67ll_lottery_log(user_id, create_date DESC);

-- 查询 fx67ll_lottery_log 的所有索引
SHOW INDEX FROM `fx67ll_lottery_log`;

-- 如何删除索引
DROP INDEX idx_create_date ON fx67ll_lottery_log;

-- 查询
EXPLAIN 
SELECT * FROM fx67ll_lottery_log 
WHERE create_date <= '2025-02-28' 
  AND user_id = 'fx67ll' 
  AND del_flag = 0
ORDER BY create_date DESC 
LIMIT 5;

-- 查询
SELECT * FROM fx67ll_lottery_log 
WHERE create_date <= '2025-02-28' 
  AND user_id = 1 
  AND del_flag = 0
ORDER BY create_date DESC 
LIMIT 5;

-- 查询
SELECT
  CASE
    WHEN number_type = 1 THEN '大乐透'
    WHEN number_type = 2 THEN '双色球'
    WHEN number_type = 3 THEN '排列三'
    WHEN number_type = 4 THEN '排列五'
    WHEN number_type = 5 THEN '七星彩'
    ELSE '总计'
  END AS lottery_type,
  COUNT(*) AS total_tickets,
  (COUNT(IF(chase_number IS NOT NULL AND chase_number <> '', 1, NULL) + SUM(LENGTH(record_number) - LENGTH(REPLACE(record_number, '/', '')) + 1)) AS total_numbers,
  SUM(CASE WHEN is_win = 'Y' THEN 1 ELSE 0 END) AS winning_tickets,
  SUM(CASE WHEN is_win = 'Y' THEN CAST(winning_price AS SIGNED) ELSE 0 END) AS total_winning_amount
FROM
  fx67ll_lottery_log
GROUP BY
  number_type WITH ROLLUP;

-- 查询
SELECT
  CASE
    WHEN number_type = 1 THEN '大乐透'
    WHEN number_type = 2 THEN '双色球'
    WHEN number_type = 3 THEN '排列三'
    WHEN number_type = 4 THEN '排列五'
    WHEN number_type = 5 THEN '七星彩'
    ELSE '总计'
  END AS lottery_type,
  COUNT(*) AS total_tickets,
  -- 追号数量 + 单号码记录总数
  COUNT(IF(chase_number IS NOT NULL AND chase_number <> '', 1, NULL)) 
  + SUM(LENGTH(record_number) - LENGTH(REPLACE(record_number, '/', '')) + 1) 
    AS total_numbers,
  SUM(CASE WHEN is_win = 'Y' THEN 1 ELSE 0 END) AS winning_tickets,
  SUM(CASE WHEN is_win = 'Y' THEN CAST(winning_price AS SIGNED) ELSE 0 END) AS total_winning_amount
FROM
  fx67ll_lottery_log
GROUP BY
  number_type WITH ROLLUP;



-- ----------------------------
-- 9、IP记录表
-- ----------------------------
drop table if exists fx67ll_ip_log;

-- 建表
create table fx67ll_ip_log (
  ip_id                              bigint(20)       not null auto_increment    comment 'ip记录主键',
  ip_type                            char(1)          not null                   comment 'ip类型（0代表其他 1代表点赞 2代表接口）',
  ip_address                         varchar(39)      not null default ''        comment 'ip地址（支持ipv4和ipv6）',
  ip_page_url                        varchar(666)     default ''                 comment 'ip的网站网址',
  ip_source_location                 varchar(1023)    default ''                 comment 'ip来源位置（参考格式：国家/地区 + 城市）',
  ip_remark                          varchar(1023)    default ''                 comment 'ip记录备注',
  del_flag                           char(1)          not null default '0'       comment '删除标志（0代表存在 2代表删除）',
  user_id                            bigint(20)       not null                   comment '用户id',
  create_by                          varchar(64)      default ''                 comment '记录创建者',
  create_time                        datetime                                    comment '记录创建时间',
  update_by                          varchar(64)      default ''                 comment '记录更新者',
  update_time                        datetime                                    comment '记录更新时间',
  primary key (ip_id),
  -- 基础索引
  index idx_ip_address (ip_address),
  index idx_user_id (user_id),
  index idx_ip_type (ip_type),
  -- 联合索引：优化"用户+IP+删除状态"的查询（原有核心场景）
  index idx_user_ip_del (user_id, ip_address, del_flag),
  -- 联合索引：优化"用户+场景+删除状态"的查询（新增场景化查询）
  index idx_user_type_del (user_id, ip_type, del_flag),
  -- 联合索引：优化"IP+场景+删除状态"的查询（新增场景化查询）
  index idx_ip_type_del (ip_address, ip_type, del_flag),
  -- 场景地址索引：按场景地址查询IP（如统计某接口的访问IP）
  index idx_ip_page_url (ip_page_url)
) engine=innodb auto_increment=1 default charset=utf8mb4 comment='ip记录表';

-- 查询
select * from fx67ll_ip_log;



-- ----------------------------
-- 10、网站点赞记录表
-- ----------------------------
drop table if exists fx67ll_like_log;

-- 建表
create table fx67ll_like_log (
  like_id                            bigint(20)       not null auto_increment    comment '点赞记录主键',
  ip_id                              bigint(20)       not null                   comment '关联的ip记录id（关联fx67ll_ip_log表的ip_id）',
  like_page_url                      varchar(666)     not null                   comment '被点赞的网站网址',
  like_count                         int(11)          not null default 1         comment '单次点赞数',
  like_remark                        varchar(1023)    default ''                 comment '网站点赞记录备注',
  is_canceled                        tinyint(1)       not null default 0         comment '是否取消点赞（0代表有效 1代表已取消）',
  create_time                        datetime                                    comment '记录创建时间',
  update_time                        datetime                                    comment '记录更新时间',
  primary key (like_id),
  -- 核心索引：优化按页面查询的场景
  index idx_like_page_url (like_page_url),
  -- 联合索引：优化"按页面+是否有效"的查询（统计有效点赞）
  index idx_page_canceled (like_page_url, is_canceled),
  -- 关联IP表的索引（用于通过IP反查点赞记录）
  index idx_ip_id (ip_id)
) engine=innodb auto_increment=1 default charset=utf8mb4 comment='网站点赞记录表';

-- 查询
select * from fx67ll_like_log;  



-- ----------------------------
-- 11、富文本记录表
-- ----------------------------
drop table if exists fx67ll_note_log;

-- 建表
create table fx67ll_note_log (
  note_id                            bigint(20)       not null auto_increment    comment '富文本记录主键',
  note_content                       varchar(1023)    not null                   comment '富文本内容',
  note_remark                        varchar(1023)    default ''                 comment '富文本记录备注',
  del_flag                           char(1)          not null default '0'       comment '删除标志（0代表存在 2代表删除）',
  user_id                            bigint(20)       not null                   comment '用户id',
  create_by                          varchar(64)      default ''                 comment '记录创建者',
  create_time                        datetime                                    comment '记录创建时间',
  update_by                          varchar(64)      default ''                 comment '记录更新者',
  update_time                        datetime                                    comment '记录更新时间',
  primary key (note_id)
) engine=innodb auto_increment=1 default charset=utf8mb4 comment='富文本记录表';

-- 查询
select * from fx67ll_note_log; 
