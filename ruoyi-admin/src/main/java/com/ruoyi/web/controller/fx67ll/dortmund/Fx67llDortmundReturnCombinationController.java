package com.ruoyi.web.controller.fx67ll.dortmund;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnBatchAdoptRequest;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnBatchVerifyRequest;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnCombination;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnCombinationExportResponse;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGenerateRequest;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGeneration;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGenerationExportResponse;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnImage;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnManualImportRequest;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnManualTask;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnManualTemplate;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundReturnCombinationService;
import com.ruoyi.fx67ll.dortmund.util.DortmundExportUtils;

/**
 * Dortmund回报组合Controller
 *
 * @author fx67ll
 * @date 2026-11-09
 */
@RestController
@RequestMapping("/dortmund/return")
public class Fx67llDortmundReturnCombinationController extends BaseController {
    /** 组合导出字段白名单；证据链体积大且仅服务自动AI复核，不纳入通用数据导出。 */
    private static final String[] COMBINATION_EXPORT_COLUMNS = {
            "combinationId", "generationId", "combinationCode", "combinationTitle", "combinationReason",
            "combinedOdds", "estimatedReturn", "deviationAmount", "adoptedFlag", "reviewBy", "reviewTime",
            "verificationStatus", "verifyBy", "verifyTime", "combinationRemark", "aiConfidence", "riskNotes",
            "userId", "createBy", "createTime", "updateBy", "updateTime"
    };
    /** 批次导出仅保留业务可见字段，不暴露请求快照、模型调用快照与租约治理信息。 */
    private static final String[] GENERATION_EXPORT_COLUMNS = {
            "generationId", "generationCode", "generationMode", "inputAmount", "targetAmount",
            "generationStatus", "generationRemark", "generatedCount", "startedTime", "finishedTime",
            "userId", "resultSource", "manualImportBy", "manualImportTime", "dataAsOf",
            "createBy", "createTime", "updateBy", "updateTime"
    };

    @Autowired
    private IFx67llDortmundReturnCombinationService fx67llDortmundReturnCombinationService;

    /**
     * 查询后端当前生效的预定义业务目标，供分析页展示只读固定约束。
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:generate')")
    @GetMapping("/business-objectives")
    public AjaxResult businessObjectives() {
        return success(fx67llDortmundReturnCombinationService.getPredefinedBusinessObjectives());
    }

    /**
     * 生成回报组合
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:generate')")
    @Log(title = "Dortmund回报组合生成", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping("/generate")
    public AjaxResult generate(@RequestBody Fx67llDortmundReturnGenerateRequest request) {
        return success(fx67llDortmundReturnCombinationService.generate(request));
    }

    /**
     * 创建人工AI处理任务；任务创建即冻结业务快照，不进入自动AI调度队列。
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:generate')")
    @Log(title = "Dortmund回报组合人工任务", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping("/manual-task")
    public AjaxResult createManualTask(@RequestBody Fx67llDortmundReturnGenerateRequest request) {
        return success(fx67llDortmundReturnCombinationService.createManualTask(request));
    }

    /**
     * 查询回报组合生成批次列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:list')")
    @GetMapping("/generation/list")
    public TableDataInfo generationList(Fx67llDortmundReturnGeneration query) {
        startPage();
        return getDataTable(fx67llDortmundReturnCombinationService.selectGenerationList(query));
    }

    /**
     * 获取回报组合生成批次详情
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:query')")
    @GetMapping("/generation/{generationId:\\d+}")
    public AjaxResult generationInfo(@PathVariable("generationId") Long generationId,
            @RequestParam(value = "sortField", required = false) String sortField,
            @RequestParam(value = "sortOrder", required = false) String sortOrder) {
        return success(fx67llDortmundReturnCombinationService.selectGenerationById(
                generationId, sortField, sortOrder));
    }

    /**
     * 查询待人工导入或遗留失败批次的人工AI处理任务。
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:generate')")
    @GetMapping("/generation/{generationId:\\d+}/manual-task")
    public AjaxResult manualTask(@PathVariable("generationId") Long generationId) {
        Fx67llDortmundReturnManualTask task =
                fx67llDortmundReturnCombinationService.buildManualTask(generationId);
        return success(task);
    }

    /**
     * 获取可复制的人工AI任务与标准化结果模板。
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:generate')")
    @GetMapping("/manual-template")
    public AjaxResult manualTemplate() {
        Fx67llDortmundReturnManualTemplate template =
                fx67llDortmundReturnCombinationService.buildManualTemplate();
        return success(template);
    }

    /**
     * 导入第三方AI标准化结果并完成人工AI处理批次。
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:generate')")
    @Log(title = "Dortmund回报组合人工AI结果导入", businessType = BusinessType.INSERT)
    @PostMapping("/generation/{generationId:\\d+}/manual-import")
    public AjaxResult importManualResult(@PathVariable("generationId") Long generationId,
            @RequestBody Fx67llDortmundReturnManualImportRequest request) {
        return success(fx67llDortmundReturnCombinationService.importManualResult(generationId, request));
    }

    /**
     * 查询回报组合列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llDortmundReturnCombination query) {
        startPage();
        return getDataTable(fx67llDortmundReturnCombinationService.selectCombinationList(query));
    }

    /**
     * APP 查询回报组合卡片列表。
     * <p>沿用当前登录用户的数据范围；分页参数由 APP 专用分页工具解析，不依赖管理端菜单权限。</p>
     */
    @GetMapping("/getReturnCombinationListForApp")
    public TableDataInfo getReturnCombinationListForApp(Fx67llDortmundReturnCombination query) {
        startPageForApp();
        return getDataTable(fx67llDortmundReturnCombinationService.selectCombinationList(query));
    }

