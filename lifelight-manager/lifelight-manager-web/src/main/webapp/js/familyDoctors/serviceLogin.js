$(function(){
    layui.use(['laypage', 'layer','form','laydate'], function () {
        var $ = layui.jquery;
        var laypage = layui.laypage, layer = layui.layer, laydate = layui.laydate, form = layui.form();
        form.render('select')
        var importPeople = {};
        var tabRoleId ="";
        var managerId = window.utils.getCookie("managerId");
        var roleId = window.utils.getCookie("roleId");
        if(roleId == "1"){
        	var doctor_type = window.utils.getCookie("doctor_type");
        }else{
        	var doctor_type = "";
        }
        var start = {
    	    min: '1900-01-01'
    	    ,max: '2049-06-16'
	    	,format: 'YYYY-MM-DD'
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
    		url: "/xl/queryDictionary?typeId=1",
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
    	getProvince('');
		//获取省份
		function getProvince(provinceVal){
			$.get('/address/getAllProvince',function(res){
				if(res.successful){
					var option = '';
					$.each(res.data,function(index,item){
						if(provinceVal == item.code){
							option += '<option value="'+item.code+'" selected>'+item.name+'</option>';
						}else{
							option += '<option value="'+item.code+'">'+item.name+'</option>';
						}
					})
					$('#province').html('<option value="">请选择省份</option>').append(option);
					form.render('select');
				}else{
					layer.msg('获取省份失败！')
				}
			})
		}
		form.on('select(province)', function(data){
		  	var provinceCode = data.value;
		  	$('#townCode').val("")
			getCity(provinceCode,'');
		});
		//获取城市
		function getCity(provinceCode,cityVal){
			$.get('/address/getCityByProvinceCode?provinceCode='+provinceCode,function(res){
				if(res.successful){
					var option = '';
					$.each(res.data,function(index,item){
						option += '<option value="'+item.code+'" '+(cityVal == item.code ? "selected" : "")+'>'+item.name+'</option>';
					})
					$('#city').html('<option value="">请选择城市</option>').append(option);
					form.render('select');
				}else{
					layer.msg('获取城市失败！')
				}
			})
		}
		form.on('select(city)', function(data){
		  	var cityCode = data.value;
		  	getTown(cityCode,'');
		});
		//获取乡/镇
		function getTown(cityCode,townVal){
			$.get('/address/getTownByTownCode?cityCode='+cityCode,function(res){
				if(res.successful){
					var option = '';
					$.each(res.data,function(index,item){
						option += '<option value="'+item.code+'" '+(townVal == item.code ? "selected" : "")+'>'+item.name+'</option>';
					})
					$('#town').html('<option value="">请选择乡/镇</option>').append(option);
					form.render('select');
				}else{
					layer.msg('获取乡/镇失败！')
				}
			})
		}
    	
    	$("#search").on("click",function(){
    		loadTable(tabRoleId,1)
    	})
    	function loadTable(type,currentPage){
    		var obj = new Object();
    		obj.name = $("#name").val()
    		obj.documentId = $("#documentId").val()
    		obj.isSign = $("#isSign").val()
    		obj.cardNum = $("#cardNum").val()
    		obj.provinceCode = $("#province").val()
    		obj.cityCode = $("#city").val()
    		obj.townCode = $("#town").val()
    		obj.signStartTime = $("#signStartTime").val()
    		obj.signEndTime = $("#signEndTime").val()
    		obj.keyCrowdType = type
    		obj.showCount = '10'
    		obj.currentPage = currentPage;
    		/*if(roleId =="1" && doctor_type =="1"){
    			obj.documentDoctor = managerId
    		}*/
    		if(!(roleId =="4" || roleId =="6")){
    			obj.documentDoctor = managerId
    		}
    		$.ajax({
    			url: '/xl/queryPersonDocument',
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
	        					str += '<td><input type="checkbox" lay-skin="primary" data-phone="'+item.mobile+'" id="'+item.managerId+'"></td>'
	        					str += '<td>'+item.name+'</td>'
	        					var sex = item.sex == "1"?"男":"女"
	        					str += '<td>'+sex+'</td>'
	        					var healthCard = !item.healthCard?"":item.healthCard
	        					str += '<td>'+healthCard+'</td>'
	        					str += '<td>'+item.cardNum+'</td>'
	        					var residentialAddress = item.residentialAddress ===null?"":item.residentialAddress
	        					str += '<td>'+residentialAddress+'</td>'
	        					var mobile = item.mobile ===null?"":item.mobile
	        					str += '<td>'+mobile+'</td>'
	        					//重点人群
	        					if(!!item.keyCrowdType){
	        						str += '<td>'+getKeyCrowd(item.keyCrowdType)+'</td>'
	        					}else{
	        						str += '<td></td>'
	        					}
	        					var documentDoctor = item.documentDoctor ===null?"":item.documentDoctor
	        					str += '<td>'+documentDoctor+'</td>'
	        					var documentDate = item.documentDate ===null?"":item.documentDate
	        					str += '<td>'+documentDate+'</td>'
	        					var isSign = item.isSign == '1'?'是':'否'
        						str += '<td>'+isSign+'</td>'
        						str += '<td>'
    							str += '<a href="addFamilyDoctors.html?documentId='+item.id+'" class="layui-btn layui-btn-small">修改</a>'
    							if(item.isSign == '2'){
    								str += '<a href="serviceSign.html?documentId='+item.id+'&type=login" class="layui-btn layui-btn-small">签约登记</a>'
    							}
	        					str += '</td></tr>'
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
	        			$(".layui-table tbody").html(str);
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
				error: function(res){
					$('.list-table tbody').html('<tr><td colspan="11" align="center">列表获取失败</td></tr>');
				}
    		})
    	}
    	function getKeyCrowd(type){
    		var itemName = '';
			var typeList = type.split(",")
    		$.each(importPeople,function(index,item){
    			for(var i =0;i<typeList.length;i++){
    				if(typeList[i] == item.itemId){
    					if(itemName){
    						itemName+=","
    					}
        				itemName+=item.itemName
        			}
    			}
    		})
    		return itemName
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
    			,area: ['500px','300px']
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
