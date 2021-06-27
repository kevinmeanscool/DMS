package com.system.dms.service;

import com.system.dms.entity.*;
import org.aspectj.weaver.ast.Test;

import java.util.List;

public interface MissionService {
    //MissionModule
    long addMissionMudule(MissionModule missionModule);
    boolean removeMissionModule(long id);
    MissionModule getMissionModuleById(long id);
    MissionModule getMissionModuleByCategoryAndIndex(long category,long moduleIndex);
    String publishMissionModule(Mission mission);
    MissionModule getInitialModulesOfProjectBuilder(long uid,long index,long category,long statement);
    MissionModule getPublishedModulesOfProjectBuilder(long uid,long index,long category,long statement);
    List<ProjectMember> searchMemberByPIDAndRoleType(long proId, long roleType);
    List<MissionModule> getMissionModuleListByMissionList(List<Mission> missionList);
    List<MissionModule> getTypeModulesOfProjectPartner(long pid,long uid,long category,long statement);
    boolean isTypeDefectsOfMissionModuleExisting(long missionModuleId,long statement);
    boolean isAllDefectOfMissionModuleRepaired(long missionModuleId);
    List<MissionModule> getMissionModuleListByAssignMissionList(List<AssignMission> assignMissionList);
    List<MissionModule> getAllUnPublishedSonModule(long moduleId);
    List<MissionModule> getAllUnFinishedSonModule(long moduleId);
    List<MissionModule> getAllSonTreeNode(MissionModule missionModule);
    boolean updateMissionModuleName(String mimoName,long category,long moduleIndex);


    //Mission
    List<Mission> getMissionListByOwn(long proId,long receiverId,long missionStatus);
    List<Mission> getMissionListByModuleList(List<MissionModule> missionModuleList);
    Mission getMissionObjByModuleId(long moduleId);
    List<Mission> getMissionListOfPublisherByStatus(long publisherId,long status,long proId);
    List<Mission> getMissionListOfReceiverByStatus(long receiverId,long status,long proId);
    boolean changeMissionStatus(long moduleId,long status);
    boolean isMissionExistingByMissionModule(long moduleId);

    //TestList
    String publishTestMission(TestList testList);
    TestList getTestMissionByMMId(long mmid);
    List<TestList> getTestListOfReceiverByStatus(long receiverId,long status,long proId);
    List<MissionModule> getMissionModuleListByTestList(List<TestList> testLists);
    boolean changeTestStatus(long moduleId,long status);
    boolean deleteTestMission(long moduleId);

    //AssignMission
    String publishAssignMission(AssignMission assignMission);
    List<AssignMission> getAssignMissionListOfReceiverByStatus(long receiverId,long status,long proId);
    AssignMission getAssignMissionObjByModuleId(long moduleId);
    boolean changeAssignMissionStatus(long moduleId,long status);
    List<AssignMission> getAssignMissionListOfPublisherByStatus(long publisherId,long status,long proId);

    //Project
    boolean updateProject(Project project);

    //Requirement
    Long addReq(Requirement requirement);
    boolean deleteReq(Long reqId);
    boolean updateReq(Requirement requirement);
    Requirement getReq(Long reqId);

    //TotalDesign
    Long addTolDesign(TotalDesign totalDesign);
    boolean updateTolDesign(TotalDesign totalDesign);
    boolean updateTolDesign_noImg(TotalDesign totalDesign);

    //DetailedDesign
    Long addDetDesgin(DetailedDesign detailedDesign);
    boolean updateDetDesign(DetailedDesign detailedDesign);
    boolean updateDetDesign_noImg(DetailedDesign detailedDesig);

    //Defect
    long addDefect(Defect defect);
    Defect getDefectById(long id);
    boolean updateSolutionDescriptionOfDefect(Defect defect);
    boolean updateNoteOfDefect(Defect defect);
    List<Defect> getDefectListByModuleId(List<MissionModule> defectMissionModuleList);
    List<MissionModule> getAllDefectMissionModuleOfMissionModule(long moduleId);
    boolean updateDefect(Defect defect);

    //AllOperation
    boolean removeModuleByCateAndId(long category,long id);
    Object getDetailsOfMissionModule(MissionModule missionModule);
    boolean clearTrack(List<MissionModule> missionModuleList);

    //Message
    boolean addMessageRecord(Message_mission message_mission);
    List<Message_mission> getMessageByUser(long userId,long proId);
    List<Integer> getCountOfMessageNotReading(long userId,List<Project> projectList);
    boolean clearNotReadingFlag(long userId,long proId);

}
