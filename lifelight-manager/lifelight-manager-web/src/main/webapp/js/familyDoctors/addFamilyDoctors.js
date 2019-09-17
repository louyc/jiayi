$(function(){
	 layui.use(['element','layer','jquery','form','laydate'],function(){
		 var $=layui.jquery,layer = layui.layer, laydate = layui.laydate,form = layui.form(),element = layui.element;
		 var documentId = window.utils.getRequestParam("documentId");//档案Id
		 var operationHistoryValue = '2',traumaHistoryValue = '2',bloodTransfusionHistoryValue = '2';
		 var variable = "";
		 var roleId = window.utils.getCookie("roleId");
		 var managerId = window.utils.getCookie("managerId");
		 if(!!documentId){//修改
			 getData()
		 }else{//新增
			/*loading()*/
			var guid = window.utils.guid();
			$("#documentId").val(guid);
			if(roleId == "1"){
				getDoctorOrg();
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
	        				getDoctors(orginId,managerId);
	        				loading(orginId)
	        			}
	        		}
			 })
			 
		}
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
	                        var item = list[num];
                    		if(item.templateType == "1"){
                    			variable = JSON.parse(item.variable)
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
		                				html += '<input element-type="5" type="radio" name="variable'+i+'" data-id="'+dictionaryItem[j].itemId+'" lay-skin="primary" title="'+dictionaryItem[j].itemName+'">'
		                			}
		                				break;
		                	}
	                    	html +='</div></div>'
	                    }
	                    $("#otherMessage").append(html);
	                    form.render()
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
							 form.render();
						 }
					 }
				},
				error : function(res){
					layer.msg("查询建档医生失败")
				}
			 })
		 }
    	 /*查询机构*/
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
			 var orginId = data.value;
			 if(!documentId){
				 loading(orginId)
			 }
			 getDoctors(orginId)
		 });
		 function getData(){
			 $.ajax({
			 	url: '/xl/queryOnePersonDocument?id='+documentId,
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
						var item = JSON.parse(data.variable).variable;
						variable = JSON.parse(data.variable);
						var html = "";
	                    for(var i = 0;i < item.length;i++){
	                    	if(item[i].signOrginName == data.documentOrg){
	                    		html += '<div class="layui-form-item variable-element" variable-type="'+item[i].propertyType+'">';
	                    	}else{
	                    		variable.variable.splice(i,1)
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
		 $("#goBack").on("click",function(){
			 history.back(-1)
		 })
		 function saveInfo(){
			 var obj = new Object();
			 var url = "";
			 if(!!documentId){//修改
				 obj.id = documentId
				 url = '/xl/updatePersonDocument'
			 }else{//新增
				 url = '/xl/registPersonDocument'
			 }
			 obj.name = $("#name").val()
			 if(!obj.name){
				 layer.msg("姓名不能为空！");
				 return false
			 }
			 obj.sex = $("#sex").val()
			 obj.cardType = $("#cardType").val()
			 obj.cardNum = $("#cardNum").val()
			 if(!obj.cardNum){
				 layer.msg("身份证号不能为空！");
				 return false
			 }
			 var reg = /^[1-9]\d{5}(18|19|2([0-9]))\d{2}(0[0-9]|10|11|12)([0-2][1-9]|30|31)\d{3}[0-9Xx]$/;
			 /*if(!reg.test(cardNum)){
				 layer.msg("请输入正确的18位身份证号！");
				 return false
			 }*/
			 obj.birthday = $("#birthday").val()
			 obj.mobile = $("#mobile").val()
			 obj.healthCard = $("#healthCard").val()
			 obj.documentId = $("#documentId").val()
			 if(!obj.documentId){
				 layer.msg("档案编号不能为空！");
				 return false
			 }
			 obj.newFarmersCard = $("#newFarmersCard").val()
			 obj.medicalInsuranceCard = $("#medicalInsuranceCard").val()
			 
			 obj.provinceCode = $("#provinceCode").val()
			 obj.cityCode = $("#cityCode").val()
			 obj.townCode = $("#townCode").val()
			 
			 obj.residentialAddress = $("#residentialAddress").val()
			 obj.permanentAddress = $("#permanentAddress").val()
			 obj.workUnit = $("#workUnit").val()
			 obj.familyRelations = $("#familyRelations").val()
			 obj.contactsName = $("#contactsName").val()
			 obj.contactsMobile = $("#contactsMobile").val()
			 obj.livingState = $("#livingState").val()
			 obj.livingType = $("#livingType").val()
			 if(!obj.livingType){
				 layer.msg("请选择居住类型！");
				 return false
			 }
			 obj.registeredResidence = $("#registeredResidence").val()
			 obj.arriveDate = $("#arriveDate").val()
			 obj.country = $("#country").val()
			 obj.nation = $("#nation").val()
			 obj.bloodType = $("#bloodType").val()
			 obj.rh = $("#rh").val()
			 obj.degreeEducation = $("#degreeEducation").val()
			 obj.occupation = $("#occupation").val()
			 obj.marriageStatus = $("#marriageStatus").val()
			 obj.dateMarriage = $("#dateMarriage").val()
			 obj.documentDoctor = $("#documentDoctor").val();
			 var signOrginName = $("#signOrginName").val();
			 if(!signOrginName){
				 layer.msg("建档机构不能为空！");
				 return false
			 }
			 if(!obj.documentDoctor){
				 layer.msg("建档医生不能为空！");
				 return false
			 }
			 obj.documentDate = $("#documentDate").val()
			 //其他信息
			 obj.medicalCosts = $("#medicalCosts").val()
			 obj.medicalCardNumber = $("#medicalCardNumber").val()
			 obj.lowInsuranceCar = $("#lowInsuranceCar").val()//医保卡号
			 obj.drugAllergyHistory = $("#drugAllergyHistory").val()
			 obj.pastHistory = $("#pastHistory").val()
			 obj.motherMedicalHistory = $("#motherMedicalHistory").val()
			 obj.fatherMedicalHistory = $("#fatherMedicalHistory").val()
			 obj.childrenMedicalHistory = $("#childrenMedicalHistory").val()
			 obj.brotherMedicalHistory = $("#brotherMedicalHistory").val()
			 obj.heredityHistory = $("#heredityHistory").val()//遗传病史
			 obj.isDisability = $("#isDisability").val()
			 obj.disabilityCard = $("#disabilityCard").val()
			 obj.exposeHistory = $("#exposeHistory").val()
			 // 有无手术史   有无外伤史   有无输血史
			 if(operationHistoryValue == '1'){
				 var operationHistory = []
				 $(".sss").each(function(){
					 var obj = new Object();
					 obj.name = $(this).children(".layui-input-inline").children("input").eq('0').val()
					 obj.time = $(this).children(".layui-input-inline").children("input").eq('1').val()
					 operationHistory.push(obj)
				 })
				 obj.operationHistory = JSON.stringify(operationHistory)
			 }else{
				 obj.operationHistory='';
			 }
			 if(traumaHistoryValue == '1'){
				 var traumaHistory = []
				 $(".wss").each(function(){
					 var obj = new Object();
					 obj.name = $(this).children(".layui-input-inline").children("input").eq('0').val()
					 obj.time = $(this).children(".layui-input-inline").children("input").eq('1').val()
					 traumaHistory.push(obj)
				 })
				 obj.traumaHistory = JSON.stringify(traumaHistory)
			 }else{
				 obj.traumaHistory='';
			 }
			 if(bloodTransfusionHistoryValue == '1'){
				 var bloodTransfusionHistory = []
				 $(".sxs").each(function(){
					 var obj = new Object();
					 obj.name = $(this).children(".layui-input-inline").children("input").eq('0').val()
					 obj.time = $(this).children(".layui-input-inline").children("input").eq('1').val()
					 bloodTransfusionHistory.push(obj)
				 })
				 obj.bloodTransfusionHistory = JSON.stringify(bloodTransfusionHistory)
			 }else{
				 obj.bloodTransfusionHistory='';
			 }
			 
			 obj.kitchenExhaust = $("#kitchenExhaust").val()
			 obj.fuelType = $("#fuelType").val()
			 obj.water = $("#water").val()
			 obj.toilet = $("#toilet").val()
			 obj.livestock = $("#livestock").val()
			 obj.remarks = $("#remarks").val()
			 obj.keyCrowdType = '';
			 $("input[name='keyCrowdType']").each(function(){
				 if($(this).next('div').attr("class").indexOf('layui-form-checked')>=0){
					 if(obj.keyCrowdType){
						 obj.keyCrowdType+=","
					 }
					 obj.keyCrowdType+=$(this).attr("data-id")
				 }
			 })
			 var age = window.utils.getAge(obj.cardNum)
			 if(age < 7){
				 if(obj.keyCrowdType){
					 obj.keyCrowdType+=","
				 }
				 obj.keyCrowdType+="6"
			 }else if(age > 60){
				 if(obj.keyCrowdType){
					 obj.keyCrowdType+=","
				 }
				 obj.keyCrowdType+="1"
			 }
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
				 		break;
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
						layer.msg("个人信息登记成功",{time: 2000},function(){
							window.location.href="serviceLogin.html"
						})
					}else{
						layer.msg(res.resultCode.message)
					}
				},
				error : function(res){
					layer.msg("个人信息登记失败")
				}
			 })
		 }
		 form.on('radio(sss)', function(data){
			 operationHistoryValue = data.value;
			 if(data.value == '1'){
				 $(".sss").show()
			 }else{
				 $(".sss").hide()
			 }
		 }); 
		 form.on('radio(wss)', function(data){
			 traumaHistoryValue = data.value;
			 if(data.value == '1'){
				 $(".wss").show()
			 }else{
				 $(".wss").hide()
			 }
		 }); 
		 form.on('radio(sxs)', function(data){
			 bloodTransfusionHistoryValue = data.value;
			 if(data.value == '1'){
				 $(".sxs").show()
			 }else{
				 $(".sxs").hide()
			 }
		 }); 
		 form.on('radio(isLowInsurance)', function(data){
			 if(data.value == '2'){
				 $("#lowInsuranceCar").val("")
				 $("#lowInsuranceCar").attr("disabled","disabled")
				 form.render()
			 }else{
				 $("#lowInsuranceCar").removeAttr("disabled")
			 }
		 }); 
		 form.on('radio(haveMedicalHistory)', function(data){
			 if(data.value == '2'){
				 $("#heredityHistory").val("")
				 $("#heredityHistory").attr("disabled","disabled")
				 form.render()
			 }else{
				 $("#heredityHistory").removeAttr("disabled")
			 }
		 }); 
		 /*保存*/
		 $("#save").click(function(){
			 saveInfo()
		 })
	 })
})
