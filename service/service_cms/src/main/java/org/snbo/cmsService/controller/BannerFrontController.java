package org.snbo.cmsService.controller;

import io.swagger.annotations.ApiOperation;
import org.snbo.cmsService.bean.CrmBanner;
import org.snbo.cmsService.service.CrmBannerService;
import org.snbo.commonutils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *     前台banner显示接口
 * </p>
 * @author sunbo
 * @create 2022-04-03-14:57
 */
@RestController
@RequestMapping("cmsService/bannerFront")
public class BannerFrontController {

    @Autowired
    private CrmBannerService bannerService;


    @GetMapping
    @ApiOperation(value = "(查)获取所有banner")
    public R getAllBanner(){
        List<CrmBanner> list = bannerService.selectAllBanner();
        return R.ok().data("banners",list);
    }
}
