 /**
  * 文件名[fileName]：AnalysisDataService.java
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430
  * @version: v1.0.0.1
  * 日期：2016年5月9日 下午3:56:20
  * Copyright 【北京天尧信息有限公司所有】 2016 
  */
 
package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import com.tyiti.easycommerce.entity.KooSku;
import com.tyiti.easycommerce.entity.SkuExt;
import com.tyiti.easycommerce.entity.Spu;
import com.tyiti.easycommerce.entity.Supplier;

/**
  *<p>类描述：。</p>
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430。
  * @version: v1.0.0.1
  * @version: v1.0.0.1。
  * @since JDK1.6。
  *<p>创建日期：2016年5月9日 下午3:56:20。</p>
  */

public interface KooService {
	 /**
	  * <p>功能描述：。</p>	
	  * @param address
	  * @return
	  * @throws Exception 
	  * @since JDK1.7。
	  * <p>创建日期:2016年5月10日 下午4:41:24。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	String getURLData(String address) throws Exception;

	 /**
	  * <p>功能描述：。</p>	
	  * @param xml
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年5月11日 下午4:45:30。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	Map<String, Object> saveSku(String xml);

	 /**
	  * <p>功能描述：。</p>	
	  * @param kooSku
	 * @param spu 
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年6月3日 上午10:27:45。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<Spu> findSpus(Integer category_id, Integer spu_id, Integer suppler_id, Integer isstatus);

	 /**
	  * <p>功能描述：根据查询条件删选信息Spu。</p>	
	  * @param category_id
	  * @param spu_id
	  * @param suppler_id
	  * @param isstatus
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年6月13日 上午11:34:23。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<KooSku> findSkuList(Integer category_id, Integer spu_id, Integer suppler_id, Integer isstatus);

	 /**
	  * <p>功能描述：查询所有的供应商。</p>	
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年6月14日 下午5:43:03。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<Supplier> findListSupllierList();

	 /**
	  * <p>功能描述：通过ID查询KooSku。</p>	
	  * @param id
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年6月20日 下午2:11:56。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	KooSku findSkuById(Integer id);

	 /**
	  * <p>功能描述：通过SKUId 查询扩展表。</p>	
	  * @param id
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年6月20日 下午2:12:06。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	SkuExt findSkuExtById(Integer id);

	 /**
	  * <p>功能描述：。</p>	
	  * @param kooSku
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年6月20日 下午3:21:05。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	Map<String, Object> updateKooSku(KooSku kooSku);

	 /**
	  * <p>功能描述：。</p>	
	  * @param kooSku
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年6月20日 下午3:21:09。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	Map<String, Object> updateSkuExt(SkuExt skuExt);

	 /**
	  * <p>功能描述：。</p>	
	 * @param password 
	  * @return
	 * @throws Exception 
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月13日 上午10:43:31。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	Map<String, Object> kooData(String password) throws Exception;


}
