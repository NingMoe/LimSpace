package com.tyiti.easycommerce.controller;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tyiti.easycommerce.entity.Sku;
import com.tyiti.easycommerce.service.SkuService;

/**
 * 
 * 项目名称：easycommerce-management-api 类名称：SkuController 类描述： 创建人：shenzhiqiang
 * 创建时间：2016-4-12 下午3:07:47
 * 
 * @version
 *
 */
@Controller
@RequestMapping(value = "/skus")
public class SkuController {
	@Autowired
	private SkuService skuService;

	@RequestMapping("/imports")
	@ResponseBody
	public Map<String, Object> importSkus(
			@RequestParam("file") MultipartFile file) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (file.isEmpty()) {
			resultMap.put("code", 400);
			resultMap.put("message", "请选择文件");
			return resultMap;
		}
		if (!file.getOriginalFilename().trim().toLowerCase().endsWith(".xls")
				&& !file.getOriginalFilename().trim().toLowerCase()
						.endsWith(".xlsx")) {
			resultMap.put("code", 400);
			resultMap.put("message", "上传文件类型错误");
			return resultMap;
		}
		if (!file.isEmpty()) {
			InputStream in;
			try {
				// 获取前台exce的输入流
				in = file.getInputStream();
				// 读取 Excel
				Workbook book = null;
				try {
					book = new HSSFWorkbook(in);
				} catch (Exception ex) {
					book = new XSSFWorkbook(in);
				}
				Sheet sheet = book.getSheetAt(0);
				// 获取到Excel文件中的所有行数
				String r0c0 = sheet.getRow(0).getCell(0).getStringCellValue();// ID
				String r0c1 = sheet.getRow(0).getCell(1).getStringCellValue();//商品名称  
				String r0c2 = sheet.getRow(0).getCell(2).getStringCellValue();// 商品价格
				String r0c3 = sheet.getRow(0).getCell(3).getStringCellValue();//  分期数
				String r0c4 = sheet.getRow(0).getCell(4).getStringCellValue();// 上下架
				String r0c5 = sheet.getRow(0).getCell(5).getStringCellValue();// 库存
				String r0c6 = sheet.getRow(0).getCell(6).getStringCellValue();// ERP-CODE
				// 表头判断

				// 获取有效的行数
				int CountrowNum = sheet.getLastRowNum();

				if (r0c0.equals("ID") && r0c1.equals("商品名称")
						&& r0c2.equals("商品价格") && r0c3.equals("分期数")
						&& r0c4.equals("上下架") && r0c5.equals("库存") && r0c6.equals("ERP-CODE")) {

					Map<String, Object> checkExcelRepeat = new HashMap<String, Object>();// 判断表格中是否有重复数据
					List<Sku> list = new ArrayList<Sku>();
					for (int rowNum = 1; rowNum <= CountrowNum; rowNum++) {
						// 读取左上端单元格
						Sku sku = new Sku();
						Row row = sheet.getRow(rowNum);// 读取第一行的数据
						if (row != null) {
							for (int i = 0; i < 7; i++) {
								String formerMessage = "";
								if (i == 0) { // ID 不能为空
									if (StringUtils.isEmpty(getCellValue(row
											.getCell(i)))) {
										resultMap.put("code", 400);
										if (resultMap.containsKey("message")) {
											formerMessage = String
													.valueOf(resultMap
															.get("message"));
										}
										resultMap.put("message", formerMessage
												+ "第" + (rowNum + 1) + "行，第"
												+ (i + 1) + "列数据为空\n");
									} else {
										// 判断一下是不是数字
										if (row.getCell(i).getCellType() != Cell.CELL_TYPE_NUMERIC) {
											resultMap.put("code", 400);
											if (resultMap
													.containsKey("message")) {
												formerMessage = String
														.valueOf(resultMap
																.get("message"));
											}
											resultMap.put("message",
													formerMessage + "第"
															+ (rowNum + 1)
															+ "行，第" + (i + 1)
															+ "列数据不为数字格式\n");
										}
										if (checkExcelRepeat
												.containsKey(getCellValue(row
														.getCell(i)))) {
											resultMap.put("code", 400);
											if (resultMap
													.containsKey("message")) {
												formerMessage = String
														.valueOf(resultMap
																.get("message"));
											}
											resultMap.put("message",
													formerMessage + "第"
															+ (rowNum + 1)
															+ "行，第" + (i + 1)
															+ "列数据重复\n");
										}
										checkExcelRepeat.put(
												getCellValue(row.getCell(i)),
												row.getCell(i));

									}
									//判断一下 数据库 id 是否存在
									Sku sku1=skuService.getSkuById(Integer.parseInt(getCellValue(row.getCell(i))));
									if(sku1==null){
										resultMap.put("code", 400);
										resultMap.put("message","第"
												+ (rowNum)
												+ "行"
												+ " 数据库 id为空值！\n");
									}
									sku.setId(Integer.parseInt(getCellValue(row
											.getCell(i))));

								}

								if (i == 1) {
									sku.setName(getCellValue(row.getCell(i)));
								}

								if (i == 2) {
									if (row.getCell(i) != null) {
										if (row.getCell(i).getCellType() != Cell.CELL_TYPE_NUMERIC) {
											resultMap.put("code", 400);
											if (resultMap
													.containsKey("message")) {
												formerMessage = String
														.valueOf(resultMap
																.get("message"));
											}
											resultMap.put("message",
													formerMessage + "第"
															+ (rowNum + 1)
															+ "行，第" + (i + 1)
															+ "列 应为金额\n");

										}
										
										
									}
									try {
										String price = getCellValue(row.getCell(i));
										BigDecimal conPrice = new BigDecimal(price);
										sku.setPrice(conPrice);
									} catch (Exception e) {
										// TODO: handle exception
										resultMap.put("message",
												formerMessage + "第"
														+ (rowNum + 1)
														+ "行，第" + (i + 1)
														+ "列 应为金额\n");
									}
									

								}
								if (i == 3) {
									row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
									if (!"".equals(row.getCell(i)
												.getStringCellValue())) {
										try {
											String[] installment = row.getCell(i)
													.getStringCellValue()
													.split(",");
											for (int j = 0; j < installment.length; j++) {
												try {
													String a = installment[j] ; 
													int num = Integer.parseInt(a);
													if (num != 1 && num != 3
															&& num != 6 && num != 9 && num!=12) {
														resultMap.put("code", 400);
														if (resultMap
																.containsKey("message")) {
															formerMessage = String
																	.valueOf(resultMap
																			.get("message"));
														}
														resultMap
																.put("message",
																		formerMessage
																				+ "第"
																				+ (rowNum + 1)
																				+ "行，第"
																				+ (i + 1)
																				+ "列 分期数不正确\n");
													}
												} catch (Exception e) {
													resultMap
													.put("message",
															formerMessage
																	+ "第"
																	+ (rowNum + 1)
																	+ "行，第"
																	+ (i + 1)
																	+ "列 分期数不正确\n");
												}

											}
										} catch (Exception e) {
											// TODO: handle exception
											resultMap
											.put("message",
													formerMessage
															+ "第"
															+ (rowNum + 1)
															+ "行，第"
															+ (i + 1)
															+ "列 分期数不正确\n");
										}
										
									
									}
									sku.setInstallment(row.getCell(i)
											.getStringCellValue());
								}
								if (i == 4) {
									 row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
									if (!"".equals(row.getCell(i)
															.getStringCellValue())) {
										if (!row.getCell(i)
												.getStringCellValue().equals("0")
												&& !row.getCell(i)
														.getStringCellValue().equals("1")) {
											resultMap.put("code", 400);
											if (resultMap
													.containsKey("message")) {
												formerMessage = String
														.valueOf(resultMap
																.get("message"));
											}
											resultMap.put("message",
													formerMessage + "第"
															+ (rowNum + 1)
															+ "行，第" + (i + 1)
															+ "列 上下架错误\n");

										}
										
										
									}
									
									try {
//										Integer.parseInt(row.getCell(i)
//												.getStringCellValue());
												sku.setStatus( Integer.parseInt(row.getCell(i)
														.getStringCellValue()));
									} catch (Exception e) {
										resultMap.put("message",
												formerMessage + "第"
														+ (rowNum + 1)
														+ "行，第" + (i + 1)
														+ "列 上下架错误\n");
									}
								}
								if (i == 5) {
									row.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
									if (!"".equals(row.getCell(i)
												.getStringCellValue())) {
										// 判断一下是不是数字
										Pattern pattern = Pattern.compile("[0-9]{1,}");
										Matcher matcher = pattern.matcher((CharSequence)(row.getCell(i)
												.getStringCellValue()));
										boolean result=matcher.matches();
										if (!result) {
											resultMap.put("code", 400);
											if (resultMap
													.containsKey("message")) {
												formerMessage = String
														.valueOf(resultMap
																.get("message"));
											}
											resultMap.put("message",
													formerMessage + "第"
															+ (rowNum + 1)
															+ "行，第" + (i + 1)
															+ "列库存不为数字格式\n");
										}
										if (Integer.parseInt(row.getCell(i)
												.getStringCellValue()) < 0) {
											resultMap.put("code", 400);
											if (resultMap
													.containsKey("message")) {
												formerMessage = String
														.valueOf(resultMap
																.get("message"));
											}
											resultMap.put("message",
													formerMessage + "第"
															+ (rowNum + 1)
															+ "行，第" + (i + 1)
															+ "列 库存错误\n");
										}
										
										
										
									}
									//row.getCell(i).setCellType(Cell.CELL_TYPE_NUMERIC);
									sku.setInventory(Integer.parseInt(row.getCell(i)
											.getStringCellValue()));
								}
								if(i ==6){
									Pattern p=Pattern.compile("[\u4e00-\u9fa5]"); 
								       Matcher m=p.matcher(row.getCell(i).getStringCellValue()); 
								       if (m.find()) {
											resultMap.put("code", 400);
											if (resultMap
													.containsKey("message")) {
												formerMessage = String
														.valueOf(resultMap
																.get("message"));
											}
											resultMap.put("message",
													formerMessage + "第"
															+ (rowNum + 1)
															+ "行，第" + (i + 1)
															+ "列 erp-code不可为中文\n");
										}
									sku.setErpCode(row.getCell(i).getStringCellValue());
								}
							}
						}
						list.add(sku);
					}
					if (resultMap.containsKey("code")) {
						if (Integer.parseInt(String.valueOf(resultMap
								.get("code"))) == 400) {
							return resultMap;
						}
					} else {
						skuService.updateByObject(list);
						resultMap.put("code", 200);
						resultMap.put("data", "您已成功更新"+CountrowNum+"条数据");
						return resultMap;
					}
				}
			} catch (Exception e) {
				resultMap.put("exception", e.getMessage());
			}
		}

		return resultMap;
	}

	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	private String getCellValue(Cell cell) {
		String strCell = "";
		if (cell == null) {
			return strCell;
		}
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:// 数字后面自动加 .的问题
			Object inputValue = null;// 单元格值
			long longVal = Math.round(cell.getNumericCellValue());
			if (Double.parseDouble(longVal + ".0") == cell
					.getNumericCellValue())
				inputValue = longVal;
			else
				inputValue = cell.getNumericCellValue();
			strCell = String.valueOf(inputValue);
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		return strCell;
	}

}