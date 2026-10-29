package com.ruoyi.fx67ll.dortmund.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatch;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicSeason;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundInfoPoint;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundInfoPointTeamRel;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicTeam;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundBasicMatchMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundBasicSeasonMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundBasicTeamMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundInfoPointMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundInfoPointTeamRelMapper;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicSeasonService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicTeamService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundInfoPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 统一信息点Service业务层处理
 *
 * @author fx67ll
 * @date 2026-10-29
 */
@Service
public class Fx67llDortmundInfoPointServiceImpl implements IFx67llDortmundInfoPointService {
    @Autowired
    private Fx67llDortmundInfoPointMapper fx67llDortmundInfoPointMapper;

    @Autowired
    private Fx67llDortmundInfoPointTeamRelMapper fx67llDortmundInfoPointTeamRelMapper;

    @Autowired
    private IFx67llDortmundBasicSeasonService fx67llDortmundBasicSeasonService;

    @Autowired
    private IFx67llDortmundBasicTeamService fx67llDortmundBasicTeamService;

    @Autowired
    private Fx67llDortmundBasicMatchMapper fx67llDortmundBasicMatchMapper;

    /**
     * 查询统一信息点
     *
     * @param infoPointId 信息点唯一标识
     * @return 统一信息点
     */
    @Override
    public Fx67llDortmundInfoPoint selectFx67llDortmundInfoPointByInfoPointId(Long infoPointId) {
        Fx67llDortmundInfoPoint infoPoint = fx67llDortmundInfoPointMapper.selectFx67llDortmundInfoPointByInfoPointId(infoPointId);
        if (infoPoint != null) {
            fillTeamRels(infoPoint);
        }
        return infoPoint;
    }

    /**
     * 查询统一信息点列表
     *
     * @param fx67llDortmundInfoPoint 统一信息点
     * @return 统一信息点集合
     */
    @Override
    public List<Fx67llDortmundInfoPoint> selectFx67llDortmundInfoPointList(Fx67llDortmundInfoPoint fx67llDortmundInfoPoint) {
        if (!hasAllDataScope()) {
            fx67llDortmundInfoPoint.setUserId(SecurityUtils.getUserId());
        }
        return fx67llDortmundInfoPointMapper.selectFx67llDortmundInfoPointList(fx67llDortmundInfoPoint);
    }

