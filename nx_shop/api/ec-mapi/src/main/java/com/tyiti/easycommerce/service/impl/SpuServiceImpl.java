package com.tyiti.easycommerce.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.SkuAttribute;
import com.tyiti.easycommerce.entity.Spu;
import com.tyiti.easycommerce.entity.SpuAttribute;
import com.tyiti.easycommerce.entity.SpuSpec;
import com.tyiti.easycommerce.repository.CategoryDao;
import com.tyiti.easycommerce.repository.SkuAttributeDao;
import com.tyiti.easycommerce.repository.SkuDao;
import com.tyiti.easycommerce.repository.SpuAttributeDao;
import com.tyiti.easycommerce.repository.SpuDao;
import com.tyiti.easycommerce.repository.SpuSpecDao;
import com.tyiti.easycommerce.service.SpuService;
import com.tyiti.easycommerce.util.exception.CommonException;

@Service
public class SpuServiceImpl implements SpuService {
	@Autowired
	private SpuDao spuDao;
	@Autowired
	private SpuAttributeDao spuAttributeDao;

	@Autowired
	private SkuDao skuDao;

	@Autowired
	private SkuAttributeDao skuAttributeDao;

	@Autowired
	private SpuSpecDao spuSpecDao;

	@Autowired
	private CategoryDao categoryDao;

	@Override
	public Spu getById(Integer id) {
		Spu spu = spuDao.selectByPrimaryKey(id);
		// spu 属性
		List<SpuAttribute> spuAttributeList = spuAttributeDao
				.getListBySpuId(id);
		spu.setSpuAttributeList(spuAttributeList);
		List<SpuSpec> spuSpecList = spuSpecDao.getListBySpuId(id);
		spu.setSpuSpecList(spuSpecList);
		List<Sku> skuAttributList = new ArrayList<Sku>();// sku 及属性
		List<Sku> skuList = skuDao.getSkuBySpuId(id); // sku 列表
		for (Sku sku : skuList) {
			int skuId = sku.getId();
			List<SkuAttribute> skuAttribute = skuAttributeDao
					.getListBySkuId(skuId);
			sku.setSkuAttributeList(skuAttribute); // sku 属性
			skuAttributList.add(sku);
		}
		spu.setSkuList(skuAttributList);
		return spu;
	}

	@Override
	@Transactional
	public void update(Spu spu) {
		int spuId = spu.getId();
		Spu spuDb = spuDao.selectByPrimaryKey(spuId);
		if (spuDb == null) {
			new CommonException("spu id 不存在");
		}
		// 1.spuSpec 维护
		// spu_spec
		for (SpuSpec spuSpec : spu.getSpuSpecList()) {
			SpuSpec spuSpecDb = spuSpecDao.getBySpecId(spuId,
					spuSpec.getSpecId());
			spuSpec.setId(spuSpecDb.getId());
			spuSpec.setSpuId(spuId);
			spuSpecDao.updateByPrimaryKeySelective(spuSpec);
		}

		Map<String, Object> params = new HashMap<String, Object>();
		int[] spuAttributeIds = new int[spu.getSpuAttributeList().size()]; // 去数据库查
																			// 保留存在的sku_attribute
		int i = 0;
		Map<Integer, Integer> spuAttributeIdMap = new HashMap<Integer, Integer>();
		// 2.spuAttribute 维护
		for (SpuAttribute spuAttribute : spu.getSpuAttributeList()) {
			SpuAttribute spuAttributeDb = spuAttributeDao.getByAttributeId(
					spuId, spuAttribute.getAttributeId());
			spuAttribute.setId(spuAttributeDb.getId());
			spuAttributeDao.updateByPrimaryKeySelective(spuAttribute);
			// spuAttributeIdMap.put(virtualId, spuAttributeDb.getId());
			spuAttributeIdMap.put(spuAttribute.getAttributeId(),
					spuAttributeDb.getId());
			spuAttributeIds[i] = spuAttributeDb.getId();
			i++;
		}
		params.put("ids", spuAttributeIds);
		int[] skuIds = new int[spu.getSkuList().size()];
		int z = 0;
		// 3.sku 字段处理 此为多条记录 spu_id name description detail erp_code
		// original_price price
		for (Sku sku : spu.getSkuList()) {
			// 反向操作， 先去sku attribute 看看 是否存在sku 如果存在 则 拿到sku id 如果不存在 新增
			String skuAttributes = "";
			int j = 0;
			for (SkuAttribute skuAttribute : sku.getSkuAttributeList()) {
				if (j > 0) {
					skuAttributes += ",";
				}
				skuAttributes = skuAttributes
						+ skuAttribute.getAttributeValue();
				j++;
			}
			params.put("skuAttributes", skuAttributes);
			// int skuId = skuAttributeDao.getSkuIdByMap(params);
			Integer skuId = sku.getId();
			sku.setSpuId(spuId);
			if (skuId != null) {
				sku.setUpdateTime(new Date());
				skuDao.updateByPrimaryKeySelective(sku);
			} else {
				sku.setCreateTime(new Date());
				skuDao.insertSelective(sku);
				skuId = sku.getId();
				// 4.sku_attribute 多条处理 sku_id spu_attribute_id attribute_value
				for (SkuAttribute skuAttribute : sku.getSkuAttributeList()) {
					skuAttribute.setSkuId(skuId);
					skuAttribute.setSpuAttributeId(spuAttributeIdMap
							.get(skuAttribute.getAttribute().getId()));
					skuAttributeDao.add(skuAttribute);
				}
			}
			skuIds[z] = skuId;
			z++;
		}
		// 删除sku 操作
		if(skuIds.length ==0){
			skuDao.deleteAllBySpu(spuId);
		}else{
			skuDao.deleteNotInByIds(skuIds, spuId);
		}
		
		spu.setUpdateTime(new Date());
		spuDao.updateByPrimaryKeySelective(spu);
	}

