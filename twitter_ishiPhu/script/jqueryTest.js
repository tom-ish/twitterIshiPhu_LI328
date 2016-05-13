

function testFade()
{
	$(document).ready(function(){
		$("#effet").click(function(){
			$("#div1").fadeIn("1500");
		});
	});
}


function testHideShow(){
	$(document).ready(function(){
		$("#hide").click(function(){
			$("p").hide();
		});
		$("#show").click(function(){
			$("p").show();
		});
	});
}

function slideShow()
{
	$(document).ready(function(){
		$("#slideOnButton").click(function(){
			$('#slide').slideDown("normal");
			$('#slideOnButton').hide();
			$('#slideOffButton').show();
		});
	});
}

function slideHide(){
	$(document).ready(function(){
		$("#slideOffButton").click(function(){
			$('#slide').slideUp("normal");
			$('#slide_hidden').show();
		});
	});		
}

function showHideMenu(){
	$(function(){
		  //Afficher
		  $(".entreprises-top-menu a").mouseenter(function(){
		    $("#sub-menu-entreprise-container").css({
		       'display': 'block'          
		    });        
		  });  


		  //Masquer
		  $("#sub-menu-entreprise-container").mouseleave(function(){
		    $("#sub-menu-entreprise-container").css({
		      'display': 'none'        
		    });        
		  });
	});
}

function toggleTest(){
	// On attend que la page soit chargée 
	jQuery(document).ready(function()
	{
	   // On cache la zone de texte
	   jQuery('#toggle').hide();
	   // toggle() lorsque le lien avec l'ID #toggler est cliqué
	   jQuery('#toggler').click(function()
	  {
	      jQuery('#toggle').toggle(400);
	      return false;
	   });
	});
}


(function($){
	$.fn.vTicker = function(options) {
		var defaults = {
			speed: 700,
			pause: 4000,
			showItems: 3,
			animation: '',
			mousePause: true,
			isPaused: false,
			direction: 'up',
			height: 0
		};

		var options = $.extend(defaults, options);

		moveUp = function(obj2, height, options){
			if(options.isPaused)
				return;
			
			var obj = obj2.children('ul');
			
	    	var clone = obj.children('li:first').clone(true);
			
			if(options.height > 0)
			{
				height = obj.children('li:first').height();
			}		
			
	    	obj.animate({top: '-=' + height + 'px'}, options.speed, function() {
	        	$(this).children('li:first').remove();
	        	$(this).css('top', '0px');
	        });
			
			if(options.animation == 'fade')
			{
				obj.children('li:first').fadeOut(options.speed);
				if(options.height == 0)
				{
				obj.children('li:eq(' + options.showItems + ')').hide().fadeIn(options.speed).show();
				}
			}

	    	clone.appendTo(obj);
		};
		
		moveDown = function(obj2, height, options){
			if(options.isPaused)
				return;
			
			var obj = obj2.children('ul');
			
	    	var clone = obj.children('li:last').clone(true);
			
			if(options.height > 0)
			{
				height = obj.children('li:first').height();
			}
			
			obj.css('top', '-' + height + 'px')
				.prepend(clone);
				
	    	obj.animate({top: 0}, options.speed, function() {
	        	$(this).children('li:last').remove();
	        });
			
			if(options.animation == 'fade')
			{
				if(options.height == 0)
				{
					obj.children('li:eq(' + options.showItems + ')').fadeOut(options.speed);
				}
				obj.children('li:first').hide().fadeIn(options.speed).show();
			}
		};
		
		return this.each(function() {
			var obj = $(this);
			var maxHeight = 0;

			obj.css({overflow: 'hidden', position: 'relative'})
				.children('ul').css({position: 'absolute', margin: 0, padding: 0})
				.children('li').css({margin: 0, padding: 0});

			if(options.height == 0)
			{
				obj.children('ul').children('li').each(function(){
					if($(this).height() > maxHeight)
					{
						maxHeight = $(this).height();
					}
				});

				obj.children('ul').children('li').each(function(){
					$(this).height(maxHeight);
				});

				obj.height(maxHeight * options.showItems);
			}
			else
			{
				obj.height(options.height);
			}
			
	    	var interval = setInterval(function(){ 
				if(options.direction == 'up')
				{ 
					moveUp(obj, maxHeight, options); 
				}
				else
				{ 
					moveDown(obj, maxHeight, options); 
				} 
			}, options.pause);
			
			if(options.mousePause)
			{
				obj.bind("mouseenter",function(){
					options.isPaused = true;
				}).bind("mouseleave",function(){
					options.isPaused = false;
				});
			}
		});
	};
	})(jQuery);




// Diaporama Background
function startDiapo(){
	/*
	function cycleImages(){
	      var $active = $('#fadein_background .active');
	      var $next = ($('#fadein_background .active').next().length > 0) ? $('#fadein_background .active').next() : $('#fadein_background img:first');
	      $next.css('z-index',-2);//move the next image up the pile
		  $active.fadeOut(1500,function(){//fade out the top image
		  $active.css('z-index',-3).show().removeClass('active');//reset the z-index and unhide the image
	      $next.css('z-index',-1).addClass('active');//make the next image the top one
	      });
	    }

	    $(window).load(function(){
			$('#fadein_background').fadeIn(1500);//fade the background back in once all the images are loaded
			  // run every 7s
			  setInterval('cycleImages()', 2000);
	    })
	
	*/
	$(function() {
		$('#fadein_background img:gt(0)').hide();
		setInterval(function () {
		    $('#fadein_background :first-child').fadeOut()
		                             .next('img')
		                             .fadeIn()
		                             .end()
		                             .appendTo('#fadein_background');
		}, 2000); // 4 seconds
	});
}