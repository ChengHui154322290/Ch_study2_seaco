package com.tp.service.cms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;

import org.springframework.stereotype.Service;

import com.tp.common.vo.cms.TempleConstant;
import com.tp.dfsutils.file.BaseFile;
import com.tp.service.cms.ICommUtilService;

@Service(value="commUtilService")
public class CommUtilService implements ICommUtilService{

	@Override
	public String uploadFile(BaseFile baseFile) {
		String fileId=null;
		OutputStreamWriter osw = null;
		Reader reader = null;  
		try {
			String fileExtName=baseFile.getFileExtName();
			if(fileExtName==null){
				return null;
			}
			reader = new InputStreamReader(new FileInputStream(baseFile.getFile()));  
			int tempchar;  
			fileId = baseFile.getFile().toString().substring(baseFile.getFile().toString().lastIndexOf("\\")+1,baseFile.getFile().toString().length());
			//一下的路径需要改成服务器地址路径
			File file = new File(TempleConstant.CMS_SINGLE_TEMPLE_PATH,fileId);
//			if(!file.exists()){
//				file.mkdirs();
//			}
			osw = new OutputStreamWriter(
					new FileOutputStream(file));  
			while ((tempchar = reader.read()) != -1){  
			if (((char)tempchar) != 'r'){  
				osw.write((char)tempchar); 
			}  
		}  
			osw.flush();  
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(osw != null){
				try {
					osw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} 
		return fileId;
	}
	
	public static void main(String[] args) {
		
	}

	
}
