package com.ruoyi.fx67ll.dortmund.service;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnCombination;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGenerateRequest;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGeneration;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnManualImportRequest;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnManualImportResult;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnManualTask;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnManualTemplate;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnImage;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnImageCleanupResult;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnVerificationResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * Dortmund回报组合Service接口
 *
 * @author fx67ll
 * @date 2026-11-09
 */
public interface IFx67llDortmundReturnCombinationService {
    /**
     * 查询后端当前生效的预定义业务目标。
     * <p>返回值只包含所有批次共享的固定约束，不包含用户补充目标、组合偏好或模式派生规则；
     * 前端据此展示只读约束说明，接口异常时使用模块公共配置兜底。</p>
     *
     * @return 预定义业务目标快照副本
     */
    public List<String> getPredefinedBusinessObjectives();

    /**
     * 生成回报组合
     *
     * @param request 回报组合生成请求
     * @return 生成批次及候选组合
     */
    public Fx67llDortmundReturnGeneration generate(Fx67llDortmundReturnGenerateRequest request);

    /**
     * 创建人工AI处理批次并冻结业务输入快照。
     *
     * @param request 回报组合生成请求
     * @return 可下载并交给第三方AI的标准任务
     */
    public Fx67llDortmundReturnManualTask createManualTask(Fx67llDortmundReturnGenerateRequest request);

    /**
     * 执行一次异步批次调度扫描，由定时器和运维手动触发共用。
     */
    public void dispatchPendingGenerations();

    /**
     * 为待人工导入或遗留失败批次构建人工第三方AI处理任务。
     *
     * @param generationId 生成批次主键
     * @return 可下载并交给第三方AI的标准任务
     */
    public Fx67llDortmundReturnManualTask buildManualTask(Long generationId);

    /**
     * 构建人工AI标准交换模板，供前端复制给第三方AI。
     *
     * @return 任务模板与标准化结果模板
     */
    public Fx67llDortmundReturnManualTemplate buildManualTemplate();

    /**
     * 导入第三方AI标准化结果并完成人工处理批次。
     *
     * @param generationId 生成批次主键
     * @param request      标准化结果正文
     * @return 导入结果
     */
    public Fx67llDortmundReturnManualImportResult importManualResult(Long generationId,
            Fx67llDortmundReturnManualImportRequest request);

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
     * 按人工登记的倍率赛果核验一条已采用回报组合。
     * <p>任一比赛缺少赛果时状态保持 null；全部一致为 Y；存在不一致为 N。</p>
     *
     * @param combinationId 回报组合主键
     * @return 单条核验结果
     */
    public Fx67llDortmundReturnVerificationResult verifyCombination(Long combinationId);

    /**
     * 批量核验已采用回报组合。
     *
     * @param combinationIds 回报组合主键集合
     * @return 成功、失败、未核验与跳过的汇总计数
     */
    public Fx67llDortmundReturnVerificationResult batchVerify(List<Long> combinationIds);

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
