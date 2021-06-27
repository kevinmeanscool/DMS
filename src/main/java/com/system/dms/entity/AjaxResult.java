package com.system.dms.entity;

public class AjaxResult {
    private Object resultObj;
    private String message;

    public Object getResultObj() {
        return resultObj;
    }

    public void setResultObj(Object resultObj) {
        this.resultObj = resultObj;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AjaxResult{" +
                "resultObj=" + resultObj +
                ", message='" + message + '\'' +
                '}';
    }
}
