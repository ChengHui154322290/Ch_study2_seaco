// 退货原因
$(function(){
    var _aftersalesparam = xigou.getLocalStorage("aftersalesparam");
    var aftersalesparam = JSON.parse(_aftersalesparam);

    if (aftersalesparam && aftersalesparam.reasonCode) {
        $('#id_reson' + aftersalesparam.reasonCode).addClass('div_reson_sel');
    }
    SelectReson(aftersalesparam);
})

function SelectReson(aftersalesparam) {
    $('.div_reson')[xigou.events.click](function(){
        aftersalesparam.reasonCode = this.getAttribute('reasonCode');
        aftersalesparam.reasonCodeDesc = this.innerText;
        xigou.setLocalStorage("aftersalesparam",JSON.stringify(aftersalesparam));
        window.location.href = 'customerservice.html';
    })
}