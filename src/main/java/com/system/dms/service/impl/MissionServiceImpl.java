package com.system.dms.service.impl;

import com.system.dms.dao.MissionMapper;
import com.system.dms.entity.*;
import com.system.dms.service.MissionService;
import com.system.dms.staticEnum.MissionMuduleEnum;
import com.system.dms.staticEnum.MissionStatusEnum;
import com.system.dms.staticEnum.StatementEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service("MissionService")
public class MissionServiceImpl implements MissionService{
    @Autowired
    MissionMapper missionMapper;



    @Override
    public long addMissionMudule(MissionModule missionModule) {
        if(missionMapper.addMissionMudule(missionModule)){
            return missionModule.getId();
        }
        else
            return Long.valueOf(-1);
    }

    @Override
    public boolean removeMissionModule(long id) {
        return missionMapper.removeMissionModule(id);
    }

    @Override
    public MissionModule getMissionModuleById(long id) {
        return missionMapper.getMissionModuleObjById(id);
    }

    @Override
    public MissionModule getMissionModuleByCategoryAndIndex(long category, long moduleIndex) {
        return missionMapper.getMissionModuleByCategoryAndIndex(category,moduleIndex);
    }

    @Override
    public MissionModule getInitialModulesOfProjectBuilder(long uid,long index, long category,long statement) {
        return missionMapper.getInitialModulesOfProjectBuilder(uid,index,category,statement);
    }

    @Override
    public MissionModule getPublishedModulesOfProjectBuilder(long uid, long index, long category,long statement) {
        return missionMapper.getPublishedModulesOfProjectBuilder(uid,index,category,statement);
    }

    @Override
    public List<ProjectMember> searchMemberByPIDAndRoleType(long proId, long roleType) {
        return missionMapper.searchMemberByPIDAndRoleType(proId,roleType);
    }

    @Override
    public List<Mission> getMissionListByOwn(long proId,long receiverId, long missionStatus) {
        return missionMapper.getMissionListByOwn(proId,receiverId,missionStatus);
    }

    @Override
    public List<Mission> getMissionListByModuleList(List<MissionModule> missionModuleList) {
        List<Mission> missionList = new LinkedList<Mission>();
        if(missionModuleList!=null&&missionModuleList.size()!=0){
            for(MissionModule m:missionModuleList){
                missionList.add(missionMapper.getMissionObjByModuleId(m.getId()));
            }
        }
        return missionList;
    }

    @Override
    public Mission getMissionObjByModuleId(long moduleId) {
        Mission mission = missionMapper.getMissionObjByModuleId(moduleId);
        if(mission==null){
            mission = new Mission();
            mission.setMissionstatus((long) -1);
        }
        return mission;
    }

    @Override
    public List<Mission> getMissionListOfPublisherByStatus(long publisherId, long status, long proId) {
        return missionMapper.getMissionListOfPublisherByStatus(publisherId,status,proId);
    }

    @Override
    public List<Mission> getMissionListOfReceiverByStatus(long receiverId, long status, long proId) {
        return missionMapper.getMissionListOfReceiverByStatus(receiverId,status,proId);
    }

    @Override
    public boolean changeMissionStatus(long moduleId, long status) {
        return missionMapper.changeMissionStatus(moduleId,status);
    }

    @Override
    public boolean isMissionExistingByMissionModule(long moduleId) {
        if(missionMapper.isMissionExistingByMissionModule(moduleId)==1)
            return true;
        else
            return false;
    }

    @Override
    public String publishTestMission(TestList testList) {
        //首先把MissionModule的statement改为发布
        if(missionMapper.setStatementToNewStatement(testList.getMmid(),StatementEnum.Published.ordinal())){
            //然后修改模块对应任务状态为：待测试
            if(missionMapper.changeMissionStatus(testList.getMmid(), MissionStatusEnum.testing.ordinal())){
                //然后上传测试任务
                if(missionMapper.publishTestList(testList)){
                    return "任务发布成功";
                }
                else {
                    return "I/O Exception";
                }
            }
            else
                return "fail to mission status change";
        }
        else {
            return "MissionModule I/O Error";
        }
    }

    @Override
    public TestList getTestMissionByMMId(long mmid) {
        return missionMapper.getTestMissionByMMId(mmid);
    }

