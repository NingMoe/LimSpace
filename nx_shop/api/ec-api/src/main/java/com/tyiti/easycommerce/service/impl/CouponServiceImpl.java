package com.tyiti.easycommerce.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.entity.Activity;
import com.tyiti.easycommerce.entity.ActivitySku;
import com.tyiti.easycommerce.entity.Coupon;
import com.tyiti.easycommerce.entity.CouponRecord;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.Tag;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.repository.ActivitySkuDao;
import com.tyiti.easycommerce.repository.CouponDao;
import com.tyiti.easycommerce.repository.CouponRecordDao;
import com.tyiti.easycommerce.repository.SkuDao;
import com.tyiti.easycommerce.repository.TagDao;
import com.tyiti.easycommerce.service.ActivityService;
import com.tyiti.easycommerce.service.CouponService;
import com.tyiti.easycommerce.util.CommonException;

@Service("couponService")
public class CouponServiceImpl implements CouponService {

	@Autowired
	private CouponDao couponDao;
	@Autowired
	private CouponRecordDao couponRecordDao;
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private TagDao tagDao;
	@Autowired
	private ActivityService activityService;
	@Autowired
	ActivitySkuDao activitySkuDao; 

	// private Log log = LogFactory.getLog(this.getClass());

	public List<CouponRecord> getCouponRecord(Integer custId, Integer skuId,Integer num) {
		// TODO Auto-generated method stub
		// 1.可使用的优惠券列表
		List<CouponRecord> couponList = new ArrayList<CouponRecord>();

		// 2.获取根据商品数量 获取商品总价格
		Sku sku = skuDao.getById(skuId);
		ActivitySku activitySku = new ActivitySku();
		activitySku.setSkuId(skuId);
		Integer activity_Id=null;
		List<Map<String, Object>> activitySkuList = activityService.findActivitySku(activitySku);
		for(Map<String, Object> activityMap:activitySkuList){
			Integer activity_stats=(Integer) activityMap.get("activityStatus");
			if(activity_stats==1&&activityMap.get("coupon")!=null){
				activity_Id=(Integer) activityMap.get("activityId");
				sku.setPrice(new BigDecimal(activityMap.get("activityPrice").toString()));
				break;
			}
		}

		BigDecimal totalPrice = sku.getPrice().multiply(new BigDecimal(num));
		// 3.获取用户未到期、未使用的优惠券
		List<CouponRecord> couponRecordList = couponRecordDao.getByCustId(custId);
		for (CouponRecord couponRecord : couponRecordList) {
			Coupon coupon = couponDao.selectByPrimaryKey(couponRecord
					.getCouponId());
			couponRecord.setCoupon(coupon);
			if (coupon.getTimeType() == 1) {// 绝对时间（优惠卷规则表的）
				couponRecord.setStartTime(coupon.getStartTime());// 如果是相对时间start_time
																	// 是空的
			} else {// 相对时间（收到优惠卷时间）
				couponRecord.setStartTime(couponRecord.getReceiveTime());// 收到优惠卷时间
			}
			if (coupon.getScope() == 1) {// 全场通用
				// 全场通用
				couponRecord.setScopeText("全场通用");
			} else if (coupon.getScope() == 2) {// sku
				// sku 专用
				Sku skuCoupon = skuDao.getById(coupon.getRefId());
				couponRecord.setScopeText(skuCoupon.getName());
			} else if (coupon.getScope() == 3) {// Tag标签
				// 标签专用 根据标签获取所有 skuId 是否包含 当前skuId
				Integer tagId = coupon.getRefId();
				Tag tag = tagDao.getById(tagId);
				couponRecord.setScopeText(tag.getName());
			}
			if (coupon.getType() == 1 || coupon.getType() == 2) {// 1.优惠券 2 优惠码
																	// 3 代金券 4
																	// 代金码
				BigDecimal threshold = new BigDecimal(0);
				if (coupon.getThreshold() != null)// 优惠起始金额
					threshold = BigDecimal.valueOf(coupon.getThreshold());
				if (totalPrice.compareTo(threshold) < 0) {// 总金额和优惠卷金额比较
															// （现：优惠卷金额大于总金额
															// 说明还没达到满减条件）
					// 消费金额没达到满减条件
					couponRecord.setAvailable(0);// 不可用 0
					couponList.add(couponRecord);
					continue;
				}

			} else {// 代金卷 代金码
				// coupon discount 满减优惠金额
				if (totalPrice.subtract(new BigDecimal(coupon.getDiscount()))
						.compareTo(new BigDecimal(10)) > -1) {
					couponRecord.setAvailable(1);
					couponRecord.setScopeText("全场通用");
					couponList.add(couponRecord);
				} else {
					couponRecord.setAvailable(0);
					couponRecord.setScopeText("全场通用");
					couponList.add(couponRecord);
				}

				continue;
			}
			Date now = new Date();
			if (coupon.getTimeType() == 1) {
				// 绝对时间 , 记录里面判断肯定是没过期 判断一下开始时间 现在能不能使用
				if (couponRecord.getStartTime().getTime() > now.getTime()) {
					// 未开始 不能使用呢
					couponRecord.setAvailable(0);
					couponList.add(couponRecord);
					continue;
				}
				if (couponRecord.getExpireTime().getTime() < now.getTime()) {
					// 未开始 不能使用呢
					couponRecord.setAvailable(0);
					couponList.add(couponRecord);
					continue;
				}
			} else {
				// 相对时间 (ExpireTime)有效期结束时间
				if (couponRecord.getExpireTime().getTime() < now.getTime()) {
					// 未开始 不能使用呢
					couponRecord.setAvailable(0);
					couponList.add(couponRecord);
					continue;
				}
			}
			if (coupon.getScope() == 1) {
				// 全场通用
				couponRecord.setScopeText("全场通用");
				couponRecord.setAvailable(1);
				couponList.add(couponRecord);
				continue;
			} else if (coupon.getScope() == 2) {
				if(activity_Id!=null){
                	Activity activity = activityService.findByActivityId(activity_Id);
                	if(activity==null||activity.getCoupon()==null||activity.getCoupon().equals("0")
							||(activity.getCoupon().equals("1")&&(coupon.getType()!=1&&coupon.getType()!=2))
							||(activity.getCoupon().equals("2")&&coupon.getType()!=3&&coupon.getType()!=4)){
                		couponRecord.setAvailable(0);
						couponList.add(couponRecord);
						continue;
					}else{
						if (coupon.getRefId() != null && coupon.getRefId().equals(skuId)) {
    						couponRecord.setAvailable(1);
//    						couponList.add(couponRecord);
    					} else {
    						couponRecord.setAvailable(0);
    						couponList.add(couponRecord);
    						continue;
    					}
					}
                }else{
                	if (coupon.getRefId() != null && coupon.getRefId().equals(skuId)) {
						couponRecord.setAvailable(1);
//						couponList.add(couponRecord);
					} else {
						couponRecord.setAvailable(0);
						couponList.add(couponRecord);
						continue;
					}
				}
			} else if (coupon.getScope() == 3) {
				// 标签专用 根据标签获取所有 skuId 是否包含 当前skuId
				Integer tagId = coupon.getRefId();
				String tagIds = getTagIds(tagId);
				String[] tagArray = tagIds.split(",");
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("tagArray", tagArray);
				param.put("skuId", skuId);
				Integer skuNum = tagDao.getTagSkus(param);
				if (skuNum > 0) {
					couponRecord.setAvailable(1);
					couponList.add(couponRecord);
					continue;
				} else {
					couponRecord.setAvailable(0);
					couponList.add(couponRecord);
					continue;
				}
			}
		}
		return couponList;
	}

