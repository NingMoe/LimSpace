package com.tyiti.easycommerce.util.mybatis;

import java.util.List;

/**
 * 建议所有MyBatis数据访问接口要继承的接口
 * @author rainyhao
 * @since 2015-5-20 上午9:31:23
 */
public interface BaseMapper<T> {
	
	/**
	 * 写入一行数据
	 * @author rainyhao
	 * @since 2015-5-20 上午9:54:40
	 * @param entity 要写入的数据内容
	 * @return
	 */
	int insert(T entity);
	
	/**
	 * 按id删除一行数据
	 * @author rainyhao
	 * @since 2015-5-20 上午9:55:04
	 * @param id 主键
	 * @return
	 */
	int delete(Integer id);
	
	/**
	 * 更新一行数据
	 * @author rainyhao
	 * @since 2015-5-20 上午9:55:26
	 * @param entity 要更新的数据
	 * @return
	 */
	int update(T entity);
	
	/**
	 * 用主键查一行数据
	 * @author rainyhao
	 * @since 2015-5-21 下午3:56:43
	 * @param id 主键值
	 * @return
	 */
	T selectByPrimaryKey(Integer id);
	
	/**
	 * 按指定的条件查一行数据
	 * @author rainyhao
	 * @since 2015-5-20 上午9:57:27
	 * @param entity 查询条件
	 * @return
	 */
	T selectForObject(T entity);
	
	/**
	 * 按指定的条件查一个List
	 * @author rainyhao
	 * @since 2015-5-20 上午9:57:51
	 * @param entity 查询条件
	 * @return
	 */
	List<T> selectForList(T entity);
	
	/**
	 * 按指定的条件分布查询的
	 * @author rainyhao
	 * @since 2015-5-20 上午10:00:52
	 * @param entity 查询条件
	 * @return
	 */
	List<T> selectForPagedList(T entity);
	
	/**
	 * 按指定的查询条件查数量
	 * @author rainyhao
	 * @since 2016-1-14 上午10:31:43
	 * @param entity 查询条件
	 * @return
	 */
	int selectCount(T entity);
	
	/**
	 * 
	 * @Title: selectupCount 
	 * @Description: 修改去重
	 * @param id
	 * @param tel
	 * @return  
	 * @return int  
	 * @throws
	 * @author hcy
	 * @date 2016年5月31日 下午3:55:31
	 */
	int selectupCount(T entity);
	
}
