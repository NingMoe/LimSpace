package com.tyiti.easycommerce.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.common.LogEnum;
import com.tyiti.easycommerce.common.SysConfig;
import com.tyiti.easycommerce.common.service.NoService;
import com.tyiti.easycommerce.entity.Activity;
import com.tyiti.easycommerce.entity.ActivitySku;
import com.tyiti.easycommerce.entity.Address;
import com.tyiti.easycommerce.entity.Cart;
import com.tyiti.easycommerce.entity.Coupon;
import com.tyiti.easycommerce.entity.CouponRecord;
import com.tyiti.easycommerce.entity.DataOrderNoMapping;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.OrderPayment;
import com.tyiti.easycommerce.entity.OrderSku;
import com.tyiti.easycommerce.entity.OrderSkuActivity;
import com.tyiti.easycommerce.entity.PickupOrder;
import com.tyiti.easycommerce.entity.PickupPoint;
import com.tyiti.easycommerce.entity.SeqNo;
import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.entity.SkuAttribute;
import com.tyiti.easycommerce.entity.SkuExt;
import com.tyiti.easycommerce.entity.Tag;
import com.tyiti.easycommerce.entity.User;
import com.tyiti.easycommerce.repository.ActivitySkuDao;
import com.tyiti.easycommerce.repository.AddressDao;
import com.tyiti.easycommerce.repository.CartDao;
import com.tyiti.easycommerce.repository.CouponDao;
import com.tyiti.easycommerce.repository.CouponRecordDao;
import com.tyiti.easycommerce.repository.DataOrderNoMappingDao;
import com.tyiti.easycommerce.repository.OrderDao;
import com.tyiti.easycommerce.repository.OrderPaymentDao;
import com.tyiti.easycommerce.repository.OrderSkuDao;
import com.tyiti.easycommerce.repository.PickupOrderDao;
import com.tyiti.easycommerce.repository.PickupPointDao;
import com.tyiti.easycommerce.repository.SeqNoDao;
import com.tyiti.easycommerce.repository.SkuAttributeDao;
import com.tyiti.easycommerce.repository.SkuDao;
import com.tyiti.easycommerce.repository.SkuExtDao;
import com.tyiti.easycommerce.repository.TagDao;
import com.tyiti.easycommerce.service.ActivityService;
import com.tyiti.easycommerce.service.OrderService;
import com.tyiti.easycommerce.service.SkuService;
import com.tyiti.easycommerce.util.CommonException;
import com.tyiti.easycommerce.util.HttpClientUtil;
import com.tyiti.easycommerce.util.LogUtil;
import com.tyiti.easycommerce.util.Md5;

import net.sf.json.JSONObject;

@Service
public class OrderServiceImpl implements OrderService {
	private Log logg = LogFactory.getLog(this.getClass());
	// 信分宝接口地址
	@Value("${xfbInterface}")
	private String xfbInterface;
	// 信分宝接口地址版本号
	@Value("${xfbVersion}")
	private String xfbVersion;
	@Value("${koo_commission_rate}")
	private String koo_commission_rate;
	// 用户是否和信分宝打通
	@Value("${connectedToXfb}")
	private boolean connectedToXfb;
	// sys
	@Value("${sys}")
	private String sys;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderSkuDao orderSkuDao;
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private AddressDao addressDao;
	@Autowired
	private SeqNoDao seqNoDao;
	@Autowired
	private DataOrderNoMappingDao dataOrderNoMappingDao;
	@Autowired
	private NoService noService;
	@Autowired
	private SkuAttributeDao skuAttributeDao;

	@Autowired
	private CouponDao couponDao;
	@Autowired
	private CouponRecordDao couponRecordDao;

	@Autowired
	private OrderPaymentDao orderPaymentDao;
	@Autowired
	private TagDao tagDao;
	@Autowired
	ActivityService activityService;
	@Autowired
	private SkuService skuService;

	@Autowired
	private PickupOrderDao pickupOrderDao;

	@Autowired
	private PickupPointDao pickupPointDao;
	@Autowired
	private SkuExtDao skuExtDao;

	@Autowired
	private CartDao cartDao;
	
	@Autowired
	private ActivitySkuDao activitySkuDao;
//	@Resource
//	private StdScheduler quartzScheduler; // 调度

