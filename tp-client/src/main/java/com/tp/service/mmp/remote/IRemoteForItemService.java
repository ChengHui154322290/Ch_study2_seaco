/**
 * 
 */
package com.tp.service.mmp.remote;


import com.tp.dto.mmp.TopicItemBbtDTO;

/**

 */
public interface IRemoteForItemService {

	TopicItemBbtDTO getTopicItemBySku(String sku);
}
