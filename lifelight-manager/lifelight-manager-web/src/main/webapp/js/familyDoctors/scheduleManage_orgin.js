$(function(){
    layui.use(['laypage', 'layer','form','laydate'], function () {
        var $ = layui.jquery;
        var laypage = layui.laypage, layer = layui.layer, laydate = layui.laydate, form = layui.form();
        form.render('select')
        var managerId = window.utils.getCookie("managerId");
        var roleId = window.utils.getCookie("roleId");
        getDoctors();
        if(roleId == "1"){
        	var doctor_type = window.utils.getCookie("doctor_type");
        }else{
        	var doctor_type = "";
        }
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
    	});
    	/*建档医生查询*/
    	function getDoctors(){
			 var obj = {"showCount":"1000","type":"4","signOrgId":managerId}
			 $.ajax({
			 	url: '/jyDoctorInfo/getJySignDoctorInfo',
				type: 'post',
				data: $.toJSON(obj),
				dataType: 'json',
				contentType: 'application/json',
				success: function(res){
					 var items = res.items;
					 if(res.successful){
						 if(!!items && items.length>0){
							 var orgins = "";
							 $.each(items,function(index,item){
								 orgins +='<option value="'+item.managerId+'">'+item.name+'</option>'
							 })
							 $("#doctorList").html(orgins);
							 form.render();
						 }
					 }
				},
				error : function(res){
					layer.msg("查询建档医生失败")
				}
			 })
		 }
    	$("#search").on("click",function(){
    		loadTable(tabRoleId,1)
    	})
    	function loadTable(type,currentPage){
    		var obj = new Object();
    		obj.name = $("#name").val()
    		obj.cardNum = $("#cardNum").val()
    		obj.year = $("#year").val() == 'all'?'':$("#year").val()
    		obj.signStartTime = $("#signStartTime").val()
    		obj.signEndTime = $("#signEndTime").val()
    		obj.auditStatus = $("#auditStatus").val()
    		obj.medicalType = $("#medicalType").val()
    		obj.finishStatus = $("#finishStatus").val()
    		obj.isPayment = $("#isPayment").val()
    		obj.servicePackage = $("#servicePackage").val();
    		obj.projectType = $("#checkList").val();
    		obj.signPersonType = type
    		obj.showCount = '10'
    		obj.currentPage = currentPage;
			obj.signDoctorId = $("#doctorList").val();
    		$.ajax({
    			url: '/xl/queryAllSchedule',
				type: 'post',
				data: $.toJSON(obj),
				dataType: 'json',
				contentType: 'application/json',
				success: function(res){
					var data = res.items;
	        		var str = '';
	        		var counts = res.data;
	        		if(res.successful){
	        			pageSum = res.pagesCount;
	        			if(!counts || counts == {}){
	        				
	        			}else{
	        				var finishSum = counts.finishSum;
	        				var noFinishSum = counts.noFinishSum;
	        				var sum = counts.sum;
	        				var html = "履约完成人数："+finishSum+"，履约未完成人数："+noFinishSum+"，签约总人数："+sum;
	        				$("#numStatistic").text(html)
	        			}
	        			if(data.length>0){
	        				$.each(data,function(index,item){
	        					str += '<tr>';
	        					str += '<td><input type="checkbox" lay-skin="primary" data-phone="'+item.mobile+'" id="'+item.managerId+'"></td>'
	        					str += '<td>'+item.name+'</td>'
	        					str += '<td>'+item.signOrgName+'</td>' //暂无签约机构
	        					str += '<td>'+item.signStartDate+'至'+item.signEndDate+'</td>'
	        					var servicePackageDesc = !!item.servicePackageDesc?item.servicePackageDesc:""
	        					str += '<td>'+item.servicePackageDesc+'</td>'
	        					str += '<td>'+item.signPayment+'</td>'
	        					str += '<td>'+item.systemHints+'</td>'
	        					str += '<td>'+getKeyCrowd(item.auditStatus)+'</td>'
        						str += '<td><button class="layui-btn layui-btn-small pass" data-id="'+item.id+'">通过</button>'
    							str += '<button class="layui-btn layui-btn-small unPass" data-id="'+item.id+'">不通过</button></td>'
    							str += '<td><button class="layui-btn layui-btn-small checkSchedule" data-id="'+item.id+'">查看</button>';
	        					str += '<button class="layui-btn layui-btn-small export" data-id="'+item.id+'">上级诊疗申请</button></td>'
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
        				//全选
        				form.on('checkbox(allChoose)', function(data){
        				    var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]');
        				    child.each(function(index, item){
        				        item.checked = data.elem.checked;
        				    });
        				    form.render('checkbox');
        				})
	        		}
				},
				erroe: function(res){
					$('.list-table tbody').html('<tr><td colspan="11" align="center">列表获取失败</td></tr>');
				}
    		})
    	}
    	/*导出PDF*/
    	$("table").delegate(".export","click",function(){
    		var id = $(this).attr("data-id");
    		$('#toHospital').val("");
        	$('#toDoctor').val("");
        	$('#toReason').val("");
    		layer.open({
	            title:'导出PDF',
	            type: 1,
	            area: ['500px', '370px'],//宽高
	            shade: 0.5,
	            content: $('#exportInfo'),
	            btn: ['申请','取消'], //按钮
	            yes: function(index){
	            	var query='?id='+id;
	            	var hospital=$.trim($('#toHospital').val());
	            	var doctor=$.trim($('#toDoctor').val());
	            	var reason=$.trim($('#toReason').val());
	            	if(!hospital || !doctor || !reason){
	            		layer.msg("请将信息填写完整！",{zIndex: layer.zindex});return;
	            	}
	            	query += "&hospital="+hospital+"&doctor="+doctor+"&reason="+reason;
	            	$("#exportBtn").attr('href','/xl/exportSchedule'+query);
	            	document.getElementById("exportBtn").click();
	            	layer.closeAll()
	            }
	        });
    	})
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
    	form.on('select(servicePackageFilter)', function(data){
	        var packageId = data.value;
	        $.ajax({
	            url:'/package/getAllServicePackage?pageSize=1000&currentsPage=1&packageId='+packageId,
	            type:'GET',
	            success : function(data){
	                if(data.status === 200 && data.successful){
	                    var list = data.items,html = '<option value="">全部</option>',num = 0;
	                    checkLists = data.items;
	                    while(list[0].listDictionary && (list[0].listDictionary.length > num)){
	                        var item = list[0].listDictionary[num];
	                        html += '<option value="'+item.itemId+'">'+item.itemName+'</option>';
	                        num++;
	                    }
	                    html && $('#checkList').html(html);
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
	    });
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
    	$("#pushMsg").on("click",function(){
    		var userList = [];
    		$("tbody tr td:first-child .layui-form-checkbox").each(function(){
    			if($(this).attr("class").indexOf("layui-form-checked") >= 0){
    				userList.push($(this).prev("input").attr("id"))
    			}
    		})
    		if(userList.length<=0){
    			layer.msg("请选择用户");return;
    		}
        	layer.open({
        		type:2
        		,title:'消息推送'
    			,area: ['500px','500px']
        		,content: '../data/ueEditMessage.html'
    	    	,btn: ['确定','关闭'] //按钮
        		,yes: function(index){
        			var UE = $(".layui-layer-iframe").find("iframe")[0].contentWindow.UE;
        			var goodsDesc = UE.getEditor('container').getContent();
        			var imgAry = "";
        			var isGo = true;
        			var doctorId = window.utils.getCookie("managerId");
        			$(goodsDesc).find('img').each(function(){
       	             	var src = $(this).attr('src');
       	             	imgAry += src + ",";
                    });
                    $.ajax({
            			url:'/message/sendDoctorMessageList?doctorId='+managerId+'&toUserIds='+userList+'&context='+goodsDesc,
            			type: 'POST',
        	        	success:function(res){
        	        		if(res.successful && res.resultCode.code == 'SUCCESS'){
        	        			layer.msg("消息推送成功！",{time: 2000,zIndex: layer.zIndex},function(){
        	        				layer.closeAll();
        	        			});
        	        		}else{
        	        			isGo = false;
        	        			layer.msg("消息推送失败！",{time: 2000,zIndex: layer.zIndex});
        	        			return;
        	        		}
       	        		},
       	        		error:function(err){
       	        			isGo = false;
       	        			layer.msg("消息推送失败！",{time: 2000,zIndex: layer.zIndex});
       	        			return;
       	        		}
         			})
             		if(imgAry != "" && imgAry.length > 0 ){
             			if(isGo){
             				$.ajax({
           	        			url:'/file/upFile',
           	        			type: 'POST',
           	        			async:false,
           	    				data: {files:imgAry.substring(0,imgAry.length-1)},
           	    	        	success:function(res){
           	    	        		if(res.successful && res.resultCode.code == 'SUCCESS'){
           	    	        			
           	    	        		}else{
           	    	        			layer.msg('图片'+res.resultCode.message,{time: 2000,zIndex: layer.zIndex})
           	    	        			return;
           	    	        		}
               	        		},
               	        		error:function(err){
               	        			layer.msg('图片'+res.resultCode.message,{time: 2000,zIndex: layer.zIndex})
               	        			return;
               	        		}
                 			});
             			}
             		}
        		}
        		,zIndex: layer.zIndex //重点1
    	        ,success: function(layero){
    	        	
    	        }
        	})
    	});
    	//下发短信
	 	$("#telePhoneMsg").on("click",function(){
    		var userList = "";
    		$("#phoneMsg").val("");
    		$("tbody tr td:first-child .layui-form-checkbox").each(function(){
    			if($(this).attr("class").indexOf("layui-form-checked") >= 0){
    				if(!!userList){
    					userList += ","
    				}
    				userList += $(this).prev("input").attr("data-phone");
    			}
    		})
    		if(userList.length<=0){
    			layer.msg("请选择用户");return;
    		}
    		layer.open({
        		type:1
        		,title:'短信下发'
    			,area: ['500px','500px']
        		,content: $("#phoneMsg")
    	    	,btn: ['确定','关闭'] //按钮
        		,yes: function(index){
        			var phoneMsg = $("#phoneMsg_text").val();
        			if(!phoneMsg){
        				layer.msg("请输入下发短信信息",{zIndex:layer.zIndex})
        			}else{
        				var obj_msg = new Object();
        				obj_msg.telephoneMsg = phoneMsg;
        				obj_msg.phoneNum = userList;
        				$.ajax({
        	    			url: '/xl/groupSMS',
        					type: 'post',
        					data: $.toJSON(obj_msg),
        					dataType: 'json',
        					contentType: 'application/json',
        					success: function(res){
        		        		if(res.successful){
        		        			layer.msg("下发短信成功！",{time: 1500,zIndex: layer.zIndex},function(){
        		        				layer.closeAll();
        		        			})
        		        		}else{
        		        			layer.msg("下发短信失败！",{zIndex: layer.zIndex})
        		        		}
        					},
        					error: function(res){
        						layer.msg("下发短信失败！",{zIndex: layer.zIndex})
        					}
        	    		})
        			}
        		}
	    		,zIndex: layer.zIndex //重点1
		        ,success: function(layero){
		        	
		        }
	    	})
    	});
    })
})
