window.onload=function(){
	layui.use(['form', 'layedit', 'laydate','element','laypage','layer'], function(){
	  var form = layui.form(),layer = layui.layer,layedit = layui.layedit,
	      laydate = layui.laydate ,laypage = layui.laypage
	      element=layui.element();
	  var managerId = window.utils.getCookie("managerId");
	  var managerId = '74c747d1-26e4-4fe7-a6cb-dba6e8612030';
	  var pieHeight = $("body").height()-100;
      $('#fwwcl').height(pieHeight);
      $('#xmzxjd').height(pieHeight);
      
      fwwclCount();
      xmzxjdCount();
      function fwwclCount(){
   	    $.ajax({
			url: "/statistics/serviceStatistics?startDate=&endDate=&orgId="+managerId,
			type: "get",
			success : function(res){
				if(res.data.length > 0){
					var seriesData = [];
					for(var i=0;i <=1;i++){
						var obj = new Object();
						if(i==0){
							obj.name="已完成数";
							obj.y=res.data[0].finishSum;
						}else{
							obj.name="未完成数";
							obj.y=res.data[0].countSum - res.data[0].finishSum
						}
						seriesData.push(obj)
					}
					$('#fwwcl').highcharts({
				          chart: {
				              plotBackgroundColor: null,
				              plotBorderWidth: null,
				              plotShadow: false
				          },
				          title: {
				              text: '有偿签约包数统计图'
				          },
				          subtitle: {
						      text: ''
					      },
					      credits: {
			                  enabled:false
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
				                  /*events: {
										 click: function(e) { 
											 location.href = e.point.url+"?dutiesKey="+e.point.dutiesKey+"&dutiesName="+e.point.name;
										 }
				                  }*/
				              }
				          },
				          series: [{
				              type: 'pie',
				              name: '履约服务占比',
				              data: seriesData
				          }]
				      });
				}
			},
			error : function(data){
				layer.close(layer.index);// 取消遮罩
					layer.msg('请求失败！')
			}
   	    })
      }
      function xmzxjdCount(){
    	  $.ajax({
    		  url: "/statistics/executeStatistics?startDate=&endDate=&orgId="+managerId,
    		  type: "get",
    		  success : function(res){
    			  if(res.data.length > 0){
    				  var seriesData = [];
    				  for(var item of res.data[0].list){
    					  var obj = new Object();
						  obj.name=item.itemName;
						  obj.y=item.countSum;
						  obj.item = item;
    					  seriesData.push(obj)
    				  }
    				  $('#xmzxjd').highcharts({
    					  chart: {
    						  plotBackgroundColor: null,
    						  plotBorderWidth: null,
    						  plotShadow: false
    					  },
    					  title: {
    						  text: '签约服务项目执行进度统计'
    					  },
    					  subtitle: {
    						  text: ''
    					  },
    					  tooltip: {
    						  headerFormat: '{series.name}<br>',
    						  pointFormat: '{point.name}: {point.y} ，<b>{point.percentage:.1f}%</b>'
    					  },
    					  credits: {
			                  enabled:false
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
										 var item_info = e.point.item;
										 $('#xmzxjd_info').highcharts({
					    					  chart: {
					    						  type: 'column'
					    					  },
					    					  title: {
					    						  text: '签约服务项目执行进度统计'
					    					  },
					    					  subtitle: {
											      text: '执行率：'+item_info.finishRate
										      },
										      credits: {
								                  enabled:false
										      },
					    					  tooltip: {
					    						  headerFormat: '{series.name}<br>',
					    						  pointFormat: '{point.name}: {point.y} ，<b>{point.percentage:.1f}%</b>'
					    					  },
					    					  plotOptions: {
					    						 column: {
					    							stacking: 'percent'
					    						 }
					    					  },
					    					  yAxis: {
					    						  min: 0,
					    						 title: {
					    							text: '百分比'
					    						 }
					    					  },
					    					  series: [
					    						  {
 						    						  name: '未完成',
 						    						  data: [item_info.countItemSum - item_info.finishItemSum]
					    						  },
					    						  {
 						    						  name: '已完成',
 						    						  data: [item_info.finishItemSum]
					    						  }
				    						  ]
					    				  });
										  layer.open({
											  title:'添加推动消息',
						    	              type: 1,
						    	              area: ['700px', '500px'],//宽高
						    	              shade: 0.8,
						    	              btn:['关闭'],
						    	              content: $('#xmzxjd_box'),
						    	              success: function(index){
						    	            	  
						    	              }
										  })
									 }
								  }
    						  }
    					  },
    					  series: [{
    						  type: 'pie',
    						  name: '有偿签约包数',
    						  data: seriesData
    					  }]
    				  });
    			  }
    		  },
    		  error : function(data){
    			  layer.close(layer.index);// 取消遮罩
    			  layer.msg('请求失败！')
    		  }
    	  })
      }
	})
}