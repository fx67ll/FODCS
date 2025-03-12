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
-- 7、简易面试题记录表
-- ----------------------------
drop table if exists fx67ll_interview_simple_log;

create table fx67ll_interview_simple_log (
  interview_simple_id                bigint(20)      not null auto_increment    comment '简易面试题记录主键',
  interview_simple_type              char(1)                                    comment '简易面试题类型（1代表前端 2代表后端 3代表大数据）',
  interview_simple_question_title    varchar(19999)   default ''                comment '简易面试题问题描述',
  interview_simple_answer_content    varchar(19999)   default ''                comment '简易面试题答案详解',
  interview_simple_remark            varchar(19999)   default ''                comment '简易面试题备注',
  del_flag                           char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  user_id                            bigint(20)                                 comment '用户ID',
  create_by                          varchar(64)     default ''                 comment '记录创建者',
  create_time 	                     datetime                                   comment '记录创建时间',
  update_by                          varchar(64)     default ''                 comment '记录更新者',
  update_time                        datetime                                   comment '记录更新时间',
  primary key (interview_simple_id)
) engine=innodb auto_increment=1 comment = '简易面试题记录表';


    
   