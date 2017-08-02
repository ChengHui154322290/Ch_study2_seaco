package com.tp.service.sch;

import java.io.IOException;
import java.net.UnknownHostException;

import com.tp.dto.common.ResultInfo;

/**
 * 搜索文档相关服务
 * Created by ldr on 2016/2/17.
 */
public interface IDocService {

    void push() throws IllegalAccessException, InterruptedException, IOException;

    /**
     * 全量推送Doc
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     */
     ResultInfo pushAllItemDoc() throws IOException, IllegalAccessException;

     /**
      * 删除全部Doc
      * @return
      * @throws IOException
      * @throws IllegalAccessException
     */
     ResultInfo delItemDocTotal() throws IOException, IllegalAccessException, InterruptedException;

    /**
     * 根据时间戳更新Doc
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InterruptedException
     */
     ResultInfo updateItemDoc() throws IOException, IllegalAccessException, InterruptedException;


    /**
     * 全量更新Doc
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InterruptedException
     */
    ResultInfo updateItemDocTotal() throws IOException, IllegalAccessException, InterruptedException;



    ResultInfo updateShopDoc() throws IOException, IllegalAccessException, InterruptedException;

    ResultInfo updateShopDocTotal() throws IOException, IllegalAccessException, InterruptedException;

    ResultInfo deleteShopDocTotal() throws IOException, IllegalAccessException, InterruptedException;

}
