$(function(){
	 layui.use(['element','layer','jquery','form','laydate'],function(){
		 var $=layui.jquery,layer = layui.layer, laydate = layui.laydate,form = layui.form(),element = layui.element;
		 var documentId = window.utils.getRequestParam("documentId");//档案Id
		 var operationHistoryValue = '2',traumaHistoryValue = '2',bloodTransfusionHistoryValue = '2';
		 /*查询所有国籍*/
		 $.ajax({
		 	url: '/address/getAllCountry',
			type: 'get',
			success: function(res){
				if(res.successful && res.status == '200'){
					var str = '',data = res.data;
					if(!!data && data.length>0){
						for(var i = 0;i < data.length;i++){
							if(data[i].nameZh == "中国"){
								str +='<option value="'+data.id+'" selected="selected">'+data[i].nameZh+'</option>'
							}else{
								str +='<option value="'+data.id+'">'+data[i].nameZh+'</option>'
							}
						}
					}
					$("#country").html(str)
					form.render()
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
			 success: function(res){
				 if(res.successful && res.status == '200'){
					 var str = '',data = res.data;
					 if(!!data && data.length>0){
						 for(var i = 0;i < data.length;i++){
							 if(data[i].nameZh == "汉族"){
								str +='<option value="'+data.id+'" selected="selected">'+data[i].mzName+'</option>'
							}else{
								str +='<option value="'+data.id+'">'+data[i].mzName+'</option>'
							}
						 }
					 }
					 $("#nation").html(str)
					 form.render()
				 }else{
					 layer.msg(res.resultCode.message)
				 }
			 },
			 error : function(res){
				 layer.msg("查询国籍失败")
			 }
		 })
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
		 if(!!documentId){//修改
			 getData()
		 }else{//新增
			var guid = window.utils.guid();
			$("#documentId").val(guid);
			getOrginList('')
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
						$("#drugAllergyHistory").val(data.drugAllergyHistory)
						$("#pastHistory").val(data.pastHistory)
						$("#motherMedicalHistory").val(data.motherMedicalHistory)
						$("#fatherMedicalHistory").val(data.fatherMedicalHistory)
						$("#childrenMedicalHistory").val(data.childrenMedicalHistory)
						$("#brotherMedicalHistory").val(data.brotherMedicalHistory)
						$("#heredityHistory").val(data.heredityHistory)
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
			 obj.documentDoctor = $("#documentDoctor").val()
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
				 $("#lowInsuranceCar").attr("disabled","disabled")
			 }else{
				 $("#lowInsuranceCar").removeAttr("disabled")
			 }
		 }); 
		 form.on('radio(haveMedicalHistory)', function(data){
			 if(data.value == '2'){
				 $("#heredityHistory").attr("disabled","disabled")
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
