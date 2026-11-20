package com.ruoyi.fx67ll.dortmund.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundInfoPoint;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatch;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundInfoPointTeamRel;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundBasicMatchMapper;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundInfoPointTeamRelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.fx67ll.dortmund.mapper.Fx67llDortmundBasicTeamMapper;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicTeam;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundTeamOptions;
import com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundInfoPointService;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundBasicTeamService;
import com.ruoyi.fx67ll.dortmund.support.DortmundFieldValidator;

import static com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig.Field.TEAM_CODE;
import static com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig.Field.TEAM_COUNTRY;
import static com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig.Field.TEAM_LOGO_URL;
import static com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig.Field.TEAM_NAME;
import static com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig.Field.TEAM_NAME_EN;
import static com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig.Field.TEAM_NAME_SHORT;
import static com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig.Field.TEAM_REMARK;
import static com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig.Field.TEAM_TAG;
import static com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig.Field.TEAM_VENUE;
import static com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig.Team.SORT_DEFAULT;
import static com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig.Team.SORT_MAX;
import static com.ruoyi.fx67ll.dortmund.config.DortmundModuleConfig.Team.SORT_MIN;
import org.springframework.transaction.annotation.Transactional;

/**
 * 球队管理Service业务层处理
 * <p>
 * 删除球队前必须确认没有被比赛或信息点关系引用；新增时可在同一事务内创建球队信息点。
 *
 * @author ruoyi
 * @date 2026-03-03
 */
@Service
public class Fx67llDortmundBasicTeamServiceImpl implements IFx67llDortmundBasicTeamService {
    @Autowired
    private Fx67llDortmundBasicTeamMapper fx67llDortmundBasicTeamMapper;

    @Autowired
    private Fx67llDortmundBasicMatchMapper fx67llDortmundBasicMatchMapper;

    @Autowired
    private Fx67llDortmundInfoPointTeamRelMapper fx67llDortmundInfoPointTeamRelMapper;

    @Autowired
    private IFx67llDortmundInfoPointService fx67llDortmundInfoPointService;

    /**
     * 查询球队管理
     *
     * @param teamId 球队管理主键
     * @return 球队管理
     */
    @Override
    public Fx67llDortmundBasicTeam selectFx67llDortmundBasicTeamByTeamId(Long teamId) {
        return fx67llDortmundBasicTeamMapper.selectFx67llDortmundBasicTeamByTeamId(teamId);
    }

    /**
     * 查询球队管理列表
     *
     * @param fx67llDortmundBasicTeam 球队管理
     * @return 球队管理
     */
    @Override
    public List<Fx67llDortmundBasicTeam> selectFx67llDortmundBasicTeamList(Fx67llDortmundBasicTeam fx67llDortmundBasicTeam) {
        return fx67llDortmundBasicTeamMapper.selectFx67llDortmundBasicTeamList(fx67llDortmundBasicTeam);
    }

    /**
     * 通过 UserId 查询球队管理列表
     *
     * @param fx67llDortmundBasicTeam 球队管理
     * @return 球队管理
     */
    @Override
    public List<Fx67llDortmundBasicTeam> selectFx67llDortmundBasicTeamListByUserId(Fx67llDortmundBasicTeam fx67llDortmundBasicTeam) {
        fx67llDortmundBasicTeam.setUserId(SecurityUtils.getUserId());
        return fx67llDortmundBasicTeamMapper.selectFx67llDortmundBasicTeamList(fx67llDortmundBasicTeam);
    }

    /**
     * 汇总球队主场与标签选项。
     * <p>
     * 先放入内置目录，再追加球队表去重值；标签字段兼容英文逗号和中文逗号分隔。
     * 这样第三方导入的自定义值可见，同时接口异常时前端仍有稳定兜底。
     */
    @Override
    public Fx67llDortmundTeamOptions selectFx67llDortmundTeamOptions() {
        Map<String, Fx67llDortmundTeamOptions.Option> venueOptions = new LinkedHashMap<>();
        Map<String, Fx67llDortmundTeamOptions.Option> tagOptions = new LinkedHashMap<>();
        for (String[] item : DortmundModuleConfig.TeamCatalog.VENUE_OPTIONS) {
            addOption(venueOptions, item[0], item[1], "info");
        }
        for (String[] item : DortmundModuleConfig.TeamCatalog.TAG_OPTIONS) {
            addOption(tagOptions, item[0], item[1], item[2]);
        }
        List<Fx67llDortmundBasicTeam> rows = fx67llDortmundBasicTeamMapper.selectDistinctTeamVenueAndTag();
        if (rows != null) {
            for (Fx67llDortmundBasicTeam row : rows) {
                String venue = row == null ? null : row.getTeamVenue();
                if (venue != null && !venue.trim().isEmpty()) {
                    addOption(venueOptions, venue.trim(), venue.trim(), "info");
                }
                String tags = row == null ? null : row.getTeamTag();
                if (tags == null) {
                    continue;
                }
                for (String tag : tags.split(DortmundModuleConfig.Team.TAG_SPLIT_REGEX)) {
                    String normalized = tag == null ? "" : tag.trim();
                    if (!normalized.isEmpty()) {
                        addOption(tagOptions, normalized, normalized, "info");
                    }
                }
            }
        }
        Fx67llDortmundTeamOptions result = new Fx67llDortmundTeamOptions();
        result.getTeamVenueOptions().addAll(venueOptions.values());
        result.getTeamTagOptions().addAll(tagOptions.values());
        return result;
    }

