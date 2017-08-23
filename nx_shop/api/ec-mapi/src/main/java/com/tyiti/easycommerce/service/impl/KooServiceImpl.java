 /**
  * 文件名[fileName]：AnalysisDataServiceImpl.java
  * @author 孔垂龙[tangtg] Email:chuilong.kong@xinfenbao.com Tel:15801649430
  * @version: v1.0.0.1
  * 日期：2016年5月9日 下午3:56:34
  * Copyright 【北京天尧信息有限公司所有】 2016 
  */
 
package com.tyiti.easycommerce.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.tyiti.easycommerce.entity.Category;
import com.tyiti.easycommerce.entity.CategoryKoo;
import com.tyiti.easycommerce.entity.KooCourse;
import com.tyiti.easycommerce.entity.KooSku;
import com.tyiti.easycommerce.entity.SkuExt;
import com.tyiti.easycommerce.entity.Spu;
import com.tyiti.easycommerce.entity.Supplier;
import com.tyiti.easycommerce.entity.TagSku;
import com.tyiti.easycommerce.repository.CategoryKooDao;
import com.tyiti.easycommerce.repository.KooCourseDao;
import com.tyiti.easycommerce.repository.KooDao;
import com.tyiti.easycommerce.repository.SkuExtDao;
import com.tyiti.easycommerce.repository.SpuDao;
import com.tyiti.easycommerce.repository.SupplierDao;
import com.tyiti.easycommerce.repository.TagSkuDao;
import com.tyiti.easycommerce.service.CategoryService;
import com.tyiti.easycommerce.service.KooService;
import com.tyiti.easycommerce.service.TagService;

/**
  *<p>类描述：。</p>
  * @version: v1.0.0.1
  * @version: v1.0.0.1。
  * @since JDK1.6。
  *<p>创建日期：2016年5月9日 下午3:56:34。</p>
  */
@Service

public class KooServiceImpl implements KooService  {
	Logger logger = Logger.getLogger(KooServiceImpl.class); 
	@Autowired
	SpuDao spuDao;
	@Autowired
	KooDao skuDao;
	@Autowired
	KooCourseDao kooCourseDao;
	@Autowired
    CategoryService categoryService;
	@Autowired
	TagService tagService;
//	@Autowired
//	TagDao tagDao;
	@Autowired
	TagSkuDao tagSkuDao;
	@Autowired
	CategoryKooDao categoryKooDao;
	@Autowired
	SkuExtDao skuExtDao;
	@Autowired
	SupplierDao supplierDao;
	@Value("${kooDataAddress}")
	private String kooDataAddress;
	
