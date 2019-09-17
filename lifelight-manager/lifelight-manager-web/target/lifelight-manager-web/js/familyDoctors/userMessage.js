window.onload=function(){
	layui.use(['element','layer','jquery','form','laypage','laydate'],function(){
		var $=layui.jquery,layer = layui.layer, laydate = layui.laydate,form = layui.form(),element = layui.element();
        var laypage = layui.laypage;
		var user_id=window.utils.getCookie("user_id");
		var pageSum = "";
        var messageFrom = window.utils.getRequestParam("managerId");//消息用户id
        var userName = window.utils.getRequestParam("userName");//消息用户名
        getMessage(1)
        function getMessage(pageNum){
        	var _obj=new Object();
        	_obj.messageTo=user_id;
        	_obj.messageFrom=messageFrom;
        	_obj.type=3;
        	_obj.status="";
        	_obj.equipment=2;
        	_obj.showCount=10;
        	_obj.currentPage=pageNum;
        	var dataJson=$.toJSON(_obj);
        	$.ajax({
        		url:'/message/getPageMessages',
        		type:'POST',
        		data:dataJson,
        		dataType:"json",
        		contentType: "application/json",
        		success:function(data){
        			if(data.successful){
        				pageSum = data.pagesCount;
        				var str='';
        				var datas=data.items;
        				if(datas.length <= 0){
        					layer.msg("暂无对话！");return;
        				}
        				$.each(datas,function(index,item){
        					if(item.messageTo == user_id){//用户发给医生
        						str+='<tr><td>';
            					str+='<p style="line-height: 24px;">'+userName+'：</p>';
            					str+='<div style="padding: 0 30px">';
            					var content = item.content;
            					if(!!content.split("<img")[1]){
    	        					var content2 = ' style="max-height: 150px;"'+content.split("<img")[1]
    	        					var content1 = content.split("<img")[0] + '<img';
            						var content3 =  content1+""+content2;
            						str+=content3;
            					}else{
            						str+=content;
            					}
            					str+='</div>';
            					str+='<p style="margin-top: 15px;padding: 0 30px">'+item.createTime+'</p>';
            					str+='</td></tr>';
        					}else{
        						str+='<tr><td>';
            					str+='<p style="line-height: 24px;">我：</p>';
            					str+='<div style="padding: 0 30px">';
            					var content = item.content;
            					if(!!content.split("<img")[1]){
    	        					var content2 = ' style="max-height: 150px;"'+content.split("<img")[1]
    	        					var content1 = content.split("<img")[0] + '<img';
            						var content3 =  content1+""+content2;
            						str+=content3;
            					}else{
            						str+=content;
            					}
            					str+='</div>';
            					str+='<p style="margin-top: 15px;padding: 0 30px">'+item.createTime+'</p>';
            					str+='</td></tr>';
        					}
        					
        				})
        				$(".layui-table tbody").html(str);
        				//分页
        				laypage({
        					cont: 'pagging'
    						, pages: pageSum
    						, curr: data.pageNumber||1
    						,jump: function(obj, first){
    							if(!first){
    								pageNum = obj.curr;
    								getMessage(pageNum);
    							}
    						}
        				});
        			}
        		}
        	})
        }
        $("#copyThat").on("click",function(){
        	var userId = messageFrom;
        	layer.open({
        		type:2
        		,title:'消息推送'
    			,area: ['500px','500px']
        		,content: '/html/data/ueEditMessage.html'
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
            			url:'/message/sendDoctorMessage?doctorId='+user_id+'&toUserId='+userId+'&context='+goodsDesc,
            			type: 'POST',
        	        	success:function(res){
        	        		if(res.successful && res.resultCode.code == 'SUCCESS'){
        	        			layer.msg("消息推送成功！",{time: 2000,zIndex: layer.zIndex},function(){
        	        				layer.closeAll();
        	        				window.location.reload();
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
    })
}