	private String getTagIds(Integer tagId) {
		String tagIds = tagId + "";
		List<Tag> tagList = tagDao.getByParentId(tagId);
		for (Tag tag : tagList) {
			tagIds = tagIds + "," + tag.getId();
			getTagIds(tag.getId());
		}
		return tagIds;
	}

	@Override
	public SearchResult<Map<String, Object>> getMyCouponRecord(User user,
			Map<String, Object> param) {
		// 分页信息
		if (param.get("limit") != null && param.get("limit") != "") {
			param.put("limit",
					Integer.parseInt(String.valueOf(param.get("limit"))));
		}
		if (param.get("offset") != null && param.get("offset") != "") {
			param.put("offset",
					Integer.parseInt(String.valueOf(param.get("offset"))));
		}
		SearchResult<Map<String, Object>> result = new SearchResult<Map<String, Object>>();
		param.put("custId", user.getId());
		result.setRows(couponRecordDao.getMyCouponRecordList(param));
		result.setTotal(couponRecordDao.getMyCouponRecordCount(param));
		return result;
	}

	@Transactional
	public void recevieCouponRecord(Integer custId, Integer id) {
		Coupon coupon = couponDao.selectByPrimaryKey(id);

		// 判断该用户是否已领取过该优惠券
		Integer couponRecordCount = couponRecordDao.checkIsReceived(custId, id);
		if (couponRecordCount > 0) {
			throw new CommonException("已经领取过该类型优惠券");
		}
		Date now = new Date();
		if (coupon == null) {
			throw new CommonException("不存在此优惠券");
		} else {
			if (coupon.getCount().compareTo(coupon.getReceivedNum()) < 1) {
				throw new CommonException("优惠券已发放完毕");
			}
			if (coupon.getType() == 2 || coupon.getType() == 4) {
				throw new CommonException("不能获取优惠券");
			}
			if (coupon.getReceiveStartTime().getTime() > now.getTime()) {
				throw new CommonException("活动未开始");
			}
			if (coupon.getReceiveEndTime().getTime() < now.getTime()) {
				throw new CommonException("活动已结束");
			}
		}
		Date expireTime = null;
		if (coupon.getTimeType() == 1) {
			expireTime = coupon.getExpireTime();
		} else {
			Calendar ca = Calendar.getInstance();
			ca.add(Calendar.DATE, coupon.getExpireInDays());// 30为增加的天数，可以改变的
			expireTime = ca.getTime();
		}
		// 开始插入优惠券
		CouponRecord couponRecord = new CouponRecord();
		couponRecord.setCustId(custId);
		couponRecord.setCouponId(coupon.getId());
		couponRecord.setCreateTime(now);
		couponRecord.setReceiveTime(now);
		couponRecord.setExpireTime(expireTime);
		couponRecordDao.insertSelective(couponRecord);
		// 修改优惠券使用情况
		coupon.setReceivedNum(coupon.getReceivedNum() + 1);
		couponDao.updateByPrimaryKeySelective(coupon);
	}

