<div class="box_center">
	<ul id="tab" class="tab" >
				<li>
					<input type="button" value="PC模板 " />
				</li>
				<li>
					<input type="button" value="手机模板" id="mobileEditorTabBtn" />
				</li>
				<li>
					<input type="button" value="PC->手机" id="copyPc2Mobile" />
				</li>
	</ul>
	<table class="input tabContent"">
			<tr>
				<td>
				<textarea id="editor" name="itemDesc.description" class="editor" style="width: 100%;">
					${desc.description}
				</textarea>
				</td>
				
			</tr>
	</table>
	<table  class="input tabContent">
			<tr>
				<td>
				<textarea id="mobileEditor" name="itemDescMobile.description" class="editor" style="width:100%;">
					${descMobile.description}
				</textarea>
				</td>
			</tr>
	</table>
</div>