    @Override
    public List<TestList> getTestListOfReceiverByStatus(long receiverId, long status, long proId) {
        return missionMapper.getTestListOfReceiverByStatus(receiverId,status,proId);
    }

    @Override
    public List<MissionModule> getMissionModuleListByTestList(List<TestList> testLists) {
        List<MissionModule> missionModuleList = new LinkedList<MissionModule>();
        for(TestList testList:testLists){
            MissionModule missionModule = missionMapper.getMissionModuleObjById(testList.getMmid());
            missionModuleList.add(missionModule);
        }

        return missionModuleList;
    }

    @Override
    public boolean changeTestStatus(long moduleId, long status) {
        return missionMapper.changeTestStatus(moduleId,status);
    }

    @Override
    public boolean deleteTestMission(long moduleId) {
        return missionMapper.deleteTestMission(moduleId);
    }

    @Override
    public String publishAssignMission(AssignMission assignMission) {
        if(missionMapper.publishAssignMission(assignMission)){
            return "任务指派成功";
        }
        else
            return "I/O Exception";
    }

    @Override
    public List<AssignMission> getAssignMissionListOfReceiverByStatus(long receiverId, long status, long proId) {
        return missionMapper.getAssignMissionListOfReceiverByStatus(receiverId,status,proId);
    }

    @Override
    public AssignMission getAssignMissionObjByModuleId(long moduleId) {
        return missionMapper.getAssignMissionObjByModuleId(moduleId);
    }

    @Override
    public boolean changeAssignMissionStatus(long moduleId, long status) {
        return missionMapper.changeAssignMissionStatus(moduleId,status);
    }

    @Override
    public List<AssignMission> getAssignMissionListOfPublisherByStatus(long publisherId, long status, long proId) {
        return missionMapper.getAssignMissionListOfPublisherByStatus(publisherId,status,proId);
    }

    @Override
    public boolean updateProject(Project project) {
        return missionMapper.updateProject(project);
    }

    @Override
    public List<MissionModule> getMissionModuleListByMissionList(List<Mission> missionList) {
        List<MissionModule> missionModuleList = new LinkedList<MissionModule>();
        for(Mission mission:missionList){
            MissionModule missionModule = missionMapper.getMissionModuleObjById(mission.getMmid());
            missionModuleList.add(missionModule);
        }

        return missionModuleList;
    }

    @Override
    public List<MissionModule> getTypeModulesOfProjectPartner(long pid , long uid, long category, long statement) {
        if(statement == StatementEnum.UnPublished.ordinal()){
            return missionMapper.getInitialModulesOfProjectPartner(pid,uid,category,statement);
        }
        else if (statement == StatementEnum.Published.ordinal()){
            return missionMapper.getPublishedModulesOfProjectPartner(pid,uid,category,statement);
        }
        else {
            return  null;
        }
    }

    @Override
    public boolean isTypeDefectsOfMissionModuleExisting(long missionModuleId,long statement) {
        if(missionMapper.isTypeDefectsOfMissionModuleExisting(missionModuleId,statement) == 0)
            return false;
        else
            return true;
    }

    @Override
    public boolean isAllDefectOfMissionModuleRepaired(long missionModuleId) {
        //首先获取该模块下的所有缺陷模块
        List<MissionModule> defectMissionModuleList = missionMapper.getAllDefectMissionModuleOfMissionModule(missionModuleId);
        //再获取对应的任务列表的状态是否存在未修复的任务
        for(MissionModule missionModule:defectMissionModuleList){
            Mission mission = missionMapper.getMissionObjByModuleId(missionModule.getId());
            if(mission.getMissionstatus()!=MissionStatusEnum.finished.ordinal()){
                return false;
            }
        }
        return true;
    }

    @Override
    public List<MissionModule> getMissionModuleListByAssignMissionList(List<AssignMission> assignMissionList) {
        List<MissionModule> missionModuleList = new LinkedList<MissionModule>();
        for(AssignMission assignMission:assignMissionList){
            MissionModule missionModule = missionMapper.getMissionModuleObjById(assignMission.getMmid());
            missionModuleList.add(missionModule);
        }

        return missionModuleList;
    }

    @Override
    public List<MissionModule> getAllUnPublishedSonModule(long moduleId) {
        return missionMapper.getAllStatementSonModule(moduleId,StatementEnum.UnPublished.ordinal());
    }

