package com.dmcn.two.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "page")
public class PageConfig {
    public  int pageSize;
    public  int pageNum;
    public  boolean reasonable;
    public  boolean toCamelVunderline;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public boolean isReasonable() {
        return reasonable;
    }

    public void setReasonable(boolean reasonable) {
        this.reasonable = reasonable;
    }

    public boolean isToCamelVunderline() {
        return toCamelVunderline;
    }

    public void setToCamelVunderline(boolean toCamelVunderline) {
        this.toCamelVunderline = toCamelVunderline;
    }
}
