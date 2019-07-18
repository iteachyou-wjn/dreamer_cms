/**
 * jquery 提示插件
 * 王俊南 
 * 2018-07-17
 * 
 * $("body").MessageBox({
		type: 'success',
		message:result.info,
		callbak:function(){
			window.location.href = "/unit/index";
		}
	});
 */
(function($){
	$.fn.MessageBox = function(options){
		var $this = $(this);
		var _this = this;
		return this.each(function(){
		    var loadingPosition ='';
		    var defaultProp = {
		    	type : "tips",//tips    warn    error
		    	message: "",
		    	width:300,
		    	height:40,
		    	top:30,
		    	timeout:1500,
		    	callbak:null
		    };

		    var opt = $.extend(defaultProp,options || {});
		    
		    //初始化
		    defaultProp._initialization = function(){
		    	defaultProp._render();
		    };
		    
		    defaultProp._render = function(){
		    	//UUID
			    var s = [];
	            var hexDigits = "0123456789abcdef";
	            for (var i = 0; i < 36; i++) {
	                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
	            }
	            s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
	            s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
	            s[8] = s[13] = s[18] = s[23] = "-";
	            var uuid = s.join("");
		    	$div = $("<div id='message-box"+uuid+"' class='message-box'></div>");
		    	$background = $("<div></div>").text(opt.message);
		    	$closeBtn = $("<div class='message-box-close'><i></i></div>");
		    	
		    	$div.append($background);
		    	$div.append($closeBtn);
		    	var windowWidth = $(window).width();
		    	
		    	$div.css({
		    		left: ((windowWidth - opt.width) / 2) + "px",
		    		width: opt.width + "px",
		    		height: opt.height + "px",
		    		lineHeight: opt.height + "px"
		    	});
		    	
		    	$background.css({
		    		width: opt.width + "px",
		    		height: opt.height + "px",
		    	});
		    	
		    	if(opt.type == "tips"){
		    		$div.addClass("message-tips");
		    		$background.addClass("message-tips-background message-tips-label");
		    	}else if(opt.type == "success"){
		    		$div.addClass("message-success");
		    		$background.addClass("message-success-background message-success-label");
		    	}else if(opt.type == "warn"){
		    		$div.addClass("message-warn");
		    		$background.addClass("message-warn-background message-warn-label");
		    	}else if(opt.type == "error"){
		    		$div.addClass("message-error");
		    		$background.addClass("message-error-background message-error-label");
		    	}
		    	
		    	$this.append($div);
		    	
		    	var flag = false;
		    	
		    	//绑定关闭按钮
		    	$closeBtn.click(function(){
		    		flag = true;
		    		$div.remove();
		    	});
		    	
		    	if($div != null){
		    		$div.fadeOut(opt.timeout,function(){
		    			if(opt.callbak){
				    		opt.callbak();
				    	}
		    		});
		    	}
		    }
		    
		    defaultProp._initialization();
		});
	}

})(jQuery)