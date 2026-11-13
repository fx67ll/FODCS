package com.ruoyi.fx67ll.dortmund.service;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnCombination;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGenerateRequest;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGeneration;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnImage;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnImageCleanupResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * Dortmund回报组合Service接口
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public interface IFx67llDortmundReturnCombinationService {
    /**
     * 生成回报组合
     *
     * @param request 回报组合生成请求
     * @return 生成批次及候选组合
     */
    public Fx67llDortmundReturnGeneration generate(Fx67llDortmundReturnGenerateRequest request);

    /**
     * 执行一次异步批次调度扫描，由定时器和运维手动触发共用。
     */
    public void dispatchPendingGenerations();

    /**
     * 查询回报组合生成批次列表
     *
     * @param fx67llDortmundReturnGeneration 生成批次查询条件
     * @return 生成批次集合
     */
    public List<Fx67llDortmundReturnGeneration> selectGenerationList(
            Fx67llDortmundReturnGeneration fx67llDortmundReturnGeneration);

    /**
     * 查询回报组合生成批次详情
     *
     * @param generationId 生成批次主键
     * @param sortField    候选排序字段，可为空
     * @param sortOrder    候选排序方向，asc/desc，可为空
     * @return 生成批次详情
     */
    public Fx67llDortmundReturnGeneration selectGenerationById(Long generationId, String sortField, String sortOrder);

    /**
     * 查询回报组合列表
     *
     * @param fx67llDortmundReturnCombination 回报组合查询条件
     * @return 回报组合集合
     */
    public List<Fx67llDortmundReturnCombination> selectCombinationList(
            Fx67llDortmundReturnCombination fx67llDortmundReturnCombination);

    /**
     * 查询回报组合详情
     *
     * @param combinationId 回报组合主键
     * @return 回报组合详情
     */
    public Fx67llDortmundReturnCombination selectCombinationById(Long combinationId);

    /**
     * 修改回报组合采用状态
     *
     * @param combinationId 回报组合主键
     * @param adoptedFlag 是否采用
     * @return 结果
     */
    public int updateAdoptedFlag(Long combinationId, String adoptedFlag);

    /**
     * 批量采用回报组合。
     *
     * @param combinationIds 组合主键集合
     * @return 实际更新条数
     */
    public int batchAdopt(List<Long> combinationIds);

    /**
     * 为已采用回报组合上传图片。
     *
     * @param combinationId 回报组合主键
     * @param file          上传的图片文件
     * @return 已绑定的图片登记
     */
    public Fx67llDortmundReturnImage uploadCombinationImage(Long combinationId, MultipartFile file) throws Exception;

    /**
     * 删除已采用回报组合的图片。
     *
     * @param combinationId 回报组合主键
     * @param imageId       图片登记主键
     * @return 结果
     */
    public int removeCombinationImage(Long combinationId, Long imageId);

    /**
     * 查询组合当前有效图片，仅返回B状态。
     *
     * @param combinationId 回报组合主键
     * @return 有效图片集合
     */
    public List<Fx67llDortmundReturnImage> selectImagesByCombinationId(Long combinationId);

    /**
     * 查询时间范围内的孤儿图片。
     */
    public List<Fx67llDortmundReturnImage> selectOrphanImageList(Fx67llDortmundReturnImage query);

    /**
     * 批量删除孤儿图片并返回逐项结果。
     */
    public Fx67llDortmundReturnImageCleanupResult deleteOrphanImages(List<Long> imageIds);
}
