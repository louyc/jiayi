$(function(){
	 layui.use(['element','layer','jquery','form','laydate'],function(){
		 var $=layui.jquery,layer = layui.layer, laydate = layui.laydate,form = layui.form(),element = layui.element;
		 
		 var documentId = window.utils.getRequestParam("documentId");//档案Id
		 var qianyueId='';
		 var type = window.utils.getRequestParam("type");//登记/修改登记的标识 login：登记
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
					if(type != "login"){
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
						var signPersonType = data.signPersonType.split(",")
						$("input[name='signPersonType']").each(function(){
							for(var i = 0;i <signPersonType.length;i++ ){
								if($(this).attr("data-id") == signPersonType[i]){
									$(this).attr("checked","checked")
								}
							}
						})
						$("#servicePackage").val(data.servicePackage);
					}
				}
				form.render()
			},
			error: function(res){
				layer.msg("查询失败")
			}
		})
		 $("#goBack").on("click",function(){
			 history.back(-1)
		 })
		 $("#save").on("click",function(){
			 var obj = new Object();
			 var url = '';
			 if(type == "login"){//新增
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
			 var orginId = data.value
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
		 if(type == "login"){//新增
			getOrginList('');
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
									 }
									 if(item.managerId == orginId){
										 orgins +='<option value="'+item.managerId+'" selected>'+item.name+'</option>'
									 }else{
										 orgins +='<option value="'+item.managerId+'">'+item.name+'</option>'
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
		 form.on('select(servicePackage)', function(data){
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
	 })
})
