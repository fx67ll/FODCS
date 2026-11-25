package com.ruoyi.fx67ll.dortmund.service;

import java.util.List;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatch;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundBasicMatchExcel;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundExtra;

/**
 * 比赛记录Service接口
 * 
 * @author ruoyi
 * @date 2026-03-03
 */
public interface IFx67llDortmundBasicMatchService 
{
    /**
     * 查询比赛记录
     * 
     * @param matchId 比赛记录主键
     * @return 比赛记录
     */
    public Fx67llDortmundBasicMatch selectFx67llDortmundBasicMatchByMatchId(Long matchId);

    /**
     * 按当前用户数据范围查询比赛详情。
     * <p>普通用户仅能读取本人比赛；全部数据权限角色可读取全量比赛。</p>
     *
     * @param matchId 比赛记录主键
     * @return 当前用户可访问的比赛详情
     */
    public Fx67llDortmundBasicMatch selectFx67llDortmundBasicMatchDetailForCurrentUser(Long matchId);

    /**
     * 查询比赛记录列表
     * 
     * @param fx67llDortmundBasicMatch 比赛记录
     * @return 比赛记录集合
     */
    public List<Fx67llDortmundBasicMatch> selectFx67llDortmundBasicMatchList(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch);

    /**
     * 按当前用户数据范围查询比赛记录列表。
     * <p>普通用户强制按本人 user_id 过滤；全部数据权限角色不强行收窄归属范围。</p>
     *
     * @param fx67llDortmundBasicMatch 比赛记录
     * @return 当前用户可访问的比赛记录集合
     */
    public List<Fx67llDortmundBasicMatch> selectFx67llDortmundBasicMatchListForCurrentUser(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch);

    /**
     * 新增比赛记录
     * 
     * @param fx67llDortmundBasicMatch 比赛记录
     * @return 结果
     */
    public int insertFx67llDortmundBasicMatch(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch);

    /**
     * 修改比赛记录
     * 
     * @param fx67llDortmundBasicMatch 比赛记录
     * @return 结果
     */
    public int updateFx67llDortmundBasicMatch(Fx67llDortmundBasicMatch fx67llDortmundBasicMatch);

    /**
     * APP 端只更新比赛赛果与倍率赛果。
     * <p>
     * 移动端详情只允许补录或修正赛果，不允许改变赛季、主客队、开赛时间等编排字段；
     * 赛果变化时沿用 Web 端规则失效方案摘要和回报组合核验结论。
     * </p>
     *
     * @param matchId     比赛记录主键
     * @param matchResult 常规赛果，可为空表示清除
     * @param oddsResult  倍率赛果，可为空表示清除
     * @return 结果
     */
    public int updateMatchResultsForApp(Long matchId, String matchResult, String oddsResult);

    /**
     * 批量删除比赛记录
     * 
     * @param matchIds 需要删除的比赛记录主键集合
     * @return 结果
     */
    public int deleteFx67llDortmundBasicMatchByMatchIds(Long[] matchIds);

    /**
     * 删除比赛记录信息
     * 
     * @param matchId 比赛记录主键
     * @return 结果
     */
    public int deleteFx67llDortmundBasicMatchByMatchId(Long matchId);

    /**
     * 批量导入第三方AI或用户粘贴的标准化比赛 JSON。
     * <p>赛季和球队按业务名称/编码解析为当前用户可访问的主键；所有条目先完成校验再统一写入，
     * 任一条错误都会给出带条目序号的原因并回滚整批，保证页面预览序号与后端提示一致。</p>
     *
     * @param rows 用户确认插入的标准化比赛条目
     * @return 导入成功条数
     */
    public int importFx67llDortmundBasicMatchJson(List<Fx67llDortmundBasicMatchExcel> rows);
}
