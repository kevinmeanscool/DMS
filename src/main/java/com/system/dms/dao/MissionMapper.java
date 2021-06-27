package com.system.dms.dao;

import com.system.dms.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MissionMapper {
    //MissionModule
    @Select("select * from MissionModule where id = #{id}")
    MissionModule getMissionModuleObjById(@Param("id") long id);
    @Select("select * from MissionModule where category = #{category} and moduleindex = #{moduleIndex}")
    MissionModule getMissionModuleByCategoryAndIndex(@Param("category") long category,@Param("moduleIndex") long moduleIndex);
    @Insert("insert into MissionModule value (id,#{mimoname},#{category},#{moduleindex},#{statement},current_timestamp,current_timestamp,#{fatherindex},#{operatorid},#{proid})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    boolean addMissionMudule(MissionModule missionModule);
    @Delete("delete from MissionModule where id = #{id}")
    boolean removeMissionModule(@Param("id") long id);
    @Select("select * from MissionModule where category = #{category} and moduleindex = #{index} and operatorid = #{uid} and statement = #{statement}")
    MissionModule getInitialModulesOfProjectBuilder(@Param("uid") long uid,@Param("index") long index, @Param("category") long category,@Param("statement") long statement);
    @Select("select * from MissionModule where category = #{category} and moduleindex = #{index} and operatorid = #{uid} and statement = #{statement}")
    MissionModule getPublishedModulesOfProjectBuilder(@Param("uid") long uid,@Param("index") long index, @Param("category") long category,@Param("statement") long statement);
    @Select("select * from MissionModule where category = #{category} and statement = #{statement} and operatorid = #{operatorId} and proid = #{proId}")
    List<MissionModule> getInitialModulesOfProjectPartner(@Param("proId") long proId,@Param("operatorId") long uid,@Param("category") long category,@Param("statement") long statement);
    @Select("select * from MissionModule where category = #{category} and statement = #{statement} and operatorid = #{operatorId} and proid = #{proId}")
    List<MissionModule> getPublishedModulesOfProjectPartner(@Param("proId") long proId,@Param("operatorId") long uid,@Param("category") long category,@Param("statement") long statement);
    @Update("update MissionModule set statement = #{newStatement} where id = #{id}")
    boolean setStatementToNewStatement(@Param("id") long id,@Param("newStatement") long newStatement);
    @Select("select count(*) from MissionModule where fatherindex = #{missionModuleId} and category = 4 and statement = #{statement}")
    int isTypeDefectsOfMissionModuleExisting(@Param("missionModuleId") long missionModuleId,@Param("statement")long statement);
    @Select("select * from MissionModule where fatherindex = #{missionModuleId} and category = 4")
    List<MissionModule> getAllDefectMissionModuleOfMissionModule(@Param("missionModuleId") long missionModuleId);
    @Select("select * from MissionModule where statement = #{statement} and fatherindex = #{moduleId}")
    List<MissionModule> getAllStatementSonModule(@Param("moduleId")long moduleId,@Param("statement")long statement);
    @Select("select * from MissionModule where fatherindex = #{id}")
    List<MissionModule> getALlSonNode(MissionModule missionModule);
    @Update("update MissionModule set mimoname = #{mimoName} where category = #{category} and moduleindex = #{moduleIndex}")
    boolean updateMissionModuleName(@Param("mimoName") String mimoName,@Param("category") long category,@Param("moduleIndex") long moduleIndex);


    //Mission
    @Insert("insert into Mission value(id,#{mmid},#{missionstatus},current_timestamp,current_timestamp,#{publisherid},#{receiverid},#{proid})")
    boolean publishMission(Mission mission);
    @Select("select * from Mission where publisherid = #{publisherId} and status = #{status}")
    List<Mission> getMissionOfPublisherByPublisherIdAndStatus(long publisherId,long status);
    @Select("select * from Mission where receiverid = #{receiverId} and missionstatus = #{missionStatus} and proid = #{proId}")
    List<Mission> getMissionListByOwn(@Param("proId") long proId,@Param("receiverId") long receiverId, @Param("missionStatus") long missionStatus);
    @Select("select * from Mission where mmid = #{moduleId}")
    Mission getMissionObjByModuleId(@Param("moduleId") long moduleId);
    @Select("select * from Mission where missionstatus = #{status} and publisherid = #{publisherId} and proid = #{proId}")
    List<Mission> getMissionListOfPublisherByStatus(@Param("publisherId") long publisherId, @Param("status") long status, @Param("proId") long proId);
    @Select("select * from Mission where missionstatus = #{status} and receiverid = #{receiverId} and proid = #{proId}")
    List<Mission> getMissionListOfReceiverByStatus(@Param("receiverId")long receiverId, @Param("status") long status, @Param("proId") long proId);
    @Update("update Mission set missionStatus = #{status} where mmid = #{moduleId}")
    boolean changeMissionStatus(@Param("moduleId") long moduleId, @Param("status") long status);
    @Select("select count(*) from Mission where mmid = #{moduleId}")
    int isMissionExistingByMissionModule(long moduleId);
    @Delete("delete from Mission where mmid = #{moduleId}")
    boolean deleteMission(@Param("moduleId") long moduleId);

    //TestList
    @Insert("insert into TestList value(id,#{mmid},#{missionstatus},current_timestamp,current_timestamp,#{publisherid},#{receiverid},#{proid})")
    boolean publishTestList(TestList testList);
    @Select("select * from TestList where mmid = #{mmid}")
    TestList getTestMissionByMMId(@Param("mmid") long mmid);
    @Select("select * from TestList where receiverid = #{receiverId} and missionstatus = #{status} and proid = #{proId}")
    List<TestList> getTestListOfReceiverByStatus(@Param("receiverId") long receiverId, @Param("status") long status, @Param("proId") long proId);
    @Update("update TestList set missionstatus = #{status} where mmid = #{moduleId}")
    boolean changeTestStatus(@Param("moduleId") long moduleId,@Param("status") long status);
    @Delete("delete from TestList where mmid = #{moduleId}")
    boolean deleteTestMission(@Param("moduleId") long moduleId);
    @Select("select count(*) from TestList where mmid = #{moduleId}")
    int isTestMissionExistingByMissionModule(long moduleId);

    //AssignMission
    @Insert("insert into AssignMission value(id,#{mmid},#{missionstatus},current_timestamp,current_timestamp,#{publisherid},#{receiverid},#{proid})")
    boolean publishAssignMission(AssignMission assignMission);
    @Select("select * from AssignMission where missionstatus = #{status} and receiverid = #{receiverId} and proid = #{proId}")
    List<AssignMission> getAssignMissionListOfReceiverByStatus(@Param("receiverId")long receiverId, @Param("status") long status, @Param("proId") long proId);
    @Select("select * from AssignMission where mmid = #{moduleId}")
    AssignMission getAssignMissionObjByModuleId(@Param("moduleId")long moduleId);
    @Update("update AssignMission set missionStatus = #{status} where mmid = #{moduleId}")
    boolean changeAssignMissionStatus(@Param("moduleId") long moduleId, @Param("status") long status);
    @Select("select * from AssignMission where missionstatus = #{status} and publisherid = #{publisherId} and proid = #{proId}")
    List<AssignMission> getAssignMissionListOfPublisherByStatus(@Param("publisherId")long publisherId, @Param("status") long status, @Param("proId") long proId);
    @Select("select count(*) from AssignMission where mmid = #{moduleId}")
    int isAssignMissionExistingByMissionModule(long moduleId);
    @Delete("delete from AssignMission where mmid = #{moduleId}")
    boolean deleteAssignMission(@Param("moduleId") long moduleId);

    //ProjectMember
    @Select("select * from ProjectMember where proid = #{proId} and roletype = #{roleType}")
    List<ProjectMember> searchMemberByPIDAndRoleType(@Param("proId") long proId, @Param("roleType") long roleType);


    //Project
    @Select("select * from Project where id = #{id}")
    Project getProjectById(@Param("id") long id);
    @Delete("delete from Project where id = #{id}")
    boolean removeProject(@Param("id") long id);
    @Update("update Project set projectname = #{projectname},environment = #{environment},pattern = #{pattern} where id = #{id}")
    boolean updateProject(Project project);

    //Requirement
    @Insert("insert into Requirement value (id,#{reqname},#{businessreq},#{userreq},#{functionalreq},#{nonfunctionalreq},current_timestamp,current_timestamp,#{statement},#{operatorid})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    boolean addReq(Requirement requirement);
    @Delete("delete from Requirement where id = #{id}")
    boolean removeReq(@Param("id") long id);
    @Select("select * from Requirement where id = #{reqId}")
    Requirement getReq(@Param("reqId") Long reqId);
    @Update("update Requirement set reqname = #{reqname},businessreq = #{businessreq},userreq = #{userreq},functionalreq = #{functionalreq},nonfunctionalreq = #{nonfunctionalreq} where id = #{id}")
    boolean updateReq(Requirement requirement);

    //TotalDesign
    @Insert("insert into TotalDesign value (id,#{tolname},#{processlogic},#{softwarestructure},current_timestamp,current_timestamp,#{statement},#{operatorid})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    boolean addTolDesign(TotalDesign totalDesign);
    @Update("update TotalDesign set tolname = #{tolname},processlogic = #{processlogic},softwarestructure = #{softwarestructure} where id = #{id}")
    boolean updateTolDesign(TotalDesign totalDesign);
    @Update("update TotalDesign set tolname = #{tolname},processlogic = #{processlogic} where id = #{id}")
    boolean updateTolDesign_noImg(TotalDesign totalDesign);
    @Select("select * from TotalDesign where id = #{id}")
    TotalDesign getTotalDesignObjById(@Param("id") long id);
    @Delete("delete from TotalDesign where id = #{id}")
    boolean removeTolDesign(@Param("id") long id);

    //DetailedDesign
    @Insert("insert into DetailedDesign value (id,#{detname},#{processlogic},#{arithmetic},#{limitingcondition},#{input},#{output},current_timestamp,current_timestamp,#{statement},#{operatorid})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    boolean addDetDesign(DetailedDesign detailedDesign);
    @Update("update DetailedDesign set detname = #{detname},processlogic = #{processlogic},arithmetic = #{arithmetic}, limitingcondition = #{limitingcondition}, input = #{input}, output = #{output} where id = #{id}")
    boolean updateDetDesign(DetailedDesign detailedDesign);
    @Update("update DetailedDesign set detname = #{detname},arithmetic = #{arithmetic}, limitingcondition = #{limitingcondition}, input = #{input}, output = #{output} where id = #{id}")
    boolean updateDetDesign_noImg(DetailedDesign detailedDesign);
    @Select("select * from DetailedDesign where id = #{id}")
    DetailedDesign getDetailedDesignObjById(@Param("id") long id);
    @Delete("delete from DetailedDesign where id = #{id}")
    boolean removeDetDesign(@Param("id") long id);

    //Defect
    @Insert("insert into Defect value(id,#{title},#{defectdescription},#{severity},current_timestamp,#{solutiondescription},#{note},#{operatorid})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    boolean addDefect(Defect defect);
    @Update("update Defect set solutiondescription = #{solutiondescription} where id = #{id}")
    boolean updateSolutionDescriptionOfDefect(Defect defect);
    @Select("select * from Defect where id = #{id}")
    Defect getDefectById(@Param("id") long id);
    @Update("update Defect set note = #{note} where id = #{id}")
    boolean updateNoteOfDefect(Defect defect);
    @Delete("delete from Requirement where id = #{id}")
    boolean removeDefect(@Param("id") long id);
    @Update("update Defect set title = #{title},defectdescription = #{defectdescription},severity = #{severity} where id = #{id}")
    boolean updateDefect(Defect defect);

    //Actions
    @Insert("insert into Message_Mission value (id,#{subject_id},#{object_id},#{message_mission},current_timestamp,#{proid},#{viewflag})")
    boolean addMessageRecord(Message_mission message_mission);
    @Select("select * from Message_Mission where (subject_id = #{userId} or object_id = #{userId}) and proid = #{proId} order by publishtime desc")
    List<Message_mission> getMessageByUser(@Param("userId") long userId,@Param("proId") long proId);
    @Select("select count(*) from Message_Mission where object_id = #{userId} and proid = #{proId} and viewflag = 'false' ")
    int getCountOfMessageNotReading(@Param("userId") long userId,@Param("proId") long proId);
    @Update("update Message_Mission set viewflag = 'true' where object_id = #{userId} and proid = #{proId}")
    boolean clearNotReadingFlag(@Param("userId") long userId,@Param("proId") long proId);
}
