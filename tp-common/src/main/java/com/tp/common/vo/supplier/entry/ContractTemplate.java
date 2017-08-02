package com.tp.common.vo.supplier.entry;

/**
 * 合同模板
 * 
 */
public enum ContractTemplate {
	
	//非海淘  自营/代销
	PLDF("PLDF","品类代发","西客商城供应商合作运营协议","pdf/PLDF_template.pdf"),
	//非海淘 代发
	PLRC("PLRC","品类入仓","西客商城商品销售合同","pdf/PLRC_template.pdf"),
	//海淘  自营/代销
	HTZY("HTZY","海淘直邮","西客商城","pdf/HTZY_template.pdf"),
	//海淘  代发
	HTBS("HTBS","海淘保税","西客商城","pdf/HTBS_template.pdf");
	
	private String key;
    private String name;
    private String title;
    private String templateFile;
    
    private ContractTemplate(String key,String name,String title,String templateFile){
    	this.key = key;
    	this.name = name;
    	this.title = title;
    	this.templateFile = templateFile;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}


}