    /**
     * APP 查询回报组合详情。
     * <p>详情复用 Web 端业务组装逻辑；若账号没有管理端图片查询权限，APP 仍补齐当前组合有效图片，
     * 保证移动端图片上传区能够展示真实状态。图片操作本身仍由服务层校验数据范围与采用状态。</p>
     */
    @GetMapping("/getReturnCombinationInfoForApp/{combinationId:\\d+}")
    public AjaxResult getReturnCombinationInfoForApp(@PathVariable("combinationId") Long combinationId) {
        Fx67llDortmundReturnCombination detail =
                fx67llDortmundReturnCombinationService.selectCombinationById(combinationId);
        if (detail.getImages() == null) {
            detail.setImages(fx67llDortmundReturnCombinationService.selectImagesByCombinationId(combinationId));
        }
        return success(detail);
    }

    /**
     * APP 核验单条已采用回报组合。
     */
    @Log(title = "APP 回报组合赛果核验", businessType = BusinessType.UPDATE)
    @PostMapping("/verifyReturnCombinationForApp/{combinationId:\\d+}")
    public AjaxResult verifyReturnCombinationForApp(@PathVariable("combinationId") Long combinationId) {
        return success(fx67llDortmundReturnCombinationService.verifyCombination(combinationId));
    }

    /**
     * APP 为已采用回报组合上传图片。
     */
    @Log(title = "APP 回报组合图片", businessType = BusinessType.INSERT)
    @PostMapping("/uploadReturnImageForApp/{combinationId:\\d+}")
    public AjaxResult uploadReturnImageForApp(@PathVariable("combinationId") Long combinationId,
            @RequestParam("file") MultipartFile file) throws Exception {
        return success(fx67llDortmundReturnCombinationService.uploadCombinationImage(combinationId, file));
    }