    /**
     * 新增统一信息点
     * <p>
     * 类型二选一：T 必须有赛季和至少一支球队、不得提交比赛；M 必须有比赛、不得提交赛季和球队
     *
     * @param fx67llDortmundInfoPoint 统一信息点
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertFx67llDortmundInfoPoint(Fx67llDortmundInfoPoint fx67llDortmundInfoPoint) {
        checkInfoPointFields(fx67llDortmundInfoPoint);
        resolveTeamIds(fx67llDortmundInfoPoint);
        if (fx67llDortmundInfoPoint.getPointStatus() == null || fx67llDortmundInfoPoint.getPointStatus().trim().isEmpty()) {
            fx67llDortmundInfoPoint.setPointStatus("0");
        }
        if (fx67llDortmundInfoPoint.getAllowPublic() == null || fx67llDortmundInfoPoint.getAllowPublic().trim().isEmpty()) {
            fx67llDortmundInfoPoint.setAllowPublic("0");
        }
        fx67llDortmundInfoPoint.setUserId(SecurityUtils.getUserId());
        fx67llDortmundInfoPoint.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundInfoPoint.setCreateTime(DateUtils.getNowDate());
        int rows = fx67llDortmundInfoPointMapper.insertFx67llDortmundInfoPoint(fx67llDortmundInfoPoint);
        if (rows > 0) {
            saveTeamRels(fx67llDortmundInfoPoint);
        }
        return rows;
    }

    /**
     * 修改统一信息点
     * <p>
     * 类型、赛季、比赛归属和状态不可通过本接口修改，球队关系按提交集合差异启停
     *
     * @param fx67llDortmundInfoPoint 统一信息点
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateFx67llDortmundInfoPoint(Fx67llDortmundInfoPoint fx67llDortmundInfoPoint) {
        if (fx67llDortmundInfoPoint.getInfoPointId() == null) {
            throw new ServiceException("信息点主键不能为空！");
        }
        Fx67llDortmundInfoPoint oldInfoPoint = fx67llDortmundInfoPointMapper.selectFx67llDortmundInfoPointByInfoPointId(fx67llDortmundInfoPoint.getInfoPointId());
        if (oldInfoPoint == null) {
            throw new ServiceException("信息点不存在！");
        }
        if (!checkInfoPointDataScope(oldInfoPoint.getUserId())) {
            throw new ServiceException("没有权限修改该信息点！");
        }
        checkInfoPointTypeAndCombination(oldInfoPoint, fx67llDortmundInfoPoint);
        checkInfoPointFields(fx67llDortmundInfoPoint);
        fx67llDortmundInfoPoint.setInfoPointType(null);
        fx67llDortmundInfoPoint.setSeasonId(null);
        fx67llDortmundInfoPoint.setMatchId(null);
        fx67llDortmundInfoPoint.setPointStatus(null);
        fx67llDortmundInfoPoint.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundInfoPoint.setUpdateTime(DateUtils.getNowDate());
        int rows = fx67llDortmundInfoPointMapper.updateFx67llDortmundInfoPoint(fx67llDortmundInfoPoint);
        if (rows > 0) {
            fx67llDortmundInfoPoint.setInfoPointType(oldInfoPoint.getInfoPointType());
            fx67llDortmundInfoPoint.setSeasonId(oldInfoPoint.getSeasonId());
            fx67llDortmundInfoPoint.setMatchId(oldInfoPoint.getMatchId());
            if ("T".equals(oldInfoPoint.getInfoPointType())) {
                checkTeamIdsForUpdate(fx67llDortmundInfoPoint);
                saveTeamRels(fx67llDortmundInfoPoint);
            }
        }
        return rows;
    }

    /**
     * 修改时校验球队集合：为空表示保持现有关系不变，非空按提交集合校验球队存在
     *
     * @param fx67llDortmundInfoPoint 请求中的信息点
     */
    private void checkTeamIdsForUpdate(Fx67llDortmundInfoPoint fx67llDortmundInfoPoint) {
        Long[] teamIds = fx67llDortmundInfoPoint.getTeamIds();
        if (teamIds == null || teamIds.length == 0) {
            // 空集合视为保持现有球队关系不变，与不传语义一致
            return;
        }
        for (Long teamId : teamIds) {
            if (fx67llDortmundBasicTeamService.selectFx67llDortmundBasicTeamByTeamId(teamId) == null) {
                throw new ServiceException("所选球队不存在，请重新选择！");
            }
        }
    }

