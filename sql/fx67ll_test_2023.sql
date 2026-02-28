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
  index idx_user_ip_type_del (user_id, ip_type, del_flag),
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
  note_content                       varchar(4444)    not null                   comment '富文本内容',
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



-- ----------------------------
-- 12、麻将室预约表（预留精简计费字段，后续增加计费配置表）
-- ----------------------------
drop table if exists fx67ll_mahjong_reservation_log;
drop table if exists fx67ll_mahjong_room;

-- 建表，麻将室表
create table fx67ll_mahjong_room (
  mahjong_room_id                    bigint(20)       not null auto_increment    comment '麻将室主键',
  user_id                            bigint(20)       not null                   comment '管理员主键',
  user_name                          varchar(30)      not null                   comment '管理员名称',
  mahjong_room_name                  varchar(64)      not null                   comment '麻将室名称',
  mahjong_room_description           varchar(1023)    not null                   comment '麻将室描述',
  mahjong_room_capacity              int(2)           not null default 4         comment '容纳人数（默认4人）',
  mahjong_room_price_config          json             null                       comment '预留：计费配置（未来存储分时段/包夜规则等JSON数据）',
  mahjong_room_status                char(1)          not null default '0'       comment '状态（0开放 1关闭）',
  mahjong_room_remark                varchar(1023)    default ''                 comment '麻将室备注',
  del_flag                           char(1)          not null default '0'       comment '删除标志（0代表存在 2代表删除）',
  create_by                          varchar(64)      default ''                 comment '记录创建者',
  create_time                        datetime         default current_timestamp  comment '记录创建时间',
  update_by                          varchar(64)      default ''                 comment '记录更新者',
  update_time                        datetime         default current_timestamp on update current_timestamp  comment '记录更新时间',
  primary key (mahjong_room_id),
  key idx_room_status (mahjong_room_status, del_flag),  -- 优化状态查询
  key idx_room_name (mahjong_room_name, del_flag)        -- 优化按名称搜索
) engine=innodb auto_increment=1 default charset=utf8mb4 comment='麻将室表';

-- 添加约束：限制状态和删除标志的取值范围
alter table fx67ll_mahjong_room 
add constraint chk_room_status check (mahjong_room_status in ('0', '1')),
add constraint chk_room_del_flag check (del_flag in ('0', '2'));

-- 建表，麻将室预约订单记录表
create table fx67ll_mahjong_reservation_log (
  mahjong_reservation_log_id         bigint(20)       not null auto_increment    comment '麻将室预约订单记录主键',
  user_id                            bigint(20)       not null                   comment '预约用户主键',
  mahjong_room_id                    bigint(20)       not null                   comment '麻将室主键',
  mahjong_room_name                  varchar(64)      not null                   comment '麻将室名称',
  reservation_start_time             datetime         not null                   comment '预约开始时间（含日期和小时）',
  reservation_end_time               datetime         not null                   comment '预约结束时间（含日期和小时）',
  reservation_contact                varchar(20)      default ''                 comment '预约联系方式（电话）',
  reservation_amount                 decimal(10,2)    null                       comment '预留：费用金额（未来存储实际费用）',
  reservation_status                 char(1)          not null default '0'       comment '麻将室预约订单记录状态（0正常 1取消 2完成）',
  reservation_remark                 varchar(1023)    default ''                 comment '麻将室预约订单记录备注',
  del_flag                           char(1)          not null default '0'       comment '删除标志（0代表存在 2代表删除）',
  create_by                          varchar(64)      default ''                 comment '记录创建者',
  create_time                        datetime         default current_timestamp  comment '记录创建时间',
  update_by                          varchar(64)      default ''                 comment '记录更新者',
  update_time                        datetime         default current_timestamp on update current_timestamp  comment '记录更新时间',
  primary key (mahjong_reservation_log_id),
  -- 外键关联（限制物理删除，避免误删历史数据）
  constraint fk_reservation_user foreign key (user_id) references sys_user(user_id) on delete restrict,
  constraint fk_reservation_room foreign key (mahjong_room_id) references fx67ll_mahjong_room(mahjong_room_id) on delete restrict,
  -- 索引调整：移除date()函数，用原始时间字段建索引（兼容所有MySQL版本）
  key idx_user_reservation (user_id, del_flag, reservation_status),  -- 我的预约记录查询
  key idx_room_reservation (mahjong_room_id, del_flag, reservation_start_time),  -- 按房间+时间查询（替代原date()函数索引）
  key idx_reservation_status (reservation_status, del_flag, reservation_start_time)  -- 按状态+时间范围查询
) engine=innodb auto_increment=1 default charset=utf8mb4 comment='麻将室预约订单记录表';

