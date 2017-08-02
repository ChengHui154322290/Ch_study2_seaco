$(function() {
	$(".btn-app")[xigou.events.click]
			(function() {

				// 打开本地应用函数
				var open = function(url, timeout) {
					function try_to_open_app() {
						timeout = setTimeout(function() {
							window.location.href = url;
						}, timeout);
					}
					try_to_open_app();
				}

				if (/android/i.test(navigator.userAgent)) {
					if (/MicroMessenger/i.test(navigator.userAgent)) {
						alert("This is MicroMessenger browser,请使用本地浏览器打开");// 这是微信平台下浏览器
					} else {
						open("seagoor://app.51seaco.com?page=1", 10);
						open(
								"http://android.myapp.com/myapp/detail.htm?apkName=com.tp.gj",
								1000);
					}
				}

				if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {
					// alert(This is iOS'browser.);//这是iOS平台下浏览器
					if (/MicroMessenger/i.test(navigator.userAgent)) {
						alert("微信内置浏览器不支持打开本地应用,请点击右上角使用本地浏览器打开");// 这是微信平台下浏览器
					} else {
						open("https://itunes.apple.com/cn/app/xi-gou-quan-qiu-gou/id1080175355?mt=8");
					}
				}

			});

});
