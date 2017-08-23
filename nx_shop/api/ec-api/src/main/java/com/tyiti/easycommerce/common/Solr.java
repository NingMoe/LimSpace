package com.tyiti.easycommerce.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

public class Solr {
	
	private static SolrServer server;
	public Solr(String solr_Url){
		server = new HttpSolrServer(solr_Url);
	}
    public List<Integer> GetSkuIdByInput(String input,String sortField,String sorttype) 
    {
    	List<Integer> li = new ArrayList<Integer>() ;
    	
        SolrQuery params = new SolrQuery();
        
        // 查询关键词，*:*代表所有属性、所有值，即所有index
        params.set("q", "name:"+input);
        
        // 分页，start=0就是从0开始，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
        params.set("start", 0);
        params.set("rows", Integer.MAX_VALUE);
            
        // 排序，如果按照id排序，那么将score desc 改成 id desc(or asc)
        // params.set("sort", "score desc");
        
        //若参数sortField的值为id，则不传
        if(!"id".equals(sortField)){
        	params.set("sort", sortField+" "+sorttype);
        }
        // 返回信息*为全部，这里是全部加上score，如果不加下面就不能使用score
        params.set("fl", "id");
        params.set("wt", "json");
        
        QueryResponse response = null;
        try {
            response = server.query(params);
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        
        SolrDocumentList docs = response.getResults();  
        for (SolrDocument sd : docs) {  
        	li.add(Integer.parseInt(sd.getFieldValue("id").toString()));
        } 
        return li ;
    }
}