    /**
     * 修改信息点状态
     * <p>
     * 条件更新，影响行数不是 1 时按已处理、状态变化或记录不存在返回
     *
     * @param fx67llDortmundInfoPoint 信息点唯一标识与目标状态
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeFx67llDortmundInfoPointStatus(Fx67llDortmundInfoPoint fx67llDortmundInfoPoint) {
        if (fx67llDortmundInfoPoint.getInfoPointId() == null) {
            throw new ServiceException("信息点主键不能为空！");
        }
        String targetStatus = fx67llDortmundInfoPoint.getPointStatus();
        if (!"0".equals(targetStatus) && !"1".equals(targetStatus)) {
            throw new ServiceException("信息点状态只能为启用或停用！");
        }
        Fx67llDortmundInfoPoint oldInfoPoint = fx67llDortmundInfoPointMapper.selectFx67llDortmundInfoPointByInfoPointId(fx67llDortmundInfoPoint.getInfoPointId());
        if (oldInfoPoint == null) {
            throw new ServiceException("信息点不存在或状态已变化，请刷新后重试！");
        }
        if (!checkInfoPointDataScope(oldInfoPoint.getUserId())) {
            throw new ServiceException("没有权限修改该信息点！");
        }
        if (targetStatus.equals(oldInfoPoint.getPointStatus())) {
            throw new ServiceException("信息点已处理，状态未发生变化！");
        }
        fx67llDortmundInfoPoint.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundInfoPoint.setUpdateTime(DateUtils.getNowDate());
        return fx67llDortmundInfoPointMapper.updateFx67llDortmundInfoPointStatus(fx67llDortmundInfoPoint);
    }

    /**
     * 查询比赛的有效信息点
     * <p>
     * T 类型同时检查启用、球队关联启用、赛季与比赛赛季一致、赛季状态、结束日期和生效时间窗；
     * M 类型只看启用、比赛匹配和自身时间窗；是否可公开照常返回，但不作为排除原因
     *
     * @param matchId 比赛ID
     * @return 信息点集合
     */
    @Override
    public List<Fx67llDortmundInfoPoint> selectFx67llDortmundInfoPointValidListByMatchId(Long matchId) {
        Fx67llDortmundBasicMatch match = fx67llDortmundBasicMatchMapper.selectFx67llDortmundBasicMatchByMatchId(matchId);
        if (match == null) {
            throw new ServiceException("所选比赛不存在，请重新选择！");
        }
        List<Fx67llDortmundInfoPoint> resultList = new ArrayList<>();

        Fx67llDortmundInfoPoint mQuery = new Fx67llDortmundInfoPoint();
        mQuery.setMatchId(matchId);
        if (!hasAllDataScope()) {
            mQuery.setUserId(SecurityUtils.getUserId());
        }
        fillValidFlag(fx67llDortmundInfoPointMapper.selectFx67llDortmundInfoPointList(mQuery), match, resultList);

        Fx67llDortmundInfoPoint tQuery = new Fx67llDortmundInfoPoint();
        tQuery.setInfoPointType("T");
        if (!hasAllDataScope()) {
            tQuery.setUserId(SecurityUtils.getUserId());
        }
        List<Fx67llDortmundInfoPoint> tList = fx67llDortmundInfoPointMapper.selectFx67llDortmundInfoPointList(tQuery);
        for (Fx67llDortmundInfoPoint infoPoint : tList) {
            fillTeamRels(infoPoint);
        }
        fillValidFlag(tList, match, resultList);
        return resultList;
    }

    /**
     * 填充信息点的有效性标记与排除原因
     *
     * @param infoPointList 信息点集合
     * @param match         当前比赛
     * @param resultList    结果集合
     */
    private void fillValidFlag(List<Fx67llDortmundInfoPoint> infoPointList, Fx67llDortmundBasicMatch match, List<Fx67llDortmundInfoPoint> resultList) {
        if (infoPointList == null) {
            return;
        }
        Date now = DateUtils.getNowDate();
        for (Fx67llDortmundInfoPoint infoPoint : infoPointList) {
            String excludeReason = null;
            if ("1".equals(infoPoint.getPointStatus())) {
                excludeReason = "信息点已停用";
            } else if ("M".equals(infoPoint.getInfoPointType())) {
                if (!match.getMatchId().equals(infoPoint.getMatchId())) {
                    excludeReason = "比赛归属与当前比赛不一致";
                } else if (!inEffectiveTimeWindow(infoPoint, now)) {
                    excludeReason = "当前时间不在生效时间窗内";
                }
            } else {
                excludeReason = checkTypeValid(infoPoint, match, now);
            }
            infoPoint.setValid(excludeReason == null);
            infoPoint.setExcludeReason(excludeReason);
            resultList.add(infoPoint);
        }
    }

