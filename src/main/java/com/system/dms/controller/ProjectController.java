package com.system.dms.controller;

import com.system.dms.entity.*;
import com.system.dms.service.MissionService;
import com.system.dms.service.ProjectService;
import com.system.dms.service.UserService;
import com.system.dms.staticEnum.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private MissionService missionService;

    @RequestMapping(value = "/toManageMember")
    public String toManageMember(@ModelAttribute("proId") long proId,Model model){
        //根据项目id返回项目信息
        Project project = projectService.getProjectByProId(proId);
        //根据项目id返回成员列表
        List<ProjectMember> projectMemberList = projectService.getProjectMemberListByProId(proId);
        //根据用户id返回用户名列表
        List<String> usernameList = userService.getAllUsernameByProjectMemberList(projectMemberList);
        //转发到下一页面
        model.addAttribute("projectInfo",project);
        model.addAttribute("projectMemberList",projectMemberList);
        model.addAttribute("usernameList",usernameList);
        return "MemberManagePage";
    }

    @RequestMapping(value = "/toProjectView")
    public String toProjectView(@ModelAttribute("proId") long proId,Model model){
        //根据项目id返回项目模块对象
        MissionModule projectMissionModule = missionService.getMissionModuleByCategoryAndIndex(MissionMuduleEnum.Project.ordinal(),proId);
        //查询该项目下模块的树形结构
        List<MissionModule> projectMissionModuleTree = missionService.getAllSonTreeNode(projectMissionModule);
        //将模型树转换成真正的树对象结构
        TreeGraph treeGraph = getTreeGraph(projectMissionModuleTree);

        List<TreeGraph> treeGraphList = new ArrayList<TreeGraph>();

        treeGraphList.add(treeGraph);

        JSONArray json = JSONArray.fromObject(treeGraphList);

        model.addAttribute("tree",json);

        return "ProjectView";
    }

    @RequestMapping(value = "/toMessages")
    public String toMessages(@ModelAttribute("proId") long proId,Model model,HttpServletRequest request){
        //获取当前操作对象
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userInfo");

        //获取当前对象在项目中的称呼
        ProjectMember projectMember = projectService.getProjectMemberObjAsPartner(proId,user.getId());

        //获取消息队列
        List<Message_mission> message_missions = missionService.getMessageByUser(user.getId(),proId);

        //点击后消除未读消息
        missionService.clearNotReadingFlag(user.getId(),proId);

        model.addAttribute("message_missions",message_missions);
        model.addAttribute("projectMember",projectMember);

        return "Messages";
    }

    @RequestMapping(value = "/getNodeSonTreeGraph")
    public String getMissionModuleSonTreeGraph(@ModelAttribute("missionModuleId") long missionModuleId,Model model){
        //根据模块id返回模块对象
        MissionModule missionModule = missionService.getMissionModuleById(missionModuleId);
        //查询该项目下模块的树形结构
        List<MissionModule> projectMissionModuleTree = missionService.getAllSonTreeNode(missionModule);
        //将模型树转换成真正的树对象结构
        TreeGraph treeGraph = getTreeGraph(projectMissionModuleTree);

        List<TreeGraph> treeGraphList = new ArrayList<TreeGraph>();

        treeGraphList.add(treeGraph);

        JSONArray json = JSONArray.fromObject(treeGraphList);

        model.addAttribute("tree",json);

        return "ProjectView";
    }

    @RequestMapping(value = "/toMissionWindowAsBuilder")
    public String toMissionWindowAsBuilder(@ModelAttribute("proId") long proId,Model model,HttpServletRequest request){
//        获取当前对象
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userInfo");
        //获取当前项目信息
        Project project = projectService.getProjectByProId(proId);
        //获取当前对象在本项目下的角色
        ProjectMember projectMember = projectService.getProjectMemberObjAsBuilder(proId,user.getId());

//        可操作模块(项目创建者无可操作集)

        //初始模块(项目)
        MissionModule missionModule_initial = missionService.getInitialModulesOfProjectBuilder(user.getId(),proId,MissionMuduleEnum.Project.ordinal(),StatementEnum.UnPublished.ordinal());
        //已发布模块(项目toPM)
        MissionModule missionModule_published = missionService.getPublishedModulesOfProjectBuilder(user.getId(),proId,MissionMuduleEnum.Project.ordinal(),StatementEnum.Published.ordinal());
        //根据已发布模块获取任务状态列表
        List<MissionModule> missionModuleList = new ArrayList<MissionModule>();
        if(missionModule_published!=null){
            missionModuleList.add(missionModule_published);
        }
        List<Mission> publishedMissionList = missionService.getMissionListByModuleList(missionModuleList);

        List<MissionModule> operableMissionModuleList = new ArrayList<MissionModule>();
        List<MissionModule> initialMissionModuleList = new ArrayList<MissionModule>();
        if(missionModule_initial!=null)
            initialMissionModuleList.add(missionModule_initial);
        List<MissionModule> publishedMissionModuleList = new ArrayList<MissionModule>();
        System.out.println(missionModule_published==null);
        if(missionModule_published!=null)
            publishedMissionModuleList.add(missionModule_published);

        List<MissionModule> integratableMissionModuleList = new ArrayList<MissionModule>();
        List<MissionModule> repairingMissionModuleList = new ArrayList<MissionModule>();
        List<MissionModule> finishedMissionModuleList = null;

        //已完成模块
        //待修复模块
        List<Mission> repairingMissionList = missionService.getMissionListOfReceiverByStatus(user.getId(),MissionStatusEnum.repairing.ordinal(),proId);
        repairingMissionModuleList = missionService.getMissionModuleListByMissionList(repairingMissionList);
        //任务发布者是自己的
        List<Mission> finishedMissionList_publisher = missionService.getMissionListOfPublisherByStatus(user.getId(),MissionStatusEnum.finished.ordinal(),proId);
        finishedMissionModuleList = missionService.getMissionModuleListByMissionList(finishedMissionList_publisher);

        //推送到前端
        model.addAttribute("project",project);
        model.addAttribute("projectMember",projectMember);
        model.addAttribute("operableMissionModuleList",operableMissionModuleList);
        model.addAttribute("operableMissionModuleList_size",operableMissionModuleList.size());
        model.addAttribute("initialMissionModuleList",initialMissionModuleList);
        model.addAttribute("initialMissionModuleList_size",initialMissionModuleList.size());
        model.addAttribute("publishedMissionModuleList",publishedMissionModuleList);
        model.addAttribute("publishedMissionList",publishedMissionList);
        model.addAttribute("publishedMissionModuleList_size",publishedMissionModuleList.size());
        model.addAttribute("integratableMissionModuleList",integratableMissionModuleList);
        model.addAttribute("integratableMissionModuleList_size",integratableMissionModuleList.size());
        model.addAttribute("repairingMissionModuleList",repairingMissionModuleList);
        model.addAttribute("repairingMissionModuleList_size",repairingMissionModuleList.size());
        model.addAttribute("finishedMissionModuleList",finishedMissionModuleList);
        model.addAttribute("finishedMissionModuleList_size",finishedMissionModuleList.size());


        return "MissionWindow";
    }

    @RequestMapping(value = "/toMissionWindowAsPart")
    /**
     * 该方法为任务窗口载入方法，根据当前用户在项目中不同的身份，显示不同的界面和数据
     */
    public String toMissionWindowAsPart(@ModelAttribute("proId") long proId,Model model,HttpServletRequest request){
        //获取当前对象
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userInfo");
        //获取当前项目信息
        Project project = projectService.getProjectByProId(proId);
        //获取当前对象在本项目下的角色
        ProjectMember projectMember = projectService.getProjectMemberObjAsPartner(proId,user.getId());

        //程序员和测试员不与其他人共用一套检索方法

        List<MissionModule> operableMissionModuleList = null;
        List<MissionModule> initialMissionModuleList = null;
        List<MissionModule> publishedMissionModuleList = null;
        List<MissionModule> integratableMissionModuleList = null;
        List<MissionModule> repairingMissionModuleList = null;
        List<MissionModule> finishedMissionModuleList = null;
        List<Mission> publishedMissionList = null;

        if(projectMember.getRoletype()==MemberRoleEnum.Programmer.ordinal()){
            //对详细设计任务块进行操作
            //可操作模块(Mission列表中，任务接收者是自己的，任务是初始状态/待修复)
            List<Mission> unfinishedMissionList = missionService.getMissionListOfReceiverByStatus(user.getId(),MissionStatusEnum.Unfinished.ordinal(),proId);
            operableMissionModuleList = missionService.getMissionModuleListByMissionList(unfinishedMissionList);
             //再检索指派任务队列
             List<AssignMission> assignMissionList_operable = missionService.getAssignMissionListOfReceiverByStatus(user.getId(),MissionStatusEnum.Unfinished.ordinal(),proId);
             operableMissionModuleList.addAll(missionService.getMissionModuleListByAssignMissionList(assignMissionList_operable));

            //初始模块(Mission列表中，任务接收者是自己的，任务是已编辑)
            List<Mission> editedMissionList = missionService.getMissionListOfReceiverByStatus(user.getId(),MissionStatusEnum.edited.ordinal(),proId);
            initialMissionModuleList = missionService.getMissionModuleListByMissionList(editedMissionList);
             //再检索指派任务队列
             List<AssignMission> assignMissionList_initail = missionService.getAssignMissionListOfReceiverByStatus(user.getId(),MissionStatusEnum.edited.ordinal(),proId);
             initialMissionModuleList.addAll(missionService.getMissionModuleListByAssignMissionList(assignMissionList_initail));

            //已发布模块(Mission列表中，任务接收者是自己，任务是待测试||任务接收者是自己，任务是已完成)
              //待测试
            List<Mission> testingMissionList = missionService.getMissionListOfReceiverByStatus(user.getId(),MissionStatusEnum.testing.ordinal(),proId);
            publishedMissionModuleList = missionService.getMissionModuleListByMissionList(testingMissionList);
              //再检索指派任务队列
              List<AssignMission> assignMissionList_published = missionService.getAssignMissionListOfReceiverByStatus(user.getId(),MissionStatusEnum.testing.ordinal(),proId);
              publishedMissionModuleList.addAll(missionService.getMissionModuleListByAssignMissionList(assignMissionList_published));
            //待修复模块
            List<Mission> repairingMissionList = missionService.getMissionListOfReceiverByStatus(user.getId(),MissionStatusEnum.repairing.ordinal(),proId);
            repairingMissionModuleList = missionService.getMissionModuleListByMissionList(repairingMissionList);
            //已完成模块
            List<Mission> finishedMissionList = missionService.getMissionListOfReceiverByStatus(user.getId(),MissionStatusEnum.finished.ordinal(),proId);
            finishedMissionModuleList = missionService.getMissionModuleListByMissionList(finishedMissionList);
              //再检索指派任务队列
              List<AssignMission> assignMissionList_finished = missionService.getAssignMissionListOfReceiverByStatus(user.getId(),MissionStatusEnum.finished.ordinal(),proId);
              finishedMissionModuleList.addAll(missionService.getMissionModuleListByAssignMissionList(assignMissionList_finished));

            //根据已发布模块获取任务状态列表
            publishedMissionList = missionService.getMissionListByModuleList(publishedMissionModuleList);

            //不需要的集成
            integratableMissionModuleList = new ArrayList<MissionModule>();
        }
        else if(projectMember.getRoletype()==MemberRoleEnum.QA.ordinal()){
            //对详细设计任务块进行操作
            //可操作模块(TestList列表中，任务接收者是自己的,任务状态是待测试)
            List<TestList> testingMissionList = missionService.getTestListOfReceiverByStatus(user.getId(),MissionStatusEnum.testing.ordinal(),proId);
            operableMissionModuleList = missionService.getMissionModuleListByTestList(testingMissionList);
            //初始模块(MissionModule列表中，任务模块的发布者是自己的，任务声明是未发布：缺陷|可能存在缺陷)
            initialMissionModuleList = missionService.getTypeModulesOfProjectPartner(proId,user.getId(),matchMissionModuleCategory(projectMember.getRoletype()),StatementEnum.UnPublished.ordinal());
            //已发布模块(MissionModule列表中，任务发布者是自己的，任务是已发布：缺陷|确认存在缺陷,发布缺陷的同时对相应的程序员操作的任务块进行状态改写：待修复)
            publishedMissionModuleList = missionService.getTypeModulesOfProjectPartner(proId,user.getId(),matchMissionModuleCategory(projectMember.getRoletype()),StatementEnum.Published.ordinal());
            //待修复模块(TestList列表中，任务接收者是自己的,任务状态是待修复)
            List<TestList> repairMissionList = missionService.getTestListOfReceiverByStatus(user.getId(),MissionStatusEnum.repairing.ordinal(),proId);
            repairingMissionModuleList = missionService.getMissionModuleListByTestList(repairMissionList);
            //已完成模块(TestList中，mission状态为已完成的模块)
            List<TestList> testLists = missionService.getTestListOfReceiverByStatus(user.getId(),MissionStatusEnum.finished.ordinal(),proId);
            finishedMissionModuleList = missionService.getMissionModuleListByTestList(testLists);

            //根据已发布模块获取任务状态列表
            publishedMissionList = missionService.getMissionListByModuleList(publishedMissionModuleList);

            //不需要的集成
            integratableMissionModuleList = new ArrayList<MissionModule>();
        }
        else{
            //可操作模块(Mission列表中，任务接收者是自己的，任务是初始状态/待修复)
            List<Mission> unfinishedMissionList = missionService.getMissionListOfReceiverByStatus(user.getId(),MissionStatusEnum.Unfinished.ordinal(),proId);
            operableMissionModuleList = missionService.getMissionModuleListByMissionList(unfinishedMissionList);
            //初始模块
            initialMissionModuleList = missionService.getTypeModulesOfProjectPartner(proId,user.getId(),matchMissionModuleCategory(projectMember.getRoletype()),StatementEnum.UnPublished.ordinal());
            //已发布模块
            publishedMissionModuleList = missionService.getTypeModulesOfProjectPartner(proId,user.getId(),matchMissionModuleCategory(projectMember.getRoletype()),StatementEnum.Published.ordinal());
            //可集成模块(Mission列表中，任务的接收者是自己，任务是已编码状态或待测试)
            List<Mission> integratableMissionList_edited = missionService.getMissionListOfReceiverByStatus(user.getId(),MissionStatusEnum.edited.ordinal(),proId);
            List<Mission> integratableMissionList_testing = missionService.getMissionListOfReceiverByStatus(user.getId(),MissionStatusEnum.testing.ordinal(),proId);
            integratableMissionModuleList = missionService.getMissionModuleListByMissionList(integratableMissionList_edited);
            integratableMissionModuleList.addAll(missionService.getMissionModuleListByMissionList(integratableMissionList_testing));
            //待修复模块
            List<Mission> repairingMissionList = missionService.getMissionListOfReceiverByStatus(user.getId(),MissionStatusEnum.repairing.ordinal(),proId);
            repairingMissionModuleList = missionService.getMissionModuleListByMissionList(repairingMissionList);
            //已完成模块
             //任务发布者是自己的
            List<Mission> finishedMissionList_publisher = missionService.getMissionListOfPublisherByStatus(user.getId(),MissionStatusEnum.finished.ordinal(),proId);
            finishedMissionModuleList = missionService.getMissionModuleListByMissionList(finishedMissionList_publisher);
             //因为有集成环节，任务接收者是自己的也要显示
            List<Mission> finishedMissionList_receiver = missionService.getMissionListOfReceiverByStatus(user.getId(),MissionStatusEnum.finished.ordinal(),proId);
            finishedMissionModuleList.addAll(missionService.getMissionModuleListByMissionList(finishedMissionList_receiver));
              //再检索指派任务队列
              List<AssignMission> assignMissionList_finished = missionService.getAssignMissionListOfPublisherByStatus(user.getId(),MissionStatusEnum.finished.ordinal(),proId);
              finishedMissionModuleList.addAll(missionService.getMissionModuleListByAssignMissionList(assignMissionList_finished));
            //根据已发布模块获取任务状态列表
            publishedMissionList = missionService.getMissionListByModuleList(publishedMissionModuleList);

        }


        //推送到前端
        model.addAttribute("project",project);
        model.addAttribute("projectMember",projectMember);
        model.addAttribute("operableMissionModuleList",operableMissionModuleList);
        model.addAttribute("operableMissionModuleList_size",operableMissionModuleList.size());
        model.addAttribute("initialMissionModuleList",initialMissionModuleList);
        model.addAttribute("initialMissionModuleList_size",initialMissionModuleList.size());
        model.addAttribute("publishedMissionModuleList",publishedMissionModuleList);
        model.addAttribute("publishedMissionList",publishedMissionList);
        model.addAttribute("publishedMissionModuleList_size",publishedMissionModuleList.size());
        model.addAttribute("integratableMissionModuleList",integratableMissionModuleList);
        model.addAttribute("integratableMissionModuleList_size",integratableMissionModuleList.size());
        model.addAttribute("repairingMissionModuleList",repairingMissionModuleList);
        model.addAttribute("repairingMissionModuleList_size",repairingMissionModuleList.size());
        model.addAttribute("finishedMissionModuleList",finishedMissionModuleList);
        model.addAttribute("finishedMissionModuleList_size",finishedMissionModuleList.size());

        return "MissionWindow";
    }

    @RequestMapping(value = "/buildProject")
    public String buildProject(@ModelAttribute("project") Project project, HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("userInfo");
        project.setAccessright(Long.valueOf(AccessRightEnum.PRIVATE.ordinal()));
        project.setStatement(Long.valueOf(StatementEnum.Initial.ordinal()));
        project.setOperatorid(user.getId());
        String result = projectService.buildProject(project);

        if(result.equals("创建成功")){
            //获取项目id
            long proId = projectService.getProIdByProNameAndOperatorId(project);
            //生成初始角色表
            ProjectMember projectMember = new ProjectMember();
            projectMember.setProid(proId);
            projectMember.setUserid(user.getId());
            projectMember.setStatement(Long.valueOf(StatementEnum.Initial.ordinal()));
            if(project.getCategory().equals("PSP")){
                projectMember.setMembername(MemberRoleEnum.Independent.getRole());
                projectMember.setRoletype(Long.valueOf(MemberRoleEnum.Independent.ordinal()));
                projectService.insertProjectMember(projectMember);
                //生成任务模块
                MissionModule missionModule = new MissionModule();
                missionModule.setMimoname(project.getProjectname());
                missionModule.setCategory(Long.valueOf(MissionMuduleEnum.Project.ordinal()));
                missionModule.setModuleindex(proId);
                missionModule.setStatement(Long.valueOf(StatementEnum.UnPublished.ordinal()));
                missionModule.setFatherindex(Long.valueOf(-1));//根节点
                missionModule.setOperatorid(user.getId());
                if(missionService.addMissionMudule(missionModule)==-1){
                    return "fail";
                }
            }
            if(project.getCategory().equals("TSP")){
                projectMember.setMembername("项目发起人");
                projectMember.setRoletype(Long.valueOf(MemberRoleEnum.ProjectBuilder.ordinal()));
                projectService.insertProjectMember(projectMember);
                //生成任务模块
                MissionModule missionModule = new MissionModule();
                missionModule.setMimoname(project.getProjectname());
                missionModule.setCategory(Long.valueOf(MissionMuduleEnum.Project.ordinal()));
                missionModule.setModuleindex(proId);
                missionModule.setStatement(Long.valueOf(StatementEnum.UnPublished.ordinal()));
                missionModule.setFatherindex(Long.valueOf(-1));//根节点
                missionModule.setOperatorid(user.getId());
                missionModule.setProid(proId);
                if(missionService.addMissionMudule(missionModule)==-1){
                    return "fail";
                }
            }
            return "redirect:/toManageProject";
        }
        else{
            model.addAttribute("message","<"+project.getProjectname()+">,"+result);
            return "BuildProjectPage";
        }
    }

    @RequestMapping(value = "/addMember")
    @ResponseBody
    public String addMember(@RequestParam String ObjectProjectMember,@RequestParam String username){
        JSONObject projectMemberObject = JSONObject.fromObject(ObjectProjectMember);
        ProjectMember projectMember = (ProjectMember) projectMemberObject.toBean(projectMemberObject,ProjectMember.class);
        String result;

        if(userService.isUsernameExisting(username)){
            //根据username检索id
            User user = userService.getUserInfoByUsername(username);
            //封装projectMember对象
            projectMember.setUserid(user.getId());
            projectMember.setStatement(Long.valueOf(StatementEnum.Initial.ordinal()));
            //添加记录
            result = projectService.addMember(projectMember);
        }
        else {
            result = "用户名不存在!";
        }

        return result;
    }

    @RequestMapping(value = "/removeMember")
    @ResponseBody
    public String removeMember(@RequestParam("proId") String proId,@RequestParam("username") String username){
        long pid = Long.valueOf(proId);
        //通过username获取uid
        User user = userService.getUserInfoByUsername(username);
        return projectService.removeMember(pid,user.getId());
    }

    @RequestMapping(value = "/updateMember")
    @ResponseBody
    public String updateMember(@RequestParam("proId") String proId,@RequestParam("username") String username,@RequestParam("membername") String membername,@RequestParam("roletype") String roletype){
        //通过username找id
        User user = userService.getUserInfoByUsername(username);
        String result = projectService.updateMemberInfo(Long.valueOf(proId),user.getId(),membername,Long.valueOf(roletype));
        System.out.println(result);
        return  result;
    }

    @ResponseBody
    @RequestMapping(value = "/getRoleTypeInProject")
    public String getRoleTypeInProject(@RequestParam("proId") long proId, HttpServletRequest request){
        //获取当前用户对象
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userInfo");
        //获取当前用户在当前项目下的角色
        ProjectMember projectMember = projectService.getProjectMemberObjAsPartner(proId,user.getId());
        return String.valueOf(projectMember.getRoletype());
    }

    //Method
    public long matchMissionModuleCategory(long roleType){
        long category = -1;
        if(roleType == MemberRoleEnum.ProductManager.ordinal())
            category = MissionMuduleEnum.Requirement.ordinal();
        else if(roleType == MemberRoleEnum.ProjectManager.ordinal())
            category = MissionMuduleEnum.TotalDesign.ordinal();
        else if(roleType == MemberRoleEnum.GroupLeader.ordinal())
            category = MissionMuduleEnum.DetailedDesign.ordinal();
        else if(roleType == MemberRoleEnum.QA.ordinal())
            category = MissionMuduleEnum.Defect.ordinal();
        return category;
    }

    /**
     * 根据线性表述树结构生成链接表示树结构
     * @param missionModuleList
     * @return
     */
    private TreeGraph getTreeGraph(List<MissionModule> missionModuleList){
        //新建TreeGraph对象
        TreeGraph treeGraph = new TreeGraph();
        //设置根节点
        if(missionModuleList!=null){
            //根节点
            treeGraph.setId(missionModuleList.get(0).getId());
            treeGraph.setParent("null");
            treeGraph.setName(missionModuleList.get(0).getMimoname());
            treeGraph.setCategory(missionModuleList.get(0).getCategory());
            treeGraph.setMissionStat(missionService.getMissionObjByModuleId(missionModuleList.get(0).getId()).getMissionstatus());
            treeGraph.setChildren(buildTreeGraph(missionModuleList,treeGraph));
        }

        return  treeGraph;
    }

    /**
     * 深度遍历树结构并链接
     * @param missionModuleList
     * @param treeGraph
     * @return
     */
    private List<TreeGraph> buildTreeGraph(List<MissionModule> missionModuleList,TreeGraph treeGraph){
        //新建子序列
        List<TreeGraph> treeGraphs = new ArrayList<TreeGraph>();

        for(MissionModule missionModule:missionModuleList){
            //寻找子节点模块
            if(missionModule.getFatherindex() == treeGraph.getId()){
                TreeGraph treeGraphSon = new TreeGraph();
                treeGraphSon.setId(missionModule.getId());
                treeGraphSon.setName(missionModule.getMimoname());
                treeGraphSon.setParent(treeGraph.getName());
                treeGraphSon.setCategory(missionModule.getCategory());
                treeGraphSon.setMissionStat(missionService.getMissionObjByModuleId(missionModule.getId()).getMissionstatus());
                treeGraphs.add(treeGraphSon);

                treeGraphSon.setChildren(buildTreeGraph(missionModuleList,treeGraphSon));
            }
        }

        if(treeGraphs.size() == 0)
            return null;
        else
            return treeGraphs;
    }

    @ResponseBody
    @RequestMapping(value = "/delPro")
    public String delPro(@RequestParam("proId") long proId){
        //获得MissionModule对象
        MissionModule pro_missionModule = missionService.getMissionModuleByCategoryAndIndex(MissionMuduleEnum.Project.ordinal(),proId);
        //获取子树列表
        List<MissionModule> missionModuleTree = missionService.getAllSonTreeNode(pro_missionModule);
        //删除任务
        if(missionService.clearTrack(missionModuleTree)){
            //删除项目相关成员
            if(projectService.delProjectAllMember(proId)){
                return "true";
            }
            else {
                return "false";
            }
        }
        else {
            return "false";
        }
    }
}
