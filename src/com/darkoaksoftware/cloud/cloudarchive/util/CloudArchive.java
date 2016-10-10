package com.darkoaksoftware.cloud.cloudarchive.util;

import java.io.FileInputStream;
import java.util.Properties;

public class CloudArchive {

	private CloudArchive() {	}

	public static void main(String[] args) throws Exception {
		System.out.println("ARGS:"+args.length);
		if (args.length==0||args[0].length()==0){
			System.out.println(
					"=====================================================================================================\n"+
					"Usage: java -jar CloudArchive-0.0.1-SNAPSHOT-jar-with-dependencies.jar cloudarchive.conf\n"+
					"eg:\n"+		
					"source=F:/source directory\n"+
					"extentions=json,etc\n"+
					"catalog=F:/cataloglocation/cloudarchive.cat\n"+
					"bucket=mybucket\n"+
					"accessKey=DEADC0DEDEADC0DEDEADC0DE\n"+
					"secretKey=z3cr3tK3yz3cr3tK3yz3cr3tK3yz3cr3tK3y\n"+
					"=====================================================================================================\n");
			System.exit(1);
		}
		String sprops = args[0];
		Properties props = new Properties();
		FileInputStream fis = new FileInputStream(sprops);
		props.load(fis);
		fis.close();
		FileUtils.moveToS3(props);
	}

}