    @Override
    public List<MissionModule> getAllUnFinishedSonModule(long moduleId) {
        //先获取所有已发布的子模型
        List<MissionModule> missionModuleList_published = missionMapper.getAllStatementSonModule(moduleId,StatementEnum.Published.ordinal());
        System.out.println(missionModuleList_published.size());
        //然后判断是否是完成状态
        List<MissionModule> unfinishedSonModuleList = new LinkedList<MissionModule>();
        for(MissionModule missionModule:missionModuleList_published){
            Mission mission = missionMapper.getMissionObjByModuleId(missionModule.getId());
            if(mission.getMissionstatus()!=MissionStatusEnum.finished.ordinal()){
                unfinishedSonModuleList.add(missionModule);
            }
        }

        System.out.println(unfinishedSonModuleList.size());

        return unfinishedSonModuleList;
    }

    @Override
    public List<MissionModule> getAllSonTreeNode(MissionModule missionModule) {
        //建立存储队列
        List<MissionModule> missionModuleList = new LinkedList<MissionModule>();
        //将当前节点先存入队首
        missionModuleList.add(missionModule);
        //一个while循环遍历节点的子树
        int i = 0;
        int length = missionModuleList.size();

        while(i<length){
            //查找当前节点的所有子节点，入队
            //如果是缺陷，则跳过，因为缺陷是究极叶子节点
            if(missionModuleList.get(i).getModuleindex() != MissionMuduleEnum.Defect.ordinal()){
                List<MissionModule> sonList = missionMapper.getALlSonNode(missionModuleList.get(i));
                missionModuleList.addAll(sonList);
            }
            length = missionModuleList.size();
            i++;
        }

//        for(MissionModule missionModuleTest:missionModuleList){
//            System.out.println(missionModuleTest.toString());
//        }

        return missionModuleList;
    }

    @Override
    public boolean updateMissionModuleName(String mimoName, long category, long moduleIndex) {
        return missionMapper.updateMissionModuleName(mimoName,category,moduleIndex);
    }

    @Override
    public String publishMissionModule(Mission mission) {
        //首先把MissionModule的statement改为发布
        if(missionMapper.setStatementToNewStatement(mission.getMmid(),StatementEnum.Published.ordinal())){
            //然后上传任务
            if(missionMapper.publishMission(mission)){
                return "任务发布成功";
            }
            else {
                return "I/O Exception";
            }
        }
        else {
            return "MissionModule I/O Error";
        }
    }

    @Override
    public Long addReq(Requirement requirement) {
        if(missionMapper.addReq(requirement)){
            return requirement.getId();
        }
        else
            return Long.valueOf(-1);
    }

    @Override
    public Long addDetDesgin(DetailedDesign detailedDesign) {
        if(missionMapper.addDetDesign(detailedDesign)){
            return detailedDesign.getId();
        }
        else
            return Long.valueOf(-1);
    }

    @Override
    public boolean updateDetDesign(DetailedDesign detailedDesign) {
        return missionMapper.updateDetDesign(detailedDesign);
    }

    @Override
    public boolean updateDetDesign_noImg(DetailedDesign detailedDesig) {
        return missionMapper.updateDetDesign_noImg(detailedDesig);
    }

    @Override
    public long addDefect(Defect defect) {
        if(missionMapper.addDefect(defect)){
            return defect.getId();
        }
        else
            return Long.valueOf(-1);
    }

    @Override
    public Defect getDefectById(long id) {
        return missionMapper.getDefectById(id);
    }

    @Override
    public boolean updateSolutionDescriptionOfDefect(Defect defect) {
        return missionMapper.updateSolutionDescriptionOfDefect(defect);
    }

    @Override
    public boolean updateNoteOfDefect(Defect defect) {
        return missionMapper.updateNoteOfDefect(defect);
    }

    @Override
    public List<Defect> getDefectListByModuleId(List<MissionModule> defectMissionModuleList) {
        List<Defect> defectList = new LinkedList<Defect>();
        for(MissionModule missionModule:defectMissionModuleList){
            defectList.add(missionMapper.getDefectById(missionModule.getModuleindex()));
        }
        return defectList;
    }

    @Override
    public List<MissionModule> getAllDefectMissionModuleOfMissionModule(long moduleId) {
        return missionMapper.getAllDefectMissionModuleOfMissionModule(moduleId);
    }

    @Override
    public boolean updateDefect(Defect defect) {
        return missionMapper.updateDefect(defect);
    }

