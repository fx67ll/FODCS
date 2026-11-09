package com.ruoyi.fx67ll.dortmund.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatch;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundOdds;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundOddsMapper;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicMatchService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundOddsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dortmund倍率记录Service业务层处理
 *
 * @author fx67ll
 * @date 2026-11-09
 */
@Service
public class Fx67llDortmundOddsServiceImpl implements IFx67llDortmundOddsService {
    private static final BigDecimal MAX_WEIGHT = new BigDecimal("100");

    private static final int ODDS_REMARK_MAX_LENGTH = 1023;

    private static final String YES = "Y";

    private static final String NO = "N";

    @Autowired
    private Fx67llDortmundOddsMapper fx67llDortmundOddsMapper;

    @Autowired
    private IFx67llDortmundBasicMatchService fx67llDortmundBasicMatchService;

    /**
     * 查询倍率记录
     *
     * @param oddsId 倍率记录主键
     * @return 倍率记录
     */
    @Override
    public Fx67llDortmundOdds selectFx67llDortmundOddsByOddsId(Long oddsId) {
        Fx67llDortmundOdds odds = fx67llDortmundOddsMapper.selectFx67llDortmundOddsByOddsId(oddsId);
        if (odds == null || (!hasAllDataScope() && !SecurityUtils.getUserId().equals(odds.getUserId()))) {
            return null;
        }
        return odds;
    }

    /**
     * 查询倍率记录列表
     *
     * @param fx67llDortmundOdds 倍率记录查询条件
     * @return 倍率记录集合
     */
    @Override
    public List<Fx67llDortmundOdds> selectFx67llDortmundOddsList(Fx67llDortmundOdds fx67llDortmundOdds) {
        if (fx67llDortmundOdds == null) {
            fx67llDortmundOdds = new Fx67llDortmundOdds();
        }
        if (!hasAllDataScope()) {
            fx67llDortmundOdds.setUserId(SecurityUtils.getUserId());
        }
        fx67llDortmundOdds.setDelFlag("0");
        return fx67llDortmundOddsMapper.selectFx67llDortmundOddsList(fx67llDortmundOdds);
    }

    /**
     * 查询比赛当前有效倍率记录
     *
     * @param matchId 比赛主键
     * @return 当前有效倍率记录
     */
    @Override
    public Fx67llDortmundOdds selectActiveFx67llDortmundOddsByMatchId(Long matchId) {
        Fx67llDortmundOdds odds = fx67llDortmundOddsMapper.selectActiveFx67llDortmundOddsByMatchId(matchId);
        if (odds == null || (!hasAllDataScope() && !SecurityUtils.getUserId().equals(odds.getUserId()))) {
            return null;
        }
        return odds;
    }

