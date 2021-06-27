package com.system.dms.service.impl;

import com.system.dms.dao.ProjectMapper;
import com.system.dms.entity.Project;
import com.system.dms.entity.ProjectMember;
import com.system.dms.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service("ProjectService")
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    private ProjectMapper projectMapper;
    @Override
    public String buildProject(Project project) {
        if(projectMapper.isProjectNameBuiltBySameUId(project.getProjectname(),project.getOperatorid())==0){
            if(projectMapper.buildProject(project))
                return "创建成功";
            else
                return "I/O Exception";
        }
        else
            return "您已创建过相同名称的项目名，请更换项目名。";
    }

    @Override
    public List<Project> getProjectListByIdAsBuilder(long userId) {
        return projectMapper.getProjectListByUId(userId);
    }

    @Override
    public long getProIdByProNameAndOperatorId(Project project) {
        return projectMapper.getProIdByProNameAndOperatorId(project.getProjectname(),project.getOperatorid());
    }

    @Override
    public boolean insertProjectMember(ProjectMember projectMember) {
        return projectMapper.insertProjectMember(projectMember);
    }

    @Override
    public List<Project> getProjectListByIdAsParticipant(long uid) {
        //先获取符合类型的ProId
        List<Long> proIdList = projectMapper.getProIdByUIdAsParticipant(uid);

        int length = proIdList.size();
        int i = 0;
        List<Project> projectList = new LinkedList<Project>();
        while(i!=length){
            projectList.add(projectMapper.getProjectByProId(proIdList.get(i)));
            i++;
        }
        return projectList;
    }

    @Override
    public String addMember(ProjectMember projectMember) {
        boolean flag = projectMapper.isUserProjectBuilder(projectMember.getProid(),projectMember.getUserid())==1;
        //如果是创建者，那么还可以赋予一个实际职务
        if(flag){
            if(projectMapper.isUserIdExistingInProId(projectMember)<2){
                if(projectMapper.addMemberRecord(projectMember)){
                    return "添加成功";
                }
                else
                    return "I/O Exception";
            }
            else
                return "创建者只可以额外赋予一个实际职务";
        }
        else {
            if(projectMapper.isUserIdExistingInProId(projectMember)!=1){
                if(projectMapper.addMemberRecord(projectMember)){
                    return "添加成功";
                }
                else
                    return "I/O Exception";
            }
            else{
                return "用户已存在于该项目中，请勿重复添加。";
            }
        }
    }

    @Override
    public Project getProjectByProId(long proId) {
        return projectMapper.getProjectByProId(proId);
    }

    @Override
    public List<ProjectMember> getProjectMemberListByProId(long proId) {
        return projectMapper.getProjectMemberListByProId(proId);
    }

    @Override
    public String removeMember(long proId, long uid) {
        if(projectMapper.isUserProjectBuilder(proId,uid)==1){
            return "不可移除项目发起者";
        }
        else {
            if(projectMapper.removeMemberRecord(proId,uid))
                return "移除成功";
            else
                return "I/O Exception";
        }
    }

    @Override
    public String updateMemberInfo(long pid, long uid, String membername, long roletype) {
        if(projectMapper.isUserProjectBuilder(pid,uid)==1){
            return "不可修改项目发起者信息";
        }
        else {
            if(projectMapper.updateMemberRecord(pid,uid,membername,roletype))
                return "修改成功";
            else
                return "I/O Exception";
        }

    }

    @Override
    public ProjectMember getProjectMemberObjAsBuilder(long proId, long uid) {
        return projectMapper.getProjectMemberObjAsBuilder(proId,uid);
    }

    @Override
    public ProjectMember getProjectMemberObjAsPartner(long proId, long uid) {
        return projectMapper.getProjectMemberObjAsPartner(proId,uid);
    }

    @Override
    public boolean delProjectAllMember(long proId) {
        return projectMapper.delProjectAllMember(proId);
    }
}
