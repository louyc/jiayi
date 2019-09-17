layui.use(['element', 'form', 'layer', 'jquery'], function() {
    var $ = layui.jquery,form = layui.form(),element = layui.element();
    var layer = layui.layer;
    var templateList = "";
    // 加载
    function loading(){
    	var obj = new Object();
        $.ajax({
            url:'/template/getAll',
            type:'POST',
            data: $.toJSON(obj),
            dataType:'json',
            contentType: "application/json",
            success : function(data){
                if(data.status === 200 && data.resultCode.code === 'SUCCESS'){
                    var list = data.data.templateList,html = '',num = 0;
                    templateList = list;
                    while(list && (list.length > num)){
                        var item = list[num];
                        html += '<tr>';
                        var template = item.templateType == '1'?'基本信息':'签约信息';
                        html += '<td>'+template+'</td>';
                        html += '<td>';
                        var variable = !!item.variable?item.variable:"";
                        html += '<button class="layui-btn layui-btn-small edit-node" templateId="'+item.id+'" templateType="'+item.templateType+'">修改</button>';
                        if(item.templateType == "1"){
                        	html += '<a href="../familyDoctors/addFamilyDoctors.html" class="layui-btn layui-btn-small">查看</button>';
                        }else{
                        	html += '<a href="../familyDoctors/serviceSign.html?documentId=&type=login" class="layui-btn layui-btn-small">查看</button>';
                        }
                        html += '</td>';	
                        html += '</tr>';
                        num++;
                    }
                    html && $('#table_body').html(html);
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

    loading();

    // 新增节点
    $('.add-node').on('click',function(e){
    	layer.open({
            type: 1 
            ,title: '请选择模版类型'
            ,area: ['350px', '250px']
    		,shade: 0.5
            ,content: $('#tipForm_edit')
            ,btn: ['确认','关闭'] //按钮
	    	,zIndex: layer.zIndex //重点1
	        ,success: function(layero){
	          layer.setTop(layero); //重点2
	        }
    		,yes: function(index, layero){
	            var templateType = $("#templateType").val();
	            var isExit = false;
	            $("#table_body tr td .edit-node").each(function(){
	            	if(templateType == $(this).attr("templateType")){
	            		isExit = true
	            	}
	            })
	            if(isExit){
	            	layer.msg("已存在此模版，无需重复添加",{zIndex:layer.zIndex})
	            }else{
	            	var obj = new Object();
	            	obj.templateType = templateType;
	            	obj.variable = "";
	            	$.ajax({
	                    url:'/template/create',
	                    type:'POST',
	                    data: $.toJSON(obj),
	                    dataType:'json',
	                    contentType: "application/json",
	                    success : function(data){
	                    	if(data.resultCode.code=="SUCCESS"){
	                    		layer.msg("添加成功");
		                    	layer.close(index);
		                    	loading();
	                    	}else{
	                    		layer.msg(data.resultCode.message);
	                    	}
	                    	
	                    },
	                    error:function(error){
	                        layer.msg('添加失败!');
	                    }
	                });
	            }
	        }
    	});
    });
    // 绑定事件
    function bind(){
        // 修改节点
        $('.edit-node').on('click',function(){
        	var index = $(this).parents("td").parents("tr").index();
        	var variable = templateList[index].variable?templateList[index].variable:'empty'
        	var templateType = $(this).attr('templateType')
        	var templateId = $(this).attr('templateId')
        	window.utils.setCookie('template',variable)
        	window.location.href = 'addPersonalInfoConfig.html?templateType='+templateType+'&templateId='+templateId
			/*layer.open({
	            type: 2 //2为弹出iframe
	            ,title: '模版字段管理'
	            ,area: ['100%', '100%']
	    		,shade: 0.5
	            ,content: 'addPersonalInfoConfig.html?templateType='+templateType+'&templateId='+templateId
	            ,btn: ['关闭'] //按钮
		    	,zIndex: layer.zIndex //重点1
		        ,success: function(layero){
		          layer.setTop(layero); //重点2
		        }
	    	});*/
        });
    };
});