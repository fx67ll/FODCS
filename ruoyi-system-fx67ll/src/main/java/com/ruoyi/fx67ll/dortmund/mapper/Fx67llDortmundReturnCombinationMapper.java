package com.ruoyi.fx67ll.dortmund.mapper;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnCombination;
import org.apache.ibatis.annotations.Param;

/**
 * 回报组合Mapper接口
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public interface Fx67llDortmundReturnCombinationMapper {
    public Fx67llDortmundReturnCombination selectFx67llDortmundReturnCombinationByCombinationId(Long combinationId);

    public Fx67llDortmundReturnCombination selectFx67llDortmundReturnCombinationByCombinationIdForUpdate(
            Long combinationId);

    public List<Fx67llDortmundReturnCombination> selectFx67llDortmundReturnCombinationList(
            Fx67llDortmundReturnCombination fx67llDortmundReturnCombination);

    public int insertFx67llDortmundReturnCombination(
            Fx67llDortmundReturnCombination fx67llDortmundReturnCombination);

    public int updateAdoptedFlag(Fx67llDortmundReturnCombination fx67llDortmundReturnCombination);

    /**
     * 写入赛果核验状态和最近核验审计信息。
     *
     * @param fx67llDortmundReturnCombination 包含组合主键、状态及核验审计字段的对象
     * @return 更新条数
     */
    public int updateVerificationResult(Fx67llDortmundReturnCombination fx67llDortmundReturnCombination);

    /**
     * 比赛结果发生变化后，将该比赛参与的全部回报组合恢复为未核验。
     *
     * @param matchId  比赛主键
     * @param updateBy 触发失效的用户名
     * @return 更新条数
     */
    public int resetVerificationByMatchId(@Param("matchId") Long matchId, @Param("updateBy") String updateBy);

    /**
     * 批量查询组合核验状态，供方案计划列表聚合展示。
     *
     * @param combinationIds 回报组合主键集合
     * @return 仅包含组合主键和核验状态的记录
     */
    public List<Fx67llDortmundReturnCombination> selectVerificationStatusesByIds(
            @Param("combinationIds") List<Long> combinationIds);

    public int countPlanRelationsByCombinationId(Long combinationId);

    /**
     * 统计批次下已落库的回报组合数量。
     *
     * @param generationId 生成批次主键
     * @return 组合数量
     */
    public int countByGenerationId(Long generationId);

    /** 查询批次全部候选，用于追加导入的签名去重。 */
    public List<Fx67llDortmundReturnCombination> selectByGenerationId(Long generationId);

    /** 锁定批次全部候选，防止覆盖重导与采用、方案关联或图片登记并发交错。 */
    public List<Fx67llDortmundReturnCombination> selectByGenerationIdForUpdate(Long generationId);

    /** 统计批次内已采用候选数；覆盖导入前必须为零。 */
    public int countAdoptedByGenerationId(Long generationId);

    /** 统计批次候选的方案关联数；覆盖导入前必须为零。 */
    public int countPlanRelationsByGenerationId(Long generationId);

    /** 统计批次候选的图片登记数；覆盖导入前必须为零。 */
    public int countImagesByGenerationId(Long generationId);

    /** 覆盖导入时删除批次候选主表记录；调用方必须先清理明细并通过全部业务守卫。 */
    public int deleteByGenerationId(Long generationId);
}
