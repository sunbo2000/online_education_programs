package org.snbo.uCenterService.mapper;

import org.apache.ibatis.annotations.Param;
import org.snbo.uCenterService.bean.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author snbo
 * @since 2022-04-06
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {
    Integer countRegister(@Param("day") String day);
}
