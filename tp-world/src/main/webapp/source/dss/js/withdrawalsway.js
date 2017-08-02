$(function(){
    selectWay();
});

function selectWay(){
    $('.div_sel_way')[xigou.events.click](function(){
        var withdrawalsParam = xigou.getSessionStorage('withdrawalsParam', true);
        if (!withdrawalsParam) {
            return;
        }
        var $this = $(this);
        withdrawalsParam.payway = $this.attr('way');
        xigou.setSessionStorage('withdrawalsParam', withdrawalsParam, true);
        setTimeout(window.location.href = 'withdrawals.html', 250);
    })
}