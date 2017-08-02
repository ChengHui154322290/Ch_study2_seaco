package com.qiniu;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;

/**
 *  文件上传
 * */
public class QiniuUpload {


    public String ACCESS_KEY;

	public String SECRET_KEY;

    private final static Logger logger = LoggerFactory.getLogger(QiniuUpload.class);

    UploadManager um = new UploadManager();
    Auth auth;
    public static String token;
    public BucketManager bucketManager;
    Response resp;
    public  void init(String bucket){
          auth = Auth.create(ACCESS_KEY,SECRET_KEY);
          token = auth.uploadToken(bucket);
        bucketManager= new BucketManager(auth);
    }
	/**
	 * 文件上传
     * @param  filePath  文件路径
     * @param  newName  新的文件名称
	 * */
	public Response uploadFile(String filePath,String newName,String bucket ) throws QiniuException {

         init(bucket);//初始化
		try {
			  resp = um.put(filePath, newName,token);
            logger.info("上传图片：",resp.bodyString());

		} catch (QiniuException ex) {
            logger.info("上传图片错误信息：", ex.getMessage());
            ex.printStackTrace();
		}
        return resp;
	}


	/**
	 * 文件上传 指定文件的类型(mimeType)
     *  @param  filePath  文件路径
     * @param  newName  新的文件名称
     *@param mimeType    文件类型   "image/gif
     *
	 * */
	public Response uploadFile(String filePath, String newName, String mimeType,String bucket ) {
        init(bucket);//初始化
		try {
			Response resp = um.put(filePath,newName, token, null, mimeType, false);
            logger.info("上传图片：", resp.bodyString());

		} catch (QiniuException ex) {
            logger.info("上传图片错误信息：", ex.getMessage());
		}
        return resp;
    }

    public String upload(byte[] data ,String bucket,String extension){
        try {
            String name  = UUID.randomUUID().toString().replace("-","")+ (extension == null? "" :"."+extension.toString());
            Response resp = um.put(data,name,getToken(bucket));
            if(resp.isOK()){
                return name;
            }
        } catch (QiniuException e) {
            logger.error("上传图片错误:",e);
        }catch (Exception e){
            logger.error("上传图片错误:",e);
        }
        return null;
    }

	public String getToken(String name){
		if(StringUtils.isBlank(name)) return "";
		 auth = Auth.create(ACCESS_KEY,SECRET_KEY);
         return auth.uploadToken(name);
	}
    // 覆盖上传
    public String getUpToken1(String bucket, String key){
        return auth.uploadToken(bucket, key);
    }
    //上传详情到七牛
    public void simpleUploadWithoutKey(String description,String key,String relptoken,String bucket) throws QiniuException {
        init(bucket);//初始化

        String head="<head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head>";
        description=head+description;
        try {
            resp = um.put(description.getBytes("utf-8"), key, relptoken);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(resp.toString());
    }
    public void getfileContent(String bucket ) throws QiniuException {
        init(bucket);
        FileInfo info = bucketManager.stat(bucket, "1151028117775435.html");

        System.out.print(info.toString());
    }

    public String getACCESS_KEY() {
		return ACCESS_KEY;
	}
	public void setACCESS_KEY(String aCCESS_KEY) {
		ACCESS_KEY = aCCESS_KEY;
	}
	public String getSECRET_KEY() {
		return SECRET_KEY;
	}
	public void setSECRET_KEY(String sECRET_KEY) {
		SECRET_KEY = sECRET_KEY;
	}


}