-- 添加约束：限制时间逻辑和状态取值
alter table fx67ll_mahjong_reservation_log 
add constraint chk_reservation_time check (reservation_start_time < reservation_end_time),
add constraint chk_reservation_status check (reservation_status in ('0', '1', '2')),
add constraint chk_reservation_del_flag check (del_flag in ('0', '2'));

-- 修改原有用户表的字段 user_type 为非空，并且完善注释
alter table sys_user
modify column user_type varchar(2) default '00' not null comment '用户类型（00系统用户，79超神用户）';

-- 给原有用户表新增字段 user_key，contact_info，用于区分chaoshen用户，兼容复杂的方式
alter table `sys_user`
add column `user_key` varchar(30) default '' not null comment '用户标识（chaoshen=超神麻将室用户）' after `user_type`
alter table `sys_user`
add column `contact_info` varchar(100) default '' comment '联系方式（手机号之外的联系方式）' after `phonenumber`;

-- 为超神用户注册新增独立配置开关，避免与普通用户注册开关冲突
insert into `sys_config` (`config_name`, `config_key`, `config_value`, `config_type`, `remark`, `create_time`, `update_time`)
values ('超神用户注册开关', 'sys.account.registerUserChaoshen', 'true', 'Y', '账号自助-是否开启超神用户注册功能', now(), now());

-- 查询语句
select * from fx67ll_mahjong_room where del_flag = '0'; 
select * from fx67ll_mahjong_reservation_log where del_flag = '0';



-- ----------------------------
-- 13、AI 表设计
-- ----------------------------
-- AI Prompt 模板表
drop table if exists fx67ll_ai_prompt_template;
-- AI Prompt 模版分组表  
drop table if exists fx67ll_ai_prompt_group;
-- AI Prompt 场景编码表
drop table if exists fx67ll_ai_prompt_scene;
-- AI Prompt 模型配置表
drop table if exists fx67ll_ai_prompt_model;
-- AI Prompt 模型配置表
drop table if exists fx67ll_ai_prompt_model;
-- AI Prompt 限流/熔断规则表（适配Sentinel框架）
drop table if exists fx67ll_ai_prompt_limit_rule;
-- AI 调用请求日志表
drop table if exists fx67ll_ai_request_log;
-- AI 调用请求日统计日志表
drop table if exists fx67ll_ai_request_daily_log;
-- AI 调用请求月统计日志表
drop table if exists fx67ll_ai_request_monthly_log;
-- AI 调用请求年统计日志表
drop table if exists fx67ll_ai_request_yearly_log;

