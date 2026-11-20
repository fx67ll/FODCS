-- =====================================================
-- 球队管理表 批量导入数据脚本
-- 适用表：fx67ll_dortmund_team
-- 数据范围：2026-2027赛季五大联赛 + 2026赛季日职J1/J2
-- 排序规则：全局统一排序，team_sort 从1开始逐条递增
-- 说明：必填字段完整填充，logo地址已按规则填充，user_id默认1
-- =====================================================

-- 清空原有数据（可选，按需打开）
-- TRUNCATE TABLE fx67ll_dortmund_team;

-- ------------------------------
-- 一、德甲（18支）
-- ------------------------------
INSERT INTO `fx67ll_dortmund_team` 
(`team_code`, `team_name`, `team_name_short`, `team_name_en`, `team_logo_url`, `team_venue`, `team_country`, `team_tag`, `team_status`, `team_sort`, `team_remark`, `user_id`, `create_by`, `update_by`, `del_flag`)
VALUES
('DORTMUND', '多特蒙德足球俱乐部', '我横', 'Borussia Dortmund', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=9&avatarPadding=53', '伊杜纳信号公园球场', '德国', '攻势足球,青年近卫军,主场龙', '0', 1, '', 1, '', '', '0'),
('BAYERN_MUNCHEN', '拜仁慕尼黑足球俱乐部', '拜仁', 'FC Bayern München', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=6&avatarPadding=48', '安联球场', '德国', '争冠热门,老牌劲旅,主场龙', '0', 2, '', 1, '', '', '0'),
('LEVERKUSEN', '勒沃库森足球俱乐部', '勒沃库森', 'Bayer 04 Leverkusen', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=4&avatarPadding=42', '拜耳竞技场', '德国', '争冠梯队,攻势足球', '0', 3, '', 1, '', '', '0'),
('RB_LEIPZIG', '莱比锡红牛足球俱乐部', '莱比锡', 'RB Leipzig', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=11&avatarPadding=57', '红牛竞技场', '德国', '高位逼抢,青年近卫军,客场龙', '0', 4, '', 1, '', '', '0'),
('MOENCHENGLADBACH', '门兴格拉德巴赫足球俱乐部', '门兴', 'Borussia Mönchengladbach', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=7&avatarPadding=50', '普鲁士公园球场', '德国', '老牌劲旅,中游混子', '0', 5, '', 1, '', '', '0'),
('SCHALKE_04', '沙尔克04足球俱乐部', '沙尔克', 'FC Schalke 04', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=5&avatarPadding=45', '费尔廷斯竞技场', '德国', '降班马,老牌劲旅', '0', 6, '', 1, '', '', '0'),
('WERDER_BREMEN', '云达不莱梅足球俱乐部', '不莱梅', 'SV Werder Bremen', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=8&avatarPadding=55', '威悉球场', '德国', '老牌劲旅,攻势足球', '0', 7, '', 1, '', '', '0'),
('HAMBURG', '汉堡足球俱乐部', '汉堡', 'Hamburger SV', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=10&avatarPadding=49', '人民公园球场', '德国', '降班马,老牌劲旅', '0', 8, '', 1, '', '', '0'),
('FRANKFURT', '法兰克福足球俱乐部', '法兰克福', 'Eintracht Frankfurt', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=12&avatarPadding=60', '德意志银行公园球场', '德国', '杯赛大师,客场龙', '0', 9, '', 1, '', '', '0'),
('STUTTGART', '斯图加特足球俱乐部', '斯图加特', 'VfB Stuttgart', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=5&avatarPadding=47', 'MHPArena球场', '德国', '攻势足球,状态火热', '0', 10, '', 1, '', '', '0'),
('HOFFENHEIM', '霍芬海姆足球俱乐部', '霍芬海姆', 'TSG 1899 Hoffenheim', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=7&avatarPadding=44', 'SNP竞技场', '德国', '传控流,中游混子', '0', 11, '', 1, '', '', '0'),
('FREIBURG', '弗赖堡足球俱乐部', '弗赖堡', 'Sport-Club Freiburg', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=9&avatarPadding=52', '欧洲公园球场', '德国', '稳扎稳打,中游混子', '0', 12, '', 1, '', '', '0'),
('KOELN', '科隆足球俱乐部', '科隆', '1. FC Köln', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=6&avatarPadding=56', '莱茵能源球场', '德国', '主场龙,中游混子', '0', 13, '', 1, '', '', '0'),
('MAINZ', '美因茨足球俱乐部', '美因茨', '1. FSV Mainz 05', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=8&avatarPadding=41', 'MEWA竞技场', '德国', '中游混子,主场龙', '0', 14, '', 1, '', '', '0'),
('UNION_BERLIN', '柏林联合足球俱乐部', '柏林联合', '1. FC Union Berlin', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=10&avatarPadding=59', '老林务所畔球场', '德国', '铁血之师,防守反击', '0', 15, '', 1, '', '', '0'),
('AUGSBURG', '奥格斯堡足球俱乐部', '奥格斯堡', 'FC Augsburg', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=4&avatarPadding=54', 'WWK竞技场', '德国', '保级队,防守反击', '0', 16, '', 1, '', '', '0'),
('ELVERSBERG', '埃尔弗斯贝格足球俱乐部', '埃弗斯堡', 'SV Elversberg', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=11&avatarPadding=46', 'URSAPHARM竞技场', '德国', '升班马,保级队', '0', 17, '', 1, '', '', '0'),
('PADERBORN', '帕德博恩07足球俱乐部', '帕德博恩', 'SC Paderborn 07', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=7&avatarPadding=58', 'Home Deluxe竞技场', '德国', '升班马,保级队', '0', 18, '', 1, '', '', '0');

-- ------------------------------
-- 二、英超（20支）
-- ------------------------------
INSERT INTO `fx67ll_dortmund_team` 
(`team_code`, `team_name`, `team_name_short`, `team_name_en`, `team_logo_url`, `team_venue`, `team_country`, `team_tag`, `team_status`, `team_sort`, `team_remark`, `user_id`, `create_by`, `update_by`, `del_flag`)
VALUES
('MAN_CITY', '曼彻斯特城足球俱乐部', '曼城', 'Manchester City FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=5&avatarPadding=50', '伊蒂哈德球场', '英格兰', '争冠热门,传控流,土豪新军', '0', 19, '', 1, '', '', '0'),
('MAN_UNITED', '曼彻斯特联足球俱乐部', '曼联', 'Manchester United FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=8&avatarPadding=47', '老特拉福德球场', '英格兰', '争冠热门,老牌劲旅,豪门', '0', 20, '', 1, '', '', '0'),
('LIVERPOOL', '利物浦足球俱乐部', '利物浦', 'Liverpool FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=12&avatarPadding=55', '安菲尔德球场', '英格兰', '争冠热门,主场龙,逆风硬', '0', 21, '', 1, '', '', '0'),
('ARSENAL', '阿森纳足球俱乐部', '阿森纳', 'Arsenal FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=6&avatarPadding=43', '酋长球场', '英格兰', '争冠热门,传控流,主场龙', '0', 22, '', 1, '', '', '0'),
('CHELSEA', '切尔西足球俱乐部', '切尔西', 'Chelsea FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=9&avatarPadding=59', '斯坦福桥球场', '英格兰', '争冠热门,土豪新军', '0', 23, '', 1, '', '', '0'),
('TOTTENHAM', '托特纳姆热刺足球俱乐部', '热刺', 'Tottenham Hotspur FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=7&avatarPadding=49', '托特纳姆热刺球场', '英格兰', '争冠梯队,攻势足球,客场龙', '0', 24, '', 1, '', '', '0'),
('NEWCASTLE', '纽卡斯尔联足球俱乐部', '纽卡', 'Newcastle United FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=10&avatarPadding=52', '圣詹姆斯公园球场', '英格兰', '争冠梯队,土豪新军,主场龙', '0', 25, '', 1, '', '', '0'),
('ASTON_VILLA', '阿斯顿维拉足球俱乐部', '维拉', 'Aston Villa FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=4&avatarPadding=56', '维拉公园球场', '英格兰', '争冠梯队,攻势足球', '0', 26, '', 1, '', '', '0'),
('EVERTON', '埃弗顿足球俱乐部', '埃弗顿', 'Everton FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=11&avatarPadding=45', '希尔迪金森球场', '英格兰', '老牌劲旅,保级狂魔', '0', 27, '', 1, '', '', '0'),
('LEEDS', '利兹联足球俱乐部', '利兹联', 'Leeds United FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=5&avatarPadding=41', '埃兰路球场', '英格兰', '降班马,攻势足球', '0', 28, '', 1, '', '', '0'),
('BRIGHTON', '布莱顿足球俱乐部', '布莱顿', 'Brighton & Hove Albion FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=8&avatarPadding=58', '美国运通球场', '英格兰', '传控流,中游混子', '0', 29, '', 1, '', '', '0'),
('CRYSTAL_PALACE', '水晶宫足球俱乐部', '水晶宫', 'Crystal Palace FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=6&avatarPadding=54', '塞尔赫斯特公园球场', '英格兰', '中游混子,防守反击', '0', 30, '', 1, '', '', '0'),
('FULHAM', '富勒姆足球俱乐部', '富勒姆', 'Fulham FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=9&avatarPadding=44', '克拉文农场球场', '英格兰', '中游混子,主场龙', '0', 31, '', 1, '', '', '0'),
('BRENTFORD', '布伦特福德足球俱乐部', '布伦特福德', 'Brentford FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=12&avatarPadding=51', 'Gtech社区球场', '英格兰', '高空轰炸,中游混子', '0', 32, '', 1, '', '', '0'),
('BOURNEMOUTH', '伯恩茅斯足球俱乐部', '伯恩茅斯', 'AFC Bournemouth', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=7&avatarPadding=46', '活力球场', '英格兰', '中游混子,防守反击', '0', 33, '', 1, '', '', '0'),
('NOTTINGHAM_FOREST', '诺丁汉森林足球俱乐部', '森林', 'Nottingham Forest FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=10&avatarPadding=57', '城市足球场', '英格兰', '中游混子,保级狂魔', '0', 34, '', 1, '', '', '0'),
('IPSWICH', '伊普斯维奇镇足球俱乐部', '伊普斯维奇', 'Ipswich Town FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=4&avatarPadding=50', '波特曼路球场', '英格兰', '升班马,黑马潜质', '0', 35, '', 1, '', '', '0'),
('SUNDERLAND', '桑德兰足球俱乐部', '桑德兰', 'Sunderland AFC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=11&avatarPadding=48', '光明球场', '英格兰', '升班马,老牌劲旅', '0', 36, '', 1, '', '', '0'),
('COVENTRY', '考文垂足球俱乐部', '考文垂', 'Coventry City FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=5&avatarPadding=53', '考文垂建筑协会球场', '英格兰', '升班马,保级队', '0', 37, '', 1, '', '', '0'),
('HULL_CITY', '赫尔城足球俱乐部', '赫尔城', 'Hull City AFC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=8&avatarPadding=42', 'MKM球场', '英格兰', '升班马,保级队', '0', 38, '', 1, '', '', '0');

-- ------------------------------
-- 三、西甲（20支）
-- ------------------------------
INSERT INTO `fx67ll_dortmund_team` 
(`team_code`, `team_name`, `team_name_short`, `team_name_en`, `team_logo_url`, `team_venue`, `team_country`, `team_tag`, `team_status`, `team_sort`, `team_remark`, `user_id`, `create_by`, `update_by`, `del_flag`)
VALUES
('REAL_MADRID', '皇家马德里足球俱乐部', '皇马', 'Real Madrid CF', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=9&avatarPadding=55', '圣地亚哥伯纳乌球场', '西班牙', '争冠热门,豪门,杯赛大师', '0', 39, '', 1, '', '', '0'),
('BARCELONA', '巴塞罗那足球俱乐部', '巴萨', 'FC Barcelona', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=6&avatarPadding=47', '诺坎普球场', '西班牙', '争冠热门,传控流,豪门', '0', 40, '', 1, '', '', '0'),
('ATLETICO_MADRID', '马德里竞技足球俱乐部', '马竞', 'Atlético Madrid', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=12&avatarPadding=50', '利雅得航空大都会球场', '西班牙', '争冠热门,防守反击,铁血之师', '0', 41, '', 1, '', '', '0'),
('SEVILLA', '塞维利亚足球俱乐部', '塞维利亚', 'Sevilla FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=7&avatarPadding=58', '拉蒙桑切斯皮斯胡安球场', '西班牙', '杯赛大师,欧战资格', '0', 42, '', 1, '', '', '0'),
('VALENCIA', '瓦伦西亚足球俱乐部', '瓦伦西亚', 'Valencia CF', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=5&avatarPadding=44', '梅斯塔利亚球场', '西班牙', '老牌劲旅,中游混子', '0', 43, '', 1, '', '', '0'),
('VILLARREAL', '比利亚雷亚尔足球俱乐部', '黄潜', 'Villarreal CF', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=10&avatarPadding=52', '陶瓷球场', '西班牙', '欧战资格,稳扎稳打', '0', 44, '', 1, '', '', '0'),
('REAL_SOCIEDAD', '皇家社会足球俱乐部', '皇社', 'Real Sociedad', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=4&avatarPadding=56', '阿诺埃塔球场', '西班牙', '欧战资格,传控流', '0', 45, '', 1, '', '', '0'),
('REAL_BETIS', '皇家贝蒂斯足球俱乐部', '贝蒂斯', 'Real Betis Balompié', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=11&avatarPadding=49', '贝尼托比利亚马林球场', '西班牙', '欧战资格,传控流', '0', 46, '', 1, '', '', '0'),
('ATHLETIC_BILBAO', '毕尔巴鄂竞技足球俱乐部', '毕尔巴鄂', 'Athletic Club Bilbao', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=8&avatarPadding=41', '圣马梅斯球场', '西班牙', '老牌劲旅,主场龙', '0', 47, '', 1, '', '', '0'),
('ESPANYOL', '皇家西班牙人足球俱乐部', '西班牙人', 'RCD Espanyol', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=6&avatarPadding=59', '科尔内拉-埃尔普拉特球场', '西班牙', '中游混子,主场龙', '0', 48, '', 1, '', '', '0'),
('GETAFE', '赫塔费足球俱乐部', '赫塔费', 'Getafe CF', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=9&avatarPadding=45', '阿方索佩雷斯球场', '西班牙', '防守反击,摆大巴,中游混子', '0', 49, '', 1, '', '', '0'),
('CELTA_VIGO', '维戈塞尔塔足球俱乐部', '塞尔塔', 'RC Celta de Vigo', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=7&avatarPadding=53', '巴拉伊多斯球场', '西班牙', '中游混子,攻势足球', '0', 50, '', 1, '', '', '0'),
('RAYO_VALLECANO', '巴列卡诺足球俱乐部', '巴列卡诺', 'Rayo Vallecano', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=12&avatarPadding=46', '巴列卡斯球场', '西班牙', '中游混子,攻势足球', '0', 51, '', 1, '', '', '0'),
('OSASUNA', '奥萨苏纳足球俱乐部', '奥萨苏纳', 'CA Osasuna', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=5&avatarPadding=57', '萨达尔球场', '西班牙', '中游混子,铁血之师', '0', 52, '', 1, '', '', '0'),
('ALAVES', '阿拉维斯足球俱乐部', '阿拉维斯', 'Deportivo Alavés', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=10&avatarPadding=42', '门迪索罗萨球场', '西班牙', '保级队,防守反击', '0', 53, '', 1, '', '', '0'),
('ELCHE', '埃尔切足球俱乐部', '埃尔切', 'Elche CF', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=4&avatarPadding=51', '马丁内斯巴莱罗球场', '西班牙', '保级队,防守反击', '0', 54, '', 1, '', '', '0'),
('DEPORTIVO', '拉科鲁尼亚足球俱乐部', '拉科', 'RC Deportivo La Coruña', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=11&avatarPadding=54', '里亚索球场', '西班牙', '升班马,老牌劲旅', '0', 55, '', 1, '', '', '0'),
('LEVANTE', '莱万特足球俱乐部', '莱万特', 'Levante UD', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=8&avatarPadding=48', '瓦伦西亚市政球场', '西班牙', '升班马,保级队', '0', 56, '', 1, '', '', '0'),
('MALAGA', '马拉加足球俱乐部', '马拉加', 'Málaga CF', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=6&avatarPadding=50', '拉罗萨莱达球场', '西班牙', '升班马,保级队', '0', 57, '', 1, '', '', '0'),
('RACING_SANTANDER', '桑坦德竞技足球俱乐部', '桑坦德', 'Racing Santander', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=9&avatarPadding=56', '萨尔迪内罗球场', '西班牙', '升班马,保级队', '0', 58, '', 1, '', '', '0');

-- ------------------------------
-- 四、意甲（20支）
-- ------------------------------
INSERT INTO `fx67ll_dortmund_team` 
(`team_code`, `team_name`, `team_name_short`, `team_name_en`, `team_logo_url`, `team_venue`, `team_country`, `team_tag`, `team_status`, `team_sort`, `team_remark`, `user_id`, `create_by`, `update_by`, `del_flag`)
VALUES
('JUVENTUS', '尤文图斯足球俱乐部', '尤文', 'Juventus FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=7&avatarPadding=52', '都灵安联球场', '意大利', '争冠热门,豪门,老牌劲旅', '0', 59, '', 1, '', '', '0'),
('AC_MILAN', 'AC米兰足球俱乐部', '米兰', 'AC Milan', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=10&avatarPadding=47', '圣西罗球场', '意大利', '争冠热门,豪门,老牌劲旅', '0', 60, '', 1, '', '', '0'),
('INTER_MILAN', '国际米兰足球俱乐部', '国米', 'FC Internazionale Milano', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=5&avatarPadding=58', '梅阿查球场', '意大利', '争冠热门,豪门,后防稳如狗', '0', 61, '', 1, '', '', '0'),
('ROMA', '罗马足球俱乐部', '罗马', 'AS Roma', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=12&avatarPadding=49', '罗马奥林匹克球场', '意大利', '争冠梯队,老牌劲旅', '0', 62, '', 1, '', '', '0'),
('NAPOLI', '那不勒斯足球俱乐部', '那不勒斯', 'SSC Napoli', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=6&avatarPadding=44', '迭戈阿曼多马拉多纳球场', '意大利', '争冠梯队,主场龙', '0', 63, '', 1, '', '', '0'),
('LAZIO', '拉齐奥足球俱乐部', '拉齐奥', 'SS Lazio', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=9&avatarPadding=55', '罗马奥林匹克球场', '意大利', '争冠梯队,欧战资格', '0', 64, '', 1, '', '', '0'),
('FIORENTINA', '佛罗伦萨足球俱乐部', '紫百合', 'ACF Fiorentina', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=8&avatarPadding=41', '阿特米奥弗兰基球场', '意大利', '欧战资格,老牌劲旅', '0', 65, '', 1, '', '', '0'),
('ATALANTA', '亚特兰大足球俱乐部', '亚特兰大', 'Atalanta BC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=11&avatarPadding=57', '意大利蓝色竞技球场', '意大利', '欧战资格,攻势足球', '0', 66, '', 1, '', '', '0'),
('TORINO', '都灵足球俱乐部', '都灵', 'Torino FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=4&avatarPadding=53', '都灵奥林匹克球场', '意大利', '中游混子,防守反击', '0', 67, '', 1, '', '', '0'),
('GENOA', '热那亚足球俱乐部', '热那亚', 'Genoa CFC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=7&avatarPadding=46', '路易吉费拉里斯球场', '意大利', '老牌劲旅,中游混子', '0', 68, '', 1, '', '', '0'),
('BOLOGNA', '博洛尼亚足球俱乐部', '博洛尼亚', 'Bologna FC 1909', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=10&avatarPadding=50', '雷纳托达拉拉球场', '意大利', '中游混子,稳扎稳打', '0', 69, '', 1, '', '', '0'),
('UDINESE', '乌迪内斯足球俱乐部', '乌迪内斯', 'Udinese Calcio', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=5&avatarPadding=59', '弗留利球场', '意大利', '保级队,防守反击', '0', 70, '', 1, '', '', '0'),
('CAGLIARI', '卡利亚里足球俱乐部', '卡利亚里', 'Cagliari Calcio', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=12&avatarPadding=42', '乌尼波尔穹顶球场', '意大利', '保级队,主场龙', '0', 71, '', 1, '', '', '0'),
('SASSUOLO', '萨索洛足球俱乐部', '萨索洛', 'US Sassuolo Calcio', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=6&avatarPadding=54', '马佩三色城球场', '意大利', '保级队,传控流', '0', 72, '', 1, '', '', '0'),
('MONZA', '蒙扎足球俱乐部', '蒙扎', 'AC Monza', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=9&avatarPadding=48', '布里安特奥球场', '意大利', '中游混子,土豪新军', '0', 73, '', 1, '', '', '0'),
('LECCE', '莱切足球俱乐部', '莱切', 'US Lecce', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=8&avatarPadding=56', '德尔马雷球场', '意大利', '保级队,攻势足球', '0', 74, '', 1, '', '', '0'),
('COMO', '科莫足球俱乐部', '科莫', 'Como 1907', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=11&avatarPadding=45', '朱塞佩西尼加利亚球场', '意大利', '中游混子,土豪新军', '0', 75, '', 1, '', '', '0'),
('FROSINONE', '弗罗西诺内足球俱乐部', '弗罗西诺内', 'Frosinone Calcio', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=4&avatarPadding=51', '贝尼托斯特尔佩球场', '意大利', '保级队,升班马', '0', 76, '', 1, '', '', '0'),
('PARMA', '帕尔马足球俱乐部', '帕尔马', 'Parma Calcio 1913', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=7&avatarPadding=58', '恩尼奥塔迪尼球场', '意大利', '保级队,升班马', '0', 77, '', 1, '', '', '0'),
('VENEZIA', '威尼斯足球俱乐部', '威尼斯', 'Venezia FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=10&avatarPadding=43', '皮埃尔路易吉本索球场', '意大利', '升班马,保级队', '0', 78, '', 1, '', '', '0');

-- ------------------------------
-- 五、法甲（18支）
-- ------------------------------
INSERT INTO `fx67ll_dortmund_team` 
(`team_code`, `team_name`, `team_name_short`, `team_name_en`, `team_logo_url`, `team_venue`, `team_country`, `team_tag`, `team_status`, `team_sort`, `team_remark`, `user_id`, `create_by`, `update_by`, `del_flag`)
VALUES
('PSG', '巴黎圣日耳曼足球俱乐部', '大巴黎', 'Paris Saint-Germain FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=8&avatarPadding=50', '王子公园球场', '法国', '争冠热门,土豪新军,豪门', '0', 79, '', 1, '', '', '0'),
('MARSEILLE', '马赛足球俱乐部', '马赛', 'Olympique de Marseille', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=5&avatarPadding=55', '韦洛德罗姆球场', '法国', '争冠热门,老牌劲旅,主场龙', '0', 80, '', 1, '', '', '0'),
('LYON', '里昂足球俱乐部', '里昂', 'Olympique Lyonnais', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=11&avatarPadding=47', '安盟球场', '法国', '老牌劲旅,欧战资格', '0', 81, '', 1, '', '', '0'),
('MONACO', '摩纳哥足球俱乐部', '摩纳哥', 'AS Monaco', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=6&avatarPadding=59', '路易二世球场', '摩纳哥', '争冠梯队,土豪新军', '0', 82, '', 1, '', '', '0'),
('LILLE', '里尔足球俱乐部', '里尔', 'LOSC Lille', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=9&avatarPadding=44', '皮埃尔莫鲁瓦球场', '法国', '争冠梯队,稳扎稳打', '0', 83, '', 1, '', '', '0'),
('RENNES', '雷恩足球俱乐部', '雷恩', 'Stade Rennais FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=12&avatarPadding=52', '罗阿宗公园球场', '法国', '欧战资格,中游混子', '0', 84, '', 1, '', '', '0'),
('LENS', '朗斯足球俱乐部', '朗斯', 'RC Lens', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=7&avatarPadding=46', '博拉尔特德勒利球场', '法国', '争冠梯队,主场龙', '0', 85, '', 1, '', '', '0'),
('NICE', '尼斯足球俱乐部', '尼斯', 'OGC Nice', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=4&avatarPadding=57', '安联里维埃拉球场', '法国', '中游混子,土豪新军', '0', 86, '', 1, '', '', '0'),
('STRASBOURG', '斯特拉斯堡足球俱乐部', '斯特拉斯堡', 'RC Strasbourg Alsace', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=10&avatarPadding=49', '梅纳乌球场', '法国', '中游混子,铁血之师', '0', 87, '', 1, '', '', '0'),
('TOULOUSE', '图卢兹足球俱乐部', '图卢兹', 'Toulouse FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=5&avatarPadding=53', '图卢兹市政球场', '法国', '中游混子,主场龙', '0', 88, '', 1, '', '', '0'),
('BREST', '布雷斯特足球俱乐部', '布雷斯特', 'Stade Brestois 29', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=8&avatarPadding=41', '弗朗西斯勒布莱球场', '法国', '欧战资格,主场龙', '0', 89, '', 1, '', '', '0'),
('AUXERRE', '欧塞尔足球俱乐部', '欧塞尔', 'AJ Auxerre', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=11&avatarPadding=56', '阿贝德尚球场', '法国', '中游混子,稳扎稳打', '0', 90, '', 1, '', '', '0'),
('LORIENT', '洛里昂足球俱乐部', '洛里昂', 'FC Lorient', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=6&avatarPadding=48', '穆斯图瓦尔球场', '法国', '中游混子,攻势足球', '0', 91, '', 1, '', '', '0'),
('LE_HAVRE', '勒阿弗尔足球俱乐部', '勒阿弗尔', 'Le Havre AC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=9&avatarPadding=58', '海洋球场', '法国', '中游混子,防守反击', '0', 92, '', 1, '', '', '0'),
('ANGERS', '昂热足球俱乐部', '昂热', 'Angers SCO', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=7&avatarPadding=42', '雷蒙科帕球场', '法国', '保级队,防守反击', '0', 93, '', 1, '', '', '0'),
('TROYES', '特鲁瓦足球俱乐部', '特鲁瓦', 'ESTAC Troyes', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=12&avatarPadding=54', '奥布球场', '法国', '升班马,保级队', '0', 94, '', 1, '', '', '0'),
('LE_MANS', '勒芒足球俱乐部', '勒芒', 'Le Mans FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=4&avatarPadding=50', '玛丽马尔万球场', '法国', '升班马,保级队', '0', 95, '', 1, '', '', '0'),
('PARIS_FC', '巴黎足球俱乐部', '巴黎FC', 'Paris FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=10&avatarPadding=45', '让布安球场', '法国', '升班马,保级队', '0', 96, '', 1, '', '', '0');

-- ------------------------------
-- 六、日职联J1（20支）
-- ------------------------------
INSERT INTO `fx67ll_dortmund_team` 
(`team_code`, `team_name`, `team_name_short`, `team_name_en`, `team_logo_url`, `team_venue`, `team_country`, `team_tag`, `team_status`, `team_sort`, `team_remark`, `user_id`, `create_by`, `update_by`, `del_flag`)
VALUES
('YOKOHAMA_FM', '横滨水手足球俱乐部', '横滨水手', 'Yokohama F. Marinos', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=9&avatarPadding=51', '日产体育场', '日本', '争冠热门,攻势足球,客场龙', '0', 97, '', 1, '', '', '0'),
('KAWASAKI_FRONTALE', '川崎前锋足球俱乐部', '川崎前锋', 'Kawasaki Frontale', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=6&avatarPadding=57', '等力陆上竞技场', '日本', '争冠热门,传控流', '0', 98, '', 1, '', '', '0'),
('KASHIMA_ANTLERS', '鹿岛鹿角足球俱乐部', '鹿岛鹿角', 'Kashima Antlers', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=12&avatarPadding=46', 'Mercari体育场', '日本', '争冠热门,老牌劲旅,主场龙', '0', 99, '', 1, '', '', '0'),
('URAWA_REDS', '浦和红钻足球俱乐部', '浦和红钻', 'Urawa Red Diamonds', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=5&avatarPadding=53', '埼玉2002体育场', '日本', '老牌劲旅,主场龙,杯赛大师', '0', 100, '', 1, '', '', '0'),
('GAMBA_OSAKA', '大阪钢巴足球俱乐部', '大阪钢巴', 'Gamba Osaka', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=8&avatarPadding=49', '松下吹田体育场', '日本', '老牌劲旅,中游混子', '0', 101, '', 1, '', '', '0'),
('NAGOYA_GRAMPUS', '名古屋鲸鱼足球俱乐部', '名古屋鲸鱼', 'Nagoya Grampus', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=11&avatarPadding=55', '丰田体育场', '日本', '中游混子,防守反击', '0', 102, '', 1, '', '', '0'),
('VISSEL_KOBE', '神户胜利船足球俱乐部', '神户胜利船', 'Vissel Kobe', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=7&avatarPadding=42', 'Noevir神户球场', '日本', '土豪新军,争冠梯队', '0', 103, '', 1, '', '', '0'),
('FC_TOKYO', 'FC东京足球俱乐部', 'FC东京', 'FC Tokyo', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=4&avatarPadding=58', '味之素体育场', '日本', '中游混子,主场龙', '0', 104, '', 1, '', '', '0'),
('CEREZO_OSAKA', '大阪樱花足球俱乐部', '大阪樱花', 'Cerezo Osaka', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=10&avatarPadding=47', '淀之樱花球场', '日本', '中游混子,攻势足球', '0', 105, '', 1, '', '', '0'),
('SANFRECCE_HIROSHIMA', '广岛三箭足球俱乐部', '广岛三箭', 'Sanfrecce Hiroshima', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=6&avatarPadding=54', 'Edion和平翼广岛球场', '日本', '争冠梯队,稳扎稳打', '0', 106, '', 1, '', '', '0'),
('KASHIWA_REYSOL', '柏太阳神足球俱乐部', '柏太阳神', 'Kashiwa Reysol', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=9&avatarPadding=44', '三协Frontier柏球场', '日本', '争冠梯队,攻势足球', '0', 107, '', 1, '', '', '0'),
('KYOTO_SANGA', '京都不死鸟足球俱乐部', '京都不死鸟', 'Kyoto Sanga FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=12&avatarPadding=50', '京瓷桑加体育场', '日本', '争冠梯队,主场龙', '0', 108, '', 1, '', '', '0'),
('AVISPA_FUKUOKA', '福冈黄蜂足球俱乐部', '福冈黄蜂', 'Avispa Fukuoka', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=5&avatarPadding=56', 'Best Denki球场', '日本', '中游混子,防守反击', '0', 109, '', 1, '', '', '0'),
('SHONAN_BELLMARE', '湘南比马足球俱乐部', '湘南比马', 'Shonan Bellmare', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=8&avatarPadding=41', '平冢球场', '日本', '保级队,中游混子', '0', 110, '', 1, '', '', '0'),
('SAGAN_TOSU', '鸟栖砂岩足球俱乐部', '鸟栖砂岩', 'Sagan Tosu', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=11&avatarPadding=59', '鸟栖球场', '日本', '降班马,中游混子', '0', 111, '', 1, '', '', '0'),
('MACHIDA_ZELVIA', '町田泽维亚足球俱乐部', '町田泽维亚', 'FC Machida Zelvia', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=7&avatarPadding=48', '町田GION球场', '日本', '中游混子,稳扎稳打', '0', 112, '', 1, '', '', '0'),
('TOKYO_VERDY', '东京绿茵足球俱乐部', '东京绿茵', 'Tokyo Verdy', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=4&avatarPadding=52', '味之素体育场', '日本', '升班马,老牌劲旅', '0', 113, '', 1, '', '', '0'),
('MITO_HOLLYHOCK', '水户蜀葵足球俱乐部', '水户蜀葵', 'Mito HollyHock', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=10&avatarPadding=45', '笠松体育场', '日本', '升班马,保级队', '0', 114, '', 1, '', '', '0'),
('JEF_CHIBA', '千叶市原足球俱乐部', '千叶市原', 'JEF United Chiba', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=6&avatarPadding=57', '福田电子竞技场', '日本', '升班马,保级队', '0', 115, '', 1, '', '', '0'),
('FAGIANO_OKAYAMA', '冈山绿雉足球俱乐部', '冈山绿雉', 'Fagiano Okayama', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=9&avatarPadding=43', 'JFE晴之国球场', '日本', '升班马,保级队', '0', 116, '', 1, '', '', '0');

-- ------------------------------
-- 七、日职乙J2（20支）
-- ------------------------------
INSERT INTO `fx67ll_dortmund_team` 
(`team_code`, `team_name`, `team_name_short`, `team_name_en`, `team_logo_url`, `team_venue`, `team_country`, `team_tag`, `team_status`, `team_sort`, `team_remark`, `user_id`, `create_by`, `update_by`, `del_flag`)
VALUES
('CONSADOLE_SAPPORO', '北海道札幌冈萨多足球俱乐部', '札幌冈萨多', 'Hokkaido Consadole Sapporo', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=7&avatarPadding=55', '札幌穹顶体育场', '日本', '降班马,老牌劲旅', '0', 117, '', 1, '', '', '0'),
('VEGALTA_SENDAI', '仙台七夕足球俱乐部', '仙台七夕', 'Vegalta Sendai', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=10&avatarPadding=48', 'Yurtec仙台球场', '日本', '降班马,冲甲热门', '0', 118, '', 1, '', '', '0'),
('ALBIREX_NIIGATA', '新潟天鹅足球俱乐部', '新潟天鹅', 'Albirex Niigata', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=5&avatarPadding=59', 'Denka大天鹅球场', '日本', '降班马,冲甲热门', '0', 119, '', 1, '', '', '0'),
('JUBILO_IWATA', '磐田喜悦足球俱乐部', '磐田喜悦', 'Júbilo Iwata', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=12&avatarPadding=44', '雅马哈球场', '日本', '老牌劲旅,冲甲热门', '0', 120, '', 1, '', '', '0'),
('YOKOHAMA_FC', '横滨FC足球俱乐部', '横滨FC', 'Yokohama FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=6&avatarPadding=52', '三泽球场', '日本', '冲甲热门,攻势足球', '0', 121, '', 1, '', '', '0'),
('OMIYA_ARDIJA', '大宫松鼠足球俱乐部', '大宫松鼠', 'Omiya Ardija', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=9&avatarPadding=46', 'NACK5球场大宫', '日本', '中游混子,主场龙', '0', 122, '', 1, '', '', '0'),
('TOKUSHIMA_VORTIS', '德岛漩涡足球俱乐部', '德岛漩涡', 'Tokushima Vortis', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=8&avatarPadding=57', '鸣门大冢球场', '日本', '中游混子,主场龙', '0', 123, '', 1, '', '', '0'),
('MONTEDIO_YAMAGATA', '山形山神足球俱乐部', '山形山神', 'Montedio Yamagata', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=11&avatarPadding=49', 'ND软体育场', '日本', '中游混子,主场龙', '0', 124, '', 1, '', '', '0'),
('V_VAREN_NAGASAKI', '长崎航海足球俱乐部', '长崎航海', 'V-Varen Nagasaki', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=4&avatarPadding=54', '长崎县立球场', '日本', '中游混子,稳扎稳打', '0', 125, '', 1, '', '', '0'),
('VENTFORET_KOFU', '甲府风林足球俱乐部', '甲府风林', 'Ventforet Kofu', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=7&avatarPadding=41', '小濑山球场', '日本', '中下游队,杯赛黑马', '0', 126, '', 1, '', '', '0'),
('BLAUBLITZ_AKITA', '秋田蓝色闪电足球俱乐部', '秋田闪电', 'Blaublitz Akita', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=10&avatarPadding=58', 'SOYU球场', '日本', '中游混子,防守反击', '0', 127, '', 1, '', '', '0'),
('KATALLER_TOYAMA', '富山胜利足球俱乐部', '富山胜利', 'Kataller Toyama', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=5&avatarPadding=50', '富山田径体育场', '日本', '冲甲热门,主场龙', '0', 128, '', 1, '', '', '0'),
('TOCHIGI_CITY', '枥木市足球俱乐部', '枥木FC', 'Tochigi City FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=12&avatarPadding=45', 'CFS球场', '日本', '保级队,防守反击', '0', 129, '', 1, '', '', '0'),
('FUJIEDA_MYFC', '藤枝MYFC足球俱乐部', '藤枝MYFC', 'Fujieda MYFC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=6&avatarPadding=56', '藤枝足球场', '日本', '中下游队,保级队', '0', 130, '', 1, '', '', '0'),
('IWAKI_FC', '磐城足球俱乐部', '磐城FC', 'Iwaki FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=9&avatarPadding=42', '夏威夷人球场', '日本', '中下游队,保级队', '0', 131, '', 1, '', '', '0'),
('VANRAURE_HACHINOHE', '八户云罗里足球俱乐部', '八户云罗里', 'Vanraure Hachinohe', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=8&avatarPadding=53', 'Prifoods球场', '日本', '中下游队,保级队', '0', 132, '', 1, '', '', '0'),
('FC_IMABARI', 'FC今治足球俱乐部', 'FC今治', 'FC Imabari', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=11&avatarPadding=47', '亚瑟士里山球场', '日本', '中下游队,稳扎稳打', '0', 133, '', 1, '', '', '0'),
('RENOFA_YAMAGUCHI', '山口雷法足球俱乐部', '山口雷法', 'Renofa Yamaguchi FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=4&avatarPadding=59', '山口县立球场', '日本', '中下游队,保级队', '0', 134, '', 1, '', '', '0'),
('KAGOSHIMA_UTD', '鹿儿岛联足球俱乐部', '鹿儿岛联', 'Kagoshima United FC', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=7&avatarPadding=48', '鹿儿岛县立球场', '日本', '保级队,升班马', '0', 135, '', 1, '', '', '0'),
('FC_RYUKYU', '琉球FC足球俱乐部', '琉球FC', 'FC Ryukyu', 'https://vip.fx67ll.com/vip-api/getRandomAvatar?isNeedMoreMosaic=Y&avatarBlockNum=10&avatarPadding=51', '冲绳县综合球场', '日本', '保级队,中下游队', '0', 136, '', 1, '', '', '0');
