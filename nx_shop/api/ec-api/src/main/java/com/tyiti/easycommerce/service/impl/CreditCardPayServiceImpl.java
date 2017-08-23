package com.tyiti.easycommerce.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.common.ConfigKey;
import com.tyiti.easycommerce.common.SysConfig;
import com.tyiti.easycommerce.entity.CreditCardPay;
import com.tyiti.easycommerce.entity.Order;
import com.tyiti.easycommerce.entity.OrderSku;
import com.tyiti.easycommerce.entity.PayRecode;
import com.tyiti.easycommerce.repository.OrderDao;
import com.tyiti.easycommerce.repository.OrderPaymentDao;
import com.tyiti.easycommerce.repository.OrderSkuDao;
import com.tyiti.easycommerce.repository.PayRecodeDao;
import com.tyiti.easycommerce.service.CreditCardPayService;
import com.tyiti.easycommerce.service.KooPayOrderService;
import com.tyiti.easycommerce.service.SkuService;
import com.tyiti.easycommerce.util.DoXml;
import com.tyiti.easycommerce.util.LogUtil;
import com.tyiti.easycommerce.util.SignUtil;

@Service
public class CreditCardPayServiceImpl implements CreditCardPayService {

	
	@Value("${paymentUrl}")
	private String paymentUrl;  //订单支付网关地址
	
	@Value("${notifyUrl}")
	private String notifyUrl;  //通知支付回调地址
	
	@Value("${MerNo}")
	private String  MerNo;  //商户编号
	
	@Value("${MerType}")
	private String  MerType;  //商户类型
	
	@Value("${MerName}")
	private String  MerName;  //商户名称
	
	@Value("${TermNo}")
	private String TermNo;   //终端代码
	
	@Value("${MerArea}")
	private String  MerArea;  //商户区域码
	
	@Value("${EntCertNo}")
	private String  EntCertNo;  //营业执照

    @Value("${PGType}")
    private String PGType; //网关类型

	@Value("${pfxPath}")
	private String pfxPath;

	@Value("${cebsuccUrl}")
	private String cebsuccUrl;  //支付成功跳转页面
	
	@Value("${cebfailUrl}")
	private String cebfailUrl;  //支付失败跳转页面
	
	@Value("${lotterylUrl}")
	private String lotterylUrl;  //抽奖跳转页面
	
	@Value("${lotterySkuId}")
	private Integer lotterySkuId;  //参加抽奖活动的skuId
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private PayRecodeDao payRecodeDao;
	
	@Autowired
	private SkuService skuService;
	@Autowired
	KooPayOrderService kooPayOrderService;
	@Autowired
	OrderSkuDao orderSkuDao;
	
	@Autowired 
	private OrderPaymentDao orderPaymentDao;
	
	private static Log logger = LogFactory.getLog(CreditCardPayServiceImpl.class);
	SignUtil su = new SignUtil();

