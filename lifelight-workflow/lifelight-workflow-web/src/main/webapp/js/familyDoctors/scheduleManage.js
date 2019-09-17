$(function(){
    layui.use(['laypage', 'layer','form','laydate'], function () {
        var $ = layui.jquery;
        var laypage = layui.laypage, layer = layui.layer, laydate = layui.laydate, form = layui.form();
        form.render('select')
        var importPeople = {}
        var tabRoleId =""
        var start = {
    	    min: '1900-01-01 00:00:00'
    	    ,max: '2049-06-16 23:59:59'
	    	,format: 'YYYY-MM-DD '
    	    ,istoday: true
    	    ,istime: true
    	    ,choose: function(datas){
    	      end.min = datas; //开始日选好后，重置结束日的最小日期
    	      end.start = datas //将结束日的初始值设定为开始日
    	    }
        };
    	var end = {
    	    min: '1900-01-01 '
    	    ,max: '2049-06-16 '
    	    ,format: 'YYYY-MM-DD '
    	    ,istoday: true
    	    ,istime: true
    	    ,choose: function(datas){
    	      start.max = datas; //结束日选好后，重置开始日的最大日期
    	    }
    	};
    	document.getElementById('signStartTime').onclick = function(){
		    start.elem = this;
		    laydate(start);
    	}
    	document.getElementById('signEndTime').onclick = function(){
		    end.elem = this
		    laydate(end);
    	}
    	$.ajax({
    		url: "/xl/queryDictionary?typeId=15",
    		type: "get",
    		success : function(res){
    			var data = res.data,roles = '<li class="layui-this" data-id="">全部</li>';
    			importPeople = data;
    			for(var k=0; k < data.length; k++){
					roles +='<li data-id="'+data[k].itemId+'">'+data[k].itemName+'</li>';
    			}
    			$(".layui-tab-title").html(roles);
    			//初始化表格
    			tabRoleId = $(".layui-tab-title li:first-child").attr("data-id");
    			loadTable(tabRoleId,1);
    			//加载完角色选项卡后加载layui 选项卡所需的模块
    			layui.use('element', function(){ 
    				var element = layui.element();
    				//监听Tab切换，选择不同角色
    				element.on('tab(docDemoTabBrief)', function(data){
    				   tabRoleId = $(".layui-tab-title li:eq("+data.index+")").attr("data-id");
    				   loadTable(tabRoleId,1);
    				});
    			});
    		},
    		error : function(data){
    			layer.close(layer.index);//取消遮罩
    			layui.use('layer', function(){
    				var layer = layui.layer;
    			    layer.msg('请求失败！')
    			});
    		}
    	})
    	$("#search").on("click",function(){
    		loadTable(tabRoleId,1)
    	})
    	function loadTable(type,currentPage){
    		var obj = new Object();
    		obj.name = $("#name").val()
    		obj.cardNum = $("#cardNum").val()
    		obj.year = $("#year").val()
    		obj.signStartTime = $("#signStartTime").val()
    		obj.signEndTime = $("#signEndTime").val()
    		obj.auditStatus = $("#auditStatus").val()
    		obj.medicalType = $("#medicalType").val()
    		obj.finishStatus = $("#finishStatus").val()
    		obj.isPayment = $("#isPayment").val()
    		obj.servicePackage = $("#servicePackage").val()
    		obj.signPersonType = type
    		obj.showCount = '10'
    		obj.currentPage = currentPage
    		$.ajax({
    			url: '/xl/queryAllSchedule',
				type: 'post',
				data: $.toJSON(obj),
				dataType: 'json',
				contentType: 'application/json',
				success: function(res){
					var data = res.items;
	        		var str = '';
	        		if(res.successful){
	        			pageSum = res.pagesCount;
	        			if(data.length>0){
	        				$.each(data,function(index,item){
	        					str += '<tr>';
	        					str += '<td>'+item.name+'</td>'
	        					str += '<td>'+item.signOrgName+'</td>' //暂无签约机构
	        					str += '<td>'+item.signStartDate+'至'+item.signEndDate+'</td>'
	        					str += '<td>'+item.servicePackageDesct+'</td>'
	        					str += '<td>'+item.signPayment+'</td>'
	        					str += '<td>'+item.systemHints+'</td>'
	        					str += '<td>'+getKeyCrowd(item.auditStatus)+'</td>'
        						str += '<td><button class="layui-btn layui-btn-small pass" data-id="'+item.id+'">通过</button>'
    							str += '<button class="layui-btn layui-btn-small unPass" data-id="'+item.id+'">不通过</button></td>'
    							str += '<td><button class="layui-btn layui-btn-small checkSchedule" data-id="'+item.id+'">查看</button></td>'
								str += '</tr>';
	        				})
	        				//分页
			        		laypage({
						        cont: 'pagging'
					    		, pages: pageSum
						        , curr: res.pageNumber||1
						        ,jump: function(obj, first){
						        	if(!first){
							            pageNum = obj.curr;
							            loadTable(type,pageNum);
						            }
						        }
			        		});
		        		}else{
		        			str += '<tr><td colspan="11" align="center">暂无数据</td></tr>'
		        		}
	        			$(".list-table tbody").html(str);
        				form.render();
	        		}
				},
				erroe: function(res){
					$('.list-table tbody').html('<tr><td colspan="11" align="center">列表获取失败</td></tr>');
				}
    		})
    	}
    	$("table").delegate(".checkSchedule","click",function(){
    		var id = $(this).attr("data-id");
    		$.ajax({
    			url: "/xl/queryOneSchedule?id="+id,
				type: "get",
				success : function(res){
					if(res.successful){
						var data = res.data
						if(data.servicePace){
							$("#serviceTypeInfo").html(data.servicePace)
						}else{
							$("#serviceTypeInfo").html("无")
						}
						if(data.project){
							$("#checkProjectInfo").html(data.project)
						}else{
							$("#checkProjectInfo").html("无")
						}
					}else{
						layer.msg("检查项目查询失败！")
					}
					layer.open({
			    		type:1
			    		,title:'服务包履约情况'
			    		,content: $('#infoSign')
						,area: ['500px','450px']
			    	})
				},
				error: function(res){
					layer.msg("检查项目查询失败！")
				}
    		})
    	})
    	$("table").delegate(".pass","click",function(){
    		var id=$(this).attr("data-id")
    		check(1,id)
    	})
    	$("table").delegate(".unPass","click",function(){
    		var id=$(this).attr("data-id")
    		check(2,id)
    	})
    	function check(type,id){
    		$.ajax({
    			url: "/xl/updateAuditStatus?id="+id+"&auditStatus="+type,
				type: "get",
				success : function(res){
					layer.msg("审核成功！",{time: 1500},function(){
						loadTable(type,1);
					})
				},
				error: function(res){
					layer.msg("审核失败！")
				}
    		})
    	}
    	function getKeyCrowd(type){
    		var auditStatus = [
    				{id:'1',value:'通过审核'},
    				{id:'2',value:'未通过审核'},
    				{id:'3',value:'未经过审核'}
    		]
    		var value = '';
    		$.each(auditStatus,function(index,item){
    			if(type == item.id){
    				value = item.value
    			}
    		})
    		return value
    	}
    })
})
