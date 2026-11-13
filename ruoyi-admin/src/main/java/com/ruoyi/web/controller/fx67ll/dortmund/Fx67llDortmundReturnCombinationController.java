package com.ruoyi.web.controller.fx67ll.dortmund;

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
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnCombination;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnCombinationExportResponse;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGenerateRequest;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGeneration;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnGenerationExportResponse;
import com.ruoyi.fx67ll.dortmund.domain.Fx67llDortmundReturnImage;
import com.ruoyi.fx67ll.dortmund.service.IFx67llDortmundReturnCombinationService;

/**
 * Dortmund回报组合Controller
 *
 * @author fx67ll
 * @date 2026-11-09
 */
@RestController
@RequestMapping("/dortmund/return")
public class Fx67llDortmundReturnCombinationController extends BaseController {
    @Autowired
    private IFx67llDortmundReturnCombinationService fx67llDortmundReturnCombinationService;

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
    @GetMapping("/generation/{generationId}")
    public AjaxResult generationInfo(@PathVariable("generationId") Long generationId,
            @RequestParam(value = "sortField", required = false) String sortField,
            @RequestParam(value = "sortOrder", required = false) String sortOrder) {
        return success(fx67llDortmundReturnCombinationService.selectGenerationById(
                generationId, sortField, sortOrder));
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
     * 获取回报组合详情
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:query')")
    @GetMapping("/{combinationId}")
    public AjaxResult getInfo(@PathVariable("combinationId") Long combinationId) {
        return success(fx67llDortmundReturnCombinationService.selectCombinationById(combinationId));
    }

    /**
     * 采用回报组合
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:review')")
    @Log(title = "Dortmund回报组合审核", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/{combinationId}/adopt")
    public AjaxResult adopt(@PathVariable("combinationId") Long combinationId) {
        return toAjax(fx67llDortmundReturnCombinationService.updateAdoptedFlag(combinationId, "Y"));
    }

    /**
     * 不采用回报组合
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:review')")
    @Log(title = "Dortmund回报组合审核", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/{combinationId}/reject")
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
     * 为已采用回报组合上传图片
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:image:upload')")
    @Log(title = "Dortmund回报组合图片", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping("/{combinationId}/image")
    public AjaxResult uploadImage(@PathVariable("combinationId") Long combinationId,
            @RequestParam("file") MultipartFile file) throws Exception {
        return success(fx67llDortmundReturnCombinationService.uploadCombinationImage(combinationId, file));
    }

    /**
     * 删除已采用回报组合的图片
     */
    @PreAuthorize("@ss.hasPermi('dortmund:return:image:remove')")
    @Log(title = "Dortmund回报组合图片", businessType = BusinessType.DELETE)
    @DeleteMapping("/{combinationId}/image/{imageId}")
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
    public void export(HttpServletResponse response, Fx67llDortmundReturnCombination query) {
        List<Fx67llDortmundReturnCombination> list = fx67llDortmundReturnCombinationService.selectCombinationList(query);
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
    public void exportGenerations(HttpServletResponse response, Fx67llDortmundReturnGeneration query) {
        List<Fx67llDortmundReturnGeneration> list =
                fx67llDortmundReturnCombinationService.selectGenerationList(query);
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
