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
	      var managerId = window.utils.getCookie("managerId");
		  getWeakData()
          /*请求待办*/
          waitFor();
          function waitFor(){
        	  var obj = new Object();
        	  obj.buildType = "1";
        	  obj.managerId = managerId;
        	  var time = new Date();
        	  obj.startDate = window.utils.dateFormat2(time);
        	  obj.endDate = window.utils.dateFormat2(time);
        	  $.ajax({
    	    	  url:'/oprationDate/sign',
    	    	  type:'POST',
    	    	  data: $.toJSON(obj),
    	    	  dataType: 'json',
    	    	  contentType: "application/json",
    	          success:function(data){
    	        	  if(data.successful){
    	        		  var quanWid = $(".quan").width();
    	    	          $(".quan").height(quanWid);
    	    	          $("#first-container").height(quanWid);
    	        		  /*圆环加载*/
    	    	          var chart = new CircleChart({//用户
    	    		    	    svg: svg1,
    	    		    	    start: - Math.PI / 2,
    	    		    	    margin: 1,
    	    		    	    radius: 100,
    	    		    	    title: data.items[0].signCount?data.items[0].signCount:"0",
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
    	        	  }
    	          }
    	      })
          };
       
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
        //查看明细
    	$('.layui-table').delegate('.findMore','click',function(){
       		var val=$(this).parents('tr').children('td').eq(0).text();
       		$('#goods-info-box').attr('time',val);
           	layer.open({
            	  type:1
            	  ,title:'详情'
            	  ,content: $('#goods-info-box')
                  ,area: ['100%','100%']
            })
       	})
       	function getWeakData(){
    		var obj = new Object();
	       	obj.buildType = "1";
	       	obj.managerId = managerId;
	       	var time = new Date();
	       	obj.endDate = window.utils.dateFormat2(time);
	       	var oneweekdate = new Date(time-7*24*3600*1000);
	       	obj.startDate = window.utils.dateFormat2(oneweekdate);
	       	$.ajax({
	   	    	url:'/oprationDate/sign',
	   	    	type:'POST',
	   	    	data: $.toJSON(obj),
	   	    	dataType: 'json',
	   	    	contentType: "application/json",
	   	        success:function(data){
	   	        	if(data.resultCode.code == "SUCCESS"){
	   	        		if(data.items){
	   	        			var item = data.items;
	   	        			var dataAry = [];
	   	        			var userCounts=[]
	   	        			for(var value of item){
	   	        				orderDateAry.push(window.utils.simpleDateFormat(value.countDate,'yyyy-MM-dd'));
	   	        				userCounts.push(value.signCount)
	   	        			}
	   	        			dataAry.push({name: '周签约统计',data: userCounts});
	   	        			columnSexChart('first-container',orderDateAry,dataAry);
	   	        		}
	   	        	}else{
	   	        		layer.msg("查询失败！")
	   	        	}
	   	        	
	   	        },
	   	        error(error){
	   	        	 
	   	        }
	       	});
    	}
        //周统计
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
	})
}