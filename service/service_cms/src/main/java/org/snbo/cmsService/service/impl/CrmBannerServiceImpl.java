package org.snbo.cmsService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.snbo.cmsService.bean.CrmBanner;
import org.snbo.cmsService.mapper.CrmBannerMapper;
import org.snbo.cmsService.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author snbo
 * @since 2022-04-03
 */

@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {
    //查询所有
    @Override
    @Cacheable(value = "banner", key = "'selectIndexList'")
    public List<CrmBanner> selectAllBanner() {
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort");
        wrapper.last("limit 4");
        List<CrmBanner> list = baseMapper.selectList(wrapper);
        return list;
    }

    @Override
    @CacheEvict(value = "banner", allEntries = true)
    public void removeBannerById(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @CacheEvict(value = "banner", allEntries = true)
    public void saveBanner(CrmBanner banner) {
        baseMapper.insert(banner);
    }

    @Override
    @CacheEvict(value = "banner", allEntries = true)
    public void updateBannerById(CrmBanner banner) {
        baseMapper.updateById(banner);
    }


}
