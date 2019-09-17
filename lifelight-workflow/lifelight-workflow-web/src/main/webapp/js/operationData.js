window.onload=function(){
	layui.use(['form', 'layedit', 'laydate','element','laypage','layer'], function(){
		  var form = layui.form(),layer = layui.layer,layedit = layui.layedit,
		      laydate = layui.laydate ,laypage = layui.laypage
		      element=layui.element();
          var layid = location.hash.replace(/^#test=/, '');
	          element.tabChange('test', layid);
	          element.on('tab(test)', function(elem){
	              location.hash = 'test='+ $(this).attr('lay-id');
	          });
	       
	        //切换tab
	          $('.user-tab li').click(function(){
	        	  clearInterval(timer);
	        	  var type = $(this).attr('type');
	        	  if(type == 0){
	        		  getOrderInfo();
	        		  /*getCont(1);*/
	        	  }else if(type == 1){
	        		  user(0);
	        	  }else if(type == 2){
	        		  enroll();
	        	  }else if(type == 3){
	        		  change();
	        	  }else if(type == 4){
	        		  code();
	        	  }else if(type == 5){
	        		  waitFor();
	        	  }else if(type == 6){
	        		  /*jiayCount();*/
	        		  timer = setInterval(function(){
	        	    	   jiayCount();
	        	       },60000);
	        	  }
	          })
	          var pieHeight = $("body").height()-100;
	          $('#pieCharts').height(pieHeight);
	          $('#qyUserCharts').height(pieHeight);
	          $('#lyUserCharts').height(pieHeight);
	          /*请求待办*/
	          /*waitFor();*/
	          function waitFor(){
	        	  $.ajax({
	    	    	  url:'/message/scheduleCountMessages',
	    	    	  type:'GET',
	    	          success:function(data){
	    	        	  if(data.successful){
	    	        		  var quanWid = $(".quan").width();
	    	    	          $(".quan").height(quanWid);
	    	    	          window.onresize = function(){
	    	    	        	  window.location.reload();
	    	    	          }
	    	        		  /*圆环加载*/
	    	    	          var chart = new CircleChart({//用户
	    	    		    	    svg: svg1,
	    	    		    	    start: - Math.PI / 2,
	    	    		    	    margin: 1,
	    	    		    	    radius: 100,
	    	    		    	    title: data.data.userCount?data.data.userCount:"0",
	    	    		    	    titleSize: 24,
	    	    		    	    strokeWidth:10,
	    	    		    	    arcs: [
	    	    		    	         {
	    	    		    	            value: 1,
	    	    		    	            color: '#2C7CCA',
	    	    		    	            text: ""
	    	    		    	        }
	    	    		    	    ]
	    	    		       });
	    	    	          var chart = new CircleChart({//服务
	    	    		    	    svg: svg2,
	    	    		    	    start: - Math.PI / 2,
	    	    		    	    margin: 1,
	    	    		    	    radius: 100,
	    	    		    	    title: data.data.serviceCount?data.data.serviceCount:"0",
	    	    		    	    titleSize: 24,
	    	    		    	    strokeWidth:10,
	    	    		    	    arcs: [
	    	    		    	         {
	    	    		    	            value: 1,
	    	    		    	            color: '#F80809',
	    	    		    	            text: ""
	    	    		    	        }
	    	    		    	    ]
	    	    		       });
	    	    	          var chart = new CircleChart({//订单
	    	    		    	    svg: svg3,
	    	    		    	    start: - Math.PI / 2,
	    	    		    	    margin: 1,
	    	    		    	    radius: 100,
	    	    		    	    title: data.data.orderCount?data.data.orderCount:"0",
	    	    		    	    titleSize: 24,
	    	    		    	    strokeWidth:10,
	    	    		    	    arcs: [
	    	    		    	         {
	    	    		    	            value: 1,
	    	    		    	            color: '#1FA404',
	    	    		    	            text: ""
	    	    		    	        }
	    	    		    	    ]
	    	    		       });
	    	        	  }
	    	          }
	    	      })
	          };
           /*getOrderInfo();*/
          function getOrderInfo(){
        	  var pageSize=10,currentPage=1;
   	       	  var times = new Date();
	      	  var nowTime = $("#orderOverTime").val()||window.utils.simpleDateFormat(times,'yyyy-MM-dd');
	      	  var beforeTime = $("#orderBeginTime").val()||window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
	          var data = {showCount:pageSize,currentPage:currentPage,startDate:beforeTime,endDate:nowTime};
	          var type = $('#orderSelect').val();
	          data.type = type== 'all' ? null : type;
	          var dataJson = $.toJSON(data);
        	   //销售总数量
        	  $.ajax({
    	    	  url:'/orderInfo/countTotalAmount',
    	    	  type:'POST',
    	    	  data:dataJson,
    	    	  dataType:"json",
    	          contentType: "application/json",
    	          success:function(data){
    	        	  if(data.successful){
    	        		  var str='';
    	        		  str+='<p>'+(data.data?data.data:' ')+'</p>'
    	        	  }
    	        	  $(".orderCount").html(str);
    	          }
    	      })
	          //销售金额
	           $.ajax({
    	    	  url:'/orderInfo/countTotalPrice',
    	    	  type:'POST',
    	    	  data:dataJson,
    	    	  dataType:"json",
    	          contentType: "application/json",
    	          success:function(data){
    	        	  if(data.successful){
    	        		  var str='';
    	        		  str+='<p>'+(data.data?data.data:' ')+'</p>'
    	        	  }
    	        	  $(".sellCount").html(str);
    	          }
    	      })
    	      getCont(currentPage,pageSize)
          }
       
        //订单管理
         function getCont(currentPage,pageSize){
              var times = new Date();
          	  var nowTime = $("#orderOverTime").val() || window.utils.simpleDateFormat(times,'yyyy-MM-dd');
          	  var beforeTime = $("#orderBeginTime").val() || window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
              var data = {showCount:pageSize,currentPage:currentPage,startDate:beforeTime,endDate:nowTime};
              var type = $('#orderSelect').val();
              data.type = type== 'all' ? null : type;
              var dataJson = $.toJSON(data);
        	  $.ajax({
         		url:'/orderInfo/countOrderInfo',
         		type:'POST',
         		data:dataJson,
         		dataType:"json",
                contentType: "application/json",
                success:function(data){
                 	if(data.successful){
                 		var str='',list=data.items;
                 		if(list.length==0){
                 			$('.layui-orderTable tbody').html('<tr><td colspan="4" style="text-align:center;">暂无数据</td></tr>')
                 			return false;
                 		}
                 		if(list.length>0){
                 			for(var i=0;i<list.length;i++){
	                 			str+='<tr><td>'+(list[i].countDate?list[i].countDate:'')+'</td><td>'+(list[i].countNumber?list[i].countNumber:0)+'</td><td>'+(list[i].countPrice?list[i].countPrice:0)+'</td></tr>'
	                 		}
                 		}
                 		// 分页
                    	laypage({
                    	    cont: 'pageTag'
                    	    ,pages: data.pagesCount
                    	    ,groups: 5 //连续显示分页数
                    	    ,curr:data.pageNumber || 1
                    	    ,jump: function(obj, first){
                    	    	if(!first){
                    	    		//得到了当前页，用于向服务端请求对应数据
                        		    currentPage = obj.curr;
                        		    getCont(currentPage);
                    	    	}
                    		}
                    	});
                    	$('.layui-orderTable tbody').html(str);
                    	currentPage = data.pageNumber*1;
                        totleCount = data.totalItemsCount*1;
                        pageCount = data.pagesCount*1;
                 	}else{
                 		layer.msg(data.resultCode.message);
                 	}
                 },error:function(error){
                     layer.msg('查询失败!');
                 }
         	})
         }    
       //订单搜索
        $(".search").click(function(){
        	//开始时间  
  	        var startData=$("#orderBeginTime").val();
  	       //结束时间
  	        var endDate=$("#orderOverTime").val();
          	if(!startData ||  !endDate){
          		layer.msg('请选择开始和结束时间');
          		return;
          	}
          	getCont(1);
          	getOrderInfo();
        })
        
        
        //注册统计
         $("#enrollSearch").click(function(){
        	//开始时间  
  	        var enrollStartTime=$("#enrollBeginTime").val();
  	       //结束时间
  	        var enrollEndTime=$("#endOverTime").val();
          	if(!enrollStartTime || !enrollEndTime){
          		layer.msg('请选择开始和结束时间');
          		return;
          	}
          	enroll(1);	
        })
       
       var pageSizer=10,currPager=1;
       function  enroll(currPager){
        	  var times = new Date();
          	  var nowTime = $("#endOverTime").val() || window.utils.simpleDateFormat(times,'yyyy-MM-dd');
          	  var beforeTime =$("#enrollBeginTime").val() || window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
              var data = {showCount:pageSizer,currentPage:currPager,startDate:beforeTime,endDate:nowTime};
              var dataJson=$.toJSON(data);
              $.ajax({
            	  url:'/oprationDate/regist',
            	  type:'POST',
	           	  data:dataJson,
	           	  dataType:"json",
                  contentType: "application/json",
                  success:function(data){
                	  $('#orderCountss').html(data.data);
                	  if(data.successful){
                   		var str='',list=data.items;
                   		if(list.length==0){
                   			$('.layui-orderTable tbody').html('<tr><td colspan="4" style="text-align:center;">暂无数据</td></tr>')
                   			return false;
                   		}
                   		if(list.length>0){
                   			for(var i=0;i<list.length;i++){
  	                 			str+='<tr><td >'+(list[i].countDate?list[i].countDate:'')+'</td><td>'+(list[i].countNumber?list[i].countNumber:0)+'</td><td><button class="layui-btn findMore">查看明细</button></td></tr>'
  	                 		}
                   		}
                      	laypage({
                      	    cont: 'pageTwo'
                      	    ,pages: data.pagesCount
                      	    ,groups: 5 
                      	    ,curr:data.pageNumber || 1
                      	    ,jump: function(obj, first){
                      	    	if(!first){
                      	    		currPager = obj.curr;
                          		  enroll(currPager);
                      	    	}
                      		}
                      	});
                      	$('.enroll tbody').html(str);
                   	}else{
                   		layer.msg(data.resultCode.message);
                   	}
                  },error:function(error){
                	  layer.msg('失败');
                  }
              })
        }
        
        //查看明细
        	$('.layui-table').delegate('.findMore','click',function(){
           		var val=$(this).parents('tr').children('td').eq(0).text();
           		$('#goods-info-box').attr('time',val);
           		more(currExplicit,val);
               	layer.open({
                	  type:1
                	   ,title:'详情'
                	   ,content: $('#goods-info-box')
                       ,area: ['800px','450px']
                })
           	})
           	
        	var pageExplicit=5,currExplicit=1;
           	function more(currExplicit,val){
           		var val = val || $('#goods-info-box').attr('time');
           		var data = {showCount:pageExplicit,currentPage:currExplicit,createTime:val};
           		var dataJson=$.toJSON(data);
       			$.ajax({
               		url:'/oprationDate/detaile',
               		type:'POST',
               		data:dataJson,
               		dataType:"json",
                    contentType: "application/json",
                    success:function(result){
                   	 if(result.successful){
                   		var list=result.items;
                       	var str='';
                       	for(var i=0;i<list.length;i++){
                       		var item=list[i].sex;
                       		if(item==0){
                       			item='保密'
                       		}else if(item==1){
                       			item='男'
                       		}else if(item==2){
                       			item='女'
                       		}
                       		var mobile=list[i].mobile;
                       		if(mobile==null){
                       			mobile='子账户'
                       		}
                       		var itemName=list[i].name;
                       		if(itemName==null){
                       			itemName='未填写'
                       		}
                       		var itemBir=list[i].birthday;
                       		if(itemBir==null){
                       			itemBir='未填写'
                       		}
                       		str+='<tr><td>'+itemName+'</td><td>'+mobile+'</td><td>'+item+'</td><td>'+itemBir+'</td><td>'+(list[i].createTime?list[i].createTime:'')+'</td></tr>';
                       	}
                       	laypage({
                         	    cont: 'pageExplicit'
                         	    ,pages: result.pagesCount
                         	    ,groups: 5 
                         	    ,curr:result.pageNumber || 1
                         	    ,jump: function(obj, first){
                         	    	if(!first){
                         	    		currExplicit = obj.curr;
                        	    		more(currExplicit,val);
                         	    	}
                         		}
                         	});
                       	$('.explicit tbody').html(str);
                   	}
                   }
           	})
       	}
        //转化率
        $("#changeSearch").click(function(){
        	//开始时间  
  	        var chengeStartTime=$(".changeStartTime").val();
  	       //结束时间
  	        var changeEndTime=$(".changeEndTime").val();
          	if(chengeStartTime=='' ||  changeEndTime==''){
          		layer.msg('请选择开始和结束时间');
          		return;
          	}
          	change(1);	
        })
        
        var pageSizez=10,currPage=1;
        function change(currPage){
        	 var times = new Date();
         	 var nowTime = $(".changeEndTime").val()||window.utils.simpleDateFormat(times,'yyyy-MM-dd');
         	 var beforeTime =$(".changeStartTime").val()||window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
             var data = {showCount:pageSizez,currentPage:currPage,startDate:beforeTime,endDate:nowTime};
             var dataJson=$.toJSON(data);
             $.ajax({
            	 url:'/oprationDate/conversion',
            	 type:'POST',
            	 data:dataJson,
            	 dataType:"json",
                 contentType: "application/json",
                 success:function(items){
           		  $(".visitorCount").html(items.data.visitorSum);
           		  $(".purchaserCount").html(items.data.purchaserSum);
           		  $(".conversionRate").html(items.data.conversionSum);
                	  if(items.successful){
                     		var str='',list=items.items;
                     		if(list.length==0){
                     			$('.change_Table tbody').html('<tr><td colspan="4" style="text-align:center;">暂无数据</td></tr>')
                     			return false;
                     		}
                     		if(list.length>0){
                     			for(var i=0;i<list.length;i++){
                     				str+='<tr><td>'+(list[i].countDate?list[i].countDate:'')+'</td><td>'+(list[i].visitorCount?list[i].visitorCount:0)+'</td><td>'+(list[i].purchaserCount?list[i].purchaserCount:0)+'</td><td>'+(list[i].conversionRate?list[i].conversionRate:0)+'</td></tr>'  	                 			
    	                 		}
                     		}
                        	laypage({
                        	    cont: 'pageThree'
                        	    ,pages: items.pagesCount
                        	    ,groups: 5 
                        	    ,curr:items.pageNumber || 1
                        	    ,jump: function(obj, first){
                        	    	if(!first){
                        	    		currPage = obj.curr;
                            		    change(currPage);
                        	    	}
                        		}
                        	});
                        	$('.change_Table tbody').html(str);
                     	}else{
                     		layer.msg('失败');
                     	}
                 	},error:function(error){
                 		layer.msg('失败');
                 	}
             })
        }
        
       
        //推广码
        $("#codeSearch").click(function(){
          	code(1)
        })
        
    	var pageSizet=10,currPaget=1;
        function code(currPaget){
        	 var doctorName=$("#organization").val();
        	 var promotioname=$("#promoter").val();
        	 var times = new Date();
         	 var buyNowTime = $("#payEndTime").val() || window.utils.simpleDateFormat(times,'yyyy-MM-dd');
         	 var buyBeforeTime =$("#payStartTime").val() || window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
         	 var stopNowTime = $("#registEndTime").val() || window.utils.simpleDateFormat(times,'yyyy-MM-dd');
        	 var stopBeforeTime =$("#registStartTime").val() || window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
             var data = {showCount:pageSizet,currentPage:currPaget,registStartTime:stopBeforeTime,registEndTime:stopNowTime,payStartTime:buyBeforeTime,payEndTime:buyNowTime,promotionName:promotioname,doctorName:doctorName};
             var dataJson=$.toJSON(data);
             $.ajax({
            	 url:'/oprationDate/qrcode',
            	 type:'POST',
            	 data:dataJson,
            	 dataType:"json",
                 contentType: "application/json",
                 success:function(items){
                	 $('.promotionName').html(items.data.sumPeople);
                	 $('.orderPrice').html(items.data.sumPrice);
                	 if(items.successful){
                  		var str='',list=items.items;
                  		if(list.length==0){
                  			$('.codeTable tbody').html('<tr><td colspan="8" style="text-align:center;">暂无数据</td></tr>')
                  			return false;
                  		}
                  		if(list.length>0){
                  			for(var i=0;i<list.length;i++){
                  				str+='<tr><td class="codeUserId">'+(list[i].userName?list[i].userName:'')+'</td><td id="promotionName">'+(list[i].promotionName?list[i].promotionName:'')+'</td><td>'+(list[i].doctorName?list[i].doctorName:'')+'</td><td id="loginTime">'+(list[i].loginTime?list[i].loginTime:'')+'</td><td id="orderId">'+(list[i].orderCode?list[i].orderCode:'')+'</td><td id="payTime">'+(list[i].payTime?list[i].payTime:'')+'</td><td id="orderName">'+(list[i].orderName?list[i].orderName :'')+'</td><td id="orderPrice">'+(list[i].orderPrice?list[i].orderPrice:0)+'</td></tr>'  	                 			
 	                 		}
                  		}
                     	laypage({
                     	    cont: 'pageFour'
                     	    ,pages: items.pagesCount
                     	    ,groups: 5 
                     	    ,curr:items.pageNumber || 1
                     	    ,jump: function(obj, first){
                     	    	if(!first){
                     	    		currPaget = obj.curr;
                         		   code(currPaget);
                     	    	}
                     		}
                     	});
                     	$('.codeTable tbody').html(str);
                  	}else{
                  		layer.msg('失败');
                  	}
                 },error:function(error){
                	 layer.msg('获取推广码失败');
                 }
             })
        }
       
       
       
        //导出详情
       $('#export').on('click',function(){
	   		var query = '?';
	   		var buyStartData = $.trim($('.buyStartData').val());
	        var relationName = $.trim($('.relationName').val());
	        var stopStartData = $.trim($('.stopStartData').val());
	        var stopEndData = $('.stopEndData').val();
	       	var organization =  $('#organization').val();
	        var promoter = $('#promoter').val();
	       	if(buyStartData)  query += 'buyStartData='+buyStartData+'&';
	       	if(relationName)  query += 'relationName='+relationName+'&';
	       	if(stopStartData)  query += 'stopStartData='+stopStartData+'&';
	       	if(stopEndData)  query += 'stopEndData='+stopEndData+'&';
	       	if(organization)  query += 'organization='+organization+'&';
	       	if(promoter)  query += 'promoter='+promoter+'&';
	
	       	$(this).attr('href','/oprationDate/export'+query);
	   });
        
       
       
      //用户分析
        $('.userTab li').on('click',function(){
	        var type=$(this).attr('type');
	        user(type);
	    })
	     $("#userSearch").click(function(){
        	//开始时间  
	    	var startTime=$("#beginTime").val();
  	       //结束时间
  	        var endTime=$("#overTime").val();
          	if(!startTime || !endTime){
          		layer.msg('请选择开始和结束时间');
          		return;
          	}
          	user(null);
        })
	    
       function user(type){
        	var times = new Date();
        	var nowTime = $("#overTime").val() || window.utils.simpleDateFormat(times,'yyyy-MM-dd');
        	var beforeTime =$("#beginTime").val() || window.utils.simpleDateFormat(times.getTime()-6*24*3600*1000,'yyyy-MM-dd');
           	type = type || $('.userTab .layui-this').attr('type');
            var data = {startDate:beforeTime,endDate:nowTime,type:type};
            var dataJson=$.toJSON(data);
            $.ajax({
            	url:'/oprationDate/userAnalysis',
            	type:'POST',
            	data:dataJson,
            	dataType:"json",
                contentType: "application/json",
                success:function(data){
                	if(data.successful){
                  		var orderDateAry=[],numObj=new Object(),orderManNumAry=[],orderWomanNumAry=[],orderSecretNumAry=[],childrenCount=[],juvenileCount=[],youthCount=[],middleAgeCount=[],oldAgeCount=[],noAgeCount=[];
                  		var countDateList=[],proAry=[];
                  		if(type == 1){
        					countDateList=data.data.countDateList;
        					$.each(data.data.proviceList,function(index,item){
        						var dataAry = [];
        						$.each(item.data.split(','),function(index,item){
        							dataAry.push(Number(item));
        						})
        						var proObj = {data:dataAry,name:item.name};
        						proAry.push(proObj);
        					})
        				}else{
                     		 $.each(data.data,function(index,item){
     	        				orderDateAry.push(window.utils.simpleDateFormat(item.countDate,'yyyy-MM-dd'));
     	        				if(type == 0){
     	        					orderManNumAry.push(item.manCount);
     		        				orderWomanNumAry.push(item.womanCount);
     		        				orderSecretNumAry.push(item.secretCount);
     	        				}else if(type == 2){
     	        					childrenCount.push(item.childrenCount);
     		        				juvenileCount.push(item.juvenileCount);
     		        				youthCount.push(item.youthCount);
     		        				middleAgeCount.push(item.middleAgeCount);
     		        				oldAgeCount.push(item.oldAgeCount);
     		        				noAgeCount.push(item.noAgeCount);
     	        				}
     		        				
                    		 })
        				}
                		numObj = {man:orderManNumAry,woman:orderWomanNumAry,secret:orderSecretNumAry,children:childrenCount,juven:juvenileCount,youth:youthCount,middleAge:middleAgeCount,oldAge:oldAgeCount,noAge:noAgeCount};
                   		 if(type == 0){
                   			 dataAry = [{name: '男',data: numObj.man},{name: '女',data: numObj.woman},{name: '保密',data: numObj.secret}];
                   			 columnSexChart('first-container',orderDateAry,dataAry);
                   		 }else if(type == 1){
                   		 	columnAreaChart('second-container',countDateList,proAry);
                   		 }else if(type == 2){
                   			 dataAry = [{name: '儿童',data: numObj.children},{name: '少年',data: numObj.juven},{name: '青年',data: numObj.youth},{name: '中年',data: numObj.middleAge},{name: '老年',data: numObj.oldAge},{name: '未知',data: numObj.noAge}];          
                   			 columnSexChart('third-container',orderDateAry,dataAry);
               		 	}                 	
                  	 }
                },
                error:function(error){
                	layer.msg('获取失败');
                }
            })
        }
      //性别、年龄
        var orderDateAry=[],numObj=new Object();
        function columnSexChart(ele,orderDateAry,dataAry){
           $('#'+ele).highcharts({
                chart: {
                    type: 'column'
                },
                title: {
                    text: ''
                },
                xAxis: {
                    categories: orderDateAry
                },
               yAxis: {
                    min: 0,
                    title: {
                        text: ''
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                   pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0">{point.y}</td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                series: dataAry
           });
          }
        //区域  
       function columnAreaChart(eve,countDateList,proAry){
        	$('#'+eve).highcharts({
        		chart: {
    		        type:'column'
    		   },
    		   title: {
                   text: ''
               },
               xAxis: {
                   categories: countDateList                               
               },
               yAxis: {
                   min: 0,
                   title: {
                       text: ''
                   }
               },
               tooltip: {
                   headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                   pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                   '<td style="padding:0">{point.y}</td></tr>',
                   footerFormat: '</table>',
                   shared: true,
                   useHTML: true
               },
               series: proAry
        	})
        }
       jiayCount();
       clearInterval(timer);
       var timer = setInterval(function(){
    	   jiayCount();
       },60000);
       function jiayCount(){
    	   var seriesData=[];
    	   var qySeriesData=[];
    	   var lySriesData=[];
    	   $.ajax({
 	    	  url:'/sbOrderStatistic/getSbDutiesCount',
 	    	  type:'GET',
 	    	  async:false,
 	          success:function(data){
 	        	  if(data.successful){
 	        		  if(!!data.data){
 	        			  $("#qianyuePeople span").html(data.data.signCount)
 	        			  $("#lvyuePeople span").html(data.data.dutiesCount)
 	        			  var lvyueRate = GetPercent(data.data.dutiesCount, data.data.signCount);
 	        			  $("#lvyueRate span").html(lvyueRate);
 	        			  for(let item of data.data.dutiesList){
 	        				 var obj = new Object();
 	        				 obj.name=item.dutiesName;
        					 obj.y=item.dutiesCount;
    						 obj.url="countPie.html";
    						 obj.dutiesKey=item.dutiesKey;
 	        				 seriesData.push(obj);
 	        			  }
 	        		  }else{
 	        			  layer.msg("暂无数据！")
 	        		  }
 	        	  }
 	          }
 	       })
 	       $.ajax({
 	    	   url:'/sbOrderStatistic/getSbOrgUserCount',
 	    	   type:'GET',
 	    	   async:false,
 	    	   success:function(data){
 	    		   if(data.successful){
 	    			   if(!!data.data){
 	    				   for(let item of data.data){
 	    					   var obj1 = new Object();
 	    					   obj1.name=item.dutiesName;
 	    					   obj1.y=item.dutiesCount;
 	    					   obj1.dutiesKey=item.dutiesKey;
 	    					   qySeriesData.push(obj1);
 	    				   }
 	    			   }else{
 	    				   layer.msg("暂无数据！")
 	    			   }
 	    		   }
 	    	   }
 	       })
 	       $.ajax({
 	    	   url:'/sbOrderStatistic/getSbOrgDutiesCount',
 	    	   type:'GET',
 	    	   async:false,
 	    	   success:function(data){
 	    		   if(data.successful){
 	    			   if(!!data.data){
 	    				   for(let item of data.data){
 	    					   var obj = new Object();
 	    					   obj.name=item.dutiesName;
 	    					   obj.y=item.dutiesCount;
 	    					   obj.dutiesKey=item.dutiesKey;
 	    					   lySriesData.push(obj);
 	    				   }
 	    			   }else{
 	    				   layer.msg("暂无数据！")
 	    			   }
 	    		   }
 	    	   }
 	       })
           $('#pieCharts').highcharts({
               chart: {
                   plotBackgroundColor: null,
                   plotBorderWidth: null,
                   plotShadow: false
               },
               title: {
                   text: '履约服务分布图'
               },
               subtitle: {
    			   text: '单位（人次）'
    		   },
               tooltip: {
                   headerFormat: '{series.name}<br>',
                   pointFormat: '{point.name}: {point.y} ，<b>{point.percentage:.1f}%</b>'
               },
               plotOptions: {
                   pie: {
                       allowPointSelect: true,
                       cursor: 'pointer',
                       dataLabels: {
                           enabled: true,
                           format: '<b>{point.name}: {point.y} ， <b>{point.percentage:.1f} %',
                           style: {
                               color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                           }
                       },
                       events: {
                           click: function(e) {
                        	   location.href = e.point.url+"?dutiesKey="+e.point.dutiesKey+"&dutiesName="+e.point.name; //上面是当前页跳转，如果是要跳出新页面，那就用
                               //window.open(e.point.url);
                               //这里的url要后面的data里给出
                           }
                       }
                   }
               },
               series: [{
                   type: 'pie',
                   name: '履约服务占比',
                   data: seriesData
               }]
           });
    	   $('#qyUserCharts').highcharts({
    		   chart: {
    			   plotBackgroundColor: null,
    			   plotBorderWidth: null,
    			   plotShadow: false
    		   },
    		   title: {
    			   text: '签约用户占比'
    		   },
    		   subtitle: {
    			   text: '单位（人）'
    		   },
    		   tooltip: {
    			   headerFormat: '{series.name}<br>',
    			   pointFormat: '{point.name}: {point.y} ，<b>{point.percentage:.1f}%</b>'
    		   },
    		   plotOptions: {
    			   pie: {
    				   allowPointSelect: true,
    				   cursor: 'pointer',
    				   dataLabels: {
    					   enabled: true,
    					   format: '<b>{point.name}：{point.y} ，</b>{point.percentage:.1f} %',
    					   style: {
    						   color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
    					   }
    				   }
    			   }
    		   },
    		   series: [{
    			   type: 'pie',
    			   name: '签约用户占比',
    			   data: qySeriesData
    		   }]
    	   });
    	   $('#lyUserCharts').highcharts({
    		   chart: {
    			   plotBackgroundColor: null,
    			   plotBorderWidth: null,
    			   plotShadow: false
    		   },
    		   title: {
    			   text: '履约用户占比'
    		   },
    		   subtitle: {
    			   text: '单位（人）'
    		   },
    		   tooltip: {
    			   headerFormat: '{series.name}<br>',
    			   pointFormat: '{point.name}: {point.y} ， <b>{point.percentage:.1f}%</b>'
    		   },
    		   plotOptions: {
    			   pie: {
    				   allowPointSelect: true,
    				   cursor: 'pointer',
    				   dataLabels: {
    					   enabled: true,
    					   format: '<b>{point.name}: {point.y} ，</b>{point.percentage:.1f} %',
    					   style: {
    						   color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
    					   }
    				   }
    			   }
    		   },
    		   series: [{
    			   type: 'pie',
    			   name: '履约用户占比',
    			   data: lySriesData
    		   }]
    	   });
    	   // 海淀区家庭医生服务履约率
    	   $.ajax({
    		   url:'/sbOrderStatistic/getSbOrgStatisticCount',
    		   type:'GET',
    		   async:false,
 	    	   success:function(data){
 	    		   if(data.successful){
 	    			   if(data.data){
 	    				   var temp = '',tempObj = {};
 	    				   for(let item of data.data){
 	    					   if(item.type==0){
 	    						   temp +='<tr>'
 	    						   temp += '<td>'+item.orgName+'</td>';
 	    						   temp += '<td>'+item.signCount+'</td>';
 	 	 	 	    			   temp += '<td>'+item.dutiesCount+'</td>';
 	 	 	 	    			   temp += '<td>'+item.ratio+'</td>';
 	 	 	    				   temp+='</tr>'
 	    					   }else if(item.type==1){
 	    						   tempObj = item;
 	    					   }
 	    				   }
 	    				   if(tempObj && Object.keys(tempObj).length>0){
 	    					   temp +='<tr style="border-top:1px solid #000;">'
    						   temp += '<td>合计：</td>';
    						   temp += '<td>'+tempObj.signCount+'</td>';
	 	 	    			   temp += '<td>'+tempObj.dutiesCount+'</td>';
	 	 	    			   temp += '<td>'+tempObj.ratio+'</td>';
	 	    				   temp+='</tr>'
 	    				   }
 	    				   $('#performanceTable table tbody').html(temp);
 	    			   }else{
 	    				   layer.msg("暂无海淀区家庭医生服务履约数据！")
 	    			   }
 	    		   }
 	    	   }
    	   });
       }
       //计算页面滚动条位置
       function getScrollTop(){   
    	    var scrollTop=0;   
    	    if(document.documentElement&&document.documentElement.scrollTop){   
    	        scrollTop=document.documentElement.scrollTop;   
    	    }else if(document.body){   
    	        scrollTop=document.body.scrollTop;   
    	    }   
    	    return scrollTop;   
    	} 
       //计算两个整数的百分比值 
       function GetPercent(num, total) {
	       num = parseFloat(num); 
	       total = parseFloat(total); 
	       if (isNaN(num) || isNaN(total)) { 
	    	   return "-"; 
	       } 
	       return total <= 0 ? "0%" : (Math.round(num / total * 10000) / 100.00 + "%"); 
       } 
	})
}