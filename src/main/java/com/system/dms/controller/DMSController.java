package com.system.dms.controller;

import com.system.dms.entity.DMS;
import com.system.dms.service.DMSService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/DMSInfo")
public class DMSController {
    @Autowired
    private DMSService dmsService;

    /**
     * 获取论坛的基本信息，保存至Session里
     * @param request
     * @return
     */
    @RequestMapping(value = "/toSystemSetting")
    public String getBasicInfoOfSystem(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("DMSInfo",dmsService.getBasicInfoOfSystem());
//        System.out.println(dmsService.getBasicInfoOfSystem().toString());
        return "SystemSetting";
    }

    /**
     * 活泉前端传送的json对象，更新论坛的基本信息
     * @param ObjectDMS
     * @return dao结果
     */
    @ResponseBody
    @RequestMapping("/updateBasicInfoOfSystem")
    public Object updateBasicInfoOfSystem(@RequestParam String ObjectDMS){
        JSONObject dmsObject = JSONObject.fromObject(ObjectDMS);
        DMS DMS = (DMS) dmsObject.toBean(dmsObject,DMS.class);
//        System.out.println(DMS.toString());
        boolean result = dmsService.updateBasicInfoOfSystem(DMS);
//        System.out.println(result);
        if(result == true){
            System.out.println("success");
            return "success";
        }
        else {
            System.out.println("fail");
            return "fail";
        }
    }
}
