-- ----------------------------
-- 1、号码日志记录表
-- ----------------------------
drop table if exists fx67ll_lottory_log;
create table fx67ll_lottory_log (
  lottory_id           bigint(20)      not null auto_increment    comment '号码日志主键',
  record_number        varchar(1023)   not null default ''        comment '当日购买号码',
  chase_number         varchar(1023)   default ''                 comment '当日固定追号',
  winning_number       varchar(14)     default ''                 comment '当日中奖号码',
  number_type          int(1)          not null                   comment '当日购买的彩票类型（1大乐透 2双色球）',
  week_type            int(1)          not null                   comment '星期几（1周一 2周二 3周三 4周四 5周五 6周六 7周日）',
  has_more_purchases   char(1)         not null default 'N'       comment '是否有追加购买（Y是 N否）',
  sort                 int             not null                   comment '排序',
  del_flag             char(1)         not null default '0'       comment '删除标志（0代表存在 2代表删除）',
  user_id              bigint(20)      not null                   comment '用户ID',
  create_by            varchar(64)     not null default ''        comment '记录创建者',
  create_time 	       datetime        not null                   comment '记录创建时间',
  update_by            varchar(64)     default ''                 comment '记录更新者',
  update_time          datetime                                   comment '记录更新时间',
  primary key (lottory_id)
) engine=innodb auto_increment=1 comment = '号码日志记录表';


-- ----------------------------
-- 2、固定追号配置表
-- ----------------------------
drop table if exists fx67ll_lottory_chase;
create table fx67ll_lottory_chase (
  chase_id          bigint(20)      not null auto_increment    comment '固定追号主键',
  chase_number      varchar(1023)   not null default ''        comment '每日固定追号',
  number_type       int(1)          not null                   comment '固定追号的彩票类型（1大乐透 2双色球）',
  week_type         int(1)          not null                   comment '星期几的固定追号（1周一 2周二 3周三 4周四 5周五 6周六 7周日）',
  sort              int             not null                   comment '排序',
  del_flag          char(1)         not null default '0'       comment '删除标志（0代表存在 2代表删除）',
  user_id           bigint(20)      not null                   comment '用户ID',
  create_by         varchar(64)     not null default ''        comment '记录创建者',
  create_time 	    datetime        not null                   comment '记录创建时间',
  update_by         varchar(64)     default ''                 comment '记录更新者',
  update_time       datetime                                   comment '记录更新时间',
  primary key (chase_id)
) engine=innodb auto_increment=1 comment = '固定追号配置表';


-- ----------------------------
-- 3、个人彩票生成配置表
-- ----------------------------
drop table if exists fx67ll_lottory_setting;
create table fx67ll_lottory_setting (
  user_id           bigint(20)      not null auto_increment    comment '用户ID',
  chase_number      varchar(1023)   not null default ''        comment '个人彩票生成配置',
  del_flag          char(1)         not null default '0'       comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     not null default ''        comment '记录创建者',
  create_time 	    datetime        not null                   comment '记录创建时间',
  update_by         varchar(64)     default ''                 comment '记录更新者',
  update_time       datetime                                   comment '记录更新时间',
  primary key (user_id)
) engine=innodb auto_increment=1 comment = '固定追号配置表';

