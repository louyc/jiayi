layui.use(['element', 'form', 'layer', 'jquery','laypage'], function() {
    var $ = layui.jquery,form = layui.form(),element = layui.element();
    var layer = layui.layer,laypage = layui.laypage;
    var checkLists = [];
  //查询所有检查项
    $.ajax({
        url:'/api/contracte/queryDictionary?typeId=42&itemName=&pageSize=1000&currentsPage=1',
        type:'GET',
        success : function(data){
            if(data.status === 200 && data.successful){
                var list = data.items,html = '<option value="">选择或搜索选择检查项</option>',num = 0;
                checkLists = data.items;
                while(list && (list.length > num)){
                    var item = list[num];
                    html += '<option value="'+item.id+'">'+item.itemName+'</option>';
                    num++;
                }
                html && $('#checkListSelect').html(html);
                form.render()
            }else{
                layer.msg(data.resultCode.message);
            }
        },
        error:function(error){
            layer.closeAll();
            layer.msg('查询失败!');
        }
    });
    // 加载
    function loading(currentsPage){
    	var itemName = $("#itemName").val();
        $.ajax({
            url:'/package/getAllServicePackage?pageSize=10&currentsPage='+currentsPage+'&packageName='+itemName,
            type:'GET',
            success : function(data){
                if(data.status === 200 && data.successful){
                    var list = data.items,html = '',num = 0;
                    while(list && (list.length > num)){
                        var item = list[num];
                        html += '<tr>';
                        html += '<td>'+item.packageName+'</td>';
                        html += '<td>'+item.price+'</td>';
                        html += '<td>'+item.selfPayment+'</td>';
                        html += '<td>'+item.compensatePayment+'</td>';
                        var checkList = "";
                        var dicIds = "";
                        for(var i = 0;i < item.listDictionary.length;i++){
                        	if(checkList){
                        		checkList+=","
                        	}
                        	checkList+=item.listDictionary[i].itemName
                        	if(dicIds){
                        		dicIds+=","
                        	}
                        	dicIds+=item.listDictionary[i].id
                        }
                        html += '<td>'+checkList+'</td>';
                        var packageDesc = !item.packageDesc?"":item.packageDesc;
                        html += '<td>'+packageDesc+'</td>';
                        html += '<td>';
                        html += '<button class="layui-btn layui-btn-small edit-node" data-id="'+item.id+'" data-packageName="'+item.packageName+'" data-price="'+item.price+'" data-selfPayment="'+item.selfPayment+'" data-compensatePayment="'+item.compensatePayment+'" data-dicIds="'+dicIds+'" data-checkList="'+checkList+'" data-packageDesc="'+item.packageDesc+'">修改</button>';
                        html += '<button class="layui-btn layui-btn-small layui-btn-danger delete-node" data-id="'+item.id+'">删除</button>';
                        html += '</td>';	
                        html += '</tr>';
                        num++;
                    }
                    html && $('#table_body').html(html);
                    bind();
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
    $("#addCheckBtn").on("click",function(){
    	var checkId = $("#checkListSelect").val()
    	var checkText = $("#checkListSelect option:selected").text()
    	var html="";
    	html +='<span class="checks" data-id="'+checkId+'">'+checkText+'<i class="layui-icon fontIcon">&#x1007;</i></span>';
    	$("#checkLists .layui-input-block").append(html)
    	$(".fontIcon").on("click",function(){
    		$(this).parents("span").remove()
    	})
    })
    // 新增节点
    $('#add-node').on('click',function(e){
    	layer.open({
            type: 1 
            ,title: '服务包信息'
            ,area: ['850px', '600px']
    		,shade: 0.5
            ,content: $('#tipForm_edit')
            ,btn: ['确认','关闭'] //按钮
	        ,success: function(layero){
	        	$("#packageName").val("");
	        	$("#price").val("");
	        	$("#selfPayment").val("");
	        	$("#compensatePayment").val("");
	        	$("#packageDesc").val("");
	        	$("#checkLists span.checks").remove();
	            layer.setTop(layero); //重点2
	        }
    		,yes: function(index, layero){
	            var packageName = $("#packageName").val();
	            if(!packageName){
	            	layer.msg("请输入服务包名称",{zIndex: layer.zIndex});return;
	            }
	            var price = $("#price").val();
	            if(!price){
	            	layer.msg("请输入服务包签约金额",{zIndex: layer.zIndex});return;
	            }
	            var selfPayment = $("#selfPayment").val();
	            if(!selfPayment){
	            	layer.msg("请输入服务包自付金额",{zIndex: layer.zIndex});return;
	            }
	            var compensatePayment = $("#compensatePayment").val();
	            if(!compensatePayment){
	            	layer.msg("请输入服务包合作社补偿金额",{zIndex: layer.zIndex});return;
	            }
	            var dicIds = "";
	            $("#checkLists span.checks").each(function(){
            		if(dicIds){
            			dicIds+=","
            		}
            		dicIds += $(this).attr("data-id")
	            })
	            if(!dicIds){
	            	layer.msg("请选择检查项",{zIndex: layer.zIndex});return;
	            }
	            var packageDesc = $("#packageDesc").val();
            	var obj = new Object();
            	obj.packageName = packageName;
            	obj.price = price	;
            	obj.selfPayment = selfPayment;
            	obj.compensatePayment = compensatePayment;
            	obj.dicIds = dicIds;
            	obj.packageDesc = packageDesc;
            	$.ajax({
                    url:'/package/createServicePackage',
                    type:'POST',
                    data: $.toJSON(obj),
                    dataType:'json',
                    contentType: "application/json",
                    success : function(data){
                    	layer.msg("添加成功");
                    	layer.close(index);
                    	$("#packageName").val("")
                    	$("#price").val("")
                    	$("#selfPayment").val("")
                    	$("#compensatePayment").val("")
                    	$("#packageDesc").val("")
                    	$("#checkList .layui-form-checkbox").removeClass("layui-form-checked")
                    	loading(1);
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
        	var id = $(this).attr("data-id");
        	var packageName = $(this).attr("data-packageName");
        	var price = $(this).attr("data-price");
        	var selfPayment = $(this).attr("data-selfPayment");
        	var compensatePayment = $(this).attr("data-compensatePayment");
        	var dicIds = $(this).attr("data-dicIds").split(",");
        	var checklist = $(this).attr("data-checklist").split(",");
        	var packageDesc = $(this).attr("data-packageDesc");
        	$("#packageName").val(packageName);
        	$("#price").val(price);
        	$("#selfPayment").val(selfPayment);
        	$("#compensatePayment").val(compensatePayment);
        	$("#packageDesc").val(packageDesc);
        	var html = ""
        	for(var i = 0; i < dicIds.length;i++){
        		html +='<span class="checks" data-id="'+dicIds[i]+'">'+checklist[i]+'<i class="layui-icon fontIcon">&#x1007;</i></span>'
        	}
    		$("#checkLists .layui-input-block").html(html)
    		$(".fontIcon").on("click",function(){
	    		$(this).parents("span").remove()
	    	})
            form.render()
        	layer.open({
                type: 1 
                ,title: '服务包信息'
                ,area: ['850px', '600px']
        		,shade: 0.5
                ,content: $('#tipForm_edit')
                ,btn: ['确认','关闭'] //按钮
    	    	,zIndex: layer.zIndex //重点1
    	        ,success: function(layero){
    	          layer.setTop(layero); //重点2
    	        }
        		,yes: function(index, layero){
        			var packageName = $("#packageName").val();
    	            if(!packageName){
    	            	layer.msg("请输入服务包名称",{zIndex: layer.zIndex});return;
    	            }
    	            var price = $("#price").val();
    	            if(!price){
    	            	layer.msg("请输入服务包签约金额",{zIndex: layer.zIndex});return;
    	            }
    	            var selfPayment = $("#selfPayment").val();
    	            if(!selfPayment){
    	            	layer.msg("请输入服务包自付金额",{zIndex: layer.zIndex});return;
    	            }
    	            var compensatePayment = $("#compensatePayment").val();
    	            if(!compensatePayment){
    	            	layer.msg("请输入服务包合作社补偿金额",{zIndex: layer.zIndex});return;
    	            }
    	            var dicIds = "";
    	            $("#checkLists span.checks").each(function(){
	            		if(dicIds){
	            			dicIds+=","
	            		}
	            		dicIds += $(this).attr("data-id")
    	            })
    	            if(!dicIds){
    	            	layer.msg("请选择检查项",{zIndex: layer.zIndex});return;
    	            }
                	var obj = new Object();
                	obj.packageName = packageName;
                	obj.price = price;
                	obj.selfPayment = selfPayment;
                	obj.compensatePayment = compensatePayment;
                	obj.packageDesc = $("#packageDesc").val();
                	obj.dicIds = dicIds;
                	obj.id = id;
                	$.ajax({
                        url:'/package/updateServicePackage',
                        type:'POST',
                        data: $.toJSON(obj),
                        dataType:'json',
                        contentType: "application/json",
                        success : function(data){
                        	if(data.resultCode.code=="SUCCESS"){
                        		layer.msg("修改成功");
                            	layer.close(index);
                            	$("#packageName").val("")
                            	$("#price").val("")
                            	$("#selfPayment").val("")
                            	$("#compensatePayment").val("")
                            	$("#packageDesc").val("")
                            	$("#checkList .layui-form-checkbox").removeClass("layui-form-checked")
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
    		layer.confirm('确定删除此服务包吗?', {title:'提示'}, function(index){
    			$.ajax({
                    url:'/package/deleteServicePackage?id='+id,
                    type:'get',
                    success : function(data){
                    	layer.msg("删除成功");
                    	loading(1);
                    },
                    error:function(error){
                        layer.msg('删除失败!');
                    }
                });
			});
		});
    };
});