/**
 * Zepto picLazyLoad Plugin
 * ximan http://ons.me/484.html
 * 20140517 v1.0
 */
;
(function($) {
	$.fn.picLazyLoad = function(settings) {
		var $this = $(this),
			_winScrollTop = 0,
			_winHeight = $(window).height();
		settings = $.extend({
			threshold: 0,
			placeholder: 'modifiable/img/loading_bg.png'
		}, settings || {});
		lazyLoadPic();
		$(window).on('scroll', function() {
			_winScrollTop = $(window).scrollTop();
			lazyLoadPic();
		});

		function lazyLoadPic() {
			$this.each(function() {
				var $self = $(this);
				if ($self.is('img')) {
					if ($self.attr('data-original')) {
						var _offsetTop = $self.offset().top;
						if ((_offsetTop - settings.threshold) <= (_winHeight + _winScrollTop)) {
							$self.attr('src', $self.attr('data-original'));
							$self.removeAttr('data-original');
						}
					}
				} 
			});
		}
	}
})(Zepto)