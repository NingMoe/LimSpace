package com.tyiti.easycommerce.util.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.tyiti.easycommerce.util.xml.entity.weixin.PayNotifyRes;
import com.tyiti.easycommerce.util.xml.entity.weixin.Unifiedorder;

public class XmlUtil {
	public static Object fromXml(String xml, Class<?> clazz) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			return unmarshaller.unmarshal(new StringReader(xml));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String toXml(Object root, Class<?> clazz) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
			StringWriter writer = new StringWriter();
			Marshaller marshaller = jaxbContext.createMarshaller();
			// 是否格式化XML
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
			//设置编码方式
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			// 设置XML声明
//			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", Boolean.FALSE);
			
			marshaller.marshal(root, writer);
			return writer.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		Unifiedorder user = new Unifiedorder();
		user.setAppid("aaa");
		System.out.println(toXml(user, Unifiedorder.class));
		PayNotifyRes res = new PayNotifyRes();
		res.setReturnCode("success");
		res.setReturnMsg("成功");
		System.out.println(toXml(res, PayNotifyRes.class));
	}

}