	/**
	 * 订单支付请求
	 */
	@Override
	public CreditCardPay creditCardPay(Integer id) {
		
		/******下订单之前订单是否是有效订单结束***/
		logger.info(">>>>>>>>>>调用光大银行支付接口，进行支付，当前支付订单号为：id="+id);
		CreditCardPay creditCardPay = new CreditCardPay();
		Order order = orderDao.getById(id);
		BigDecimal amount = order.getAmount();
		//用户需支付金额
		BigDecimal payAmount = new BigDecimal(0.00);
		BigDecimal discount = new BigDecimal(0.00);
		BigDecimal installmentAmount = order.getInstallmentAmount();
		if (installmentAmount!=null && installmentAmount.compareTo(BigDecimal.ZERO)>0) {
			payAmount = installmentAmount;
		}else {
			double discounted = orderDao.getDiscount(id);
			if (discounted>0) {
				discount = new BigDecimal(discounted);
				payAmount = amount.subtract(discount);
			}else {
				payAmount = amount;
			}
		}
		logger.info("》》》》》》光大支付实际付款金额(减去优惠券金额)为："+payAmount+"元《《《《《《");
		//订单日期
		java.text.DateFormat format2 = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        String orderDate = format2.format(order.getCreateTime());

        Integer DbtTerm = order.getInstallmentMonths();
		String feeAmtRate = SysConfig.configMap.get(ConfigKey.FEE_AMT_RATE_PREFIX + DbtTerm);
		String intAmtRate = SysConfig.rateConfigMap.get(String.valueOf(DbtTerm));
		// 商户佣金: FeeAmt = OrderAmt * 期数对应的商户费率
		double FeeAmt = (amount.multiply(new BigDecimal(feeAmtRate))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		// 持卡人手续费金额: IntAmt = OrderAmt * 商户期数对应的持卡人手续费率
		double IntAmt = (amount.multiply(new BigDecimal(intAmtRate))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        // 分期方式
        String Installments = (DbtTerm.intValue() < 10 ? "0" : "") + DbtTerm;
        if (DbtTerm.intValue() == 0 || DbtTerm.intValue() == 1) {
        		Installments = "00";
        }
        String mer_Name = MerName;
		
		//支付流水号  订单号+7位随机数
		String orderNo = "";  //订单号
		orderNo = order.getNo();
		String orderFlowNo = orderNo+(int)(Math.random()*10000000);
		logger.info("订单"+orderNo+"对应生成的流水号为："+orderFlowNo);
		/**
		 * 保存支付流水号
		 */
		PayRecode payRecode = new PayRecode();
		payRecode.setOrderId(orderNo);
		payRecode.setTradeNo(orderFlowNo);
		payRecode.setPayState("A1"); //未支付
		payRecodeDao.addPayRecode(payRecode);
		
		String signReqMsg = "";
		String xml="<?xml version=\"1.0\" encoding=\"GBK\"?>"+
					"<stream>"+
						"<MerNo>"+MerNo+"</MerNo>"+  // 特约商户代码
						"<MerType>"+MerType+"</MerType>"+    // 商户类型
						"<MerName>"+mer_Name+"</MerName>"+    //商户名称
						"<TermNo>"+TermNo+"</TermNo>"+   //终端代码
						"<MerArea>"+MerArea+"</MerArea>"+   //商户区域码
						"<EntCertNo>"+EntCertNo+"</EntCertNo>"+   //营业执照
						"<NotifyURL>"+notifyUrl+"</NotifyURL>"+   //通知URL
						"<MemberID>"+order.getAddressName()+"</MemberID>"+   
						"<TrustedUrl>xinfenbao.com|oa.xinfenbao.com</TrustedUrl>"+   //通知模式
						"<OrderDate>"+orderDate+"</OrderDate>"+     //订单日期时间 yyyy-mm-dd hh:mm:ss
						"<OriOrderNo>"+orderNo+"</OriOrderNo>"+    //原始订单号
						"<OrderFlowNo>"+orderFlowNo+"</OrderFlowNo>"+    //订单流水号
						"<CurrID>001</CurrID>"+  //订单支付币种
						"<OrderAmt>"+payAmount+"</OrderAmt>"+    //订单金额
						"<FeeAmt>"+FeeAmt+"</FeeAmt>"+   //商户佣金
						"<IntAmt>"+IntAmt+"</IntAmt>"+   //持卡人手续费金额
						"<DbtTerm>"+Installments+"</DbtTerm>"+    //分期方式
						"<PGType>" + PGType + "</PGType>"+   //网关类型 1-普通网关；2-大额网关 
						"<TTL>1</TTL>"+    //订单有效期 小时
						"<Memo></Memo>"+    //备注
					"</stream>";
		//签名接口调用
		logger.info(">>>>>>签名前的密文为："+xml);
		signReqMsg = su.sign(xml,pfxPath);
		System.out.println("签名后的密文为："+signReqMsg);
		creditCardPay.setURL(paymentUrl);
		creditCardPay.setSignReqMsg(signReqMsg);
		return creditCardPay;
	}

	/**
	 * 光大银行支付结果通知接口
	 */
	@Override
	public Map<String, Object> notifyCall(String signMsg) {
		Map<String, Object> map = new HashMap<String, Object>();
		Order order = new Order();
		PayRecode payRecode = new PayRecode();
		String orderNo = "";
		String tradeNo = "";
		String signMsgISO = "";
		String signMsgString = "";
		//解析返回的xml结果
		logger.info("支付成功回调传递的参数值："+signMsg);
		try {
			signMsgISO = new String(signMsg.getBytes("UTF-8"),"ISO-8859-1");
			signMsgString = new String(signMsgISO.getBytes("ISO-8859-1"),"UTF-8");
			System.out.println("转义编码格式："+signMsgString);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String resultXml = su.veryfy(signMsgString);
		DoXml doXml = new DoXml();
		try {
			Map<String, String> maps = doXml.doXMLParse(resultXml);
			String TranResult = (String) maps.get("TranResult");
			if ("0".equals(TranResult)) {  //交易成功
				orderNo = (String) maps.get("OriOrderNo");
				Order order2 = orderDao.getByNo(orderNo);
				logger.info("本次支付的订单号为：>>>>>>"+orderNo);
				if (order2!=null&&order2.getStatus()!=2) {//订单未付款
					order.setNo(orderNo);
					payRecode.setOrderId(orderNo);
					tradeNo = (String) maps.get("OrderFlowNo");
					payRecode.setTradeNo(tradeNo);
					Integer count = payRecodeDao.queryOrder(orderNo,tradeNo);
					if (count>0) {
						//需改支付流水表记录
						String payNo = (String) maps.get("PayNo");  //银行网上支付交易流水号
						String accNo = (String) maps.get("AccNo");  //支付卡号
						String cstName = (String) maps.get("CstName"); //持卡人姓名
						String ip = (String) maps.get("IP");  //客户支付ip地址
						payRecode.setPayNo(payNo);
						payRecode.setAccNo(accNo);
						payRecode.setCstName(cstName);
						payRecode.setIp(ip);
						int countPay = payRecodeDao.updatePayRecode(payRecode);
						if (countPay>0) {
							//修改订单表
							List<OrderSku> lists = orderSkuDao.getByOrderId(order2.getId());
							if(lists!=null&&lists.size()>0){
								if(lists.get(0).getSkuErpCode().equals("koo")){
									payRecodeDao.updateKooOrder(orderNo);
								}else{
									payRecodeDao.updateOrder(orderNo);
								}
							}else{
								map.put("code", 500);
								map.put("RetMsg", "没有找到该订单商品！");
								logger.info(LogUtil.info("光大支付回调", "没有找到该订单商品！", null, "订单no:"+orderNo));
								return map;
							}
							
							//减库存
							if(skuService.subtractInventory(order2.getId())!=1){
								map.put("code", 500);
								map.put("RetMsg", "修改库存失败");
								logger.info(LogUtil.info("光大支付回调", "修改库存失败", null, "订单no:"+orderNo));
								return map;
							}
							//修改t_order_payment 记录
							orderPaymentDao.updateOrderPaymentStatus(order2.getId(), 1); //修改所有记录状态
							System.out.println(order2.getId()+"----------------konglongceshi---------------------");
							map.put("code", 200);
							map.put("orderId", order2.getId());
						}else {
							map.put("code", 500);
							map.put("RetMsg", "修改流水表失败");
							logger.info(">>>>>>>>修改流水表记录失败，流水号为："+tradeNo);
						}
					}else {
						map.put("code", 500);
						map.put("RetMsg", "订单不存在。");
						logger.info(">>>>>>>订单"+tradeNo+"不存在。");
					}
				}else if (order2!=null&&order2.getStatus()==2) {
					logger.info("》》》该订单 "+orderNo+" 的状态已经修改！《《《《");
					map.put("code", 200);
					map.put("orderId", order2.getId());
				} 
			}else {  //交易失败
				map.put("code", 500);
				map.put("RetMsg", maps.get("RetMsg"));
			}
		} catch (Exception e) {
			logger.info(">>>>>>>xml字符串解析失败。。。。");
			map.put("code", 500);
			map.put("RetMsg", "xml字符串解析失败");
			e.printStackTrace();
		}
		
		
		//根据返回的结果，做处理，修改数据库
		return map;
	}
	
	public String getCebsuccUrl() {
		return cebsuccUrl;
	}

	public String getCebfailUrl() {
		return cebfailUrl;
	}

	@Override
	public String getLotteryUrl() {
		return lotterylUrl;
	}

	@Override
	public Integer getLotterySkuId() {
		return lotterySkuId;
	}

}
