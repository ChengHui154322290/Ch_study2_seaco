<#include "/common/common.ftl"/>
<@backend title="" js=['/statics/backend/js/layer/layer.min.js',
'/statics/backend/js/basedata/category.js']
css=[] >

<style>
    .my-btn {
        padding-right: 75px;
        width: 200px;
    }
</style>

<div class="mt10">
    <div class="box">
        <div class="box_border">
            <div class="box_center pt10 pb10">
                <form class="jqtransform" method="post" id="catEdit">

                    <div id="div1_submit" style="text-align:left;">


                        <input class="btn btn82  my-btn" type="button" value="update"/>
                        </br>
                        <input class="btn btn82  my-btn" type="button" value="updateSearchData"/>
                        </br>
                        <input class="btn btn82  my-btn" type="button" value="updateDoc"/>
                        </br>
                        <input class="btn btn82  my-btn" type="button" value="updateDataAndDoc"/>
                        </br>
                        <input class="btn btn82  my-btn" type="button" value="updateDataAndDocTotal"/>
                        </br>
                        <input class="btn btn82  my-btn" type="button" value="clearDoc"/>
                        </br>
                        <input class="btn btn82  my-btn" type="button" value="updateSHOPDoc"/>
                        </br>
                        <input class="btn btn82  my-btn" type="button" value="updateSHOPDocTOTAL"/>
                        </br>

                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
    <script>
        $(function () {
            $(".my-btn").on("click", function () {
                var _this = this;
                var v = $(_this).val();
                var url = domain + "/search/operate/" + v;
                $.post(url, function (data) {
                    if (data.success) {
                        alert("SUCCESS");
                    }
                    else {
                        alert(data.msg.message);
                    }

                })

            })
        });


    </script>

</@backend>