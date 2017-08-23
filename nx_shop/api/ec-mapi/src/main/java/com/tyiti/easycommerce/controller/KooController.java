 /**
  * 文件名[fileName]：AnalysisDataController.java
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430
  * @version: v1.0.0.1
  * 日期：2016年5月9日 下午3:35:36
  * Copyright 【北京天尧信息有限公司所有】 2016 
  */
 
package com.tyiti.easycommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tyiti.easycommerce.entity.KooSku;
import com.tyiti.easycommerce.entity.SkuExt;
import com.tyiti.easycommerce.entity.Spu;
import com.tyiti.easycommerce.entity.Supplier;
import com.tyiti.easycommerce.service.KooService;

/**
  *<p>类描述：。</p>
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430。
  * @version: v1.0.0.1
  * @version: v1.0.0.1。
  * @since JDK1.6。
  *<p>创建日期：2016年5月9日 下午3:35:36。</p>
  */
@Controller
public class KooController {
	
	Logger logger = Logger.getLogger(KooController.class); 
	
	@Autowired
	KooService analysisDataService;

	 /**
	   * <p>功能描述：。</p>	
	   * @throws Exception 
	   * @since JDK1.7。
	   * <p>创建日期:2016年5月10日 下午4:30:37。</p>
	   * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	 @RequestMapping(value = "/kooAnalysisData", method = RequestMethod.GET)
	 @ResponseBody
	 public Map<String, Object>  analysisData(String password) throws Exception{
		 System.out.println("===========================进入定时任务开始======================");
		 Map<String, Object> map =analysisDataService.kooData(password);
		 System.out.println("===========================进入定时任务结束======================");
		 return map;
	 }
	 /***
	   * <p>功能描述：。</p>	
	   * @return
	   * @since JDK1.7。
	   * <p>创建日期:2016年6月3日 上午10:20:24。</p>
	   * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	 @RequestMapping(value = "/findSpus", method = RequestMethod.GET)
	 @ResponseBody
     public Map<String, Object> findSkuManagement(String categoryId,String spuId, String isstatus,String supplerId){
    	 Map<String, Object> map = new HashMap<String, Object>();
    	 Integer category_id =null;
    	 Integer spu_id=null;
    	 Integer suppler_id=null;
    	 Integer status = null;
    	 if(!(StringUtils.isEmpty(categoryId)||categoryId.equals("null"))){
    		 category_id= Integer.parseInt(categoryId);
    	 }
    	 if(!(StringUtils.isEmpty(spuId)||spuId.equals("null"))){
    		 spu_id = Integer.parseInt(spuId);
    	 }
    	 if(!(StringUtils.isEmpty(supplerId)||supplerId.equals("null"))){
    		 suppler_id = Integer.parseInt(supplerId);
    	 }
    	 if(!(StringUtils.isEmpty(isstatus)||supplerId.equals("null"))){
    		 status = Integer.parseInt(isstatus);
    	 }
    	 List<Spu> spulist = analysisDataService.findSpus(category_id,spu_id,suppler_id,status);
    	 map.put("data", spulist);
    	 return map;
     }
     /***
       * <p>功能描述：根据查询条件查询筛选SKU信息。</p>	
       * @return
       * @since JDK1.7。
       * <p>创建日期:2016年6月13日 上午11:17:28。</p>
       * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
      */
	 @RequestMapping(value = "/findSkuList", method = RequestMethod.GET)
	 @ResponseBody
     public Map<String, Object> findSkuList(String categoryId,String spuId, String isstatus,String supplerId){
    	 Map<String, Object> map = new HashMap<String, Object>();
    	 Integer category_id =null;
    	 Integer spu_id=null;
    	 Integer suppler_id=null;
    	 Integer status = null;
    	 if(!(StringUtils.isEmpty(categoryId)||categoryId.equals("null"))){
    		 category_id= Integer.parseInt(categoryId);
    	 }
    	 if(!(StringUtils.isEmpty(spuId)||spuId.equals("null"))){
    		 spu_id = Integer.parseInt(spuId);
    	 }
    	 if(!(StringUtils.isEmpty(supplerId)||supplerId.equals("null"))){
    		 suppler_id = Integer.parseInt(supplerId);
    	 }
    	 if(!(StringUtils.isEmpty(isstatus)||isstatus.equals("null"))){
    		 status = Integer.parseInt(isstatus);
    	 }
    	 List<KooSku> kooSkulist = analysisDataService.findSkuList(category_id,spu_id,suppler_id,status);
    	 map.put("data", kooSkulist);
    	 return map;
     }
	 /***
	   * <p>功能描述：查询所有供货商的信息。</p>	
	   * @return
	   * @since JDK1.7。
	   * <p>创建日期:2016年6月14日 下午5:34:54。</p>
	   * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	 @RequestMapping(value="findListSupllierList")
	 @ResponseBody
	public Map<String,Object> findListSupllierList(){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Supplier> supplierlist = analysisDataService.findListSupllierList();
		map.put("data", supplierlist);
		return map;
	}
	 /***
	   * <p>功能描述：。</p>	
	   * @param Id
	   * @return
	   * @since JDK1.7。
	   * <p>创建日期:2016年6月20日 下午2:35:20。</p>
	   * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	 @RequestMapping(value="findSkuById", method = RequestMethod.POST)
	 @ResponseBody
    public Map<String,Object>findSkuById(@Param("id")Integer id){
		Map<String, Object> map = new HashMap<String, Object>();
		KooSku kooSku = analysisDataService.findSkuById(id);
		SkuExt skuExt = analysisDataService.findSkuExtById(id);
		map.put("kooSku", kooSku);
		map.put("skuExt", skuExt);
		return map;
    }
	 /***
	   * <p>功能描述：保存信息Sku信息。</p>	
	   * @param kooSku
	   * @param skuExt
	   * @return
	   * @since JDK1.7。
	   * <p>创建日期:2016年6月20日 下午3:19:22。</p>
	   * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	 @RequestMapping(value="updateKooSku", method = RequestMethod.POST,headers = {"Content-type=application/json"})
	 @ResponseBody
	public Map<String,Object>updateKooSku(@RequestBody KooSku kooSku){
		Map<String, Object> map = new HashMap<String, Object>();
		map = analysisDataService.updateKooSku(kooSku);
//		map = analysisDataService.updateSkuExt(skuExt);
		return map;
		
	}
	 /***
	   * <p>功能描述：保存信息Sku信息。</p>	
	   * @param kooSku
	   * @param skuExt
	   * @return
	   * @since JDK1.7。
	   * <p>创建日期:2016年6月20日 下午3:19:22。</p>
	   * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	 @RequestMapping(value="updateSkuExt", method = RequestMethod.POST,headers = {"Content-type=application/json"})
	 @ResponseBody
	 public Map<String,Object>updateSkuExt(@RequestBody SkuExt skuExt){
		Map<String, Object> map = new HashMap<String, Object>();
		map = analysisDataService.updateSkuExt(skuExt);
		return map;
		
	}

}
