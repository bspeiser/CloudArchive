package com.darkoaksoftware.cloud.cloudarchive.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class FileUtils {

	public FileUtils() {
		// TODO Auto-generated constructor stub
	}

	

	/*public static void main(String[] args) throws Exception {

		String sprops = args[0];
		Properties props = new Properties();
		FileInputStream fis = new FileInputStream(sprops);
		props.load(fis);
		fis.close();
		moveToS3(props);
	}*/

	public static void moveToS3(Properties props) throws Exception {
		String source = props.getProperty("source");
		String extensions = props.getProperty("extentions");
		String bucket = props.getProperty("bucket");
		String catalog = props.getProperty("catalog");
		String accessKey= props.getProperty("accessKey");
		String secretKey=props.getProperty("secretKey");
		S3Util s3 = new S3Util(accessKey,secretKey);
		String[] aAxtensions = extensions.split(",");
		Catalog cat = Catalog.load(catalog);
		try {
			for (String sExtension : aAxtensions) {
				ArrayList<File> files = discoverFile(source, sExtension);
				for (File f : files) {
					if (!cat.inCatalog(f)) {
						s3.uploadFile(bucket, getAwsName(source, f), f.getAbsolutePath());
						cat.add(f);
					} else {
						System.out.println("Skipped:" + f.getName());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cat.store();
		}
	}

	public static ArrayList<File> discoverFile(String directoryName, String extension) {
		ArrayList<File> retList = new ArrayList<File>();
		ArrayList<File> allList = new ArrayList<File>();
		listFilesAndFilesSubDirectories(directoryName, allList);
		for (File file : allList) {
			if (file.getAbsolutePath().endsWith(extension)) {
				retList.add(file);
			}
		}
		return retList;
	}

	public static String getAwsName(String directoryName, File file) {
		String fullName = file.getAbsolutePath();
		String awsName = fullName.substring(directoryName.length() + 1, fullName.length());
		awsName = awsName.replace("\\", "/");
		System.out.println(awsName);
		return awsName;

	}

	public static void listFilesAndFilesSubDirectories(String directoryName, ArrayList<File> retList) {
		File directory = new File(directoryName);
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				retList.add(file);
			} else if (file.isDirectory()) {
				listFilesAndFilesSubDirectories(file.getAbsolutePath(), retList);
			}
		}
	}
}
