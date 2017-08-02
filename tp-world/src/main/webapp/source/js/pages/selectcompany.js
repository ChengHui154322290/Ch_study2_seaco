$(function(){
	
	if(isWeiXin()){
    	$(".ui-header").hide();
    	$(".ul_company_list").css({
    		"margin-top":"0"
    	})
    	$("title").html("物流公司");
    }
	
	
    var asid = xigou.getQueryString("asid");

    var _aftersaleslist = xigou.getLocalStorage("aftersaleslist");
    var datalist = JSON.parse(_aftersaleslist);
    var selectCompanyCode = -1;
    var Index = -1;
    for (var i = 0; i < datalist.length; i++) {
        var data = datalist[i];
        if (data.asid == asid) {
            selectCompanyCode = data.companycode;
            Index = i;
            break;
        }
    }

    var params = {
        'token': xigou.getToken(),
    };

    xigou.aftersales.companylist({
        requestBody: params,
        callback:function(response, status) { // 回调函数
            if (status != xigou.dictionary.success || 0 != response.code) {
                return;
            }

            if (!response.data || !response.data.length < 0) {
                return;
            }

            var length = response.data.length;
            var htmlData = [];
            for (var i = 0; i < length; i++) {
                var companyInfp =response.data[i];
                if (companyInfp.code == selectCompanyCode) {
                    htmlData.push('<li class="div_company div_company_sel" ccode="' + companyInfp.code + '">' + companyInfp.name + '</li>');
                }
                else {
                    htmlData.push('<li class="div_company" ccode="' + companyInfp.code + '">' + companyInfp.name + '</li>');
                }
            }
            $(".ul_company_list").append(htmlData.join(" "));

            $('.div_company')[xigou.events.click](function(){
                var ccode = this.getAttribute('ccode');
                var cname = this.innerHTML;

                if (Index != -1) {
                    datalist[Index].company = cname;
                    datalist[Index].companycode = ccode;
                }

                xigou.setLocalStorage("aftersaleslist",JSON.stringify(datalist));
                history.go(-1);
            })
        }
    })
})