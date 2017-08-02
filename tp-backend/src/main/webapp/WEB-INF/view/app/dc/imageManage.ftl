<#include "/common/common.ftl"/>
<@backend title="APP首页动态配置" js=[
'/statics/backend/js/dateTime2/js/jquery-ui.min.js',
'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-addon.js',
'/statics/backend/js/dateTime2/js/jquery-ui-timepicker-zh-CN.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/js/validation/jquery.validate.min.js',
'/statics/backend/js/formValidator/formValidator-4.0.1.min.js',
'/statics/backend/js/formValidator/formValidatorRegex.js',
'/statics/backend/js/formValidator/DateTimeMask.js',
'/statics/backend/js/layer/layer.min.js',
'/statics/qiniu/js/plupload/plupload.full.min.js',
'/statics/qiniu/js/plupload/plupload.dev.js',
'/statics/qiniu/js/plupload/moxie.js',
'/statics/qiniu/js/plupload/moxie.js',
'/statics/qiniu/src/qiniu.js',
'/statics/qiniu/js/highlight/highlight.js',
'/statics/qiniu/js/ui.js',
'/statics/qiniu/xgUplod.js',
'/statics/backend/js/app/image_manage_upload.js',
 '/statics/backend/js/app/dc.js']
css=[
'/statics/backend/js/jqueryui/css/cupertino/jquery-ui-1.9.2.custom.min.css',
'/statics/backend/js/dateTime2/css/jquery-ui-timepicker-addon.css'] >
    <#if result.msg??>${result.msg.message}<#else>
        <#assign info = result.data>
    <style>
        * {
            margin: 0;
            padding: 0;
        }

        .main {
            width: 250px;
            min-height: 250px;
            /*border: 1px solid red;*/
            float: left;
            margin: 20px;
        }

        .picture {
            width: 200px;
            height: 200px;
            /*border: 1px solid red;*/
            margin: 0 auto;
            margin-top: 10px;
        }

        .picture_url {
            width: 200px;
            margin: 10px auto;
            word-wrap: break-word;
        }

        .picture_url img {
            width: 200px;
        }

        .box_border_child{
            width: 100%;
            height: 30px;
            line-height: 30px;
            text-align: center;
            position: absolute;
            bottom: 50px;
        }

        .img_content{
            position:relative;
            float: left;
            padding-bottom:100px;
        }

        .box_border{
            border:none;
        }

        .border{
            border:1px solid #d3dbde;
        }

    </style>

    <form id=" ">
        <div id="search_bar" class="mt10">
            <input type="hidden" value="${bucketname}" id="bucketname">
            <input type="hidden" value="${bucketURL}" id="bucketURL">
            <div class="box">
                <div class="box_border border">
                    <div id="container"">
                    <a class="btn btn-default btn-lg " id="pickfiles" href="#" imagenameattribute="logo">
                        <i class="glyphicon glyphicon-plus"></i>
                        <button>选择文件</button>
                    </a>
                </div>

                    <#--<button class="ext_btn ext_btn_submit" id="pickfiles">上传图片</button>-->
                </div>
                <div class="img_content">
                    <div class="box_border box_border_content" id="img-list">

                    </div>
                    <div class="box_border box_border_child border">
                        <div id="load-more" style="cursor: pointer">加載更多</div>
                        <#--<input type="button" value="加載更多" id="load-more">-->
                    </div>
                </div>
            </div>

        </div>
    </form>
    </#if>
</@backend>
