package com.ruoyi.fx67ll.dortmund.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatch;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundBasicMatchMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundInfoPointMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundBasicSeasonMapper;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicSeason;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicSeasonService;
import com.ruoyi.fx67ll.dortmund.support.DortmundDataScope;
import com.ruoyi.fx67ll.dortmund.support.DortmundFieldValidator;

import static com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig.Field.SEASON_CODE;
import static com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig.Field.SEASON_NAME;
import static com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig.Field.SEASON_REMARK;
import static com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig.Season.SORT_DEFAULT;
import static com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig.Season.SORT_MAX;
import static com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig.Season.SORT_MIN;

/**
 * 赛季管理Service业务层处理
 * <p>
 * 删除赛季前必须确认没有被比赛或信息点引用；赛季编码由数据库全局唯一索引校验。
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@Service
public class Fx67llDortmundBasicSeasonServiceImpl implements IFx67llDortmundBasicSeasonService {
    @Autowired
    private Fx67llDortmundBasicSeasonMapper fx67llDortmundBasicSeasonMapper;

    @Autowired
    private Fx67llDortmundBasicMatchMapper fx67llDortmundBasicMatchMapper;

    @Autowired
    private Fx67llDortmundInfoPointMapper fx67llDortmundInfoPointMapper;

    /**
     * 查询赛季管理
     *
     * @param seasonId 赛季管理主键
     * @return 赛季管理
     */
    @Override
    public Fx67llDortmundBasicSeason selectFx67llDortmundBasicSeasonBySeasonId(Long seasonId) {
        return fx67llDortmundBasicSeasonMapper.selectFx67llDortmundBasicSeasonBySeasonId(seasonId);
    }

    /**
     * 按当前用户数据范围查询赛季详情。
     * <p>对象不存在和无权访问统一返回受控提示，避免通过详情接口推断其他用户的数据。</p>
     *
     * @param seasonId 赛季管理主键
     * @return 当前用户可访问的赛季
     */
    @Override
    public Fx67llDortmundBasicSeason selectFx67llDortmundBasicSeasonDetailForCurrentUser(Long seasonId) {
        return requireSeasonAccessible(seasonId);
    }

    /**
     * 查询赛季管理列表
     *
     * @param fx67llDortmundBasicSeason 赛季管理
     * @return 赛季管理
     */
    @Override
    public List<Fx67llDortmundBasicSeason> selectFx67llDortmundBasicSeasonList(Fx67llDortmundBasicSeason fx67llDortmundBasicSeason) {
        return fx67llDortmundBasicSeasonMapper.selectFx67llDortmundBasicSeasonList(fx67llDortmundBasicSeason);
    }

    /**
     * 按当前用户数据范围查询赛季管理列表。
     * <p>普通用户强制按本人 user_id 过滤，全部数据权限角色保留其显式筛选条件。</p>
     *
     * @param fx67llDortmundBasicSeason 赛季管理
     * @return 当前用户可访问的赛季集合
     */
    @Override
    public List<Fx67llDortmundBasicSeason> selectFx67llDortmundBasicSeasonListForCurrentUser(
            Fx67llDortmundBasicSeason fx67llDortmundBasicSeason) {
        Long ownerUserId = DortmundDataScope.ownerFilterForCurrentUser();
        if (ownerUserId != null) {
            fx67llDortmundBasicSeason.setUserId(ownerUserId);
        }
        return fx67llDortmundBasicSeasonMapper.selectFx67llDortmundBasicSeasonList(fx67llDortmundBasicSeason);
    }

    /**
     * 新增赛季管理
     *
     * @param fx67llDortmundBasicSeason 赛季管理
     * @return 结果
     */
    @Override
    public int insertFx67llDortmundBasicSeason(Fx67llDortmundBasicSeason fx67llDortmundBasicSeason) {
        validateSeasonFields(fx67llDortmundBasicSeason);
        fx67llDortmundBasicSeason.setUserId(SecurityUtils.getUserId());
        fx67llDortmundBasicSeason.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundBasicSeason.setCreateTime(DateUtils.getNowDate());
        fx67llDortmundBasicSeason.setDelFlag("0");
        try {
            return fx67llDortmundBasicSeasonMapper.insertFx67llDortmundBasicSeason(fx67llDortmundBasicSeason);
        } catch (DuplicateKeyException e) {
            // 赛季编码由数据库全局唯一索引约束，不随用户归属拆分。
            throw new ServiceException("赛季编码已存在，请重新输入！");
        }
    }

    /**
     * 修改赛季管理
     *
     * @param fx67llDortmundBasicSeason 赛季管理
     * @return 结果
     */
    @Override
    public int updateFx67llDortmundBasicSeason(Fx67llDortmundBasicSeason fx67llDortmundBasicSeason) {
        if (fx67llDortmundBasicSeason.getSeasonId() == null) {
            throw new ServiceException("赛季主键不能为空！");
        }
        Fx67llDortmundBasicSeason oldSeason = requireSeasonAccessible(fx67llDortmundBasicSeason.getSeasonId());
        validateSeasonFields(fx67llDortmundBasicSeason);
        // 归属与创建审计只允许由服务端维护，防止客户端通过修改请求转移数据或篡改创建信息。
        fx67llDortmundBasicSeason.setUserId(oldSeason.getUserId());
        fx67llDortmundBasicSeason.setCreateBy(oldSeason.getCreateBy());
        fx67llDortmundBasicSeason.setCreateTime(oldSeason.getCreateTime());
        fx67llDortmundBasicSeason.setDelFlag(oldSeason.getDelFlag());
        fx67llDortmundBasicSeason.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundBasicSeason.setUpdateTime(DateUtils.getNowDate());
        try {
            return fx67llDortmundBasicSeasonMapper.updateFx67llDortmundBasicSeason(fx67llDortmundBasicSeason);
        } catch (DuplicateKeyException e) {
            // 修改编码时仍按全局唯一索引校验，不允许通过用户归属复用他人编码。
            throw new ServiceException("赛季编码已存在，请重新输入！");
        }
    }

    /**
     * 批量删除赛季管理
     *
     * @param seasonIds 需要删除的赛季管理主键
     * @return 结果
     */
    @Override
    public int deleteFx67llDortmundBasicSeasonBySeasonIds(Long[] seasonIds) {
        if (seasonIds == null || seasonIds.length == 0) {
            return 0;
        }
        // 先校验整批归属，再检查引用，确保任一无权记录都不会触发部分删除。
        for (Long seasonId : seasonIds) {
            requireSeasonAccessible(seasonId);
        }
        for (Long seasonId : seasonIds) {
            checkSeasonReferenceBeforeDelete(seasonId);
        }
        return fx67llDortmundBasicSeasonMapper.deleteFx67llDortmundBasicSeasonBySeasonIds(seasonIds);
    }

    /**
     * 删除赛季管理信息
     *
     * @param seasonId 赛季管理主键
     * @return 结果
     */
    @Override
    public int deleteFx67llDortmundBasicSeasonBySeasonId(Long seasonId) {
        requireSeasonAccessible(seasonId);
        checkSeasonReferenceBeforeDelete(seasonId);
        return fx67llDortmundBasicSeasonMapper.deleteFx67llDortmundBasicSeasonBySeasonId(seasonId);
    }

    /**
     * 读取并校验赛季归属。
     *
     * @param seasonId 赛季管理主键
     * @return 当前用户可维护的赛季
     */
    private Fx67llDortmundBasicSeason requireSeasonAccessible(Long seasonId) {
        Fx67llDortmundBasicSeason season = fx67llDortmundBasicSeasonMapper
                .selectFx67llDortmundBasicSeasonBySeasonId(seasonId);
        if (season == null) {
            throw new ServiceException("赛季不存在或没有访问权限！");
        }
        DortmundDataScope.requireAccessible(season.getUserId(), "赛季不存在或没有访问权限！");
        return season;
    }

    /**
     * 校验赛季字段并统一去除文本首尾空格，规则与前端表单和数据库字段约束保持一致
     *
     * @param fx67llDortmundBasicSeason 赛季管理
     */
    private void validateSeasonFields(Fx67llDortmundBasicSeason fx67llDortmundBasicSeason) {
        fx67llDortmundBasicSeason.setSeasonCode(DortmundFieldValidator.requireText(
                fx67llDortmundBasicSeason.getSeasonCode(), "赛季编码", SEASON_CODE));
        fx67llDortmundBasicSeason.setSeasonName(DortmundFieldValidator.requireText(
                fx67llDortmundBasicSeason.getSeasonName(), "赛季名称", SEASON_NAME));
        fx67llDortmundBasicSeason.setSeasonStatus(DortmundFieldValidator.validateEnum(
                fx67llDortmundBasicSeason.getSeasonStatus(), "赛季状态", true, "0", "1", "2"));
        if (fx67llDortmundBasicSeason.getSeasonSort() == null) {
            fx67llDortmundBasicSeason.setSeasonSort(SORT_DEFAULT);
        }
        DortmundFieldValidator.validateInteger(fx67llDortmundBasicSeason.getSeasonSort(),
                "赛季排序", SORT_MIN, SORT_MAX, false);
        fx67llDortmundBasicSeason.setSeasonRemark(DortmundFieldValidator.optionalText(
                fx67llDortmundBasicSeason.getSeasonRemark(), "赛季备注", SEASON_REMARK));
        if (fx67llDortmundBasicSeason.getSeasonStartDate() == null
                || fx67llDortmundBasicSeason.getSeasonEndDate() == null) {
            throw new ServiceException("赛季开始日期和结束日期不能为空！");
        }
        if (fx67llDortmundBasicSeason.getSeasonStartDate().after(fx67llDortmundBasicSeason.getSeasonEndDate())) {
            throw new ServiceException("赛季开始日期不得晚于结束日期！");
        }
    }

    /**
     * 删除前校验赛季是否被比赛或信息点引用，被引用时返回统一"已被引用"错误
     *
     * @param seasonId 赛季管理主键
     */
    private void checkSeasonReferenceBeforeDelete(Long seasonId) {
        Fx67llDortmundBasicMatch matchQuery = new Fx67llDortmundBasicMatch();
        matchQuery.setSeasonId(seasonId);
        List<Fx67llDortmundBasicMatch> matchList = fx67llDortmundBasicMatchMapper.selectFx67llDortmundBasicMatchList(matchQuery);
        if (matchList != null && !matchList.isEmpty()) {
            throw new ServiceException("赛季已被比赛引用，不能删除；可改用结束赛季");
        }
        if (fx67llDortmundInfoPointMapper.countSeasonInfoPointsBySeasonId(seasonId) > 0) {
            throw new ServiceException("赛季已被信息点引用，不能删除；可改用结束赛季");
        }
    }
}
