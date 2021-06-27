package com.system.dms.dao;

import com.system.dms.entity.Project;
import com.system.dms.entity.ProjectMember;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProjectMapper {
    @Insert("insert into Project(id,projectname,category,environment,pattern,accessright,buildtime,finalupdatetime,statement,operatorid) value (id,#{projectname},#{category},#{environment},#{pattern},#{accessright},CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,#{statement},#{operatorid})")
    boolean buildProject(Project project);
    @Select("select * from Project where operatorid = #{userId}")
    List<Project> getProjectListByUId(@Param("userId") long userId);
    @Select("select * from Project where id = #{proId}")
    Project getProjectByProId(@Param("proId") long proId);
    @Select("select count(*) from Project where operatorid = #{uid} and projectname = #{projectName}")
    int isProjectNameBuiltBySameUId(@Param("projectName") String projectName,@Param("uid") long uid);
    @Select("select id from Project where operatorid = #{uid} and projectname = #{projectName}")
    long getProIdByProNameAndOperatorId(@Param("projectName") String projectName,@Param("uid") long uid);
    @Insert("insert into ProjectMember(id,proid,userid,membername,roletype,registertime,finalupdatetime,statement) value (id,#{proid},#{userid},#{membername},#{roletype},CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,#{statement})")
    boolean insertProjectMember(ProjectMember projectMember);
    @Select("select proid from ProjectMember where userid = #{uid} and roletype != 0 and roletype != 1")
    List<Long> getProIdByUIdAsParticipant(@Param("uid") long uid);
    @Select("select count(*) from ProjectMember where userid = #{userid} and proid = #{proid} and statement != 2")
    int isUserIdExistingInProId(ProjectMember projectMember);
    @Insert("insert into ProjectMember(id,proid,userid,membername,roletype,registertime,finalupdatetime,statement) value (id,#{proid},#{userid},#{membername},#{roletype},CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,#{statement})")
    boolean addMemberRecord(ProjectMember projectMember);
    @Select("select * from ProjectMember where proid = #{proId}")
    List<ProjectMember> getProjectMemberListByProId(@Param("proId") long proId);
    @Delete("delete from ProjectMember where proid = #{proId} and userid = #{uid}")
    boolean removeMemberRecord(@Param("proId") long proId,@Param("uid") long uid);
    @Update("update ProjectMember set membername = #{membername}, roletype = #{roletype} where proid = #{proId} and userid = #{uid}")
    boolean updateMemberRecord(@Param("proId") long proId,@Param("uid") long uid,@Param("membername") String membername,@Param("roletype") long roletype);
    @Select("select count(*) from Project where id = #{proId} and operatorid = #{uid}")
    int isUserProjectBuilder(@Param("proId") long proId,@Param("uid") long uid);
    @Select("select * from ProjectMember where proid = #{proId} and userid = #{uid} and (roletype = 0 or roletype = 1)")
    ProjectMember getProjectMemberObjAsBuilder(@Param("proId") long proId, @Param("uid") long uid);
    @Select("select * from ProjectMember where proid = #{proId} and userid = #{uid} and roletype != 0 and roletype != 1")
    ProjectMember getProjectMemberObjAsPartner(@Param("proId") long proId, @Param("uid") long uid);
    @Delete("delete from ProjectMember where proid = #{proId}")
    boolean delProjectAllMember(@Param("proId") long proId);
}
