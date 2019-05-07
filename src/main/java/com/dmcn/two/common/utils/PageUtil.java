package com.dmcn.two.common.utils;

import java.util.List;
import java.util.Map;

/**
 * 分页的工具类，它是一个泛型类，T相当于是一个变量。创建该类的对象时可以在尖括号当中设置指定的类型
 * 例如：PageUtil<Product> products = new PageUtil<Product>();
 * @param <T>
 */
public class PageUtil<T> {
    /**
     * 保存当前页的数据
     */
    private List<T> data;

    /**
     * 保存查询条件，键为查询的字段，值为查询的内容。
     */
    Map<String,Object> conditions;

    /**
     * 每一页的大小
     */
    public static final int PAGESIZE = 3;

    /**
     * 当前页
     */
    private int page;

    /**
     * 总页数
     */
    private int pageCount;

    /**
     * 总数据量
     */
    private int dataCount;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageCount() {
        if (dataCount % PAGESIZE == 0){
            return dataCount / PAGESIZE;
        }else {
            return dataCount / PAGESIZE + 1;
        }
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getDataCount() {
        return dataCount;
    }

    public void setDataCount(int dataCount) {
        this.dataCount = dataCount;
    }

    public Map<String, Object> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, Object> conditions) {
        this.conditions = conditions;
    }
}
