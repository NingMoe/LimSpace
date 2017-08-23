package com.tyiti.easycommerce.repository;

import java.util.List;

import com.tyiti.easycommerce.entity.Supplier;

public interface SupplierDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Supplier record);

    int insertSelective(Supplier record);

    Supplier selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Supplier record);

    int updateByPrimaryKey(Supplier record);

	 /**
	  * <p>功能描述：查询所有供应商信息。</p>	
	  * @return
	  * @since JDK1.7。
	  * <p>创建日期:2016年6月14日 下午5:45:43。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	List<Supplier> findListSupllierList();
}