	@Override
	public CouponRecord selectCouponRecordById(Integer couponRecordId) {
		return couponRecordDao.selectByPrimaryKey(couponRecordId);
	}

	@Transactional
	public void updateCouponInfo(Integer custId, Integer couponRecordId) {
		Date now = new Date();
		CouponRecord couponRecord = couponRecordDao
				.selectByPrimaryKey(couponRecordId);
		couponRecord.setIsUsed(1);
		couponRecord.setUpdateTime(now);
		couponRecordDao.updateByPrimaryKeySelective(couponRecord);
		Coupon coupon = couponDao
				.selectByPrimaryKey(couponRecord.getCouponId());
		coupon.setUsedNum(coupon.getUsedNum() + 1);
		coupon.setUpdateTime(now);
		couponDao.updateByPrimaryKeySelective(coupon);
	}

	@Transactional
	public void recevieCouponRecordByCode(Integer custId, String code) {
		if (code == null || code.equals("")) {
			throw new CommonException("请输入优惠码/代金码");
		}
		CouponRecord couponRecord = couponRecordDao.getCouponRecordByCode(code);
		if (couponRecord == null) {
			throw new CommonException("优惠码/代金码输入错误");
		}
		if (couponRecord.getCustId() != null) {
			throw new CommonException("此码已被绑定");
		}
		Integer couponRecordCount = couponRecordDao.checkIsReceived(custId,
				couponRecord.getCouponId());
		if (couponRecordCount > 0) {
			throw new CommonException("已经领取过该类型优惠码/代金码");
		}
		Coupon coupon = couponDao
				.selectByPrimaryKey(couponRecord.getCouponId());

		Date now = new Date();
		if (coupon == null) {
			throw new CommonException("不存在此活动");
		} else {
			if (coupon.getStop() == 1) {
				throw new CommonException("优惠码/代金码已过期");
			}
			if (coupon.getReceiveStartTime().getTime() > now.getTime()) {
				throw new CommonException("活动未开始");
			}
			if (coupon.getReceiveEndTime().getTime() < now.getTime()) {
				throw new CommonException("活动已结束");
			}
		}
		coupon.setUpdateTime(now);
		coupon.setReceivedNum(coupon.getReceivedNum() + 1);
		couponDao.updateByPrimaryKeySelective(coupon);
		couponRecord.setCustId(custId);
		couponRecord.setReceiveTime(now);
		if (coupon.getTimeType() == 1) {
			couponRecord.setExpireTime(coupon.getExpireTime());
		} else if (coupon.getTimeType() == 2) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			calendar.add(Calendar.DATE, coupon.getExpireInDays());// 把日期往后增加一天.整数往后推,负数往前移动
			couponRecord.setExpireTime(calendar.getTime());
		}
		couponRecordDao.updateByPrimaryKeySelective(couponRecord);
	}

	@Override
	public Double getCouponByRecordId(Integer couponRecordId) {
		// TODO Auto-generated method stub
		return couponRecordDao.getCouponDiscount(couponRecordId);
	}

	/**
	 * 购物车中的优惠卷
	 */
	@Override
	public List<CouponRecord> getAllCartCoupon(List<Sku> skuFromCart, Integer cusId) {

		// TODO Auto-generated method stub
		// 1.可使用的优惠券列表
		if(skuFromCart == null ){
			throw new CommonException("购买商品错误");
		}
		List<CouponRecord> couponList = new ArrayList<CouponRecord>();
		List<CouponRecord> couponRecordList = couponRecordDao.getByCustId(cusId);
		if(couponRecordList==null||couponRecordList.size()==0){//用户没有优惠卷
			return couponList;
		}
//		获取商品总价以及设置活动信息
		BigDecimal totalPrice = new BigDecimal(0);
		Map<String,Map<String,Object>> skuActivity = new HashMap<String, Map<String,Object>>();
		List<Sku> skus =  new ArrayList<Sku>();
		for (Sku arrSku : skuFromCart) {
			Sku sku = skuDao.getById(arrSku.getId());
//			单个商品总价
			BigDecimal skuTotalPrice = new BigDecimal(0);
			Integer activityType=null;
			Map<String,Object> skuActivityMap = new HashMap<String, Object>(); 
			if(sku == null){
				throw new CommonException("商品不存在");
			}
			ActivitySku activitySku = new ActivitySku();
			activitySku.setSkuId(arrSku.getId());
			List<Map<String, Object>> activitySkuList = activityService.findActivitySku(activitySku);
			boolean activityNotUse=false;
			for(Map<String, Object> activityMap:activitySkuList){
				Integer activity_stats=(Integer) activityMap.get("activityStatus");
				if(activity_stats==1&&activityMap.get("coupon")!=null){
					if("0".equals(activityMap.get("coupon"))){
						activityNotUse=true;
					}else{
						activityType = (Integer) activityMap.get("activityType");
						sku.setPrice(new BigDecimal(activityMap.get("activityPrice").toString()));
						skuActivityMap.put("activityType", activityType);
					}
					break;
				}
			}
			if(!activityNotUse){
				skuTotalPrice = activityService.compareSkuTotalPrice(activityType, sku.getPrice(), arrSku.getNum());
			}
			totalPrice = totalPrice.add(skuTotalPrice);
			sku.setNum(arrSku.getNum());
			skuActivityMap.put("activityNotUse", activityNotUse);
			skuActivityMap.put("sku", sku);
			skuActivity.put(arrSku.getId().toString(), skuActivityMap);
			skus.add(sku); // 把 购物车传过来的id 和数量 转换成sku的列
		}
		for (CouponRecord couponRecord : couponRecordList) {
			Coupon coupon = couponDao.selectByPrimaryKey(couponRecord.getCouponId());
			couponRecord.setCoupon(coupon);
			if (coupon.getTimeType() == 1) {// 绝对时间（优惠卷规则表的）
				couponRecord.setStartTime(coupon.getStartTime());// 如果是相对时间start_time
																	// 是空的
			} else {// 相对时间（收到优惠卷时间）
				couponRecord.setStartTime(couponRecord.getReceiveTime());// 收到优惠卷时间
			}
//			设置显示内容
			setCouponScopeText(couponRecord, coupon);
			// 根据时间判断优惠卷是否在可使用的范围内容
			boolean couponCanUse = couponCanUseByTime(couponRecord, coupon);
			if(!couponCanUse){//不可用		
				couponList.add(couponRecord);
				continue;
			}
//			根据优惠卷适用范围分别判断是否可用
			if(coupon.getScope()==1){
				if(totalPrice.compareTo(new BigDecimal(0))<=0){//活动中不可用,除去商品为空的情况下，总价为空只能是所有商品均在活动中
					couponRecord.setAvailable(0);//此处0为活动不可用
					couponList.add(couponRecord);
					continue;
				}
//				可用，差钱
//				设置使用优惠卷的最低金额，
				BigDecimal lowPrice= new BigDecimal(0);
				if(coupon.getType() == 3 || coupon.getType() == 4){
					lowPrice = new BigDecimal(coupon.getDiscount()).add(new BigDecimal(10));
				}else{
					lowPrice = BigDecimal.valueOf(coupon.getThreshold());
				}
				if(totalPrice.compareTo(lowPrice)>-1){//可用
					couponRecord.setAvailable(1);
					couponList.add(couponRecord);
					continue;
				}
				couponRecord.setAvailable(2);
				couponRecord.setUnavailableMoney(lowPrice.subtract(totalPrice));
				couponList.add(couponRecord);
				continue;
			}else if(coupon.getScope()==2){//Sku优惠卷判断
				if(!skuActivity.containsKey(coupon.getRefId().toString())){//商品不在购物车中
					couponRecord.setAvailable(0);
					couponList.add(couponRecord);
					continue;
				}
				Map<String,Object> skuActivityMap = skuActivity.get(coupon.getRefId().toString());
				Boolean activityNotUse = (Boolean) skuActivityMap.get("activityNotUse");
				Integer activityType = (Integer) skuActivityMap.get("activityType");
				Sku sku = (Sku) skuActivityMap.get("sku");
				if(activityNotUse){//该商品在活动中，且该活动设置不能使用优惠卷
					couponRecord.setAvailable(0);//此处0为活动不可用
					couponList.add(couponRecord);
					continue;
				}
				BigDecimal skuTotalPrice = activityService.compareSkuTotalPrice(activityType, sku.getPrice(), sku.getNum());
				if(skuTotalPrice.compareTo(new BigDecimal(coupon.getThreshold()))>0){//可用
					couponRecord.setAvailable(1);
					couponList.add(couponRecord);
					continue;
				}
				//差钱
				couponRecord.setAvailable(2);
				couponRecord.setUnavailableMoney(new BigDecimal(coupon.getThreshold()).subtract(skuTotalPrice));
				couponList.add(couponRecord);
			}else if(coupon.getScope()==3){//tag标签判断
				/**
				 * 把一堆sku 按照标签分类 求各类商品的总价
				 */
				Integer tagId = coupon.getRefId();
				String tagIds = getTagIds(tagId);
				String[] tagArray = tagIds.split(",");
				List<Sku> list = new ArrayList<Sku>();
				for (Sku arrSkus : skus) {
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("tagArray", tagArray);
					param.put("skuId", arrSkus.getId());
					// 判断这个商品属于这个标签
					Integer skuNum = tagDao.getTagSkus(param);
					if (skuNum > 0) {
						list.add(arrSkus);
					}
				}
				if(list.size()==0){//没有tag商品
					couponRecord.setAvailable(0);
					couponList.add(couponRecord);
					continue;
				}
				BigDecimal totalPriceCatory = new BigDecimal(0);
				for (Sku s : list) {
					Map<String,Object> skuActivityMap = skuActivity.get(s.getId().toString());
					Boolean activityNotUse = (Boolean) skuActivityMap.get("activityNotUse");
					Integer activityType = (Integer) skuActivityMap.get("activityType");
					if(!activityNotUse){
						BigDecimal skuTotalPrice = activityService.compareSkuTotalPrice(activityType, s.getPrice(), s.getNum());
						totalPriceCatory = totalPriceCatory.add(skuTotalPrice);
					}
				}
				if(totalPriceCatory.compareTo(new BigDecimal(0))<=0){
					couponRecord.setAvailable(0);//此处0为活动不可用
					couponList.add(couponRecord);
					continue;
				}
				if (totalPriceCatory.compareTo(BigDecimal.valueOf(coupon
						.getThreshold())) == -1) {
					couponRecord.setAvailable(2);
					couponRecord.setUnavailableMoney(BigDecimal.valueOf(
							coupon.getThreshold()).subtract(
							totalPriceCatory));// 还差多少钱可以使用
					couponList.add(couponRecord);
					continue;
				} else {
					couponRecord.setAvailable(1);
					couponRecord.setUnavailableMoney(new BigDecimal(0));
					couponList.add(couponRecord);
					continue;
				}
			}
		}
		return couponList;
	}

	@Transactional
	public Integer sendCouponRecord(Integer custId, Integer id) {
		Coupon coupon = couponDao.selectByPrimaryKey(id);
		Date now = new Date();
		if (coupon == null) {
			throw new CommonException("不存在此优惠券");
		} else {
			if (coupon.getCount().compareTo(coupon.getReceivedNum()) < 1) {
				throw new CommonException("优惠券已发放完毕");
			}
			if (coupon.getType() == 2 || coupon.getType() == 4) {
				throw new CommonException("不能获取优惠券");
			}
			if (coupon.getReceiveStartTime().getTime() > now.getTime()) {
				throw new CommonException("活动未开始");
			}
			if (coupon.getReceiveEndTime().getTime() < now.getTime()) {
				throw new CommonException("活动已结束");
			}
		}
		Date expireTime = null;
		if (coupon.getTimeType() == 1) {
			expireTime = coupon.getExpireTime();
		} else {
			Calendar ca = Calendar.getInstance();
			ca.add(Calendar.DATE, coupon.getExpireInDays());// 30为增加的天数，可以改变的
			expireTime = ca.getTime();
		}
		// 开始插入优惠券
		CouponRecord couponRecord = new CouponRecord();
		couponRecord.setCustId(custId);
		couponRecord.setCouponId(coupon.getId());
		couponRecord.setCreateTime(now);
		couponRecord.setReceiveTime(now);
		couponRecord.setExpireTime(expireTime);
		couponRecordDao.insertGetId(couponRecord);
		// 修改优惠券使用情况
		coupon.setReceivedNum(coupon.getReceivedNum() + 1);
		couponDao.updateByPrimaryKeySelective(coupon);
		return couponRecord.getId();
	}
	/**
	 * 根据优惠卷使用范围，设置优惠卷显示名称
	 * @param couponRecord
	 * @param coupon
	 */
	private void setCouponScopeText(CouponRecord couponRecord, Coupon coupon) {
		if (coupon.getScope() == 1) {// 全场通用
			// 全场通用
			couponRecord.setScopeText(coupon.getName());
		} else if (coupon.getScope() == 2) {// sku
			// sku 专用
			Sku skuCoupon = skuDao.getById(coupon.getRefId());
			couponRecord.setScopeText(skuCoupon.getName());
		} else if (coupon.getScope() == 3) {// Tag标签
			// 标签专用 根据标签获取所有 skuId 是否包含 当前skuId
			Integer tagId = coupon.getRefId();
			Tag tag = tagDao.getById(tagId);
			couponRecord.setScopeText(tag.getName());
		}
	}
	/**
	 * 根据优惠卷时间判断优惠卷是否在可用时间内
	 * @param couponRecord
	 * @param coupon
	 * @return
	 */
	private boolean couponCanUseByTime(CouponRecord couponRecord, Coupon coupon) {
		Date now = new Date();
		if (coupon.getTimeType() == 1) {
			// 绝对时间 , 记录里面判断肯定是没过期 判断一下开始时间 现在能不能使用
			if (couponRecord.getStartTime().getTime() > now.getTime()) {
				// 未开始 不能使用呢
				couponRecord.setAvailable(0);
				couponRecord.setUnavailableMoney(new BigDecimal(0));
				return false;
			}
			if (couponRecord.getExpireTime().getTime() < now.getTime()) {
				// 未开始 不能使用呢
				couponRecord.setAvailable(0);
				couponRecord.setUnavailableMoney(new BigDecimal(0));
				return false;
			}
		} else {
			// 相对时间 (ExpireTime)有效期结束时间
			if (couponRecord.getExpireTime().getTime() < now.getTime()) {
				// 未开始 不能使用呢
				couponRecord.setAvailable(0);
				couponRecord.setUnavailableMoney(new BigDecimal(0));
				return false;
			}
		}
		return true;
	}
}