    /**
     * 获取回报组合详情
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:query')")
    @GetMapping("/{combinationId:\\d+}")
    public AjaxResult getInfo(@PathVariable("combinationId") Long combinationId) {
        return success(fx67llDortmundReturnCombinationService.selectCombinationById(combinationId));
    }

    /**
     * 采用回报组合
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:review')")
    @Log(title = "Dortmund回报组合审核", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/{combinationId:\\d+}/adopt")
    public AjaxResult adopt(@PathVariable("combinationId") Long combinationId) {
        return toAjax(fx67llDortmundReturnCombinationService.updateAdoptedFlag(combinationId, "Y"));
    }

    /**
     * 不采用回报组合
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:review')")
    @Log(title = "Dortmund回报组合审核", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/{combinationId:\\d+}/reject")
    public AjaxResult reject(@PathVariable("combinationId") Long combinationId) {
        return toAjax(fx67llDortmundReturnCombinationService.updateAdoptedFlag(combinationId, "N"));
    }

    /**
     * 批量采用回报组合
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:review')")
    @Log(title = "Dortmund回报组合批量审核", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/batch/adopt")
    public AjaxResult batchAdopt(@RequestBody Fx67llDortmundReturnBatchAdoptRequest request) {
        return success(fx67llDortmundReturnCombinationService.batchAdopt(
                request == null ? null : request.getCombinationIds()));
    }

    /**
     * 按人工登记的比赛结果核验一条已采用回报组合
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:review')")
    @Log(title = "Dortmund回报组合赛果核验", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/{combinationId:\\d+}/verify")
    public AjaxResult verify(@PathVariable("combinationId") Long combinationId) {
        return success(fx67llDortmundReturnCombinationService.verifyCombination(combinationId));
    }

    /**
     * 批量核验已采用回报组合的赛果
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:review')")
    @Log(title = "Dortmund回报组合批量赛果核验", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/batch/verify")
    public AjaxResult batchVerify(@RequestBody Fx67llDortmundReturnBatchVerifyRequest request) {
        return success(fx67llDortmundReturnCombinationService.batchVerify(
                request == null ? null : request.getCombinationIds()));
    }

    /**
     * 为已采用回报组合上传图片
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:image:upload')")
    @Log(title = "Dortmund回报组合图片", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping("/{combinationId:\\d+}/image")
    public AjaxResult uploadImage(@PathVariable("combinationId") Long combinationId,
            @RequestParam("file") MultipartFile file) throws Exception {
        return success(fx67llDortmundReturnCombinationService.uploadCombinationImage(combinationId, file));
    }

    /**
     * 删除已采用回报组合的图片
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:image:remove')")
    @Log(title = "Dortmund回报组合图片", businessType = BusinessType.DELETE)
    @DeleteMapping("/{combinationId:\\d+}/image/{imageId:\\d+}")
    public AjaxResult removeImage(@PathVariable("combinationId") Long combinationId,
            @PathVariable("imageId") Long imageId) {
        return toAjax(fx67llDortmundReturnCombinationService.removeCombinationImage(combinationId, imageId));
    }

    /**
     * 查询指定日期范围内的孤儿图片
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:image:query')")
    @GetMapping("/image/orphans")
    public AjaxResult orphanImages(Fx67llDortmundReturnImage query) {
        return success(fx67llDortmundReturnCombinationService.selectOrphanImageList(query));
    }

    /**
     * 批量删除选中的孤儿图片
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:image:remove')")
    @Log(title = "Dortmund孤儿回报组合图片", businessType = BusinessType.DELETE)
    @RepeatSubmit
    @DeleteMapping("/image/orphans")
    public AjaxResult removeOrphanImages(@RequestBody List<Long> imageIds) {
        return success(fx67llDortmundReturnCombinationService.deleteOrphanImages(imageIds));
    }

    /**
     * 导出回报组合列表
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:export')")
    @Log(title = "Dortmund回报组合", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llDortmundReturnCombination query,
            @RequestParam(value = "exportType", required = false, defaultValue = "excel") String exportType)
            throws IOException {
        List<Fx67llDortmundReturnCombination> list = fx67llDortmundReturnCombinationService.selectCombinationList(query);
        if (DortmundExportUtils.isStructured(exportType)) {
            DortmundExportUtils.export(response, "dortmund-return-combination", exportType,
                    "fx67ll_dortmund_return_combination", COMBINATION_EXPORT_COLUMNS, list);
            return;
        }
        List<Fx67llDortmundReturnCombinationExportResponse> exportList = new ArrayList<>();
        for (Fx67llDortmundReturnCombination item : list) {
            exportList.add(new Fx67llDortmundReturnCombinationExportResponse(item));
        }
        ExcelUtil<Fx67llDortmundReturnCombinationExportResponse> util =
                new ExcelUtil<Fx67llDortmundReturnCombinationExportResponse>(
                        Fx67llDortmundReturnCombinationExportResponse.class);
        util.exportExcel(response, exportList, "Dortmund回报组合");
    }

    /**
     * 导出生成批次记录；不包含请求快照和AI供应商治理信息
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:export')")
    @Log(title = "Dortmund回报组合批次", businessType = BusinessType.EXPORT)
    @PostMapping("/generation/export")
    public void exportGenerations(HttpServletResponse response, Fx67llDortmundReturnGeneration query,
            @RequestParam(value = "exportType", required = false, defaultValue = "excel") String exportType)
            throws IOException {
        List<Fx67llDortmundReturnGeneration> list =
                fx67llDortmundReturnCombinationService.selectGenerationList(query);
        if (DortmundExportUtils.isStructured(exportType)) {
            DortmundExportUtils.export(response, "dortmund-return-generation", exportType,
                    "fx67ll_dortmund_return_generation", GENERATION_EXPORT_COLUMNS, list);
            return;
        }
        List<Fx67llDortmundReturnGenerationExportResponse> exportList = new ArrayList<>();
        for (Fx67llDortmundReturnGeneration item : list) {
            exportList.add(new Fx67llDortmundReturnGenerationExportResponse(
                    item,
                    item.getStartedTime() == null ? null
                            : DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", item.getStartedTime()),
                    item.getFinishedTime() == null ? null
                            : DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", item.getFinishedTime())));
        }
        ExcelUtil<Fx67llDortmundReturnGenerationExportResponse> util =
                new ExcelUtil<Fx67llDortmundReturnGenerationExportResponse>(
                        Fx67llDortmundReturnGenerationExportResponse.class);
        util.exportExcel(response, exportList, "Dortmund回报组合批次");
    }
}
