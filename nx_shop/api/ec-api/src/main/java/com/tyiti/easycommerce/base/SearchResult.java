package com.tyiti.easycommerce.base;

import java.io.Serializable;
import java.util.List;

/**
 * 
* @ClassName: SearchResult
* @Description: TODO(查询结果集)
* @author yanzy
* @date 2016-3-23 下午6:40:27
*
* @param <T>
 */
public class SearchResult<T> implements Serializable {

    private static final long serialVersionUID = 6755166005578428162L;

    /**
     * 总记录条数
     */
    private long total;

    /**
     * 查询结果集
     */
    private List<T> rows;
    
    public SearchResult() {
        super();
    }

    public SearchResult(List<T> rows, long total) {
        super();
        this.rows = rows;
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        if (total == 0 && rows != null) {
            return rows.size();
        }
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}