layui.use(['element', 'form', 'layer', 'jquery','laypage'], function() {
	var $ = layui.jquery,form = layui.form(),element = layui.element();
	var layer = layui.layer,laypage = layui.laypage;
	var updateId = '';
	var populate = {pageSize:10,currentPage:1,totleCount:10,pageCount:2}
	var wxId =""
	
	// 加载
	function loading(val,populate){
		layer.open({type: 3,content: ""});
		var inUse = $("#inUse").val() == "all"? "" : $("#inUse").val();
		var obj = new Object();
		obj.name = $("#domainName").val();
		obj.inUse = inUse;
		obj.showCount = populate.pageSize;
		obj.currentsPage = populate.currentPage;
		$.ajax({
            url:'/weixinConfigure/getPageWinxinConfigure',
            type:'POST',
            data: $.toJSON(obj),
            dataType:'json',
            contentType: "application/json",
            success : function(data){
            	layer.close(layer.index);//取消遮罩
            	if(data.successful && (data.status === 200) && (data.resultCode.code === 'SUCCESS')){
                    var list = data.items,html = '';
                    if(!list || list.length==0){
                    	layer.msg("暂无数据")
                    	return false;
                    }
                    for(var i=0;i<list.length;i++){
                    	var item = list[i];
                    	html += '<tr>';
                    	var weixinName = !!item.weixinName?item.weixinName:"";
                        html += '<td>'+weixinName+'</td>';
                        html += '<td>'+item.domainName+'</td>';
                        html += '<td>'+item.appid+'</td>';
                        html += '<td>'+item.secret+'</td>';
                        html += '<td>'+item.paysignKey+'</td>';
                        html += '<td>'+item.mchid+'</td>';
                        html += '<td>'+item.host+'</td>';
                        html += '<td>'+item.filePath+'</td>';
                        html += '<td>'+item.fileHost+'</td>';
                        /*html+='<td><input type="checkbox" lay-skin="switch" data-id="'+item.id+'" lay-filter="ifcanuse" lay-text="是|否"';
            			if(item.inUse == "0"){
            				html+='checked';
                		}
            			html+='></td>';*/
                        html += '<td>';
                        var objString = JSON.stringify(item)
                        html += "<button class='layui-btn layui-btn-small edit-node' custom='"+objString+"'>修 改</button>";
                        html += '<button class="layui-btn layui-btn-small menu-node" customId="'+item.id+'">菜单配置</button>';
                        html += '<button class="layui-btn layui-btn-small stencil-node" customId="'+item.id+'">模版</button>';
                        html += '</td>';
                        html += '</tr>';
                    }
                    // 分页
                	laypage({
                	    cont: 'pageTag'
                	    ,pages: data.pagesCount
                	    ,groups: 5 //连续显示分页数
                	    ,curr:data.pageNumber || 1
                	    ,jump: function(obj, first){
                	    	if(!first){
                	    		//得到了当前页，用于向服务端请求对应数据
                    		    populate.currentPage = obj.curr;
                    		    loading("",populate);
                	    	}
                		}
                	});
                    html && $('#tagsBody').html(html);
                    form.render()
                    populate.currentPage = data.pageNumber*1;//当前页数
                    populate.totleCount = data.totalItemsCount*1;//总数
                    populate.pageCount = data.pagesCount*1; //总页数
                    bind();
                }else{
                    layer.msg(data.resultCode.message);
                }
            },
            error:function(error){
            	layer.close(layer.index);//取消遮罩
                layer.msg('查询失败!');
            }
        });
	}
	loading('',populate);
	// update
	form.on('submit(updateform)', function(data) {
        var data = data.field;
        var url = "";
        var obj = new Object();
        if(!updateId){//新增
        	url = '/weixinConfigure/createWeixinConfigure'
        }else{//修改
        	obj.id=updateId
        	url = '/weixinConfigure/updateWeixinConfigure'
        }
        obj.weixinName = $("#weixinName").val()
        obj.domainName = $("#domainName_add").val()
        obj.appid = $("#appid").val()
        obj.secret = $("#secret").val()
        obj.paysignKey = $("#paysignKey").val()
        obj.mchid = $("#mchid").val()
        obj.host = $("#host").val()
        obj.filePath = $("#filePath").val()
        obj.fileHost = $("#fileHost").val()
        $.ajax({
            url:url,
            type:'POST',
            data:$.toJSON(obj),
            dataType:'json',
            contentType:'application/json',
            success:function(data){
                if(data.status === 200 && data.resultCode.code === 'SUCCESS'){
                    layer.close(layer.index);
                    layer.msg('保存成功!');
                    loading('',populate);
                    $('button[type="reset"]').click();
                }else{
                    layer.msg(data.resultCode.message);
                }
            },
            error:function(error){
                console.error(error);
                layer.closeAll();
                layer.msg('修改失败!');
            }
        });
        updateId = '';
        return false;
    });
	// 弹出add层
	$('.add-node').on('click',function(event){
		layer.open({
            title:'新增商品标签',
            type: 1,
            area: ['650px', '600px'],//宽高
            btnAlign: 'c',
            shade: 0.5,
            content: $('#form_update') 
        });
	});
	// 搜索
	$('#search-query').on('click',function(e){
		populate.currentPage = 1;
		loading("",populate);
		layer.closeAll();
	});
	function bind(){
		// 弹出update层
		$('.edit-node').on('click',function(event){
			layer.open({
	            title:'微信信息',
	            type: 1,
	            area: ['650px', '650px'],//宽高
	            btnAlign: 'c',
	            shade: 0.5,
	            content: $('#form_update') 
	        });
			var custom = JSON.parse($(this).attr('custom'));
			updateId = custom.id;
			$('#form_update').find('[name="weixinName"]').val(custom.weixinName);
			$('#form_update').find('[name="domainName_add"]').val(custom.domainName);
			$('#form_update').find('[name="appid"]').val(custom.appid);
			$('#form_update').find('[name="secret"]').val(custom.secret);
			$('#form_update').find('[name="paysignKey"]').val(custom.paysignKey);
			$('#form_update').find('[name="mchid"]').val(custom.mchid);
			$('#form_update').find('[name="host"]').val(custom.host);
			$('#form_update').find('[name="filePath"]').val(custom.filePath);
			$('#form_update').find('[name="fileHost"]').val(custom.fileHost);
		});
		$('.menu-node').on('click',function(event){
			var wxId = $(this).attr('customId')
			layer.open({
	            type: 2 //2为弹出iframe
	            ,title: '微信菜单'
	            ,area: ['100%', '100%']
	    		,shade: 0.5
	            ,content: 'wxMenuController.html?wxId='+wxId
	            ,btn: ['关闭'] //按钮
		    	,zIndex: layer.zIndex //重点1
		        ,success: function(layero){
		          layer.setTop(layero); //重点2
		        }
	    	});
		});
		function mobanLoading(){
			var obj = {weixinId:wxId}
			$.ajax({
                  url:'/weixinConfigure/getPageWinxinTemplate',
                  type:'POST',
                  data:$.toJSON(obj),
                  dataType:"json",
                  contentType: "application/json",
                  success : function(data){
                	  var item = data.items;
                	  if(item && item.length > 0){
                		  var html = "";
                		  for(var i =0;i<item.length;i++){
                			  if(item[i].inUse == "0"){
                				  continue
                			  }
	                		  html +="<tr>";
	                		  html +="<td><input type='text' autocomplete='off' value='"+item[i].templateId+"' class='layui-input moban_id'/></td>";
	                		  html +="<td><input type='text' autocomplete='off' value='"+item[i].templateDesc+"' class='layui-input moban_desc'/></td>";
	                		  html +="<td><button data-id='"+item[i].id+"'  class='layui-btn layui-btn-small moban_save'>保存</button>";
	                		  html +="<button data-id='"+item[i].id+"'  class='layui-btn layui-btn-small moban_delete'>删除</button>";
	                		  html +="</td>";
	                		  html +="</tr>"; 
                		  }
                		  $("#mobanBody").html(html)
                		  moban_bind()
                	  }else{
                		  layer.msg("暂无数据")
                	  }
                  },
                  error : function(data){
                	  layer.close(layer.index);//取消遮罩
                  	  layer.msg('失败！');
                  },
             });
		}
		$('.stencil-node').on('click',function(event){
			wxId = $(this).attr('customId')
			layer.open({
				type: 1 //2为弹出iframe
				,title: '摸版'
				,area: ['750px', '400px']
				,shade: 0.5
				,content: $('#form_stencil') 
				,success: function(layero){
					mobanLoading()
				}
			});
		});
		form.on('switch(ifcanuse)', function(data){
    		layer.open({type: 3,content: ""});
  		    var ifcanuse = data.elem.checked; //开关是否开启，“是”是true，“否”是false
  		    var id = data.elem.getAttribute("data-id");
  		    var inUse = "";
  		    if(ifcanuse){
  		    	inUse = "0"
  		    }else{
  		    	inUse = "1"
  		    }
      		$.ajax({
                  url:'/weixinConfigure/deleteWeixinConfigure?id='+id+'&inUse='+inUse,
                  type:'POST',
                  dataType:"json",
                  contentType: "application/json",
                  success : function(data){
                	  layer.close(layer.index);//取消遮罩
                	  if(ifcanuse){
                		  layer.msg(data.resultCode.message);
                	  }else{
                		  layer.msg("取消删除成功");
                	  }
                  },
                  error : function(data){
                	  layer.close(layer.index);//取消遮罩
                  	  layer.msg('失败！');
                  },
             });
  		}); 
	}
	/*新增模版*/
	$("#addMoban").on("click",function(){
		var html = "";
		html +="<tr>";
		html +="<td><input type='text' autocomplete='off' class='layui-input moban_id'/></td>";
		html +="<td><input type='text' autocomplete='off' class='layui-input moban_desc'/></td>";
		html +="<td><button class='layui-btn layui-btn-small moban_save'>保存</button>";
		html +="<button class='layui-btn layui-btn-small moban_delete'>删除</button>";
		html +="</td>";
		html +="</tr>";
		if($("#mobanBody tr").length < 2){
			$("#mobanBody").append(html)
		}else{
			layer.msg("最多添加两个模版")
		}
		moban_bind()
	});
	function moban_bind(){
		$(".moban_save").on("click",function(){
			var url ="";
			var mobanId = $(this).attr("data-id");
			var templateId = $(this).parent("td").prev("td").prev("td").children("input").val();
			var templateDesc = $(this).parent("td").prev("td").children("input").val();
			if(!mobanId){//新增
				url = '/weixinConfigure/addWinxinTemplate'
				var obj = {weixinId: wxId,templateId: templateId,templateDesc: templateDesc}
			}else{//修改
				url = '/weixinConfigure/updateWinxinTemplate'
				var obj = {id: mobanId,weixinId: wxId,templateId: templateId,templateDesc: templateDesc}
			}
			$.ajax({
                url:url,
                type:'POST',
                data: $.toJSON(obj),
                dataType:"json",
                contentType: "application/json",
                success : function(data){
              	  if(data.successfu && data.resultCode == "200"){
              		  mobanLoading()
              	  }else{
              		  layer.msg("保存成功");
              	  }
                },
                error : function(data){
                	  layer.msg('保存失败！');
                },
           });
		})
		$(".moban_delete").on("click",function(){
			var mobanId = $(this).attr("data-id");
			if(!mobanId){//新增
				$(this).parent("td").parent("tr").remove()
			}else{
				$.ajax({
	                url:'/weixinConfigure/delWinxinTemplate?id='+mobanId,
	                type:'get',
	                success : function(data){
	              	    if(data.successful && data.resultCode.code == "SUCCESS"){
	              		    layer.msg(data.resultCode.message);
	              		    mobanLoading()
	              	    }else{
	              		    layer.msg("删除失败");
	              	    }
	                },
	                error : function(data){
                	    layer.msg('删除失败！');
	                },
	           });
			}
		})
	}
});