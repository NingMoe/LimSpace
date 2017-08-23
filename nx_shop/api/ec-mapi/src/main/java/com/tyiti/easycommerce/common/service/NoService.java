package com.tyiti.easycommerce.common.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tyiti.easycommerce.entity.DataOrderNoMapping;
import com.tyiti.easycommerce.entity.SeqNo;
import com.tyiti.easycommerce.repository.DataOrderNoMappingDao;
import com.tyiti.easycommerce.repository.SeqNoDao;

@Service
public class NoService {
	private static final int NO_PREFIX_ORDER = 8;
	private static final int NO_PREFIX_CANCELLATION = 0;
	private static final int NO_PREFIX_RETURN = 0;
	
	private static final String NO_KEY_ORDER = "8_";
	private static final String NO_KEY_CANCELLATION = "0_";
	private static final String NO_KEY_RETURN = "0_";

	@Autowired
	private SeqNoDao seqNoDao;
	@Autowired
	private DataOrderNoMappingDao dataOrderNoMappingDao;
	
	/**
	 * 获取订单号
	 * @return
	 */
	public String getOrderNo() {
		return this.getNo(NO_PREFIX_ORDER, NO_KEY_ORDER);
	}
	
	/**
	 * 获取申请取消单号
	 * @return
	 */
	public String getCancellationNo() {
		return this.getNo(NO_PREFIX_CANCELLATION, NO_KEY_CANCELLATION);
	}
	
	/**
	 * 获取申请退货单号
	 * @return
	 */
	public String getReturnNo() {
		return this.getNo(NO_PREFIX_RETURN, NO_KEY_RETURN);
	}
	
	private String getNo(int prefix, String keyPrefix) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		Date now = new Date();
		String dateStr = sdf.format(now);
		String key = keyPrefix + sdf.format(now);
		// 生成5位序列号
		Integer maxSeqNo = seqNoDao.getMaxSeqNo(key);
		if (maxSeqNo == null) {
			maxSeqNo = 0;
		}
		SeqNo seqNoEntity = new SeqNo();
		seqNoEntity.setKey(key);
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

		return prefix + sb.toString() + dateStr;
	}

}
