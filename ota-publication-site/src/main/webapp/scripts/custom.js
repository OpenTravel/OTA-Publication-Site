jQuery(document).ready(function($){
	$('.custom-carousel li').slice(0, 4).clone().appendTo($(".custom-carousel ul"));
	d = $('.custom-carousel li').length - 4;
	$('.custom-carousel li').slice(1, $(".custom-carousel li").length - 4).clone().prependTo($(".custom-carousel ul"));
	$('.custom-carousel li').slice(d-1, d+3).css("display", "block");
	$('#carousel-home').jcarouselAutoscroll({
    autostart: true
});
	
	//portfolio - show link
	jQuery('.fdw-background').hover(
		function () {
			jQuery(this).animate({opacity:'1'});
		},
		function () {
			jQuery(this).animate({opacity:'0'});
		}
	);	
	  
	  $(window).resize(function() {
		  clearTimeout(slider_timeout);
		  if ( $(window).width() > 767 ) {
			  slider_timeout = setTimeout( function() {
					  $(".slider-wrapper").removeAttr("style");
		  				if ( $(".navbar-collapse").hasClass("in") ) {
						  	$(".navbar-collapse").removeClass("in");
						  	$(".navbar-collapse").addClass("collapse");
						}
			  }, 300);
		  }
	  });
	
	function carouselLoop(){
		var q = 0;
		setTimeout(function() {
				$('.custom-carousel').find('li:visible:first').hide();
				if (q < $(".custom-carousel li").length - 8){	
					var n = $('.custom-carousel').find('li:visible:last').index();
					$(".custom-carousel li").eq(n+1).show();
					q = q + 1;
				} else {
					q = 3;
					$(".custom-carousel li").css("display", "none");
					$('.custom-carousel li').slice(d-1, d+3).css("display", "block");
					//m + 1;
				}
			carouselLoop();
		}, 5000);
	}
	
	carouselLoop();

});
