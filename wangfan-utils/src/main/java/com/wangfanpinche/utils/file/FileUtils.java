package com.wangfanpinche.utils.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import com.wangfanpinche.utils.web.IpUtils;

public class FileUtils {

	/**
	 * 转换成MkFile类
	 * 
	 * @param request
	 * @param file
	 * @return
	 */
	public static MkFile conveterMkFile(MultipartFile file, HttpServletRequest request) throws Exception {
		MkFile mkFile = new MkFile();

		mkFile.setExtension(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
		String absFilePath = getAbsFilePath(request) + "." + mkFile.getExtension();
		mkFile.setPath(request.getServletContext().getRealPath("/") + absFilePath);
		mkFile.setUrl(request.getContextPath() + "/" + absFilePath);
		mkFile.setFilesize(file.getSize());
		mkFile.setFilename(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("/") + 1));
		mkFile.setOldFilename(file.getOriginalFilename());
		mkFile.setUserIp(IpUtils.getIpAddr(request));

		
		// 32位加密
		File f = new File(mkFile.getPath());
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}

		FileOutputStream fos = new FileOutputStream(mkFile.getPath());
		IOUtils.copy(file.getInputStream(), fos);
		fos.flush();
		fos.close();
		
		mkFile.setMd5(getFileMd5(mkFile.getPath()));
		return mkFile;
	}

	/**
	 * 获取文件MD5
	 * @param mkFile
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String getFileMd5(String filePath) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		try (InputStream is = Files.newInputStream(Paths.get(filePath)); DigestInputStream dis = new DigestInputStream(is, md)) {
			/* 这里面不需要代码了直接往下走... */
		}
		byte[] byteDigest = md.digest();
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < byteDigest.length; offset++) {
			i = byteDigest[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}

	/**
	 * 获取文件路径
	 * @param request
	 * @return
	 */
	public static String getAbsFilePath(HttpServletRequest request) {
		LocalDateTime d = LocalDateTime.now();
		String format = d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
		String webPath = "file" + "/" + format.split("-")[0] + "/" + format.split("-")[1] + "/" + format.split("-")[2] + "/" + format.split("-")[3] + "_" + format.split("-")[4] + "_" + format.split("-")[5] + "_" + UUID.randomUUID().toString();
		return webPath;
	}

}
