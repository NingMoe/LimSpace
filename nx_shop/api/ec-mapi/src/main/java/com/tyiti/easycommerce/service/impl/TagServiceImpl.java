package com.tyiti.easycommerce.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.SkuShelvesSchedule;
import com.tyiti.easycommerce.entity.Tag;
import com.tyiti.easycommerce.entity.TagSku;
import com.tyiti.easycommerce.repository.SkuDao;
import com.tyiti.easycommerce.repository.SkuShelvesScheduleDao;
import com.tyiti.easycommerce.repository.TagDao;
import com.tyiti.easycommerce.repository.TagSkuDao;
import com.tyiti.easycommerce.service.TagService;
import com.tyiti.easycommerce.util.exception.CommonException;

@Service("tagService")
public class TagServiceImpl implements TagService {

	@Autowired
	private TagDao tagDao;
	@Autowired
	private TagSkuDao tagSkuDao;
	@Autowired
	private SkuShelvesScheduleDao sssDao;
	@Autowired
	private SkuDao skuDao;

	@Override
	public Map<String, Object> add(Tag tag) {
		Map<String, Object> map = new HashMap<String, Object>();
		String code = tag.getCode();
		int codeNum = tagDao.selectCodeCount(code);
		if (codeNum >= 1) {
			map.put("code", "400");
			map.put("message", "标签代码已存在：" + code);
			return map;
		}

		if (tag.getParentId() == null) {
			map.put("code", "400");
			map.put("message", "父节点为空");
		} else {
			if (tag.getParentId() != 0
					&& tagDao.selectByPrimaryKey(tag.getParentId()) == null) {
				map.put("code", "400");
				map.put("message", "父节点不存在");
				return map;
			}
			tag.setRank(tagDao.getMaxRank(tag.getParentId()) + 1);
			int affectedRows = tagDao.insertSelective(tag);
			if (affectedRows == 1) {
				map.put("code", "200");
				map.put("message", "OK");
			} else {
				map.put("code", "500");
				map.put("message", "插入数据库失败");
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> edit(Tag tag) {
		Map<String, Object> map = new HashMap<String, Object>();

		String code = tag.getCode();
		Integer id = tag.getId();
		int codeNum = tagDao.selectCodeNum(id, code);
		if (codeNum >= 1) {
			map.put("code", "400");
			map.put("message", "标签代码已存在：" + code);
			return map;
		}
		if (tag.getParentId() == null || tag.getId() == null) {
			map.put("code", "400");
			map.put("message", "节点为空");
		} else {
			int num = tagDao.updateByPrimaryKeySelective(tag);
			if (num == 1) {
				map.put("code", "200");
				map.put("message", "Ok");
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> del(Tag tag) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (tag.getParentId() == null || tag.getId() == null) {
			map.put("code", "400");
			map.put("message", "节点为空");
		} else {
			int num = tagDao.updateByPrimaryKeyDel(tag);
			if (num == 1) {
				map.put("code", "200");
				map.put("message", "Ok");
			}
		}
		return map;
	}

	@Override
	public boolean invalidateById(Integer id) {
		Tag tag = new Tag();
		tag.setId(id);
		tag.setInvalid(1);
		int affecedRows = tagDao.updateByPrimaryKeySelective(tag);
		return affecedRows > 0;
	}

	@Override
	@Transactional
	public Map<String, Object> rank(Tag tag) {
		Map<String, Object> map = new HashMap<String, Object>();
		String[] ids = tag.getIds().split(",");
		String[] ranks = tag.getRanks().split(",");
		if (ids.length != ranks.length) {
			map.put("code", "200");
			map.put("message", "排列序号不允许为空");
		} else {
			int num = 0;
			for (int i = 0; i < ids.length; i++) {
				Tag tagRank = new Tag();
				tagRank.setId(Integer.parseInt(ids[i]));
				tagRank.setRank(Integer.parseInt(ranks[i]));
				num += tagDao.updateByPrimaryKeySelective(tagRank);
			}
			map.put("code", "200");
			map.put("message", "排序" + num + "条");
		}
		return map;
	}

	@Override
	public List<Tag> getTagList(int parentId) {
		// TODO Auto-generated method stub

		return tagDao.getTagByParentId(parentId);
	}

	@Override
	public List<Tag> getAllTags() {
		// TODO Auto-generated method stub
		return tagDao.getAllTags();
	}

	@Override
	@Transactional
	public void saveTagSkus(Integer id, List<Integer> skuIds) {
		// 获取当前关联的 skuId 列表
		List<TagSku> tagSkus = tagSkuDao.getByTagId(id);

		// 2层循环匹配，标记哪些是新增的，哪些是更新的
		List<TagSku> inserts = new LinkedList<TagSku>();
		List<TagSku> updates = new LinkedList<TagSku>();
		Map<Integer, Boolean> matched = new HashMap<Integer, Boolean>();
		for (TagSku tagSku : tagSkus) {
			Integer oldSkuId = tagSku.getSkuId();
			int index = 0;
			for (Integer skuId : skuIds) {
				if (skuId == oldSkuId) {
					tagSku.setRank(index + 1);
					updates.add(tagSku);
					matched.put(skuId, Boolean.TRUE);
				}
				index++;
			}
		}

		int index = 0;
		for (Integer skuId : skuIds) {
			TagSku tagSku = new TagSku();
			tagSku.setTagId(id);
			tagSku.setSkuId(skuId);
			tagSku.setRank(index + 1);
			// 新增
			if (!matched.containsKey(skuId)) {
				inserts.add(tagSku);
			}
			index++;
		}

		// 全部删除
		if (tagSkus.size() > 0) {
			tagSkuDao.invalidateByTagId(id);
		}

		// 更新
		if (updates.size() > 0) {
			tagSkuDao.updateRows(updates);
		}

		// 新增
		if (inserts.size() > 0) {
			tagSkuDao.insertRows(inserts);
		}
	}

	@Override
	public void rank(int id, Integer rank) {
		// rank 排序到哪个位置
		Tag tag = tagDao.selectByPrimaryKey(id);
		Integer tagRank = tag.getRank();
		Integer parentId = tag.getParentId();
		if (tagRank == rank) {
			return;
		}

		if (rank < tagRank) {
			// 向上排序
			try {
				int other = tagDao.updateSortOtherUp(tagRank, parentId, rank);
				if (other != 0) {
					tag.setRank(rank);
					int own = tagDao.updateSortOwn(tag);
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
				int other = tagDao.updateSortOtherDown(tagRank, parentId, rank);
				if (other != 0) {
					tag.setRank(rank);
					int own = tagDao.updateSortOwn(tag);
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

	@Override
	public void toggleStatus(Integer id) {
		tagDao.toggleStatus(id);
	}

	@Override
	public List<Sku> getSkusByTag(int id) {
		return tagDao.getSkusByTag(id);
	}

	@Override
	public Tag getById(int id) {
		return tagDao.selectByPrimaryKey(id);
	}

	/**
	 * 按父标签id和层级、商品条数获取标签树及商品列表
	 * 
	 * @param id
	 * @param level
	 * @param limit
	 * @return
	 */
	public Map<String, Object> getSkuListByParentId(Integer id, Integer level,
			Integer limit) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<Tag> list = tagDao.getSkuListByParentId(id, level, limit);

		Integer lastDepth = 0;
		Integer lastParentId = 0;
		Map<String, List<Tag>> tagMap = new HashMap<String, List<Tag>>();
		List<Tag> tags = null;
		Tag tag = null;

		for (int i = list.size() - 1; i >= 0; i--) {
			tag = list.get(i);
			if (tag.getDepth() != lastDepth) {
				if (lastDepth != 0 && tag.getDepth() < lastDepth) {
					tags = tagMap.get(lastDepth + "_" + lastParentId);
					Collections.reverse(tags);
					tag.setChildrens(tags);
				}
				if (tagMap
						.containsKey(tag.getDepth() + "_" + tag.getParentId())) {
					tags = tagMap.get(tag.getDepth() + "_" + tag.getParentId());
				} else {
					tags = new ArrayList<Tag>();
				}
			}
			if (tag.getSkus() != null && tag.getSkus().size() > limit) {
				tag.setSkus(tag.getSkus().subList(0, limit));
			}
			tags.add(tag);
			lastDepth = tag.getDepth();
			lastParentId = tag.getParentId();
			tagMap.put(tag.getDepth() + "_" + tag.getParentId(), tags);
		}
		if (list != null && list.size() != 0) {
			Collections.reverse(tagMap.get(1 + "_" + id));
			map.put("list", tagMap.get(1 + "_" + id));
		} else {
			map.put("list", tagMap.get(1 + "_" + id));
		}

		tag = null;
		tags = null;
		tagMap = null;

		map.put("code", 200);
		map.put("messsge", "OK");
		return map;
	}

	@Override
	public Integer getTagIdByCode(String code) {
		// TODO Auto-generated method stub
		return tagDao.getTagIdByCode(code);
	}

	/**
	 * 按父标签id和层级、商品条数获取标签树及商品列表 (根据定时上下架设置)
	 * 
	 * @param id
	 * @param level
	 * @param limit
	 * @return
	 */
	public Map<String, Object> getSkuListByParentIdForTimeSku(Integer id,
			Integer level, Integer limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Tag> list = tagDao.getSkuListByParentId(id, level, limit);

		// 获取定时上下架列表
		SkuShelvesSchedule sssm = new SkuShelvesSchedule();
		sssm.setInvalid(0);
		List<SkuShelvesSchedule> sssList = sssDao.getSSSList(sssm);

		Integer lastDepth = 0;
		Integer lastParentId = 0;
		Map<String, List<Tag>> tagMap = new HashMap<String, List<Tag>>();
		List<Tag> tags = null;
		Tag tag = null;
		for (int i = list.size() - 1; i >= 0; i--) {
			// 获取父标签下的第一级
			tag = list.get(i);
			// 判断深度 是否为最后的深度
			if (tag.getDepth() != lastDepth) {
				// 是否存在下一个深度等级
				if (lastDepth != 0 && tag.getDepth() < lastDepth) {
					tags = tagMap.get(lastDepth + "_" + lastParentId);
					Collections.reverse(tags);
					// 排除已设置上下架的商品
					tag.setChildrens(tags);
				}
				if (tagMap
						.containsKey(tag.getDepth() + "_" + tag.getParentId())) {
					tags = tagMap.get(tag.getDepth() + "_" + tag.getParentId());
				} else {
					tags = new ArrayList<Tag>();
				}
			}
			if (tag.getSkus() != null && tag.getSkus().size() > limit) {
				tag.setSkus(tag.getSkus().subList(0, limit));
			}

			// 取出已设置
			Sku sku = null;
			for (int j = tag.getSkus().size() - 1; j >= 0; j--) {
				sku = tag.getSkus().get(j);
				for (SkuShelvesSchedule sss : sssList) {
					if (sku.getId().equals(sss.getSkuId())) {
						tag.getSkus().remove(sku);
					}
				}
			}

			tags.add(tag);
			lastDepth = tag.getDepth();
			lastParentId = tag.getParentId();
			tagMap.put(tag.getDepth() + "_" + tag.getParentId(), tags);
		}
		if (list != null && list.size() != 0) {
			Collections.reverse(tagMap.get(1 + "_" + id));
			map.put("list", tagMap.get(1 + "_" + id));
		} else {
			map.put("list", tagMap.get(1 + "_" + id));
		}
		tag = null;
		tags = null;
		tagMap = null;
		map.put("code", 200);
		map.put("messsge", "OK");
		return map;
	}

	/**
	 * 按父标签id和层级、商品条数获取标签树及商品列表(根据预警设置)
	 * 
	 * @param id
	 * @param level
	 * @param limit
	 * @return
	 */
	public Map<String, Object> getSkuListByParentIdForWarning(Integer id,
			Integer level, Integer limit) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<Tag> list = tagDao.getSkuListByParentId(id, level, limit);
		// 获取已设置预警的sku
		List<Sku> skuList = skuDao.getWarningSku();
		Integer lastDepth = 0;
		Integer lastParentId = 0;
		Map<String, List<Tag>> tagMap = new HashMap<String, List<Tag>>();
		List<Tag> tags = null;
		Tag tag = null;
		for (int i = list.size() - 1; i >= 0; i--) {
			tag = list.get(i);
			if (tag.getDepth() != lastDepth) {
				if (lastDepth != 0 && tag.getDepth() < lastDepth) {
					tags = tagMap.get(lastDepth + "_" + lastParentId);
					Collections.reverse(tags);
					tag.setChildrens(tags);
				}
				if (tagMap
						.containsKey(tag.getDepth() + "_" + tag.getParentId())) {
					tags = tagMap.get(tag.getDepth() + "_" + tag.getParentId());
				} else {
					tags = new ArrayList<Tag>();
				}
			}
			if (tag.getSkus() != null && tag.getSkus().size() > limit) {
				tag.setSkus(tag.getSkus().subList(0, limit));
			}
			// 取出已设置
			Sku sku = null;
			for (int j = tag.getSkus().size() - 1; j >= 0; j--) {
				sku = tag.getSkus().get(j);
				for (Sku sss : skuList) {
					if (sku.getId().equals(sss.getId())) {
						tag.getSkus().remove(sku);
					}
				}
			}
			tags.add(tag);
			lastDepth = tag.getDepth();
			lastParentId = tag.getParentId();
			tagMap.put(tag.getDepth() + "_" + tag.getParentId(), tags);
		}
		if (list != null && list.size() != 0) {
			Collections.reverse(tagMap.get(1 + "_" + id));
			map.put("list", tagMap.get(1 + "_" + id));
		} else {
			map.put("list", tagMap.get(1 + "_" + id));
		}

		tag = null;
		tags = null;
		tagMap = null;

		map.put("code", 200);
		map.put("messsge", "OK");
		return map;
	}

}
