function indexInit(availableTags){
	//菜单模板
	navtemplate = Handlebars.compile('{{#each this}}<div class="dr-framenav-c-i">{{#if children}}<h3><i class="fa {{icon}} fa-lg"></i><span>{{text}}</span><i class="dr-fr fa fa-angle-down fa-lg"></i></h3><div class="dr-framenav-c-i-c">{{#each children}}<a href="{{href}}">{{text}}</a>{{/each}}</div>{{else}}<h3><i class="fa {{icon}} fa-lg"></i><span><a href="{{href}}">{{text}}</a></span></h3>{{/if}}</div>{{/each}}');
	var aIframe = ['<div class="show-iframe"><iframe scrolling="yes" data-href="','','" frameborder="0" src="','','"></iframe></div>'];
	//选项卡模板
	var aTab = ['<li class="dr-li active" data-href="','','"><a class="dr-tabwrapper-refresh" title="点击刷新"><span class="fa fa-circle-thin"></span><b class="dr-refresh fa fa-refresh"></b></a><div>','未知','</div><a id="" href="" class="dr-tabwrapper-close" hidefocus="" title="点击关闭标签"><span class="fa fa-circle-thin"></span><b class="fa fa-close"></b></a></li>'];
	//固定选项卡模板
	var aTabUnfold = ['<li class="dr-li active" data-href="','','"><a class="dr-tabwrapper-refresh" title="点击刷新"><span class="fa fa-circle-thin"></span><b class="dr-refresh fa fa-refresh"></b></a><div>','未知','</div><a id="" href="" style="display:none;" class="dr-tabwrapper-close" hidefocus="" title="点击关闭标签"><span class="fa fa-circle-thin"></span><b class="fa fa-close"></b></a></li>'];
	
	var drUnfoldT = Handlebars.compile('{{#each this}}<li data-title="{{title}}" data-href="{{href}}" data-unfold="{{unfold}}"><span class="dr-close"><i class="fa fa-file-o"></i></span><a href="">{{title}}</a></li>{{/each}}');
	var drUserUnfoldT = Handlebars.compile('{{#each this}}<li data-title="{{title}}" data-href="{{href}}" data-unfold="{{unfold}}"><span class="dr-close"><i class="fa fa-file-o"></i></span><a href="">{{title}}</a><span title="点击解锁" class="dr-unlock"><i class="fa fa-lock fa-lg"></i></span></li>{{/each}}');
	var drTabListT = Handlebars.compile('{{#each this}}<li data-title="{{title}}" data-href="{{href}}" data-unfold="{{unfold}}"><span class="dr-close"><i class="fa fa-file-o"></i></span><a href="">{{title}}</a><span title="点击锁定" class="dr-lock"><i class="fa fa-unlock-alt fa-lg"></i></span><span title="点击关闭" class="dr-close"><i class="fa fa-close fa-lg"></i></span></li>{{/each}}');
	
	//导航快捷搜索数据格式化
	var availableTagsU = _.reduce(availableTags, function(ary, obj){ 
		if(_.has(obj, "children")){
			return _.reduce(obj.children,function(ary, obj){
				ary.push([obj.text,obj.href])
				return ary;
			},ary)
		}
		ary.push([obj.text,obj.href])
		return ary; 
	}, []);
	
	//导航快捷搜索autocomplete模板样式
	$.widget( "ui.autocomplete", $.ui.autocomplete, {
		_renderItem: function( ul, item ) {
		  return $( "<li>" )
			.attr( "data-href", item[1] )
			.append( item[0] )
			.appendTo( ul );
		}
	});
	
	//左侧菜单生成
	$("#dr-framenav .dr-framenav-c").append($(navtemplate(availableTags)));
	
	
	
	//左侧菜单折叠展开
	$(".dr-framenav-c").on("click",".dr-framenav-c-i h3",function(e){
		var $this = $(this).parents(".dr-framenav-c-i");
		if($this.find(".dr-framenav-c-i-c").length==0){
			$this.addClass("active2").siblings().removeClass("active2");
		}else if($this.hasClass("active")){
			$this.removeClass("active");
			$this.find(".dr-framenav-c-i-c").slideUp();
			$this.find("h3 i.dr-fr").removeClass("fa-rotate-180");
		}else{
			$this.addClass("active").find(".dr-framenav-c-i-c").slideDown().end().siblings().removeClass("active").removeClass("active2").find(".dr-framenav-c-i-c").slideUp();
			$this.find("h3 i.dr-fr").addClass("fa-rotate-180");
			$this.siblings().find("h3 i.dr-fr").removeClass("fa-rotate-180");
		}
	})
	$(".dr-framenav-c-i").each(function(index, element) {
        if($(this).hasClass("select")){
			$(this).find("h3").click();
		}
    });
	
	//tab 刷新
	$("#dr-tabwrapper").on("click",".dr-li .dr-tabwrapper-refresh",function(){
		var $parent = $(this).parents("li");
		var shref = $parent.data("href");
		
		$("#fr-iframebox .show-iframe").each(function(index, element) {
            if($(this).data("href")==shref){
				$(this).find('iframe').attr('src',shref);
				return false;
			}
        });
		
		return false;
	})
	
	//tab 关闭
	$("#dr-tabwrapper").on("click",".dr-li .dr-tabwrapper-close",function(){
		var $parent = $(this).parents("li");
		var shref = $parent.data("href");
		
		$("#fr-iframebox .show-iframe").each(function(index, element) {
            if($(this).data("href")==shref){
				$(this).remove();
				$parent.remove();
				return false;
			}
        });
		
		
		if($parent.hasClass("active")){
			$("#fr-iframebox .show-iframe:last-child").show().siblings().hide();
			$("#dr-tabwrapper ul li:last").addClass("active").siblings().removeClass("active");
		}
				
		
		menuManage()
		return false;
	})
	
	//tab 切换
	$("#dr-tabwrapper").on("click","li",function(){
		var $this = $(this);
		var shref = $this.data("href");
		$(this).addClass("active").siblings().removeClass("active")
		$("#fr-iframebox .show-iframe").each(function(index, element) {
            if($(this).data("href")==shref){
				$(this).show().siblings().hide();
				return false;
			}
        });
		
		return false;
	})
	
	//菜单点击打开
	$(".dr-framenav-c").on("click",".dr-framenav-c-i a",function(e){
		e.preventDefault();
		var shref = $(this).attr("href");
		var stitle =  $(this).text();
		
		var matchState = false;
		$(".dr-framenav-c-i a").removeClass("active");
		$(this).addClass("active");

		$("#fr-iframebox .show-iframe").each(function(index, element) {
            if($(this).data("href")==shref){
				matchState = true;
				$("#dr-tabwrapper li").each(function(index, element) {
                    if($(this).data("href")==shref){
						$(this).addClass("active").siblings().removeClass("active");
						
						$("#fr-iframebox .show-iframe").each(function(index, element) {
							if($(this).data("href")==shref){
								$(this).show().siblings().hide();
								return false;
							}
						});
						
						return false;
					}
                });
				return false;
			}
        });
		if(matchState) return;
		if(shref){
			aIframe[1] = shref;
			aIframe[3] = shref;
			$(".show-iframe").hide();
			var $newIframe = $(aIframe.join(''));
			$newIframe.data("href",shref);
			$("#fr-iframebox").append($newIframe);
			
			aTab[1]= shref;
			aTab[3]= stitle;
			
			var $newTab = $(aTab.join(''));
			$newTab.data("href",shref);
			$newTab.data("title",stitle);
		
			$("#dr-tabwrapper ul li").removeClass("active");
			$("#dr-tabwrapper ul").append($newTab);		
			menuManage()
		}
		e.preventDefault();
	})
	
	//导航快捷搜索
    $( "#tags" ).autocomplete({
		source: availableTagsU,
		select: function( event, ui ) {
			var shref = ui.item[1];
			var stitle =  ui.item[0];
						
			var matchState = false;
	
			$("#fr-iframebox .show-iframe").each(function(index, element) {
				if($(this).data("href")==shref){
					matchState = true;
					$("#dr-tabwrapper li").each(function(index, element) {
						if($(this).data("href")==shref){
							$(this).addClass("active").siblings().removeClass("active");
							return false;
						}
					});
					return false;
				}
			});
			if(matchState) return;
			if(shref){
				aIframe[1] = shref;
				aIframe[3] = shref;
				$(".show-iframe").hide();
				var $newIframe = $(aIframe.join(''));
				$newIframe.data("href",shref);
				$("#fr-iframebox").append($newIframe);
				
				aTab[1]= shref;
				aTab[3]= stitle;
				
				var $newTab = $(aTab.join(''));
				$newTab.data("href",shref);
				$newTab.data("title",stitle);
			
				$("#dr-tabwrapper ul li").removeClass("active");
				$("#dr-tabwrapper ul").append($newTab);		
				menuManage()
			}
		}
    }).focus(function(){
		if($(this).val().length>0){
			$(".ui-autocomplete").show()
		}
	});
	
	//固定打开的标签
	function unfoldPageManage(ary){
		if(!_.isArray(ary) || _.isEmpty(ary) ) return;
		$.each(ary,function(i,v){
			var stitle = v[0];
			var shref = v[1];
			if(v[2]===1){
				aIframe[1] = shref;
				aIframe[3] = shref;
				$(".show-iframe").hide();
				var $newIframe = $(aIframe.join(''));
				$newIframe.data("href",shref);
				$("#fr-iframebox").append($newIframe);
				
				aTabUnfold[1]= shref;
				aTabUnfold[3]= stitle;
				
				var $newTab = $(aTabUnfold.join(''));
				$newTab.data("href",shref);
				$newTab.data("title",stitle);
				$newTab.data("unfold",1);
			
				$("#dr-tabwrapper ul li").removeClass("active");
				$("#dr-tabwrapper ul").append($newTab);	
			}else if(v[2]===2){
				aIframe[1] = shref;
				aIframe[3] = shref;
				$(".show-iframe").hide();
				var $newIframe = $(aIframe.join(''));
				$newIframe.data("href",shref);
				$("#fr-iframebox").append($newIframe);
				
				aTabUnfold[1]= shref;
				aTabUnfold[3]= stitle;
				
				var $newTab = $(aTabUnfold.join(''));
				$newTab.data("href",shref);
				$newTab.data("title",stitle);
				$newTab.data("unfold",2);
			
				$("#dr-tabwrapper ul li").removeClass("active");
				$("#dr-tabwrapper ul").append($newTab);	
			}else{
				aIframe[1] = shref;
				aIframe[3] = shref;
				$(".show-iframe").hide();
				var $newIframe = $(aIframe.join(''));
				$newIframe.data("href",shref);
				$("#fr-iframebox").append($newIframe);
				
				aTab[1]= shref;
				aTab[3]= stitle;
				
				var $newTab = $(aTab.join(''));
				$newTab.data("href",shref);
				$newTab.data("title",stitle);
				$newTab.data("unfold",0);
			
				$("#dr-tabwrapper ul li").removeClass("active");
				$("#dr-tabwrapper ul").append($newTab);	
			}
		})

	}
	unfoldPageManage(unfoldPage)
	
	
	
	//隐藏左侧菜单
	var menuSwtichState = true;
	$("#dr-tabwrapper-swtich").click(function(){
		if(menuSwtichState){
			$("#dr-tabwrapper-swtich i").addClass("fa-rotate-180");
			$("#dr-framenav").animate({"left":"-201px"},300)
			$("#dr-right").animate({"left":"0"},300,function(){
				menuSwtichState = false;
			})
		}else{
			$("#dr-tabwrapper-swtich i").removeClass("fa-rotate-180");
			$("#dr-framenav").animate({"left":"0px"},300)
			$("#dr-right").animate({"left":"200px"},300,function(){
				menuSwtichState = true;
			})
		}
		
	})
	
	//选项卡可拖动
	$("#dr-ul-wrap ul").draggable({
	  axis: "x"
	});
	
	
	
	//
	function menuManage(){
		var drUnfoldD = []
		var drUserUnfoldD = [];
		var drTabListD = [];
		$("#dr-tabwrapper li").each(function(index, element) {
			if(!$(this).data("unfold")){
				drTabListD.push(
					{
						title:$(this).data("title"),
						href:$(this).data("href"),
						unfold:$(this).data("unfold")
					}
				)
			}else if($(this).data("unfold")==1){
				drUnfoldD.push(
					{
						title:$(this).data("title"),
						href:$(this).data("href"),
						unfold:$(this).data("unfold")
					}
				)
			}else if($(this).data("unfold")==2){
				drUserUnfoldD.push(
					{
						title:$(this).data("title"),
						href:$(this).data("href"),
						unfold:$(this).data("unfold")
					}
				)
			}
		});
		$("#dr-menu-management .dr-unfold .dr-menu-management-item").html(drUnfoldT(drUnfoldD));
		$("#dr-menu-management .dr-user-unfold .dr-menu-management-item").html(drUserUnfoldT(drUserUnfoldD));
		$("#dr-menu-management .dr-tab-list .dr-menu-management-item").html(drTabListT(drTabListD));
	}
	var tabwrapperMenuState = false;
	$("#dr-tabwrapper-menu").click(function(){
		menuManage()
		if(tabwrapperMenuState){
			$("#dr-menu-management").animate({"right":"-300px"},300,function(){tabwrapperMenuState=false});
		}else{
			$("#dr-menu-management").animate({"right":0},300,function(){tabwrapperMenuState=true});
		}
		
	})
	
	$("#dr-menu-management").on("click","a",function(e){
		e.preventDefault();
		var shref = $(this).parents("li").attr("data-href");
		$("#fr-iframebox .show-iframe").each(function(index, element) {
			if($(this).data("href")==shref){
				$(this).show().siblings().hide();
				$("#dr-tabwrapper li").each(function(index, element) {
					if($(this).data("href")==shref){
						$(this).addClass("active").siblings().removeClass("active");
						return false;
					}
				});
				return false;
			}
		});
	})
	$("#dr-menu-management").on("click",".dr-close",function(e){
		e.preventDefault();
		var shref = $(this).parents("li").attr("data-href");
		var stitle = $(this).parents("li").attr("data-title");
		$(this).parents("li").remove();
		userLocalTabData(stitle,shref,0,1)
		$("#fr-iframebox .show-iframe").each(function(index, element) {
			if($(this).data("href")==shref){
				$(this).remove();
				$("#dr-tabwrapper li").each(function(index, element) {
					if($(this).data("href")==shref){
						$(this).remove();
						if($(this).hasClass("active")){
							$("#fr-iframebox .show-iframe:last-child").show().siblings().hide();
							$("#dr-tabwrapper ul li:last").addClass("active").siblings().removeClass("active");
						}
						
						return false;
					}
				});
				return false;
			}
		});
	})
	$("#dr-menu-management").on("click",".dr-lock",function(e){
		e.preventDefault();
		var shref = $(this).parents("li").attr("data-href");
		var stitle = $(this).parents("li").attr("data-title");
		$("#dr-menu-management .dr-user-unfold .dr-menu-management-item").append($(this).parents("li"));
		$("#dr-tabwrapper li").each(function(index, element) {
			if($(this).data("href")==shref){
				$(this).data("unfold",2)
				$(this).find(".dr-tabwrapper-close").hide();
				return false;
			}
		});
		userLocalTabData(stitle,shref,2)
		menuManage()
	})
	$("#dr-menu-management").on("click",".dr-unlock",function(e){
		e.preventDefault();
		var shref = $(this).parents("li").attr("data-href");
		var stitle = $(this).parents("li").attr("data-title");
		$("#dr-menu-management .dr-tab-list .dr-menu-management-item").append($(this).parents("li"));
		$("#dr-tabwrapper li").each(function(index, element) {
			if($(this).data("href")==shref){
				$(this).data("unfold",0)
				$(this).find(".dr-tabwrapper-close").show();
				return false;
			}
		});
		userLocalTabData(stitle,shref,0)
		menuManage()
	})
	
	var localUserfoldPage = JSON.parse(localStorage.getItem("userfoldPage"));
	
	if(_.isArray(localUserfoldPage)&&!_.isEmpty(localUserfoldPage)){
		userfoldPage = localUserfoldPage;
	}
	if(!_.isEmpty(userfoldPage)){
		unfoldPageManage(userfoldPage);
	}
	
	
	function userLocalTabData(title,href,unfold){
		if(unfold!=2){
			if(_.isEmpty(userfoldPage) ) { return}
			userfoldPage = _.reduce(userfoldPage,function(n,m){
				if(m[1]===href) return n;
				n.push(m);
				return n;
			},[])
		}else{
			if(!_.isArray(userfoldPage) || _.isEmpty(userfoldPage) ) {
				userfoldPage =[];
				userfoldPage.push([title,href,unfold]);
			}
			var userfoldPageState = true;
			userfoldPage = _.reduce(userfoldPage,function(n,m){
				if(m[1]===href) {
					n.push([m[0],m[1],unfold]);
					userfoldPageState = false;
				}else{
					n.push(m);
				}
				return n;
			},[])
			if(userfoldPageState){
				userfoldPage.push([title,href,unfold]);
			}
		}
		
		localStorage.setItem("userfoldPage", JSON.stringify(userfoldPage));
	}

	


	
}