    /**
     * 校验 T 类型信息点对当前比赛的有效性
     *
     * @param infoPoint T 类型信息点
     * @param match     当前比赛
     * @param now       当前时间
     * @return 排除原因（有效时为空）
     */
    private String checkTypeValid(Fx67llDortmundInfoPoint infoPoint, Fx67llDortmundBasicMatch match, Date now) {
        if (!inEffectiveTimeWindow(infoPoint, now)) {
            return "当前时间不在生效时间窗内";
        }
        if (infoPoint.getSeasonId() == null || !infoPoint.getSeasonId().equals(match.getSeasonId())) {
            return "信息点赛季与比赛赛季不一致";
        }
        boolean teamMatched = false;
        if (infoPoint.getTeamRels() != null) {
            for (Fx67llDortmundInfoPointTeamRel rel : infoPoint.getTeamRels()) {
                if ("1".equals(rel.getRelationStatus())) {
                    continue;
                }
                if (rel.getTeamId().equals(match.getHomeTeamId()) || rel.getTeamId().equals(match.getAwayTeamId())) {
                    teamMatched = true;
                    break;
                }
            }
        }
        if (!teamMatched) {
            return "目标球队关联已停用或未关联当前比赛球队";
        }
        Fx67llDortmundBasicSeason season = fx67llDortmundBasicSeasonService.selectFx67llDortmundBasicSeasonBySeasonId(infoPoint.getSeasonId());
        if (season == null) {
            return "信息点所属赛季不存在";
        }
        if ("1".equals(season.getSeasonStatus())) {
            return "赛季状态为已结束";
        }
        if (season.getSeasonEndDate() != null && now.after(season.getSeasonEndDate())) {
            return "当前日期晚于赛季结束日期";
        }
        return null;
    }

    /**
     * 判断当前时间是否在信息点生效时间窗内（空端不参与限制）
     *
     * @param infoPoint 信息点
     * @param now       当前时间
     * @return 是否在时间窗内
     */
    private boolean inEffectiveTimeWindow(Fx67llDortmundInfoPoint infoPoint, Date now) {
        if (infoPoint.getEffectiveStartTime() != null && now.before(infoPoint.getEffectiveStartTime())) {
            return false;
        }
        return infoPoint.getEffectiveEndTime() == null || !now.after(infoPoint.getEffectiveEndTime());
    }

    /**
     * 校验信息点基础字段与类型组合
     *
     * @param fx67llDortmundInfoPoint 统一信息点
     */
    private void checkInfoPointFields(Fx67llDortmundInfoPoint fx67llDortmundInfoPoint) {
        if (fx67llDortmundInfoPoint.getPointTitle() == null || fx67llDortmundInfoPoint.getPointTitle().trim().isEmpty()) {
            throw new ServiceException("信息点标题不能为空！");
        }
        if (fx67llDortmundInfoPoint.getPointContent() == null || fx67llDortmundInfoPoint.getPointContent().trim().isEmpty()) {
            throw new ServiceException("信息点内容不能为空！");
        }
        if (fx67llDortmundInfoPoint.getSourceName() == null || fx67llDortmundInfoPoint.getSourceName().trim().isEmpty()) {
            throw new ServiceException("信息来源名称不能为空！");
        }
        if (fx67llDortmundInfoPoint.getCollectTime() == null) {
            throw new ServiceException("采集或录入时间不能为空！");
        }
        if (fx67llDortmundInfoPoint.getEffectiveStartTime() != null && fx67llDortmundInfoPoint.getEffectiveEndTime() != null
                && fx67llDortmundInfoPoint.getEffectiveStartTime().after(fx67llDortmundInfoPoint.getEffectiveEndTime())) {
            throw new ServiceException("信息点生效时间不得晚于失效时间！");
        }
        if (fx67llDortmundInfoPoint.getAllowPublic() != null && !"0".equals(fx67llDortmundInfoPoint.getAllowPublic())
                && !"1".equals(fx67llDortmundInfoPoint.getAllowPublic())) {
            throw new ServiceException("是否可公开只能为不可公开或可公开！");
        }
    }

