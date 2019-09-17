$(function(){
	 layui.use(['element','layer','jquery','form','laydate'],function(){
		 var $=layui.jquery,layer = layui.layer, laydate = layui.laydate,form = layui.form(),element = layui.element;
		 
		 var documentId = window.utils.getRequestParam("documentId");//档案Id
		 var qianyueId='';
		 var requestType = window.utils.getRequestParam("type");//登记/修改登记的标识 login：登记
		 var variable = "";
		 var roleId = window.utils.getCookie("roleId");
		 var managerId = window.utils.getCookie("managerId");
		 function loading(orginId){
	    	var obj = new Object();
	        $.ajax({
	            url:'/template/getAll',
	            type:'POST',
	            data: $.toJSON(obj),
	            dataType:'json',
	            async: true,
	            contentType: "application/json",
	            success : function(data){
	                if(data.status === 200 && data.resultCode.code === 'SUCCESS'){
	                    var list = data.data.templateList,html = '',num = 0;
	                    while(list && (list.length > num)){
	                        var items = list[num];
                    		if(items.templateType == "2"){
                    			variable = JSON.parse(items.variable)
	                    		break
	                    	}
	                        num++;
	                    }
	                    $(".variable-element").remove()
	                    var html="";
	                    var item = variable.variable;
	                    for(var i = 0;i < item.length;i++){
	                    	if(item[i].signOrginName == orginId){
	                    		html += '<div class="layui-form-item variable-element" variable-type="'+item[i].propertyType+'">';
	                    	}else{
	                    		variable.variable.splice(i,1);
	                    		continue;
	                    		html += '<div class="layui-form-item variable-element" style="display: none" variable-type="'+item[i].propertyType+'">';
	                    	}
                    		html += '<label class="layui-form-label">'+item[i].propertyName+'</label>';
                    		html += '<div class="layui-input-block">';
                    		switch(item[i].propertyType){
                    		case '0':
                    			html += '<input element-type="0" type="text" autocomplete="off" class="layui-input"/>';
                    			break;
                    		case '1':
                    			html += '<select element-type="1">';
                    			var dictionaryItem = item[i].propertyContent;
                    			html += '<option value="">请选择</option>';
                    			for(var j =0;j < dictionaryItem.length;j++){
                    				html += '<option value="'+j+'">'+dictionaryItem[j]+'</option>'
                    			}
                    			html += '</select>'
                    				break;
                    		case '2':
                    			var dictionaryItem = item[i].propertyContent;
                    			for(var j =0;j < dictionaryItem.length;j++){
                    				html += '<input element-type="2" type="radio" name="variable'+i+'" data-id="'+j+'" lay-skin="primary" title="'+dictionaryItem[j]+'">'
                    			}
                    			break;
                    		case '3':
                    			var dictionaryItem = item[i].propertyContent;
                    			for(var j =0;j < dictionaryItem.length;j++){
                    				html += '<input element-type="3" type="checkbox" name="variable'+i+'" data-id="'+j+'" lay-skin="primary" title="'+dictionaryItem[j]+'">'
                    			}
                    			break;
                    		case '4':
                    			html += '<textarea element-type="4" placeholder="请输入'+item[i].propertyName+'" class="layui-textarea" ></textarea>'
                    			break;
                    		case '5':
                    			var dictionaryItem = window.utils.getServicePack();
                    			for(var j =0;j < dictionaryItem.length;j++){
                    				html += '<input element-type="5" type="radio" value="'+dictionaryItem[j].itemId+'" name="variable'+i+'" data-id="'+dictionaryItem[j].itemId+'" lay-skin="primary" lay-filter="servicePackage" title="'+dictionaryItem[j].itemName+'">'
                    			}
                    			break;
                    		}
                    		html +='</div></div>'
	                    }
	                    $("#otherMessage").append(html);
	                    form.render()
	                    getMoney();
	                }else{
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
    	document.getElementById('signStartDate').onclick = function(){
		    start.elem = this;
		    laydate(start);
    	}
    	document.getElementById('signEndDate').onclick = function(){
		    end.elem = this
		    laydate(end);
    	}
    	if(!!documentId){
    		$.ajax({
    			url: '/xl/queryPersonContract?documentId='+documentId,
    			type: 'get',
    			success: function(res){
    				var data = res.data
    				if(!!data){
    					$("#documentId").val(data.documentNum);
    					if(!data.lowInsuranceCar){
    						$("#isPoor").val("1")
    					}else{
    						$("#isPoor").val("2")
    					}
    					$("#name").val(data.name);
    					$("#cardNum").val(data.cardNum);
    					$("#sex").val(data.sex);
    					var age = window.utils.getAge(data.cardNum)
    					$("#age").val(age);
    					$("#name").val(data.name);
    					$("#familyRelations").val(data.familyRelations);
    					$("#degreeEducation").val(data.degreeEducation);
    					$("#occupation").val(data.occupation);
    					getProvince(data.provinceCode);
    					getCity(data.provinceCode,data.cityCode);
    					getTown(data.cityCode,data.townCode);
    					
    					$("#residentialAddress").val(data.residentialAddress);
    					var date = new Date();
    					var year = date.getFullYear();
    					$("#signStartDate").val(year+"-01-01");
    					$("#signEndDate").val(year+"-12-31");
    					if(requestType != "login"){
    						qianyueId = data.id
    						
    						$("#signDate").val(data.signDate);
    						$("#mobile").val(data.mobile);
    						$("#familyCount").val(data.familyCount);
    						$("#signMode").val(data.signMode);
    						$("#captainName").val(data.captainName);
    						$("#doctorMobile").val(data.doctorMobile);
    						$("#health").val(data.health);
    						$("#year").val(data.year);
    						
    						/*$("#signOrginName").val(data.signOrgId);
    					$("#signDoctorId").val(data.signDoctorId);*/
    						getOrginList(data.signOrgId)
    						getDoctors(data.signOrgId,data.signDoctorId)
    						
    						$("#serviceContent").val(data.serviceContent);
    						$("#healthAssessment").val(data.healthAssessment);
    						$("#arrangement").val(data.arrangement);
    						$("#serviceMode").val(data.serviceMode);
    						$("#medicalType").val(data.medicalType);
    						$("#signPayment").val(data.signPayment);
    						$("#selfPayment").val(data.selfPayment);
    						$("#compensationPayment").val(data.compensationPayment);
    						if(!data.signPersonType || data.signPersonType == null){
    							
    						}else{
    							var signPersonType = data.signPersonType.split(",")
    							$("input[name='signPersonType']").each(function(){
    								for(var i = 0;i <signPersonType.length;i++ ){
    									if($(this).attr("data-id") == signPersonType[i]){
    										$(this).attr("checked","checked")
    									}
    								}
    							})
    						}
    						$("#servicePackage").val(data.servicePackage);
    						//配置模版字段
    						var item = JSON.parse(data.variable).variable;
    						variable = JSON.parse(data.variable);
    						var html = "";
    	                    for(var i = 0;i < item.length;i++){
    	                    	if(item[i].signOrginName == data.signOrgId){
    	                    		html += '<div class="layui-form-item variable-element" variable-type="'+item[i].propertyType+'">';
    	                    	}else{
    	                    		variable.variable.splice(i,1);
    	                    		continue;
    	                    		html += '<div class="layui-form-item variable-element" style="display: none" variable-type="'+item[i].propertyType+'">';
    	                    	}
    	                    	html += '<label class="layui-form-label">'+item[i].propertyName+'</label>';
    	                    	html += '<div class="layui-input-block">';
    	                    	switch(item[i].propertyType){
    		                		case '0':
    		                			html += '<input element-type="0" type="text" autocomplete="off" class="layui-input"/>';
    		                				break;
    		                		case '1':
    		                			html += '<select element-type="1">';
    		                			var dictionaryItem = item[i].propertyContent;
    		                			html += '<option value="">请选择</option>';
    		                			for(var j =0;j < dictionaryItem.length;j++){
    		                				html += '<option value="'+j+'">'+dictionaryItem[j]+'</option>'
    		                			}
                    					html += '</select>'
    		                				break;
    		                		case '2':
    		                			var dictionaryItem = item[i].propertyContent;
    		                			for(var j =0;j < dictionaryItem.length;j++){
    		                				html += '<input element-type="2" type="radio" name="variable'+i+'" data-id="'+j+'" lay-skin="primary" title="'+dictionaryItem[j]+'">'
    		                			}
    		                				break;
    		                		case '3':
    		                			var dictionaryItem = item[i].propertyContent;
    		                			for(var j =0;j < dictionaryItem.length;j++){
    		                				html += '<input element-type="3" type="checkbox" name="variable'+i+'" data-id="'+j+'" lay-skin="primary" title="'+dictionaryItem[j]+'">'
    		                			}
    		                				break;
    		                		case '4':
    	                				html += '<textarea element-type="4" placeholder="请输入'+item[i].propertyName+'" class="layui-textarea" ></textarea>'
    		                				break;
    		                		case '5':
    		                			var dictionaryItem = window.utils.getServicePack();
    		                			for(var j =0;j < dictionaryItem.length;j++){
    		                				html += '<input element-type="5" type="radio" name="variable'+i+'" value="'+dictionaryItem[j].itemId+'" data-id="'+dictionaryItem[j].itemId+'" lay-skin="primary" lay-filter="servicePackage" title="'+dictionaryItem[j].itemName+'">'
    		                			}
    		                				break;
    		                	}
    	                    	html +='</div></div>'
    	                    }
    	                    $("#otherMessage").append(html);
    	                    form.render()
    	                    getMoney()
    						//配置模版字段
    						var variable_value = JSON.parse(data.variable)
    						$(".variable-element").each(function(index,item){
    							var type=$(this).attr("variable-type")
    							switch(type){
    								case'0':
    									$(this).children(".layui-input-block").children("input").val(variable_value.variable[index].value)
    									break;
    								case'1':
    									$(this).children(".layui-input-block").children("select").val(variable_value.variable[index].value)
    									break;
    								case'2':
    									var radioInput = $(this).children(".layui-input-block").children("input");
    									for(var i = 0;i < radioInput.length;i++){
    										if(radioInput.eq(i).attr("data-id") == variable_value.variable[index].value){
    											radioInput.eq(i).attr("checked","checked")
    										}
    									}
    									break;
    								case'3':
    									var value = variable_value.variable[index].value;
    									var checkBoxInput = $(this).children(".layui-input-block").children("input");
    									for(var i = 0;i < checkBoxInput.length;i++){
    										if(value.indexOf(checkBoxInput.eq(i).attr("data-id")) >= 0){
    											checkBoxInput.eq(i).attr("checked","checked")
    										}
    									}
    									break;
    								case'4':
    									$(this).children(".layui-input-block").children("textarea").val(variable_value.variable[index].value)
    									break;
    								case '5'://服务包
    							 		var radioInput = $(this).children(".layui-input-block").children("input");
    									for(var i = 0;i < radioInput.length;i++){
    										if(radioInput.eq(i).attr("data-id") == variable_value.variable[index].value){
    											radioInput.eq(i).attr("checked","checked")
    										}
    									}
    							 		break;
    							}
    						})
    					}
    				}
    				form.render()
    			},
    			error: function(res){
    				layer.msg("查询失败")
    			}
    		})
    	 }
		 $("#goBack").on("click",function(){
			 history.back(-1)
		 })
		 $("#save").on("click",function(){
			 var obj = new Object();
			 var url = '';
			 if(requestType == "login"){//新增
				 url = '/xl/registPersonContract'
			 }else{//修改
				 url = '/xl/updatePersonContract'
				 obj.id = qianyueId;
			 }
			 obj.documentId = documentId;
			 obj.signDate = $("#signDate").val();
			 obj.mobile = $("#mobile").val();
			 obj.familyCount = $("#familyCount").val();
			 obj.signMode = $("#signMode").val();
			 obj.captainName = $("#captainName").val();
			 obj.doctorMobile = $("#doctorMobile").val();
			 obj.health = $("#health").val();
			 obj.year = $("#year").val();
			 obj.signDoctorId = $("#signDoctorId").val();
			 obj.signStartDate = $("#signStartDate").val();
			 obj.signEndDate = $("#signEndDate").val();
			 obj.serviceContent = $("#serviceContent").val();
			 obj.healthAssessment = $("#healthAssessment").val();
			 obj.arrangement = $("#arrangement").val();
			 obj.serviceMode = $("#serviceMode").val();
			 obj.medicalType = $("#medicalType").val();
			 obj.signPayment = $("#signPayment").val();
			 obj.selfPayment = $("#selfPayment").val();
			 obj.compensationPayment = $("#compensationPayment").val();
			 obj.servicePackage = $("#servicePackage").val();
			 obj.signPersonType = '';
			 $("input[name='signPersonType']").each(function(){
				 if($(this).next('div').attr("class").indexOf('layui-form-checked')>=0){
					 if(obj.signPersonType){
						 obj.signPersonType+=","
					 }
					 obj.signPersonType+=$(this).attr("data-id")
				 }
			 })
			 if(!obj.signPersonType){
				 layer.msg("签约人群不能为空");
				 return false;
			 }
			 if(!!variable){
				 for(var i = 0;i < variable.variable.length;i++){
					 var type = $(".variable-element").eq(i).attr("variable-type");
					 switch(type){
					 case '0'://输入框
						 variable.variable[i].value=$(".variable-element").eq(i).children(".layui-input-block").children("input").val();
						 break;
					 case '1'://下拉
						 variable.variable[i].value=$(".variable-element").eq(i).children(".layui-input-block").children("select").val();
						 break;
					 case '2'://单选
						 var vlaue = "";
						 $(".variable-element").eq(i).children(".layui-input-block").children(".layui-form-radio").each(function(){
							 if($(this).children("i").attr("class").indexOf("layui-anim-scaleSpring") >=0){
								 vlaue = $(this).prev("input").attr("data-id")
							 }
						 })
						 variable.variable[i].value=vlaue
						 break;
					 case '3'://多选
						 var vlaue = "";
						 $(".variable-element").eq(i).children(".layui-input-block").children(".layui-form-checkbox").each(function(){
							 if($(this).attr("class").indexOf("layui-form-checked") >=0){
								 if(!vlaue){
									 vlaue += ","
								 }
								 vlaue += $(this).prev("input").attr("data-id")
							 }
						 })
						 variable.variable[i].value=vlaue
						 break;
					 case '4'://文本框
						 variable.variable[i].value=$(".variable-element").eq(i).children(".layui-input-block").children("textarea").val();
						 break;
					 case '5'://服务包
						 var vlaue = "";
						 $(".variable-element").eq(i).children(".layui-input-block").children(".layui-form-radio").each(function(){
							 if($(this).children("i").attr("class").indexOf("layui-anim-scaleSpring") >=0){
								 vlaue = $(this).prev("input").attr("data-id")
							 }
						 })
						 variable.variable[i].value=vlaue
					 }
				 }
			 }
			 obj.variable = JSON.stringify(variable)
			 $.ajax({
			 	url: url,
				type: 'post',
				data: $.toJSON(obj),
				dataType: 'json',
				contentType: 'application/json',
				success: function(res){
					if(res.successful && res.status == '200'){
						layer.msg("签约信息登记成功",{time: 2000},function(){
							window.history.go(-1);
						})
					}else{
						layer.msg(res.resultCode.message)
					}
				},
				error : function(res){
					layer.msg("签约信息登记失败")
				}
			 })
		 })
		 form.on('select(signOrginName)', function(data){
			 var orginId = data.value;
			 if(requestType == "login"){
				 loading(orginId)
			 }
			 getDoctors(orginId)
		 });
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
					$('#provinceCode').html('<option value="">请选择省份</option>').append(option);
					form.render('select');
				}else{
					layer.msg('获取省份失败！')
				}
			})
		}
		//获取城市
		function getCity(provinceCode,cityVal){
			$.get('/address/getCityByProvinceCode?provinceCode='+provinceCode,function(res){
				if(res.successful){
					var option = '';
					$.each(res.data,function(index,item){
						option += '<option value="'+item.code+'" '+(cityVal == item.code ? "selected" : "")+'>'+item.name+'</option>';
					})
					$('#cityCode').html('<option value="">请选择城市</option>').append(option);
					form.render('select');
				}else{
					layer.msg('获取城市失败！')
				}
			})
		}
		//获取乡/镇
		function getTown(cityCode,townVal){
			$.get('/address/getTownByTownCode?cityCode='+cityCode,function(res){
				if(res.successful){
					var option = '';
					$.each(res.data,function(index,item){
						option += '<option value="'+item.code+'" '+(townVal == item.code ? "selected" : "")+'>'+item.name+'</option>';
					})
					$('#townCode').html('<option value="">请选择乡/镇</option>').append(option);
					form.render('select');
				}else{
					layer.msg('获取乡/镇失败！')
				}
			})
		}
		 /*获取签约机构*/
		 if(requestType == "login"){//新增
			 if(roleId == "1"){
				 getDoctorOrg()
			 }else{
				 getOrginList('');
			 }
		 }
		 //查询当前医生机构
		 function getDoctorOrg(){
			 var data={showCount:1,currentPage:1,doctorId:managerId,signType:1,name:""};
			 var dataJson=$.toJSON(data);
			 $.ajax({
					url:'/jyOrgInfo/getJyDoctorSignOrg',
					type:'POST',
					data:dataJson,
					dataType:"json",
	        		contentType: "application/json",
	        		success:function(data){
	        			if(data.successful){
	        				var orginId = data.items[0].managerId;
	        				getOrginList(orginId);
	        				getDoctors(orginId,managerId)
	        				loading(orginId)
	        			}
	        		}
			 })
			 
		 }
		 function getOrginList(orginId){
			 var obj = {"showCount":"0","type":"4"}
			 $.ajax({
				 	url: '/jyOrgInfo/getJyOrgInfoList',
					type: 'post',
					data: $.toJSON(obj),
					dataType: 'json',
					contentType: 'application/json',
					success: function(res){
						 var items = res.items;
						 if(res.successful){
							 if(!!items && items.length>0){
								 var orgins='';
								 var firstOrgin = ""
								 $.each(items,function(index,item){
									 if(index == '0'){
										 firstOrgin = item.managerId
										 if(requestType == "login"){
											 if( roleId == "1"){
												 
											 }else{
												 loading(firstOrgin)
											 }
										 }
									 }
									 if(item.managerId == orginId){
										 orgins +='<option value="'+item.managerId+'" selected>'+item.name+'</option>'
									 }else{
										 orgins +='<option value="'+item.managerId+'">'+item.name+'</option>'
									 }
									 if(requestType != "login"){
										 $("#signOrginName").attr("disabled","disabled")
									 }
								 })
								 $("#signOrginName").html(orgins);
								 if(!orginId){
									 getDoctors(firstOrgin,'')
								 }
								 form.render()
							 }
						 }
					},
					error : function(res){
						layer.msg("查询签约结构失败")
					}
			 })
		 }
		 /*获取签约医生*/
		 function getDoctors(orginId,doctorId){
			 var obj = {"showCount":"0","type":"4","signOrgId":orginId}
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
								 var orgins='';
								 $.each(items,function(index,item){
									 if(doctorId == item.managerId){
										 orgins +='<option value="'+item.managerId+'" selected>'+item.name+'</option>'
									 }else{
										 orgins +='<option value="'+item.managerId+'">'+item.name+'</option>'
									 }
								 })
								 $("#signDoctorId").html(orgins);
								 form.render()
							 }
						 }
					},
					error : function(res){
						layer.msg("查询签约医生失败")
					}
			 })
		 }
		 function getMoney(){
			 form.on('radio(servicePackage)', function(data){
				 var servicePackageId = data.value
				 $.ajax({
					 	url: '/xl/queryServiceContent?id='+servicePackageId,
						type: 'post',
						success: function(res){
							 if(res.successful){
								 var data = res.data;
								 $("#serviceContent").val(data.content);
								 var moneys = data.payment
								 $("#signPayment").val(moneys.split(",")[0])
								 $("#selfPayment").val(moneys.split(",")[3])
								 $("#compensationPayment").val(moneys.split(",")[1])
							 }
						},
						error : function(res){
							layer.msg("查询签约医生失败")
						}
				 })
			 });
		 }
	 })
})
