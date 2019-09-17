$(function(){
    layui.use(['laypage', 'layer','form','laydate'], function () {
        var $ = layui.jquery;
        var laypage = layui.laypage, layer = layui.layer, laydate = layui.laydate, form = layui.form();
        var managerId = window.utils.getCookie("managerId");
        /*var managerId = '74c747d1-26e4-4fe7-a6cb-dba6e8612030';*/
        var start = {
    	    min: '1900-01-01'
    	    ,max: '2049-06-16'
	    	,format: 'YYYY-MM-DD '
    		,value: '2018-01-01'
    	    ,istoday: false
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
    	document.getElementById('startDate').onclick = function(){
		    start.elem = this;
		    laydate(start);
    	}
    	document.getElementById('endDate').onclick = function(){
		    end.elem = this
		    laydate(end);
    	}
    	var date = new Date();
		var nowTime = date.getTime();
    	var todayTime = window.utils.simpleDateFormat(nowTime,'yyyy-MM-dd');//今日日期
    	$("#endDate").val(todayTime);
    	form.render();
    	/*$("#startDate").val("2018-01-01")*/
    	loadTable();
    	function loadTable(){
    		var startDate= $("#startDate").val();
    		var endDate= $.trim($("#endDate").val())
    		if(!startDate || !endDate){
    			layer.msg("请选择统计日期");return;
    		}
    		layer.open({type: 3,content: ""});
    		$.ajax({
    			url: "/statistics/executeStatistics?startDate="+startDate+"&endDate="+endDate+"&orgId="+managerId,
    			type: "get",
    			success : function(res){
    				layer.close(layer.index);//取消遮罩
    				if(res.data.length <= 0){
    					layer.msg("暂无数据！");
    					layer.close(layer.index);//取消遮罩
    					return ;
    				}else{
    					var tr1 = '<th rowspan="2">分组</th>'+
    		                '<th rowspan="2">机构名称</th>'+
    		                '<th rowspan="2">有偿签约包数</th>'+
    		                '<th rowspan="2">内含项目总数</th>'+
    		                '<th rowspan="2">执行项目总数</th>'+
    		                '<th rowspan="2">执行率(%)</th>';
    					var tr2 = "";
    					var tdStr = "<td>"+ res.data[0].orgName+"</td><td>小计</td><td>"+res.data[0].countSum+"</td><td>"+res.data[0].countItemSum+"</td><td>"+res.data[0].finishItemSum+"</td><td>"+res.data[0].finishRate+"</td>";
    					for(var item of res.data[0].list){
    						tr1 += "<th colspan='4' style='text-align: center'>"+item.itemName+"</th>";
    						tr2 += "<th>有偿签约数</th>";
    						tr2 += "<th>内含项目总数(个)</th>";
    						tr2 += "<th>执行项目总数(个)</th>";
    						tr2 += "<th>执行率(%)</th>";
    						tdStr += "<td>"+item.countSum+"</td>";
    						tdStr += "<td>"+item.countItemSum+"</td>";
    						tdStr += "<td>"+item.finishItemSum+"</td>";
    						var finishRate = !!item.finishRate?item.finishRate:"";
    						tdStr += "<td>"+finishRate+"</td>";
    					}
    					$("#mainTable thead tr:first-child").html(tr1);
    					$("#mainTable thead tr:last-child").html(tr2);
    					$("#mainTable tbody tr").html(tdStr);
    				}
    			},
    			error : function(data){
    				layer.close(layer.index);//取消遮罩
					layer.msg('请求失败！')
    			}
    		})
    	}
    	$("#search").on("click",function(){
    		loadTable()
    	})
    	$("#exportBtn").on("click",function(){
    		var query='?';
    		var startDate= $("#startDate").val();
    		var endDate= $.trim($("#endDate").val())
    		if(!startDate || !endDate){
    			layer.msg("请选择统计日期");return;
    		}
    		query +="startDate="+startDate+"&";
    		query +="endDate="+endDate+"&";
    		query +="orgId="+managerId;
    		$(this).attr('href','/statistics/exportExecuteStatistics'+query);
    	})
    })
})
