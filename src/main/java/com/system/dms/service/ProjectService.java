package com.system.dms.service;

import com.system.dms.entity.MissionModule;
import com.system.dms.entity.Project;
import com.system.dms.entity.ProjectMember;

import java.util.List;

public interface ProjectService {
    String buildProject(Project project);
    List<Project> getProjectListByIdAsBuilder(long userId);
    long getProIdByProNameAndOperatorId(Project project);
    boolean insertProjectMember(ProjectMember projectMember);
    List<Project> getProjectListByIdAsParticipant(long userId);
    String addMember(ProjectMember projectMember);
    String removeMember(long proId,long uid);
    Project getProjectByProId(long proId);
    List<ProjectMember> getProjectMemberListByProId(long proId);
    String updateMemberInfo(long pid,long uid,String membername,long roletype);
    ProjectMember getProjectMemberObjAsBuilder(long proId,long uid);
    ProjectMember getProjectMemberObjAsPartner(long proId,long uid);
    boolean delProjectAllMember(long proId);
}
