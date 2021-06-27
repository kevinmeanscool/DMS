package com.system.dms.dao;

import com.system.dms.entity.Securitycode;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SecurityCodeMapper {

    @Select("select count(*) from SecurityCode where username = #{username}")
    int isRecordExisting(Securitycode securitycode);

    @Select("select count(*) from user where email = #{email}")
    int isEmailUsed(Securitycode securitycode);

    @Insert("insert into SecurityCode(id,username,securitycode,email) value (id,#{username},#{securitycode},#{email})")
    boolean addRecordOfSecurityCode(Securitycode securitycode);

    @Update("update SecurityCode set securitycode = #{securitycode},email = #{email} where username = #{username}")
    boolean updateRecordOfSecurityCode(Securitycode securitycode);

    @Select("select * from SecurityCode where username = #{username}")
    Securitycode getSecuritycodeObjectByUsername(@Param("username") String username);

    @Select("select count(*) from user where username = #{username}")
    int isUserNameExisting(@Param("username") String username);

    @Select("select count(*) from user where email = #{email}")
    int isEmailExisting(@Param("email") String email);

}
