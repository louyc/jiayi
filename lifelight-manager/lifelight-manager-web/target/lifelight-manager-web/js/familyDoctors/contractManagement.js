$(function(){
    layui.use(['laypage', 'layer','form','laydate'], function () {
        var $ = layui.jquery;
        var laypage = layui.laypage, layer = layui.layer, laydate = layui.laydate, form = layui.form();
        form.render('select')
        var managerId = window.utils.getCookie("managerId");
        var roleId = window.utils.getCookie("roleId");
        if(roleId == "1"){
        	var doctor_type = window.utils.getCookie("doctor_type");
        }else{
        	var doctor_type = "";
        }
        var documentId = "";
        var realDocumentId = "";
        var importPeople = {}
        var tabRoleId =""
    	var projectType = "";
        var myType = false;
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
    		obj.currentPage = currentPage;
    		/*if(roleId =="1" && doctor_type =="1"){
    			obj.signDoctorId = managerId
    		}*/
    		if(!(roleId =="4" || roleId =="6")){
    			obj.signDoctorId = managerId
    		}
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
	        					str += '<td><input type="checkbox" lay-skin="primary" data-phone="'+item.mobile+'" id="'+item.managerId+'"></td>'
	        					str += '<td>'+item.name+'</td>'
	        					str += '<td>'+getFamilyRelations(item.familyRelations)+'</td>' //与户主关系
	        					var sex = item.sex=='1'?'男':'女'
	        					str += '<td>'+sex+'</td>'
	        					str += '<td>'+item.cardNum+'</td>'
	        					var residentialAddress = !item.residentialAddress?"":item.residentialAddress
	        					str += '<td>'+residentialAddress+'</td>'
	        					if(!!getKeyCrowd(item.keyCrowdType)){
	        						str += '<td>'+getKeyCrowd(item.keyCrowdType)+'</td>'
	        					}else{
	        						str += '<td></td>'
	        					}
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
        						str += '<td><button class="layui-btn layui-btn-small login" data-name="'+item.name+'" data-id="'+item.id+'">登记</button>'
        						str += '<button class="layui-btn layui-btn-small healthyData" data-managerId="'+item.managerId+'">健康数据</button>'
        						/*str += '<button class="layui-btn layui-btn-small useProcess" data-managerId="'+item.managerId+'">服用流程</button>'*/
        						str += '<a href="userMessage.html?managerId='+item.managerId+'&userName='+item.name+'" class="layui-btn layui-btn-small layui-btn-normal shouMessage" data-managerId="'+item.managerId+'">查看消息</a>'
        						str += '</td>'
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
	        			$("#mainTable tbody").html(str);
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
					$('#mainTable tbody').html('<tr><td colspan="14" align="center">列表获取失败</td></tr>');
				}
    		})
    	}
    	/*   修改   */
    	$("#mainTable").delegate(".edit","click",function(){
    		var id=$(this).attr("data-id")
    		window.location.href="serviceSign.html?documentId="+id+"&type=edit"
    	})
    	/*   续签   */
    	$("#mainTable").delegate(".keppOn","click",function(){
    		var id=$(this).attr("data-id")
    		updateContractStatus(id,'change',3)
    	})
    	/*   终止签约   */
    	$("#mainTable").delegate(".stop","click",function(){
    		var id=$(this).attr("data-id")
    		updateContractStatus(id,'change',2)
    	})
    	/*   取消终止  */
    	$("#mainTable").delegate(".cancel","click",function(){
    		var id=$(this).attr("data-id")
    		updateContractStatus(id,'change',1)
    	})
    	/*   删除  */
    	$("#mainTable").delegate(".deleteSchedule","click",function(){
    		var id=$(this).attr("data-id")
    		updateContractStatus(id,'delete',0)
    	})
    	/*签约检查进度信息填写*/
    	$("#handleInfo").delegate("#addform","click",function(){
    		var id=$("#personScheduleId").val();
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
	    		case "2":
	    			projectObj.xuetang = $("#xuetang").val();
	    		  break;
	    		case "3":
	    			projectObj.RU_LEU = $("#RU_LEU").val();
	    			projectObj.RU_Pro = $("#RU_Pro").val();
	    			projectObj.RU_Glu = $("#RU_Glu").val();
	    			break;
	    		case "4":
	    			projectObj.ST_T = $("#ST_T").val();
	    			break;
	    		case "16":
	    			projectObj.ACF_fxz = $("#ACF_fxz").val();
	    			break;
	    		case "5":
	    			projectObj.FO_gdyp = $("#FO_gdyp").val();
	    			break;
	    		case "6":
	    			projectObj.BF_CHOL = $("#BF_CHOL").val();
	    			projectObj.BF_HDL_C = $("#BF_HDL_C").val();
	    			projectObj.BF_TG = $("#BF_TG").val();
	    			projectObj.BF_LDL_C = $("#BF_LDL_C").val();
	    			break;
	    		case "7":
	    			projectObj.TBLL = $("#TBLL").val();
	    			projectObj.ALT = $("#ALT").val();
	    			projectObj.AST = $("#AST").val();
	    			break;
	    		case "8":
	    			projectObj.BUN = $("#BUN").val();
	    			projectObj.Gr = $("#Gr").val();
	    			break;
	    		case "9":
	    			projectObj.FI_jtxj = $("#FI_jtxj").val();
	    			break;
	    		case "10":
	    			projectObj.YTJ_ru = $("#YTJ_ru").val();
	    			projectObj.YTJ_xuetang = $("#YTJ_xuetang").val();
	    			projectObj.YTJ_SPO2 = $("#YTJ_SPO2").val();
	    			projectObj.YTJ_ecg = $("#YTJ_ecg").val();
	    			projectObj.YTJ_bf = $("#YTJ_bf").val();
	    			break;
	    		case "11":
	    			projectObj.SUA_ua = $("#SUA_ua").val();
	    			break;
	    		case "12":
	    			projectObj.DB_man = $("#DB_man").val();
	    			break;
	    		case "14":
	    			projectObj.BR_br = $("#BR_br").val();
	    			break;
	    		case "17":
	    			projectObj.OB_other = $("#OB_other").val();
	    			break;
	    		/*case "CB":
	    			projectObj.xuetang = $("#xuetang").val();
	    			break;*/
	    		case "13":
	    			projectObj.CB_women = $("#CB_women").val();
	    			break;
	    		case "1":
	    			projectObj.BP_info = $("#BP_info").val();
	    			break;
	    		case "15":
	    			projectObj.FC_pt = $("#FC_pt").val();
	    			projectObj.FC_APTT = $("#FC_APTT").val();
	    			projectObj.FC_TT = $("#FC_TT").val();
	    			projectObj.FC_FIB = $("#FC_FIB").val();
	    			break;
	    		case "18":
	    			projectObj.BP_info = $("#MI_CTnI").val();
	    			projectObj.MI_CKMB = $("#MI_CKMB").val();
	    			projectObj.MI_Myoo = $("#MI_Myoo").val();
	    			break;
	    		case "19":
	    			projectObj.TC_TC = $("#TC_TC").val();
	    			break;
	    		case "20":
	    			projectObj.BGC_xuetang = $("#BGC_xuetang").val();
	    			break;
	    		case "21":
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
    		var id=$(this).attr("data-id");
    		documentId = id;
    		$("#personScheduleId").val(id);
    		layer.open({
	    		type:1
	    		,title:'检查信息'
	    		,content: $('#handleInfo')
				,area: ['900px','90%']
	    	})
	    	projectType = $(this).attr("data-type");
    		$(".project").hide()
	    	$(".type"+projectType).show();
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
	    		case "2":
	    			$("#xuetang").val(result.xuetang);
	    		  break;
	    		case "3":
	    			$("#RU_LEU").val(result.RU_LEU);
	    			$("#RU_Pro").val(result.RU_Pro);
	    			$("#RU_Glu").val(result.RU_Glu);
	    			break;
	    		case "4":
	    			$("#ST_T").val(result.ST_T);
	    			break;
	    		case "16":
	    			$("#ACF_fxz").val(result.ACF_fxz);
	    			break;
	    		case "5":
	    			$("#FO_gdyp").val(result.FO_gdyp);
	    			break;
	    		case "6":
	    			$("#BF_CHOL").val(result.BF_CHOL);
	    			$("#BF_HDL_C").val(result.BF_HDL_C);
	    			$("#BF_TG").val(result.BF_TG);
	    			$("#BF_LDL_C").val(result.BF_LDL_C);
	    			break;
	    		case "7":
	    			$("#TBLL").val(result.TBLL);
	    			$("#ALT").val(result.ALT);
	    			$("#AST").val(result.AST);
	    			break;
	    		case "8":
	    			$("#BUN").val(result.BUN);
	    			$("#Gr").val(result.Gr);
	    			break;
	    		case "9":
	    			$("#FI_jtxj").val(result.FI_jtxj);
	    			break;
	    		case "10":
	    			$("#YTJ_ru").val(result.YTJ_ru);
	    			$("#YTJ_xuetang").val(result.YTJ_xuetang);
	    			$("#YTJ_SPO2").val(result.YTJ_SPO2);
	    			$("#YTJ_ecg").val(result.YTJ_ecg);
	    			$("#YTJ_bf").val(result.YTJ_bf);
	    			break;
	    		case "11":
	    			$("#SUA_ua").val(result.SUA_ua);
	    			break;
	    		case "12":
	    			$("#DB_man").val(result.DB_man);
	    			break;
	    		/*case "13":
	    			$("#xuetang").val(result.xuetang);
	    			break;*/
	    		case "14":
	    			$("#BR_br").val(result.BR_br);
	    			break;
	    		case "17":
	    			$("#OB_other").val(result.OB_other);
	    			break;
	    		case "13":
	    			$("#CB_women").val(result.CB_women);
	    			break;
	    		case "1":
	    			$("#BP_info").val(result.BP_info);
	    			break;
	    		case "15":
	    			$("#FC_pt").val(result.FC_pt);
	    			$("#FC_APTT").val(result.FC_APTT);
	    			$("#FC_TT").val(result.FC_TT);
	    			$("#FC_FIB").val(result.FC_FIB);
	    			break;
	    		case "18":
	    			$("#MI_CTnI").val(result.MI_CTnI);
	    			$("#MI_CKMB").val(result.MI_CKMB);
	    			$("#MI_Myoo").val(result.MI_Myoo);
	    			break;
	    		case "19":
	    			$("#TC_TC").val(result.TC_TC);
	    			break;
	    		case "20":
	    			$("#BGC_xuetang").val(result.BGC_xuetang);
	    			break;
	    		case "21":
	    			$("#HBA1C_HBAIC").val(result.HBA1C_HBAIC);
	    			break;
			}
    		form.render();
    		
    	})
    	/*    登记*/
    	$("table").delegate(".login","click",function(){
    		var id=$(this).attr("data-id");
    		realDocumentId = id;
    		var name=$(this).attr("data-name")
    		$.ajax({
	    		url: "/xl/queryPersonSchedule?documentId="+id,
	    		type: "get",
	    		success : function(res){
	    			var data = res.data;
	    			if(res.data && res.data.length>0){
	    				var str ="";
	    				$.each(data,function(index,item){
	    					if(!!item.examinationInfo){
	    						myType = true;
	    					}
	    					documentId = item.id;
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
			if(!type){
				return itemName
    		}
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
		
		$('#mainTable').delegate('.healthyData','click',function(){
    		var userId = $(this).attr('data-managerId');
    		/*var userId = '0ead9c01-864a-11e7-b34c-00163e084de6';*/
    		$('#dataDetail').attr('userId',userId);
    		layer.open({
    			type: 1
    			,title:'数据详情'
    			,content: $('#dataDetail')
    			,area: ['680px','450px']
    			,btn: []
    			,btnAlign: 'c'
    			,yes: function(index, layero){
    				 
    			 }
    		});
    		getItems();
    		getDetail();
    	})
    	//获取项目名称列表
    	function getItems(){
    		$.get('/deviceData/queryDeviceItems',function(res){
    			if(res.successful){
    				var option = '';
    				$.each(res.data,function(index,item){
    					option += '<option value="'+item.deviceItemId+'">'+(item.deviceItemName).replace(/\"/g,'')+'</option>'
    				})
    				$('#dataType').html('<option  value="all">请选择项目名称</option>').append(option);
    				form.render('select');
    			}else{
    				layer.msg('检测项目获取失败！');
    			}
    		})
    	}
    	var start_data = {
    	    min: '1900-01-01 00:00:00'
    	    ,max: '2099-06-16 23:59:59'
	    	,format: 'YYYY-MM-DD hh:mm:ss'
    	    ,istoday: true
    	    ,choose: function(datas){
    	    	end_data.min = datas; //开始日选好后，重置结束日的最小日期
    	    	end_data.start = datas //将结束日的初始值设定为开始日
    	    }
    	};
    	  
    	var end_data = {
    	    min: '1900-01-01 00:00:00'
    	    ,max: '2099-06-16 23:59:59'
    	    ,format: 'YYYY-MM-DD hh:mm:ss'
    	    ,istoday: true
    	    ,choose: function(datas){
    	    	start_data.max = datas; //结束日选好后，重置开始日的最大日期
    	    }
    	};
    	document.getElementById('startTime_data').onclick = function(){
    		start_data.elem = this;
		    laydate(start_data);
    	}
    	document.getElementById('endTime_data').onclick = function(){
    		end_data.elem = this
		    laydate(end_data);
    	}
	 	$('#d-search').click(function(){
	 		getDetail();
	    }) 
	    var dataAry = [
			{name:"urine_VitC",value:"维生素C",unit:''},
	        {name:"urine_leukocytes",value:"白细胞",unit:''},
	        {name:"urine_bilirubin",value:"胆红素",unit:''},
	        {name:"urobilinogen",value:"尿胆原",unit:''},
	        {name:"urine_glucose",value:"葡萄糖",unit:''},
	        {name:"urine_specific_gravity",value:"比重",unit:''},
	        {name:"urine_protein",value:"蛋白质",unit:''},
	        {name:"urine_ketone",value:"酮体",unit:''},
	        {name:"urine_occult_blood",value:"潜血",unit:''},
	        {name:"urine_nitrite",value:"亚硝酸盐",unit:''},
	        {name:"urine_PH",value:"酸碱度",unit:''},
	        {name:"degreased_body_weight",value:"去脂体重",unit:'kg'},
	        {name:"protein_weight",value:"蛋白质重量",unit:'kg'},
	        {name:"health_score",value:"健康评分",unit:''},
	        {name:"water_percentage",value:"水分比",unit:'%'},
	        {name:"weight",value:"体重",unit:'kg'},
	        {name:"fat_control",value:"脂肪控制量",unit:'kg'},
	        {name:"basic_metabolism_rate",value:"基础代谢率",unit:'Kcal/d'},
	        {name:"fat_content",value:"脂肪",unit:'kg'},
	        {name:"bone_weight",value:"骨质重",unit:'kg'},
	        {name:"body_type",value:"身体类型",unit:'kg'},
	        {name:"visceral_fat_index",value:"内脏脂肪指数",unit:''},
	        {name:"fat_level",value:"肥胖等级",unit:''},
	        {name:"muscle_content",value:"肌肉",unit:'kg'},
	        {name:"weight_control",value:"体重控制量",unit:'kg'},
	        {name:"standard_weight",value:"标准体重",unit:'kg'},
	        {name:"muscle_control",value:"肌肉控制量",unit:'kg'},
	        {name:"height",value:"身高",unit:'cm'},
	        {name:"BMI",value:"健康指数",unit:''},
			{name:"body_fat_percentage",value:"体脂百分比",unit:'%'},
			{name:"HDL",value:"高密度蛋白胆固醇",unit:'mmol/L'},
			{name:"CE",value:"总胆固醇与高密度脂蛋白比值",unit:''},
			{name:"TG",value:"甘油三酯",unit:'mmol/L'},
			{name:"LDL",value:"低密度脂蛋白胆固醇",unit:'mmol/L'},
			{name:"chol",value:"总胆固醇",unit:'mmol/L'},
			{name:"hct",value:"红细胞比容",unit:'%'},
			{name:"hb",value:"血红蛋白",unit:'g/dL'},
			{name:"ECG_t_axis",value:"t_axis",unit:'degree'},
			{name:"ECG_sv1",value:"sv1",unit:'uV'},
			{name:"ECG_qtc",value:"qtc",unit:'ms'},
			{name:"ECG_rv5",value:"rv5",unit:'uV'},
			{name:"ECG_qrs",value:"qrs",unit:'ms'},
			{name:"ECG_rv5_svl",value:"rv5_svl",unit:'uV'},
			{name:"ECG_p_r",value:"p_r",unit:'ms'},
			{name:"ECG_qrs_axis",value:"qrs_axis",unit:'degree'},
			{name:"ECG_rr",value:"rr",unit:'ms'},
			{name:"ECG_qt",value:"qt",unit:'ms'},
			{name:"ECG_p_axis",value:"p_axis",unit:'degree'},
			{name:"heart_rate",value:"心率",unit:'bpm'}
	    ]
		//获取数据详情列表
	    function  getDetail(pageNum){
	    		var dataItemId = $('#dataType').val();
				var dataType = dataItemId == 'all' ? '' : dataItemId;
	    		var startTime = $('#startTime_data').val();
	    		var endTime = $('#endTime_data').val();
	    		pageNum = pageNum || 1;
	    		var pagesize = 5, userId = $('#dataDetail').attr('userId');;
	    		var query = '?pageSize='+pagesize+'&currentsPage='+pageNum+'&dataType='+dataType+'&startTime='+startTime+'&endTime='+endTime+'&managerId='+userId
	    		$.ajax({
	    			url:'/deviceData/queryUserInfo'+query,
	    			type:'GET',
	    			success:function(res){
		    			var str = '';
	    				if(res.successful && res.resultCode.code == 'SUCCESS'){
	    					if(res.items && res.items.length > 0){
	    						var data = res.items[0];
	    						/*var age = data.age, sex = data.sex;
	    						age = age?age:'无';
	    						sex = sex == 0 ? '保密' :(sex == 1 ? '男' : (sex == 2 ? '女' : '无'));*/
	    						/*$('#dataDetail .username').text(data.name?data.name:'无');
	    						$('#dataDetail .usersex').text(sex);
	    						$('#dataDetail .userage').text(age);*/
	    						$.each(res.items,function(index,item){
	    							var itemData = eval("("+item.dataValue+")");
	    							var d_value = itemData.value ? itemData.value : (itemData.wp ? itemData.wp : '');
	    							if(item.dataType == 'BODY'){
	    								d_value = eval("("+item.dataValue+")");
	    								itemData.name = '体脂秤';
	    								itemData.date = window.utils.simpleDateFormat(item.createTime,'yyyy-MM-dd HH:mm:ss');
	    							}
	    							else if(item.dataType == 'BF'){
	    								d_value = eval("("+item.dataValue+")");
	    								delete d_value.date; delete d_value.dataType; delete d_value.name; delete d_value.units;
	    								itemData.name = '血脂五项';
	    								itemData.date = window.utils.simpleDateFormat(item.createTime,'yyyy-MM-dd HH:mm:ss');
	    							}
	    							else if(item.dataType == 'BHB'){
	    								d_value = eval("("+item.dataValue+")");
	    								delete d_value.date; delete d_value.dataType; delete d_value.name; delete d_value.units;
	    								itemData.name = '血红蛋白';
	    								itemData.date = window.utils.simpleDateFormat(item.createTime,'yyyy-MM-dd HH:mm:ss');
									}
	    							if(window.utils.isObj(d_value)){
	    								var val1 = '';
	    								for(var val in d_value){
	    									$.each(dataAry,function(index,itemVal){
	    										if(itemVal.name == val){
	    											val1 += '<p>'+itemVal.value+': '+d_value[val]+''+itemVal.unit+'</p>';
	    										}
	    									})
	    								}
	    								d_value = val1;
	    							}else{
	    								d_value = d_value+''+itemData.units;
	    							}
	    							str += '<tr><td>'+itemData.name+'</td><td>'+d_value+'</td><td>'+itemData.date+'</td></tr>';
	    						})
	    					}else{
	    						str += '<tr><td colspan="3" align="center">暂无数据</td></tr>'
	    					}
	    				}else{
	    					console.log(res.resultCode.message);
	    					str += '<tr><td colspan="3">列表获取失败</td></tr>'
	    				}
	    				$('#dataDetail tbody').html(str);
	    				laypage({
	    					cont: 'pageBox'
				    		, pages: res.pagesCount
					        , curr: res.pageNumber||1
					        ,jump: function(obj, first){
					        	if(!first){
						            pageNum = obj.curr;
						            getDetail(pageNum);
					            }
					        }
					    });
	    			},
		            error:function(error){
		                console.error(error)
		                $('#dataDetail tbody').html('<tr><td colspan="5">列表获取失败</td></tr>');
		            }
	    		})
			}
	 	var processManagerId = ""
	 	$('#mainTable').delegate('.useProcess','click',function(){
	 		processManagerId = $(this).attr('data-managerId');
    		layer.open({
    			type: 1
    			,title:'服用流程'
				,content: $('#process')
				,area: ['100%','100%']
    			,btn: []
    			,btnAlign: 'c'
    			,yes: function(index, layero){
    				
    			}
    		});
    	})
    	//调用流程
    	$('#process').delegate('.userProcess','click',function(){
    		var userId = window.utils.getCookie("user_id")
    		var query = '?userId='+userId+'&doctorId=""&orderId='+processManagerId+'&procedureId='+$(this).attr("data-id")
    		$.ajax({
    			url:'/procedures/getProcedureDialog'+query,
    			type:'post',
    			success:function(res){
    				if(res.resultCode.code == 'SUCCESS'){
    					var host = 'workflow-api.lifelight365.com';
        				var targetUrl = "http://"+host+"/v1/procedure-dialog/"+res.data.id;
        				window.open(targetUrl);
    				}else{
    					layer.msg("流程获取失败")
    				}
    				
    			},
	            error:function(error){
	            	layer.msg("流程获取失败")
	            }
    		})
    	})
    	//查询流程
    	$('#process').delegate('.checkProcess','click',function(){
    		layer.msg("工程师吐血开发中。。。")
    		return
    		var obj = new Object()
    		obj.managerId = processManagerId;
    		obj.showCount = 10;
    		obj.currentPage = 1;
    		$.ajax({
    			url:'xl/subject/getSubjects.action',
    			type:'post',
    			data: $.toJSON(obj),
				dataType: 'json',
				contentType: 'application/json',
    			success:function(res){
    				if(res.resultCode.code == 'SUCCESS'){
    				}else{
    					layer.msg("流程获取失败")
    				}
    				/*laypage({
    					cont: 'pageBox'
			    		, pages: res.pagesCount
				        , curr: res.pageNumber||1
				        ,jump: function(obj, first){
				        	if(!first){
					            pageNum = obj.curr;
					            getDetail(pageNum);
				            }
				        }
				    });*/
    			},
	            error:function(error){
	            	layer.msg("流程获取失败")
	            }
    		})
    	})
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
	 	$("#testCheckIn").on("click",function(){
	 		window.open("/html/familyDoctors/checkInInfo.html?documentId="+documentId);  
	 	})
	 	$("#testCheckIn_out").on("click",function(){
	 		if(myType){
	 			window.open("/html/familyDoctors/checkInInfo.html?documentId="+documentId+"&type=2");//不让编辑  
	 		}else{
	 			window.open("/html/familyDoctors/checkInInfo.html?realDocumentId="+realDocumentId+"&documentId="+documentId+"&type=1");//让编辑  
	 		}
	 	})
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
