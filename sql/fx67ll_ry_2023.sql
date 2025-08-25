-- ----------------------------
-- 11、字典类型表
-- ----------------------------
drop table if exists sys_dict_type;
create table sys_dict_type
(
  dict_id          bigint(20)      not null auto_increment    comment '字典主键',
  dict_name        varchar(100)    default ''                 comment '字典名称',
  dict_type        varchar(100)    default ''                 comment '字典类型',
  status           char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                   comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                   comment '更新时间',
  remark           varchar(500)    default null               comment '备注',
  primary key (dict_id),
  unique (dict_type)
) engine=innodb auto_increment=100 comment = '字典类型表';

insert into sys_dict_type values(1,  '用户性别',   'sys_user_sex',            '0', 'admin', sysdate(), '', null, '用户性别列表');
insert into sys_dict_type values(2,  '菜单状态',   'sys_show_hide',           '0', 'admin', sysdate(), '', null, '菜单状态列表');
insert into sys_dict_type values(3,  '系统开关',   'sys_normal_disable',      '0', 'admin', sysdate(), '', null, '系统开关列表');
insert into sys_dict_type values(4,  '任务状态',   'sys_job_status',          '0', 'admin', sysdate(), '', null, '任务状态列表');
insert into sys_dict_type values(5,  '任务分组',   'sys_job_group',           '0', 'admin', sysdate(), '', null, '任务分组列表');
insert into sys_dict_type values(6,  '系统是否',   'sys_yes_no',              '0', 'admin', sysdate(), '', null, '系统是否列表');
insert into sys_dict_type values(7,  '通知类型',   'sys_notice_type',         '0', 'admin', sysdate(), '', null, '通知类型列表');
insert into sys_dict_type values(8,  '通知状态',   'sys_notice_status',       '0', 'admin', sysdate(), '', null, '通知状态列表');
insert into sys_dict_type values(9,  '操作类型',   'sys_oper_type',           '0', 'admin', sysdate(), '', null, '操作类型列表');
insert into sys_dict_type values(10, '系统状态',   'sys_common_status',       '0', 'admin', sysdate(), '', null, '登录状态列表');

-- 彩票管理枚举项
insert into sys_dict_type values(11, '彩票类型',   'fx67ll_lottery_type',     '0', 'admin', sysdate(), '', null, '彩票类型列表');
insert into sys_dict_type values(12, '星期枚举',   'sys_week_type',           '0', 'admin', sysdate(), '', null, '星期枚举列表');

-- 打卡管理枚举项
insert into sys_dict_type values(13, '打卡类型',   'fx67ll_punch_type',       '0', 'admin', sysdate(), '', null, '打卡类型列表');

-- 极简面试题管理枚举项
insert into sys_dict_type values(14, '学科类型',   'fx67ll_subject_type',     '0', 'admin', sysdate(), '', null, '学科类型列表');
insert into sys_dict_type values(15, '问题类型',   'fx67ll_question_type',    '0', 'admin', sysdate(), '', null, '问题类型列表');



-- ----------------------------
-- 12、字典数据表
-- ----------------------------
drop table if exists sys_dict_data;
create table sys_dict_data
(
  dict_code        bigint(20)      not null auto_increment    comment '字典编码',
  dict_sort        int(4)          default 0                  comment '字典排序',
  dict_label       varchar(100)    default ''                 comment '字典标签',
  dict_value       varchar(100)    default ''                 comment '字典键值',
  dict_type        varchar(100)    default ''                 comment '字典类型',
  css_class        varchar(100)    default null               comment '样式属性（其他样式扩展）',
  list_class       varchar(100)    default null               comment '表格回显样式',
  is_default       char(1)         default 'N'                comment '是否默认（Y是 N否）',
  status           char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                   comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                   comment '更新时间',
  remark           varchar(500)    default null               comment '备注',
  primary key (dict_code)
) engine=innodb auto_increment=100 comment = '字典数据表';

