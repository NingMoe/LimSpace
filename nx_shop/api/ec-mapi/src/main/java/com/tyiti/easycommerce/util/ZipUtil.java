package com.tyiti.easycommerce.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtil {
	/**
	 * 解压缩zip文件到文件夹
	 * @param zipFile input zip file
	 * @param outputFolder
	 */
	public static void unzip(String zipFile, String outputFolder) {
		byte[] buffer = new byte[1024];

		try {
			// get the zip file content
			ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
			// get the zipped file list entry
			ZipEntry zipEntry = zipInputStream.getNextEntry();

			while (zipEntry != null) {
				String fileName = zipEntry.getName();
				File newFile = new File(outputFolder + File.separator + fileName);
				// create all non exists folders
				// else you will hit FileNotFoundException for compressed folder
				if (zipEntry.isDirectory()) {
					newFile.mkdirs();
				} else {
					FileOutputStream fos = new FileOutputStream(newFile);

					int len;
					while ((len = zipInputStream.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}

					fos.close();
				}
				zipEntry = zipInputStream.getNextEntry();
			}

			zipInputStream.closeEntry();
			zipInputStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
