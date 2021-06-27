package com.system.dms.dao;

import com.system.dms.entity.DMS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DMSMapper {
    /**
     * 获取系统基本信息
     * @return 一个系统对象
     */
    @Select("select * from dms where id = 1")
    public DMS getBasicInfoOfSystem();

    /**
     * 更新系统基本信息
     * @param dms
     * @return 一个bool操作结果，如果操作成功返回true，否则false
     */
    @Update("update dms set systemname = #{systemname},copyright = #{copyright},status = #{status} where id = #{id}")
    public boolean updateBasicInfoOfSystem(DMS dms);
}
