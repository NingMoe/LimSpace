package com.tyiti.easycommerce.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.tyiti.easycommerce.vo.Topic;

public interface TopicService {
	public Map<String, Object> upload(MultipartFile multipartFile, String path, String newPath);
	
	List<Topic> list();
}