    /**
     * 新增倍率记录，支持同一事务内创建比赛
     *
     * @param fx67llDortmundOdds 倍率记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertFx67llDortmundOdds(Fx67llDortmundOdds fx67llDortmundOdds) {
        if (fx67llDortmundOdds == null) {
            throw new ServiceException("倍率记录不能为空！");
        }
        if (fx67llDortmundOdds.getNewMatch() != null) {
            if (fx67llDortmundOdds.getMatchId() != null) {
                throw new ServiceException("已有比赛和新比赛不能同时提交！");
            }
            if (fx67llDortmundOdds.getNewMatch().getNewOdds() != null) {
                throw new ServiceException("新比赛不能继续嵌套新增倍率记录！");
            }
            fx67llDortmundBasicMatchService.insertFx67llDortmundBasicMatch(fx67llDortmundOdds.getNewMatch());
            fx67llDortmundOdds.setMatchId(fx67llDortmundOdds.getNewMatch().getMatchId());
        }
        checkOddsFields(fx67llDortmundOdds);
        checkMatchDataScope(fx67llDortmundOdds.getMatchId());
        Date now = DateUtils.getNowDate();
        fx67llDortmundOdds.setOddsCode(fx67llDortmundOdds.getOddsCode().trim());
        fx67llDortmundOdds.setUserId(SecurityUtils.getUserId());
        fx67llDortmundOdds.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundOdds.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundOdds.setCreateTime(now);
        fx67llDortmundOdds.setUpdateTime(now);
        fx67llDortmundOdds.setDelFlag("0");
        if (fx67llDortmundOdds.getActiveFlag() == null) {
            fx67llDortmundOdds.setActiveFlag(NO);
        }
        try {
            if (YES.equals(fx67llDortmundOdds.getActiveFlag())) {
                lockMatch(fx67llDortmundOdds.getMatchId());
                fx67llDortmundOddsMapper.deactivateOddsByMatchId(fx67llDortmundOdds);
            }
            return fx67llDortmundOddsMapper.insertFx67llDortmundOdds(fx67llDortmundOdds);
        } catch (DuplicateKeyException e) {
            throw new ServiceException("倍率记录编码已存在！");
        }
    }

    /**
     * 修改非冻结倍率记录
     *
     * @param fx67llDortmundOdds 倍率记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateFx67llDortmundOdds(Fx67llDortmundOdds fx67llDortmundOdds) {
        if (fx67llDortmundOdds == null) {
            throw new ServiceException("倍率记录不能为空！");
        }
        Fx67llDortmundOdds oldOdds = checkOddsDataScope(fx67llDortmundOdds.getOddsId());
        if (YES.equals(oldOdds.getActiveFlag())) {
            throw new ServiceException("当前有效倍率记录已冻结，请新增倍率版本！");
        }
        if (fx67llDortmundOddsMapper.countCombinationItemsByOddsId(oldOdds.getOddsId()) > 0) {
            throw new ServiceException("倍率记录已被回报组合引用，不能修改！");
        }
        fx67llDortmundOdds.setMatchId(oldOdds.getMatchId());
        fx67llDortmundOdds.setOddsCode(oldOdds.getOddsCode());
        fx67llDortmundOdds.setActiveFlag(oldOdds.getActiveFlag());
        checkOddsFields(fx67llDortmundOdds);
        fx67llDortmundOdds.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundOdds.setUpdateTime(DateUtils.getNowDate());
        return fx67llDortmundOddsMapper.updateFx67llDortmundOdds(fx67llDortmundOdds);
    }

    /**
     * 原子切换比赛当前有效倍率记录
     *
     * @param oddsId 倍率记录主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int activateFx67llDortmundOdds(Long oddsId) {
        Fx67llDortmundOdds odds = checkOddsDataScope(oddsId);
        lockMatch(odds.getMatchId());
        odds.setUpdateBy(SecurityUtils.getUsername());
        odds.setUpdateTime(DateUtils.getNowDate());
        if (YES.equals(odds.getActiveFlag())) {
            return 1;
        }
        fx67llDortmundOddsMapper.activateOtherOddsByMatchId(odds);
        return fx67llDortmundOddsMapper.activateFx67llDortmundOdds(odds);
    }

    /**
     * 批量删除倍率记录
     *
     * @param oddsIds 需要删除的倍率记录主键集合
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteFx67llDortmundOddsByOddsIds(Long[] oddsIds) {
        if (oddsIds == null || oddsIds.length == 0) {
            return 0;
        }
        for (Long oddsId : oddsIds) {
            Fx67llDortmundOdds odds = checkOddsDataScope(oddsId);
            if (YES.equals(odds.getActiveFlag())) {
                throw new ServiceException("当前有效倍率记录不能删除！");
            }
            if (fx67llDortmundOddsMapper.countCombinationItemsByOddsId(oddsId) > 0) {
                throw new ServiceException("倍率记录已被回报组合引用，不能删除！");
            }
        }
        return fx67llDortmundOddsMapper.deleteFx67llDortmundOddsByOddsIds(oddsIds);
    }

    /**
     * 校验倍率记录字段
     *
     * @param fx67llDortmundOdds 倍率记录
     */
    private void checkOddsFields(Fx67llDortmundOdds fx67llDortmundOdds) {
        if (fx67llDortmundOdds.getMatchId() == null) {
            throw new ServiceException("比赛不能为空！");
        }
        if (fx67llDortmundOdds.getOddsCode() == null || fx67llDortmundOdds.getOddsCode().trim().isEmpty()
                || fx67llDortmundOdds.getOddsCode().trim().length() > 23) {
            throw new ServiceException("倍率记录编码不能为空且不能超过23个字符！");
        }
        if (fx67llDortmundOdds.getSourceTime() == null || fx67llDortmundOdds.getHandicapValue() == null) {
            throw new ServiceException("倍率来源时间和让分值不能为空！");
        }
        if (fx67llDortmundOdds.getActiveFlag() != null && !YES.equals(fx67llDortmundOdds.getActiveFlag())
                && !NO.equals(fx67llDortmundOdds.getActiveFlag())) {
            throw new ServiceException("倍率有效状态不正确！");
        }
        if (fx67llDortmundOdds.getOddsRemark() != null
                && fx67llDortmundOdds.getOddsRemark().length() > ODDS_REMARK_MAX_LENGTH) {
            throw new ServiceException("倍率记录说明不能超过1023个字符！");
        }
        checkOddsValue(fx67llDortmundOdds.getHomeWinOdds(), "主胜倍率");
        checkOddsValue(fx67llDortmundOdds.getDrawOdds(), "平局倍率");
        checkOddsValue(fx67llDortmundOdds.getAwayWinOdds(), "客胜倍率");
        checkOddsValue(fx67llDortmundOdds.getHandicapHomeWinOdds(), "让分主胜倍率");
        checkOddsValue(fx67llDortmundOdds.getHandicapDrawOdds(), "让分平局倍率");
        checkOddsValue(fx67llDortmundOdds.getHandicapAwayWinOdds(), "让分客胜倍率");
        checkWeight(fx67llDortmundOdds.getHomeWinWeight(), "主胜权重");
        checkWeight(fx67llDortmundOdds.getDrawWeight(), "平局权重");
        checkWeight(fx67llDortmundOdds.getAwayWinWeight(), "客胜权重");
        checkWeight(fx67llDortmundOdds.getHandicapHomeWinWeight(), "让分主胜权重");
        checkWeight(fx67llDortmundOdds.getHandicapDrawWeight(), "让分平局权重");
        checkWeight(fx67llDortmundOdds.getHandicapAwayWinWeight(), "让分客胜权重");
    }

