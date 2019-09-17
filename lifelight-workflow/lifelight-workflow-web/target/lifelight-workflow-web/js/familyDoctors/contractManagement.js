$(function(){
    layui.use(['laypage', 'layer','form','laydate'], function () {
        var $ = layui.jquery;
        var laypage = layui.laypage, layer = layui.layer, laydate = layui.laydate, form = layui.form();
        form.render('select')
        var importPeople = {}
        var tabRoleId =""
    	var projectType = ""
        var start = {
    	    min: '1900-01-01'
    	    ,max: '2049-06-16'
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
    		obj.inputPerson = $("#inputPerson").val()
    		obj.name = $("#name").val()
    		obj.healthCard = $("#healthCard").val()
    		obj.cardNum = $("#cardNum").val()
    		obj.year = $("#year").val()=="all"?"":$("#year").val()
    		obj.signStatus = $("#signStatus").val()
    		obj.auditStatus = $("#auditStatus").val()
    		obj.signStartTime = $("#signStartTime").val()
    		obj.signEndTime = $("#signEndTime").val()
    		obj.servicePackage = $("#servicePackage").val()
    		obj.isPayment = $("#isPayment").val()
    		obj.medicalType = $("#medicalType").val()
    		obj.finishStatus = $("#finishStatus").val()
    		obj.signPersonType = type
    		obj.showCount = '10'
    		obj.currentPage = currentPage
    		$.ajax({
    			url: '/xl/queryAllContract',
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
	        					str += '<td>'+getFamilyRelations(item.familyRelations)+'</td>' //与户主关系
	        					var sex = item.sex=='1'?'男':'女'
	        					str += '<td>'+sex+'</td>'
	        					str += '<td>'+item.cardNum+'</td>'
	        					var residentialAddress = !item.residentialAddress?"":item.residentialAddress
	        					str += '<td>'+residentialAddress+'</td>'
	        					str += '<td>'+getKeyCrowd(item.keyCrowdType)+'</td>'
	        					var mobile = !item.mobile?"":item.mobile
	        					str += '<td>'+mobile+'</td>'
	        					var signStatus = item.signStatus == '1'?'签约':'终止签约'
	        					str += '<td>'+signStatus+'</td>'
	        					str += '<td>'+item.signStartDate+'至'+item.signEndDate+'</td>'
	        					var signDate = !!item.signDate?item.signDate:'';
	        					str += '<td>'+signDate+'</td>'
	        					var signOrgName = !item.signOrgName?"":item.signOrgName
	        					str += '<td>'+signOrgName+'</td>'
        						str += '<td><button class="layui-btn layui-btn-small edit" data-id="'+item.id+'">修改</button></td>'
        						str += '<td><button class="layui-btn layui-btn-small login" data-name="'+item.name+'" data-id="'+item.id+'">登记</button></td>'
    							str += '<td>'
								if(item.signStatus == '1'){
									str += '<button class="layui-btn layui-btn-small keppOn" data-id="'+item.id+'">续签</button>';
									str += '<button class="layui-btn layui-btn-small stop" data-id="'+item.id+'">终止签约</button>';
								}else{
									str += '<button class="layui-btn layui-btn-small cancel" data-id="'+item.id+'">撤销终止</button>';
								}
	        					str += '<button class="layui-btn layui-btn-small layui-btn-danger deleteSchedule" data-id="'+item.id+'">删除</button>';
								str += '</td></tr>';
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
		        			str += '<tr><td colspan="14" align="center">暂无数据</td></tr>'
		        		}
	        			$(".list-table tbody").html(str);
        				form.render();
	        		}
				},
				erroe: function(res){
					$('.list-table tbody').html('<tr><td colspan="14" align="center">列表获取失败</td></tr>');
				}
    		})
    	}
    	/*   修改   */
    	$("table").delegate(".edit","click",function(){
    		var id=$(this).attr("data-id")
    		window.location.href="serviceSign.html?documentId="+id+"&type=edit"
    	})
    	/*   续签   */
    	$("table").delegate(".keppOn","click",function(){
    		var id=$(this).attr("data-id")
    		updateContractStatus(id,'change',3)
    	})
    	/*   终止签约   */
    	$("table").delegate(".stop","click",function(){
    		var id=$(this).attr("data-id")
    		updateContractStatus(id,'change',2)
    	})
    	/*   取消终止  */
    	$("table").delegate(".cancel","click",function(){
    		var id=$(this).attr("data-id")
    		updateContractStatus(id,'change',1)
    	})
    	/*   删除  */
    	$("table").delegate(".deleteSchedule","click",function(){
    		var id=$(this).attr("data-id")
    		updateContractStatus(id,'delete',0)
    	})
    	/*签约检查进度信息填写*/
    	$("#handleInfo").delegate("#addform","click",function(){
    		var id=$("#personScheduleId").val()
    		var managerId = window.utils.getCookie("managerId");
    		var projectValue = {
    			'name':$("#info_name").val(),
    			'sex':$("#info_sex").val(),
    			'project_type_content':$("#info_project_type_desc").val(),
    			'time':$("#info_time").val(),
    			'project_type_desc':$("#info_project_type_content").val(),
    			'remark':$("#info_remark").val(),
    			'result':'',
    			'certificationsUrl' : tempImagesUrl.join(';')
    		}
    		var projectObj = {}
    		switch(projectType){
	    		case "BG":
	    			projectObj.xuetang = $("#xuetang").val();
	    		  break;
	    		case "RU":
	    			projectObj.RU_LEU = $("#RU_LEU").val();
	    			projectObj.RU_Pro = $("#RU_Pro").val();
	    			projectObj.RU_Glu = $("#RU_Glu").val();
	    			break;
	    		case "ECG":
	    			projectObj.ST_T = $("#ST_T").val();
	    			break;
	    		case "ACF":
	    			projectObj.ACF_fxz = $("#ACF_fxz").val();
	    			break;
	    		case "FO":
	    			projectObj.FO_gdyp = $("#FO_gdyp").val();
	    			break;
	    		case "BF":
	    			projectObj.BF_CHOL = $("#BF_CHOL").val();
	    			projectObj.BF_HDL_C = $("#BF_HDL_C").val();
	    			projectObj.BF_TG = $("#BF_TG").val();
	    			projectObj.BF_LDL_C = $("#BF_LDL_C").val();
	    			break;
	    		case "LF":
	    			projectObj.TBLL = $("#TBLL").val();
	    			projectObj.ALT = $("#ALT").val();
	    			projectObj.AST = $("#AST").val();
	    			break;
	    		case "RF":
	    			projectObj.BUN = $("#BUN").val();
	    			projectObj.Gr = $("#Gr").val();
	    			break;
	    		case "YTJ":
	    			projectObj.YTJ_ru = $("#YTJ_ru").val();
	    			projectObj.YTJ_xuetang = $("#YTJ_xuetang").val();
	    			projectObj.YTJ_SPO2 = $("#YTJ_SPO2").val();
	    			projectObj.YTJ_ecg = $("#YTJ_ecg").val();
	    			projectObj.YTJ_bf = $("#YTJ_bf").val();
	    			break;
	    		case "FI":
	    			projectObj.FI_jtxj = $("#FI_jtxj").val();
	    			break;
	    		case "SUA":
	    			projectObj.xuetang = $("#SUA_ua").val();
	    			break;
	    		case "BR":
	    			projectObj.BR_br = $("#BR_br").val();
	    			break;
	    		case "DB":
	    			projectObj.DB_man = $("#DB_man").val();
	    			break;
	    		case "OB":
	    			projectObj.OB_other = $("#OB_other").val();
	    			break;
	    		case "CB":
	    			projectObj.xuetang = $("#xuetang").val();
	    			break;
	    		case "BG":
	    			projectObj.CB_women = $("#CB_women").val();
	    			break;
	    		case "BP":
	    			projectObj.BP_info = $("#BP_info").val();
	    			break;
	    		case "FC":
	    			projectObj.FC_pt = $("#FC_pt").val();
	    			projectObj.FC_APTT = $("#FC_APTT").val();
	    			projectObj.FC_TT = $("#FC_TT").val();
	    			projectObj.FC_FIB = $("#FC_FIB").val();
	    			break;
	    		case "MI":
	    			projectObj.BP_info = $("#MI_CTnI").val();
	    			projectObj.MI_CKMB = $("#MI_CKMB").val();
	    			projectObj.MI_Myoo = $("#MI_Myoo").val();
	    			break;
	    		case "TC":
	    			projectObj.TC_TC = $("#TC_TC").val();
	    			break;
	    		case "BGC":
	    			projectObj.BGC_xuetang = $("#BGC_xuetang").val();
	    			break;
	    		case "HBA1C":
	    			projectObj.HBA1C_HBAIC = $("#HBA1C_HBAIC").val();
	    			break;
    		}
    		projectValue.result = JSON.stringify(projectObj) 
    		var obj = new Object();
    		obj.id = id;
    		obj.inputPerson = managerId;
    		obj.projectValue = JSON.stringify(projectValue);
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
    	/*登记或修改检查信息 按钮事件*/
    	$("#infoSign").delegate(".handleInfo","click",function(){
    		var id=$(this).attr("data-id")
    		$("#personScheduleId").val(id);
    		layer.open({
	    		type:1
	    		,title:'需要录入的检查项目'
	    		,content: $('#handleInfo')
				,area: ['900px','90%']
	    	})
	    	projectType = $(this).attr("data-type");
    		$(".project").hide()
	    	$("."+projectType).show();
    		var dataValue = JSON.parse($(this).attr("data-value"));
    		if(!dataValue){
    			return
    		}
    		var resultStr = dataValue.result;
    		$("#info_name").val(dataValue.name);
    		$("#info_sex").val(dataValue.sex);
    		$("#info_project_type_content").val(dataValue.project_type_content);
    		$("#info_time").val(dataValue.time);
    		$("#info_project_type_desc").val(dataValue.project_type_desc);
    		$("#info_remark").val(dataValue.remark);
    		$('.blockImages').html("");
			loadImage(dataValue.certificationsUrl);
    		if(!resultStr){
    			form.render()
    			return
    		}
    		var result = JSON.parse(resultStr);
    		switch(projectType){
	    		case "BG":
	    			$("#xuetang").val(result.xuetang);
	    		  break;
	    		case "RU":
	    			$("#RU_LEU").val(result.RU_LEU);
	    			$("#RU_Pro").val(result.RU_Pro);
	    			$("#RU_Glu").val(result.RU_Glu);
	    			break;
	    		case "ECG":
	    			$("#ST_T").val(result.ST_T);
	    			break;
	    		case "ACF":
	    			$("#ACF_fxz").val(result.ACF_fxz);
	    			break;
	    		case "FO":
	    			$("#FO_gdyp").val(result.FO_gdyp);
	    			break;
	    		case "BF":
	    			$("#BF_CHOL").val(result.BF_CHOL);
	    			$("#BF_HDL_C").val(result.BF_HDL_C);
	    			$("#BF_TG").val(result.BF_TG);
	    			$("#BF_LDL_C").val(result.BF_LDL_C);
	    			break;
	    		case "LF":
	    			$("#TBLL").val(result.TBLL);
	    			$("#ALT").val(result.ALT);
	    			$("#AST").val(result.AST);
	    			break;
	    		case "RF":
	    			$("#BUN").val(result.BUN);
	    			$("#Gr").val(result.Gr);
	    			break;
	    		case "YTJ":
	    			$("#YTJ_ru").val(result.YTJ_ru);
	    			$("#YTJ_xuetang").val(result.YTJ_xuetang);
	    			$("#YTJ_SPO2").val(result.YTJ_SPO2);
	    			$("#YTJ_ecg").val(result.YTJ_ecg);
	    			$("#YTJ_bf").val(result.YTJ_bf);
	    			break;
	    		case "FI":
	    			$("#FI_jtxj").val(result.FI_jtxj);
	    			break;
	    		case "SUA":
	    			$("#SUA_ua").val(result.SUA_ua);
	    			break;
	    		case "BR":
	    			$("#BR_br").val(result.BR_br);
	    			break;
	    		case "DB":
	    			$("#DB_man").val(result.DB_man);
	    			break;
	    		case "OB":
	    			$("#OB_other").val(result.OB_other);
	    			break;
	    		case "CB":
	    			$("#xuetang").val(result.xuetang);
	    			break;
	    		case "BG":
	    			$("#CB_women").val(result.CB_women);
	    			break;
	    		case "BP":
	    			$("#BP_info").val(result.BP_info);
	    			break;
	    		case "FC":
	    			$("#FC_pt").val(result.FC_pt);
	    			$("#FC_APTT").val(result.FC_APTT);
	    			$("#FC_TT").val(result.FC_TT);
	    			$("#FC_FIB").val(result.FC_FIB);
	    			break;
	    		case "MI":
	    			$("#MI_CTnI").val(result.MI_CTnI);
	    			$("#MI_CKMB").val(result.MI_CKMB);
	    			$("#MI_Myoo").val(result.MI_Myoo);
	    			break;
	    		case "TC":
	    			$("#TC_TC").val(result.TC_TC);
	    			break;
	    		case "BGC":
	    			$("#BGC_xuetang").val(result.BGC_xuetang);
	    			break;
	    		case "HBA1C":
	    			$("#HBA1C_HBAIC").val(result.HBA1C_HBAIC);
	    			break;
			}
    		form.render();
    		
    	})
    	/*    登记*/
    	$("table").delegate(".login","click",function(){
    		var id=$(this).attr("data-id")
    		var name=$(this).attr("data-name")
    		$.ajax({
	    		url: "/xl/queryPersonSchedule?documentId="+id,
	    		type: "get",
	    		success : function(res){
	    			var data = res.data;
	    			if(res.data && res.data.length>0){
	    				var str ="";
	    				$.each(data,function(index,item){
	    					str += "<tr>";
	    					str += "<td>"+name+"</td>";
	    					str += "<td>"+item.servicePackageDesc+"</td>";
	    					str += "<td>"+item.projectTypeDesc+"</td>";
	    					var checkTime = !item.checkTime ? "":item.checkTime
	    					str += "<td>"+checkTime+"</td>";
	    					var checkOrg = !item.checkOrg ? "":item.checkOrg
	    					str += "<td>"+checkOrg+"</td>";
	    					var inputPerson = !item.inputPerson ? "":item.inputPerson
	    					str += "<td>"+inputPerson+"</td>";
	    					var project = ''
	    					if(!item.projectValue){
	    						var projectValue = new Object();
	    						projectValue.name = name;
	    						projectValue.sex = item.sex;
	    						projectValue.project_type_content = item.projectTypeDesc;
	    						projectValue.time = '';
	    						projectValue.project_type_desc = '';
	    						projectValue.remark = '';
	    						projectValue.result = '';
	    						project = JSON.stringify(projectValue)
	    					}else{
	    						var projectValue = JSON.parse(item.projectValue)
	    						projectValue.name = name
	    						projectValue.sex = item.sex
	    						projectValue.project_type_content = item.projectTypeDesc;
	    						project = JSON.stringify(projectValue)
	    					}
	    					str += "<td><button class='layui-btn layui-btn-small handleInfo' data-value='"+project+"' data-id='"+item.id+"' data-type='"+item.projectType+"'>登记或修改检查信息</button>" +
	    							"<button class='layui-btn layui-btn-small layui-btn-danger delete' data-id='"+item.id+"'>删除</button></td>";
	    					str += "</tr>";
	    				})
    					$("#infoSign tbody").html(str)
    					layer.open({
				    		type:1
				    		,title:'需要录入的检查项目'
				    		,content: $('#infoSign')
							,area: ['100%','100%']
				    	})
				    	$("#infoSign tbody .delete").on("click",function(){
				    		var id = $(this).attr("data-id")
				    		$.ajax({
					    		url: "/xl/deletePersonSchedule?id="+id,
					    		type: "get",
					    		success : function(res){
				    				layer.msg(res.resultCode.message,{time: 1000},function(){
				    					window.location.reload()
				    				})
					    		},
					    		error : function(data){
				    			    layer.msg('请求失败！')
					    		}
					    	})
				    	})
	    			}
	    		},
	    		error : function(data){
    			    layer.msg('请求失败！')
	    		}
	    	})
    	})
    	function updateContractStatus(id,type,data){
    		var url = ""
    		if(type == "change"){
    			url = "/xl/updateContractStatus?documentId="+id+"&signStatus="+data
    		}else{
    			url = "/xl/updateContractStatus?documentId="+id+"&inUse="+data
    		}
    		$.ajax({
	    		url: url,
	    		type: "get",
	    		success : function(res){
    				layer.msg(res.resultCode.message,{time: 1000},function(){
    					loadTable(tabRoleId,1)
    				})
	    		},
	    		error : function(data){
    			    layer.msg('请求失败！')
	    		}
	    	})
    	}
    	function getKeyCrowd(type){
    		var auditStatus = [
    				{id:'1',value:'老年人'},
    				{id:'2',value:'高血压'},
    				{id:'3',value:'糖尿病'},
    				{id:'4',value:'重性精神病'},
    				{id:'5',value:'孕产妇'},
    				{id:'6',value:'儿童'}
    		]
    		var itemName = '';
    		var typeList = type.split(",");
    		$.each(auditStatus,function(index,item){
    			for(var i =0;i<typeList.length;i++){
    				if(typeList[i] == item.id){
    					if(itemName){
    						itemName+=","
    					}
        				itemName+=item.value
        			}
    			}
    		})
    		return itemName
    	}
    	function getFamilyRelations(type){
    		var auditStatus = [
    			{id:'1',value:'本人或户主'},
    			{id:'2',value:'配偶'},
    			{id:'3',value:'子'},
    			{id:'4',value:'女'},
    			{id:'5',value:'孙子、孙女或外孙子、外孙女'},
    			{id:'6',value:'父母'},
    			{id:'7',value:'祖父母或外祖父母'},
    			{id:'8',value:'兄、弟、姐、妹'},
    			{id:'9',value:'其他'}
    			]
    		var value = '';
    		$.each(auditStatus,function(index,item){
    			if(type == item.id){
    				value = item.value
    			}
    		})
    		return value
    	}
    	/*导出*/
    	$('#exportBtn').on('click',function(){
        	var query='?';
        	var inputPerson=$.trim($('#inputPerson').val());
        	var name=$.trim($('#name').val());
        	var healthCard=$.trim($('#healthCard').val());
        	var cardNum=$.trim($('#cardNum').val());
        	var year=$('#year').val()=='all'?'':$('#year').val();
        	var signStatus=$('#signStatus').val();
        	var auditStatus=$('#auditStatus').val();
        	var signStartTime=$('#signStartTime').val();
        	var signEndTime=$('#signEndTime').val();
        	var servicePackage=$('#servicePackage').val();
        	var isPayment=$('#isPayment').val();
        	var medicalType=$('#medicalType').val();
        	var finishStatus=$('#finishStatus').val();
        	if(inputPerson) query+='inputPerson='+inputPerson+'&';
        	if(name) query+='name='+name+'&';
        	if(healthCard) query+='healthCard='+healthCard+'&';
        	if(cardNum) query+='cardNum='+cardNum+'&';
        	if(year) query+='year='+year+'&';
        	if(signStatus) query+='signStatus='+signStatus+'&';
        	if(auditStatus) query+='auditStatus='+auditStatus+'&';
        	if(signStartTime) query+='signStartTime='+signStartTime+'&';
        	if(signEndTime) query+='signEndTime='+signEndTime+'&';
        	if(servicePackage) query+='servicePackage='+servicePackage+'&';
        	if(isPayment) query+='isPayment='+isPayment+'&';
        	if(medicalType) query+='medicalType='+medicalType+'&';
        	if(finishStatus) query+='finishStatus='+finishStatus+'&';
        	console.log(query)
        	$(this).attr('href','/xl/exportAllContract'+query);
        })
        var tempImagesUrl = [];
        /*图片上传*/
        $('#aptitude').change(function(e) {
			if((tempImagesUrl && tempImagesUrl.length >= 5)) {
				return layer.msg('已达到文件上限，无法上传');
			}
			if((this.files.length + tempImagesUrl.length) > 5) {
				var _this = this;
				var num = this.files.length - (5 - tempImagesUrl.length);
				return layer.msg('已达到文件上限，超出' + num + '张，最多支持上传五张图片。请重新选择', function() {
					/* _this.files = _this.files.splice(0,num);
					var newFiles = {};
					for (var i = 0, file; file = _this.files[i]; i++) {
						if(i>=num){return;}
						newFiles[i] = file;
					} */
				});
			}
			if(!this.files || this.files.length == 0) {
				return;
			}
			for(var i = 0, file; file = this.files[i]; i++) {
				var fileUrl = $.trim(file.name);
				var extStart = fileUrl.lastIndexOf(".");
				var ext = fileUrl.substring(extStart, fileUrl.length).toUpperCase();
				if(fileUrl == '') {
					layer.msg('请选择图片上传！');
					return;
				} else if(ext != '.PNG' && ext != '.GIF' && ext != '.BMP' && ext != '.JPG') {
					layer.msg('图片格式不支持，请重新选择上传！');
					return
				}
			}
			var that = this;
			imgUpload(this, function(data) {
				if(data.successful) {
					var temp = data.data ? data.data.split(';') : [];
					if(temp && temp[temp.length - 1] === "") {
						temp.pop();
					}
					if(tempImagesUrl.length > 0) {
						tempImagesUrl = tempImagesUrl.concat(temp);
					} else {
						tempImagesUrl = temp;
					}
					var html = '';
					var style = 'display:inline-block;vertical-align: middle;width:95px;height:95px;text-align:center;'
					console.log(that.files);
					for(var i = 0, file; file = that.files[i]; i++) {
						if(that.files && file) {
							html += '<div style="' + style + '"><img style="width: 100%;height:100%;" src="' + window.URL.createObjectURL(file) + '"/><i class="layui-icon" style="font-size: 30px; color: #1E9FFF;" icon-num="'+i+'">&#xe640;</i></div>';
						} else {
							html += '<div style="' + style + '"><img style="width: 100%;height:100%;" src="' + file.value + '"/><i class="layui-icon" style="font-size: 30px; color: #1E9FFF;" icon-num="'+i+'">&#xe640;</i></div>';
						}
					}
					delAndEnlarge(html,tempImagesUrl);
				} else {
					layer.msg("图片上传到服务器失败！");
				}
			}, function(error) {
				layer.msg("图片上传到服务器失败");
				console.log(error);
			});
		});
    	// 删除，放大
	    function delAndEnlarge(html,tempImagesUrl){
	    	html && $('.blockImages').append(html);
			$('.blockImages div img').bind('click',function(e){
				layer.photos({
				    photos: $(this).parent()
				    ,anim: 5 
				});
			});
			$('.blockImages div i.layui-icon').bind('click',function(e){
				var _this = this;
				layer.confirm('您确定要删除此张图片?',{title:'警告'}, function(index){
					$('.blockImages div')[$(_this).attr('icon-num')].remove();
					tempImagesUrl.splice($(_this).attr('icon-num'),1);
					$('.blockImages div i.layui-icon').each(function(key,val){
						$(this).attr('icon-num',key);
					});
				    layer.close(index);
				});
			});
			/*if(state == 'PEDING'){
				$('.layui-show i.layui-icon').css('display','none');
			}*/
	    }
	    // 上传图片
		function imgUpload(that, cb, errFn) {
			var formData = new FormData();
			for(var i = 0, file; file = that.files[i]; i++) {
				formData.append('file',file);
			}
			$.ajax({
				type: "POST",
				url: "/file/upload",
				data: formData,
				async: false,
				cache: false,
				processData: false,
				contentType: false,
				dataType: "json",
				success: function(data) {
					cb(data);
				},
				error: function(error) {
					errFn(error);
				}
			});
		};
		// 加载图片
		function loadImage(certificationsUrl) {
			// data.data.imageUrl = 'http://res.layui.com/images/fly/layim.jpg';
			// data.data.certificationsUrl = 'http://res.layui.com/images/fly/fly.jpg;http://res.layui.com/images/fly/layim.jpg;http://res.layui.com/images/fly/layim.jpg;http://res.layui.com/images/fly/layim.jpg;http://res.layui.com/images/fly/layim.jpg';
			if(certificationsUrl){
				var certificationsUrlArr = certificationsUrl ? certificationsUrl.split(';') : [];
				if(certificationsUrlArr && certificationsUrlArr[certificationsUrlArr.length - 1] === "") {
					certificationsUrlArr.pop();
				}
				tempImagesUrl = certificationsUrlArr;
				var html = '';
				var style = 'display:inline-block;vertical-align: middle;width:95px;height:95px;text-align:center;position:relative;margin:15px 0 30px 0';
				$.each(certificationsUrlArr,function(key,value){
					html += '<div style="' + style + '"><img style="width: 100%;height:100%;" src="' + value + '"/><i class="layui-icon" style="font-size: 30px; color: #1E9FFF;position: absolute;left: 32px;bottom: -35px;z-index: 1;" icon-num="'+key+'">&#xe640;</i></div>';
				})
				delAndEnlarge(html,tempImagesUrl);
			}else{
				$('.blockImages').html("");
				tempImagesUrl = []
			}
		}
    })
})
