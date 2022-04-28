package org.snbo.cmsService.service;

import org.snbo.cmsService.bean.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author snbo
 * @since 2022-04-03
 */
public interface CrmBannerService extends IService<CrmBanner> {
    List<CrmBanner> selectAllBanner();
    void removeBannerById(String id);
    void saveBanner(CrmBanner banner);
    void updateBannerById(CrmBanner banner);
}
