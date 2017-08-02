package com.tp.dfsutils.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;

import com.tp.dfsutils.constants.MetaDataKey;
import com.tp.dfsutils.file.ImgFile;
import com.tp.dfsutils.service.impl.DfsServiceImpl;
import com.tp.dfsutils.util.TrackerServerFactory;
import com.tp.dfsutils.util.TrackerServerPool;

/**
 * 混测器
 * @describe 
 * @author 叶礼锋
 * 
 * 2014-12-22 上午11:16:20
 */
@SuppressWarnings(value = { "unused", "deprecation", "serial", "unchecked", "static-access", "rawtypes" })
class t1 {
	public static void main(String[] args) {
		String path=args[0];
		PooledObjectFactory factory=new TrackerServerFactory(new String[]{"192.168.200.50:22122"});
		GenericObjectPoolConfig config=new GenericObjectPoolConfig();
		config.setMaxTotal(10);//池配置还有很多的参数，参考GenericObjectPoolConfig源代码
		TrackerServerPool pool=new TrackerServerPool(factory,config);
		DfsServiceImpl imageService=new DfsServiceImpl(pool);
		File file=new File(path);
		ImgFile info=new ImgFile();
		System.out.println(file.exists());
		info.setFile(file);
		Map<MetaDataKey,String> map=new HashMap();
		map.put(MetaDataKey.FILENAME, "TEST.JPG");
		map.put(MetaDataKey.CREATOR, "叶礼锋");
		info.setMetaData(map);
		info.setRawWidth(640);
		String fileid=imageService.uploadFile(info);
		System.out.println(fileid);
	}
}
class t2 {
	public static void main(String[] args) throws Exception {
		AesCipherService aes=new AesCipherService();
		Key key=aes.generateNewKey(128);
		System.out.println(Arrays.toString(key.getEncoded()));
//		aes.encrypt(new FileInputStream("z:/IMG_5109.JPG"), new FileOutputStream("z:/NOTICE.txt"), key.getEncoded());
		aes.decrypt(new FileInputStream("z:/wKjIM1SZcoaAIJ6sAAAakAZGWio965.xml"), new FileOutputStream("z:/t.xml"), Hex.decode("373df52d4bf17a9bc7fc6a8ec9123ec2"));
	}
}