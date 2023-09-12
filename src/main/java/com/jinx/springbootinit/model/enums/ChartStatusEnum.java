package com.jinx.springbootinit.model.enums;

public enum ChartStatusEnum {
    WAIT("wait"),
    SUCCESS("succeed"),
    FAIL("failed"),
    Running("running"),
    ;
    private final String status;

    ChartStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
