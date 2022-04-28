package org.snbo.cmsService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.snbo.cmsService.bean.CrmBanner;
import org.snbo.cmsService.service.CrmBannerService;
import org.snbo.commonutils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;


/**
 * <p>
 * banner表 后台管理接口
 * </p>
 *
 * @author snbo
 * @since 2022-04-03
 */
@RestController
@RequestMapping("/cmsService/bannerAdmin")
public class BannerAdminController {

    @Autowired
    private CrmBannerService bannerService;

    @PostMapping
    @ApiOperation(value = "(增)添加banner")
    public R saveBanner(@RequestBody CrmBanner banner) {
        bannerService.saveBanner(banner);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "(删)根据id删除banner")
    public R removeBanner(@PathVariable String id) {
        bannerService.removeBannerById(id);
        return R.ok();
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "(查)根据id查找")
    public R getById(@PathVariable String id) {
        CrmBanner banner = bannerService.getById(id);
        return R.ok().data("banner", banner);
    }

    @PutMapping
    @ApiOperation(value = "(改)根据id修改Banner")
    public R updateBanner(@RequestBody CrmBanner banner) {
        bannerService.updateBannerById(banner);
        return R.ok();
    }

    @GetMapping("/{current}/{size}")
    @ApiOperation(value = "(查)分页查询")
    public R getPage(@PathVariable Long current, @PathVariable Long size) {
        Page<CrmBanner> bannerPage = new Page<>(current, size);
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
        bannerService.page(bannerPage, wrapper);
        return R.ok().data("total", bannerPage.getTotal()).data("banners", bannerPage.getRecords());
    }
}