    @Override
    public boolean deleteReq(Long reqId) {
        return false;
    }

    @Override
    public boolean updateReq(Requirement requirement) {
        return missionMapper.updateReq(requirement);
    }

    @Override
    public Requirement getReq(Long reqId) {
        return missionMapper.getReq(reqId);
    }

    @Override
    public Long addTolDesign(TotalDesign totalDesign) {
        if(missionMapper.addTolDesign(totalDesign)){
            return totalDesign.getId();
        }
        else
            return Long.valueOf(-1);
    }

    @Override
    public boolean updateTolDesign(TotalDesign totalDesign) {
        return missionMapper.updateTolDesign(totalDesign);
    }

    @Override
    public boolean updateTolDesign_noImg(TotalDesign totalDesign) {
        return missionMapper.updateTolDesign_noImg(totalDesign);
    }

    @Override
    public boolean removeModuleByCateAndId(long category, long id) {
        //根据类型调用不同的DAO
        if(category == MissionMuduleEnum.Requirement.ordinal()){
            return missionMapper.removeReq(id);
        }
        else {
            return false;
        }
    }

    @Override
    public Object getDetailsOfMissionModule(MissionModule missionModule) {
        //进行类型判断
        long type = missionModule.getCategory();
        long objId = missionModule.getModuleindex();
        if(type == MissionMuduleEnum.Project.ordinal()){
            return missionMapper.getProjectById(objId);
        }
        else if(type == MissionMuduleEnum.Requirement.ordinal()){
            return missionMapper.getReq(objId);
        }
        else if(type == MissionMuduleEnum.TotalDesign.ordinal()){
            return missionMapper.getTotalDesignObjById(objId);
        }
        else if(type == MissionMuduleEnum.DetailedDesign.ordinal()){
            return missionMapper.getDetailedDesignObjById(objId);
        }
        else if(type == MissionMuduleEnum.Defect.ordinal()){
            return missionMapper.getDefectById(objId);
        }
        else {
            return null;
        }
    }

    @Override
    public boolean clearTrack(List<MissionModule> missionModuleList) {
        for(MissionModule missionModule:missionModuleList){
            delTrack(missionModule);
        }
        return true;
    }

    @Override
    public boolean addMessageRecord(Message_mission message_mission) {
        return missionMapper.addMessageRecord(message_mission);
    }

    @Override
    public List<Message_mission> getMessageByUser(long userId,long proId) {
        return missionMapper.getMessageByUser(userId,proId);
    }

    @Override
    public List<Integer> getCountOfMessageNotReading(long userId, List<Project> projectList) {
        List<Integer> counts = new ArrayList<Integer>();
        for(Project project:projectList){
            int result = missionMapper.getCountOfMessageNotReading(userId,project.getId());
            counts.add(result);
        }
        return counts;
    }

    @Override
    public boolean clearNotReadingFlag(long userId, long proId) {
        return missionMapper.clearNotReadingFlag(userId,proId);
    }


    //method

    private void delTrack(MissionModule missionModule){
        //先删除任务
        long id = missionModule.getId();
        long real_id = missionModule.getModuleindex();
        //TestList_del
        if(missionMapper.isTestMissionExistingByMissionModule(id)!=0){
            missionMapper.deleteTestMission(id);
        }
        //AssignMission_del
        if(missionMapper.isAssignMissionExistingByMissionModule(id)!=0){
            missionMapper.deleteAssignMission(id);
        }
        //Mission_del
        if(missionMapper.isMissionExistingByMissionModule(id)!=0){
            missionMapper.deleteMission(id);
        }

        //然后删除真实模块
        long category = missionModule.getCategory();

        if(category == MissionMuduleEnum.Project.ordinal()){
            missionMapper.removeProject(real_id);
        }
        else if(category == MissionMuduleEnum.Requirement.ordinal()){
            missionMapper.removeReq(real_id);
        }
        else if(category == MissionMuduleEnum.TotalDesign.ordinal()){
            missionMapper.removeTolDesign(real_id);
        }
        else if(category == MissionMuduleEnum.DetailedDesign.ordinal()){
            missionMapper.removeDetDesign(real_id);
        }
        else if(category == MissionMuduleEnum.Defect.ordinal()){
            missionMapper.removeDefect(real_id);
        }

        //最后删除抽象模块
        missionMapper.removeMissionModule(id);
    }

}