insert into sys_dict_data values(1,  1,  '男',       '0',       'sys_user_sex',        '',   '',        'Y', '0', 'admin', sysdate(), '', null, '性别男');
insert into sys_dict_data values(2,  2,  '女',       '1',       'sys_user_sex',        '',   '',        'N', '0', 'admin', sysdate(), '', null, '性别女');
insert into sys_dict_data values(3,  3,  '未知',     '2',       'sys_user_sex',        '',   '',        'N', '0', 'admin', sysdate(), '', null, '性别未知');
insert into sys_dict_data values(4,  1,  '显示',     '0',       'sys_show_hide',       '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '显示菜单');
insert into sys_dict_data values(5,  2,  '隐藏',     '1',       'sys_show_hide',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '隐藏菜单');
insert into sys_dict_data values(6,  1,  '正常',     '0',       'sys_normal_disable',  '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data values(7,  2,  '停用',     '1',       'sys_normal_disable',  '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '停用状态');
insert into sys_dict_data values(8,  1,  '正常',     '0',       'sys_job_status',      '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data values(9,  2,  '暂停',     '1',       'sys_job_status',      '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '停用状态');
insert into sys_dict_data values(10, 1,  '默认',     'DEFAULT', 'sys_job_group',       '',   '',        'Y', '0', 'admin', sysdate(), '', null, '默认分组');
insert into sys_dict_data values(11, 2,  '系统',     'SYSTEM',  'sys_job_group',       '',   '',        'N', '0', 'admin', sysdate(), '', null, '系统分组');
insert into sys_dict_data values(12, 1,  '是',       'Y',       'sys_yes_no',          '',   'success', 'Y', '0', 'admin', sysdate(), '', null, '系统默认是');
insert into sys_dict_data values(13, 2,  '否',       'N',       'sys_yes_no',          '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '系统默认否');
insert into sys_dict_data values(14, 1,  '通知',     '1',       'sys_notice_type',     '',   'warning', 'Y', '0', 'admin', sysdate(), '', null, '通知');
insert into sys_dict_data values(15, 2,  '公告',     '2',       'sys_notice_type',     '',   'success', 'N', '0', 'admin', sysdate(), '', null, '公告');
insert into sys_dict_data values(16, 1,  '正常',     '0',       'sys_notice_status',   '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data values(17, 2,  '关闭',     '1',       'sys_notice_status',   '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '关闭状态');
insert into sys_dict_data values(18, 99, '其他',     '0',       'sys_oper_type',       '',   'info',    'N', '0', 'admin', sysdate(), '', null, '其他操作');
insert into sys_dict_data values(19, 1,  '新增',     '1',       'sys_oper_type',       '',   'info',    'N', '0', 'admin', sysdate(), '', null, '新增操作');
insert into sys_dict_data values(20, 2,  '修改',     '2',       'sys_oper_type',       '',   'info',    'N', '0', 'admin', sysdate(), '', null, '修改操作');
insert into sys_dict_data values(21, 3,  '删除',     '3',       'sys_oper_type',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '删除操作');
insert into sys_dict_data values(22, 4,  '授权',     '4',       'sys_oper_type',       '',   'primary', 'N', '0', 'admin', sysdate(), '', null, '授权操作');
insert into sys_dict_data values(23, 5,  '导出',     '5',       'sys_oper_type',       '',   'warning', 'N', '0', 'admin', sysdate(), '', null, '导出操作');
insert into sys_dict_data values(24, 6,  '导入',     '6',       'sys_oper_type',       '',   'warning', 'N', '0', 'admin', sysdate(), '', null, '导入操作');
insert into sys_dict_data values(25, 7,  '强退',     '7',       'sys_oper_type',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '强退操作');
insert into sys_dict_data values(26, 8,  '生成代码', '8',       'sys_oper_type',       '',   'warning', 'N', '0', 'admin', sysdate(), '', null, '生成操作');
insert into sys_dict_data values(27, 9,  '清空数据', '9',       'sys_oper_type',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '清空操作');
insert into sys_dict_data values(28, 1,  '成功',     '0',       'sys_common_status',   '',   'primary', 'N', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data values(29, 2,  '失败',     '1',       'sys_common_status',   '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '停用状态');
insert into sys_dict_data values(28, 1,  '成功',     '0',       'sys_common_status',   '',   'primary', 'N', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data values(29, 2,  '失败',     '1',       'sys_common_status',   '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '停用状态');

-- 彩票管理枚举项插入值
insert into sys_dict_data values(30, 1,  '大乐透',   '1',       'fx67ll_lottery_type', '',   '',        'N', '0', 'admin', sysdate(), '', null, '大乐透');
insert into sys_dict_data values(31, 2,  '双色球',   '2',       'fx67ll_lottery_type', '',   '',        'N', '0', 'admin', sysdate(), '', null, '双色球');
insert into sys_dict_data values(32, 1,  '星期一',   '1',       'sys_week_type',       '',   '',        'N', '0', 'admin', sysdate(), '', null, '星期一');
insert into sys_dict_data values(33, 2,  '星期二',   '2',       'sys_week_type',       '',   '',        'N', '0', 'admin', sysdate(), '', null, '星期二');
insert into sys_dict_data values(34, 3,  '星期三',   '3',       'sys_week_type',       '',   '',        'N', '0', 'admin', sysdate(), '', null, '星期三');
insert into sys_dict_data values(35, 4,  '星期四',   '4',       'sys_week_type',       '',   '',        'N', '0', 'admin', sysdate(), '', null, '星期四');
insert into sys_dict_data values(36, 5,  '星期五',   '5',       'sys_week_type',       '',   '',        'N', '0', 'admin', sysdate(), '', null, '星期五');
insert into sys_dict_data values(37, 6,  '星期六',   '6',       'sys_week_type',       '',   '',        'N', '0', 'admin', sysdate(), '', null, '星期六');
insert into sys_dict_data values(38, 7,  '星期日',   '7',       'sys_week_type',       '',   '',        'N', '0', 'admin', sysdate(), '', null, '星期日');

-- 打卡管理枚举项插入值
insert into sys_dict_data values(39, 1,  '上班打卡', '1',       'fx67ll_punch_type',   '',   '',        'N', '0', 'admin', sysdate(), '', null, '上班打卡');
insert into sys_dict_data values(40, 2,  '下班打卡', '2',       'fx67ll_punch_type',   '',   '',        'N', '0', 'admin', sysdate(), '', null, '下班打卡');

-- 极简面试题管理学科类型枚举项插入值
insert into sys_dict_data values(41, 1,  '前端',     '1',       'fx67ll_subject_type',    '',   '',     'N', '0', 'admin', sysdate(), '', null, '前端');
insert into sys_dict_data values(42, 2,  '后端',     '2',       'fx67ll_subject_type',    '',   '',     'N', '0', 'admin', sysdate(), '', null, '后端');
insert into sys_dict_data values(43, 3,  '大数据',   '3',       'fx67ll_subject_type',    '',   '',     'N', '0', 'admin', sysdate(), '', null, '大数据');

-- 极简面试题管理问题类型枚举项插入值
insert into sys_dict_data values(47, 1,  '单选题',   '1',       'fx67ll_question_type',   '',   '',     'N', '0', 'admin', sysdate(), '', null, '单选题');
insert into sys_dict_data values(48, 2,  '多选题',   '2',       'fx67ll_question_type',   '',   '',     'N', '0', 'admin', sysdate(), '', null, '多选题');
insert into sys_dict_data values(49, 3,  '解答题',   '3',       'fx67ll_question_type',   '',   '',     'N', '0', 'admin', sysdate(), '', null, '解答题');

-- 彩票管理第二期新增的枚举项插入值
insert into sys_dict_data values(44, 3,  '排列三',   '3',       'fx67ll_lottery_type',    '',   '',     'N', '0', 'admin', sysdate(), '', null, '排列三');
insert into sys_dict_data values(45, 4,  '排列五',   '4',       'fx67ll_lottery_type',    '',   '',     'N', '0', 'admin', sysdate(), '', null, '排列五');
insert into sys_dict_data values(46, 5,  '七星彩',   '5',       'fx67ll_lottery_type',    '',   '',     'N', '0', 'admin', sysdate(), '', null, '七星彩');