	@Override
	@Transactional
	public Map<String, Object> addOrder(User user, Order order) {
		Map<String, Object> map = new HashMap<String, Object>();
		//先把优惠券的id 拿出来
		Integer couponRecordId = order.getCouponRecordId();	
		// 1 保存用户信息

		//把自提点id 先拿出来 
		Integer pickupPointId = order.getPickupPointId();
		// 1 保存订单记录
		Integer custId = user.getId();

		// 1.1 生成订单号
		order.setNo(noService.getOrderNo());

		Sku sku = skuDao.getById(order.getSkuId());
		if (sku == null) {
			map.put("code", 400);
			map.put("message", "商品未找到");
			return map;
		}
		if (sku.getStatus() == 0) {
			map.put("code", 400);
			map.put("message", "商品已下架");
			return map;
		}
		int skuCount  = order.getSkuCount();
		List<Map<String, Object>> activitySkulist=null;
		if(sku.getPickup() != 1 &&!sku.getErpCode().equals("koo")){
			// 获取默认地址
			Address address = addressDao.getByUserIdOrId(order.getAddressId(),custId);
			if (address == null) {
				map.put("code", 400);
				map.put("message", "地址未找到");
				return map;
			}
			order.setAddressMobile(address.getMobile());
			order.setAddressName(address.getUsername());
			order.setAddressFullText(address.getFullText());
			order.setAddressZip(address.getZip());

		}
		/****校验商品是不是活动商品*****/
		Integer activityId = order.getActivityId();
		if(!StringUtils.isEmpty(activityId)){
			activitySkulist = activityService.findSpikeActivitySkuList(activityId,sku.getId());
			map = getActivity(user,order,sku,activitySkulist);
			Integer code = (Integer) map.get("code");
			if(code==200){
				order = (Order) map.get("order");	
			}else{
				logg.info(">>>>>>>>>>订单超过限购数量不能往下走了>>>>>>>>>>>>>>>>>>");
				return map;
			}
 
		}else{
			// 判断库存
			if (sku.getInventory() == null || (sku.getInventory() - skuCount) < 0) {
				map.put("code", 400);
				map.put("message", "库存不足");
				return map;
			}
			order.setAmount(sku.getPrice().multiply(new BigDecimal(skuCount)));
		}
		SkuExt skuExt = skuExtDao.findSkuExtBySkuId(order.getSkuId());// 保存第三方金额信息
		if(skuExt!=null&&skuExt.getField9()!=null){
			order.setThirdPartyAmount(new BigDecimal(skuExt.getField9()));
		}
		boolean couponFlag = false;
		// 使用优惠 金额
		BigDecimal discount = new BigDecimal(0.00);
		// 预设首付支付状态 ， 用于t_order_payment 
		Integer paymentType  = 10 ; 
		if (order.getCouponRecordId() != null) {
			// 1.获取正要使用的优惠券信息|判断使用条件 2 获取活动的信息 判断活动的使用规则
			CouponRecord couponRecord = couponRecordDao.selectByPrimaryKey(order.getCouponRecordId());
			if (couponRecord == null) {
				map.put("code", 400);
				map.put("message", "未找到符合条件的优惠券");
				return map;
			} else {
				if (!couponRecord.getCustId().equals(custId)) {
					map.put("code", 400);
					map.put("message", "请使用自己的优惠券");
					return map;
				}
				if (couponRecord.getIsUsed() == 1) {
					map.put("code", 400);
					map.put("message", "优惠券已使用过");
					return map;
				}
			}
			Date now = new Date();
			if (couponRecord.getExpireTime().getTime() < now.getTime()) {
				map.put("code", 400);
				map.put("message", "该优惠券已过期");
				return map;
			}
			// 判断商品是否符合优惠券的使用条件
			Coupon coupon = couponDao.selectByPrimaryKey(couponRecord.getCouponId());
			if(coupon.getType() == 1 || coupon.getType() ==2 ){//优惠卷
				if(!StringUtils.isEmpty(activityId)){//判断当前活动是否
					activitySkulist = activityService.findSpikeActivitySkuList(activityId,sku.getId());
					if(activitySkulist!=null&&activitySkulist.size()>0&&activitySkulist.get(0)!=null){
						Activity activity = activityService.findByActivityId(activityId);
						if(activity.getCoupon()==null){
							//订单金额小于优惠券满减条件参数
							map.put("code", 400);
							map.put("message", "该活动商品没有参加优惠活动");
							return map;
						}else if(activity.getCoupon().equals("1")){//订单金额小于优惠卷减满
							if(new BigDecimal(activitySkulist.get(0).get("activityPrice").toString()).compareTo( BigDecimal.valueOf(coupon.getThreshold()))<0){
								//订单金额小于优惠券满减条件参数
								map.put("code", 400);
								map.put("message", "订单金额小于优惠券满减条件");
								return map;
							}
						}
					}
				}else{
					if(sku.getPrice().multiply(new BigDecimal(skuCount)).compareTo( BigDecimal.valueOf(coupon.getThreshold()))<0){
						//订单金额小于优惠券满减条件参数
						map.put("code", 400);
						map.put("message", "订单金额小于优惠券满减条件");
						return map;
					}
				}
			}else{//代金券活动
                if(!StringUtils.isEmpty(activityId)){//判断当前活动是否
                	activitySkulist = activityService.findSpikeActivitySkuList(activityId,sku.getId());
                	if(activitySkulist!=null&&activitySkulist.size()>0&&activitySkulist.get(0)!=null){
                		Activity activity = activityService.findByActivityId(activityId);
                		if(activity.getCoupon()==null){
							//订单金额小于优惠券满减条件参数
							map.put("code", 400);
							map.put("message", "该活动商品没有参加优惠活动");
							return map;
						}else if(activity.getCoupon().equals("2")){
							if(new BigDecimal(activitySkulist.get(0).get("activityPrice").toString()).multiply(new BigDecimal(skuCount)).subtract(new BigDecimal(coupon.getDiscount())).compareTo(new BigDecimal(10))<0){
	    						//订单金额 - 代金券  不小于10
	    						map.put("code", 400);
	    						map.put("message", "支付金额不小于10元");
	    						return map;
	    					}		
						}
                	}
				}else{
					if(sku.getPrice().multiply(new BigDecimal(skuCount)).subtract(new BigDecimal(coupon.getDiscount())).compareTo(new BigDecimal(10))<0){
						//订单金额 - 代金券  不小于10
						map.put("code", 400);
						map.put("message", "支付金额不小于10元");
						return map;
					}	
				}
				
			}
			if (coupon.getScope() == 2) {
				// sku 判断
				if (coupon.getRefId() != null && !coupon.getRefId().equals(order.getSkuId())) {
					map.put("code", 400);
					map.put("message", "该优惠券不能用于该商品");
					return map;
				}
			} else if (coupon.getScope() == 3) {
				// tag 判断
				//标签专用  根据标签获取所有 skuId 是否包含 当前skuId
				Integer tagId = coupon.getRefId() ; 
				String tagIds = getTagIds(tagId);
				String[] tagArray=  tagIds.split(",");
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("tagArray", tagArray);
				param.put("skuId", order.getSkuId());
				Integer skuNum = tagDao.getTagSkus(param);
				if(skuNum == 0 ){
					throw new CommonException("该优惠券不能用于该商品");
				} 
			}
			discount =  new BigDecimal(coupon.getDiscount());
		}
		// 判断可用额度是否足够
		if (connectedToXfb) {
			// 判断是否需要分期支付
			if (order.getDownPayment().compareTo(order.getAmount()) < 0) {
				// 判断请求参数是否合法
				if ((order.getDownPayment().add(order.getInstallmentAmount())).compareTo(order.getAmount()) != 0) {
					throw new CommonException("请求参数不合法");
				}
				// 设置订单支付类型
				if (order.getDownPayment().compareTo(new BigDecimal(0)) == 0) {
					order.setPayMethod(2);
				} else {
					order.setPayMethod(3);
				}
				//如果用了优惠券
				if(discount.compareTo(new BigDecimal(0.00))>0)
					if(order.getDownPayment().compareTo(discount)<1){
						//首付<=优惠券金额  则首付减成0  ；分期 = 分期-优惠券金额
						paymentType = 11;//优惠
						if(order.getInstallmentAmount().add(order.getDownPayment()).compareTo(discount)>-1){
							order.setInstallmentAmount(order.getInstallmentAmount().subtract(discount).add(order.getDownPayment()));
						}else{
							order.setInstallmentAmount(new BigDecimal(0.00));
						}
						order.setDownPayment(new BigDecimal(0.00));
					}else{
						//首付大于等于优惠券金额  只首付减一下
						paymentType = 10 ; //微信
						order.setDownPayment(order.getDownPayment().subtract(discount));
					}
					// 分期金额>0时调用信分宝 wyy 2016/07/05
					if (order.getInstallmentAmount().compareTo(new BigDecimal(0)) > 0) {
						// 先登录信分宝
						String json = HttpClientUtil.httpLogin(xfbInterface, "",user.getMobile(), user.getPassword(), xfbVersion);
						JSONObject obj = JSONObject.fromObject(json);
						if (obj != null && !obj.isEmpty()) {
							if ("0".equals(obj.getString("resultCode"))) {
								String response = HttpClientUtil.httpGetUserScore(
										xfbInterface, user,
										obj.getString("userId"), xfbVersion);
								JSONObject result = JSONObject.fromObject(response);
								int resultCode = result.getInt("resultCode");
								if (resultCode != 0) {
									map.put("code", 500);
									map.put("resultCode", resultCode);
									map.put("message",
											result.getString("resultMessage"));
									return map;
								}else {
									Map<String, Object> data = new HashMap<String, Object>();
									// 判断可用额度
									if (order.getInstallmentAmount().compareTo(
											new BigDecimal(result.getString("useSum"))) == 1) {
										map.put("code", 404);
										map.put("message", "可用信用额度不足");
										data.put("creditSum",result.getString("creditSum"));
										data.put("usableSum",result.getString("useSum"));
										map.put("data", data);
										return map;
									}
							  }
						} else {
							map.put("code", 500);
							map.put("resultCode", obj.getString("resultCode"));
							map.put("message", obj.getString("resultMessage"));
							return map;
						}
					}
					
				}
				// 设置分期费率
				order.setInstallmentRate(new BigDecimal(SysConfig.rateConfigMap.get(String.valueOf(order.getInstallmentMonths()))));
			} else {
				// 判断请求参数是否合法
				if (order.getDownPayment().compareTo(order.getAmount()) == 0) {
					// 全首付
					paymentType = 10;
					order.setPayMethod(1);
				} else {
					map.put("code", 400);
					map.put("message", "请求参数不合法...");
					return map;
				}
			}
		} else if ("ceb".equals(sys)) {
			// 光大商城，无首付，全分期
			paymentType = 20  ; //分期
			order.setDownPayment(new BigDecimal(0));
			if(order.getAmount().compareTo(discount)>-1){
				order.setInstallmentAmount(order.getAmount().subtract(discount));
			}else{
				order.setInstallmentAmount(new BigDecimal(0.01));
			}
			
			order.setPayMethod(2);
		} else {
			// 全首付
			order.setPayMethod(1);
			if (order.getDownPayment().compareTo(order.getAmount()) != 0) {
				map.put("code", 400);
				map.put("message", "请求参数不合法");
				return map;
			}
			if(order.getDownPayment().compareTo(discount)>-1){
				order.setDownPayment(order.getDownPayment().subtract(discount));
			}else{
				//强制微信支付0.01
				order.setDownPayment(new BigDecimal(0.01));
			}
			
		}
		// 生成订单
		int affectedRows = orderDao.addOrder(order);
		if (affectedRows < 1) {
			return null;
		}

		order = orderDao.getById(order.getId());
		order.setActivityId(activityId);//活动ID
		order.setSkuCount(skuCount);//个数
		map.put("data", order);
		/***保存OrderSku信息*/
		saveOrderSku(sku,order,couponFlag,activitySkulist);
		/*******保存活动信息********/
		saveOrderSkuActivity(sku,order,discount,activitySkulist);
		//插入自提订单 <!----------------------------------------------------
				if(sku.getPickup() == 1 ){
					if(pickupPointId==null){
						throw new CommonException("自提点不存在");
					}else{
						PickupPoint pickupPoint = pickupPointDao.selectByPrimaryKey(pickupPointId);
						if(pickupPoint == null){
							throw new CommonException("自提点不存在");
						}
					}
					PickupOrder  pickupOrder = new PickupOrder();
					pickupOrder.setPickupPointId(pickupPointId);
					pickupOrder.setOrderId(order.getId());
					pickupOrder.setCreateTime(new Date());
					pickupOrder.setStatus(0);
					String code = "";
					Integer num = 0 ;
					do {
						// 生成6位随机码
						 code = createCode(6);
						 num = pickupOrderDao.selectCodeNum(code);
					} while (num > 0);
					pickupOrder.setCode(code);
					pickupOrderDao.insertSelective(pickupOrder);
				}
		if (couponRecordId != null) {// 插入优惠券信息
			Date now = new Date();
			CouponRecord couponRecord = couponRecordDao.selectByPrimaryKey(couponRecordId);
			couponRecord.setIsUsed(1);
			couponRecord.setUseTime(now);
			couponRecordDao.updateByPrimaryKeySelective(couponRecord);
			Coupon coupon = couponDao.selectByPrimaryKey(couponRecord.getCouponId());
			coupon.setUsedNum(coupon.getUsedNum()+1);
			couponDao.updateByPrimaryKey(coupon);	
		}
		//插入订单优惠金额
		if(couponRecordId !=null){
			OrderPayment	orderPaymentDiscount = new OrderPayment();
			orderPaymentDiscount.setOrderId(order.getId());
			orderPaymentDiscount.setCategory(3);//其他支付方式
			orderPaymentDiscount.setType(11);
			orderPaymentDiscount.setAmount(discount);
			orderPaymentDiscount.setSource(couponRecordId);
			orderPaymentDiscount.setCreateTime(new Date());
			orderPaymentDao.insertSelective(orderPaymentDiscount);	
		}
		//插入首付金额
		OrderPayment	orderPaymentDownPayment = new OrderPayment();
		orderPaymentDownPayment.setOrderId(order.getId());
		orderPaymentDownPayment.setCategory(1);
		orderPaymentDownPayment.setType(paymentType);
		orderPaymentDownPayment.setAmount(order.getDownPayment());
		orderPaymentDownPayment.setCreateTime(new Date());
		orderPaymentDao.insertSelective(orderPaymentDownPayment);
		//插入订单分期金额
		OrderPayment	orderPaymentInstallmentAmount = new OrderPayment();
		orderPaymentInstallmentAmount.setOrderId(order.getId());
		orderPaymentInstallmentAmount.setCategory(2);
		orderPaymentInstallmentAmount.setType(20);
		orderPaymentInstallmentAmount.setAmount(order.getInstallmentAmount());
		orderPaymentInstallmentAmount.setCreateTime(new Date());
		orderPaymentDao.insertSelective(orderPaymentInstallmentAmount);
		map.put("code", 200);
		map.put("message", "ok");
		return map;
	}
	   /**
	  * <p>功能描述：保存所有活动信息。</p>	
	  * @param sku
	  * @param order
	  * <p>创建日期:2016年8月4日 下午3:02:47。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	 * @param discount 
	 * @param activitySkulist 
	  */
	private void saveOrderSkuActivity(Sku sku, Order order, BigDecimal discount, List<Map<String, Object>> activitySkulist) {
		List<OrderSkuActivity> skuActivityList = new ArrayList<OrderSkuActivity>();
		Integer couponRecordId =order.getCouponRecordId();//优惠卷ID
		OrderSkuActivity  orderSkuActivity=null;
		if(couponRecordId!=null){
			CouponRecord couponRecord = couponRecordDao.selectByPrimaryKey(couponRecordId);//查询优惠卷
			couponRecord.setIsUsed(1);
			couponRecord.setUseTime(new Date());
			couponRecordDao.updateByPrimaryKeySelective(couponRecord);
			orderSkuActivity = new OrderSkuActivity();
			orderSkuActivity.setActivityType(3);//优惠卷活动
			orderSkuActivity.setOrderId(order.getId());
			orderSkuActivity.setActivitySkuid(sku.getId());
			orderSkuActivity.setActivityId(couponRecordId);// 优惠卷活动的ID
			orderSkuActivity.setDiscountedPrice(discount);//金额
			orderSkuActivity.setCreateTime(new Date());
			skuActivityList.add(orderSkuActivity);
		}
		Integer activityId = order.getActivityId();//活动信息Idtem
		Activity activity = activityService.findByActivityId(activityId);
		if(activity!=null&&activitySkulist!=null&&activitySkulist.get(0)!=null){
			orderSkuActivity = new OrderSkuActivity();
			orderSkuActivity.setActivityType(activity.getActivityType());//活动类型
			orderSkuActivity.setOrderId(order.getId());
			orderSkuActivity.setActivitySkuid(sku.getId());
			orderSkuActivity.setActivityId(activityId);// 活动ID
			orderSkuActivity.setDiscountedPrice(new BigDecimal(activitySkulist.get(0).get("discountedPrice")==null?"0":activitySkulist.get(0).get("discountedPrice").toString()));//优惠金额
			orderSkuActivity.setCreateTime(new Date());
			skuActivityList.add(orderSkuActivity);
		}
		if(skuActivityList.size()>0){
			activityService.saveOrderSkuActivity(skuActivityList);
		}
	}
	/***
	    * <p>功能描述：保存OrderSku信息快照。。</p>	
	    * @param sku
	    * @param order
	    * @param couponFlag
	    * @param activitySkulist
	    * <p>创建日期:2016年7月29日 下午4:53:56。</p>
	    * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	   */
	private void saveOrderSku(Sku sku, Order order, boolean couponFlag, List<Map<String, Object>> activitySkulist) {
		String imagesOriginal = sku.getImagesOriginal();
		String skuHeadThumbnail = null;
		if (imagesOriginal != null) {
			skuHeadThumbnail = imagesOriginal.split(",")[0];
		}
		String orderSkuAttribute = "";
		List<SkuAttribute> skuAttributes = skuAttributeDao.getListBySkuId(sku
				.getId());
		for (int i = 0; i < skuAttributes.size(); i++) {
			orderSkuAttribute += skuAttributes.get(i).getAttribute().getName()
					+ ":" + skuAttributes.get(i).getAttributeValue() + " ";
		}
		OrderSku orderSku = new OrderSku();
		orderSku.setOrderId(order.getId());
		orderSku.setSkuId(sku.getId());
		orderSku.setSkuName(sku.getName());
		orderSku.setSkuDescription(sku.getDescription());
		orderSku.setSkuDetail(sku.getDetail());
		orderSku.setSkuErpCode(sku.getErpCode());
		orderSku.setSkuHeadThumbnail(skuHeadThumbnail);
		orderSku.setSkuCount(order.getSkuCount());
		SkuExt skuExt = skuExtDao.findSkuExtBySkuId(sku.getId());
		if(skuExt!=null&&sku.getErpCode().equals("koo")){
			orderSku.setCommissionRate(new BigDecimal(koo_commission_rate));
			orderSku.setThirdPartyId(skuExt.getField1());
		}
		if(!StringUtils.isEmpty(order.getActivityId())){//如果参加了活动，商品单价为活动价格
			orderSku.setSkuPrice(new BigDecimal(activitySkulist.get(0).get("activityPrice").toString()));
		}else{
			orderSku.setSkuPrice(sku.getPrice());
		}
		
		orderSku.setSkuOriginalPrice(sku.getOriginalPrice());
		orderSku.setSkuAttribute(orderSkuAttribute);
		if (couponFlag) {
			orderSku.setActivityType(2);
			orderSku.setActivityId(order.getCouponRecordId());
		}
		orderSkuDao.addOrderSku(orderSku);
	}
	/**
	  * <p>功能描述 判断商品是否参加活动。并且记录商品的价格</p>	
	 * @param map 
	 * @param user 
	 * @param order 
	 * @param sku 
	 * @param activitySkulist 
	  * @return
	  * <p>创建日期:2016年7月29日 下午3:50:21。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	private Map<String, Object> getActivity(User user, Order order, Sku sku, List<Map<String, Object>> activitySkulist) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("code", 200);
		map.put("message", "OK");
		logg.info(">>>>>>>>>> 判断商品是否参加活动。并且记录商品的价格>>>>>>>>>>>>>>>>>>");
		Integer custId = user.getId();//用户ID
		int skuCount  = order.getSkuCount();//商品数量
		List<Map<String, Object>>  orderSkuList=null;
		Integer activityId = order.getActivityId();
		Activity activity = activityService.findByActivityId(activityId);
		if(activity==null){
			map.put("code", 400);
			map.put("message", "活动已经取消");
			return map;
		}
		logg.info(">>>>>>>>>> 判断商品是否参加活动。并且记录商品的价格>>>>>>>>>>>>>>>>>>");
		if(activity.getActivityStatus()==2){//秒杀限购活动
			map = activityService.updateActivitySkuList(activity.getId());
			map.put("code", 400);
			map.put("message", "活动已经结束!");
			return map;
		}
		Date nowDate = new Date();
		Date startTime = activity.getStartTime();
		Date endTime = activity.getEndTime();
		if(startTime.after(nowDate)){// 开始时间在 现在时间之后 这活动为开始
			map.put("code", 400);
			map.put("message", "活动尚未开始");
			return map;
		}
		if(activity.getActivityType()==1){
			if(endTime.before(nowDate)){// 结束时间在现在时间之前则活动结束
				map.put("code", 400);
				map.put("message", "活动已经结束");
				return map;
			}
			//秒杀活动如果当前活动状态未开始，但是活动时间正在进行中这修改活动状态为进行中，如果活动状态已经为进行中则不需要修改
			if(startTime.before(nowDate)&&endTime.after(nowDate)&&activity.getActivityStatus()==0){
				activity.setActivityStatus(1);
				map = activityService.updateActivity(activity);
				if(!map.get("code").toString().equals("200")){
					return map;
				}
			}
		}else if(activity.getActivityType()==2){//限购
			if(Integer.parseInt(activitySkulist.get(0).get("reservedInventory").toString())<=0){
				map.put("code", 400);
				map.put("message", "活动商品已经卖完，谢谢您的参与！");
				return map;
			}
			//限购活动若果当前时间在开始时间之后，并且库存大于零，并且活动没有开始则修改当前活动为进行中，如果活动已经是进行中则不需要修改
			if(startTime.before(nowDate)
					&&(Integer.parseInt(activitySkulist.get(0).get("reservedInventory").toString())>0)
					&&activity.getActivityStatus()==0){
				activity.setActivityStatus(1);
			}
		}
		
		if(activitySkulist==null||activitySkulist.size()==0||activitySkulist.get(0)==null){
			map.put("code", 400);
			map.put("message", "该商品没有参加当前活动");
			return map;
		}
		logg.info(activity.getActivityType()+">>>>>>>>>> activity.getActivityType()>>>>>>>>>>>>>>>>>>");
		if(activity.getActivityType()==2){//限购
			if(Integer.parseInt(activitySkulist.get(0).get("reservedInventory").toString())<=0){
				map.put("code", 400);
				map.put("message", "活动商品已经卖完，谢谢您的参与！");
				return map;
			}
			logg.info(activity.getActivityMode()+">>>>>>>>>>activity.getActivityMode()>>>>>>>>>>>>>>>>>>");
//			if(2==activity.getActivityMode()){
				orderSkuList= orderSkuDao.findOrderSkuActivity(order.getSkuId(),custId,activityId);
				if(orderSkuList!=null&&orderSkuList.get(0)!=null){
					if(Integer.parseInt(orderSkuList.get(0).get("skuCount").toString())>=Integer.valueOf(activitySkulist.get(0).get("purchaseNum").toString())){
						map.put("code", 400);
						map.put("message", "对不起,您已经超过该产品的限购个数！这次活动每人限购"+activitySkulist.get(0).get("purchaseNum").toString());
						return map;
					}
				}
//			}
			/*else{//因为没有过多不能超卖
				if(order.getSkuCount()>Integer.parseInt(activitySkulist.get(0).get("purchaseNum").toString())){//因为超过个数所有用户允许超卖
					activitySkulist.get(0).put("ActivityPrice", sku.getPrice());//	用户硬要超过则商品变回原价
				}
			}*/
			// 判断库存
			logg.info(">>>>>>>>>>判断库存>>>>>>>>>>>>>>>>>>");
			if (activitySkulist.get(0) == null || (Integer.parseInt(activitySkulist.get(0).get("reservedInventory").toString()) - skuCount) < 0) {
				map.put("code", 400);
				map.put("message", "您购买的商品库存数量已经不足，请从新选择数量！");
				return map;
			}
			order.setAmount(new BigDecimal(activitySkulist.get(0).get("activityPrice").toString()).multiply(new BigDecimal(skuCount))); 
		}else{//秒杀活动
			logg.info(">>>>>>>>>>秒杀活动>>>>>>>>>>>>>>>>>>");
			orderSkuList= orderSkuDao.findOrderSkuActivity(order.getSkuId(),custId,activityId);
			if(orderSkuList!=null&&orderSkuList.get(0)!=null&&activitySkulist.get(0).get("purchaseNum")!=null){
				if(Integer.parseInt(orderSkuList.get(0).get("skuCount").toString())>=Integer.parseInt(activitySkulist.get(0).get("purchaseNum").toString())){
					map.put("code", 400);
					map.put("message", "对不起,您已经超过该产品的限购个数！这次活动每人限购"+activitySkulist.get(0).get("purchaseNum").toString());
					return map;
				}
				
			}
			logg.info(activitySkulist.get(0).get("reservedInventory").toString()+">>>>>>>>>activitySkulist.get(0).get(reservedInventory).toString()>>>>>>>>>>>>>>>");
			if(order.getSkuCount()>Integer.parseInt(activitySkulist.get(0).get("reservedInventory").toString())){
				// 判断库存
				if (sku.getInventory() == null|| sku.getInventory() < 0) {
					map.put("code", 400);
					map.put("message", "库存不足");
					return map;
				}
			}
			order.setAmount(new BigDecimal(activitySkulist.get(0).get("activityPrice").toString()).multiply(new BigDecimal(skuCount))); 	
		}
		map.put("code", 200);
		map.put("order", order);
		return map;
	}
	// 生成码
	private String createCode(int n) {
		String code = "";
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			code += String.valueOf(random.nextInt(10));
		}
		return code;
	}
	/**
	* @Title: getTagIds
	* 获取所有子节点
	* @return String    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	private String getTagIds(Integer tagId) {
		String tagIds = tagId+"";	
		List<Tag> tagList = tagDao.getByParentId(tagId);
		for (Tag tag : tagList) {
			tagIds =tagIds+ "," + tag.getId() ;
			getTagIds(tag.getId());
		}
		return tagIds;
	}
	public int getOrderCountByCustIdAndStatus(Integer custId, Integer status) {
		return orderDao.getByCustIdAndStatus(custId, status);
	}



	@Override
	public Order getById(Integer id) {
		return orderDao.getById(id);
	}

	/**
	 * 我的订单列表
	 * */
	public List<Order> getOrderList(Integer status, Integer custId,
			Integer offset, Integer limit) {
		// 按退货时间排序
		if (status != null && status == 8) {
			return orderDao.orderListReturn(status, custId, offset, limit);
		}
		return orderDao.orderList(status, custId, offset, limit);
	}

	/**
	 * 我的订单列表总条数
	 * */
	public int getOrderListCount(Integer status, Integer custId) {
		return orderDao.getOrderListCount(status, custId);
	}

	/**
	 * 订单详情
	 * */
	public Order getOrderDetail(Integer id, Integer custId) {
		return orderDao.orderDetail(id, custId);
	}

	@Override
	public String getOrderNo() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		Date now = new Date();
		String dateStr = sdf.format(now);
		// 生成5位序列号
		Integer maxSeqNo = seqNoDao.getMaxSeqNo(dateStr);
		if (maxSeqNo == null) {
			maxSeqNo = 0;
		}
		SeqNo seqNoEntity = new SeqNo();
		seqNoEntity.setKey(dateStr);
		seqNoEntity.setSeqNo(maxSeqNo + 1);
		seqNoDao.addSeqNo(seqNoEntity);
		seqNoEntity = seqNoDao.getById(seqNoEntity.getId());
		int seqNo = seqNoEntity.getSeqNo();
		String seqNoStr = String.valueOf(100000 + seqNo).substring(1);
		// 序列号前面加1位随机数字，分成两部分
		int rand = (int) (Math.random() * 10);
		seqNoStr = rand + seqNoStr;
		// 分别按数据库字典替换
		DataOrderNoMapping mapping1 = dataOrderNoMappingDao
				.getBySource(seqNoStr.substring(0, 3));
		DataOrderNoMapping mapping2 = dataOrderNoMappingDao
				.getBySource(seqNoStr.substring(3));
		// 第二位和第四位对换
		StringBuilder sb = new StringBuilder(mapping1.getTarget()
				+ mapping2.getTarget());
		char ch2 = sb.charAt(1);
		char ch4 = sb.charAt(3);
		sb.setCharAt(1, ch4);
		sb.setCharAt(3, ch2);

		return "8" + sb.toString() + dateStr;
	}

	/**
	 * 支付分期
	 */
	@Override
	public Map<String, Object> paymentBefore(Integer order_id,
			String payPassword, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		// 信分宝创建订单+分期
		if (connectedToXfb) {
			// 支付密码
			Md5 md5 = new Md5();
			if (!user.getPayPassword().equals(md5.getMD5ofStr(payPassword))) {
				map.put("code", 400);
				map.put("message", "支付密码错误");
				return map;
			}
			Order order = orderDao.getById(order_id);
			/******下订单之前订单是否是有效订单 开始***/
			map = getOrderIsSuccessOrder(order,user);
			Integer code = (Integer) map.get("code");
			if(code!=200){
				return map;
			}
			/******下订单之前订单是否是有效订单结束***/
			List<OrderSku> lists = orderSkuDao.getByOrderId(order.getId());		
			if(lists==null||lists.size()<=0){
				map.put("code", 400);
				map.put("message", "没有找到该订单的商品！");
				return map;
			}
			// 先登录信分宝
			String json = HttpClientUtil.httpLogin(xfbInterface, "",user.getMobile(), user.getPassword(), xfbVersion);
			JSONObject obj = JSONObject.fromObject(json);
			if (obj != null && !obj.isEmpty()) {
				if ("0".equals(obj.getString("resultCode"))) {
					String response = "";
					// 先判断是否有首付，并且首付未支付，如果是该情况，则调用创建订单接口，只扣额度，不生成账单
					if (order.getDownPayment().compareTo(new BigDecimal(0)) > 0 && !order.getDownPaymentPayed()) {
						response = HttpClientUtil.httpCreateOrder(xfbInterface, user, obj.getString("userId"), order,lists.get(0), xfbVersion);
					}else{
						response = HttpClientUtil.httpCreateOrderAndStage(xfbInterface, user, obj.getString("userId"), order,lists.get(0), xfbVersion);
					}
					JSONObject result = JSONObject.fromObject(response);
					int resultCode = result.getInt("resultCode");
					if (resultCode != 0) {
						map.put("code", 500);
						map.put("resultCode", resultCode);
						map.put("message", result.getString("resultMessage"));
						return map;
					} else {
						// 修改订单 installment_time为当前时间 installment_payed改为true
						// 订单支付类型 1: 全款 2: 全分期 3: 首付+分期'
						order.setInstallmentTime(new Date());
						order.setInstallmentPayed(true);
						// 判断首付和分期是否都完成
						if((order.getDownPayment().compareTo(new BigDecimal(0)) > 0 && !order.getDownPaymentPayed()) || 
								(order.getInstallmentAmount().compareTo(new BigDecimal(0)) > 0 && !order.getInstallmentPayed())){
							// 说明至少有一种未支付，什么都不用做
						}else{
							// 支付完成，修改订单状态，减库存
							if(lists.get(0).getSkuErpCode().equals("koo")){//如果是新东发产品
								order.setStatus(6);
							}else{
								// 支付完成，修改订单状态，减库存
								order.setStatus(2); // 0: 草稿 1: 未付款 2: 已付款 3: 已确认 4:已制单 5: 已发货 6: 已签收 9: 已取消',	
							}							
							// 减库存
							skuService.subtractInventory(order.getId());
						}
						// 与信分宝分期id关联
						order.setStageId(result.getString("resultMessage"));
						int num = orderDao.updateOrder(order);
						if (num == 1) {
							//交易完成  修改 t_order_payment
							orderPaymentDao.updateOrderPaymentStatus(order.getId(), 1);
							map.put("code", "200");
							map.put("orderId", order.getId());
							map.put("message", "OK");
						} else {
							map.put("code", 400);
							map.put("message", "更新数据库失败");
						}
					}
				} else {
					map.put("code", 500);
					map.put("resultCode", obj.getString("resultCode"));
					map.put("message", obj.getString("resultMessage"));
					return map;
				}
			}
		}
		return map;
	}
    
	 /**
	  * <p>功能描述：判断订单是否有效。</p>	
	 * @param map 
	  * @param order
	 * @param user 
	  * @return
	  * <p>创建日期:2016年8月12日 上午10:05:56。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	public Map<String, Object> getOrderIsSuccessOrder(Order order, User user) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", 200);
		map.put("message", "OK");
		
		logg.info(">>>>>>>>>>进入回调接口>>>>>>>>>>>>>>>>>>");
		if (order == null) {
			map.put("code", 404);
			map.put("message", "未找到（当前用户下的）该订单");
			return map;
		}
		logg.info(order.getId()+">>>>>>>>>>支付校验>>>>>>>>>>>>>>>>>>");
		// 判断库存
		List<OrderSku> lists = orderSkuDao.getByOrderId(order.getId());
		if (lists != null && lists.size() != 0) {
			List<OrderSkuActivity> orderSkuActivitySkuList = activityService.findOrderSkuActivityList(order.getId(),lists.get(0).getSkuId());
			logg.info(order.getId()+">>>>>>>>>>支付校验>>>>>>>>>>>>>>>>>>"+lists.get(0).getSkuId());
			Sku sku = skuDao.getById(lists.get(0).getSkuId());
			if(orderSkuActivitySkuList==null||orderSkuActivitySkuList.size()<=0||orderSkuActivitySkuList.get(0)==null){
				if (sku.getInventory() == null|| sku.getInventory() - lists.get(0).getSkuCount() < 0) {
					map.put("code", 400);
					map.put("message", "库存不足");
					return map;
				}
			}else{
				List<Map<String, Object>> activitySkulist = activityService.findSpikeActivitySkuList(orderSkuActivitySkuList.get(0).getActivityId(),lists.get(0).getSkuId());
				order.setActivityId(orderSkuActivitySkuList.get(0).getActivityId());
				order.setSkuId(lists.get(0).getSkuId());
				order.setSkuCount(lists.get(0).getSkuCount());
				logg.info(">>>>支付校验>>>>>> 判断商品是否参加活动。并且记录商品的价格>>>>>>>>>>>>>>>>>>");
				Integer custId = user.getId();//用户ID
				int skuCount  = order.getSkuCount();//商品数量
				List<Map<String, Object>>  orderSkuList=null;
				Integer activityId = order.getActivityId();
				Activity activity = activityService.findByActivityId(activityId);
				if(activity==null){
					map.put("code", 400);
					map.put("message", "活动已经取消");
					return map;
				}
				logg.info(">>>>>>支付校验>>>> 判断商品是否参加活动。并且记录商品的价格>>>>>>>>>>>>>>>>>>");
				if(activity.getActivityStatus()==2){//活动已经结束!
					map = activityService.updateActivitySkuList(activity.getId());
					map.put("code", 400);
					map.put("message", "活动已经结束!");
					return map;
				}
				Date nowDate = new Date();
				Date startTime = activity.getStartTime();
				Date endTime = activity.getEndTime();
				if(startTime.after(nowDate)){// 开始时间在 现在时间之后 这活动为开始
					map.put("code", 400);
					map.put("message", "活动尚未开始");
					return map;
				}
				if(activity.getActivityType()==1){//秒杀
					if(activity.getActivityStatus()==1){//活动正在进行中
						//判断当前时间是不是超过活动时间，超过这修改活动状态，以及把所有该活动的没有付款的订单改为无效状态
						if(endTime.before(nowDate)){
							activity.setActivityStatus(2);
							map = activityService.updateActivity(activity);
							map = activityService.updateActivitySkuList(activity.getId());
						}
					}
					
				}else if(activity.getActivityType()==2){//限购
					if(activity.getActivityStatus()==1){//活动正在进行中
						//判断当前时间是不是超过活动时间，超过这修改活动状态，以及把所有该活动的没有付款的订单改为无效状态
						if(Integer.parseInt(activitySkulist.get(0).get("reservedInventory").toString())<=0){
							activity.setActivityStatus(2);
							map = activityService.updateActivity(activity);
							map = activityService.updateActivitySkuList(activity.getId());
						}
					}
					if(Integer.parseInt(activitySkulist.get(0).get("reservedInventory").toString())<=0){
						map.put("code", 400);
						map.put("message", "活动已经结束，谢谢您的参与！");
						return map;
					}
					
				}
				
				if(activitySkulist==null||activitySkulist.size()==0||activitySkulist.get(0)==null){
					map.put("code", 400);
					map.put("message", "该商品没有参加当前活动");
					return map;
				}
				logg.info(activity.getActivityType()+">>>>>>>支付校验>>> activity.getActivityType()>>>>>>>>>>>>>>>>>>");
				if(activity.getActivityType()==2){//限购
					if(Integer.parseInt(activitySkulist.get(0).get("reservedInventory").toString())<=0){
						map.put("code", 400);
						map.put("message", "活动商品已经卖完，谢谢您的参与！");
						return map;
					}
					logg.info(activity.getActivityMode()+">>>>>>支付校验>>>>activity.getActivityMode()>>>>>>>>>>>>>>>>>>");
//					if(2==activity.getActivityMode()){
					orderSkuList= orderSkuDao.findOrderSkuActivity(order.getSkuId(),custId,activityId);
					logg.info(orderSkuList.get(0).get("skuCount").toString()+">>>>支付校验>>>>>>activity.getActivityMode()>>>>>>>>>>>>>>>>>>");
					if(orderSkuList!=null&&orderSkuList.get(0)!=null){
						if(Integer.parseInt(orderSkuList.get(0).get("skuCount").toString())>Integer.valueOf(activitySkulist.get(0).get("purchaseNum").toString())){
							map.put("code", 400);
							map.put("message", "对不起,您已经超过该产品的限购个数！这次活动每人限购"+activitySkulist.get(0).get("purchaseNum").toString()+"个!");

							return map;
						}
					}
//					}
					/*else{//因为没有过多不能超卖
						if(order.getSkuCount()>Integer.parseInt(activitySkulist.get(0).get("purchaseNum").toString())){//因为超过个数所有用户允许超卖
							activitySkulist.get(0).put("ActivityPrice", sku.getPrice());//	用户硬要超过则商品变回原价
						}
					}*/
					// 判断库存
					logg.info(">>>>>>>>支付校验>>判断库存>>>>>>>>>>>>>>>>>>");
					if(Integer.parseInt(activitySkulist.get(0).get("reservedInventory").toString())>0&&order.getSkuCount()>Integer.parseInt(activitySkulist.get(0).get("reservedInventory").toString())){
 
						map.put("code", 400);
						map.put("message", "当前库存为"+activitySkulist.get(0).get("reservedInventory").toString()+"个，您购买的商品数量多于剩余库存数,请您取消重新下单!");
						return map;
						
					}
					if(Integer.parseInt(activitySkulist.get(0).get("reservedInventory").toString())<=0){
						map.put("code", 400);
						map.put("message", "活动商品已经卖完，谢谢您的参与！");
						return map;
					}
					order.setAmount(new BigDecimal(activitySkulist.get(0).get("activityPrice").toString()).multiply(new BigDecimal(skuCount))); 
				}else{//秒杀活动
					logg.info(">>>>>>支付校验>>>>秒杀活动>>>>>>>>>>>>>>>>>>");
					orderSkuList= orderSkuDao.findOrderSkuActivity(order.getSkuId(),custId,activityId);
					if(orderSkuList!=null&&orderSkuList.get(0)!=null&&activitySkulist.get(0).get("purchaseNum")!=null){
						if(Integer.parseInt(orderSkuList.get(0).get("skuCount").toString())>Integer.parseInt(activitySkulist.get(0).get("purchaseNum").toString())){
							map.put("code", 400);
							map.put("message", "对不起,您已经超过该产品的限购个数！这次活动每人限购"+activitySkulist.get(0).get("purchaseNum").toString()+"个!");
							return map;
						}
					}
					logg.info(activitySkulist.get(0).get("reservedInventory").toString()+">>>>>>>>>activitySkulist.get(0).get(reservedInventory).toString()>>>>>>>>>>>>>>>");
					if(Integer.parseInt(activitySkulist.get(0).get("reservedInventory").toString())>0&&order.getSkuCount()>Integer.parseInt(activitySkulist.get(0).get("reservedInventory").toString())){
						map.put("code", 400);
						map.put("message", "当前库存为"+activitySkulist.get(0).get("reservedInventory").toString()+"个，您购买的商品数量多于剩余库存数,请您取消重新下单!");
						return map;
						
					}
					if(Integer.parseInt(activitySkulist.get(0).get("reservedInventory").toString())<=0){
						map.put("code", 400);
						map.put("message", "活动商品已经卖完，谢谢您的参与！");
						return map;
					}
					order.setAmount(new BigDecimal(activitySkulist.get(0).get("activityPrice").toString()).multiply(new BigDecimal(skuCount))); 	
				}
				map.put("code", 200);
				map.put("order", order);
				return map;
			
			}
			
		} else {
			map.put("code", 400);
			map.put("message", "商品未找到");
			return map;
		}
		return map;
	}
	@Override
	public int updateOrder(Order order) {
		return orderDao.updateOrder(order);
	}

	@Override
	public Order getByNo(String no) {
		return orderDao.getByNo(no);
	}

	@Override
	public int updateNameAndMobile(Map<String, Object> param) {
		return orderDao.updateNameAndMobile(param);
	}

	@Override
	public List<Order> getOrderSkuList(Map<String, Object> params) {
		// 按退货时间排序
		// if (status != null && status == 8) {
		// return orderDao.orderListReturn(status, custId, offset, limit);
		// }
		return orderDao.selectOrderSkusList(params);
	}

	@Override
	public Long getOrderSkuListCount(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return orderDao.selectOrderSkusCount(params);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Map<String, Object> addSkusOrder(User user, Order order) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 先把优惠券的id 拿出来
		Integer couponRecordId = order.getCouponRecordId();

		// 把自提点id 先拿出来
		Integer pickupPointId = order.getPickupPointId();

		// 1 保存订单记录
		Integer custId = user.getId();

		// 准备移除购物车商品
		List<Cart> cart = order.getCart();

		// 1.1 生成订单号
		order.setNo(noService.getOrderNo());
		// 处理 order_sku
		List<OrderSku> orderSkus = order.getOrderSku();
		// 使用优惠 金额 初始化
		BigDecimal discount = new BigDecimal(0.00);
		// 检查是否 为 自提单子
		int pickupNum = 0;
		// 检查 新东方 订单
		int kooNum = 0;
		// 页面传过来的分期数
		Integer installment = order.getInstallmentMonths();
		// 商品 总价格
		BigDecimal totalPrice = new BigDecimal(0.00);
		// 使用优惠券的商品总价格
		BigDecimal couponSkusAmount = new BigDecimal(0.00);
		/******** 验证商品 可购买性  start******************/
		
		for (OrderSku orderSku : orderSkus) {
			Sku sku = skuDao.getById(orderSku.getSkuId());
			if (sku == null) {
				map.put("code", 400);
				map.put("message", "序号" + orderSku.getSkuId() + "商品未找到");
				return map;
			}
			if (sku.getStatus() == 0) {
				map.put("code", 400);
				map.put("message", sku.getName() + "商品已下架");
				return map;
			}
			SkuExt skuExt = skuExtDao.findSkuExtBySkuId(orderSku.getSkuId());// 保存第三方金额信息
			if (skuExt != null && skuExt.getField9() != null) {
				order.setThirdPartyAmount(new BigDecimal(skuExt.getField9()));
			}
			Integer skuCount = orderSku.getSkuCount();
			/****校验商品是不是活动商品*****/
			Integer activityId = orderSku.getActivityId();
			if(!StringUtils.isEmpty(activityId)){
				map = getOrderActivityPrice(sku,skuCount,custId,orderSku);
				if(map.get("code").equals(400)){
					return map ; 
				}else{
					sku=(Sku) map.get("sku");
				}
			}else{
				// 判断库存
				if (sku.getInventory() == null|| (sku.getInventory() - skuCount) < 0) {
					map.put("code", 400);
					map.put("message", sku.getName() + "库存不足");
					return map;
				}
			}
			/****校验商品是不是活动商品结束*****/
			// 计算商品总价格
			totalPrice = sku.getPrice().multiply(new BigDecimal(skuCount)).add(totalPrice);

			Boolean installmentCheck = false;
			if (sku.getPickup() == 1) {
				pickupNum = pickupNum + 1;
			}
			if (sku.getErpCode().equals("koo")) {
				kooNum = kooNum + 1;
			}
			String skuIinstallmentMonths = sku.getInstallment();
			if (skuIinstallmentMonths != null
					&& !skuIinstallmentMonths.equals("")) { // 确定一下是不是能分期
				String[] months = skuIinstallmentMonths.split(",");
				for (String mo : months) {
					if (Integer.parseInt(mo) >= installment) {
						installmentCheck = true;
					}
				}
				if (!installmentCheck) {
					map.put("code", 400);
					map.put("message", "分期错误");
					return map;
				}
			} else {
				map.put("code", 400);
				map.put("message", "分期错误");
				return map;
			}
		}
		if (pickupNum != 0 && pickupNum != orderSkus.size()) {
			map.put("code", 400);
			map.put("message", "自提商品不能与普通商品一起结算");
			return map;
		} else if (kooNum != 0 && kooNum != orderSkus.size()) {
			map.put("code", 400);
			map.put("message", "新东方商品不能与普通商品一起结算");
			return map;
		}
		if(pickupNum>0 || kooNum>0 ){
			//自提 订单和 新东方 不存地址
		}else{
			// 获取默认地址
			Address address = addressDao.getByUserIdOrId(
					order.getAddressId(), custId);
			if (address == null) {
				map.put("code", 400);
				map.put("message", "地址未找到");
				return map;
			}
			order.setAddressMobile(address.getMobile());
			order.setAddressName(address.getUsername());
			order.setAddressFullText(address.getFullText());
			order.setAddressZip(address.getZip());
		}
		
		/******** 验证商品 可购买性  end******************/
		// 分两种情况做操作 1 使用优惠券 2 不使用
		/**************** 优惠券 验证 start ***************************************/
		// 存放skuList 用于和优惠券 skuId ，该sku总价
		List<List<Number>> skuList = new ArrayList<List<Number>>();
		if (order.getCouponRecordId() != null) {
			map = couponCheck(order, custId, orderSkus, skuList, couponSkusAmount);
			if(map.containsKey("tempList")&&map.containsKey("couponUse")){
				skuList = (List<List<Number>>) map.get("tempList");
				discount = new BigDecimal(String.valueOf(map.get("couponUse")));
				couponSkusAmount =new BigDecimal(String.valueOf( map.get("couponSkusAmount")));
			}else{
				return map ; 
			}
		}
		/********************** 优惠券验证 end *************************/

		order.setAmount(totalPrice);
		/********************** 处理sku 和优惠券关系 计算出 每个sku的实际支付价格 *************************/
		// skuList
		if (installment != null) {
			order.setInstallmentMonths(installment);
		}

		// 预设首付支付状态 ， 用于t_order_payment
		Integer paymentType = 10; // 支付方式，10: 微信支付 20: 分期 11: 代金券
		/**********选择支付方  start ************************/
		map = choosePayType(order,discount,user,paymentType);
		if(map.containsKey("payOrder")){
			order = (Order) map.get("payOrder") ; 
		}else{
			return map ;
		}
		/**********选择支付方  end ************************/
		// 生成订单
		int affectedRows = orderDao.addOrder(order);
		if (affectedRows < 1) {
			return null;
		}
		// 下面开始插入订单 ，然后插入 order_sku
		Integer orderId = order.getId();
		order = orderDao.getById(order.getId());
		map.put("data", order);
		//下面所有操作 遇到异常或错误必须 抛出异常 使 事务回滚
		/******插入 t_order_sku  记录表  start********************/
		 insertOrderSku(orderSkus, orderId,skuList,couponSkusAmount,discount);
		/******插入 t_order_sku  记录表  end********************/
		/*******保存活动信息开始********/
		insertOrderSkuActivity(orderSkus,order,discount);
		/*******保存活动信息结束********/
		/*****插入自提订单 start ***********/
		insertPickupOrder( order , pickupPointId, pickupNum);
		/*****插入自提订单 end  ***********/
		
		/***** 插入优惠券信息 start  ***********/
		if (couponRecordId != null) {
			insertCoupon(couponRecordId) ; 
		}
			
		/***** 插入优惠券信息 end ***********/

		/******插入流水记录  start ********************/
		insertOrderPayment(order ,paymentType,couponRecordId , discount);
		
		/******插入流水记录  end ********************/

		/******移除购物车  start ********************/
		if (cart != null)
		removeCart(cart);
		/******移除购物车  end ********************/
		LogUtil.log(user.getId(), user.getMobile(), order.getId(),  LogEnum.OperateModel.ORDER.getKey(), LogEnum.Action.TIJIAODINGDAN.getKey(), LogEnum.Action.TIJIAODINGDAN.getValue(), LogEnum.Source.SHOP.getKey(), 1);
		map.put("code", 200);
		map.put("message", "ok");
		return map;

	}
 
	/**
	* @Title: insertPickupOrder
	* @Description:  插入自提订单
	* @return void    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	private void insertPickupOrder(Order order ,Integer pickupPointId,Integer pickupNum ) {
		if (pickupNum != 0) {
			if (pickupPointId == null) {
				throw new CommonException("自提点不存在");
			} else {
				PickupPoint pickupPoint = pickupPointDao
						.selectByPrimaryKey(pickupPointId);
				if (pickupPoint == null) {
					throw new CommonException("自提点不存在");
				}
			}
			PickupOrder pickupOrder = new PickupOrder();
			pickupOrder.setPickupPointId(pickupPointId);
			pickupOrder.setOrderId(order.getId());
			pickupOrder.setCreateTime(new Date());
			pickupOrder.setStatus(0);
			String code = "";
			Integer num = 0;
			do {
				// 生成6位随机码
				code = createCode(6);
				num = pickupOrderDao.selectCodeNum(code);
			} while (num > 0);
			pickupOrder.setCode(code);
			pickupOrderDao.insertSelective(pickupOrder);
		}
	}
	private Map<String, Object> choosePayType(Order order, BigDecimal discount, User user, Integer paymentType) {
		Map<String, Object> map  = new HashMap<String, Object>(); 
		// 判断可用额度是否足够
		if (connectedToXfb) {
			// 判断是否需要分期支付
			if (order.getDownPayment().compareTo(order.getAmount()) < 0) {
				// 判断请求参数是否合法
				if ((order.getDownPayment().add(order.getInstallmentAmount()))
						.compareTo(order.getAmount()) != 0) {
					throw new CommonException("请求参数不合法");
				}

				// 设置订单支付类型 1: 全款 2: 全分期 3: 首付+分期
				if (order.getDownPayment().compareTo(new BigDecimal(0)) == 0) {
					order.setPayMethod(2);
				} else {
					order.setPayMethod(3);
				}

				// 如果用了优惠券
				if (discount.compareTo(new BigDecimal(0.00)) > 0) {
					// 优惠券金额>首付款时
					if (order.getDownPayment().compareTo(discount) < 1) {
						// 首付<=优惠券金额 则首付减成0 ；分期 = 分期-优惠券金额
						paymentType = 11;// 优惠
						// 如果分期金额+首付金额>优惠金额
						if (order.getInstallmentAmount()
								.add(order.getDownPayment())
								.compareTo(discount) > -1) {
							order.setInstallmentAmount(order
									.getInstallmentAmount().subtract(discount)
									.add(order.getDownPayment()));
						} else {
							if ((order.getDownPayment().add(order
									.getInstallmentAmount())).compareTo(order
									.getAmount()) != 0) {
								throw new CommonException("请求参数不合法");
							}
						}
					}
				}
				// 分期金额>0时调用信分宝 wyy 2016/07/05
				if (order.getInstallmentAmount().compareTo(new BigDecimal(0)) > 0) {
					// 先登录信分宝
					String json = HttpClientUtil.httpLogin(xfbInterface, "",
							user.getMobile(), user.getPassword(), xfbVersion);
					JSONObject obj = JSONObject.fromObject(json);
					if (obj != null && !obj.isEmpty()) {
						if ("0".equals(obj.getString("resultCode"))) {
							String response = HttpClientUtil.httpGetUserScore(
									xfbInterface, user,
									obj.getString("userId"), xfbVersion);
							JSONObject result = JSONObject.fromObject(response);
							int resultCode = result.getInt("resultCode");
							if (resultCode != 0) {
								/*map.put("code", 500);  事务处理
								map.put("resultCode", resultCode);
								map.put("message",result.getString("resultMessage")
										);
								return map;*/
								throw new CommonException(result.getString("resultMessage"));
							} else {
//								Map<String, Object> data = new HashMap<String, Object>();
								// 判断可用额度
								if (order.getInstallmentAmount().compareTo(
										new BigDecimal(result
												.getString("useSum"))) == 1) {
//									map.put("code", 404);
//									map.put("message", "可用信用额度不足");
//									data.put("creditSum",
//											result.getString("creditSum"));
//									data.put("usableSum",
//											result.getString("useSum"));
//									map.put("data", data);
//									return map;
									throw new CommonException("可用信用额度不足");
								}
							}
						} else {
//							map.put("code", 500);
//							map.put("resultCode", obj.getString("resultCode"));
//							map.put("message", obj.getString("resultMessage"));
//							return map;
							throw new CommonException(obj.getString("resultMessage"));
						}
					}
					// 设置订单支付类型
					if (order.getDownPayment().compareTo(new BigDecimal(0)) == 0) {
						order.setPayMethod(2);
					} else {
						order.setPayMethod(3);
					}
				}
				// 设置分期费率
				order.setInstallmentRate(new BigDecimal(SysConfig.rateConfigMap
						.get(String.valueOf(order.getInstallmentMonths()))));
			} else {
				// 判断请求参数是否合法
				if (order.getDownPayment().compareTo(order.getAmount()) == 0) {
					// 全首付
					paymentType = 10;
					order.setPayMethod(1);
				} else {
//					map.put("code", 400);
//					map.put("message", "请求参数不合法...");
//					return map;
					throw new CommonException("参数不合法");
				}
			}
		} else if ("ceb".equals("ceb")) {
			// 光大商城，无首付，全分期
			paymentType = 20; // 分期
			order.setDownPayment(new BigDecimal(0));
			if (order.getAmount().compareTo(discount) > -1) {
				order.setInstallmentAmount(order.getAmount().subtract(discount));
			} else {
				order.setInstallmentAmount(new BigDecimal(0.01));
			}

			order.setPayMethod(2);
		} else {
			// 全首付
			order.setPayMethod(1);
			if (order.getDownPayment().compareTo(order.getAmount()) != 0) {
//				map.put("code", 400);
//				map.put("message", "请求参数不合法");
//				return map;
				throw new CommonException("参数不合法");
			}
			if (order.getDownPayment().compareTo(discount) > -1) {
				order.setDownPayment(order.getDownPayment().subtract(discount));
			} else {
				// 强制微信支付0.01
				order.setDownPayment(new BigDecimal(0.01));
			}

		}
		map.put("payOrder", order);
		return map;
	}
	/**
	* @Title: removeCart
	* @Description: 移除购物车商品
	* @return void    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	private void removeCart(List<Cart> cart) {
		for (Cart c : cart) {
			logg.info("开始移除购物商品");
			c = cartDao.selectByPrimaryKey(c.getId());
			if (c != null) {
				c.setStatus(2);
				cartDao.updateByPrimaryKeySelective(c);
			}
		}
	}
	/**
	* @Title: insertOrderPayment
	* @Description:  添加 流水信息
	* @return void    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	private void insertOrderPayment(Order order, Integer paymentType,
			Integer couponRecordId, BigDecimal discount) {
		// 插入订单优惠金额
		if (couponRecordId != null) {
			OrderPayment orderPaymentDiscount = new OrderPayment();
			orderPaymentDiscount.setOrderId(order.getId());
			orderPaymentDiscount.setCategory(3);// 其他支付方式
			orderPaymentDiscount.setType(11);
			orderPaymentDiscount.setAmount(discount);
			orderPaymentDiscount.setSource(couponRecordId);
			orderPaymentDiscount.setCreateTime(new Date());
			orderPaymentDao.insertSelective(orderPaymentDiscount);
		}
		// 插入首付金额
		OrderPayment orderPaymentDownPayment = new OrderPayment();
		orderPaymentDownPayment.setOrderId(order.getId());
		orderPaymentDownPayment.setCategory(1);
		orderPaymentDownPayment.setType(paymentType);
		orderPaymentDownPayment.setAmount(order.getDownPayment());
		orderPaymentDownPayment.setCreateTime(new Date());
		orderPaymentDao.insertSelective(orderPaymentDownPayment);
		// 插入订单分期金额
		OrderPayment orderPaymentInstallmentAmount = new OrderPayment();
		orderPaymentInstallmentAmount.setOrderId(order.getId());
		orderPaymentInstallmentAmount.setCategory(2);
		orderPaymentInstallmentAmount.setType(20);
		orderPaymentInstallmentAmount.setAmount(order.getInstallmentAmount());
		orderPaymentInstallmentAmount.setCreateTime(new Date());
		orderPaymentDao.insertSelective(orderPaymentInstallmentAmount);
	}
	/**
	* @Title: insertCoupon
	* @Description: 添加优惠券使用信息
	* @return void    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	private void insertCoupon(Integer couponRecordId) {
		Date now = new Date();
		CouponRecord couponRecord = couponRecordDao
				.selectByPrimaryKey(couponRecordId);
		couponRecord.setIsUsed(1);
		couponRecord.setUseTime(now);
		couponRecordDao.updateByPrimaryKeySelective(couponRecord);
		// 修改优惠券的使用数量
		Coupon coupon = couponDao.selectByPrimaryKey(couponRecord
				.getCouponId());
		coupon.setUsedNum(coupon.getUsedNum() + 1);
		couponDao.updateByPrimaryKey(coupon);
	}
	/**
	* @Title: insertOrderSku
	* @Description: 保存 orderSku
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	private void insertOrderSku(List<OrderSku> orderSkus, Integer orderId, List<List<Number>> skuList, BigDecimal couponSkusAmount, BigDecimal discount) {
		BigDecimal couponMountCount=new BigDecimal(0);
		int orderSkuIndex = 0;
		for (OrderSku orderSku : orderSkus) {
			Sku sku = skuDao.getById(orderSku.getSkuId());
			/**********增加活动判断 如果活动进行中 则更改为活动价格 start******************/
			Integer activityId = orderSku.getActivityId() ; 
			if(activityId !=null){
				ActivitySku activitySku = activitySkuDao.getActiveBySku(orderSku.getSkuId());
				if(!activitySku.getActivityId().equals(activityId)){
					throw new CommonException("活动错误");
				}else{
					sku.setPrice(activitySku.getActivityPrice());
				}
			}
			/**********增加活动判断 end ******************/
			// 处理优惠券 金额 分配到每件商品上
			BigDecimal factAmount = new BigDecimal(0.00);
			BigDecimal avgPrice = new BigDecimal(0.00);
			if (skuList.size() > 0) {
				for (List<Number> list : skuList) {
					if (list.get(0).equals(sku.getId())) {
						// 该商品实付总金额
						MathContext mc = new MathContext(2,
								RoundingMode.HALF_DOWN);
						BigDecimal conponMount = new BigDecimal(0);
						if(orderSkuIndex==(skuList.size()-1)){//最后一次，不按比例计算，而是按照总优惠价格减去其他商品优惠的价格
							conponMount = discount.subtract(couponMountCount);
						}else{
							conponMount = new BigDecimal(String
									.valueOf(list.get(1))).divide(
											couponSkusAmount, mc)
											.multiply(discount);
							couponMountCount = couponMountCount.add(conponMount);
						}
						factAmount = new BigDecimal(Double.valueOf(String
								.valueOf(list.get(1))))
								.subtract(conponMount);
						// 实付单价
						avgPrice = factAmount.divide(new BigDecimal(orderSku
								.getSkuCount()));
						orderSku.setAvgPrice(avgPrice == null ? sku.getPrice()
								: avgPrice);
						orderSku.setFactAmount(factAmount == null ? sku
								.getPrice().multiply(
										new BigDecimal(orderSku.getSkuCount()))
								: factAmount);
						orderSkuIndex++;
					}
				}
			} else {
				avgPrice = sku.getPrice();
				factAmount = sku.getPrice().multiply(
						new BigDecimal(orderSku.getSkuCount()));
				orderSku.setAvgPrice(avgPrice == null ? sku.getPrice()
						: avgPrice);
				orderSku.setFactAmount(factAmount == null ? sku
						.getPrice().multiply(
								new BigDecimal(orderSku.getSkuCount()))
						: factAmount);
			}

			String orderSkuAttribute = "";
			List<SkuAttribute> skuAttributes = skuAttributeDao
					.getListBySkuId(sku.getId());
			for (int i = 0; i < skuAttributes.size(); i++) {
				orderSkuAttribute += skuAttributes.get(i).getAttribute()
						.getName()
						+ ":" + skuAttributes.get(i).getAttributeValue() + " ";
			}
			String imagesOriginal = sku.getImagesOriginal();
			String skuHeadThumbnail = null;
			if (imagesOriginal != null) {
				skuHeadThumbnail = imagesOriginal.split(",")[0];
			}
			orderSku.setOrderId(orderId);
			orderSku.setSkuId(sku.getId());
			orderSku.setSkuName(sku.getName());
			orderSku.setSkuDescription(sku.getDescription());
			orderSku.setSkuDetail(sku.getDetail());
			orderSku.setSkuErpCode(sku.getErpCode());
			orderSku.setSkuHeadThumbnail(skuHeadThumbnail);
			orderSku.setSkuCount(orderSku.getSkuCount());
			orderSku.setSkuPrice(sku.getPrice());
			orderSku.setSkuOriginalPrice(sku.getOriginalPrice());
			orderSku.setSkuAttribute(orderSkuAttribute);
			if("koo".equals(sku.getErpCode())){
				SkuExt skuExt = skuExtDao.findSkuExtBySkuId(sku.getId());
				if(skuExt!=null){
					orderSku.setCommissionRate(new BigDecimal(koo_commission_rate));
					orderSku.setThirdPartyId(skuExt.getField1());	
				}

			}
			orderSkuDao.addOrderSku(orderSku);
		}
	}
	/**
	* @Title: couponCheck
	* @Description: 下单方法    用于判断优惠券是否可用
	* @return Map<String,Object>    返回类型
	* @author Yan Zuoyu
	* @throws
	 */
	public Map<String,Object> couponCheck(Order order,Integer custId,List<OrderSku> orderSkus,List<List<Number>> skuList,BigDecimal couponSkusAmount){
		Map<String,Object> map = new HashMap<String, Object>();
		// 1.获取正要使用的优惠券信息|判断使用条件 2 获取活动的信息 判断活动的使用规则
		CouponRecord couponRecord = couponRecordDao
				.selectByPrimaryKey(order.getCouponRecordId());
		if (couponRecord == null) {
			map.put("code", 400);
			map.put("message", "优惠券不存在");
			return map;
		} else {
			if (!couponRecord.getCustId().equals(custId)) {
				map.put("code", 400);
				map.put("message", "请使用自己的优惠券");
				return map;
			}
			if (couponRecord.getIsUsed() == 1) {
				map.put("code", 400);
				map.put("message", "优惠券已使用过");
				return map;
			}
		}
		Date now = new Date();
		if (couponRecord.getExpireTime().getTime() < now.getTime()) {
			map.put("code", 400);
			map.put("message", "该优惠券已过期");
			return map;
		}
		Coupon coupon = couponDao.selectByPrimaryKey(couponRecord
				.getCouponId());
		if(coupon.getType().equals(3)||coupon.getType().equals(4)){
			//代金码  代金券
			if(coupon.getThreshold() == null){
				coupon.setThreshold(0.00);
			}
		}
		for (OrderSku orderSku : orderSkus) {
			Sku sku = skuDao.getById(orderSku.getSkuId());
			Integer skuCount = orderSku.getSkuCount();
			/**********增加活动判断 如果活动进行中 则更改为活动价格 start******************/
			Integer activityId = orderSku.getActivityId() ; 
			if(activityId !=null){
				ActivitySku activitySku = activitySkuDao.getActiveBySku(orderSku.getSkuId());
				if(!activitySku.getActivityId().equals(activityId)){
					map.put("code", 400);
					map.put("message", "活动错误");
					return map;
				}else{
					sku.setPrice(activitySku.getActivityPrice());
				}
			}
			/**********增加活动判断 end ******************/
			// 优惠券、优惠码、代金码等的适用范围： 1: 全场通用 2: SKU 3: Tag 标签
			if (coupon.getScope() == 1) {
				// 绑定优惠券和 sku 关系
				List<Number> tempList = new ArrayList<Number>();
				tempList.add(0, sku.getId());
				tempList.add(1,
						sku.getPrice().multiply(new BigDecimal(skuCount)));
				skuList.add(tempList);
				couponSkusAmount = sku.getPrice()
						.multiply(new BigDecimal(skuCount))
						.add(couponSkusAmount);
			} else if (coupon.getScope() == 2) {
				// sku 判断
				if (!coupon.getRefId().equals(orderSku.getSkuId())) {
					// map.put("code", 400);
					// map.put("message", "该优惠券不能用于该商品");
					// return map;
					continue;
				} else if (new BigDecimal(coupon.getThreshold())
						.compareTo(sku.getPrice().multiply(
								new BigDecimal(skuCount))) < 1) {
					// 绑定优惠券和 sku 关系
					List<Number> tempList = new ArrayList<Number>();
					tempList.add(0, sku.getId());
					tempList.add(
							1,
							sku.getPrice().multiply(
									new BigDecimal(skuCount)));
					skuList.add(tempList);
					couponSkusAmount = sku.getPrice()
							.multiply(new BigDecimal(skuCount))
							.add(couponSkusAmount);
				}

			} else if (coupon.getScope() == 3) {
				// tag 判断
				// 标签专用 根据标签获取所有 skuId 是否包含 当前skuId
				Integer tagId = coupon.getRefId();
				// 获取该节点下的所有子节点
				String tagIds = getTagIds(tagId);
				String[] tagArray = tagIds.split(",");

				Map<String, Object> param = new HashMap<String, Object>();
				param.put("tagArray", tagArray);
				param.put("skuId", orderSku.getSkuId());
				Integer skuNum = tagDao.getTagSkus(param);
				if (skuNum == 0) {
					// throw new CommonException("该优惠券不能用于该商品");
					continue;
				} else {
					// 绑定优惠券和 sku 关系
					List<Number> tempList = new ArrayList<Number>();
					tempList.add(0, sku.getId());
					tempList.add(
							1,
							sku.getPrice().multiply(
									new BigDecimal(skuCount)));
					skuList.add(tempList);
					couponSkusAmount = sku.getPrice()
							.multiply(new BigDecimal(skuCount))
							.add(couponSkusAmount);
				}
			}
		}
		map.put("couponUse",coupon.getDiscount());
		map.put("couponSkusAmount", couponSkusAmount);
		map.put("tempList", skuList);
		return map;
	}
	 /**
	  * <p>功能描述：根据Sku信息判断当前商品是否参加活动，如果参加活动则而且活动正在进行中的话返回当前Sku的活动价格。</p>	
	 * @param skuCount 
	 * @param sku 
	 * @param custId 
	 * @param orderSku 
	  * @return
	  * <p>创建日期:2016年9月13日 下午4:20:01。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	private Map<String, Object> getOrderActivityPrice(Sku sku, Integer skuCount, Integer custId, OrderSku orderSku) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer activityId = orderSku.getActivityId();
		Date nowDate = new Date();
		List<Map<String, Object>> activitySkuList = activityService.findSpikeActivitySkuList(activityId,sku.getId());
		if(activitySkuList!=null&&activitySkuList.size()>0){
			for(Map<String, Object>  orderSkumap:activitySkuList){
				 activityId = (Integer) orderSkumap.get("activityId");
				 Integer purchaseNum = (Integer) orderSkumap.get("purchaseNum");
				 Integer reservedInventory = (Integer) orderSkumap.get("reservedInventory");
				 if(activityId!=null){
					 Activity activity = activityService.findByActivityId(activityId);
					 Integer activityStats= activity.getActivityStatus();//
					 Date startTime = activity.getStartTime();
					 List<Map<String, Object>>  orderSkuList= orderSkuDao.findOrderSkuActivity(sku.getId(),custId,activityId);
					 if(startTime.after(nowDate)){
						 map.put("code", 400);
					     map.put("message", "活动尚未开始，请你稍等！");
					     return map;
					 }
					 if(activity.getActivityType()==1){//秒杀
					     Date endTime = activity.getEndTime();
					     if((activity.getActivityStatus()==0)&&(nowDate.before(startTime)&&endTime.after(startTime))){
					    	 //修改数据库，活动为开始
							 activity.setActivityStatus(2);
							 map = activityService.updateActivity(activity);
							 if(!map.get("code").toString().equals("200")){
								 return map;
						     }
					     }else if(endTime.before(nowDate)||activityStats==2){// 修改数据库并提示用户活动信息已经结束
					    	 activity.setActivityStatus(2);
							 map = activityService.updateActivity(activity);
							 map = activityService.updateActivitySkuList(activity.getId());
							 map.put("code", 400);
						     map.put("message", "活动已经结束，谢谢您的参与！");
						     return map;
					     }
					     logg.info(">>>>>>>>>>秒杀活动>>>>>>>>>>>>>>>>>>");
							if(orderSkuList!=null&&orderSkuList.get(0)!=null&&purchaseNum!=null){
								if(Integer.parseInt(orderSkuList.get(0).get("skuCount").toString())>=purchaseNum){
									map.put("code", 400);
									map.put("message", "对不起,您已经超过该产品的限购个数！这次活动每人限购"+purchaseNum+"个！");
									 return map;
								}
								
							}
							logg.info(reservedInventory+">>>>>>>>>activitySkulist.get(0).get(reservedInventory).toString()>>>>>>>>>>>>>>>");
							if(skuCount>reservedInventory){
								map.put("code", 400);
								map.put("message", "库存不足，当前活动库存数为"+reservedInventory+"个！");
								 return map;
							}
					 }else{//限购
						 if((activity.getActivityStatus()==0)&&nowDate.before(startTime)&&(reservedInventory>0)){
							//修改数据库，活动为开始
							 activity.setActivityStatus(2);
							 map = activityService.updateActivity(activity);
							 if(!map.get("code").toString().equals("200")){
								 return map;
						     }
						 }else if(reservedInventory<=0||activityStats==2){//修改数据库并提示用户活动信息已经结束
					    	 activity.setActivityStatus(2);
							 map = activityService.updateActivity(activity);
							 map = activityService.updateActivitySkuList(activity.getId());
							 map.put("code", 400);
						     map.put("message", "活动商品已经卖完，谢谢您的参与！");
						     return map;
						 }
					    
						if(orderSkuList!=null&&orderSkuList.get(0)!=null){
							if(Integer.parseInt(orderSkuList.get(0).get("skuCount").toString())>=purchaseNum){
								map.put("code", 400);
								map.put("message", "对不起,您已经超过该产品的限购个数！这次活动每人限购"+purchaseNum);
								 return map;
							}
						}
						logg.info(">>>>>>>>>>判断库存>>>>>>>>>>>>>>>>>>");
						if ((reservedInventory - skuCount) < 0) {
							map.put("code", 400);
							map.put("message", "您购买的商品库存数量已经不足，请从新选择数量！");
							 return map;
						}
					 }
					 
				 }
				 sku.setPrice(new BigDecimal(orderSkumap.get("activityPrice").toString()));
				 map.put("code", 200);
				 map.put("sku", sku);
			}
			
		}
		return map;
	}
	 /**
	  * <p>功能描述：保存参加当前的活动信息。</p>	
	  * @param orderSkus
	  * @param order
	  * @param discount
	  * <p>创建日期:2016年9月19日 下午2:43:03。</p>
	  * <p>更新日期:[日期YYYY-MM-DD][更改人姓名][变更描述]。</p>
	  */
	private void insertOrderSkuActivity(List<OrderSku> orderSkus, Order order, BigDecimal discount) {
		List<OrderSkuActivity> skuActivityList = new ArrayList<OrderSkuActivity>();
		OrderSkuActivity  orderSkuActivity=null;
		for (OrderSku orderSku : orderSkus) {
			Integer activityId = orderSku.getActivityId();//活动信息Idtem
			if(!StringUtils.isEmpty(activityId)){
				List<Map<String, Object>> activitySkuList = activityService.findSpikeActivitySkuList(activityId,orderSku.getSkuId());
				if(activitySkuList!=null&&activitySkuList.size()>0){
					Activity activity = activityService.findByActivityId(activityId);
					orderSkuActivity = new OrderSkuActivity();
					orderSkuActivity.setActivityType(activity.getActivityType());//活动类型
					orderSkuActivity.setOrderId(order.getId());
					orderSkuActivity.setActivitySkuid(orderSku.getSkuId());
					orderSkuActivity.setActivityId(activityId);// 活动ID
					orderSkuActivity.setDiscountedPrice(new BigDecimal(activitySkuList.get(0).get("discountedPrice")==null?"0":activitySkuList.get(0).get("discountedPrice").toString()));//优惠金额
					orderSkuActivity.setCreateTime(new Date());
					skuActivityList.add(orderSkuActivity);
				}else{
					throw new CommonException("活动商品错误");
				}
				
			}
		}
		if(skuActivityList.size()>0){
			activityService.saveOrderSkuActivity(skuActivityList);
		}
	}
}