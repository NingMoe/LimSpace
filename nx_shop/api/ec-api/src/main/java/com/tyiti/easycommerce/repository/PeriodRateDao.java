package com.tyiti.easycommerce.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import com.tyiti.easycommerce.entity.PeriodRate;

@Component("periodRateDao")
public interface PeriodRateDao {
    int deleteByPrimaryKey(Integer id);

    int insert(PeriodRate record);

    int insertSelective(PeriodRate record);

    PeriodRate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PeriodRate record);

    int updateByPrimaryKey(PeriodRate record);

	List<PeriodRate> selectList();
}