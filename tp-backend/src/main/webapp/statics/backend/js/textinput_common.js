//只能输入数字及小数点保留两位数
function doubleTextKeyup(current,event){
	if(event.keyCode>=37&&event.keyCode<=40){
		return false;
	}
	var val=current.val().replace(/[^0-9\.]/g,'');
	var priceary = val.split('\.');
	if(priceary.length>=2){
		priceary[1]=priceary[1].replace(/^(\d{1,2})\d*$/g,'$1');
		val=priceary[0]+'.'+priceary[1];
	}
	if(isNaN(val)){
		val='';
	}
	current.val(val);
}
function doubleTextKeyDown(current,event){
	var curVal =current.val();
	if(event.keyCode<37 && event.keyCode!=8 && event.keyCode!=9){
		return false;
	}
	if(event.keyCode>40 && event.keyCode<46){
		return false;
	}
	if(event.keyCode>57 &&(event.keyCode<96 || (event.keyCode>108 && event.keyCode!=190 && event.keyCode!=110))){
		return false;
	}
	if(curVal.indexOf('.')!=-1 && (event.keyCode==190 || event.keyCode==110)){
		return false;
	}
	if(curVal=='' && (event.keyCode==190||event.keyCode==110)){
		return false;
	};
	return true;
}
$('input:text[amounttext]').live('blur keyup paste',function(event){
	doubleTextKeyup($(this),event);
}).live('keydown paste',function(event){
	if(!doubleTextKeyDown($(this),event)){
		return false;
	}
});
$('input:text[moneytext]').live('keyup paste',function(event){
	doubleTextKeyup($(this),event);
}).live('keydown paste',function(event){
	var current = $(this);
	var k = current.textposition();
	var value = current.val();
	var v = value.indexOf('.');
	if(k==v && k==1 && event.keyCode==8 ){
		return false;
	}
	if(k==0 && v==1 && event.keyCode==46){
		return false;
	}
	var isNotPass= event.keyCode!=46 && event.keyCode!=8 && event.keyCode!=9 && event.keyCode!=35 && event.keyCode!=36 && event.keyCode!=37 && event.keyCode!=39;
	if(/\d+\.\d{2}/.test(value) && isNotPass && k>=current.val().length){
		return false;
	}
	if(!doubleTextKeyDown(current,event)){
		return false;
	}
}).live('blur',function(event){
	var current = $(this);
	var money = current.val();
	doubleTextKeyup(current,event);
	if(money.indexOf('.')==money.length-1){
		money=money+'00';
	}
	if(money!=null && money!=''){
		current.val(parseFloat(money).toFixed(2));
	}
	if(parseFloat(money)<=0){
		current.val('');
	}
});

//只能输入整数
$('input:text[integertext]').live('blur keyup paste',function(event){
	if(event.keyCode>=37&&event.keyCode<=40){
		return true;
	}
	var val=$(this).val().replace(/[^0-9]/g,'');
	$(this).val(val);
}).live('keydown',function(event){
	if(event.keyCode<37 && event.keyCode!=8 && event.keyCode!=9){
		return false;
	}
	if(event.keyCode>40 && event.keyCode<46){
		return false;
	}
	if(event.keyCode>57 && (event.keyCode<96 || event.keyCode>108)){
		return false;
	}
});

//正整数
$('input:text[positiveInteger]').live('keyup paste keydown',function(event){
	var val=$(this).val().replace(/[^0-9]/g,'');
	if(/^[1-9]\d*/.test(val)){
		$(this).val(val);
	}
}).live('blur',function(event){
	if (!$(this).val()||$(this).val()==0) {
		$(this).val('1');
	}
});


$.fn.extend({
    textposition:function( value ){
        var elem = this[0];
        if (elem&&(elem.tagName=="TEXTAREA"|| elem.tagName=="TEXT" || (elem.type!=undefined && elem.type!=null && elem.type.toLowerCase()=="text"))) {
               if($.browser.msie){
                       var rng;
                       if(elem.tagName == "TEXTAREA"){ 
                            rng = event.srcElement.createTextRange();
                            rng.moveToPoint(event.x,event.y);
                       }else{ 
                            rng = document.selection.createRange();
                       }
                       if( value === undefined ){
                         rng.moveStart("character",-event.srcElement.value.length);
                         return  rng.text.length;
                       }else if(typeof value === "number" ){
                         var index=this.textposition();
                         index>value?( rng.moveEnd("character",value-index)):(rng.moveStart("character",value-index));
                         rng.select();
                       }
                }else{
                    if( value === undefined ){
                         return elem.selectionStart;
                       }else if(typeof value === "number" ){
                         elem.selectionEnd = value;
                         elem.selectionStart = value;
                       }
                }
            }else{
                if( value === undefined )
                   return undefined;
            }
    }
});