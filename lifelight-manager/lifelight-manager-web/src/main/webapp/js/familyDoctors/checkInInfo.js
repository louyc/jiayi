 layui.use(['element','layer','jquery','form','laydate'],function(){
	 var $=layui.jquery,layer = layui.layer, laydate = layui.laydate,form = layui.form(),element = layui.element;
	 var documentId = window.utils.getRequestParam("documentId");//档案Id
	 var realDocumentId = window.utils.getRequestParam("realDocumentId");//档案Id
	 var myType = window.utils.getRequestParam("type");//档案Id
	 var roleId = window.utils.getCookie("roleId");
	 var managerId = window.utils.getCookie("managerId");
	 if(myType == "2"){
		 $("#save").hide();
	 };
	 if(!!documentId){
		 loadding()
	 }
	 function loadding(){
		 $.ajax({
 			url: '/xl/queryOneScheduleItem?id='+documentId,
			type: 'get',
			success: function(res){
				var examinationInfo = JSON.parse(res.data.examinationInfo);
				$.each(examinationInfo,function(index,item){
					$("#"+index).val(item);
				})
				form.render()
			},
			error: function(res){
				layer.msg("查询失败")
			}
		})
	 }
	 $("#save").on("click",function(){
		 var param = new Object();
		 $("#saveInfo .layui-tab-item input").each(function(){
			 if($(this).attr("class").indexOf("layui-unselect")<0){
				 var key = $(this).attr("id");
				 param[key] = $(this).val();
			 }
		 })
		 $("#saveInfo .layui-tab-item select").each(function(){
			 var key = $(this).attr("id");
			 param[key] = $(this).val();
		 })
		 var obj = new Object();
    		obj.id = documentId;
    		obj.inputPerson = managerId;
    		if(myType == "1"){
    			obj.documentId =realDocumentId;
    		 }
    		obj.examinationInfo = JSON.stringify(param);
    		$.ajax({
    			url: '/xl/updatePersonSchedule',
				type: 'post',
				data: $.toJSON(obj),
				dataType: 'json',
				contentType: 'application/json',
				success: function(res){
					if(res.resultCode.code == "SUCCESS"){
						layer.msg("保存成功",{time: 2000},function(){
							window.location.reload()
						})
					}else{
						layer.msg(res.resultCode.message)
					}
				},
				error: function(res){
					layer.msg("保存失败")
				}
    		})
	 })
	 form.on('select(fanghu_last)', function(data){
		 if(data.value == "2"){
			 $(data.elem).parents(".layui-form-item").children(".layui-input-inline:last-child").children("input").removeAttr("disabled")
		 }else{
			 $(data.elem).parents(".layui-form-item").children(".layui-input-inline:last-child").children("input").attr("disabled",true)
		 }
	 });
	 form.on('select(fanghu_next)', function(data){
		 if(data.value == "2"){
			 $(data.elem).parent(".layui-input-inline").next(".layui-input-inline").children("input").removeAttr("disabled")
		 }else{
			 $(data.elem).parent(".layui-input-inline").next(".layui-input-inline").children("input").attr("disabled",true)
		 }
	 });
	 form.on('select(fanghu_next4)', function(data){
		 if(data.value == "4"){
			 $(data.elem).parent(".layui-input-inline").next(".layui-input-inline").children("input").removeAttr("disabled")
		 }else{
			 $(data.elem).parent(".layui-input-inline").next(".layui-input-inline").children("input").attr("disabled",true)
		 }
	 });
	 form.on('select(fanghu_next5)', function(data){
		 if(data.value == "5"){
			 $(data.elem).parent(".layui-input-inline").next(".layui-input-inline").children("input").removeAttr("disabled")
		 }else{
			 $(data.elem).parent(".layui-input-inline").next(".layui-input-inline").children("input").attr("disabled",true)
		 }
	 });
	 form.on('select(fanghu_next7)', function(data){
		 if(data.value == "7"){
			 $(data.elem).parent(".layui-input-inline").next(".layui-input-inline").children("input").removeAttr("disabled")
		 }else{
			 $(data.elem).parent(".layui-input-inline").next(".layui-input-inline").children("input").attr("disabled",true)
		 }
	 });
	 $("#height").on("blur",function(){
		 if(!!$("#weight").val() && !!$(this).val()){
			 var weight = parseFloat($("#weight").val());
			 var height = (parseFloat($(this).val())/100).toFixed(2);
			 var bmi = computeBMI(height,weight);
			 $("#BMI").val(bmi)
		 }
	 })
	 $("#weight").on("blur",function(){
		 if(!!$("#height").val() && !!$(this).val()){
			 var weight = parseFloat($(this).val());
			 var height = (parseFloat($("#height").val())/100).toFixed(2);
			 var bmi = computeBMI(height,weight);
			 $("#BMI").val(bmi)
		 }
	 })
	 //计算BMI
	 function computeBMI(hig,wei){
	      //转化入参
	      var dataBMI = ( wei / (hig * hig) ).toFixed(2);
	      return dataBMI;
     }
	 
 })
 function otherChoose(itemArr,obj){
	 
	 var that = $(obj);
	 var itemText = that.val()
	 var html = "";
	 $.each(itemArr,function(index,item){
		 if(itemText.indexOf(item) >= 0){
			 html += '<input type="checkbox" lay-skin="primary" data-id="'+index+'" lay-filter="choose" title="'+item+'" checked="">'
		 }else{
			 html += '<input type="checkbox" lay-skin="primary" data-id="'+index+'" lay-filter="choose" title="'+item+'">'
		 }
	 })
	 $("#addOptionText").val("");
	 $("#otherChoose .checkBoxDiv").html(html);
	 $("#addOption").on("click",function(){
		 itemText = that.val();
		 var addOptionText = $("#addOptionText").val();
		 if(!!addOptionText){
			 if(!!itemText){
				 itemText += ","
			 }
			 itemText += addOptionText;
			 that.val(itemText);
		 }
	 })
	 layui.use(['element','layer','jquery','form','laydate'],function(){
		 var $=layui.jquery,layer = layui.layer, laydate = layui.laydate,form = layui.form(),element = layui.element;
		 form.render()
		 layer.open({
    		type:1
    		,title:'选择'
    		,content: $('#otherChoose')
			,area: ['450px','200px']
    	})
    	form.on('checkbox(choose)', function(data){
    		itemText = that.val()
    		var title = $(data.elem).attr('title');
    		if(data.elem.checked){//选中
    			if(itemText.indexOf(title) < 0 ){
    				if(!!itemText){
    					itemText += ","
    				}
    				itemText += title;
    			}
    		}else{
    			
    			var title_more = ","+title;
    			itemText = itemText.replace(title_more,"")
    			itemText = itemText.replace(title,"")
    		}
    		that.val(itemText);
		});
	 })
 }

