package com.tyiti.easycommerce.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.common.LogEnum;
import com.tyiti.easycommerce.entity.Coupon;
import com.tyiti.easycommerce.entity.CouponRecord;
import com.tyiti.easycommerce.repository.CouponDao;
import com.tyiti.easycommerce.repository.CouponRecordDao;
import com.tyiti.easycommerce.service.CouponService;
import com.tyiti.easycommerce.util.LogUtil;
import com.tyiti.easycommerce.util.exception.CommonException;
@Scope("prototype")
@Service("couponInfoService")
public class CouponServiceImpl implements CouponService {

	@Autowired
	private CouponDao couponDao;

	@Autowired
	private CouponRecordDao couponRecordDao;

	private Map<String,String> spreadCodeSet = new HashMap<String,String>();

	private List<CouponRecord> recordList = new ArrayList<CouponRecord>();

	@Override
	@Transactional
	public void addCoupon(Coupon coupon) {
		Date now = new Date();
		if(coupon.getReceiveStartTime().getTime()<now.getTime()){
			throw new CommonException("活动领取开始时间不能小于当前时间");
		}
		if(coupon.getReceiveEndTime().getTime()<now.getTime()){
			throw new CommonException("活动领取结束时间不能小于当前时间");
		}
		if(coupon.getReceiveStartTime().getTime()>coupon.getReceiveEndTime().getTime()){
			throw new CommonException("活动领取开始时间需小于领取结束时间");
		}
		// 判断一下 相对时间和绝对时间设置是否冲突
		if (coupon.getTimeType() == 1) {
			if (coupon.getStartTime() == null) {
				throw new CommonException("有效期开始时间不能为空");
			}
			if (coupon.getExpireTime() == null) {
				throw new CommonException("失效时间不能为空");
			}
			if(coupon.getStartTime().getTime()>=coupon.getExpireTime().getTime()){
				throw new CommonException("开始时间必须小于结束时间");
			}
			if (coupon.getExpireInDays() != null) {
				//throw new CommonException("绝对时间与有效天数冲突");
				coupon.setExpireInDays(null);
			}
		}
		if(coupon.getDiscount() == null){
			throw new CommonException("优惠金额不能为空");
		}
		if(coupon.getThreshold() != null){
			if(coupon.getThreshold()<=coupon.getDiscount()){
				throw new CommonException("优惠金额必须大于满减金额");
			}
		}
		if (coupon.getTimeType() == 2) {
			if (coupon.getExpireInDays() == null) {
				throw new CommonException("有效天数不能为空");
			}
			if (coupon.getStartTime() != null) {
				coupon.setStartTime(null);
//				throw new CommonException("相对时间与结束时间冲突");
			}
			if (coupon.getExpireTime() != null) {
				coupon.setExpireTime(null);
//				throw new CommonException("相对时间与结束时间冲突");
			}
		}
		int prefixNum = 0;
		String prefix = "";
		if (coupon.getType() == 3 || coupon.getType() == 4) {
			if (coupon.getScope() != 1) {
				throw new CommonException("代金券和代金码必须是全场通用的");
			}
		}
		if(coupon.getType() == 2 || coupon.getType() == 4){
			do {
				// 生成两位随机码
				prefix = createCouponCode(2);
				prefixNum = couponDao.selectPrefixNum(prefix);
			} while (prefixNum > 0);
			// 判断一下 优惠类型和 使用范围是否冲突

			coupon.setPrefix(prefix);
		}
		coupon.setCreateTime(new Date());
		couponDao.insertSelective(coupon);
		if (coupon.getType() == 2 || coupon.getType() == 4) {
			// 批量生成优惠码
			for (int i = 0; i < coupon.getCount(); i++) {
				String code = "";
				
				do {
					// 生成两位随机码
					code = createCouponCode(6);
					if (!spreadCodeSet.containsKey(code)){
						spreadCodeSet.put(code, code);
						CouponRecord couponRecord = new CouponRecord();
						couponRecord.setCouponId(coupon.getId());
						couponRecord.setCouponCode(prefix + code);
						if(coupon.getTimeType() ==1 ){
							couponRecord.setExpireTime(coupon.getExpireTime());
						}
						recordList.add(couponRecord);
					}
				} while (!spreadCodeSet.containsKey(code));
				 
			}
			couponRecordDao.insertBatchRecord(recordList);
		}
		LogUtil.log(coupon.getId(),
				LogEnum.OperateModel.COUPON.getKey(),
				LogEnum.Action.ADD.getKey(), "新增优惠券",
				LogEnum.Source.PLAT.getKey(), 1);
	}

