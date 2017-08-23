package com.tyiti.easycommerce.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tyiti.easycommerce.entity.Category;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.repository.CategoryDao;
import com.tyiti.easycommerce.repository.SpuDao;
import com.tyiti.easycommerce.service.CategoryService;
import com.tyiti.easycommerce.util.exception.CommonException;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryDao categoryDao ;

	@Autowired
	private SpuDao spuDao ;
	@Override
	public List<Category> getAllCategorys() {
		// TODO Auto-generated method stub
		return categoryDao.getAllCategorys();
	}

	@Override
	public void add(Category category) {
		// TODO Auto-generated method stub
		category.setCreateTime(new Date());
		category.setRank(categoryDao.getMaxRank(category.getParentId())+1);
		categoryDao.insertSelective(category);
	}

	@Override
	public void edit(Category category) {
		// TODO Auto-generated method stub
		category.setUpdateTime(new Date());
		categoryDao.updateByPrimaryKeySelective(category);
	}

	@Override
	public void del(Integer id) {
		List<Category> categoryList = categoryDao.getListByParentId(id);
		if(categoryList.size()>0){
			throw new CommonException("分类下有分类不能删除");
		}
		int spuNum = spuDao.selectCountByCategory(id);
		if(spuNum !=0){
			throw new CommonException("分类下面有商品不能删除");
		}
		Category category = categoryDao.selectByPrimaryKey(id);
		// TODO Auto-generated method stub
		category.setInvalid(1);
		categoryDao.updateByPrimaryKeySelective(category);
	}

	@Override
	@Transactional
	public void rank(int id ,int rank) {
		//rank 排序到哪个位置
		Category categoryOwn = categoryDao.selectByPrimaryKey(id);
		if(categoryOwn.getRank() == rank){
			return ;
		} else if(rank<categoryOwn.getRank()){
			//向上排序
			try {
				int other = categoryDao.updateSortOtherUp(categoryOwn.getRank(),categoryOwn.getParentId(),rank);
				if(other !=0){
					categoryOwn.setRank(rank);
					int	own = categoryDao.updateSortOwn(categoryOwn);
					if(own !=1){
						throw new CommonException("排序不成功");
					}
				}else {
					throw new CommonException("排序不成功");
				}
			} catch (Exception e) {
				throw new CommonException("排序不成功");
			}
		}else {
			try {
				//向下排序
				int other = categoryDao.updateSortOtherDown(categoryOwn.getRank(),categoryOwn.getParentId(),rank);
				if(other !=0){
					categoryOwn.setRank(rank);
					int	own = categoryDao.updateSortOwn(categoryOwn);
					if(own !=1){
						throw new CommonException("排序不成功");
					}
				}else {
					throw new CommonException("排序不成功");
				}
			} catch (Exception e) {
				throw new CommonException("排序不成功");
			}
		}
	}

	@Override
	public List<Category> getListByParentId(int parentId) {
		// TODO Auto-generated method stub
		return categoryDao.getListByParentId(parentId);
	}

	@Override
	public Category getCategoryById(int id) {
		// TODO Auto-generated method stub
		return categoryDao.selectByPrimaryKey(id);
	}

	@Override
	public void toggleStatus(Integer id) {
		categoryDao.toggleStatus(id);
	}

	@Override
	public List<Sku> getSkusByCategory(int id, boolean recursive) {
		if (!recursive) {
			return categoryDao.getSkusByCategory(id);
		} else {
//			return categoryDao.getSkusByCategoryRecursive(id);
			return categoryDao.getSkusByCategory(id);
		}
	}

	 /**
	  * <p>功能描述:。</p>	
	  * @param name
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期2016年7月25日 下午4:39:06。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public Category findKooCategoryCode(String name) {
		return categoryDao.findKooCategoryCode(name);
	}

	 /**
	  * <p>功能描述:。</p>	
	  * @param categoryId
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期2016年7月25日 下午5:04:18。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public Category selectByPrimaryKey(Integer categoryId) {
		return categoryDao.selectByPrimaryKey(categoryId);
	}

	 /**
	  * <p>功能描述:。</p>	
	  * @return
	  * <p>创建日期2016年8月29日 下午7:31:36。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	@Override
	public List<Category> getKooAllCategorys() {
		return categoryDao.getKooAllCategorys();
	}
}
