package com.taotao.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.Test;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.BucketInfo;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;

public class OssTest {
	
	private static String endpoint="oss-cn-shenzhen.aliyuncs.com";
	private static String accessKeyId="LTAIfTVYN8w5mmi1";
	private static String accessKeySecret="fDLgyulAXElgA8IZXPuosidGbnvP4f";
	//private static String bucketName="qwer-nie";
	//private static  String fileKey1="three.jpg";
	public static void main(String[] args) throws IOException {
		OSSClient ossClient=new OSSClient(endpoint, accessKeyId, accessKeySecret);
		/*doExist(ossClient);
		listObject(ossClient);
		System.out.println("------图片上传--------");
		//图片上传
		upload(ossClient);
		listObject(ossClient);
		//将上传的文件删除
		//System.out.println("-------文件删除-------");
		//delete(ossClient);
		System.out.println("--------------");
		
		listObject(ossClient);
		System.out.println("url:http:"+bucketName.toString()+"."+endpoint.toString()+"\\"+fileKey1.toString());
		ossClient.shutdown();*/
		//doExist(ossClient, "qwe-nie");
		//upload(ossClient, "qwe-nie", "niududu2.jpg", new File("C:\\Users\\pc\\Desktop\\官网图片\\132.jpg"));
		//delete(ossClient, "qwe-nie", "niududu.jpg");
		//bucketReadPublic(ossClient, "niu-dudu");	
		download(ossClient, "qwe-nie", "three.jpg", new File("C:\\Users\\pc\\Desktop\\101.jpg"));
		ossClient.shutdown();
		
	}
	/**
	 * 生成权限为公共读的Bucket
	 */
	
	public static OSSClient bucketReadPublic(OSSClient ossClient,String bucketName) {
		CreateBucketRequest createBucketRequest= new CreateBucketRequest(bucketName);
		createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
		ossClient.createBucket(createBucketRequest);
		return ossClient;
	}
	/**
	 * 健壮性判断（判断是否储存在该储存区域）
	 * @param ossClient
	 * @param bucketName
	 */
	public static void doExist(OSSClient ossClient,String bucketName) {
		if(ossClient.doesBucketExist(bucketName)) {
			System.out.println("已存在该存储区域，BucketName："+bucketName);
		}else {
			System.out.println("没有该"+bucketName+"存储区域，创建该存储区域。");
			ossClient.createBucket(bucketName);
		}
	}
	public static Boolean doExistObject(OSSClient ossClient,String bucketName,String fileKey) {
		doExist(ossClient, bucketName);
		if(ossClient.doesObjectExist(bucketName, fileKey)) {
			System.out.println("该储存区域"+bucketName+"存在文件："+fileKey);
			return true;
		}else {
			return false;
		}
	} 
	
	/**
	 *   * ?无法作为任意文件适配符
	 * @param ossClient
	 */
	public static void delete(OSSClient ossClient,String bucketName,String fileKey) {
		//健壮性判断 是否存在这个bucket储存空间，在这个储存空间下是否存在该object
			if(doExistObject(ossClient, bucketName, fileKey)) {
				ossClient.deleteObject(bucketName, fileKey);
				System.out.println("删除成功");
				listObject(ossClient, bucketName);
			}else {
				System.out.println("该储存空间无相对应的文件:"+fileKey);
			}
			
			
		
	}
	/**
	 * 上传文件
	 * @param ossClient
	 * @param bucketName
	 * @param fileKey 在目标目录也就是oss服务器上定义的文件名名
	 * @param filePath 在本地目录所定义的资源路径
	 */
	public static void upload(OSSClient ossClient,String bucketName,String fileKey,File filePath) {
		ossClient.putObject( bucketName,fileKey, filePath);
		System.out.println("文件上传成功");
		listObject(ossClient, bucketName);
		}
	/**
	 * 
	 * @param ossClient
	 * @param bucketName
	 * @param fileKey
	 * @param filePath 下载完成后的路径以及文件名称
	 * @throws IOException
	 */
	public static void download(OSSClient ossClient,String bucketName,String fileKey,File filePath) throws IOException {
		//流式下载
//		OSSObject ossObject = ossClient.getObject(bucketName, fileKey);
//		System.out.println("Object content:");
//		BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
//		while (true) {
//		    String line = reader.readLine();
//		    if (line == null) break;
//		    System.out.println("\n" + line);
//		}
//		reader.close();
		//下载到本地：
		ossClient.getObject(new GetObjectRequest(bucketName, fileKey), filePath);
		System.out.println("下载成功");
	}

	/**
	 * 	查看当前bucket下的所有object
	 * @param ossClient
	 * @param bucketName
	 */
	public static void listObject(OSSClient ossClient,String bucketName) {
		ObjectListing list = ossClient.listObjects(bucketName);
		List<OSSObjectSummary> sum = list.getObjectSummaries();
		System.out.println("该存储空间有如下文件：");
		for (OSSObjectSummary object : sum) {
			System.out.println(object.getKey());
		}
	}
	
}
