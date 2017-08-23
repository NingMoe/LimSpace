package com.tyiti.easycommerce.service.impl;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tyiti.easycommerce.entity.ReportShare;
import com.tyiti.easycommerce.entity.Share;
import com.tyiti.easycommerce.entity.ShareCoupon;
import com.tyiti.easycommerce.entity.ShareExecl;
import com.tyiti.easycommerce.entity.ShareUserExecl;
import com.tyiti.easycommerce.repository.ShareDao;
import com.tyiti.easycommerce.service.ShareService;

@Service
public class ShareServiceImpl implements ShareService {

	@Autowired
	ShareDao shareDao;

	Logger logger = Logger.getLogger(ShareServiceImpl.class);

	@Override
	public Map<String, Object> getShareList(Share share) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ReportShare> lists = shareDao.getShareAndUserList(share);
		map.put("code", 200);
		// 共计分享次数
		if (null != lists && null != share.getLimit()
				&& share.getOffset() != null && lists.size() > 0) {
			Integer offset = share.getOffset();// 开始位置
			Integer limit = offset + share.getLimit();// 条数
			if (offset > lists.size() - 1) {
				offset = 0;
				limit = share.getLimit();
			}
			if (limit > lists.size()) {
				limit = lists.size();
			}
			map.put("date", lists.subList(offset, limit));
		} else {
			map.put("date", lists);
		}
		map.put("shareNum", lists.size());
		// map.put("total", shareDao.getShareCouponCount(share));
		// 发放优惠券数量(分享者获取优惠券数量+注册者获取优惠券数量)
		Integer shareNum = shareDao.getShareCouponCount(share);
		Integer regNum = shareDao.getRegCouponCount(share);
		map.put("couponNum", shareNum + regNum);
		// 注册人数(分享者获取优惠券数量+注册者获取优惠券数量)
		map.put("regNum", regNum);

