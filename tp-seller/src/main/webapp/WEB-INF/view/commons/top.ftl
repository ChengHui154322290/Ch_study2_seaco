<div class="navbar-header pull-left">
    <a href="#" class="navbar-brand">
        <small>
            <i class="icon-leaf"></i>
            西客商家后台系统
        </small>
    </a><!-- /.brand -->
</div><!-- /.navbar-header -->
<div class="navbar-header pull-right" role="navigation">     
    <ul class="nav ace-nav">
        <li class="light-blue">
            <a data-toggle="dropdown" href="#" class="dropdown-toggle">
                <img class="nav-user-photo" src="${staticPath}/static/assets/avatars/user.jpg" alt="" />
                <span class="user-info">
                    <small>欢迎光临,</small>
                    ${(user_name_key)!}
                </span>
                <i class="icon-caret-down"></i>
            </a>
	
            <ul class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
                <li>
                    <a href="javascript:void(0)" onclick="showUserInfo('${(user_name_key)!}')">
                        <i class="icon-cog"></i>修改密码
                    </a>
                </li>
                <li>
                    <a href="/tologout.htm">
                        <i class="icon-off"></i>退出
                    </a>
                </li>
            </ul>
        </li>
        <li>
          <a href="javascript:void(0)" data-toggle="modal" data-target=".bs-example-modal-lg" onclick="showUserInfo('${(user_name_key)!}')">修改密码</a>
        </li>
        <li class="dropdown">
            <a href="/tologout.htm" class="dropdown-toggle" >退出</a>
        </li>
    </ul>
</div>    