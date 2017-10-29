var mk = mk || {};

(function ($) {
	
	/**
	 * BootStrap模态框(封闭)
	 * @version 1.0
	 * @author 马凯
	 * @date 2016-11-02
	 */
	$.fn.mkModal = function(options, param){
		if (typeof options == "string") {
	        return $.fn.mkModal.methods[options](this, param);
	    }
	    options = options || {};

	    return this.each(function () {
			var state = $(this).data("mkModal");
			var opts;
			 if (state) {
                opts = $.extend(state.options, options);
                state.isLoaded = false;
            } else {
                opts = $.extend({}, $.fn.mkModal.defaults, $.fn.mkModal.parseOptions(this), options);
                $(this).attr("title", "");
                var aaaaaa = { options: opts, mkModal: wrapMkModal(this), isLoaded: false };
                state = $(this).data( "mkModal", aaaaaa);
            }
		});
	}
	
//	function test(options){
//		var modal = $( 
//			'<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">' + 
//				'<div class="modal-dialog modal-lg">' +
//					'<div class="modal-content">' +
//						'<div class="modal-header">' + 
//						'</div>' + 
//						'<div class="modal-body">' + 
//						'</div>' + 
//						'<div class="modal-footer">' +
//							'<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>' +
//							'<button type="button" class="btn btn-primary">Save changes</button>' +
//						'</div>' +
//					'</div>' +
//				'</div>' +
//			'</div>');
//		if(options.closed){
//			modal.find('.modal-header').append('<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>');
//		}
//		modal.find('.modal-header').append('<h4 class="modal-title" id="myModalLabel">' + options.title + '</h4>');
//		modal.modal({
//			keyboard: false,
//			backdrop:'static',
//			show:options.show
//		});		
//	}
	
	function addHeader(){
		
	}
	
	//所有方法
	$.fn.mkModal.methods = {
		 //返回属性对象
        options: function (jq) {
            return $(jq[0]).data("mkModal").options;
        }
	}
	
	//解析器配置
    $.fn.mkModal.parseOptions = function (target) {
        var t = $(target);
        var options = {};
        var opts = {};
        var properties = ["id", "width", "height", "left", "top", "title", "iconCls", "cls", "headerCls", "bodyCls", "tools", "href",
                          { cache: "boolean", fit: "boolean", border: "boolean", noheader: "boolean" },
                          { collapsible: "boolean", minimizable: "boolean", maximizable: "boolean" },
                          { closable: "boolean", collapsed: "boolean", minimized: "boolean", maximized: "boolean", closed: "boolean" }];
        
        
        
		for(var i=0; i<properties.length; i++){
			var pp = properties[i];
			if (typeof pp == 'string'){
				opts[pp] = t.attr(pp);
			} else {
				for(var name in pp){
					var type = pp[name];
					if (type == 'boolean'){
						opts[name] = t.attr(name) ? (t.attr(name) == 'true') : undefined;
					} else if (type == 'number'){
						opts[name] = t.attr(name)=='0' ? 0 : parseFloat(t.attr(name)) || undefined;
					}
				}
			}
		}
		$.extend(options, opts);
        return $.extend({}, options, { loadingMessage: (t.attr("loadingMessage") != undefined ? t.attr("loadingMessage") : undefined) });
                
    };
    
    //包裹内容
    function wrapMkModal(target) {
    	$(target).addClass("modal-body");
    	var kkk  = $('<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>').append('<div class="modal-dialog modal-lg"></div>');
        var mkModal = $('<div class="modal-content"></div>').insertBefore(target);
        mkModal[0].appendChild(target);
        
        
        var bbb = kkk.find('.modal-dialog').insertBefore(mkModal[0]);
        bbb[0].append(target)
        return mkModal;
    	
    	/*
        $(target).addClass("modal-body");
        var mkModal = $('<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>').append('<div class="modal-dialog modal-lg"></div>');
        var kk = $('<div class="modal-content" ></div>');
        console.info(kk);
        kk.insertBefore($(target));
        console.info(kk.children());
        //console.info($(target).prop('outerHTML'));
        //mkModal.find('.modal-dialog').append();
        mkModal[0].appendChild(target);
        mkModal.bind("_resize", function () {
            var opts = $(target).data("mkModal").options;
            if (opts.fit == true) {
                _resize(target);
            }
            return false;
        });
        return mkModal;
        */
    };
})(jQuery);
