layui.use(['element', 'form', 'layer', 'jquery','laypage'], function() {
    var $ = layui.jquery,form = layui.form(),element = layui.element();
    var layer = layui.layer,laypage = layui.laypage;
    // 加载
    function loading(currentsPage){
    	var itemName = $("#itemName").val();
        $.ajax({
            url:'/api/contracte/queryDictionary?typeId=42&&pageSize=10&currentsPage='+currentsPage+'&itemName='+itemName,
            type:'GET',
            success : function(data){
                if(data.status === 200 && data.successful){
                    var list = data.items,html = '',num = 0;
                    while(list && (list.length > num)){
                        var item = list[num];
                        html += '<tr>';
                        var itemName = !!item.itemName?item.itemName:'';
                        html += '<td>'+itemName+'</td>';
                        html += '<td>';
                        html += '<button class="layui-btn layui-btn-small edit-node" data-id="'+item.id+'" data-name="'+item.itemName+'">修改</button>';
                        html += '<button class="layui-btn layui-btn-small delete-node" data-id="'+item.id+'">删除</button>';
                        html += '</td>';	
                        html += '</tr>';
                        num++;
                    }
                    html && $('#table_body').html(html);
                    laypage({
                  	    cont: 'pagging'
                  	    ,pages: data.pagesCount
                  	    ,groups: 5 
                  	    ,curr:data.pageNumber || 1
                  	    ,jump: function(obj, first){
                  	    	if(!first){
                  	    		var currentsPage = obj.curr;
                  	    		loading(currentsPage);
                  	    	}
                 		}
                  	});
                    bind();
                }else{
                    console.error(data);
                    layer.msg(data.resultCode.message);
                }
            },
            error:function(error){
                layer.closeAll();
                console.error(error)
                layer.msg('查询失败!');
            }
        });
    };

    loading(1);
    $('#search').on('click',function(e){
    	loading(1);
    })
    // 新增节点
    $('#add-node').on('click',function(e){
    	layer.open({
            type: 1 
            ,title: '检查项信息'
            ,area: ['350px', '220px']
    		,shade: 0.5
            ,content: $('#tipForm_edit')
            ,btn: ['确认','关闭'] //按钮
	    	,zIndex: layer.zIndex //重点1
	        ,success: function(layero){
	          layer.setTop(layero); //重点2
	        }
    		,yes: function(index, layero){
	            var itemName = $("#add_itemName").val();
	            if(!itemName){
	            	layer.msg("请输入检查项名称");return;
	            }
            	var obj = new Object();
            	obj.itemName = itemName;
            	obj.itemType = "42";
            	$.ajax({
                    url:'/api/contracte/addDictionary',
                    type:'POST',
                    data: $.toJSON(obj),
                    dataType:'json',
                    contentType: "application/json",
                    success : function(data){
                    	if(data.resultCode.code=="SUCCESS"){
                    		layer.msg("添加成功");
                        	layer.close(index);
                        	$("#add_itemName").val("")
                        	loading(1);
                    	}else{
                    		layer.msg(data.resultCode.message);
                    	}
                    	
                    },
                    error:function(error){
                        layer.msg('添加失败!');
                    }
                });
	        }
    	});
    });
    // 绑定事件
    function bind(){
        // 修改节点
        $('.edit-node').on('click',function(){
        	var data_name = $(this).attr("data-name");
        	var id = $(this).attr("data-id");
        	$("#add_itemName").val(data_name);
        	layer.open({
                type: 1 
                ,title: '检查项信息'
                ,area: ['350px', '220px']
        		,shade: 0.5
                ,content: $('#tipForm_edit')
                ,btn: ['确认','关闭'] //按钮
    	        ,success: function(layero){
    	          layer.setTop(layero); //重点2
    	        }
        		,yes: function(index, layero){
    	            var itemName = $("#add_itemName").val();
    	            if(!itemName){
    	            	layer.msg("请输入检查项名称",{zIndex: layer.zIndex});return;
    	            }
                	var obj = new Object();
                	obj.itemName = itemName;
                	obj.id = id;
                	$.ajax({
                        url:'/api/contracte/updateDictionary',
                        type:'POST',
                        data: $.toJSON(obj),
                        dataType:'json',
                        contentType: "application/json",
                        success : function(data){
                        	if(data.resultCode.code=="SUCCESS"){
                        		layer.msg("修改成功");
                            	layer.close(index);
                            	$("#add_itemName").val("")
                            	loading(1);
                        	}else{
                        		layer.msg(data.resultCode.message);
                        	}
                        },
                        error:function(error){
                            layer.msg('修改失败!');
                        }
                    });
    	        }
        	});
        });
    	// 删除节点
    	$('.delete-node').on('click',function(){
    		var id = $(this).attr("data-id")
    		layer.confirm('确定删除此检查项吗?', {title:'提示'}, function(index){
    			$.ajax({
                    url:'/api/contracte/delDictionary?id='+id,
                    type:'get',
                    success : function(data){
                    	if(data.resultCode.code=="SUCCESS"){
                    		layer.msg("删除成功");
                        	loading(1);
                        	layer.close(index);
                    	}else{
                    		layer.msg(data.resultCode.message);
                    	}
                    },
                    error:function(error){
                        layer.msg('删除失败!');
                    }
                });
			});
		});
    };
});