package com.ruoyi.web.controller.fx67ll.mahjong;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.fx67ll.mahjong.domain.Fx67llMahjongRoom;
import com.ruoyi.fx67ll.mahjong.service.IFx67llMahjongRoomService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 麻将室Controller
 *
 * @author ruoyi
 * @date 2025-10-16
 */
@RestController
@RequestMapping("/mahjong/room")
public class Fx67llMahjongRoomController extends BaseController {
    @Autowired
    private IFx67llMahjongRoomService fx67llMahjongRoomService;

    /**
     * 查询麻将室列表
     */
    @PreAuthorize("@ss.hasPermi('mahjong:room:list')")
    @GetMapping("/list")
    public TableDataInfo list(Fx67llMahjongRoom fx67llMahjongRoom) {
        startPage();
        if (SecurityUtils.getUsername().equals("fx67ll")) {
            List<Fx67llMahjongRoom> list = fx67llMahjongRoomService.selectFx67llMahjongRoomList(fx67llMahjongRoom);
            return getDataTable(list);
        } else {
            List<Fx67llMahjongRoom> list = fx67llMahjongRoomService.selectFx67llMahjongRoomListByUserId(fx67llMahjongRoom);
            return getDataTable(list);
        }
    }

    /**
     * 导出麻将室列表
     */
    @PreAuthorize("@ss.hasPermi('mahjong:room:export')")
    @Log(title = "麻将室", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Fx67llMahjongRoom fx67llMahjongRoom) {
        List<Fx67llMahjongRoom> list = fx67llMahjongRoomService.selectFx67llMahjongRoomList(fx67llMahjongRoom);
        ExcelUtil<Fx67llMahjongRoom> util = new ExcelUtil<Fx67llMahjongRoom>(Fx67llMahjongRoom.class);
        util.exportExcel(response, list, "麻将室数据");
    }

    /**
     * 获取麻将室详细信息
     */
    @PreAuthorize("@ss.hasPermi('mahjong:room:query')")
    @GetMapping(value = "/{mahjongRoomId}")
    public AjaxResult getInfo(@PathVariable("mahjongRoomId") Long mahjongRoomId) {
        return success(fx67llMahjongRoomService.selectFx67llMahjongRoomByMahjongRoomId(mahjongRoomId));
    }

    /**
     * 提供给 APP 获取麻将室详细信息
     */
//    如果只放开SecurityConfig中允许匿名请求的配置，不放开这里的权限配置，会返回获取用户信息异常的错误
//    @PreAuthorize("@ss.hasPermi('mahjong:room:query')")
    @GetMapping(value = "/getMahjongRoomInfoForApp/{mahjongRoomId}")
    public AjaxResult getMahjongRoomInfoForApp(@PathVariable("mahjongRoomId") Long mahjongRoomId) {
        // 参数校验
        if (mahjongRoomId == null || mahjongRoomId <= 0) {
            return error("无效的麻将室ID，查询失败！");
        }
        return success(fx67llMahjongRoomService.selectFx67llMahjongRoomInfoForApp(mahjongRoomId));
    }


    /**
     * 新增麻将室
     */
    @PreAuthorize("@ss.hasPermi('mahjong:room:add')")
    @Log(title = "麻将室", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Fx67llMahjongRoom fx67llMahjongRoom) {
        return toAjax(fx67llMahjongRoomService.insertFx67llMahjongRoom(fx67llMahjongRoom));
    }

    /**
     * 修改麻将室
     */
    @PreAuthorize("@ss.hasPermi('mahjong:room:edit')")
    @Log(title = "麻将室", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Fx67llMahjongRoom fx67llMahjongRoom) {
        return toAjax(fx67llMahjongRoomService.updateFx67llMahjongRoom(fx67llMahjongRoom));
    }

    /**
     * 删除麻将室
     */
    @PreAuthorize("@ss.hasPermi('mahjong:room:remove')")
    @Log(title = "麻将室", businessType = BusinessType.DELETE)
    @DeleteMapping("/{mahjongRoomIds}")
    public AjaxResult remove(@PathVariable Long[] mahjongRoomIds) {
        return toAjax(fx67llMahjongRoomService.deleteFx67llMahjongRoomByMahjongRoomIds(mahjongRoomIds));
    }
}
