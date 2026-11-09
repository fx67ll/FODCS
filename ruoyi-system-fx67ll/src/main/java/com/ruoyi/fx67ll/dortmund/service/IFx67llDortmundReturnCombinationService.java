package com.ruoyi.fx67ll.dortmund.service;

import java.util.List;

import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnCombination;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGenerateRequest;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGeneration;
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
     * @return 生成批次详情
     */
    public Fx67llDortmundReturnGeneration selectGenerationById(Long generationId);

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
     * 上传回报组合图片
     *
     * @param file 上传的图片文件
     * @return 相对对象键
     */
    public String uploadReturnImage(MultipartFile file) throws Exception;

    /**
     * 删除尚未固化的回报组合图片
     *
     * @param objectKey 图片相对对象键
     * @return 结果
     */
    public int removeTemporaryReturnImage(String objectKey);
}