		return map;
	}

	@Override
	public Map<String, Object> shareToExecl(HttpServletResponse response,
			HttpServletRequest request, Share share) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<ShareExecl> listA = shareDao.shareToExecl(share);
			List<ShareUserExecl> listB = shareDao.shareUserToExecl(share);

			// 创建workbook
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 创建sheet页
			HSSFSheet sheet = workbook.createSheet("分享人数据表");
			HSSFRow row = sheet.createRow(0);
			row.setHeight((short) 400);// 目的是想把行高设置成25px;
			HSSFCell c0 = row.createCell(0);
			HSSFCell c1 = row.createCell(1);
			HSSFCell c2 = row.createCell(2);
			HSSFCell c3 = row.createCell(3);
			HSSFCell c4 = row.createCell(4);
			HSSFCell c5 = row.createCell(5);
			// 合并单元格
			// 参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
			CellRangeAddress region = new CellRangeAddress(0, 0, (short) 0,
					(short) 0);
			sheet.addMergedRegion(region);
			// 第一个参数代表列id(从0开始),第2个参数代表宽度值
			sheet.setColumnWidth(0, 3066);
			sheet.setColumnWidth(1, 3766);
			sheet.setColumnWidth(2, 5866);
			sheet.setColumnWidth(3, 4766);
			sheet.setColumnWidth(4, 3766);
			sheet.setColumnWidth(5, 3766);
			// 设置时间格式
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			HSSFDataFormat format = workbook.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("yyyy-mm-dd hh:mm:ss"));
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			// 设置
			cellStyle.setWrapText(true);// 设置自动换行
			// 二、设置边框:
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			// 三、设置居中:
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
			HSSFFont font2 = workbook.createFont();
			font2.setFontName("仿宋_GB2312");
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
			font2.setFontHeightInPoints((short) 16);
			/*************************** 被分享人END **********************/
			/******** 表头信息 **********/
			HSSFFont font = workbook.createFont();
			font.setFontName("黑体");
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
			font.setFontHeightInPoints((short) 10);
			HSSFCellStyle headStyle = workbook.createCellStyle();
			headStyle.setFillForegroundColor((short) 17);// 设置背景色
			headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			headStyle.setFont(font);// 选择需要用到的字体格式
			// 上下居中
			headStyle.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
			headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			// 二、设置边框:
			headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			// 三、设置居中:
			headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
			// 创建单元格
			c0.setCellValue(new HSSFRichTextString("用户ID"));
			c0.setCellStyle(headStyle);
			c1.setCellValue(new HSSFRichTextString("用户手机号"));
			c1.setCellStyle(headStyle);
			c2.setCellValue(new HSSFRichTextString("邀请时间"));
			c2.setCellStyle(headStyle);
			c3.setCellValue(new HSSFRichTextString("优惠券"));
			c3.setCellStyle(headStyle);
			c4.setCellValue(new HSSFRichTextString("优惠券状态"));
			c4.setCellStyle(headStyle);
			c5.setCellValue(new HSSFRichTextString("邀请人数"));
			c5.setCellStyle(headStyle);
			/******** 表头信息 END **********/

			int rowIndex = 1;
			int rowTemp = 0;
			for (ShareExecl shareUser : listA) {
				row = sheet.createRow(rowIndex);
				c0 = row.createCell(0);
				c0.setCellValue(new HSSFRichTextString(shareUser.getUserId()
						.toString()));
				c0.setCellStyle(cellStyle);
				c1 = row.createCell(1);
				c1.setCellValue(new HSSFRichTextString(shareUser.getMobile()));
				c1.setCellStyle(cellStyle);
				c2 = row.createCell(2);
				c2.setCellValue(shareUser.getCreateTime());
				c2.setCellStyle(cellStyle);
				c5 = row.createCell(5);
				c5.setCellValue(new HSSFRichTextString(shareUser.getShareNum()
						.toString()));
				c5.setCellStyle(cellStyle);
				if (shareUser.getCouponList().size() > 0) {
					// 获取优惠券列表
					for (ShareCoupon coupon : shareUser.getCouponList()) {
						// 第一张优惠券信息不需要新建单元格
						if (rowTemp == 0) {
							c3 = row.createCell(3);
							c3.setCellValue(new HSSFRichTextString(coupon
									.getCouponName()));
							c3.setCellStyle(cellStyle);
							c4 = row.createCell(4);
							c4.setCellValue(new HSSFRichTextString(coupon
									.getIsUsed()));
							c4.setCellStyle(cellStyle);
							rowTemp++;
						} else {
							// 新建单元格
							row = sheet.createRow(rowIndex + rowTemp);
							c0 = row.createCell(0);
							c0.setCellValue(new HSSFRichTextString(shareUser
									.getUserId().toString()));
							c0.setCellStyle(cellStyle);
							c1 = row.createCell(1);
							c1.setCellValue(new HSSFRichTextString(shareUser
									.getMobile()));
							c1.setCellStyle(cellStyle);
							c2 = row.createCell(2);
							c2.setCellValue(shareUser.getCreateTime());
							c2.setCellStyle(cellStyle);
							c5 = row.createCell(5);
							c5.setCellValue(new HSSFRichTextString(shareUser
									.getShareNum().toString()));
							c5.setCellStyle(cellStyle);
							c3 = row.createCell(3);
							c3.setCellValue(new HSSFRichTextString(coupon
									.getCouponName()));
							c3.setCellStyle(cellStyle);
							c4 = row.createCell(4);
							c4.setCellValue(new HSSFRichTextString(coupon
									.getIsUsed()));
							c4.setCellStyle(cellStyle);
							rowTemp++;
						}
					}
				} else {
					// 不存在优惠券
					c3 = row.createCell(3);
					c3.setCellValue(new HSSFRichTextString(""));
					c3.setCellStyle(cellStyle);
					c4 = row.createCell(4);
					c4.setCellValue(new HSSFRichTextString(""));
					c4.setCellStyle(cellStyle);
				}
				// 参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
				region = new CellRangeAddress(rowIndex, rowIndex + rowTemp - 1,
						0, 0);// 用户Id
				sheet.addMergedRegion(region);
				region = new CellRangeAddress(rowIndex, rowIndex + rowTemp - 1,
						1, 1);// 手机号
				sheet.addMergedRegion(region);
				region = new CellRangeAddress(rowIndex, rowIndex + rowTemp - 1,
						2, 2);// 邀请时间
				sheet.addMergedRegion(region);
				region = new CellRangeAddress(rowIndex, rowIndex + rowTemp - 1,
						5, 5);// 注册数
				sheet.addMergedRegion(region);
				rowIndex += rowTemp;
				rowTemp = 0;
			}
			/************************* 总计 *****************************/
			row = sheet.createRow(rowIndex);
			c0 = row.createCell(0);
			c0.setCellValue(new HSSFRichTextString(" 分享人数： " + listA.size()));
			c0.setCellStyle(cellStyle);
			c1 = row.createCell(1);
			c1.setCellValue(new HSSFRichTextString(" 分享人数： " + listA.size()));
			c1.setCellStyle(cellStyle);
			c2 = row.createCell(2);
			c2.setCellValue(new HSSFRichTextString(" 注册人数： " + listB.size()));
			c2.setCellStyle(cellStyle);
			c3 = row.createCell(3);
			c3.setCellValue(new HSSFRichTextString(" 注册人数： " + listB.size()));
			c3.setCellStyle(cellStyle);
			c4 = row.createCell(4);
			c4.setCellValue(new HSSFRichTextString("发放优惠券数量： "
					+ shareDao.getShareCouponCount(share)));
			c4.setCellStyle(cellStyle);
			c5 = row.createCell(5);
			c5.setCellValue(new HSSFRichTextString("发放优惠券数量： "
					+ shareDao.getShareCouponCount(share)));
			c5.setCellStyle(cellStyle);
			row = sheet.createRow(rowIndex + 1);
			c0 = row.createCell(0);
			c0.setCellStyle(cellStyle);
			c1 = row.createCell(1);
			;
			c1.setCellStyle(cellStyle);
			c2 = row.createCell(2);
			c2.setCellStyle(cellStyle);
			c3 = row.createCell(3);
			c3.setCellStyle(cellStyle);
			c4 = row.createCell(4);
			c4.setCellStyle(cellStyle);
			c5 = row.createCell(5);
			c5.setCellStyle(cellStyle);
			// 参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
			region = new CellRangeAddress(row.getRowNum() - 1, row.getRowNum(),
					0, 1);// 分享人数
			sheet.addMergedRegion(region);
			region = new CellRangeAddress(row.getRowNum() - 1, row.getRowNum(),
					2, 3);// 注册人数
			sheet.addMergedRegion(region);
			region = new CellRangeAddress(row.getRowNum() - 1, row.getRowNum(),
					4, 5);// 发放优惠券人数
			sheet.addMergedRegion(region);

			/**************************** 被分享人END **********************/
			/********************** 创建sheet被分享人数据表页 *******************/
			HSSFSheet sheet1 = workbook.createSheet("分享注册用户表");
			sheet1.setColumnWidth(0, 3766);
			sheet1.setColumnWidth(1, 4766);
			sheet1.setColumnWidth(2, 3766);
			sheet1.setColumnWidth(3, 4766);
			sheet1.setColumnWidth(4, 5766);
			sheet1.setColumnWidth(5, 3766);
			sheet1.setColumnWidth(6, 3766);
			row = sheet1.createRow(0);
			row.setHeight((short) 400);// 目的是想把行高设置成25px;
			c0 = row.createCell(0);
			c1 = row.createCell(1);
			c2 = row.createCell(2);
			c3 = row.createCell(3);
			c4 = row.createCell(4);
			c5 = row.createCell(5);
			HSSFCell c6 = row.createCell(6);
			/******** 表头信息 **********/
			// 创建单元格
			c0.setCellValue(new HSSFRichTextString("注册用户ID"));
			c0.setCellStyle(headStyle);
			c1.setCellValue(new HSSFRichTextString("注册用户手机号"));
			c1.setCellStyle(headStyle);
			c2.setCellValue(new HSSFRichTextString("分享人ID"));
			c2.setCellStyle(headStyle);
			c3.setCellValue(new HSSFRichTextString("分享人手机号"));
			c3.setCellStyle(headStyle);
			c4.setCellValue(new HSSFRichTextString("受邀时间"));
			c4.setCellStyle(headStyle);
			c5.setCellValue(new HSSFRichTextString("优惠券"));
			c5.setCellStyle(headStyle);
			c6.setCellValue(new HSSFRichTextString("优惠券状态"));
			c6.setCellStyle(headStyle);
			/******** 表头信息 END **********/
			/******** 内容 **********/
			rowIndex = 1;
			for (ShareUserExecl shareUser : listB) {
				// 创建单元格 1
				row = sheet1.createRow(rowIndex);
				c0 = row.createCell(0);
				c0.setCellValue(new HSSFRichTextString(shareUser.getUserId()
						.toString()));
				c0.setCellStyle(cellStyle);
				c1 = row.createCell(1);
				c1.setCellValue(new HSSFRichTextString(shareUser.getMobile()));
				c1.setCellStyle(cellStyle);
				c2 = row.createCell(2);
				c2.setCellValue(new HSSFRichTextString(shareUser.getsUserId()
						.toString()));
				c2.setCellStyle(cellStyle);
				c3 = row.createCell(3);
				c3.setCellValue(new HSSFRichTextString(shareUser.getsMobile()));
				c3.setCellStyle(cellStyle);
				c4 = row.createCell(4);
				c4.setCellValue(shareUser.getCreateTime());
				c4.setCellStyle(cellStyle);
				c5 = row.createCell(5);
				c5.setCellValue(new HSSFRichTextString(shareUser
						.getCouponName()));
				c5.setCellStyle(cellStyle);
				c6 = row.createCell(6);
				c6.setCellValue(new HSSFRichTextString(shareUser.getIsUsed()));
				c6.setCellStyle(cellStyle);
				// 行增加1
				rowIndex++;
			}
			/******** 内容 END **********/
			row = sheet1.createRow(rowIndex);
			c0 = row.createCell(0);
			c0.setCellValue(new HSSFRichTextString(" 注册人数： " + listB.size()));
			c0.setCellStyle(cellStyle);
			c1 = row.createCell(1);
			c1.setCellStyle(cellStyle);
			c2 = row.createCell(2);
			c2.setCellValue(new HSSFRichTextString(" "));
			c2.setCellStyle(cellStyle);
			c3 = row.createCell(3);
			c3.setCellValue(new HSSFRichTextString(""));
			c3.setCellStyle(cellStyle);
			c4 = row.createCell(4);
			c4.setCellValue(new HSSFRichTextString("发放优惠券数量： " + listB.size()));
			c4.setCellStyle(cellStyle);
			c5 = row.createCell(5);
			c5.setCellStyle(cellStyle);
			c6 = row.createCell(6);
			c6.setCellStyle(cellStyle);
			row = sheet1.createRow(rowIndex + 1);
			c0 = row.createCell(0);
			c0.setCellStyle(cellStyle);
			c1 = row.createCell(1);
			;
			c1.setCellStyle(cellStyle);
			c2 = row.createCell(2);
			c2.setCellStyle(cellStyle);
			c3 = row.createCell(3);
			c3.setCellStyle(cellStyle);
			c4 = row.createCell(4);
			c4.setCellStyle(cellStyle);
			c5 = row.createCell(5);
			c5.setCellStyle(cellStyle);
			c6 = row.createCell(6);
			c6.setCellStyle(cellStyle);
			// 参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
			region = new CellRangeAddress(row.getRowNum() - 1, row.getRowNum(),
					0, 3);// 注册人数
			sheet1.addMergedRegion(region);
			region = new CellRangeAddress(row.getRowNum() - 1, row.getRowNum(),
					4, 6);// 发放优惠券数量
			sheet1.addMergedRegion(region);

			/********************** 分享人END *****************************/
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String datestr = sdf.format(new Date());
			OutputStream out = response.getOutputStream();
			response.setHeader(
					"Content-disposition",
					"attachment;filename="
							+ URLEncoder.encode(datestr + "ShareGift" + ".xls",
									"UTF-8"));
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			workbook.write(out);
			map.put("code", 200);
			map.put("message", "ok");
		} catch (Exception e) {
			logger.info("导出execl数据失败：" + e.getMessage());
			map.put("code", 406);
			map.put("message", e.getMessage());
		}
		return map;
	}

}