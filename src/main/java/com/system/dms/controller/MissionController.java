package com.system.dms.controller;

import com.system.dms.dao.MissionMapper;
import com.system.dms.entity.*;
import com.system.dms.service.MissionService;
import com.system.dms.service.ProjectService;
import com.system.dms.service.UserService;
import com.system.dms.staticEnum.MemberRoleEnum;
import com.system.dms.staticEnum.MissionMuduleEnum;
import com.system.dms.staticEnum.MissionStatusEnum;
import com.system.dms.staticEnum.StatementEnum;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/mission")
public class MissionController {
    @Autowired
    private MissionService missionService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;

    @ResponseBody
    @RequestMapping(value = "/searchMember")
    public Object searchProjectMemberByRoleType(@RequestParam("proId") long proId,@RequestParam("memberType") String memberType){
        List<ProjectMember> userIdList = missionService.searchMemberByPIDAndRoleType(proId, getIndexFromMemberRoleEnum(memberType));
        //没有相关人员
        if(userIdList.size()==0){
            return "badSearch";
        }
        else {
            //根据userIdList找对应用户的信息
            List<User> userList = new LinkedList<User>();
            for(ProjectMember projectMember : userIdList){
                //获取user对象
                User user = userService.getUserInfoById(projectMember.getUserid());
                //更换昵称为组内昵称
                user.setNickname(projectMember.getMembername());
                userList.add(user);
            }

            //将List转换为JSON对象
            JSONArray json = JSONArray.fromObject(userList);

            //返回到前端
            return json;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/publishMission")
    public Object publishMission(@RequestParam("missionModuleId") long missionModuleId, @RequestParam("receiverId") long receiverId , @RequestParam("proId") long proId, HttpServletRequest request){
        //获取当前操作对象
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userInfo");

        //获取当前任务模块信息
        MissionModule missionModule = missionService.getMissionModuleById(missionModuleId);

        Mission missionInfo = missionService.getMissionObjByModuleId(missionModuleId);

        String result = null;
        if(missionInfo==null){//普通阶段任务
            //创建任务
            Mission mission = new Mission();
            mission.setMmid(missionModuleId);
            mission.setMissionstatus(Long.valueOf(MissionStatusEnum.Unfinished.ordinal()));
            mission.setReceiverid(receiverId);
            mission.setPublisherid(user.getId());
            mission.setProid(proId);

            //发布任务
            result = missionService.publishMissionModule(mission);

            //记录消息
            addActionRecord(user.getId(),receiverId,missionModule,"线性模拟任务",proId);
        }
        else {
            //判断任务类型，如果是测试任务，就发布再TestList，避免1：1冲突

            if(missionInfo.getMissionstatus()==MissionStatusEnum.edited.ordinal()){
                //创建测试任务
                TestList testList = new TestList();
                testList.setMmid(missionModuleId);
                testList.setMissionstatus(Long.valueOf(MissionStatusEnum.testing.ordinal()));
                testList.setReceiverid(receiverId);
                testList.setPublisherid(user.getId());
                testList.setProid(proId);

                //发布测试任务
                result = missionService.publishTestMission(testList);
                //记录消息
                addActionRecord(user.getId(),receiverId,missionModule,"测试任务",proId);
            }
            else {
                //创建任务
                Mission mission = new Mission();
                mission.setMmid(missionModuleId);
                mission.setMissionstatus(Long.valueOf(MissionStatusEnum.Unfinished.ordinal()));
                mission.setReceiverid(receiverId);
                mission.setPublisherid(user.getId());
                mission.setProid(proId);

                //发布任务
                result = missionService.publishMissionModule(mission);

                //记录消息
                addActionRecord(user.getId(),receiverId,missionModule,"线性模拟任务",proId);
            }
        }

        //填充结果
        List<Object> resultList = new ArrayList<Object>();
        resultList.add(missionModule);
        if(missionInfo.getMissionstatus()==MissionStatusEnum.edited.ordinal()){
            resultList.add(missionService.getTestMissionByMMId(missionModuleId));
        }
        else {
            resultList.add(missionService.getMissionObjByModuleId(missionModuleId));
        }
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setMessage(result);
        ajaxResult.setResultObj(resultList);
        JSONObject json = JSONObject.fromObject(ajaxResult);

        return json;
    }

    @ResponseBody
    @RequestMapping(value = "/buildReq")
    public Object buildReq(@RequestParam("ObjectReq") String ObjectReq,@RequestParam("FatherIndex") String fatherIndex,@RequestParam("proId") String proId,HttpServletRequest request){
        //生成返回对象
        AjaxResult ajaxResult = new AjaxResult();
        //获取当前操作对象
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userInfo");
        //先增加需求对象
        JSONObject reqObject = JSONObject.fromObject(ObjectReq);
        Requirement requirement = (Requirement) reqObject.toBean(reqObject,Requirement.class);
        requirement.setOperatorid(user.getId());//操作对象id
        requirement.setStatement(Long.valueOf(StatementEnum.Initial.ordinal()));


        long reqAddResult = missionService.addReq(requirement);
        if(reqAddResult != -1){
            //后增加任务模块对象
            MissionModule missionModule = new MissionModule();
            missionModule.setMimoname(requirement.getReqname());
            missionModule.setCategory(Long.valueOf(MissionMuduleEnum.Requirement.ordinal()));
            missionModule.setModuleindex(reqAddResult);
            missionModule.setStatement(Long.valueOf(StatementEnum.UnPublished.ordinal()));
            missionModule.setFatherindex(Long.valueOf(fatherIndex));
            missionModule.setOperatorid(user.getId());
            missionModule.setProid(Long.valueOf(proId));
            long modAddResult = missionService.addMissionMudule(missionModule);
            if(modAddResult!=-1){
                MissionModule missionModuleInfo = missionService.getMissionModuleById(modAddResult);
                ajaxResult.setResultObj(missionModuleInfo);
                ajaxResult.setMessage("操作成功");
                JSONObject result = JSONObject.fromObject(ajaxResult);
                return result;
            }
            else
                return "fail to add ReqMod";
        }
        else
            return "fail to add Req";
    }

    @ResponseBody
    @RequestMapping(value = "/buildTolDesign")
    public Object buildTolDesign(@RequestParam("tolName") String tolName,@RequestParam("processLogic") String processLogic,@RequestParam("fatherIndex") String fatherIndex,@RequestParam("proId") String proId,@RequestParam("softwareStructure") MultipartFile softwareStructure, HttpServletRequest request){
        //生成返回对象
        AjaxResult ajaxResult = new AjaxResult();

        //获取当前操作对象
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userInfo");

        //生成总体设计对象

        TotalDesign totalDesign = new TotalDesign();
        totalDesign.setTolname(tolName);
        totalDesign.setProcesslogic(processLogic);
        totalDesign.setSoftwarestructure("");
        totalDesign.setStatement((long) StatementEnum.Initial.ordinal());
        totalDesign.setOperatorid(user.getId());


        long tolDesignId = missionService.addTolDesign(totalDesign);
        totalDesign.setId(tolDesignId);

        //上传图片
        String path = uploadImg(softwareStructure,request,"images/TotalDesign",tolDesignId);
        if(!(path.equals("error"))){
            //更新图片路径
            totalDesign.setSoftwarestructure("/images/TotalDesign/"+path);
            if(missionService.updateTolDesign(totalDesign)){
                //创建模块
                MissionModule missionModule = new MissionModule();
                missionModule.setMimoname(tolName);
                missionModule.setCategory((long) MissionMuduleEnum.TotalDesign.ordinal());
                missionModule.setModuleindex(tolDesignId);
                missionModule.setStatement((long) StatementEnum.UnPublished.ordinal());
                missionModule.setFatherindex(Long.valueOf(fatherIndex));
                missionModule.setOperatorid(user.getId());
                missionModule.setProid(Long.valueOf(proId));

                long missionModuleId = missionService.addMissionMudule(missionModule);

                if(missionModuleId!= -1){
                    MissionModule missionModuleInfo = missionService.getMissionModuleById(missionModuleId);
                    ajaxResult.setResultObj(missionModuleInfo);
                    ajaxResult.setMessage("创建成功");

                    JSONObject result = JSONObject.fromObject(ajaxResult);

                    return result;
                }
                else {
                    return "fail to create mis";
                }
            }
            else {
                return "fail to update path";
            }
        }
        else {
            return "upload exception";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/buildTolDesign_noImg")
    public Object buildTolDesign(@RequestParam("tolName") String tolName,@RequestParam("processLogic") String processLogic,@RequestParam("fatherIndex") String fatherIndex,@RequestParam("proId") String proId,HttpServletRequest request){
        //生成返回对象
        AjaxResult ajaxResult = new AjaxResult();

        //获取当前操作对象
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userInfo");

        //生成总体设计对象

        TotalDesign totalDesign = new TotalDesign();
        totalDesign.setTolname(tolName);
        totalDesign.setProcesslogic(processLogic);
        totalDesign.setSoftwarestructure("");
        totalDesign.setStatement((long) StatementEnum.Initial.ordinal());
        totalDesign.setOperatorid(user.getId());


        long tolDesignId = missionService.addTolDesign(totalDesign);
        totalDesign.setId(tolDesignId);

        //创建模块
        MissionModule missionModule = new MissionModule();
        missionModule.setMimoname(tolName);
        missionModule.setCategory((long) MissionMuduleEnum.TotalDesign.ordinal());
        missionModule.setModuleindex(tolDesignId);
        missionModule.setStatement((long) StatementEnum.UnPublished.ordinal());
        missionModule.setFatherindex(Long.valueOf(fatherIndex));
        missionModule.setOperatorid(user.getId());
        missionModule.setProid(Long.valueOf(proId));

        long missionModuleId = missionService.addMissionMudule(missionModule);

        if(missionModuleId!= -1){
            MissionModule missionModuleInfo = missionService.getMissionModuleById(missionModuleId);
            ajaxResult.setResultObj(missionModuleInfo);
            ajaxResult.setMessage("创建成功");

            JSONObject result = JSONObject.fromObject(ajaxResult);

            return result;
        }
        else {
            return "fail to create mis";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/buildDetDesign")
    public Object buildDetDesign(@RequestParam("detName")String detName,@RequestParam("arithmetic") String arithmetic,@RequestParam("limitingCondition")String limitingCondition,@RequestParam("input")String input,@RequestParam("output")String output,@RequestParam("processLogic") MultipartFile processLogic,@RequestParam("fatherIndex") String fatherIndex,@RequestParam("proId") String proId,HttpServletRequest request){
        //返回对象
        AjaxResult ajaxResult = new AjaxResult();

        //获取当前操作对象
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userInfo");

        //生成详细设计对象
        DetailedDesign detailedDesign = new DetailedDesign();
        detailedDesign.setDetname(detName);
        detailedDesign.setProcesslogic("");
        detailedDesign.setArithmetic(arithmetic);
        detailedDesign.setLimitingcondition(limitingCondition);;
        detailedDesign.setInput(input);
        detailedDesign.setOutput(output);
        detailedDesign.setStatement((long)StatementEnum.Initial.ordinal());
        detailedDesign.setOperatorid(user.getId());

        long detDesignId = missionService.addDetDesgin(detailedDesign);

        if(detDesignId!=-1){
            detailedDesign.setId(detDesignId);
            //上传图片
            String path = uploadImg(processLogic,request,"images/DetailedDesign",detDesignId);
            if(!(path.equals("error"))){
                //更新图片路径
                detailedDesign.setProcesslogic("/images/DetailedDesign/"+path);
                //更新记录图片路径
                if(missionService.updateDetDesign(detailedDesign)){
                    //建立模块
                    MissionModule missionModule = new MissionModule();
                    missionModule.setMimoname(detName);
                    missionModule.setCategory((long) MissionMuduleEnum.DetailedDesign.ordinal());
                    missionModule.setModuleindex(detDesignId);
                    missionModule.setStatement((long) StatementEnum.UnPublished.ordinal());
                    missionModule.setFatherindex(Long.valueOf(fatherIndex));
                    missionModule.setOperatorid(user.getId());
                    missionModule.setProid(Long.valueOf(proId));

                    long missionModuleId = missionService.addMissionMudule(missionModule);

                    if(missionModuleId!= -1){
                        MissionModule missionModuleInfo = missionService.getMissionModuleById(missionModuleId);
                        ajaxResult.setResultObj(missionModuleInfo);
                        ajaxResult.setMessage("创建成功");

                        JSONObject result = JSONObject.fromObject(ajaxResult);

                        return result;
                    }
                    else {
                        return "fail to create mis";
                    }

                }
                else
                    return "fail to update p url";
            }
            else
                return "fail to upload p";
        }
        else {
            return "fail to add det";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/buildDetDesign_noImg")
    public Object buildDetDesign(@RequestParam("detName")String detName,@RequestParam("arithmetic") String arithmetic,@RequestParam("limitingCondition")String limitingCondition,@RequestParam("input")String input,@RequestParam("output")String output,@RequestParam("fatherIndex") String fatherIndex,@RequestParam("proId") String proId,HttpServletRequest request){
        //返回对象
        AjaxResult ajaxResult = new AjaxResult();

        //获取当前操作对象
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userInfo");

        //生成详细设计对象
        DetailedDesign detailedDesign = new DetailedDesign();
        detailedDesign.setDetname(detName);
        detailedDesign.setProcesslogic("");
        detailedDesign.setArithmetic(arithmetic);
        detailedDesign.setLimitingcondition(limitingCondition);;
        detailedDesign.setInput(input);
        detailedDesign.setOutput(output);
        detailedDesign.setStatement((long)StatementEnum.Initial.ordinal());
        detailedDesign.setOperatorid(user.getId());

        long detDesignId = missionService.addDetDesgin(detailedDesign);

        if(detDesignId!=-1){
            detailedDesign.setId(detDesignId);
            //建立模块
            MissionModule missionModule = new MissionModule();
            missionModule.setMimoname(detName);
            missionModule.setCategory((long) MissionMuduleEnum.DetailedDesign.ordinal());
            missionModule.setModuleindex(detDesignId);
            missionModule.setStatement((long) StatementEnum.UnPublished.ordinal());
            missionModule.setFatherindex(Long.valueOf(fatherIndex));
            missionModule.setOperatorid(user.getId());
            missionModule.setProid(Long.valueOf(proId));

            long missionModuleId = missionService.addMissionMudule(missionModule);

            if(missionModuleId!= -1){
                MissionModule missionModuleInfo = missionService.getMissionModuleById(missionModuleId);
                ajaxResult.setResultObj(missionModuleInfo);
                ajaxResult.setMessage("创建成功");

                JSONObject result = JSONObject.fromObject(ajaxResult);

                return result;
            }
            else {
                return "fail to create mis";
            }
        }
        else {
            return "fail to add det";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/removeModule")
    public String removeModule(@RequestParam("missionModuleId") String missionModuleId,@RequestParam("category") String category,@RequestParam("moduleIndex") String moduleIndex){
        //先删除任务模块
        if(missionService.removeMissionModule(Long.valueOf(missionModuleId))){
            //再删除对应类型的记录
            if(missionService.removeModuleByCateAndId(Long.valueOf(category),Long.valueOf(moduleIndex))){
                return "移除成功";
            }
            else
                return "ModuleRemove Exception";
        }
        else
            return "MissionModuleRemove Exception";
    }

    @ResponseBody
    @RequestMapping(value = "/getMissionStatus")
    public String getMissionStatusByModuleId(@RequestParam("missionModuleId") String missionModuleId){
        //先判断模块有没有创建任务
        if(missionService.isMissionExistingByMissionModule(Long.valueOf(missionModuleId))){
            return String.valueOf(missionService.getMissionObjByModuleId(Long.valueOf(missionModuleId)).getMissionstatus());
        }
        else
            return String.valueOf(-1);
    }

    @ResponseBody
    @RequestMapping(value = "/getFatherOfMissionStatus")
    public String getFatherOfMissionStatusByModuleId(@RequestParam("missionModuleId") String missionModuleId){
        //获取MissionModule
        MissionModule missionModule = missionService.getMissionModuleById(Long.valueOf(missionModuleId));
        return String.valueOf(missionService.getMissionObjByModuleId(missionModule.getFatherindex()).getMissionstatus());
    }

    @ResponseBody
    @RequestMapping(value = "/completeModuleEditing")
    public Object completeModuleEditing(@RequestParam("missionModuleId") String missionModuleId){
        //生成结果容器
        AjaxResult ajaxResult = new AjaxResult();
        //改变状态为
        if(missionService.changeMissionStatus(Long.valueOf(missionModuleId),MissionStatusEnum.edited.ordinal())){
            //查询到对应到模块
            MissionModule missionModule = missionService.getMissionModuleById(Long.valueOf(missionModuleId));
            //装进结果容器中
            ajaxResult.setResultObj(missionModule);
            ajaxResult.setMessage("操作成功");

            //封装容器
            JSONObject result = JSONObject.fromObject(ajaxResult);

            return result;
        }
        else
            return "fail to change";
    }

    @ResponseBody
    @RequestMapping(value = "/buildDefect")
    public Object buildDefect(@RequestParam("title") String title,@RequestParam("defectDescription") String defectDescription,@RequestParam("severity") String severity,@RequestParam("fatherIndex") String fatherIndex,@RequestParam("proId") String proId, HttpServletRequest request){
        //创建返回对象
        AjaxResult ajaxResult = new AjaxResult();
        //获取当前操作用户
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userInfo");
        //创建新缺陷对象
        Defect defect = new Defect();
        defect.setTitle(title);
        defect.setDefectdescription(defectDescription);
        defect.setSeverity(Long.valueOf(severity));
        defect.setSolutiondescription("");
        defect.setNote("");
        defect.setOperatorid(user.getId());

        //DAO缺陷对象

        long defectId = missionService.addDefect(defect);

        if(defectId!=-1){
            //后增加任务模块对象
            MissionModule missionModule = new MissionModule();
            missionModule.setMimoname(defect.getTitle());
            missionModule.setCategory(Long.valueOf(MissionMuduleEnum.Defect.ordinal()));
            missionModule.setModuleindex(defectId);
            missionModule.setStatement(Long.valueOf(StatementEnum.UnPublished.ordinal()));
            missionModule.setFatherindex(Long.valueOf(fatherIndex));
            missionModule.setOperatorid(user.getId());
            missionModule.setProid(Long.valueOf(proId));
            long modAddResult = missionService.addMissionMudule(missionModule);
            if(modAddResult!=-1){
                MissionModule missionModuleInfo = missionService.getMissionModuleById(modAddResult);
                ajaxResult.setResultObj(missionModuleInfo);
                ajaxResult.setMessage("操作成功");
                JSONObject result = JSONObject.fromObject(ajaxResult);
                return result;
            }
            else
                return "fail to add dMod";
        }
        else
            return "fail to add d";
    }

    @ResponseBody
    @RequestMapping(value = "/publishMission_QA")
    public Object publishMission_QA(@RequestParam("missionModuleId") long missionModuleId,@RequestParam("proId") long proId, HttpServletRequest request){
        //创建返回对象
        AjaxResult ajaxResult = new AjaxResult();
        //获取当前操作用户
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userInfo");
        //获取缺陷模块
        MissionModule missionModule = missionService.getMissionModuleById(missionModuleId);
        //获取缺陷模块的父模块（详细设计模块）对应的任务
        Mission mission_father = missionService.getMissionObjByModuleId(missionModule.getFatherindex());
        //获取详细设计模块任务的接受者（）
        long receiver_father_Id = mission_father.getReceiverid();
        //获取父模块ID
        long missionModule_father_Id = missionModule.getFatherindex();


        //首先创建新的任务作为缺陷修复任务
        Mission mission = new Mission();
        mission.setMmid(missionModuleId);
        mission.setMissionstatus((long) MissionStatusEnum.Unfinished.ordinal());
        mission.setPublisherid(user.getId());
        mission.setReceiverid(receiver_father_Id);
        mission.setProid(proId);

        String result = missionService.publishMissionModule(mission);
        //记录消息
        addActionRecord(user.getId(),receiver_father_Id,missionModule,"缺陷修复任务",proId);

        if(result.equals("任务发布成功")){
            //获取任务信息
            Mission missionInfo = missionService.getMissionObjByModuleId(missionModuleId);
            //修改父任务状态为待修复
            if(missionService.changeMissionStatus(missionModule_father_Id,MissionStatusEnum.repairing.ordinal())){
                //填充结果
                List<Object> resultList = new ArrayList<Object>();
                resultList.add(missionModule);
                resultList.add(missionInfo);
                ajaxResult.setMessage(result);
                ajaxResult.setResultObj(resultList);
                JSONObject json = JSONObject.fromObject(ajaxResult);
                return json;
            }
            else
                return "fail to change fm status";
        }
        else
            return "fail to publish";
    }

    @ResponseBody
    @RequestMapping(value = "/completeTheTest")
    public Object completeTheTest(@RequestParam("missionModuleId") long missionModuleId,@RequestParam("proId") long proId, HttpServletRequest request){
        //创建返回对象
        AjaxResult ajaxResult = new AjaxResult();
        //获取当前操作用户
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userInfo");

        boolean passFlag = false;
        //判断当前模块是否存在未发布缺陷
        if(missionService.isTypeDefectsOfMissionModuleExisting(missionModuleId,StatementEnum.UnPublished.ordinal())){
            //存在未发布缺陷
            passFlag = false;
        }
        else {
            //不存在未发布缺陷
            //判断是否存在已发布缺陷
            if(missionService.isTypeDefectsOfMissionModuleExisting(missionModuleId,StatementEnum.Published.ordinal())){
                //存在已发布缺陷
                //判断已发布缺陷是否全部修复
                if(missionService.isAllDefectOfMissionModuleRepaired(missionModuleId)){
                    passFlag = true;
                }
                else {
                    passFlag = false;
                }
            }
            else {
                //不存在已发布缺陷
                passFlag = true;
            }
        }

        if(passFlag){
            missionService.changeMissionStatus(missionModuleId,MissionStatusEnum.finished.ordinal());
            missionService.changeTestStatus(missionModuleId,MissionStatusEnum.finished.ordinal());
        }
        else {
            missionService.changeMissionStatus(missionModuleId,MissionStatusEnum.repairing.ordinal());
            missionService.changeTestStatus(missionModuleId,MissionStatusEnum.repairing.ordinal());
        }

        //获取当前MissionModule对象
        MissionModule missionModule = missionService.getMissionModuleById(missionModuleId);
        ajaxResult.setResultObj(missionModule);
        ajaxResult.setMessage(String.valueOf(passFlag));
        JSONObject json = JSONObject.fromObject(ajaxResult);

        return json;
    }

    @ResponseBody
    @RequestMapping(value = "/repairTheDefect")
    public Object repairTheDefect(@RequestParam("missionModuleId") long missionModuleId,@RequestParam("proId") long proId, @RequestParam("solutionDescription") String solutionDescription){
        //创建返回对象
        AjaxResult ajaxResult = new AjaxResult();
        //获取模块对象
        MissionModule missionModule = missionService.getMissionModuleById(missionModuleId);
        //获取缺陷对象
        Defect defect = missionService.getDefectById(missionModule.getModuleindex());
        //填充信息
        defect.setSolutiondescription(solutionDescription);
        //更新缺陷信息(解决方案描述)
        if(missionService.updateSolutionDescriptionOfDefect(defect)){
            //更新任务状态
            if(missionService.changeMissionStatus(missionModuleId,MissionStatusEnum.edited.ordinal())){
                //这里判断一下是不是指派任务下的缺陷修复
                AssignMission assignMission = missionService.getAssignMissionObjByModuleId(missionModuleId);
                if(assignMission!=null){
                    //如果是指派任务
                    //要与主任务状态同步
                    missionService.changeAssignMissionStatus(missionModuleId,MissionStatusEnum.edited.ordinal());
                }
                ajaxResult.setMessage("操作成功,模块已完成编辑，待发布");
                ajaxResult.setResultObj(missionModule);
                JSONObject json = JSONObject.fromObject(ajaxResult);

                return json;
            }
            else {
                return "fail to update m";
            }
        }
        else {
            return "fail to update";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/publishMission_Pro_Defect")
    public Object publishMission_Pro_Defect(@RequestParam("missionModuleId") long missionModuleId,@RequestParam("proId") long proId){
        //创建返回对象
        AjaxResult ajaxResult = new AjaxResult();
        //获取当前模块对象
        MissionModule missionModule = missionService.getMissionModuleById(missionModuleId);
        //获取当前任务对象
        Mission mission = missionService.getMissionObjByModuleId(missionModuleId);

        //修改当前任务状态为：待测试
        if(missionService.changeMissionStatus(missionModuleId,MissionStatusEnum.testing.ordinal())){
            //判断一下是不是指派任务下的缺陷修复
            AssignMission assignMission = missionService.getAssignMissionObjByModuleId(missionModuleId);
            if(assignMission!=null){
                //如果是指派任务
                //要与主任务状态同步
                missionService.changeAssignMissionStatus(missionModuleId,MissionStatusEnum.testing.ordinal());
            }
            //增加测试缺陷的任务与缺陷任务同步
            TestList testList = new TestList();
            testList.setMmid(missionModuleId);
            testList.setMissionstatus((long) MissionStatusEnum.testing.ordinal());
            testList.setPublisherid(mission.getReceiverid());//颠倒
            testList.setReceiverid(mission.getPublisherid());
            testList.setProid(proId);
            //发布测试任务
            if(missionService.publishTestMission(testList).equals("任务发布成功")){
                //记录消息
                addActionRecord(mission.getReceiverid(),mission.getPublisherid(),missionModule,"缺陷测试任务",proId);
                //获取当前任务信息
                Mission missionInfo = missionService.getMissionObjByModuleId(missionModuleId);
                //填充结果
                List<Object> resultList = new ArrayList<Object>();
                resultList.add(missionModule);
                resultList.add(missionInfo);
                ajaxResult.setMessage("反馈成功");
                ajaxResult.setResultObj(resultList);
                JSONObject json = JSONObject.fromObject(ajaxResult);
                return json;
            }
            else {
                return "fail to publish new m";
            }
        }
        else {
            return "fail to change";
        }

    }

    @ResponseBody
    @RequestMapping(value = "/defectPassing")
    /**
     * 该方法为缺陷修复通过，通过后会自动检验父模块是否可以通过，当满足通过条件后，其父模块也将通过
     */
    public Object defectPassing(@RequestParam("missionModuleId") long missionModuleId,@RequestParam("proId") long proId,@RequestParam("note") String note){
        //创建返回对象
        AjaxResult ajaxResult = new AjaxResult();
        //获取模块对象
        MissionModule missionModule = missionService.getMissionModuleById(missionModuleId);
        //获取缺陷对象
        Defect defect = missionService.getDefectById(missionModule.getModuleindex());
        //更新日志
        defect.setNote(defect.getNote()+DateFormat.getDateTimeInstance(2, 2, Locale.CHINESE).format(new java.util.Date())+" "+note+";");
        //测试任务:完成
        //缺陷任务:完成
        if(missionService.updateNoteOfDefect(defect) && missionService.changeTestStatus(missionModuleId,MissionStatusEnum.finished.ordinal()) && missionService.changeMissionStatus(missionModuleId,MissionStatusEnum.finished.ordinal())){
            //这里判断一下是不是指派任务下的缺陷修复
            AssignMission assignMission = missionService.getAssignMissionObjByModuleId(missionModuleId);
            if(assignMission!=null){
                //如果是指派任务
                //要与主任务状态同步
                missionService.changeAssignMissionStatus(missionModuleId,MissionStatusEnum.finished.ordinal());
            }
            ajaxResult.setResultObj(missionModule);
            ajaxResult.setMessage("success");
            JSONObject json = JSONObject.fromObject(ajaxResult);
            return json;
        }
        else
            return "fail";
    }

    @ResponseBody
    @RequestMapping(value = "missionModuleDefectDedecting")
    private Object missionModuleDefectDedecting(@RequestParam("missionModuleId") long missionModuleId){
        //创建返回对象
        AjaxResult ajaxResult = new AjaxResult();

        //当缺陷被修复可以通过后，检查缺陷当父模块（详细设计等）是否存在缺陷，如果不存在，父模块就算完成

        //获取缺陷对象
        MissionModule defectMissionModule = missionService.getMissionModuleById(missionModuleId);
        //获取父模块ID
        long missionModule_Father_Id = defectMissionModule.getFatherindex();

        String result;
        boolean passFlag = false;

        //判断当前模块是否在存在于可操作队列
        if(missionService.getTestMissionByMMId(missionModule_Father_Id).getMissionstatus() == MissionStatusEnum.testing.ordinal()){
            result = "测试未完成，故对应模块保留至可操作队列";
            passFlag = false;
        }
        else {
            //判断当前模块是否存在未发布缺陷
            if(missionService.isTypeDefectsOfMissionModuleExisting(missionModule_Father_Id,StatementEnum.UnPublished.ordinal())){
                //存在未发布缺陷
                result = "对应模版存在未发布缺陷,因此对应模版保留至待修复队列";
                passFlag = false;
            }
            else {
                //不存在未发布缺陷
                //判断是否存在已发布缺陷
                if(missionService.isTypeDefectsOfMissionModuleExisting(missionModule_Father_Id,StatementEnum.Published.ordinal())){
                    //存在已发布缺陷
                    //判断已发布缺陷是否全部修复
                    if(missionService.isAllDefectOfMissionModuleRepaired(missionModule_Father_Id)){
                        result = "对应模版缺陷已全部修复，对应模块移至已完成队列";
                        passFlag = true;
                    }
                    else {
                        result = "对应模块存在缺陷待修复，对应模块保留至待修复队列";
                        passFlag = false;
                    }
                }
                else {
                    //不存在已发布缺陷
                    result = "对应模版无缺陷，对应模块移至已完成队列";
                    passFlag = true;
                }
            }
        }


        if(passFlag){
            missionService.changeMissionStatus(missionModule_Father_Id,MissionStatusEnum.finished.ordinal());
            missionService.changeTestStatus(missionModule_Father_Id,MissionStatusEnum.finished.ordinal());
        }

        //获取父模块对象
        List<Object> resultList = new ArrayList<Object>();
        MissionModule father_MissionModule = missionService.getMissionModuleById(missionModule_Father_Id);
        resultList.add(result);
        resultList.add(father_MissionModule);
        ajaxResult.setResultObj(resultList);
        ajaxResult.setMessage(String.valueOf(passFlag));

        JSONObject json = JSONObject.fromObject(ajaxResult);

        return json;
    }


    @ResponseBody
    @RequestMapping(value = "/defectReject")
    public String defectReject(@RequestParam("missionModuleId") long missionModuleId,@RequestParam("proId") long proId,@RequestParam("note") String note,HttpServletRequest request){
        //获取当前操作对象
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userInfo");
        //获取模块对象
        MissionModule missionModule = missionService.getMissionModuleById(missionModuleId);
        //获取缺陷对象
        Defect defect = missionService.getDefectById(missionModule.getModuleindex());
        //更新日志
        defect.setNote(defect.getNote()+DateFormat.getDateTimeInstance(2, 2, Locale.CHINESE).format(new java.util.Date())+" "+note+";");
        //测试任务:初始
        //缺陷任务:初始
        if(missionService.updateNoteOfDefect(defect) && missionService.changeMissionStatus(missionModuleId,MissionStatusEnum.Unfinished.ordinal()) && missionService.deleteTestMission(missionModuleId)){
            //这里判断一下是不是指派任务下的缺陷修复
            AssignMission assignMission = missionService.getAssignMissionObjByModuleId(missionModuleId);
            if(assignMission!=null){
                //如果是指派任务
                //要与主任务状态同步
                missionService.changeAssignMissionStatus(missionModuleId,MissionStatusEnum.Unfinished.ordinal());
                addActionRecord(user.getId(),assignMission.getReceiverid(),missionModule,"继续修复缺陷任务",proId);
            }
            return "success";
        }
        else
            return "fail";
    }

    @ResponseBody
    @RequestMapping(value = "/enterIntegrateStatus")
    public Object enterIntegrateStatus(@RequestParam("missionModuleId") long missionModuleId){
        //创建返回对象
        AjaxResult ajaxResult = new AjaxResult();
        //修改模块状态为：已编码
        if(missionService.changeMissionStatus(missionModuleId,MissionStatusEnum.edited.ordinal())){
            //获取模块对象
            MissionModule missionModule = missionService.getMissionModuleById(missionModuleId);
            //填充
            ajaxResult.setResultObj(missionModule);
            ajaxResult.setMessage("操作成功，'"+missionModule.getMimoname()+"'"+"进入待集成队列");
            JSONObject json = JSONObject.fromObject(ajaxResult);
            return json;
        }
        else
            return "fail to change";
    }

    @ResponseBody
    @RequestMapping(value = "/publishIngratableMission")
    public String publishIngratableMission(@RequestParam("missionModuleId") long missionModuleId,@RequestParam("proId") long proId,@RequestParam("receiverId") long receiverId,HttpServletRequest request){
        //获取当前用户
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userInfo");

        //首先修改模块任务的状态为：待测试
        if(missionService.changeMissionStatus(missionModuleId,MissionStatusEnum.testing.ordinal())){
            //然后新建测试任务
            TestList testList = new TestList();
            testList.setMmid(missionModuleId);
            testList.setMissionstatus((long) MissionStatusEnum.testing.ordinal());
            testList.setPublisherid(user.getId());
            testList.setReceiverid(receiverId);
            testList.setProid(proId);
            //记录
            addActionRecord(user.getId(),receiverId,missionService.getMissionModuleById(missionModuleId),"集成测试任务",proId);

            return missionService.publishTestMission(testList);
        }
        else {
            return "fail to change";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/matchMissionAccepter")
    public String matchMissionAccepter(@RequestParam("missionModuleId") long missionModuleId){
        //获取模块对象
        MissionModule missionModule = missionService.getMissionModuleById(missionModuleId);
        //获取缺陷模块父模块对象
        MissionModule missionModule_father = missionService.getMissionModuleById(missionModule.getFatherindex());
        //区分类型
        long missionModule_Type = missionModule_father.getCategory();
        if(missionModule_Type == MissionMuduleEnum.DetailedDesign.ordinal()){
            //获取详细设计任务状态
            if(missionService.isMissionExistingByMissionModule(missionModuleId)){
                return "软件开发工程师";
            }
            else
                return "软件测试工程师";
        }
        else if(missionModule_Type == MissionMuduleEnum.TotalDesign.ordinal()){
            return "项目组组长";
        }
        else if(missionModule_Type == MissionMuduleEnum.Requirement.ordinal()){
            return "项目经理";
        }
        else if(missionModule_Type == MissionMuduleEnum.Project.ordinal()){
            return "产品经理";
        }
        else
            return "error";
    }

    @ResponseBody
    @RequestMapping(value = "/publishAssignMission")
    public String publishAssignMission(@RequestParam("missionModuleId") long missionModuleId,@RequestParam("proId") long proId,@RequestParam("receiverId") long receiverId,HttpServletRequest request){

        //获取当前用户
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userInfo");

        //生成指派任务
        AssignMission assignMission = new AssignMission();
        assignMission.setMmid(missionModuleId);
        assignMission.setMissionstatus((long) MissionStatusEnum.Unfinished.ordinal());
        assignMission.setPublisherid(user.getId());
        assignMission.setReceiverid(receiverId);
        assignMission.setProid(proId);

        String result = missionService.publishAssignMission(assignMission);
        //记录
        addActionRecord(user.getId(),receiverId,missionService.getMissionModuleById(missionModuleId),"指派修复任务",proId);

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getAssignMissionStatus")
    public String getAssignMissionStatus(@RequestParam("missionModuleId") long missionModuleId){
        AssignMission assignMission = missionService.getAssignMissionObjByModuleId(missionModuleId);
        if(assignMission==null){
            return String.valueOf(-1);
        }
        else{
            return String.valueOf(assignMission.getMissionstatus());
        }
    }


    @ResponseBody
    @RequestMapping(value = "/whetherMissionModuleCanBeIntegrated")
    public String whetherMissionModuleCanBeIntegrated(@RequestParam("missionModuleId") long missionModuleId){
        //模块可集成的标准为：所有子模块的对应的任务状态均为《完成》
        //子模块分两种，一种是未发布的，一种是已发布的
        StringBuffer result = new StringBuffer();

        //先判断是否有未发布的子模块
        List<MissionModule> unpublishedSomModule = missionService.getAllUnPublishedSonModule(missionModuleId);
        if(unpublishedSomModule!=null && unpublishedSomModule.size()!=0){
            result.append("存在未发布的子模块：");
            for(MissionModule missionModule:unpublishedSomModule){
                result.append(missionModule.getMimoname());
            }
        }
        else {
            //再判断已发布的子模块是否全部完成
            List<MissionModule> unfinishedSomModule = missionService.getAllUnFinishedSonModule(missionModuleId);
            if(unfinishedSomModule!=null && unfinishedSomModule.size()!=0){
                result.append("存在未完成的子模块：");
                for(MissionModule missionModule:unfinishedSomModule){
                    result.append(missionModule.getMimoname()+";");
                }
            }
            else {
                result.append("ok");
            }
        }

        System.out.println(result.toString());

        return result.toString();
    }

    @RequestMapping(value = "/viewDetails")
    public Object viewDetails(@ModelAttribute("moduleId") String moduleId, @ModelAttribute("type") String type,Model model){
        model.addAttribute("type",type);
        //获取模块对象
        MissionModule missionModule = missionService.getMissionModuleById(Long.valueOf(moduleId));
        //获取信息对象
        Object info = missionService.getDetailsOfMissionModule(missionModule);
        //获取任务信息
        Object missionInfo = missionService.getMissionObjByModuleId(Long.valueOf(moduleId));
        //获取缺陷列表
        List<Defect> defectList = null;
        List<MissionModule> defectMissionModuleList = null;
        if(!type.equals(MissionMuduleEnum.Defect.ordinal())){
            defectMissionModuleList = missionService.getAllDefectMissionModuleOfMissionModule(Long.valueOf(moduleId));
            defectList = missionService.getDefectListByModuleId(defectMissionModuleList);
        }
        else {
            defectList = new ArrayList<Defect>();
            defectMissionModuleList = new ArrayList<MissionModule>();
        }

        //封装
        JSONObject json = JSONObject.fromObject(info);
        JSONObject mission = JSONObject.fromObject(missionInfo);
        JSONObject missionModuleJson = JSONObject.fromObject(missionModule);
        model.addAttribute("info",json);
        model.addAttribute("missionInfo",mission);
        model.addAttribute("missionModule",missionModuleJson);
        model.addAttribute("defectList",defectList);
        model.addAttribute("defectMissionModuleList",defectMissionModuleList);
        model.addAttribute("defectList_size",defectList.size());
        model.addAttribute("proId",missionModule.getProid());

        return "Details";
    }

    @ResponseBody
    @RequestMapping(value = "/isFunctional")
    public String isFunctional(@RequestParam("moduleId") long moduleId,HttpServletRequest request){
        //获取模块对象
        MissionModule missionModule  = missionService.getMissionModuleById(moduleId);
        //获取当前对象
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userInfo");
        //判断当前用户是否为模块的创建者
        if(missionModule.getOperatorid() == user.getId()){
            return "true";
        }
        else {
            return "false";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/delTree")
    public String delTree(@RequestParam("moduleId") long moduleId){
        //获取对象
        MissionModule missionModule = missionService.getMissionModuleById(moduleId);
        //获取子树列表
        List<MissionModule> missionModuleTree = missionService.getAllSonTreeNode(missionModule);
        //删除任务
        if(missionService.clearTrack(missionModuleTree)){
            return "true";
        }
        else {
            return "false";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/updateProject")
    public String updateProject(@RequestParam("id") long id,@RequestParam("projectName") String projectName,@RequestParam("environment") String environment,@RequestParam("pattern") String pattern){
        //封装Bean
        Project project = new Project();
        project.setId(id);
        project.setProjectname(projectName);
        project.setEnvironment(environment);
        project.setPattern(pattern);

        //更新实际模块
        if(missionService.updateProject(project)){
            //更新抽象模块
            if(missionService.updateMissionModuleName(projectName,MissionMuduleEnum.Project.ordinal(),id)){
                return "ok";
            }
            else {
                return "fail to update abstract";
            }
        }
        else
            return "fail to update real";
    }

    @ResponseBody
    @RequestMapping(value = "/updateReq")
    public String updateReq(@RequestParam("id") long id,@RequestParam("reqName") String reqName,@RequestParam("businessReq")String businessReq,@RequestParam("userReq") String userReq,@RequestParam("functionalReq") String functionalReq,@RequestParam("nonFunctionalReq") String nonFunctionalReq){
        //封装Bean
        Requirement requirement = new Requirement();
        requirement.setId(id);
        requirement.setReqname(reqName);
        requirement.setBusinessreq(businessReq);
        requirement.setUserreq(userReq);
        requirement.setFunctionalreq(functionalReq);
        requirement.setNonfunctionalreq(nonFunctionalReq);

        //更新实际模块
        if(missionService.updateReq(requirement)){
            //更新抽象模块
            if(missionService.updateMissionModuleName(reqName,MissionMuduleEnum.Requirement.ordinal(),id)){
                return "ok";
            }
            else {
                return "fail to update abstract";
            }
        }
        else
            return "fail to update real";
    }

    @ResponseBody
    @RequestMapping(value = "/updateTolDesign_noImg")
    public String updateTolDesign_noImg(@RequestParam("id") long id,@RequestParam("tolName") String tolName,@RequestParam("processLogic") String processLogic){
        //封装Bean
        TotalDesign totalDesign = new TotalDesign();
        totalDesign.setId(id);
        totalDesign.setTolname(tolName);
        totalDesign.setProcesslogic(processLogic);
        //softwareStructure

        //更新实际模块
        if(missionService.updateTolDesign_noImg(totalDesign)){
            //更新抽象模块
            if(missionService.updateMissionModuleName(tolName,MissionMuduleEnum.TotalDesign.ordinal(),id)){
                return "ok";
            }
            else {
                return "fail to update abstractModule";
            }
        }
        else
            return "fail to update RealModule";
    }

    @ResponseBody
    @RequestMapping(value = "/updateTolDesign")
    public String updateTolDesign(@RequestParam("id") long id,@RequestParam("tolName") String tolName,@RequestParam("processLogic") String processLogic,@RequestParam("softwareStructure") MultipartFile softwareStructure,HttpServletRequest request){
        //封装Bean
        TotalDesign totalDesign = new TotalDesign();
        totalDesign.setId(id);
        totalDesign.setTolname(tolName);
        totalDesign.setProcesslogic(processLogic);
        //softwareStructure

        //上传图片
        String path = uploadImg(softwareStructure,request,"images/TotalDesign",id);
        if(!(path.equals("error"))){
            //更新图片路径
            totalDesign.setSoftwarestructure("/images/TotalDesign/"+path);
            if(missionService.updateTolDesign(totalDesign)){
                return "ok";
            }
            else {
                return "fail to update path";
            }
        }
        else {
            return "upload exception";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/updateDelDesign_noImg")
    public String updateDelDesign_noImg(@RequestParam("id") long id,@RequestParam("detName") String detName,@RequestParam("arithmetic") String arithmetic,@RequestParam("limitingCondition") String limitingCondition,@RequestParam("input") String input,@RequestParam("output") String output){
        //封装bean
        DetailedDesign detailedDesign = new DetailedDesign();
        detailedDesign.setId(id);
        detailedDesign.setDetname(detName);
        //processLogic
        detailedDesign.setArithmetic(arithmetic);
        detailedDesign.setLimitingcondition(limitingCondition);
        detailedDesign.setInput(input);
        detailedDesign.setOutput(output);

        //更新实际模块
        if(missionService.updateDetDesign_noImg(detailedDesign)){
            //更新抽象模块
            if(missionService.updateMissionModuleName(detName,MissionMuduleEnum.DetailedDesign.ordinal(),id)){
                return "ok";
            }
            else {
                return "fail";
            }
        }
        else
            return "fail";
    }

    @ResponseBody
    @RequestMapping(value = "/updateDelDesign")
    public String updateDelDesign(@RequestParam("id") long id,@RequestParam("detName") String detName,@RequestParam("arithmetic") String arithmetic,@RequestParam("limitingCondition") String limitingCondition,@RequestParam("input") String input,@RequestParam("output") String output,@RequestParam("processLogic") MultipartFile processLogic,HttpServletRequest request){
        //封装bean
        DetailedDesign detailedDesign = new DetailedDesign();
        detailedDesign.setId(id);
        detailedDesign.setDetname(detName);
        //processLogic
        detailedDesign.setArithmetic(arithmetic);
        detailedDesign.setLimitingcondition(limitingCondition);
        detailedDesign.setInput(input);
        detailedDesign.setOutput(output);


        //更新图片
        String path = uploadImg(processLogic,request,"images/DetailedDesign",id);
        if(!(path.equals("error"))){
            //更新图片路径
            detailedDesign.setProcesslogic("/images/DetailedDesign/"+path);
            //更新其他信息
            if(missionService.updateDetDesign(detailedDesign)){
                return "ok";
            }
            else
                return "fail to update info";
        }
        else
            return "fail to update photo";
    }

    @ResponseBody
    @RequestMapping(value = "/updateDefect")
    public String updateDefect(@RequestParam("id") long id,@RequestParam("title") String title,@RequestParam("defectDescription") String defectDescription,@RequestParam("severity") long severity){
        //封装Bean
        Defect defect = new Defect();
        defect.setId(id);
        defect.setTitle(title);
        defect.setDefectdescription(defectDescription);
        defect.setSeverity(severity);

        //更新实际模块
        if(missionService.updateDefect(defect)){
            //更新抽象模型
            if(missionService.updateMissionModuleName(title,MissionMuduleEnum.Defect.ordinal(),id)){
                return "ok";
            }
            else {
                return "fail to update abstract";
            }
        }
        else
            return "fail to update real";
    }

    //Methods
    private long getIndexFromMemberRoleEnum(String value){
        if(MemberRoleEnum.ProjectBuilder.getRole().equals(value))
            return MemberRoleEnum.ProjectBuilder.ordinal();
        else if(MemberRoleEnum.ProductManager.getRole().equals(value))
            return MemberRoleEnum.ProductManager.ordinal();
        else if(MemberRoleEnum.ProjectManager.getRole().equals(value))
            return MemberRoleEnum.ProjectManager.ordinal();
        else if(MemberRoleEnum.GroupLeader.getRole().equals(value))
            return MemberRoleEnum.GroupLeader.ordinal();
        else if(MemberRoleEnum.Programmer.getRole().equals(value))
            return MemberRoleEnum.Programmer.ordinal();
        else if(MemberRoleEnum.QA.getRole().equals(value))
            return MemberRoleEnum.QA.ordinal();
        else  if(MemberRoleEnum.Independent.getRole().equals(value))
            return MemberRoleEnum.Independent.ordinal();
        else
            return -1;
    }
    private String uploadImg(MultipartFile file,HttpServletRequest request,String path,long soleId){
        //生成图片路径
        //MultipartFile转file
        String fileNameTemp = file.getOriginalFilename();
        String prefix = fileNameTemp.substring(fileNameTemp.lastIndexOf("."));
        File imgFile = null;
        try {
            imgFile = File.createTempFile("temp",prefix);
            file.transferTo(imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(imgFile!=null) {
            String ImgFileName = fileNameTemp;
            String rootpath = null;
            try {
                rootpath = ResourceUtils.getURL("classpath:").getPath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String imagepath = StringUtils.substringBefore(rootpath, "out") + "static/" + path;
            String imageoutpath = request.getServletContext().getRealPath(path);

            //获取保存路径的绝对地址(这里还是需要改一下)
            String filePath = imagepath;

            //生成带格式的文件名称
            String fileName = String.valueOf(soleId) + ImgFileName.substring(ImgFileName.lastIndexOf("."));
            System.out.println(fileName);

            try {
                //复制文件到web目录下持久化
                FileUtils.copyFile(imgFile, new File(filePath, fileName));
                //同时对服务器输出的image进行复制，便于前端实时响应
                FileUtils.copyFile(imgFile, new File(imageoutpath, fileName));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return fileName;
        }
        else
            return "error";
    }

    /**
     * 消息生成方法，该方法在任务发生后，生成消息，并持久化到数据库中
     * @param subject 任务发布者ID
     * @param object 任务接收者ID
     * @param missionModule 任务模块对象
     * @param missionType 任务类型
     * @param proId 项目ID
     */
    private void addActionRecord(long subject,long object,MissionModule missionModule,String missionType,long proId){
        Message_mission message_mission = new Message_mission();
        message_mission.setSubject_id(subject);
        message_mission.setObject_id(object);
        //获取主语与谓语在项目中的称呼
        ProjectMember subject_ProjectMember = projectService.getProjectMemberObjAsPartner(proId,subject);
        ProjectMember object_ProjectMember = projectService.getProjectMemberObjAsPartner(proId,object);
        String msg = MemberRoleEnum.class.getEnumConstants()[subject_ProjectMember.getRoletype().intValue()]+" "+subject_ProjectMember.getMembername()
                +" 向 "+MemberRoleEnum.class.getEnumConstants()[object_ProjectMember.getRoletype().intValue()]+" "+object_ProjectMember.getMembername()+" 发布了 "
                +missionType+" ，相关模块为："+MissionMuduleEnum.class.getEnumConstants()[missionModule.getCategory().intValue()]+" "+missionModule.getMimoname();
        message_mission.setMessage_mission(msg);
        message_mission.setProid(proId);
        message_mission.setViewflag("false");

        //记录
        missionService.addMessageRecord(message_mission);
    }
}