	@Override
	public SearchResult<Map<String, Object>> getByParmas(
			Map<String, Object> param) {
		// TODO Auto-generated method stub
		SearchResult<Map<String, Object>> result = new SearchResult<Map<String, Object>>();
		if (param.get("limit") != null && param.get("limit") != "") {
			param.put("limit",
					Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if (param.get("offset") != null && param.get("offset") != "") {
			param.put("offset",
					Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		result.setRows(spuDao.selectListByParams(param));
		result.setTotal(spuDao.selectCountByParams(param));
		return result;
	}

	@Override
	public void del(int id) {
		// TODO Auto-generated method stub
		Spu spu = new Spu();
		spu.setId(id);
		spu.setInvalid(1);// 设置无效
		skuDao.updateDeleteBySpuId(id);
		spuDao.updateByPrimaryKeySelective(spu);
	}

	@Override
	@Transactional
	public void add(Spu spu) {
		// installment head_thumbnail is_default create_time inventory

		// 1.spu 字段 处理 此为单条记录 categoryId , name ,sub_name,thumbnail ,description
		// ,mobile_description
		if (spu.getCategoryId() == null) {
			throw new CommonException("分类Id 为空");
		}
		spu.setCreateTime(new Date());
		if (spu.getRank() == null) {
			int rank = spuDao.getMaxRankByCategoryId(spu.getCategoryId()) + 1;
			spu.setRank(rank);
		}
		// 得到主键值
		spuDao.insertSelective(spu);
		int spuId = spu.getId();
		// System.out.println(spuId+"spuId");
		// 2.spu_attribute 字段处理 多条记录 spuId attributeId value_constraint_type
		// value_constraint_expression
		// spuAttributeIdMap 为 spuAttribute 虚拟的id 存储 用于spuAttribute
		// spu_attribute_id 关联
		Map<Integer, Integer> spuAttributeIdMap = new HashMap<Integer, Integer>();

		for (SpuAttribute spuAttribute : spu.getSpuAttributeList()) {
			spuAttribute.setSpuId(spuId);
			spuAttributeDao.insertSelective(spuAttribute);
			// 将虚拟的id 赋值数据库的id
			int spuAttributeId = spuAttribute.getId();

			spuAttributeIdMap
					.put(spuAttribute.getAttributeId(), spuAttributeId);
			// System.out.println("spuAttributeId="+spuAttributeId);

		}
		// spu_spec
		for (SpuSpec spuSpec : spu.getSpuSpecList()) {
			spuSpec.setSpuId(spuId);
			spuSpecDao.insertSelective(spuSpec);
		}
		// 3.sku 字段处理 此为多条记录 spu_id name description detail erp_code
		// original_price price
		for (Sku sku : spu.getSkuList()) {
			sku.setSpuId(spuId);
			sku.setCreateTime(new Date());
			skuDao.insertSelective(sku);
			int skuId = sku.getId();
			// System.out.println("skuId="+skuId);
			// 4.sku_attribute 多条处理 sku_id spu_attribute_id attribute_value
			for (SkuAttribute skuAttribute : sku.getSkuAttributeList()) {
				skuAttribute.setSkuId(skuId);
				skuAttribute.setSpuAttributeId(spuAttributeIdMap
						.get(skuAttribute.getAttribute().getId()));
				skuAttributeDao.add(skuAttribute);
			}
		}
	}

	@Override
	public SearchResult<Map<String, Object>> search(Map<String, Object> param) {
		if (param.get("limit") != null && param.get("limit") != "") {
			param.put("limit",
					Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if (param.get("offset") != null && param.get("offset") != "") {
			param.put("offset",
					Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		if (param.get("categoryId") != null) {
			int categoryId = Integer.parseInt(String.valueOf(param
					.get("categoryId")));
			// 获取树结构节点及所有子节点id
			String categoryIds = categoryDao.getIdsByCategoryId(categoryId);
			param.put("categoryIds", categoryIds.split(","));
		}
		SearchResult<Map<String, Object>> searchResult = new SearchResult<Map<String, Object>>();
		searchResult.setRows(spuDao.selectSpuList(param));
		searchResult.setTotal(spuDao.selectSpuCount(param));
		return searchResult;
	}

	@Override
	@Transactional
	public void rank(int id, Integer rank) {
		// rank 排序到哪个位置
		Spu spuOwn = spuDao.selectByPrimaryKey(id);
		if (spuOwn.getRank() == rank) {
			return;
		} else if (rank < spuOwn.getRank()) {
			// 向上排序
			try {
				int other = spuDao.updateSortOtherUp(spuOwn.getRank(),
						spuOwn.getCategoryId(), rank);
				if (other != 0) {
					spuOwn.setRank(rank);
					int own = spuDao.updateSortOwn(spuOwn);
					if (own != 1) {
						throw new CommonException("排序不成功");
					}
				} else {
					throw new CommonException("排序不成功");
				}
			} catch (Exception e) {
				throw new CommonException("排序不成功");
			}
		} else {
			try {
				// 向下排序
				int other = spuDao.updateSortOtherDown(spuOwn.getRank(),
						spuOwn.getCategoryId(), rank);
				if (other != 0) {
					spuOwn.setRank(rank);
					int own = spuDao.updateSortOwn(spuOwn);
					if (own != 1) {
						throw new CommonException("排序不成功");
					}
				} else {
					throw new CommonException("排序不成功");
				}
			} catch (Exception e) {
				throw new CommonException("排序不成功");
			}
		}
	}
}
