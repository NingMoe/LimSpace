package com.tyiti.easycommerce.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.common.LogEnum;
import com.tyiti.easycommerce.entity.Coupon;
import com.tyiti.easycommerce.entity.CouponRecord;
import com.tyiti.easycommerce.repository.CouponDao;
import com.tyiti.easycommerce.repository.CouponRecordDao;
import com.tyiti.easycommerce.service.CouponRecordService;
import com.tyiti.easycommerce.util.HttpClientUtil;
import com.tyiti.easycommerce.util.LogUtil;
import com.tyiti.easycommerce.util.exception.CommonException;

@Service("couponRecordService")
public class CouponRecordServiceImpl implements CouponRecordService {

	@Autowired
	private CouponRecordDao couponRecordDao;
	@Autowired
	private CouponDao couponDao;
	@Override
	public int addOneCouponRecord(CouponRecord couponRecord) {
		couponRecord.setReceiveTime(new Date());
		couponRecord.setIsUsed(0);
		//couponRecord.setCouCode(couponRecord.getCustId()+"-"+System.currentTimeMillis());
		return couponRecordDao.insertSelective(couponRecord);
	}
	
	// 发送短信验证码
	public int sendSmsForVerifyCode(String mobile, String verifyCode) {
		String url = "http://cf.lmobile.cn/submitdata/service.asmx/g_Submit";
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("sname", "dltrfq00");
		requestParams.put("spwd", "4UimKe2E");
		requestParams.put("scorpid", "");
		requestParams.put("sprdid", "1012818");
		requestParams.put("sdst", mobile);
		requestParams.put("smsg", verifyCode + "【分信宝】");

		// TODO 解析返回的 XML
		HttpClientUtil.httpGet(url, null, requestParams);
		return 0;
	}
	
	@Override
	public boolean updateUseStatus(int id) {
		int count = couponRecordDao.updateUseStatus(id);
		if (count < 1) {
			return false;
		}
		return true;
	}
	@Override
	@Transactional
	public int sendOneCoup(Integer couId, String mobile) {
		//查询手机号是否存在
		long record = couponRecordDao.selCountByMobile(mobile);
		if(record==0){
			throw new CommonException("该手机号不存在");
		}
		//判断该用户有没有领取该优惠券
		long count = couponRecordDao.selCountByCustId(couId,mobile);
		if(count>=1){
			throw new CommonException("用户已领取该优惠券");
		}
		int num = couponRecordDao.insertOneRecordByMobile(couId,mobile);
		if (num == 1) {
			//判断是否有库存
			Coupon coupon = couponDao.selectByPrimaryKey(couId);
			if(coupon.getReceivedNum()>=coupon.getCount()){
				throw new CommonException("优惠券不足");
			}
			coupon.setCount(coupon.getCount()-1);
			couponDao.updateByPrimaryKeySelective(coupon);
			/*String smsContent = coupon.getRecSms();
			//优惠方式
			String couTypeStr = "";
			//优惠金额
			String couMoney = coupon.getCouMoney();
			//优惠起始金额
			Double startMoney = coupon.getStartMoney();
			short couType = coupon.getCouType();
			//最终拼接内容
			String finalContent;
			if(couType==0){
				couTypeStr = "满减";
			}
			if(couType==1){
				couTypeStr = "折扣";
			}
			finalContent = smsContent.replaceAll("#01", couTypeStr);
			finalContent = finalContent.replaceAll("#02", startMoney+"");
			finalContent = finalContent.replaceAll("#03", couMoney);*/
			//发送短信
			sendSmsForVerifyCode(mobile,coupon.getReceiveSms());
			LogUtil.log(couId, LogEnum.OperateModel.COUPON.getKey(),
					LogEnum.Action.ADD.getKey(), "发送优惠券",
					LogEnum.Source.PLAT.getKey(), 1);
		}
		return num;
	}
	@Override
	public Map<String, Object> queryById(Integer id) {
		return couponRecordDao.selectById(id);
	}
	@Override
	public SearchResult<Map<String, Object>> queryInfoListByPage(
			Map<String, Object> param) {
		if(param.get("limit")!=null &&param.get("limit")!=""){
			param.put("limit", Integer.parseInt(String.valueOf(param.get("limit"))));
		} 
		if(param.get("offset")!=null && param.get("offset")!=""){
			param.put("offset", Integer.parseInt(String.valueOf(param.get("offset"))));
		} 
		SearchResult<Map<String,Object>> searchResult  = new SearchResult<Map<String,Object>>();
		List<Map<String, Object>> rows = this.couponRecordDao.selectInfoListByPage(param);
		searchResult.setRows(rows);
		searchResult.setTotal(couponRecordDao.selectInfoListCountByPage(param));
		return searchResult;
	}
	@Override
	public boolean removeById(Integer id) {
		int num = couponRecordDao.deleteByLogic(id);
		if(num !=1){
			return false;
		}
		LogUtil.log(id, LogEnum.OperateModel.COUPON.getKey(),
				LogEnum.Action.ADD.getKey(), "删除用户优惠券记录",
				LogEnum.Source.PLAT.getKey(), 1);
		return true;
	}
	@Override
	public boolean removeById(String ids) {
		String[] strs = null;
		int count = 0;
		if(ids!=null&&!"".equals(ids)){
			strs = ids.split(",");
			for(int i = 0;i<strs.length;i++){
				count += couponRecordDao.deleteByLogic(Integer.parseInt(strs[i]));
			}
			if(count!=strs.length){
				throw new CommonException("参数不正确");
			}
			for(int i = 0;i<strs.length;i++){
				LogUtil.log(Integer.parseInt(strs[i]), LogEnum.OperateModel.COUPON.getKey(),
						LogEnum.Action.ADD.getKey(), "删除用户优惠券记录",
						LogEnum.Source.PLAT.getKey(), 1);
			}
		}
		return true;
	}
	@Override
	public int updateCouponRecord(CouponRecord couponRecord) {
		return couponRecordDao.updateByPrimaryKeySelective(couponRecord);
	}

	@Override
	public Map<String, Object> queryDetailById(Integer id) {
		return couponRecordDao.selectDetailById(id);
	}
}
