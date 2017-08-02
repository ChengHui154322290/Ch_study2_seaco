<!-- 商品信息 -->
<!-- 活动详细信息 -->
<table id="topicDetailInfo" class="input tabContent">
	<tr>
		<td class="td_left" colspan="6">
			<h4>详细信息</h4>
		</td>
	</tr>
	<tr>
		<td class="td_right"><strong style="color:red;">*</strong>专场图片</td>
		<td colspan="5">
			<div class="box_center">
				<table cellspacing="0" cellpadding="0" border="0" class="list_table CRZ" id="topicImages" style="width:600px">
					<tbody>
					    <tr align="center">
							<th style="width:150px;">PC端首页横条</th>
							<th style="width:150px;">PC端明日预告</th>
							<th style="width:150px">PC端可能感兴趣</th>
							<th style="width:150px">PC端海淘首页</th>
							<th style="width:150px;">移动端</th>
						</tr>
						<tr>
							<td valign="top" style="height:100px;">
								<input type="hidden" name="topic.image" id="pcImage" value="${topicDetail.topic.image}" />
								<img id="pcImageImg" style="margin-left:10px;width:120px;height:80px;" src="${topicDetail.topicPcImageFull}"/>
								<#if ("view" != "${mode}")>
									<div style="float:right;margin-right:5px;margin-top:85px;">
										<a id="removePcImage" href="javascript:void(0);">X</a>
									</div>
								</#if>
								<div style="width:100%;text-align:center;height:20px;margin-top:5px;">
									<span id="pcImageSize">尺寸: 275*325</span>
								</div>
							</td>
							<td valign="top" style="height:100px;">
								<input type="hidden" name="topic.imageNew" id="newImage" value="${topicDetail.topic.imageNew}" />
								<img id="newImageImg" style="margin-left:10px;width:120px;height:80px;" src="${topicDetail.topicNewImageFull}"/>
								<#if ("view" != "${mode}")>
									<div style="float:right;margin-right:5px;margin-top:85px;">
										<a id="removeNewImage" href="javascript:void(0);">X</a>
									</div>
								</#if>
								<div style="width:100%;text-align:center;height:20px;margin-top:5px;">
									<span id="newImageSize">尺寸: 264*310</span>
								</div>
							</td>
							<td valign="top" style="height:100px;">
								<input type="hidden" name="topic.imageInterested" id="interestedImage" value="${topicDetail.topic.imageInterested}" />
								<img id="interestedImageImg" style="margin-left:10px;width:120px;height:80px;" src="${topicDetail.topicInterestedImageFull}"/>
								<#if ("view" != "${mode}")>
									<div style="float:right;margin-right:5px;margin-top:85px;">
										<a id="removeInterestedImage" href="javascript:void(0);">X</a>
									</div>
								</#if>
								<div style="width:100%;text-align:center;height:20px;margin-top:5px;">
									<span id="interestedImageSize">尺寸: 375*160</span>
								</div>
							</td>
							<td valign="top" style="height:100px;">
								<input type="hidden" name="topic.imageHitao" id="hitaoImage" value="${topicDetail.topic.imageHitao}" />
								<img id="hitaoImageImg" style="margin-left:10px;width:120px;height:80px;" src="${topicDetail.topicHitaoImageFull}"/>
								<#if ("view" != "${mode}")>
									<div style="float:right;margin-right:5px;margin-top:85px;">
										<a id="removeHitaoImage" href="javascript:void(0);">X</a>
									</div>
								</#if>
								<div style="width:100%;text-align:center;height:20px;margin-top:5px;">
									<span id="hitaoImageSize">尺寸: 564*325</span>
								</div>
							</td>
							<td valign="top" style="height:100px;">
								<input type="hidden" name="topic.imageMobile" id="phoneImage" value="${topicDetail.topic.imageMobile}" />
								<img id="phoneImageImg" style="margin-left:10px;width:120px;height:80px;" src="${topicDetail.topicMobileImageFull}"/>
								<#if ("view" != "${mode}")>
									<div style="float:right;margin-right:5px;margin-top:85px;">
										<a id="removePhoneImage" href="javascript:void(0);">X</a>
									</div>
								</#if>
								<div style="width:100%;text-align:center;height:20px;margin-top:5px;">
									<span id="phoneImageSize">尺寸: 608*450</span>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</td>
	</tr>
	<tr class="imageRow">
		<td class="td_right"><strong style="color:red;">*</strong>专场图片(New)</td>
		<td colspan="5">
			<div class="box_center">
				<table cellspacing="0" cellpadding="0" border="0" class="list_table CRZ imageTable" id="topicImageList" style="width:600px">
					<tbody>
					    <tr align="center">
							<th class="pcTag" style="width:150px;">封面图片</th>
							<th class="mobileTag" style="width:150px;">移动封面图片</th>
							<th class="interestTag" style="width:150px;">PC可能感兴趣/即将上线</th>
							<th class="htTag" style="width:150px;">海淘</th>
							<th class="mallTag" style="width:150px;">商城</th>
						</tr>
						<tr>
							<td valign="top" class="pcTag" style="height:100px;">
								<input type="hidden" name="topic.pcImage" id="pcImageN" value="${topicDetail.topic.pcImage}" />
								<img id="pcImageNImg" style="margin-left:10px;width:120px;height:80px;" src="${topicDetail.pcImageFull}"/>
								<#if ("view" != "${mode}")>
									<div style="float:right;margin-right:5px;margin-top:85px;">
										<a id="removePcImageN" href="javascript:void(0);">X</a>
									</div>
								</#if>
								<div style="width:100%;text-align:center;height:20px;margin-top:5px;">
									<span id="pcImageSizeN">尺寸: 1050*774</span>
								</div>
							</td>
							<td valign="top" class="mobileTag" style="height:100px;">
								<input type="hidden" name="topic.mobileImage" id="mobileImageN" value="${topicDetail.topic.mobileImage}" />
								<img id="mobileImageNImg" style="margin-left:10px;width:120px;height:80px;" src="${topicDetail.mobileImageFull}"/>
								<#if ("view" != "${mode}")>
									<div style="float:right;margin-right:5px;margin-top:85px;">
										<a id="removeMobileImageN" href="javascript:void(0);">X</a>
									</div>
								</#if>
								<div style="width:100%;text-align:center;height:20px;margin-top:5px;">
									<span id="mobileImageSizeN">尺寸: 750*350</span>
								</div>
							</td>
							<td valign="top" class="interestTag" style="height:100px;">
								<input type="hidden" name="topic.pcInterestImage" id="interestedImageN" value="${topicDetail.topic.pcInterestImage}" />
								<img id="interestedImageNImg" style="margin-left:10px;width:120px;height:80px;" src="${topicDetail.pcInterestImageFull}"/>
								<#if ("view" != "${mode}")>
									<div style="float:right;margin-right:5px;margin-top:85px;">
										<a id="removeInterestedImageN" href="javascript:void(0);">X</a>
									</div>
								</#if>
								<div style="width:100%;text-align:center;height:20px;margin-top:5px;">
									<span id="interestedImageSizeN">尺寸: 378*164</span>
								</div>
							</td>
							<td valign="top" class="htTag" style="height:100px;">
								<input type="hidden" name="topic.haitaoImage" id="hitaoImageN" value="${topicDetail.topic.haitaoImage}" />
								<img id="hitaoImageNImg" style="margin-left:10px;width:120px;height:80px;" src="${topicDetail.haitaoImageFull}"/>
								<#if ("view" != "${mode}")>
									<div style="float:right;margin-right:5px;margin-top:85px;">
										<a id="removeHitaoImageN" href="javascript:void(0);">X</a>
									</div>
								</#if>
								<div style="width:100%;text-align:center;height:20px;margin-top:5px;">
									<span id="hitaoImageSizeN">尺寸: 715*403</span>
								</div>
							</td>
							<td valign="top" class="mallTag" style="height:100px;">
								<input type="hidden" name="topic.mallImage" id="mallImageN" value="${topicDetail.topic.mallImage}" />
								<img id="mallImageNImg" style="margin-left:10px;width:120px;height:80px;" src="${topicDetail.mallImageFull}"/>
								<#if ("view" != "${mode}")>
									<div style="float:right;margin-right:5px;margin-top:85px;">
										<a id="removeMallImageN" href="javascript:void(0);">X</a>
									</div>
								</#if>
								<div style="width:100%;text-align:center;height:20px;margin-top:5px;">
									<span id="phoneMallSizeN">尺寸: 715*403</span>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</td>
	</tr>
	<tr>
		<td class="td_right"><strong style="color:red;">*</strong>专场介绍</td>
		<td colspan="5" <#if ("view" == "${mode}")>style="display:none"</#if>>
			<table cellspacing="0" cellpadding="0" border="0" class="list_table CRZ" id="topicImages" style="width:100%">
				<tbody>
					<tr>
						<td style="width:80px;text-align:center;">PC端</td>
						<td style="width:80%">
							<textarea class="editor" id="pcContentEditor" name="topic.intro" style="width: 100%;height:200px;">${topicDetail.topic.intro!}</textarea>
						</td>

						<td style="width:80px;text-align:center;">
							<input type="button" id="previewPcContent" class="btn btn82 btn_search" value="浏览"/>
						</td>
					</tr>
					<tr>
						<td style="width:80px;text-align:center;">移动端</td>
						<td style="width:80%">
							<textarea class="editor" id="phoneContentEditor" name="topic.introMobile" style="width: 100%;height:200px;">${topicDetail.topic.introMobile!}</textarea>
						</td>

						<td style="width:80px;text-align:center;">
							<input type="button" id="previewPhoneContent" class="btn btn82 btn_search" value="浏览"/>
						</td>
					</tr>
				</tbody>
			</table>
		</td>
		<td <#if ("view" != "${mode}")>style="display:none"</#if>>
			<table width="80%" cellspacing="0" cellpadding="0" border="0" class="list_table CRZ" id="topicIntros">
				 <tr align="center">
					<th>PC介绍</th>
					<th>移动端介绍</th>
				 </tr>
				 <tr align="center">
                        <td><textarea id="pcContentEditor-view" readOnly="true" style="width: 100%;height:200px;">${topicDetail.topic.intro!}</textarea></td>
                        <td><textarea id="phoneContent-view" readOnly="true" style="width: 100%;height:200px;">${topicDetail.topic.introMobile!}</textarea></td>
				 </tr>
				 <tr align="center">
					<td><input type="button" id="previewPcContentView" class="btn btn82 btn_search" value="浏览"/></td>
					<td><input type="button" id="previewPhoneContentView" class="btn btn82 btn_search" value="浏览"/></td>
				 </tr>
			</table>
		</td>
	</tr>
</table>