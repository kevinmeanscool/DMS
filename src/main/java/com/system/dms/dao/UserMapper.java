package com.system.dms.dao;

import com.system.dms.entity.Securitycode;
import com.system.dms.entity.User;
import com.system.dms.staticEnum.UserStatusEnum;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select count(*) from user where username = #{username}")
    int isUserNameExisting(User user);
    @Select("select count(*) from user where username =#{username}")
    int isUserNameExisted(@RequestParam("username") String username);
    @Insert("INSERT INTO User(id,UserName,Password,Email,Gender,PersonProfile,ImageUrl,Identity,Nickname,Status,RegisterTime,FinalUpdateTime) VALUE (id,#{username},#{password},#{email},#{gender},#{personprofile},#{imageurl},#{identity},#{nickname},#{status},CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);")
    boolean register(User user);
    @Select("select count(*) from user where username = #{username} and password = #{password}")
    int isPasswordRight(User user);
    @Select("select * from user where (username = #{username} and password = #{password}) or (username = #{username})")
    boolean login(User user);
    @Update("update user set status = #{status} where username = #{username}")
    boolean online(@Param("username") String username,@Param("status") int status);
    @Update("update user set status = #{status} where username = #{username}")
    boolean offline(@Param("username") String username,@Param("status") int status);
    @Select("select * from user where id = #{userId}")
    User getUserInfoById(long userId);
    @Select("select * from user where username = #{username}")
    User getUserInfoByUsername(String username);
    @Select("select * from user")
    List<User> getAllUserInfo();
    @Update("update user set imageurl = #{imageurl} where username = #{username}")
    boolean uploadHeadImage(User user);
    @Update("update user set nickname = #{nickname},gender = #{gender},personprofile = #{personprofile} where id = #{id}")
    boolean updateUserInfoById(User user);
    @Update("update user set password = #{newPassword} where id = #{userId}")
    boolean changePasswordById(@Param("userId") long userId,@Param("newPassword") String newPassword);
    @Update("update user set password = #{newPassword} where username = #{username}")
    boolean changePasswordByUsername(@Param("username")String username,@Param("newPassword") String newPassword);
    @Select("select username from user where id = #{uid}")
    String getUsernameById(@Param("uid") long uid);
    @Select("select username from user where email = #{email}")
    List<String> getUsernamesByEmail(@Param("email") String email);
    @Select("select count(*) from user where email = #{email}")
    int isAccountCountRight(User user);
    @Update("update User set identity = #{identity} where id = #{userId}")
    boolean changeIdentity(@Param("userId") long userId,@Param("identity") long identity);
}