    /** 以 value 去重，保留首次出现的 label 和颜色。 */
    private void addOption(Map<String, Fx67llDortmundTeamOptions.Option> options,
            String label, String value, String type) {
        if (value == null || value.trim().isEmpty() || options.containsKey(value.trim())) {
            return;
        }
        options.put(value.trim(), new Fx67llDortmundTeamOptions.Option(
                label == null || label.trim().isEmpty() ? value.trim() : label.trim(),
                value.trim(), type));
    }

    /**
     * 新增球队管理
     *
     * @param fx67llDortmundBasicTeam 球队管理
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertFx67llDortmundBasicTeam(Fx67llDortmundBasicTeam fx67llDortmundBasicTeam) {
        validateTeamFields(fx67llDortmundBasicTeam);
        fx67llDortmundBasicTeam.setUserId(SecurityUtils.getUserId());
        fx67llDortmundBasicTeam.setCreateBy(SecurityUtils.getUsername());
        fx67llDortmundBasicTeam.setCreateTime(DateUtils.getNowDate());
        int rows = fx67llDortmundBasicTeamMapper.insertFx67llDortmundBasicTeam(fx67llDortmundBasicTeam);
        insertNewInfoPoints(fx67llDortmundBasicTeam);
        return rows;
    }

    /**
     * 修改球队管理
     *
     * @param fx67llDortmundBasicTeam 球队管理
     * @return 结果
     */
    @Override
    public int updateFx67llDortmundBasicTeam(Fx67llDortmundBasicTeam fx67llDortmundBasicTeam) {
        if (fx67llDortmundBasicTeam.getTeamId() == null) {
            throw new ServiceException("球队主键不能为空！");
        }
        if (fx67llDortmundBasicTeamMapper.selectFx67llDortmundBasicTeamByTeamId(
                fx67llDortmundBasicTeam.getTeamId()) == null) {
            throw new ServiceException("球队不存在或已被删除，请刷新后重试！");
        }
        validateTeamFields(fx67llDortmundBasicTeam);
        fx67llDortmundBasicTeam.setUpdateBy(SecurityUtils.getUsername());
        fx67llDortmundBasicTeam.setUpdateTime(DateUtils.getNowDate());
        return fx67llDortmundBasicTeamMapper.updateFx67llDortmundBasicTeam(fx67llDortmundBasicTeam);
    }

    /**
     * 批量删除球队管理
     *
     * @param teamIds 需要删除的球队管理主键
     * @return 结果
     */
    @Override
    public int deleteFx67llDortmundBasicTeamByTeamIds(Long[] teamIds) {
        if (teamIds == null || teamIds.length == 0) {
            return 0;
        }
        for (Long teamId : teamIds) {
            checkTeamReferenceBeforeDelete(teamId);
        }
        return fx67llDortmundBasicTeamMapper.deleteFx67llDortmundBasicTeamByTeamIds(teamIds);
    }

    /**
     * 删除球队管理信息
     *
     * @param teamId 球队管理主键
     * @return 结果
     */
    @Override
    public int deleteFx67llDortmundBasicTeamByTeamId(Long teamId) {
        checkTeamReferenceBeforeDelete(teamId);
        return fx67llDortmundBasicTeamMapper.deleteFx67llDortmundBasicTeamByTeamId(teamId);
    }