CREATE TABLE `fx67ll_ai_prompt_template` (
  `prompt_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `prompt_name` varchar(233) NOT NULL COMMENT '模板名称',
  `group_id` bigint(20) NOT NULL COMMENT '关联分组ID（外键，模板仅属于一个分组）',
  `scene_id` bigint(20) NOT NULL COMMENT '关联场景ID（外键，模板仅属于一个场景）',
  `model_id` bigint(20) NOT NULL COMMENT '关联模型ID（外键，模板仅使用一个模型）',
  `prompt_content` text NOT NULL COMMENT 'Prompt模板内容（含变量占位符）',
  `prompt_variable_config` text COMMENT '变量配置（JSON格式字符串，示例：[{"name":"team","type":"string","required":true}]）',
  `prompt_custom_config_params` text COMMENT '模板自定义参数（JSON格式字符串，覆盖模型默认参数）',
  `prompt_status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `prompt_remark` varchar(1023) DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  PRIMARY KEY (`prompt_id`),
  KEY `idx_group_id` (`group_id`) COMMENT '分组ID索引（快速按分组查询模板）',
  KEY `idx_scene_id` (`scene_id`) COMMENT '场景ID索引（快速按场景查询模板）',
  KEY `idx_model_id` (`model_id`) COMMENT '模型ID索引（快速按模型查询模板）',
  KEY `idx_group_scene_model` (`group_id`, `scene_id`, `model_id`) COMMENT '分组+场景+模型组合索引（优化多维度组合查询）',
  KEY `idx_prompt_status` (`prompt_status`) COMMENT '模板状态索引（快速筛选启用/停用模板）',
  FOREIGN KEY (`group_id`) REFERENCES `fx67ll_ai_prompt_group`(`group_id`) ON DELETE RESTRICT COMMENT '分组外键：删除分组时禁止删除关联模板',
  FOREIGN KEY (`scene_id`) REFERENCES `fx67ll_ai_prompt_scene`(`scene_id`) ON DELETE RESTRICT COMMENT '场景外键：删除场景时禁止删除关联模板',
  FOREIGN KEY (`model_id`) REFERENCES `fx67ll_ai_prompt_model`(`model_id`) ON DELETE RESTRICT COMMENT '模型外键：删除模型时禁止删除关联模板'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI Prompt模板管理表';

CREATE TABLE `fx67ll_ai_prompt_group` (
  `group_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分组ID（主键）',
  `group_code` varchar(233) NOT NULL COMMENT '分组编码（如football_analysis、code_helper）',
  `group_name` varchar(233) NOT NULL COMMENT '分组名称（如足球分析、代码助手）',
  `group_desc` varchar(1023) DEFAULT '' COMMENT '分组描述（如“跨场景的足球相关Prompt模板分组”）',
  `group_status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `group_sort` int(4) DEFAULT 0 COMMENT '排序（用于前端展示，数值越小越靠前）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  PRIMARY KEY (`group_id`),
  UNIQUE KEY `uk_group_code` (`group_code`) COMMENT '分组编码唯一索引：防止分组编码重复',
  KEY `idx_group_status` (`group_status`) COMMENT '分组状态索引（快速筛选启用/停用分组）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI Prompt模板分组表';

CREATE TABLE `fx67ll_ai_prompt_scene` (
  `scene_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '场景ID（主键）',
  `scene_code` varchar(233) NOT NULL COMMENT '场景编码（如football_pre_match，唯一标识场景，用于接口/配置关联）',
  `scene_name` varchar(233) NOT NULL COMMENT '场景名称',
  `scene_desc` varchar(1023) DEFAULT '' COMMENT '场景描述',
  `scene_remark` varchar(1023) DEFAULT '' COMMENT '备注',
  `scene_status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `scene_sort` int(4) DEFAULT 0 COMMENT '排序（用于前端展示，数值越小越靠前）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  PRIMARY KEY (`scene_id`),
  UNIQUE KEY `uk_scene_code` (`scene_code`) COMMENT '场景编码唯一索引：防止场景编码重复',
  KEY `idx_scene_status` (`scene_status`) COMMENT '场景状态索引（快速筛选启用/停用场景）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI Prompt场景管理表';

CREATE TABLE `fx67ll_ai_prompt_model` (
  `model_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '模型ID（主键）',
  `model_code` varchar(233) NOT NULL COMMENT '模型编码（如deepseek_chat/doubao_pro）',
  `model_name` varchar(233) NOT NULL COMMENT '模型名称（如DeepSeek通用对话模型/豆包Pro）',
  `model_vendor` varchar(233) NOT NULL COMMENT '厂商标识（如deepseek/doubao/tongyi）',
  `model_api_key` varchar(1023) NOT NULL COMMENT 'API密钥键（关联fx67ll_secret_key.secret_key，字段类型varchar(1023)）',
  `model_secret_key` varchar(1023) DEFAULT '' COMMENT 'Secret密钥键（关联fx67ll_secret_key.secret_key，可为空）',
  `model_api_url` varchar(233) NOT NULL COMMENT '模型API调用地址',
  `model_api_version` varchar(10) DEFAULT 'v1' COMMENT 'API版本（如v1/v3，贴合主流模型默认值）',
  `model_config_params` text NOT NULL COMMENT '模型配置参数（JSON格式字符串，兼容所有模型参数）',
  `model_request_header` text COMMENT '请求头配置（JSON格式字符串）',
  `model_remark` varchar(1023) DEFAULT '' COMMENT '备注',
  `model_status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `model_sort` int(4) DEFAULT 0 COMMENT '排序（前端展示用，数值越小越靠前）',
  `model_token_price` decimal(10,6) DEFAULT 0.000000 COMMENT '每1000 token价格（元），用于计算调用费用',
  `model_token_currency` varchar(10) DEFAULT 'CNY' COMMENT '计价货币（默认CNY，支持USD等）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  PRIMARY KEY (`model_id`),
  UNIQUE KEY `uk_model_code` (`model_code`) COMMENT '模型编码唯一索引：避免重复配置同一模型',
  KEY `idx_model_vendor_status` (`model_vendor`, `model_status`) COMMENT '厂商+状态组合索引（快速筛选某厂商的启用模型）',
  KEY `idx_model_status` (`model_status`) COMMENT '模型状态索引（快速筛选启用/停用模型）',
  CONSTRAINT `fk_model_api_key` FOREIGN KEY (`model_api_key`) REFERENCES `fx67ll_secret_key`(`secret_key`) ON DELETE RESTRICT COMMENT 'API密钥外键：删除密钥时禁止删除关联模型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI Prompt模型配置表';

CREATE TABLE `fx67ll_ai_prompt_limit_rule` (
  `limit_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '限流/熔断规则主键ID',
  `limit_dimension` char(2) NOT NULL COMMENT '规则维度（1:模型 2:模板 3:场景 4:分组）',
  `limit_target_id` bigint(20) NOT NULL COMMENT '关联维度ID（如model_id/prompt_id/scene_id/group_id）',
  `limit_type` char(2) NOT NULL COMMENT '规则类型（1:流控 2:熔断）',
  `flow_control_mode` char(1) DEFAULT 'D' COMMENT '流控模式（D:直接  A:关联  L:链路，仅limit_type=1时有效）',
  `flow_control_effect` char(1) DEFAULT 'F' COMMENT '流控效果（F:快速失败 W:WarmUp Q:排队等待，仅limit_type=1时有效）',
  `flow_rule_type` char(1) DEFAULT 'Q' COMMENT '流控规则类型（Q:QPS C:并发数，仅limit_type=1时有效）',
  `flow_threshold` decimal(10,2) NOT NULL COMMENT '流控阈值（QPS/并发数，保留2位小数）',
  `circuit_strategy` char(1) DEFAULT 'S' COMMENT '熔断策略（S:慢调用比例 E:异常比例 N:异常数，仅limit_type=2时有效）',
  `circuit_threshold` decimal(10,2) DEFAULT 0.5 COMMENT '熔断阈值（慢调用比例/异常比例:0-1；异常数:整数，保留2位小数）',
  `circuit_grade` int(4) DEFAULT 500 COMMENT '慢调用阈值（ms，仅circuit_strategy=S时有效）',
  `circuit_window` int(4) DEFAULT 10000 COMMENT '统计窗口时长（ms，默认10s，仅limit_type=2时有效）',
  `circuit_timeout` int(4) DEFAULT 5000 COMMENT '熔断恢复时间（ms，默认5s，仅limit_type=2时有效）',
  `limit_status` char(1) DEFAULT '0' COMMENT '规则状态（0:启用 1:停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  PRIMARY KEY (`limit_id`),
  KEY `idx_limit_dim_target` (`limit_dimension`, `limit_target_id`) COMMENT '维度+目标ID索引（快速查询某维度下的规则）',
  KEY `idx_limit_type_status` (`limit_type`, `limit_status`) COMMENT '规则类型+状态索引（筛选启用的流控/熔断规则）',
  KEY `idx_limit_dim_type_status` (`limit_dimension`, `limit_type`, `limit_status`) COMMENT '维度+类型+状态索引（高频查询：某维度启用的流控/熔断规则）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI Prompt 限流/熔断规则表（适配Sentinel框架）';

CREATE TABLE `fx67ll_ai_request_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID（主键）',
  `prompt_id` bigint(20) DEFAULT NULL COMMENT '使用的模板ID（可为空，若直接调用非模板）',
  `scene_id` bigint(20) DEFAULT NULL COMMENT '关联场景ID',
  `model_id` bigint(20) NOT NULL COMMENT '调用的模型ID',
  `model_vendor` varchar(233) NOT NULL COMMENT '模型厂商（冗余存储，避免关联查询）',
  `request_content` text COMMENT '请求内容（含最终渲染后的Prompt）',
  `response_content` text COMMENT '响应内容（大文本存储）',
  `prompt_tokens` int(11) DEFAULT 0 COMMENT '输入token数',
  `completion_tokens` int(11) DEFAULT 0 COMMENT '输出token数',
  `total_tokens` int(11) DEFAULT 0 COMMENT '总token数',
  `cost` decimal(10,6) DEFAULT 0.000000 COMMENT '预估费用（元）',
  `duration_ms` int(11) DEFAULT 0 COMMENT '请求耗时（毫秒）',
  `http_status` int(3) DEFAULT NULL COMMENT 'HTTP状态码',
  `call_status` char(2) DEFAULT '00' COMMENT '调用状态（00:成功 01:失败 02:限流 03:熔断）',
  `error_msg` text COMMENT '错误信息',
  `caller_ip` varchar(233) DEFAULT '' COMMENT '调用者IP',
  `request_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '请求时间（精确到秒）',
  `create_by` varchar(64) DEFAULT '' COMMENT '调用者标识',
  PRIMARY KEY (`log_id`, `request_time`),
  KEY `idx_request_time` (`request_time`),
  KEY `idx_prompt_id` (`prompt_id`),
  KEY `idx_scene_id` (`scene_id`),
  KEY `idx_model_id` (`model_id`),
  KEY `idx_create_by` (`create_by`),
  KEY `idx_call_status` (`call_status`),
  KEY `idx_request_time_vendor` (`request_time`, `model_vendor`),
  KEY `idx_scene_model_time` (`scene_id`, `model_id`, `request_time`),
  KEY `idx_create_by_time` (`create_by`, `request_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 
PARTITION BY RANGE (TO_DAYS(request_time)) (
  PARTITION p202603 VALUES LESS THAN (TO_DAYS('2026-04-01')), -- 2026年3月
  PARTITION p202604 VALUES LESS THAN (TO_DAYS('2026-05-01')), -- 2026年4月
  PARTITION p202605 VALUES LESS THAN (TO_DAYS('2026-06-01')), -- 2026年5月
  PARTITION p_default VALUES LESS THAN MAXVALUE -- 默认分区（防止未提前创建月份分区）
) COMMENT='AI 调用请求日志表（按request_time月份分区）';

CREATE TABLE `fx67ll_ai_request_daily_log` (
  `daily_log_date` date NOT NULL COMMENT '统计日期（yyyy-MM-dd）',
  `model_id` bigint(20) NOT NULL COMMENT '模型ID',
  `scene_id` bigint(20) NOT NULL COMMENT '场景ID（可选，若需要场景级统计）',
  `total_requests` int(11) DEFAULT 0 COMMENT '总请求次数',
  `fail_requests` int(11) DEFAULT 0 COMMENT '失败请求次数',
  `limit_requests` int(11) DEFAULT 0 COMMENT '限流请求次数',
  `circuit_requests` int(11) DEFAULT 0 COMMENT '熔断请求次数',
  `total_prompt_tokens` bigint(20) DEFAULT 0 COMMENT '总输入token数',
  `total_completion_tokens` bigint(20) DEFAULT 0 COMMENT '总输出token数',
  `total_cost` decimal(12,6) DEFAULT 0.000000 COMMENT '总费用（元）',
  `avg_duration_ms` int(11) DEFAULT 0 COMMENT '平均耗时（毫秒，计算逻辑：总耗时/总请求数）',
  PRIMARY KEY (`daily_log_date`, `model_id`, `scene_id`) COMMENT '复合主键：日期+模型+场景（唯一确定一条日统计记录）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI 调用请求日统计日志表';

CREATE TABLE `fx67ll_ai_request_monthly_log` (
  `monthly_log_month` varchar(7) NOT NULL COMMENT '统计年月（yyyy-MM）',
  `model_id` bigint(20) NOT NULL COMMENT '模型ID',
  `scene_id` bigint(20) NOT NULL COMMENT '场景ID（可选，若需要场景级统计）',
  `total_requests` int(11) DEFAULT 0 COMMENT '月总请求次数',
  `fail_requests` int(11) DEFAULT 0 COMMENT '月失败请求次数',
  `limit_requests` int(11) DEFAULT 0 COMMENT '月限流请求次数',
  `circuit_requests` int(11) DEFAULT 0 COMMENT '月熔断请求次数',
  `total_prompt_tokens` bigint(20) DEFAULT 0 COMMENT '月总输入token数',
  `total_completion_tokens` bigint(20) DEFAULT 0 COMMENT '月总输出token数',
  `total_cost` decimal(12,6) DEFAULT 0.000000 COMMENT '月总费用（元）',
  `avg_duration_ms` int(11) DEFAULT 0 COMMENT '月平均耗时（毫秒）',
  PRIMARY KEY (`monthly_log_month`, `model_id`, `scene_id`) COMMENT '复合主键：年月+模型+场景（唯一确定一条月统计记录）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI 调用请求月统计日志表';

CREATE TABLE `fx67ll_ai_request_yearly_log` (
  `yearly_log_year` varchar(4) NOT NULL COMMENT '统计年份（yyyy）',
  `model_id` bigint(20) NOT NULL COMMENT '模型ID',
  `scene_id` bigint(20) NOT NULL COMMENT '场景ID（可选，若需要场景级统计）',
  `total_requests` bigint(20) DEFAULT 0 COMMENT '年总请求次数',
  `total_prompt_tokens` bigint(20) DEFAULT 0 COMMENT '年总输入token数',
  `total_completion_tokens` bigint(20) DEFAULT 0 COMMENT '年总输出token数',
  `total_cost` decimal(14,6) DEFAULT 0.000000 COMMENT '年总费用（元，保留6位小数）',
  `avg_duration_ms` int(11) DEFAULT 0 COMMENT '年平均耗时（毫秒）',
  PRIMARY KEY (`yearly_log_year`, `model_id`, `scene_id`) COMMENT '复合主键：年份+模型+场景（唯一确定一条年统计记录）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI 调用请求年统计日志表';



-- ----------------------------
-- 14、比赛赛前分析相关表设计
-- ----------------------------
-- 豆包待整合处理
CREATE TABLE `fx67ll_football_season` (
  `season_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '赛季ID（主键）',
  `season_code` varchar(50) NOT NULL COMMENT '赛季编码（如2025-2026，唯一标识）',
  `season_name` varchar(100) NOT NULL COMMENT '赛季名称（如2025-2026赛季）',
  `season_desc` varchar(500) DEFAULT '' COMMENT '赛季描述（如“包含欧冠、英超等主流赛事”）',
  `start_date` date DEFAULT NULL COMMENT '赛季开始日期',
  `end_date` date DEFAULT NULL COMMENT '赛季结束日期',
  `season_status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `season_sort` int(4) DEFAULT 0 COMMENT '排序（前端展示用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  PRIMARY KEY (`season_id`),
  UNIQUE KEY `uk_season_code` (`season_code`) COMMENT '赛季编码唯一索引',
  KEY `idx_season_status` (`season_status`) COMMENT '赛季状态索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='足球赛季管理表';
CREATE TABLE `fx67ll_football_team` (
  `team_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '球队ID（主键）',
  `team_code` varchar(50) NOT NULL COMMENT '球队编码（如man_city、real_madrid，唯一标识）',
  `team_name` varchar(100) NOT NULL COMMENT '球队名称（如曼城、皇家马德里）',
  `team_name_en` varchar(100) DEFAULT '' COMMENT '球队英文名称（如Manchester City）',
  `team_logo_url` varchar(500) DEFAULT '' COMMENT '球队Logo URL',
  `country` varchar(50) DEFAULT '' COMMENT '所属国家/地区（如英格兰、西班牙）',
  `team_desc` varchar(500) DEFAULT '' COMMENT '球队简介',
  `team_status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `team_sort` int(4) DEFAULT 0 COMMENT '排序（前端展示用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  PRIMARY KEY (`team_id`),
  UNIQUE KEY `uk_team_code` (`team_code`) COMMENT '球队编码唯一索引',
  KEY `idx_team_status` (`team_status`) COMMENT '球队状态索引',
  KEY `idx_country` (`country`) COMMENT '所属国家索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='足球球队管理表';
CREATE TABLE `fx67ll_football_match` (
  `match_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '比赛ID（主键）',
  `match_code` varchar(50) NOT NULL COMMENT '比赛编码（如20250301_man_city_real_madrid，唯一标识）',
  `season_id` bigint(20) NOT NULL COMMENT '关联赛季ID（外键）',
  `home_team_id` bigint(20) NOT NULL COMMENT '主队ID（外键）',
  `away_team_id` bigint(20) NOT NULL COMMENT '客队ID（外键）',
  `match_time` datetime NOT NULL COMMENT '比赛时间',
  `match_venue` varchar(100) DEFAULT '' COMMENT '比赛场地（如伊蒂哈德球场）',
  `match_desc` varchar(500) DEFAULT '' COMMENT '比赛简介（如欧冠1/4决赛首回合）',
  `match_status` char(1) DEFAULT '0' COMMENT '比赛状态（0未开始 1进行中 2已结束）',
  `analysis_status` char(1) DEFAULT '0' COMMENT '分析状态（0未分析 1已分析）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  PRIMARY KEY (`match_id`),
  UNIQUE KEY `uk_match_code` (`match_code`) COMMENT '比赛编码唯一索引',
  KEY `idx_season_id` (`season_id`) COMMENT '赛季ID索引',
  KEY `idx_home_team_id` (`home_team_id`) COMMENT '主队ID索引',
  KEY `idx_away_team_id` (`away_team_id`) COMMENT '客队ID索引',
  KEY `idx_match_time` (`match_time`) COMMENT '比赛时间索引',
  KEY `idx_match_status` (`match_status`) COMMENT '比赛状态索引',
  KEY `idx_analysis_status` (`analysis_status`) COMMENT '分析状态索引',
  FOREIGN KEY (`season_id`) REFERENCES `fx67ll_football_season`(`season_id`) ON DELETE RESTRICT,
  FOREIGN KEY (`home_team_id`) REFERENCES `fx67ll_football_team`(`team_id`) ON DELETE RESTRICT,
  FOREIGN KEY (`away_team_id`) REFERENCES `fx67ll_football_team`(`team_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='足球比赛记录表';
CREATE TABLE `fx67ll_football_match_analysis` (
  `analysis_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分析ID（主键）',
  `match_id` bigint(20) NOT NULL COMMENT '关联比赛ID（外键）',
  `prompt_id` bigint(20) NOT NULL COMMENT '使用的Prompt模板ID（外键，关联fx67ll_ai_prompt_template）',
  `model_id` bigint(20) NOT NULL COMMENT '使用的模型ID（外键，关联fx67ll_ai_prompt_model）',
  `ai_request_log_id` bigint(20) DEFAULT NULL COMMENT '关联AI调用日志ID（外键，关联fx67ll_ai_request_log）',
  `raw_prompt` text NOT NULL COMMENT '最终渲染后的Prompt（含球队/比赛数据）',
  `raw_ai_response` text NOT NULL COMMENT 'AI返回的原始JSON结果',
  `analysis_remark` varchar(500) DEFAULT '' COMMENT '分析备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  PRIMARY KEY (`analysis_id`),
  KEY `idx_match_id` (`match_id`) COMMENT '比赛ID索引',
  KEY `idx_prompt_id` (`prompt_id`) COMMENT 'Prompt模板ID索引',
  KEY `idx_model_id` (`model_id`) COMMENT '模型ID索引',
  KEY `idx_create_time` (`create_time`) COMMENT '创建时间索引',
  FOREIGN KEY (`match_id`) REFERENCES `fx67ll_football_match`(`match_id`) ON DELETE RESTRICT,
  FOREIGN KEY (`prompt_id`) REFERENCES `fx67ll_ai_prompt_template`(`prompt_id`) ON DELETE RESTRICT,
  FOREIGN KEY (`model_id`) REFERENCES `fx67ll_ai_prompt_model`(`model_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='足球比赛AI分析原始结果表';

-- Deepseek待整合处理
CREATE TABLE `fx67ll_football_team` (
  `team_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '球队ID',
  `team_name` varchar(100) NOT NULL COMMENT '球队全名',
  `team_short_name` varchar(50) DEFAULT '' COMMENT '球队简称',
  `country` varchar(50) DEFAULT '' COMMENT '所属国家/地区',
  `logo_url` varchar(255) DEFAULT '' COMMENT '队标URL',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  PRIMARY KEY (`team_id`),
  KEY `idx_team_name` (`team_name`) COMMENT '按名称查询'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='足球球队信息表';
CREATE TABLE `fx67ll_football_season` (
  `season_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '赛季ID',
  `season_name` varchar(50) NOT NULL COMMENT '赛季名称（如2025-2026）',
  `start_date` date DEFAULT NULL COMMENT '开始日期',
  `end_date` date DEFAULT NULL COMMENT '结束日期',
  `is_current` tinyint(1) DEFAULT 0 COMMENT '是否当前赛季（1是 0否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  PRIMARY KEY (`season_id`),
  UNIQUE KEY `uk_season_name` (`season_name`) COMMENT '赛季名称唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='足球赛季管理表';
CREATE TABLE `fx67ll_football_match_analysis` (
  `match_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '比赛分析ID',
  `season_id` bigint(20) NOT NULL COMMENT '所属赛季ID',
  `home_team_id` bigint(20) NOT NULL COMMENT '主队ID',
  `away_team_id` bigint(20) NOT NULL COMMENT '客队ID',
  `match_time` datetime DEFAULT NULL COMMENT '比赛时间',
  `prompt_id` bigint(20) DEFAULT NULL COMMENT '使用的Prompt模板ID（关联fx67ll_ai_prompt_template）',
  `ai_raw_output` text COMMENT 'AI生成的原始分析文本',
  `analysis_result` text COMMENT '结构化分析结果（JSON格式，如评分、预测等）',
  `home_score` decimal(5,2) DEFAULT NULL COMMENT '主队评分/预测得分',
  `away_score` decimal(5,2) DEFAULT NULL COMMENT '客队评分/预测得分',
  `analysis_conclusion` varchar(20) DEFAULT '' COMMENT '分析结论（如主胜/平/客胜）',
  `analysis_status` char(2) DEFAULT '00' COMMENT '分析状态（00待分析 01已完成 02失败）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0存在 2删除）',
  PRIMARY KEY (`match_id`),
  KEY `idx_season_id` (`season_id`) COMMENT '按赛季查询',
  KEY `idx_home_team` (`home_team_id`) COMMENT '按主队查询',
  KEY `idx_away_team` (`away_team_id`) COMMENT '按客队查询',
  KEY `idx_match_time` (`match_time`) COMMENT '按比赛时间排序',
  KEY `idx_prompt_id` (`prompt_id`) COMMENT '关联模板',
  FOREIGN KEY (`season_id`) REFERENCES `fx67ll_football_season`(`season_id`) ON DELETE RESTRICT,
  FOREIGN KEY (`home_team_id`) REFERENCES `fx67ll_football_team`(`team_id`) ON DELETE RESTRICT,
  FOREIGN KEY (`away_team_id`) REFERENCES `fx67ll_football_team`(`team_id`) ON DELETE RESTRICT,
  FOREIGN KEY (`prompt_id`) REFERENCES `fx67ll_ai_prompt_template`(`prompt_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='足球比赛分析记录表';


































