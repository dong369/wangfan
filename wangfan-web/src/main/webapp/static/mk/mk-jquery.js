var mk = mk || {}

/**
 * 表单载入数量
 * 如果数据是个string类型,则加载远程站点的数据, 
 * 如果不是,则把本地数据加载到表单中
 * @author 马凯
 * @Date 2016-10-25
 * @Version 1.0
 */
mk.load = function(target, data, func){
	if (typeof data == 'string'){
		$.ajax({
			url: data,
			dataType: 'json',
			success: function(data){
				_load(data, func);
			}
		});
	} else {
		_load(data, func);
	}
	
	function _load(data, func){
		var form = target;
		for(var name in data){
			var val = data[name];
			var kk = $('input[name='+name+']', form);
			if(kk.size() > 0 && $.inArray(kk.prop('type'), ['radio']) >=0){
				$('input[name='+name+'][value="' + val + '"]', form).prop('checked', true);
				continue;
			}
			$('input[name='+name+']', form).val(val);
			$('textarea[name='+name+']', form).val(val);
			$('select[name='+name+']', form).val(val);
			//把bootstrap selectpicker也load进去。
			$('select[name='+name+']', form).each(function(i, n){
				if(n.ownerDocument.defaultView.$.fn.selectpicker != undefined && n.ownerDocument.defaultView.$(n).selectpicker != undefined){
					n.ownerDocument.defaultView.$(n).selectpicker('refresh');
				}
			});
		}
		if(func != undefined){
			func(data);
		}
	}
}