package com.ruoyi.fx67ll.dortmund.service.impl;

/**
 * PHASE_GATE_SUSPENDED
 * 本文件在第 6 周之前暂不改造。
 * 方案已重做本模块，归入执行计划第一阶段（第 6–7 周，外快盈亏）。
 * 原实现整段保留在下方行注释中。验证通过后，由负责人在对应阶段删除注释并按新方案重写。
 * 当前类型不提供业务方法。
 */
public class Fx67llDortmundExtraServiceImpl {
}
// ----- original source begins -----
// package com.ruoyi.fx67ll.dortmund.service.impl;
// 
// import java.math.BigDecimal;
// import java.math.RoundingMode;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Comparator;
// import java.util.LinkedHashSet;
// import java.util.List;
// import java.util.Objects;
// import java.util.Set;
// 
// import com.ruoyi.common.exception.ServiceException;
// import com.ruoyi.common.utils.DateUtils;
// import com.ruoyi.common.utils.SecurityUtils;
// import com.ruoyi.fx67ll.lottery.domain.Fx67llLotteryLog;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundExtraMapper;
// import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtra;
// import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundExtraService;
// 
// /**
//  * 外快盈亏记录Service业务层处理
//  *
//  * @author fx67ll
//  * @date 2023-08-17
//  */
// @Service
// public class Fx67llDortmundExtraServiceImpl implements IFx67llDortmundExtraService {
//     @Autowired
//     private Fx67llDortmundExtraMapper fx67llDortmundExtraMapper;
// 
//     /**
//      * 单次合并允许传入的最大记录数量，限制 N+1 查询规模
//      */
//     private static final int MERGE_MAX_INPUT_COUNT = 23;
// 
//     /**
//      * 合并后备注字段的最大长度限制（数据库 extra_remark 为 varchar(1023)）
//      */
//     private static final int REMARK_MAX_LENGTH = 1023;
// 
//     /**
//      * 查询外快盈亏记录
//      *
//      * @param extraId 外快盈亏记录主键
//      * @return 外快盈亏记录
//      */
//     @Override
//     public Fx67llDortmundExtra selectFx67llDortmundExtraByExtraId(Long extraId) {
//         return fx67llDortmundExtraMapper.selectFx67llDortmundExtraByExtraId(extraId);
//     }
// 
//     /**
//      * 查询外快盈亏记录列表
//      *
//      * @param fx67llDortmundExtra 外快盈亏记录
//      * @return 外快盈亏记录
//      */
//     @Override
//     public List<Fx67llDortmundExtra> selectFx67llDortmundExtraList(Fx67llDortmundExtra fx67llDortmundExtra) {
//         return fx67llDortmundExtraMapper.selectFx67llDortmundExtraList(fx67llDortmundExtra);
//     }
// 
//     /**
//      * 通过 UserId 查询外快盈亏记录列表
//      *
//      * @param fx67llDortmundExtra 外快盈亏记录
//      * @return 外快盈亏记录
//      */
//     @Override
//     public List<Fx67llDortmundExtra> selectFx67llDortmundExtraListByUserId(Fx67llDortmundExtra fx67llDortmundExtra) {
//         fx67llDortmundExtra.setUserId(SecurityUtils.getUserId());
//         return fx67llDortmundExtraMapper.selectFx67llDortmundExtraList(fx67llDortmundExtra);
//     }
// 
//     /**
//      * 新增外快盈亏记录
//      *
//      * @param fx67llDortmundExtra 外快盈亏记录
//      * @return 结果
//      */
//     @Override
//     public int insertFx67llDortmundExtra(Fx67llDortmundExtra fx67llDortmundExtra) {
//         fx67llDortmundExtra.setUserId(SecurityUtils.getUserId());
//         fx67llDortmundExtra.setCreateBy(SecurityUtils.getUsername());
//         fx67llDortmundExtra.setCreateTime(DateUtils.getNowDate());
//         return fx67llDortmundExtraMapper.insertFx67llDortmundExtra(fx67llDortmundExtra);
//     }
// 
//     /**
//      * 修改外快盈亏记录
//      *
//      * @param fx67llDortmundExtra 外快盈亏记录
//      * @return 结果
//      */
//     @Override
//     public int updateFx67llDortmundExtra(Fx67llDortmundExtra fx67llDortmundExtra) {
//         fx67llDortmundExtra.setUpdateBy(SecurityUtils.getUsername());
//         fx67llDortmundExtra.setUpdateTime(DateUtils.getNowDate());
//         return fx67llDortmundExtraMapper.updateFx67llDortmundExtra(fx67llDortmundExtra);
//     }
// 
//     /**
//      * 批量删除外快盈亏记录
//      *
//      * @param extraIds 需要删除的外快盈亏记录主键
//      * @return 结果
//      */
//     @Override
//     public int deleteFx67llDortmundExtraByExtraIds(Long[] extraIds) {
//         return fx67llDortmundExtraMapper.deleteFx67llDortmundExtraByExtraIds(extraIds);
//     }
// 
//     /**
//      * 删除外快盈亏记录信息
//      *
//      * @param extraId 外快盈亏记录主键
//      * @return 结果
//      */
//     @Override
//     public int deleteFx67llDortmundExtraByExtraId(Long extraId) {
//         return fx67llDortmundExtraMapper.deleteFx67llDortmundExtraByExtraId(extraId);
//     }
// 
//     /**
//      * 合并外快盈亏历史记录
//      * <p>
//      * 详见接口注释。Web 端管理视角不做本人归属限制，仅要求所选记录为同一用户创建。
//      */
//     @Override
//     @Transactional(rollbackFor = Exception.class)
//     public Long mergeFx67llDortmundExtras(Long[] extraIds, String extraRemark) {
//         return doMergeFx67llDortmundExtras(extraIds, extraRemark, null);
//     }
// 
//     /**
//      * 提供给 APP 合并外快盈亏历史记录
//      * <p>
//      * 详见接口注释。与 Web 端合并逻辑一致，区别在于额外校验所有待合并记录均为当前登录用户本人创建，防止越权合并他人数据。
//      */
//     @Override
//     @Transactional(rollbackFor = Exception.class)
//     public Long mergeFx67llDortmundExtrasForApp(Long[] extraIds, String extraRemark) {
//         return doMergeFx67llDortmundExtras(extraIds, extraRemark, SecurityUtils.getUserId());
//     }
// 
//     /**
//      * 合并外快盈亏历史记录的内部实现
//      * <p>
//      * 详见接口注释。该方法在一个事务内完成"读取-校验-合并-新增-删除"全流程，
//      * 任一步抛出异常（含 ServiceException 业务校验异常）都会触发回滚，保证不会出现
//      * "合并了但没删"或"删了但没合并"的脏数据。
//      *
//      * @param extraIds 需要合并的外快盈亏记录主键集合
//      * @param extraRemark 合并后新记录的备注（可不填写）
//      * @param currentUserId 当前登录用户主键，非空时要求所有待合并记录均为该用户本人创建（APP 端防越权），为空时仅校验同一用户创建（Web 端管理视角）
//      * @return 合并后新生成记录的主键
//      */
//     private Long doMergeFx67llDortmundExtras(Long[] extraIds, String extraRemark, Long currentUserId) {
//         // ---------- 1. 入参基础校验 ----------
//         if (extraIds == null || extraIds.length < 2) {
//             throw new ServiceException("请至少选择两条记录进行合并！");
//         }
//         // 限制单次合并数量上限，防止恶意传入超大数组造成 N+1 查询与超长字符串拼接
//         if (extraIds.length > MERGE_MAX_INPUT_COUNT) {
//             throw new ServiceException("单次最多合并" + MERGE_MAX_INPUT_COUNT + "条记录！");
//         }
//         // 对入参主键去重，避免同一 ID 重复传入导致逻辑异常
//         Set<Long> idSet = new LinkedHashSet<>(Arrays.asList(extraIds));
//         if (idSet.size() < 2) {
//             throw new ServiceException("请至少选择两条不同的记录进行合并！");
//         }
//         // 合并备注由前端合并确认弹窗录入：去除首尾空格，未填写则不保留备注，超长直接拒绝
//         String mergedRemark = extraRemark == null ? null : extraRemark.trim();
//         if (mergedRemark != null && mergedRemark.isEmpty()) {
//             mergedRemark = null;
//         }
//         if (mergedRemark != null && mergedRemark.length() > REMARK_MAX_LENGTH) {
//             throw new ServiceException("合并后的备注长度不能超过" + REMARK_MAX_LENGTH + "个字符！");
//         }
// 
//         // ---------- 2. 加载并校验被合并记录 ----------
//         List<Fx67llDortmundExtra> extraList = new ArrayList<>();
//         for (Long extraId : idSet) {
//             Fx67llDortmundExtra extra = fx67llDortmundExtraMapper.selectFx67llDortmundExtraByExtraId(extraId);
//             if (extra == null) {
//                 throw new ServiceException("部分记录不存在或已被删除，请刷新后重试！");
//             }
//             extraList.add(extra);
//         }
//         // 归属用户校验：外快盈亏按用户维度独立记录盈亏，仅允许合并同一用户创建的记录
//         Long mergedUserId = extraList.get(0).getUserId();
//         for (Fx67llDortmundExtra extra : extraList) {
//             if (!Objects.equals(mergedUserId, extra.getUserId())) {
//                 throw new ServiceException("仅支持合并同一用户创建的记录！");
//             }
//         }
//         // APP 端越权校验：在"同一用户创建"的基础上，进一步要求该用户为当前登录用户本人
//         if (currentUserId != null && !currentUserId.equals(mergedUserId)) {
//             throw new ServiceException("存在非本人创建的记录，禁止合并！");
//         }
// 
//         // ---------- 3. 校验历史数据连续性 ----------
//         // 待合并记录按创建时间升序排序，得到时间序数组（最后一条即合并范围内最新数据）
//         extraList.sort(byCreateTimeAsc());
//         // 加载该用户的全量记录并同样按创建时间升序排序，校验所选记录在其个人历史序列中连续
//         Fx67llDortmundExtra queryExtra = new Fx67llDortmundExtra();
//         queryExtra.setUserId(mergedUserId);
//         List<Fx67llDortmundExtra> userExtraList = fx67llDortmundExtraMapper.selectFx67llDortmundExtraList(queryExtra);
//         userExtraList.sort(byCreateTimeAsc());
//         List<Integer> selectedIndexList = new ArrayList<>();
//         for (int i = 0; i < userExtraList.size(); i++) {
//             if (idSet.contains(userExtraList.get(i).getExtraId())) {
//                 selectedIndexList.add(i);
//             }
//         }
//         // 所选记录未全部出现在该用户历史序列中，说明数据已变更，要求刷新后重试
//         if (selectedIndexList.size() != idSet.size()) {
//             throw new ServiceException("部分记录不存在或已被删除，请刷新后重试！");
//         }
//         for (int i = 1; i < selectedIndexList.size(); i++) {
//             if (selectedIndexList.get(i) != selectedIndexList.get(i - 1) + 1) {
//                 throw new ServiceException("待合并的历史数据必须连续！");
//             }
//         }
// 
//         // ---------- 4. 合并数值字段并构建新记录 ----------
//         // 时间序最后一条记录（合并范围内最新数据），快照类字段与创建信息均取自该条
//         Fx67llDortmundExtra lastExtra = extraList.get(extraList.size() - 1);
//         // 盈亏金额取所选记录盈亏金额之和（字段为字符串，需解析为金额求和后保留两位小数）
//         BigDecimal totalWinMoney = BigDecimal.ZERO;
//         for (Fx67llDortmundExtra extra : extraList) {
//             totalWinMoney = totalWinMoney.add(parseMoney(extra.getWinMoney()));
//         }
//         BigDecimal mergedWinMoney = totalWinMoney.setScale(2, RoundingMode.HALF_UP);
// 
//         Fx67llDortmundExtra mergedExtra = new Fx67llDortmundExtra();
//         // 快照类字段（总金额、当前本金、落袋金额、目标金额）取最后一条原值，保证与其后记录的盈亏计算基准衔接
//         mergedExtra.setExtraMoney(lastExtra.getExtraMoney());
//         mergedExtra.setSeedMoney(lastExtra.getSeedMoney());
//         mergedExtra.setSaveMoney(lastExtra.getSaveMoney());
//         mergedExtra.setTargetMoney(lastExtra.getTargetMoney());
//         // 盈亏金额为所选记录之和，是否盈利按合并后盈亏金额正负推导（0 按未盈利处理）
//         mergedExtra.setWinMoney(mergedWinMoney.toPlainString());
//         mergedExtra.setIsWin(mergedWinMoney.compareTo(BigDecimal.ZERO) > 0 ? "Y" : "N");
//         // 备注取前端合并确认弹窗中录入的内容（未填写则不保留）
//         mergedExtra.setExtraRemark(mergedRemark);
//         // 归属用户沿用被合并记录的原值
//         mergedExtra.setUserId(mergedUserId);
//         // 创建时间取合并范围内最后一条记录的原值，创建人取该条原值（保留原始创建痕迹）
//         mergedExtra.setCreateBy(lastExtra.getCreateBy());
//         mergedExtra.setCreateTime(lastExtra.getCreateTime());
//         // 更新时间取合并操作的当前时间，更新人取当前操作人
//         mergedExtra.setUpdateBy(SecurityUtils.getUsername());
//         mergedExtra.setUpdateTime(DateUtils.getNowDate());
// 
//         // 直接走 Mapper 新增，手动设好全部字段（不调用 insertFx67llDortmundExtra，
//         // 因为它会强制覆盖 userId/createBy/createTime 为当前用户与当前时间，不符合"创建时间取最后一条原值"的要求）
//         // useGeneratedKeys 会将数据库自增主键回填到 mergedExtra.extraId，得到全新 ID
//         fx67llDortmundExtraMapper.insertFx67llDortmundExtra(mergedExtra);
// 
//         // ---------- 5. 删除全部被合并的旧记录 ----------
//         fx67llDortmundExtraMapper.deleteFx67llDortmundExtraByExtraIds(idSet.toArray(new Long[0]));
// 
//         // 返回合并后新记录的主键
//         return mergedExtra.getExtraId();
//     }
// 
//     /**
//      * 按创建时间升序的比较器（主键升序兜底），用于确定历史记录的时间先后顺序
//      */
//     private Comparator<Fx67llDortmundExtra> byCreateTimeAsc() {
//         return Comparator
//                 .comparing(Fx67llDortmundExtra::getCreateTime, Comparator.nullsFirst(Comparator.naturalOrder()))
//                 .thenComparing(Fx67llDortmundExtra::getExtraId);
//     }
// 
//     /**
//      * 解析金额字符串为 BigDecimal，空值按 0 处理，格式异常抛业务异常避免静默丢数
//      */
//     private BigDecimal parseMoney(String money) {
//         if (money == null || money.trim().isEmpty()) {
//             return BigDecimal.ZERO;
//         }
//         try {
//             return new BigDecimal(money.trim());
//         } catch (NumberFormatException e) {
//             throw new ServiceException("待合并记录的金额字段格式异常，无法合并！");
//         }
//     }
// }
// 
// ----- original source ends -----
