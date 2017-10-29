function uploaderExt(opt) {
	var index = 0;
	var $pick = $('<div id="' + opt.pick.substring(1) + '-btn">开始上传</div>');
	var $list = $('<div class="mk-uploader-list"></div>');
	var $input = $('<input type="hidden" name="' + opt.inputName + '" />');
	$(opt.pick).empty().append($list).append($pick).append($input);
	
	opt.pick = opt.pick + '-btn' ;
	var defaultOpt = {
		// 选完文件后，是否自动上传。
		auto : true,

		// swf文件路径
		swf : ctx + '/static/jslib/webuploader/Uploader.swf',

		// 文件接收服务端。
		server : ctx + '/fileentity/upload',

		fileNumLimit : 1,

		// 只允许选择图片文件。
		accept : {
			title : 'Images',
			extensions : 'gif,jpg,jpeg,bmp,png',
			mimeTypes : 'image/jpg,image/jpeg,image/png,image/bmp'
		}
	};
	
	$.extend(defaultOpt, opt);

	var uploader = WebUploader.create(defaultOpt);
	
	// 当有文件添加进来的时候
	uploader.on( 'fileQueued', function( file ) {
	    var $li = $('<div id="'  + file.id + '" class="file-item thumbnail">' + '<img>' + 
//	    		'<div class="info">' + file.name + '</div>' +
	    		'<div class="file-panel"><span class="cancel" title="删除">删除</span></div>' + 
	    		'<span class="mkfile-info"></span></div>'),
	        $img = $li.find('img'),
	        // 优化retina, 在retina下这个值是2
	        ratio = window.devicePixelRatio || 1,

	        // 缩略图大小
	        thumbnailWidth = 100 * ratio,
	        thumbnailHeight = 100 * ratio;

	    $li.find('span.cancel').click(function(e){
	    	uploader.removeFile(file);
	    	$list.find('#' + file.id).remove();
	    	$input.val('');
	    });
	    // $list为容器jQuery实例
	    $list.append( $li );

	    // 创建缩略图
	    // 如果为非图片文件，可以不用调用此方法。
	    // thumbnailWidth x thumbnailHeight 为 100 x 100
	    uploader.makeThumb( file, function( error, src ) {
	        if ( error ) {
	            $img.replaceWith('<span>不能预览</span>');
	            return;
	        }

	        $img.attr( 'src', src );
	    }, thumbnailWidth, thumbnailHeight );
	});

	//上传完成后
	uploader.on('uploadSuccess', function( file, response ) {
		if(response.success){
			$input.val(response.obj.url)
			$list.find('#' + file.id).find('.mkfile-info').addClass('success');
		} else {
			$list.find('#' + file.id).find('.mkfile-info').addClass('error');
		}
	});

	//上传失败后
	uploader.on('uploadError', function( file, response ) {
		$list.find('#' + file.id).find('.mkfile-info').addClass('error').text('上传失败!');
	});


	
	uploader.addFile = function(url){
		var $li = $('<div id="edit_pic_'  + index++ + '" class="file-item thumbnail">' + '<img>' + 
		    		'<div class="file-panel"><span class="cancel" title="删除">删除</span></div>' + 
		    		'<span class="mkfile-info"></span></div>'),
    		$img = $li.find('img').attr('src',url);
		$list.append( $li );
		$li.find('span.cancel').click(function(e){
			$(e.target).closest('div[id^="edit_pic_"]').remove();
	    	$input.val('');
	    });
		$input.val(url)
	};
	
	uploader.deleteFile = function(){
		
	};
	
	return uploader;
}

