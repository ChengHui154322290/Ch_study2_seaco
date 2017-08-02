<#include "/common/common.ftl"/>
<@backend title="消息页" js=[
    '/statics/supplier/js/common.js'
    ] 
    css=[] >
    ${message}!
    
    <script>
       var nextTab = "${(nextTab)!}";
       var needAlert = "${(needAlert)!}";
       var msg = "${(message)!}";
       jQuery(document).ready(function(){
           if(needAlert){
               alertMsg(msg);
           }
           if(nextTab){
               refreshAndCloseThis(nextTab);
           }
       });
    </script>
</@backend>