    /**
     * 删除前校验球队是否被比赛或信息点关系引用，被引用时返回统一"已被引用"错误
     *
     * @param teamId 球队管理主键
     */
    private void checkTeamReferenceBeforeDelete(Long teamId) {
        Fx67llDortmundBasicMatch matchQuery = new Fx67llDortmundBasicMatch();
        matchQuery.setHomeTeamId(teamId);
        List<Fx67llDortmundBasicMatch> homeMatchList = fx67llDortmundBasicMatchMapper.selectFx67llDortmundBasicMatchList(matchQuery);
        if (homeMatchList != null && !homeMatchList.isEmpty()) {
            throw new ServiceException("球队已被比赛引用，不能删除；可改用停用");
        }
        matchQuery.setHomeTeamId(null);
        matchQuery.setAwayTeamId(teamId);
        List<Fx67llDortmundBasicMatch> awayMatchList = fx67llDortmundBasicMatchMapper.selectFx67llDortmundBasicMatchList(matchQuery);
        if (awayMatchList != null && !awayMatchList.isEmpty()) {
            throw new ServiceException("球队已被比赛引用，不能删除；可改用停用");
        }
        Fx67llDortmundInfoPointTeamRel relQuery = new Fx67llDortmundInfoPointTeamRel();
        relQuery.setTeamId(teamId);
        List<Fx67llDortmundInfoPointTeamRel> relList = fx67llDortmundInfoPointTeamRelMapper.selectFx67llDortmundInfoPointTeamRelList(relQuery);
        if (relList != null && !relList.isEmpty()) {
            throw new ServiceException("球队已被信息点引用，不能删除；可改用停用");
        }
    }

    /**
     * 创建球队时同步写入关联的赛季球队信息点
     *
     * @param fx67llDortmundBasicTeam 球队管理对象
     */
    private void insertNewInfoPoints(Fx67llDortmundBasicTeam fx67llDortmundBasicTeam) {
        List<Fx67llDortmundInfoPoint> newInfoPoints = fx67llDortmundBasicTeam.getNewInfoPoints();
        if (newInfoPoints == null || newInfoPoints.isEmpty()) {
            return;
        }
        for (Fx67llDortmundInfoPoint infoPoint : newInfoPoints) {
            infoPoint.setInfoPointType("T");
            infoPoint.setTeamIds(new Long[] { fx67llDortmundBasicTeam.getTeamId() });
            fx67llDortmundInfoPointService.insertFx67llDortmundInfoPoint(infoPoint);
        }
    }

    /**
     * 校验球队字段并统一去除文本首尾空格，长度上限与 fx67ll_dortmund_team 表结构一致
     *
     * @param fx67llDortmundBasicTeam 球队管理对象
     */
    private void validateTeamFields(Fx67llDortmundBasicTeam fx67llDortmundBasicTeam) {
        fx67llDortmundBasicTeam.setTeamCode(DortmundFieldValidator.requireText(
                fx67llDortmundBasicTeam.getTeamCode(), "球队编码", TEAM_CODE));
        fx67llDortmundBasicTeam.setTeamName(DortmundFieldValidator.requireText(
                fx67llDortmundBasicTeam.getTeamName(), "球队全称", TEAM_NAME));
        fx67llDortmundBasicTeam.setTeamNameShort(DortmundFieldValidator.requireText(
                fx67llDortmundBasicTeam.getTeamNameShort(), "球队昵称", TEAM_NAME_SHORT));
        fx67llDortmundBasicTeam.setTeamNameEn(DortmundFieldValidator.requireText(
                fx67llDortmundBasicTeam.getTeamNameEn(), "球队英文名称", TEAM_NAME_EN));
        fx67llDortmundBasicTeam.setTeamLogoUrl(DortmundFieldValidator.requireText(
                fx67llDortmundBasicTeam.getTeamLogoUrl(), "球队Logo", TEAM_LOGO_URL));
        fx67llDortmundBasicTeam.setTeamVenue(DortmundFieldValidator.requireText(
                fx67llDortmundBasicTeam.getTeamVenue(), "球队主场", TEAM_VENUE));
        fx67llDortmundBasicTeam.setTeamCountry(DortmundFieldValidator.requireText(
                fx67llDortmundBasicTeam.getTeamCountry(), "地区/联赛", TEAM_COUNTRY));
        fx67llDortmundBasicTeam.setTeamTag(DortmundFieldValidator.requireText(
                fx67llDortmundBasicTeam.getTeamTag(), "球队标签", TEAM_TAG));
        fx67llDortmundBasicTeam.setTeamStatus(DortmundFieldValidator.validateEnum(
                fx67llDortmundBasicTeam.getTeamStatus(), "球队状态", true, "0", "1"));
        if (fx67llDortmundBasicTeam.getTeamSort() == null) {
            fx67llDortmundBasicTeam.setTeamSort(SORT_DEFAULT);
        }
        DortmundFieldValidator.validateInteger(fx67llDortmundBasicTeam.getTeamSort(),
                "球队排序", SORT_MIN, SORT_MAX, false);
        fx67llDortmundBasicTeam.setTeamRemark(DortmundFieldValidator.optionalText(
                fx67llDortmundBasicTeam.getTeamRemark(), "球队备注", TEAM_REMARK));
    }
}
