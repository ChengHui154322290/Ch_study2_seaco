<!-- 适用区域块 -->
<table id="topicArea" class="input tabContent">
	<tr>
		<td class="td_left" colspan="6">
			<h4>适用区域</h4>
		</td>
	</tr>
	<tr>
		<td></td>
		<td colspan="5">
			<#if ("view" != "${mode}")>
				<div style="width:80px;float:left;padding-right:20px;">
					<#if (1 == topicDetail.areaAllSelect)>
						<input type="checkbox" name="areaAll" id="areaAll" checked/>全部
					<#else>
						<input type="checkbox" name="areaAll" id="areaAll" />全部
					</#if>
				</div>
				<div style="float:left;padding-right:20px;">
					<#if (1 == topicDetail.areaEastChinaSelect)>
						<input type="checkbox" name="areaEastChina" id="areaEastChina" checked="checked"/>华东
					<#else>
						<input type="checkbox" name="areaEastChina" id="areaEastChina" />华东
					</#if>
				</div>
				<div style="float:left;padding-right:20px;">
					<#if (1 == topicDetail.areaNorthChinaSelect)>
						<input type="checkbox" name="areaNorthChina" id="areaNorthChina" checked="checked"/>华北
					<#else>
						<input type="checkbox" name="areaNorthChina" id="areaNorthChina" />华北
					</#if>
				</div>
				<div style="float:left;padding-right:20px;">
					<#if (1 == topicDetail.areaCentChinaSelect)>
						<input type="checkbox" name="areaCentChina" id="areaCentChina" checked="checked"/>华中
					<#else>
						<input type="checkbox" name="areaCentChina" id="areaCentChina" />华中
					</#if>
				</div>
				<div style="float:left;padding-right:20px;">
					<#if (1 == topicDetail.areaSoutChinaSelect)>
						<input type="checkbox" name="areaSoutChina" id="areaSoutChina" checked="checked"/>华南
					<#else>
						<input type="checkbox" name="areaSoutChina" id="areaSoutChina" />华南
					</#if>
				</div>
				<div style="float:left;padding-right:20px;">
					<#if (1 == topicDetail.areaNortheastChinaSelect)>
						<input type="checkbox" name="areaNortheastChina" id="areaNortheastChina" checked="checked"/>东北
					<#else>
						<input type="checkbox" name="areaNortheastChina" id="areaNortheastChina" />东北
					</#if>
				</div>
				<div style="float:left;padding-right:20px;">
					<#if (1 == topicDetail.areaNorthwestChinaSelect)>
						<input type="checkbox" name="areaNorthwestChina" id="areaNorthwestChina" checked="checked"/>西北
					<#else>
						<input type="checkbox" name="areaNorthwestChina" id="areaNorthwestChina" />西北
					</#if>
				</div>
				<div style="float:left;padding-right:20px;">
					<#if (1 == topicDetail.areaSouthwestChinaSelect)>
						<input type="checkbox" name="areaSouthwestChina" id="areaSouthwestChina" checked="checked"/>西南
					<#else>
						<input type="checkbox" name="areaSouthwestChina" id="areaSouthwestChina"/>西南
					</#if>
				</div>
			<#else>
				<#if (1 == topicDetail.areaAllSelect)>
					<div style="width:80px;float:left;padding-right:20px;height:20px;">
						全部
					</div>
				</#if>
				<#if (1 == topicDetail.areaEastChinaSelect)>
					<div style="float:left;padding-right:20px;height:20px;">
						华东
					</div>
				</#if>
				<#if (1 == topicDetail.areaNorthChinaSelect)>
					<div style="float:left;padding-right:20px;height:20px;">
						华北
					</div>
				</#if>
				<#if (1 == topicDetail.areaCentChinaSelect)>
					<div style="float:left;padding-right:20px;height:20px;">
						华中
					</div>
				</#if>
				<#if (1 == topicDetail.areaSoutChinaSelect)>
					<div style="float:left;padding-right:20px;height:20px;">
						华南
					</div>
				</#if>
				<#if (1 == topicDetail.areaNortheastChinaSelect)>
					<div style="float:left;padding-right:20px;height:20px;">
						东北
					</div>
				</#if>
				<#if (1 == topicDetail.areaNorthwestChinaSelect)>
					<div style="float:left;padding-right:20px;height:20px;">
						西北
					</div>
				</#if>
				<#if (1 == topicDetail.areaSouthwestChinaSelect)>
					<div style="float:left;padding-right:20px;height:20px;">
						西南
					</div>
				</#if>
			</#if>
		</td>
	</tr>
</table>