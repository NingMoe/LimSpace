package com.tyiti.easycommerce.controller;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.icu.text.SimpleDateFormat;
import com.tyiti.easycommerce.base.SearchResult;
import com.tyiti.easycommerce.common.Constants;
import com.tyiti.easycommerce.util.report.util.ExcelReportUtil;
import com.tyiti.easycommerce.util.spring.SpringUtil;

@Scope("prototype")
@Controller
public class ReportController {

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/excelsMessage", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> exportExcel(@Param("excelId") String excelId,@Param("ids") int[] ids,
			HttpServletResponse response, HttpServletRequest request) {
		
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			// 读取导出的xml
			String templateFilePath = "export_" + excelId + "_excel.xml";
			File templateFile = ResourceUtils.getFile("classpath:"
					+ Constants.EXPORT_TEMP_PATH + templateFilePath);
			SAXReader reader = new SAXReader();
			Document doc = reader.read(templateFile);

			Element root = doc.getRootElement();
			String seriveClassId = root.element("dataSource").attributeValue(
					"serviceClassId");
			String serviceMethod = root.element("dataSource").attributeValue(
					"serviceMethod");

			Object serviceClass = SpringUtil.getBean(seriveClassId);
			Class<? extends Object> c = serviceClass.getClass();
			Class<?> types[] = new Class[1];

			Map<Object, Object> param = new HashMap<Object, Object>();
			//0 表示不下载
			param.put("download", 0);
			param.putAll(this.getRequestParam(request));
			SearchResult<Map<String, Object>> resultMap = null;
			if (ids != null) {
				types[0] = Class.forName("java.util.Map");
				Method m = c.getMethod(serviceMethod, types);
				param.put("ids", ids);
				resultMap = (SearchResult<Map<String, Object>>) m.invoke(
						serviceClass, param);
			} else {
				types[0] = Class.forName("java.util.Map");
				Method m = c.getMethod(serviceMethod, types);
				resultMap = (SearchResult<Map<String, Object>>) m.invoke(
						serviceClass, param);
			}

//			HSSFWorkbook workbook = ExcelReportUtil.genExcelReport(
//					templateFilePath, resultMap.getRows());
//
//			SimpleDateFormat formatymd = new SimpleDateFormat("yyyyMMdd");
//			String path = "xls/" + formatymd.format(new Date());
//			String basePath = request.getSession().getServletContext()
//					.getRealPath(path);
//			File targetFile = new File(basePath);
//			if (!targetFile.exists()) {
//				targetFile.mkdirs();
//			}
//			FileOutputStream os = new FileOutputStream(basePath + "//"
//					+ fileName + ".xls");
//			workbook.write(os);
//			os.close();
			map.put("code", "200");
			map.put("message", "OK");
//			map.put("url", "/" +path + "/" + fileName+".xls");
//			map.put("fileName",fileName+".xls");
		} catch (Exception e) {
			e.getStackTrace();
			map.put("code", "400");
			map.put("message", "系统异常");
			map.put("exception", e.getMessage());
		}
		return map ;
	}

	@SuppressWarnings("rawtypes")
	protected Map<String, Object> getRequestParam(HttpServletRequest request) {

		Map<String, Object> result = new HashMap<String, Object>();
		Iterator<?> iter = request.getParameterMap().entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = entry.getKey().toString();
			String value = request.getParameter(key);
			try {
				result.put(key, Integer.parseInt(value));
			} catch (Exception e) {
				// TODO: handle exception
				result.put(key, value);
			}

		}
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/excels", method = RequestMethod.GET)
	public void exportExcelBrowser(@Param("excelId") String excelId,@Param("ids") int[] ids,
			HttpServletResponse response, HttpServletRequest request) {
		
		try {
			// 读取导出的xml
			String templateFilePath = "export_" + excelId + "_excel.xml";
			File templateFile = ResourceUtils.getFile("classpath:"
					+ Constants.EXPORT_TEMP_PATH + templateFilePath);
			SAXReader reader = new SAXReader();
			Document doc = reader.read(templateFile);

			Element root = doc.getRootElement();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
			String fileName = root.attributeValue("fileName") + "_"
					+ sdf.format(new Date());
			String seriveClassId = root.element("dataSource").attributeValue(
					"serviceClassId");
			String serviceMethod = root.element("dataSource").attributeValue(
					"serviceMethod");

			Object serviceClass = SpringUtil.getBean(seriveClassId);
			Class<? extends Object> c = serviceClass.getClass();
			Class<?> types[] = new Class[1];

			Map<Object, Object> param = new HashMap<Object, Object>();
			param.putAll(this.getRequestParam(request));
			SearchResult<Map<String, Object>> resultMap = null;
			if (ids != null) {
				types[0] = Class.forName("java.util.Map");
				Method m = c.getMethod(serviceMethod, types);
				param.put("ids", ids);
				resultMap = (SearchResult<Map<String, Object>>) m.invoke(
						serviceClass, param);
			} else {
				types[0] = Class.forName("java.util.Map");
				Method m = c.getMethod(serviceMethod, types);
				resultMap = (SearchResult<Map<String, Object>>) m.invoke(
						serviceClass, param);
			}

			HSSFWorkbook workbook = ExcelReportUtil.genExcelReport(
					templateFilePath, resultMap.getRows());

            OutputStream out = response.getOutputStream();
            response.setHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(fileName+".xls","UTF-8"));
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            try {
                workbook.write(out);    
            } finally{
                out.close();
            }
		}  catch (Exception e) {
            e.printStackTrace();
        }
	}

 
}