	// 生成码
	private String createCouponCode(int n) {
		String code = "";
		Random random = new Random();
		for (int i = 0; i < n; i++) {// 输出字母还是数字
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 字符串
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 取得大写字母还是小写字母
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
				code += Character.toLowerCase((char) (choice + random
						.nextInt(26)));
			} else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
				code += String.valueOf(random.nextInt(10));
			}
		}

		return code;
	}

	@Override
	public void updateCoupon(Coupon coupon) {
		Coupon couponDB = couponDao.selectByPrimaryKey(coupon.getId());
		if (couponDB != null) {
			if (couponDB.getCount() > coupon.getCount()) {
				throw new CommonException("优惠数量只能增加");
			}else{
				if(couponDB.getType() == 2 || couponDB.getType() ==4 ){
					throw new CommonException("优惠码或代金码 不能修改");
				}
				couponDB.setCount(coupon.getCount());
			}
		}
		couponDao.updateByPrimaryKeySelective(couponDB);
		LogUtil.log(coupon.getId(),
				LogEnum.OperateModel.COUPON.getKey(),
				LogEnum.Action.UPDATE.getKey(), "修改优惠券",
				LogEnum.Source.PLAT.getKey(), 1);
	}

	@Override
	public void deleteCoupon(Integer id) {
		Coupon couponDB = couponDao.selectByPrimaryKey(id);
		if(couponDB !=null){
			couponDB.setInvalid(1);
		}else{
			throw new CommonException("无此优惠券");
		}
		couponDao.updateByPrimaryKeySelective(couponDB);
		LogUtil.log(id, LogEnum.OperateModel.COUPON.getKey(),
				LogEnum.Action.REMOVE.getKey(), "删除优惠券",
				LogEnum.Source.PLAT.getKey(), 1);
	}

	@Override
	public Coupon getCouponById(int id) {
		return couponDao.selectByPrimaryKey(id);
	}

	@Override
	public SearchResult<Map<String, Object>> queryInfoListByPage(
			Map<String, Object> param) {
		if (param.get("limit") != null && param.get("limit") != "") {
			param.put("limit",
					Integer.parseInt(String.valueOf(param.get("limit"))));
		} 
		if (param.get("offset") != null && param.get("offset") != "") {
			param.put("offset",
					Integer.parseInt(String.valueOf(param.get("offset"))));
		} 
		SearchResult<Map<String, Object>> searchResult = new SearchResult<Map<String, Object>>();
		 
		searchResult.setRows(this.couponDao
				.selectInfoListByPage(param));
		searchResult.setTotal(couponDao.selectInfoListCountByPage(param));
		return searchResult;
	}

	@Override
	public void stopCoupon(Integer id) {
		// TODO Auto-generated method stub
		Coupon couponDB = couponDao.selectByPrimaryKey(id);
		if(couponDB !=null){
			couponDB.setStop(1);
		}else{
			throw new CommonException("无此优惠券");
		}
		couponDao.updateByPrimaryKeySelective(couponDB);
		LogUtil.log(id, LogEnum.OperateModel.COUPON.getKey(),
				LogEnum.Action.STOP.getKey(), "结束优惠券",
				LogEnum.Source.PLAT.getKey(), 1);
	}

}