    /**
     * 修改时校验请求字段与既有归属一致：类型、赛季、比赛归属创建后不可修改
     *
     * @param oldInfoPoint         数据库中的信息点
     * @param newInfoPoint         请求中的信息点
     */
    private void checkInfoPointTypeAndCombination(Fx67llDortmundInfoPoint oldInfoPoint, Fx67llDortmundInfoPoint newInfoPoint) {
        if (newInfoPoint.getInfoPointType() != null && !newInfoPoint.getInfoPointType().equals(oldInfoPoint.getInfoPointType())) {
            throw new ServiceException("信息点类型创建后不可修改，如需调整请停用原记录并新建！");
        }
        if (newInfoPoint.getSeasonId() != null && !newInfoPoint.getSeasonId().equals(oldInfoPoint.getSeasonId())) {
            throw new ServiceException("信息点所属赛季创建后不可修改，如需调整请停用原记录并新建！");
        }
        if (newInfoPoint.getMatchId() != null && !newInfoPoint.getMatchId().equals(oldInfoPoint.getMatchId())) {
            throw new ServiceException("信息点所属比赛创建后不可修改，如需调整请停用原记录并新建！");
        }
    }

    /**
     * 校验类型组合并解析球队关系
     *
     * @param fx67llDortmundInfoPoint 统一信息点
     * @return 球队ID集合（M 类型为空集合）
     */
    private List<Long> resolveTeamIds(Fx67llDortmundInfoPoint fx67llDortmundInfoPoint) {
        List<Long> teamIds = new ArrayList<>();
        if ("T".equals(fx67llDortmundInfoPoint.getInfoPointType())) {
            if (fx67llDortmundInfoPoint.getSeasonId() == null) {
                throw new ServiceException("T 类型信息点必须选择所属赛季！");
            }
            if (fx67llDortmundInfoPoint.getMatchId() != null) {
                throw new ServiceException("T 类型信息点不能关联比赛！");
            }
            if (fx67llDortmundInfoPoint.getTeamIds() == null || fx67llDortmundInfoPoint.getTeamIds().length == 0) {
                throw new ServiceException("T 类型信息点至少要关联一支球队！");
            }
            if (fx67llDortmundBasicSeasonService.selectFx67llDortmundBasicSeasonBySeasonId(fx67llDortmundInfoPoint.getSeasonId()) == null) {
                throw new ServiceException("所选赛季不存在，请重新选择！");
            }
            for (Long teamId : fx67llDortmundInfoPoint.getTeamIds()) {
                if (fx67llDortmundBasicTeamService.selectFx67llDortmundBasicTeamByTeamId(teamId) == null) {
                    throw new ServiceException("所选球队不存在，请重新选择！");
                }
                if (!teamIds.contains(teamId)) {
                    teamIds.add(teamId);
                }
            }
        } else if ("M".equals(fx67llDortmundInfoPoint.getInfoPointType())) {
            if (fx67llDortmundInfoPoint.getMatchId() == null) {
                throw new ServiceException("M 类型信息点必须选择所属比赛！");
            }
            if (fx67llDortmundInfoPoint.getSeasonId() != null) {
                throw new ServiceException("M 类型信息点不能关联赛季！");
            }
            if (fx67llDortmundInfoPoint.getTeamIds() != null && fx67llDortmundInfoPoint.getTeamIds().length > 0) {
                throw new ServiceException("M 类型信息点不能关联球队！");
            }
            if (fx67llDortmundBasicMatchMapper.selectFx67llDortmundBasicMatchByMatchId(fx67llDortmundInfoPoint.getMatchId()) == null) {
                throw new ServiceException("所选比赛不存在，请重新选择！");
            }
        } else {
            throw new ServiceException("信息点类型只能为 T 或 M！");
        }
        return teamIds;
    }

