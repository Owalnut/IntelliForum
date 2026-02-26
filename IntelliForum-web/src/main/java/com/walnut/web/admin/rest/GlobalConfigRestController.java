package com.walnut.web.admin.rest;

import com.walnut.api.model.vo.PageVo;
import com.walnut.api.model.vo.ResVo;
import com.walnut.api.model.vo.config.GlobalConfigReq;
import com.walnut.api.model.vo.config.SearchGlobalConfigReq;
import com.walnut.api.model.vo.config.dto.GlobalConfigDTO;
import com.walnut.core.permission.Permission;
import com.walnut.core.permission.UserRole;
import com.walnut.service.config.service.GlobalConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 标签后台
 *
 * @author XuYifei
 * @date 2024-07-12
 */
@RestController
@Permission(role = UserRole.LOGIN)
@Tag(name = "全局配置管理控制器", description = "全局配置")
@RequestMapping(path = {"api/admin/global/config/", "admin/global/config/"})
public class GlobalConfigRestController {

    @Autowired
    private GlobalConfigService globalConfigService;

    @Permission(role = UserRole.ADMIN)
    @PostMapping(path = "save")
    public ResVo<String> save(@RequestBody GlobalConfigReq req) {
        globalConfigService.save(req);
        return ResVo.ok("ok");
    }

    @Permission(role = UserRole.ADMIN)
    @GetMapping(path = "delete")
    public ResVo<String> delete(@RequestParam(name = "id") Long id) {
        globalConfigService.delete(id);
        return ResVo.ok("ok");
    }

    @PostMapping(path = "list")
    @Permission(role = UserRole.ADMIN)
    public ResVo<PageVo<GlobalConfigDTO>> list(@RequestBody SearchGlobalConfigReq req) {
        PageVo<GlobalConfigDTO> page = globalConfigService.getList(req);
        return ResVo.ok(page);
    }
}