    private void checkOddsValue(BigDecimal value, String fieldName) {
        if (value == null || value.signum() <= 0) {
            throw new ServiceException(fieldName + "必须大于0！");
        }
    }

    private void checkWeight(BigDecimal value, String fieldName) {
        if (value != null && (value.signum() < 0 || value.compareTo(MAX_WEIGHT) > 0)) {
            throw new ServiceException(fieldName + "必须在0到100之间！");
        }
    }

    private Fx67llDortmundOdds checkOddsDataScope(Long oddsId) {
        if (oddsId == null) {
            throw new ServiceException("倍率记录主键不能为空！");
        }
        Fx67llDortmundOdds odds = selectFx67llDortmundOddsByOddsId(oddsId);
        if (odds == null || !"0".equals(odds.getDelFlag())) {
            throw new ServiceException("倍率记录不存在或没有访问权限！");
        }
        return odds;
    }

    private void checkMatchDataScope(Long matchId) {
        Fx67llDortmundBasicMatch match = fx67llDortmundBasicMatchService.selectFx67llDortmundBasicMatchByMatchId(matchId);
        if (match == null || !"0".equals(match.getDelFlag()) || !"0".equals(match.getMatchStatus())
                || (!hasAllDataScope() && !SecurityUtils.getUserId().equals(match.getUserId()))) {
            throw new ServiceException("比赛不存在、状态异常或没有访问权限！");
        }
    }

    private void lockMatch(Long matchId) {
        if (fx67llDortmundOddsMapper.selectMatchIdForUpdate(matchId) == null) {
            throw new ServiceException("比赛不存在、已关闭或已删除！");
        }
    }

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
}
