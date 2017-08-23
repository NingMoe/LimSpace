package com.tyiti.easycommerce.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tyiti.easycommerce.service.TopicService;
import com.tyiti.easycommerce.util.ZipUtil;
import com.tyiti.easycommerce.vo.Topic;

@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    ServletContext context;

	@Value("${topic_upload_dir}")
	private String topicUploadDir;

	@Value("${topic_extract_dir}")
	private String topicExtractDir;

	@Override
	public Map<String, Object> upload(MultipartFile multipartFile, String path, String newPath) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", 0);
		map.put("message", "OK");
		
		String basePath = context.getRealPath("");
		// 创建上传目录
		File uploadDir = new File(topicUploadDir);
		if (!uploadDir.isAbsolute()) {
			topicUploadDir = basePath + File.separator + topicUploadDir;
			uploadDir = new File(topicUploadDir);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}
		}
		String fileName = topicUploadDir + File.separator + multipartFile.getOriginalFilename();
		String folderName = multipartFile.getOriginalFilename().replaceAll("[.]zip$", "");
		// 自动从zip文件包获取项目目录名
		if (path == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd");
			path = sdf.format(new Date()) + File.separator + folderName;
		}
		// 是否重命名
		boolean rename = newPath != null;
		if (newPath == null) {
			newPath = path;
		}
		String currentExtractDir = topicExtractDir + newPath;
		File newDir = new File(currentExtractDir);
		if (!newDir.isAbsolute()) {
			currentExtractDir = basePath + File.separator + currentExtractDir;
			newDir = new File(currentExtractDir);
		}
		if (newDir.exists() && rename) {
			map.put("code", 1);
			map.put("message", "上传失败：路径【" + newPath + "】已存在，请修改压缩包名称。");
			return map;
		}
		
		// 先删除旧文件夹
		File oldDir = new File(topicExtractDir + path);
		if (oldDir.exists()) {
			deleteDir(oldDir);
		}
		newDir.mkdirs();
		
		multipartFile.getName();
		File zipFile = new File(fileName);
		try {
			multipartFile.transferTo(zipFile);
			ZipUtil.unzip(fileName, currentExtractDir);
			if (!new File(currentExtractDir + File.separator + "index.html").exists()) {
				map.put("code", 2);
				map.put("message", "压缩包【根目录】下没找到 index.html 文件，请重新上传。");
			}
		} catch (IllegalStateException e) {
			map.put("code", 1);
			map.put("message", e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			map.put("code", 1);
			map.put("message", e.toString());
			e.printStackTrace();
		}
		
		return map;
	}

	private List<File> getChildren(List<File> dirList) {
		// 获取所有日
        List<File> childDirList = new LinkedList<File>();

		if (dirList == null || dirList.size() == 0) {
			return childDirList;
		}
		
        Iterator<File> itr = dirList.iterator();
        while(itr.hasNext()) {
        		File childDir = itr.next();
        		childDirList.addAll(Arrays.asList(childDir.listFiles()));
        }
        
        return childDirList;
	}
	
	@Override
	public List<Topic> list() {
		List<Topic> topics = new LinkedList<Topic>();
		File rootDir = new File(topicExtractDir);
		if (!rootDir.isAbsolute()) {
			String basePath = context.getRealPath("");
			topicExtractDir = basePath + File.separator + topicExtractDir;
		}
		rootDir = new File(topicExtractDir);
        File yearDirs[] = rootDir.listFiles();
		if (yearDirs == null) {
			return topics;
		}
        List<File> topicDirList = getChildren(getChildren(getChildren(Arrays.asList(yearDirs))));
        
        Iterator<File> topicDirItr = topicDirList.iterator();
        while(topicDirItr.hasNext()) {
        		File topicDir = topicDirItr.next();
        		// 【根目录】下没找到 index.html 文件，忽略此目录
        		if (!new File(topicDir + File.separator + "index.html").exists()) {
        			continue;
        		}
        		Topic topic = new Topic();
        		topic.setPath(topicDir.getAbsolutePath().replace(topicExtractDir, ""));
        		topic.setLastModified(topicDir.lastModified());
        		topics.add(topic);
        }

		return topics;
	}

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
        }

        // 删除文件，或空目录
        return dir.delete();
    }
}
