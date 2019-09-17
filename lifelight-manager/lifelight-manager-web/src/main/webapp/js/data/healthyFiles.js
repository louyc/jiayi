$(function(){
	layui.use(['layer','jquery','element','form','laydate'], function(){
	    var $ = layui.jquery;
	    var layer = layui.layer,element = layui.element(), laydate = layui.laydate,form = layui.form();
	    $("input,select").attr("disabled","disabled");
	    var managerId = window.utils.getRequestParam("managerId");
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
    	document.getElementById('sign_signStartDate').onclick = function(){
		    start.elem = this;
		    laydate(start);
    	}
    	document.getElementById('sign_signEndDate').onclick = function(){
		    end.elem = this
		    laydate(end);
    	}
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
					$('#sign_provinceCode').html('<option value="">请选择省份</option>').append(option);
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
					$('#sign_cityCode').html('<option value="">请选择城市</option>').append(option);
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
					$('#sign_townCode').html('<option value="">请选择乡/镇</option>').append(option);
					form.render('select');
				}else{
					layer.msg('获取乡/镇失败！')
				}
			})
		}
	    getFileData();
	    function getFileData(){
	    	getData();
	    	/*查询所有国籍*/
			 $.ajax({
			 	url: '/address/getAllCountry',
				type: 'get',
				async: false,
				success: function(res){
					if(res.successful && res.status == '200'){
						var str = '',data = res.data;
						if(!!data && data.length>0){
							for(var i = 0;i < data.length;i++){
								if(data[i].nicename == "China"){
									str +='<option value="'+data[i].id+'" selected="selected">'+data[i].nameZh+'</option>'
								}else{
									str +='<option value="'+data[i].id+'">'+data[i].nameZh+'</option>'
								}
							}
						}
						$("#country").html(str)
						form.render('select')
					}else{
						layer.msg(res.resultCode.message)
					}
				},
				error : function(res){
					layer.msg("查询国籍失败")
				}
			 })
			 /*查询所有民族*/
			 $.ajax({
				 url: '/address/getAllNation',
				 type: 'get',
				 async: false,
				 success: function(res){
					 if(res.successful && res.status == '200'){
						 var str = '',data = res.data;
						 if(!!data && data.length>0){
							 for(var i = 0;i < data.length;i++){
								 if(data[i].id == "1"){
									str +='<option value="'+data[i].id+'" selected="selected">'+data[i].mzName+'</option>'
								}else{
									str +='<option value="'+data[i].id+'">'+data[i].mzName+'</option>'
								}
							 }
						 }
						 $("#nation").html(str)
						 form.render('select')
					 }else{
						 layer.msg(res.resultCode.message)
					 }
				 },
				 error : function(res){
					 layer.msg("查询民族失败")
				 }
			 })
			 //根据身份证获取生日
			 $("#cardNum").blur(function(){
				 var cardNum = $.trim($(this).val());
				 if(cardNum.length == 0){
					 return
				 }
				 if ((cardNum.length != 15) && (cardNum.length != 18)) {
					 strReturn = "输入的身份证号位数错误";
					 layer.msg(strReturn)
				 }else{
					 if (cardNum.length == 15) {
						 tmpStr = cardNum.substring(6, 12);
						 tmpStr = "19" + tmpStr;
						 tmpStr = tmpStr.substring(0, 4) + "-" + tmpStr.substring(4, 6) + "-" + tmpStr.substring(6)
					 }else {
						 tmpStr = cardNum.substring(6, 14);
						 tmpStr = tmpStr.substring(0, 4) + "-" + tmpStr.substring(4, 6) + "-" + tmpStr.substring(6)
					 }
					 $("#birthday").val(tmpStr)
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
						$('#provinceCode').html('<option value="">请选择省份</option>').append(option);
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
						$('#cityCode').html('<option value="">请选择城市</option>').append(option);
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
						$('#townCode').html('<option value="">请选择乡/镇</option>').append(option);
						form.render('select');
					}else{
						layer.msg('获取乡/镇失败！')
					}
				})
			}
			/*建档医生查询*/
	    	function getDoctors(orginId,doctorId){
				 var obj = {"showCount":"1000","type":"4","signOrgId":orginId}
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
								 var orgins='<option value="">请选择</option>';
								 $.each(items,function(index,item){
									 if(item.managerId == doctorId){
										 orgins +='<option value="'+item.managerId+'" selected="selected">'+item.name+'</option>'
									 }else{
										 orgins +='<option value="'+item.managerId+'">'+item.name+'</option>'
									 }
								 })
								 $("#documentDoctor").html(orgins);
								 form.render()
							 }
						 }
					},
					error : function(res){
						layer.msg("查询建档医生失败")
					}
				 })
			 }
	    	 function getOrginList(orginId){
				 var obj = {"showCount":"1000","type":"4"}
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
									 var orgins='<option value="">请选择</option>';
									 var firstOrgin = ""
									 $.each(items,function(index,item){
										 if(index == '0'){
											 firstOrgin = item.managerId
										 }
										 if(item.managerId == orginId){
											 orgins +='<option value="'+item.managerId+'" selected="selected">'+item.name+'</option>'
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
	    	 form.on('select(signOrginName)', function(data){
				 var orginId = data.value
				 getDoctors(orginId)
			 });
			 function getData(){
				 $.ajax({
				 	url: '/xl/queryOnePersonDocument?managerId='+managerId,
					type: 'post',
					success: function(res){
						if(res.successful && res.status == "200"){
							var data = res.data
							$("#name").val(data.name)
							$("#sex").val(data.sex)
							$("#cardType").val(data.cardType)
							$("#cardNum").val(data.cardNum)
							$("#birthday").val(data.birthday)
							$("#mobile").val(data.mobile)
							$("#healthCard").val(data.healthCard)
							$("#documentId").val(data.documentId)
							$("#newFarmersCard").val(data.newFarmersCard)
							$("#medicalInsuranceCard").val(data.medicalInsuranceCard)
							
							getProvince(data.provinceCode);
							getCity(data.provinceCode,data.cityCode);
							getTown(data.cityCode,data.townCode);
							
							/*$("#provinceCode").val(data.provinceCode)
							$("#cityCode").val(data.cityCode)
							$("#townCode").val(data.townCode)*/
							
							$("#residentialAddress").val(data.residentialAddress)
							$("#permanentAddress").val(data.permanentAddress)
							$("#workUnit").val(data.workUnit)
							$("#ddlRHeadHousehold").val(data.ddlRHeadHousehold)
							$("#contactsName").val(data.contactsName)
							$("#contactsMobile").val(data.contactsMobile)
							$("#livingState").val(data.livingState)
							$("#livingType").val(data.livingType)
							$("#registeredResidence").val(data.registeredResidence)
							$("#arriveDate").val(data.arriveDate)
							$("#country").val(data.country)
							$("#nation").val(data.nation)
							$("#bloodType").val(data.bloodType)
							$("#rh").val(data.rh)
							$("#degreeEducation").val(data.degreeEducation)
							$("#occupation").val(data.occupation)
							$("#marriageStatus").val(data.marriageStatus)
							$("#dateMarriage").val(data.dateMarriage)
							//建档医生、建档机构
							getOrginList(data.documentOrg)
							getDoctors(data.documentOrg,data.documentDoctor)
							
							$("#documentDate").val(data.documentDate)
							$("#medicalCosts").val(data.medicalCosts)
							$("#medicalCardNumber").val(data.medicalCardNumber)
							$("#lowInsuranceCar").val(data.lowInsuranceCar)
							if(!!data.lowInsuranceCar){
								$("input[name='isLowInsurance']").eq("0").removeAttr("checked")
								$("input[name='isLowInsurance']").eq("1").attr("checked","checked")
							}
							$("#drugAllergyHistory").val(data.drugAllergyHistory)
							$("#pastHistory").val(data.pastHistory)
							$("#motherMedicalHistory").val(data.motherMedicalHistory)
							$("#fatherMedicalHistory").val(data.fatherMedicalHistory)
							$("#childrenMedicalHistory").val(data.childrenMedicalHistory)
							$("#brotherMedicalHistory").val(data.brotherMedicalHistory)
							$("#heredityHistory").val(data.heredityHistory)
							if(!!data.heredityHistory){
								$("input[name='haveMedicalHistory']").eq("0").removeAttr("checked")
								$("input[name='haveMedicalHistory']").eq("1").attr("checked","checked")
							}
							$("#isDisability").val(data.isDisability)
							$("#disabilityCard").val(data.disabilityCard)
							$("#exposeHistory").val(data.exposeHistory)
							if(!!data.operationHistory){
								operationHistoryValue = '1'
								$("input[name='operationHistory']").eq("1").attr("checked","checked")
								$("input[name='operationHistory']").eq("0").removeAttr("checked")
								$(".sss").show()
								var operationHistory = JSON.parse(data.operationHistory)
								$.each(operationHistory,function(index,item){
									$(".sss").eq(index).children(".layui-input-inline").eq("0").children("input").val(item.name)
									$(".sss").eq(index).children(".layui-input-inline").eq("1").children("input").val(item.time)
								})
							}
							if(!!data.traumaHistory){
								traumaHistoryValue = '1'
								$("input[name='traumaHistory']").eq("1").attr("checked","checked")
								$("input[name='traumaHistory']").eq("0").removeAttr("checked")
								$(".wss").show()
								var traumaHistory = JSON.parse(data.traumaHistory);
								$.each(traumaHistory,function(index,item){
									$(".wss").eq(index).children(".layui-input-inline").eq("0").children("input").val(item.name)
									$(".wss").eq(index).children(".layui-input-inline").eq("1").children("input").val(item.time)
								})
							}
							if(!!data.bloodTransfusionHistory){
								bloodTransfusionHistoryValue = '1'
								$("input[name='bloodTransfusionHistory']").eq("1").attr("checked","checked")
								$("input[name='bloodTransfusionHistory']").eq("0").removeAttr("checked")
								$(".sxs").show()
								var bloodTransfusionHistory = JSON.parse(data.bloodTransfusionHistory)
								$.each(bloodTransfusionHistory,function(index,item){
									$(".sxs").eq(index).children(".layui-input-inline").eq("0").children("input").val(item.name)
									$(".sxs").eq(index).children(".layui-input-inline").eq("1").children("input").val(item.time)
								})
							}
							$("#kitchenExhaust").val(data.kitchenExhaust)
							$("#fuelType").val(data.fuelType)
							$("#water").val(data.water)
							$("#toilet").val(data.toilet)
							$("#livestock").val(data.livestock)
							$("#remarks").val(data.remarks)
							/*$("#keyCrowdType").val(data.keyCrowdType)*/
							if(!!data.keyCrowdType){
								var keyCrowdType = data.keyCrowdType.split(",")
								$("input[name='keyCrowdType']").each(function(){
									for(var i = 0;i <keyCrowdType.length;i++ ){
										if($(this).attr("data-id") == keyCrowdType[i]){
											$(this).attr("checked","checked")
										}
									}
								})
							}
							if(data.variable){
								var item = JSON.parse(data.variable).variable;
							}else{
								var item = []
							}
							var html = "";
		                    for(var i = 0;i < item.length;i++){
		                    	html += '<div class="layui-form-item variable-element" variable-type="'+item[i].propertyType+'">';
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
			                				html += '<input element-type="5" type="radio" name="variable'+i+'" data-id="'+dictionaryItem[j].itemId+'" lay-skin="primary" title="'+dictionaryItem[j].itemName+'">'
			                			}
			                				break;
			                	}
		                    	html +='</div></div>'
		                    }
		                    $("#otherMessage").append(html);
		                    form.render()
							//配置模版字段
	                    	variable = JSON.parse(data.variable)
							$(".variable-element").each(function(index,item){
								var type=$(this).attr("variable-type")
								switch(type){
									case'0':
										$(this).children(".layui-input-block").children("input").val(variable.variable[index].value)
										break;
									case'1':
										$(this).children(".layui-input-block").children("select").val(variable.variable[index].value)
										break;
									case'2':
										var radioInput = $(this).children(".layui-input-block").children("input");
										for(var i = 0;i < radioInput.length;i++){
											if(radioInput.eq(i).attr("data-id") == variable.variable[index].value){
												radioInput.eq(i).attr("checked","checked")
											}
										}
										break;
									case'3':
										var value = variable.variable[index].value;
										var checkBoxInput = $(this).children(".layui-input-block").children("input");
										for(var i = 0;i < checkBoxInput.length;i++){
											if(value.indexOf(checkBoxInput.eq(i).attr("data-id")) >= 0){
												checkBoxInput.eq(i).attr("checked","checked")
											}
										}
										break;
									case'4':
										$(this).children(".layui-input-block").children("textarea").val(variable.variable[index].value)
										break;
									case'5':
										var radioInput = $(this).children(".layui-input-block").children("input");
										for(var i = 0;i < radioInput.length;i++){
											if(radioInput.eq(i).attr("data-id") == variable.variable[index].value){
												radioInput.eq(i).attr("checked","checked")
											}
										}
										break;
								}
							})
							form.render()
						}else{
							layer.msg("个人信息查询失败")
						}
					},
					error : function(res){
						layer.msg("个人信息查询失败")
					}
				 })
			 }
	    }
		$.ajax({
			url: '/xl/queryPersonContract?managerId='+managerId,
			type: 'get',
			async: false,
			success: function(res){
				var data = res.data
				if(!!data){
					$("#sign_documentId").val(data.documentNum);
					if(!data.lowInsuranceCar){
						$("#sign_isPoor").val("1")
					}else{
						$("#sign_isPoor").val("2")
					}
					$("#sign_name").val(data.name);
					$("#sign_cardNum").val(data.cardNum);
					$("#sign_sex").val(data.sex);
					var age = window.utils.getAge(data.cardNum)
					$("#sign_age").val(age);
					$("#sign_name").val(data.name);
					$("#sign_familyRelations").val(data.familyRelations);
					$("#sign_degreeEducation").val(data.degreeEducation);
					$("#sign_occupation").val(data.occupation);
					getProvince(data.provinceCode);
					getCity(data.provinceCode,data.cityCode);
					getTown(data.cityCode,data.townCode);
					
					$("#sign_residentialAddress").val(data.residentialAddress);
					var date = new Date();
					var year = date.getFullYear();
					$("#sign_signStartDate").val(year+"-01-01");
					$("#sign_signEndDate").val(year+"-12-31");
					qianyueId = data.id
					
					$("#sign_signDate").val(data.signDate);
					$("#sign_mobile").val(data.mobile);
					$("#sign_familyCount").val(data.familyCount);
					$("#sign_signMode").val(data.signMode);
					$("#sign_captainName").val(data.captainName);
					$("#sign_doctorMobile").val(data.doctorMobile);
					$("#sign_health").val(data.health);
					$("#sign_year").val(data.year);
					
					/*$("#signOrginName").val(data.signOrgId);
				$("#signDoctorId").val(data.signDoctorId);*/
					getOrginList(data.signOrgId)
					getDoctors(data.signOrgId,data.signDoctorId)
					
					$("#sign_serviceContent").val(data.serviceContent);
					$("#sign_healthAssessment").val(data.healthAssessment);
					$("#sign_arrangement").val(data.arrangement);
					$("#sign_serviceMode").val(data.serviceMode);
					$("#sign_medicalType").val(data.medicalType);
					$("#sign_signPayment").val(data.signPayment);
					$("#sign_selfPayment").val(data.selfPayment);
					$("#sign_compensationPayment").val(data.compensationPayment);
					var signPersonType = data.signPersonType.split(",")
					$("input[name='sign_signPersonType']").each(function(){
						for(var i = 0;i <signPersonType.length;i++ ){
							if($(this).attr("data-id") == signPersonType[i]){
								$(this).attr("checked","checked")
							}
						}
					})
					$("#sign_signPersonTypeservicePackage").val(data.servicePackage);
					//配置模版字段
					if(data.variable){
						var item = JSON.parse(data.variable).variable;
					}else{
						var item = []
					}
					var html = "";
                    for(var i = 0;i < item.length;i++){
                    	html += '<div class="layui-form-item variable-element" variable-type="'+item[i].propertyType+'">';
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
	                				html += '<input element-type="5" type="radio" name="variable'+i+'" data-id="'+dictionaryItem[j].itemId+'" lay-skin="primary" title="'+dictionaryItem[j].itemName+'">'
	                			}
	                				break;
	                	}
                    	html +='</div></div>'
                    }
                    $("#sign_otherMessage").append(html);
                    form.render()
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
				form.render()
			},
			error: function(res){
				layer.msg("查询失败")
			}
		})
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
								 $("#sign_signOrginName").html(orgins);
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
								 $("#sign_signDoctorId").html(orgins);
								 form.render()
							 }
						 }
					},
					error : function(res){
						layer.msg("查询签约医生失败")
					}
			 })
		 }
	});
})