    /**
     * 保存信息点球队关系：新增的插入、取消的停用、重新选择的启用，不物理删除
     * <p>
     * 修改时提交的球队集合为空表示保持现有关系不变
     *
     * @param fx67llDortmundInfoPoint 统一信息点
     */
    private void saveTeamRels(Fx67llDortmundInfoPoint fx67llDortmundInfoPoint) {
        if (!"T".equals(fx67llDortmundInfoPoint.getInfoPointType())) {
            return;
        }
        List<Long> teamIds = new ArrayList<>();
        if (fx67llDortmundInfoPoint.getTeamIds() != null) {
            for (Long teamId : fx67llDortmundInfoPoint.getTeamIds()) {
                if (!teamIds.contains(teamId)) {
                    teamIds.add(teamId);
                }
            }
        }
        if (teamIds.isEmpty()) {
            return;
        }
        Fx67llDortmundInfoPointTeamRel relQuery = new Fx67llDortmundInfoPointTeamRel();
        relQuery.setInfoPointId(fx67llDortmundInfoPoint.getInfoPointId());
        List<Fx67llDortmundInfoPointTeamRel> existingRelList = fx67llDortmundInfoPointTeamRelMapper.selectFx67llDortmundInfoPointTeamRelList(relQuery);
        List<Long> existingTeamIds = new ArrayList<>();
        if (existingRelList != null) {
            for (Fx67llDortmundInfoPointTeamRel rel : existingRelList) {
                existingTeamIds.add(rel.getTeamId());
                if (!teamIds.contains(rel.getTeamId())) {
                    if (!"1".equals(rel.getRelationStatus())) {
                        rel.setRelationStatus("1");
                        rel.setUpdateBy(SecurityUtils.getUsername());
                        rel.setUpdateTime(DateUtils.getNowDate());
                        fx67llDortmundInfoPointTeamRelMapper.updateFx67llDortmundInfoPointTeamRel(rel);
                    }
                } else if ("1".equals(rel.getRelationStatus())) {
                    rel.setRelationStatus("0");
                    rel.setUpdateBy(SecurityUtils.getUsername());
                    rel.setUpdateTime(DateUtils.getNowDate());
                    fx67llDortmundInfoPointTeamRelMapper.updateFx67llDortmundInfoPointTeamRel(rel);
                }
            }
        }
        for (Long teamId : teamIds) {
            if (!existingTeamIds.contains(teamId)) {
                Fx67llDortmundInfoPointTeamRel rel = new Fx67llDortmundInfoPointTeamRel();
                rel.setInfoPointId(fx67llDortmundInfoPoint.getInfoPointId());
                rel.setTeamId(teamId);
                rel.setRelationStatus("0");
                rel.setCreateBy(SecurityUtils.getUsername());
                rel.setCreateTime(DateUtils.getNowDate());
                fx67llDortmundInfoPointTeamRelMapper.insertFx67llDortmundInfoPointTeamRel(rel);
            }
        }
    }

    /**
     * 填充信息点的球队关系（只读展示）
     *
     * @param infoPoint 信息点
     */
    private void fillTeamRels(Fx67llDortmundInfoPoint infoPoint) {
        if (!"T".equals(infoPoint.getInfoPointType())) {
            return;
        }
        Fx67llDortmundInfoPointTeamRel relQuery = new Fx67llDortmundInfoPointTeamRel();
        relQuery.setInfoPointId(infoPoint.getInfoPointId());
        infoPoint.setTeamRels(fx67llDortmundInfoPointTeamRelMapper.selectFx67llDortmundInfoPointTeamRelList(relQuery));
    }

    /**
     * 判断当前用户是否拥有"全部数据"权限角色
     *
     * @return 是否全部数据权限
     */
    private boolean hasAllDataScope() {
        List<SysRole> roles = SecurityUtils.getLoginUser().getUser().getRoles();
        if (roles != null) {
            for (SysRole role : roles) {
                if ("1".equals(role.getDataScope())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 校验当前用户对信息点的数据范围
     *
     * @param infoPointUserId 信息点归属用户ID
     * @return 是否可见
     */
    private boolean checkInfoPointDataScope(Long infoPointUserId) {
        return hasAllDataScope() || SecurityUtils.getUserId().equals(infoPointUserId);
    }
}
