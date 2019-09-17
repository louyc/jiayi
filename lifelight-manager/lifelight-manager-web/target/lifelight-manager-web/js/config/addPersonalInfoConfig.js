layui.use(['element', 'form', 'layer', 'jquery'], function() {
    var $ = layui.jquery,form = layui.form(),element = layui.element();
    var layer = layui.layer;
    var variable = window.utils.getCookie('template')
    var templateType = window.utils.getRequestParam("templateType")
    var templateId = window.utils.getRequestParam("templateId")
    window.utils.getServicePack();
    var JYDictionary = window.utils.JYDictionary;
    if(variable == "empty"){//新增
    	layer.msg("暂无数据")
    	variable ={variable:[]}
    }else{
    	variable = JSON.parse(variable)
    	loadding()
    }
    function loadding(){
    	var tableHtml = "";
    	var variableArr = variable.variable
    	for(var j =0;j<variableArr.length;j++){
    		tableHtml += "<tr>";
    		tableHtml += "<td>"+variableArr[j].propertyName+"</td>";
    		if(!!variableArr[j].signOrginName){
    			var orginName = getOrginList(variableArr[j].signOrginName)
    		}else{
    			var orginName = '';
    		}
    		tableHtml += "<td>"+orginName+"</td>";
    		tableHtml += "<td>"+getPropertyType(variableArr[j].propertyType)+"</td>";
    		tableHtml += "<td>"+variableArr[j].propertyContent+"</td>";
    		tableHtml += "<td><button class='layui-btn layui-btn-small delete-node'>删除</button></td>";
    		tableHtml += "</tr>";
    	}
    	$("#table_body").html(tableHtml)
    }
    //删除 某字段
    $("#table_body").delegate(".delete-node","click",function(){
        var index = $(this).parents("td").parents("tr").index();
        var variableArr = variable.variable;
        variableArr.splice(index,1)
        var obj = new Object();
    	obj.templateType = templateType;
    	obj.id = templateId;
    	obj.variable = JSON.stringify({"variable":variable.variable});
        $.ajax({
            url:'/template/update',
            type:'POST',
            data: $.toJSON(obj),
            dataType:'json',
            contentType: "application/json",
            success : function(data){
                if(data.status === 200 && data.resultCode.code === 'SUCCESS'){
                	layer.msg("删除成功")
                	loadding()
                }else{
                    layer.msg(data.resultCode.message);
                }
            },
            error:function(error){
                console.error(error)
                layer.msg('添加失败!');
            }
        });
    });
    /*新增字段*/
    $('.add-node').on('click',function(event){
    	
		layer.open({
            title:'新增字段',
            type: 1,
            area: ['650px', '400px'],//宽高
            btnAlign: 'c',
            shade: 0.5,
            content: $('#tipForm_add'),
            success: function(){
            	//内容 添加下拉
            	$("#reset").click();
            	$(".option").remove();
            	$("#addOptionBtn").hide();
            	for(var i =0;i < JYDictionary.length;i++){
            		$("#propertyContent").append("<option value='"+JYDictionary[i].itemType+"'>"+JYDictionary[i].itemDesc+"</option>");
            	}
            	getOrginList("")
            	form.render();
            }
        });
	});
    function getOrginList(orginId){
		 var obj = {"showCount":"1000","type":"4"}
		 var orginName = "";
		 $.ajax({
		 	url: '/jyOrgInfo/getJyOrgInfoList',
			type: 'post',
			async: false,
			data: $.toJSON(obj),
			dataType: 'json',
			contentType: 'application/json',
			success: function(res){
				if(res.successful){
					var items = res.items;
					if(!!items && items.length>0){
						var orgins='<option value="">请选择</option>';
						$.each(items,function(index,item){
							orgins +='<option value="'+item.managerId+'">'+item.name+'</option>'
							if(orginId == item.managerId){
								orginName = item.name
							}
					    })
					    if(!orginId){
					    	$("#signOrginName").html(orgins);
					    }
					}
				}
			},
			error : function(res){
				layer.msg("查询机构失败")
			}
		})
		if(orginId){
	    	return orginName;
	    }
   	}
    form.on('select(propertyType)', function(data){
    	  if(data.value == '1'||data.value == '2'||data.value == '3'){
    		  $("#addOptionBtn").show()
    	  }else{
    		  $("#addOptionBtn").hide()
    	  }
	});
    $("#addOptionBtn button").on("click",function(){
    	var html="";
    	var length = $(".option").length + 1;
    	html +='<div class="layui-form-item option">';
    	html +='<label class="layui-form-label">选项'+length+'</label>';
    	html +='<div class="layui-input-inline" style="width: 240px">';
    	html +='<input type="text" style="width: 180px;display: inline-block" autocomplete="off" class="layui-input"/>';
    	html +='<i class="layui-icon fontIcon">&#x1007;</i>';
    	html +='</div>';
    	html +='</div>';
    	$(html).insertBefore($("#btns"))
    	$(".fontIcon").on("click",function(){
    		$(this).parents(".option").remove()
    	})
    })
    form.on('submit(updateform)', function(data) {
    	var signOrginName = $("#signOrginName").val();
    	var propertyName = $("#propertyName").val();
    	var propertyType = $("#propertyType").val();
    	if(!signOrginName){
    		layer.msg("请选择机构",{zIndex: layer.zIndex});return;
    	}
    	if(!propertyName){
    		layer.msg("请输入字段名称",{zIndex: layer.zIndex});return;
    	}
    	if(!propertyType){
    		layer.msg("请选择类型",{zIndex: layer.zIndex});return;
    	}
    	var propertyContent = [];
    	if(propertyType == '0' ||propertyType == '4'||propertyType == '5'){
    		propertyContent = "";
    	}else{
    		$(".option").each(function(index){
    			var optionText = $(this).find("input")[0].value
    			if(optionText){
    				propertyContent.push(optionText)
    			}
    		})
    	}
    	var variableObj = {'signOrginName':signOrginName,'propertyName':propertyName,'propertyType':propertyType,'propertyContent':propertyContent};
    	variable.variable.push(variableObj)
    	var obj = new Object();
    	obj.templateType = templateType;
    	obj.id = templateId;
    	obj.variable = JSON.stringify({"variable":variable.variable});
        $.ajax({
            url:'/template/update',
            type:'POST',
            data: $.toJSON(obj),
            dataType:'json',
            contentType: "application/json",
            success : function(data){
                if(data.status === 200 && data.resultCode.code === 'SUCCESS'){
                	layer.close(layer.index);
                	layer.msg("添加成功")
                	loadding()
                }else{
                    layer.msg(data.resultCode.message);
                }
            },
            error:function(error){
                layer.closeAll();
                console.error(error)
                layer.msg('添加失败!');
            }
        });
        return false;
    })
    //获取类型
    function getPropertyType(type){
    	var propertyType = "";
    	switch(type){
    		case '0':
    			propertyType = '输入框'
    			break;
    		case '1':
    			propertyType = '下拉选择'
    				break;
    		case '2':
    			propertyType = '单选'
    				break;
    		case '3':
    			propertyType = '多选'
    				break;
    		case '4':
    			propertyType = '文本'
    				break;
    		case '5':
    			propertyType = '服务包'
    				break;
    	}
    	return propertyType
    }
    $("#goBack").on("click",function(){
		 history.back(-1)
	})
    //获取内容
    function getPropertyContent(type){
    	var propertyContent = "";
    	for(var i =0;i < JYDictionary.length;i++){
    		if(type == JYDictionary[i].itemType){
    			propertyContent = JYDictionary[i].itemDesc
    		}
    	} 
    	return propertyContent
    }
});