	 /**
	  * <p>功能描述:。</p>	
	  * @param address
	  * @return
	 * @throws Exception 
	  * @since JDK1.7。
	  * <p>创建日期2016年5月10日 下午4:42:46。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	 public String getURLData(String address) throws Exception{
		    URL url =null;
		    StringBuilder sb = new StringBuilder(""); 
		    String xml = null;
		try {
			url = new URL(address);
			HttpURLConnection connection= (HttpURLConnection) url.openConnection(); 
			connection.setRequestProperty("Content-type", "text/html");
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.setRequestProperty("contentType", "UTF-8");
			InputStream is = connection.getInputStream(); 
			BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8")); 
			String line="";
			while ((line = br.readLine()) != null) {  
            	sb.append(line);  
            }
            is.close(); 
            xml = sb.toString();
//            System.out.println(xml);
		} catch (Exception e1) {
		
			e1.printStackTrace();
		} 
		return xml;
	 }

	 /**
	  * <p>功能描述:。</p>	
	  * @param xml
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期2016年5月11日 下午4:47:33。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Transactional
	@Override
	public Map<String, Object> saveSku(String xml) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isEmpty(xml)){
			map.put("code", "400");
			map.put("message", "获取数据失败！");
			return map;
		}
		Spu spu = null;
		KooSku sku = null;
		TagSku tagSku =null;
		SkuExt skuExt = null;
		List<Spu> spuList = new ArrayList<Spu>();
		List<KooSku> kooSkulist = new ArrayList<KooSku>();
		List<String> productIdList = new ArrayList<String>();
		ArrayList<String> productId_list = new ArrayList<String>();
		try {
			Document document = DocumentHelper.parseText(xml);
		    //获取根元素
			Element root = document.getRootElement();  
			 //获取特定的子元素
			List<CategoryKoo>  categoryList = saveCategorys(root);//保存栏目信息
			saveCourses(root);//保存课程信息
			Element  productsNode= root.element("products");
			Iterator  productIterator= productsNode.elementIterator("product");
		   while(productIterator.hasNext()){//产品信息
			     sku = new KooSku();
			     spu = new Spu();
			     skuExt = new SkuExt();
				 Element node = (Element)productIterator.next(); 
				 Iterator  it= node.elementIterator();
				 String productId = "";
				 while(it.hasNext()){  
					 Element e = (Element)it.next();
					     if(e.getName().equals("product_id")){
					    	 productId =e.getText();
					    	 productId_list.add(productId);
					    	 
					     }
					     if(e.getName().equals("product_version")){
					    	 skuExt.setField1(productId+"_"+e.getText());//新东方视频ID
					    	 productId = productId+"_"+e.getText();
					    	 productIdList.add(productId);
					     }
					     if(e.getName().equals("name")){
					    	 sku.setName(e.getText());
					     }
					     if(e.getName().equals("validity_period")){//有效期
					    	 skuExt.setField2(e.getText());//有效期
					     }
					     if(e.getName().equals("total_hour")){//总课时
					    	 skuExt.setField3(e.getText());//存放总课时
					     }
					     if(e.getName().equals("product_teacher")){//授课老师
					    	 skuExt.setField4(e.getText());
					     }
					     if(e.getName().equals("intro")){//课程简介
					    	 skuExt.setField5(e.getText());
					     }
					     if(e.getName().equals("adaptivePeople")){//适用人群
					    	 skuExt.setField6(e.getText());
					     }
					     if(e.getName().equals("studyGoal")){//学习目标盘
					    	 skuExt.setField7(e.getText());
					     }
					     if(e.getName().equals("teachingIntro")){//教材简介
					    	 skuExt.setField8(e.getText());
					     }
					     if(e.getName().equals("description")){//描述
					    	 sku.setDescription(e.getText());;
					     }
					     if(e.getName().equals("detail")){//简介
					    	 sku.setDetail(e.getText());
					     }
					     if(e.getName().equals("price")){//原价
					    	 if(!StringUtils.isEmpty(e.getText())){
					    		 skuExt.setField9(e.getText());//新东方出售价格
//					    		 System.out.println(e.getText()+"====================新东发售价===========================");
					    	 }
					    	 
					     }
					     if(e.getName().equals("retailprice")){//商品价格
					    	 System.out.println(e.getText().equals("null")+"==========="+e.getName()+"=====retailprice====新东发售价==========================="+productId);
					    	 if((!StringUtils.isEmpty(e.getText()))&&(!e.getText().equals("null"))&&(e.getText()!=null)){
					    		 sku.setOriginalPrice(new BigDecimal(e.getText()));
					    	 }
					    	 
					     }
					     if(e.getName().equals("head_thumbnail")){
					    	 sku.setHeadThumbnail(e.getText());
					    	 sku.setImagesOriginal(e.getText());
					     }
					     if(e.getName().equals("category_id")){
					    	 for(int i=0;i<categoryList.size();i++){
					    		 if(Integer.parseInt(e.getText())==categoryList.get(i).getKooCategoryId()){
					    			 spu.setCategoryId(categoryList.get(i).getCategoryId());
					    		 }
					    	 } 
					     }
				    }
				    SkuExt sku_ext = skuExtDao.findKooProductId(1,productId);
				    if(sku_ext==null){
				    	    spu.setName(sku.getName());
						    spu.setCreateTime(new Date());
						    spu.setDescription(sku.getDescription());
						    spu.setSupplierId(1);
						    spu.setRank(0);
						    spuDao.insert(spu);//保存spu
						    sku.setInstallment("1,3,6,12");
						    sku.setErpCode("koo");
						    sku.setIsDefault(false);//0 是 false 1 是true
						    sku.setCreateTime(new Date());
						    sku.setSpuId(spu.getId());
						    sku.setInvalid(0);//默认是9999
						    sku.setPrice(new BigDecimal(0));
						    sku.setInventory(9999);
						    sku.setStatus(0);
						    skuDao.insert(sku);
						    skuExt.setSkuId(sku.getId());
						    skuExt.setType(1);//1 代表新东方
						    skuExtDao.insert(skuExt);//保存扩展信息
						    spuList.add(spu);
						    kooSkulist.add(sku);
						    tagSku = new TagSku();
							tagSku.setSkuId(sku.getId());
							tagSku.setInvalid(0);
							tagSku.setRank(0);
							tagSku.setTagId(spu.getCategoryId());
							tagSku.setCreateTime(new Date());
							tagSkuDao.insert(tagSku);
				    }else{
				    	KooSku koosku =  skuDao.selectByPrimaryKey(sku_ext.getSkuId());//判断
				    	if(koosku!=null){
				    		boolean flag = false;//有字段改变则修改
					    	if(!sku.getName().equals(koosku.getName())){
					    		koosku.setName(sku.getName());
					    		flag=true;
					    	}
//					    	System.out.println(sku_ext.getField9()+"====================商城--新东发售价===="+(new BigDecimal(skuExt.getField9()).compareTo(new BigDecimal(sku_ext.getField9()))!=0)+"======================="+skuExt.getField9());
					    	if(new BigDecimal(skuExt.getField9()).compareTo(new BigDecimal(sku_ext.getField9()))!=0){
					    		sku_ext.setField9(skuExt.getField9());
					    		koosku.setStatus(0);
					    		flag=true;
					    	}
					    	if(sku.getOriginalPrice().compareTo(koosku.getOriginalPrice())!=0){
					    		koosku.setOriginalPrice(koosku.getOriginalPrice());
					    		flag=true;
					    	}
					    	if(!sku.getDetail().equals(koosku.getDetail())){
					    		koosku.setDetail(sku.getDetail());
					    		flag=true;
					    	}
					    	if(koosku.getPrice()!=null){
					    		sku_ext.setField10(koosku.getPrice().toString());//上一次商城定价
					    	}
					    	
					    	if(flag){
					    		skuExtDao.updateByPrimaryKeySelective(sku_ext);
					    		skuDao.updateByPrimaryKeySelective(koosku);
					    	}
				    	}else{
				    		map.put("code", "400");
							map.put("message", "数据不一致请联系管理员!");
							return map;
				    	}
				    }
		     }
		   List<String> list = skuDao.findBySkuProductIdList("koo");
		   List<String> diffrentList = getDiffrentList(list,productIdList);
		   for(String productId_1:diffrentList){
//			   System.out.println(productId+"===========================================================");
			   SkuExt sku_ext = skuExtDao.findKooProductId(1,productId_1);
			   KooSku koosku =  skuDao.selectByPrimaryKey(sku_ext.getSkuId());//判断
			   if(koosku.getStatus()!=0){
				   koosku.setStatus(0);
				   skuDao.updateByPrimaryKeySelective(koosku); 
			   }
			   
		   }
		} catch (DocumentException e) {
			logger.info("解析保存数据失败"+e);
			e.printStackTrace();
			map.put("code", "400");
			map.put("message", "数据保存失败！");
			return map;
		} 
		outPrintlnProduct(productId_list);
		map.put("code", "200");
		map.put("message", "数据保存成功！");
		return map;
	}

	   /**
	  * <p>功能描述：输出重复ID。</p>	
	  * @param productId_list
	  * <p>创建日期:2016年8月26日 下午2:31:37。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	private void outPrintlnProduct(ArrayList<String> alist) {
		  // 记录重复数据
        List<String> cflist = new ArrayList<String>();
        // 复制一个list
        List<String> blist = alist;
        for (int i = 0; i < alist.size(); i++) {
            boolean b = false;
            String mk = "第" + (i + 1) + "条数据和第";
            for (int j = 0; j < blist.size(); j++) {
                // 不和本身比较
                if (j != i) {
                    // 找到相同的值
                    if (alist.get(i).equals(blist.get(j))) {
                        // 不存在重复数据
                        if (!cflist.isEmpty()) {
                            boolean bo = true;
                            // 遍历重复数据集
                            for (int k = 0; k < cflist.size(); k++) {
                                // 取出第k行的重复数据
                                String[] cf = cflist.get(k).toString()
                                        .split("-");
                                // 取出第k行的第二个索引（复制数据的索引+1）
                                int s = Integer.parseInt(cf[1].toString());
                                // 比较当前顺序和重复中的顺序，如果相等则标记为false
                                if (s == (i + 1)) {
                                    bo = false;
                                }
                            }
                            // 判断是否添加到重复数据集中（true表示重复数据集里面不存在该数据，false反之）
                            if (bo) {
                                cflist.add((i + 1) + "-" + (j + 1));
                                if (b) {
                                    mk += ",";
                                }
                                mk += (j + 1);
                                // 记录重复数据时修改b的值，表示和第i行和第j行存在重复数据
                                b = true;
                            }
                        } else {
                            // 记录重复的数据，格式为2-3（第一个数字为原重复数据的索引+1，第二个为复制数据的索引+1）
                            cflist.add((i + 1) + "-" + (j + 1));
                            mk += (j + 1);
                            b = true;
                        }
                    }
                }
            }
            mk += "条数据重复,重复值：" + alist.get(i);
            if (b) {
                System.out.println(mk);
            }
        }
	}

	/**
	  * <p>功能描述：判断两个List不同的元素。</p>	
	  * @param list
	  * @param productIdList
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年7月22日 下午3:12:53。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	private List<String> getDiffrentList(List<String> list, List<String> productIdList) {
		 Map<String,Integer> map = new HashMap<String,Integer>(list.size()+productIdList.size());
         List<String> diff = new ArrayList<String>();
         List<String> maxList = list;
         List<String> minList = productIdList;
         if(list.size()>productIdList.size())
         {
             maxList = productIdList;
             minList = list;
         }
         
         for (String string : maxList) 
         {
            map.put(string, 1);                    
         }
         
         for (String string : minList) 
         {
             Integer cc = map.get(string);                    
             if(cc!=null)                    
             {
                 map.put(string, ++cc);
                 continue;                    
             }           
             map.put(string, 1);                    
         }
         
         for(Map.Entry<String, Integer> entry:map.entrySet())                    
         {                    
             if(entry.getValue()==1)                    
             {                    
                 diff.add(entry.getKey());                    
             }                    
         }                               
         return diff;                    
	}

	/**
	  * <p>功能描述：。</p>	
	  * @param root
	 * @return 
	  * @since JDK1.7。
	  * <p>创建日期:2016年5月27日 上午11:22:23。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	private List<CategoryKoo>  saveCategorys(Element root) {
		Category category = null;
		CategoryKoo categoryKoo =null;
//		Tag tag = null;
		String kooId =null;
		Element  categorysNode= root.element("categorys");
		Iterator  categoryIterator= categorysNode.elementIterator("category");
		List<Category> categorylist = new ArrayList<Category>();
		categorylist = categoryService.getKooAllCategorys();
//		List<Tag> taglist = new ArrayList<Tag>();
//		taglist = tagService.getAllTags();
		Map<String,Integer> categoryMap = new  HashMap<String,Integer>();//存放所有栏目ID
//		Map<String,Integer> tagMap = new  HashMap<String,Integer>();//存放所有栏目ID
//		Integer baseTagId =0;
		Integer basecategoryId =0;
		//存放所有信息
//		Tag catTag = tagDao.findKooCode("cat");
//		if(catTag==null){
//			return null;
//		}
		category = categoryService.findKooCategoryCode("新东方在线教育"); 
		if(category==null){
			category = new Category();
//			tag = new Tag();
			categoryKoo = new CategoryKoo();
			category.setName("新东方在线教育");
			category.setParentId(0);
			category.setInvalid(0);
			category.setCreateTime(new Date());
			category.setSupplierId(1);
			categoryService.add(category);
			Integer newId = category.getId();
			//保存标签
//			tag.setName(category.getName());
//			tag.setParentId(catTag.getId());
//			tag.setCode("koo");
//			tag.setInvalid(0);
//			tag.setIcon(category.getIcon());
//			tag.setRank(1);
//			tagDao.insert(tag);// 添加表情信息
//			Integer tagId = tag.getId();
//			baseTagId =tagId;
			basecategoryId=newId;
		}else{
			basecategoryId=category.getId();
		}
		while(categoryIterator.hasNext()){
			Element node = (Element)categoryIterator.next();
			category = new Category();
//			tag = new Tag();
			categoryKoo = new CategoryKoo();
			Iterator  it= node.elementIterator();
			while(it.hasNext()){
				Element e = (Element)it.next();
				if(e.getName().equals("category_name")){
					kooId = e.attributeValue("id");
					category.setName(e.getText());
				}
				if(e.getName().equals("category_image")){
					category.setIcon(e.getText());
				}
				if(e.getName().equals("parent_id")){
					if(e.getText()!=null&&e.getText()!="null"){
						category.setParentId(Integer.parseInt(e.getText()));
					}
				}
			}
			CategoryKoo oldKooId =categoryKooDao.findKooCategoryById(kooId);
			if(oldKooId==null){
				category.setInvalid(0);
				category.setCreateTime(new Date());
				if(category.getParentId()==null||category.getParentId()==0){
					category.setParentId(basecategoryId);
				}
				category.setSupplierId(1);
				categoryService.add(category);
				Integer newId = category.getId();
				categoryMap.put(kooId, newId);//一个旧的ID对应一个新的ID
				categorylist.add(category);
				categoryKoo.setKooCategoryId(Integer.parseInt(kooId));
				categoryKoo.setCategoryId(newId);
				categoryKoo.setCreatTime(new Date());
				categoryKooDao.insert(categoryKoo);
			}else{
				category = categoryService.selectByPrimaryKey(oldKooId.getCategoryId());
				if(category.getParentId()==null||category.getParentId()==0){
					category.setParentId(basecategoryId);
				}
				categoryService.edit(category);
			}
			
		}
		List<CategoryKoo>   kooList = categoryKooDao.findListKooCategoryList();
		for(int i=0;i<kooList.size();i++){//判断分类，或者是Tag标签是否已经存在并且更新
			CategoryKoo koo =kooList.get(i);
			for(int j=0;j<categorylist.size();j++){
				category = categorylist.get(j);
			     if(koo.getKooCategoryId().equals(category.getParentId())){
			    	 category.setParentId(koo.getCategoryId());
			    	 categoryService.edit(category);
			     }
			}
		}
		return kooList;
	}


	/**
	  * <p>功能描述：保存课程信息。</p>	
	  * @param root
	  * @since JDK1.7。
	  * <p>创建日期:2016年5月13日 上午11:53:50。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	private void saveCourses(Element root) {
		String product_id ="";
//		KooCourse coursesEntity = null;
	    List<KooCourse> coursesList = new ArrayList<KooCourse>();
		Element  categorysNode= root.element("kechenbbiao");
		Iterator<Element>  categoryIterator= categorysNode.elementIterator("product_kechenbbiao");
		while(categoryIterator.hasNext()){
			Element category_e = (Element)categoryIterator.next();
			Iterator  category_it= category_e.elementIterator();
			while(category_it.hasNext()){
				Element product_e = (Element)category_it.next();
				if(product_e.getName().equals("product_id")){//课程ID
					if(!StringUtils.isEmpty(product_e.getText())){
						product_id = product_e.getText();
					}
					 
				}
				if(product_e.getName().equals("service")){
					String service_id  = product_e.attributeValue("id");
					Iterator  service_Iterator= product_e.elementIterator();
					KooCourse courses_service_entity =null;
					while(service_Iterator.hasNext()){
						Element service_e = (Element)service_Iterator.next();
						if(service_e.getName().equals("service_node_name")){
							courses_service_entity = new KooCourse();
							courses_service_entity.setName(service_e.getText());
							courses_service_entity.setKooId(Integer.parseInt(service_id));
							courses_service_entity.setKooProductId(product_id);
							courses_service_entity.setInvalid(true);
						    
						}
						if(service_e.getName().equals("service_node_hour")){
							String service_node_hour = service_e.getText();
							if((!StringUtils.isEmpty(service_node_hour))&&(!"null".equals(service_node_hour))){
								courses_service_entity.setHourLength(service_e.getText());
							}
							
						}
					  }
					if(courses_service_entity!=null){
						
//						KooCourse courses = kooCourseDao.findCourses(courses_service_entity.getKooProductId(),courses_service_entity.getKooId());
						List<KooCourse>   courses = kooCourseDao.findCourses(courses_service_entity.getKooProductId(),courses_service_entity.getKooId());
						if(courses==null||courses.size()==0){
//						if(courses==null){
							coursesList.add(courses_service_entity);
						}
					}
				 }
				 if(product_e.getName().equals("detail")){
					Iterator  detail_Iterator= product_e.elementIterator();
					boolean flag=false;
					KooCourse coursesEntity =null;
					while(detail_Iterator.hasNext()){
						Element detail_node = (Element)detail_Iterator.next();
						if(detail_node.getName().equals("course_id")){
                            if(flag){// 循环
                            	flag =false;
                            	if(coursesEntity!=null){
//            						KooCourse courses = kooCourseDao.findCourses(coursesEntity.getKooProductId(),coursesEntity.getKooId());
            						List<KooCourse>   courses = kooCourseDao.findCourses(coursesEntity.getKooProductId(),coursesEntity.getKooId());
            						if(courses==null||courses.size()==0){
//            						if(courses==null){
            							coursesList.add(coursesEntity);
            						}
            					}
							}
							flag = true;
							coursesEntity = new KooCourse();
							coursesEntity.setKooId(Integer.parseInt(detail_node.getText()));
							
						}
						if(detail_node.getName().equals("course_name")){
							coursesEntity.setName(detail_node.getText());
							coursesEntity.setKooProductId(product_id);
							coursesEntity.setInvalid(false);
						}
						if(detail_node.getName().equals("parent_id")){
							if((!StringUtils.isEmpty(detail_node.getText()))&&(!"null".equals(detail_node.getText()))){
								coursesEntity.setKooParentId(Integer.parseInt(detail_node.getText()));
							}
						}
						if(detail_node.getName().equals("course_hour")){
							if((!StringUtils.isEmpty(detail_node.getText()))&&(!"null".equals(detail_node.getText()))){
								coursesEntity.setHourLength(detail_node.getText());
							}
						}
						if(detail_node.getName().equals("course_teacher")){
							coursesEntity.setCourseTeacher(detail_node.getText());
						}

					 }
				  }
			   }
			}
		if(coursesList!=null&&coursesList.size()>0){
			kooCourseDao.insertRows(coursesList);
		}
		
	}

	 /**
	  * <p>功能描述:。</p>	
	  * @param kooSku
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期2016年6月3日 上午10:28:41。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public List<Spu> findSpus(Integer category_id, Integer spu_id, Integer supplier_id, Integer isstatus) {
		List<Spu> spuSkuList=spuDao.findSpus(category_id,spu_id,supplier_id,isstatus);
		return spuSkuList;
	}

	 /**
	  * <p>功能描述:。</p>	
	  * @param category_id
	  * @param spu_id
	  * @param suppler_id
	  * @param isstatus
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期2016年6月13日 上午11:35:00。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public List<KooSku> findSkuList(Integer category_id, Integer spu_id, Integer supplier_id, Integer isstatus) {
		List<KooSku> kooSkuList=skuDao.findSkuList(category_id,spu_id,supplier_id,isstatus);
		return kooSkuList;
	}

	 /**
	  * <p>功能描述:。</p>	
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期2016年6月14日 下午5:43:34。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public List<Supplier> findListSupllierList() {
		List<Supplier> supplierlist = supplierDao.findListSupllierList();
		return supplierlist;
	}

	 /**
	  * <p>功能描述:。</p>	
	  * @param id
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期2016年6月20日 下午2:13:29。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public KooSku findSkuById(Integer id) {
		KooSku kooSku=skuDao.selectByPrimaryKey(id);
		return kooSku;
	}

	 /**
	  * <p>功能描述:。</p>	
	  * @param id
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期2016年6月20日 下午2:13:29。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public SkuExt findSkuExtById(Integer id) {
		SkuExt skuExt = skuExtDao.findSkuExtBySkuId(id);
		return skuExt;
	}

	 /**
	  * <p>功能描述:。</p>	
	  * @param kooSku
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期2016年6月20日 下午3:21:28。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public Map<String, Object> updateKooSku(KooSku kooSku) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			kooSku.setUpdateTime(new Date());
			skuDao.updateByPrimaryKeySelective(kooSku);
			map.put("code", "200");
			map.put("message", "保存成功！");
		}catch(Exception e){
			e.printStackTrace();
			map.put("code", "400");
			map.put("message", "保存失败！");
		}
		return map;
	}

	 /**
	  * <p>功能描述:。</p>	
	  * @param kooSku
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期2016年6月20日 下午3:21:28。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public Map<String, Object> updateSkuExt(SkuExt skuExt) {
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			skuExtDao.updateByPrimaryKeySelective(skuExt);
			map.put("code", "200");
			map.put("message", "保存成功！");
		}catch(Exception e){
			e.printStackTrace();
			map.put("code", "400");
			map.put("message", "保存失败！");
		}
		return map;
	}

	 /**
	  * <p>功能描述:。</p>	
	  * @return
	 * @throws Exception 
	  * @since JDK1.7。
	  * <p>创建日期2016年7月13日 上午10:43:52。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public Map<String, Object> kooData(String password) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		 if("koo123456!@#".equals(password)){//koo123456!@#
//			 String address = "http://nts.koolearn.com/files/tianyao/data.xml";
			 String kooAddress = kooDataAddress;
//			 String kooAddress = "http://115.182.17.59/files/tianyao/data.xml";
			 if(kooAddress==null||kooAddress==""){
				 logger.info("请求地址不正确");
				 return map;
			 }
			 System.out.println(kooAddress+"============================请求地址==========================");
			 String xml = this.getURLData(kooAddress);
			 //解析XML数据
			 map = this.saveSku(xml); 
		 }
		 return map;
	}

}
