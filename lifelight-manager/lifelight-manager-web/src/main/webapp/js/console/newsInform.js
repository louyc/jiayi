window.onload=function(){
	layui.use(['element','layer','jquery','form','laydate','laypage'],function(){
		var $=layui.jquery,layer = layui.layer, laydate = layui.laydate,form = layui.form(),element = layui.element();
		var laypage = layui.laypage;
		var pageSum = "";
		var layid = location.hash.replace(/^#test=/, '');
        element.tabChange('test', layid);
        element.on('tab(test)', function(elem){
            location.hash = 'test='+ $(this).attr('lay-id');
        });
        //消息列表
        var cookieVal=window.utils.getCookie("user_id");
        var obj=new Object();
    	obj.messageTo=cookieVal;obj.showCount=1000;obj.currentPage=1,obj.type =2;
    	var dataJson=$.toJSON(obj);
    	$.ajax({
    		url:'/message/getPageMessages',
    		type:'POST',
    		data:dataJson,
    		dataType:"json",
    		contentType: "application/json",
    		success:function(data){
    			if(data.successful){
    				var str='';
    				var datas=data.items;
    				$.each(datas,function(index,item){
    					str+='<p>'+item.content+'<span>'+item.createTime+'</span></p>'
    					/*if(item.userStatus == 1){
        				str+='<p>'+item.content+'<span>'+item.createTime+'</span></p>'
        			}else if(item.userStatus == 3){
        				str+='<p>'+item.content+'<a href="/html/supplyInfo.html" class="examine">请重新提交</a></p>'
        			}*/
    				})
    				/*$(".layui-message .layui-list").html(str);*/
    			}
    		}
    	})
        getMessage(1)
        function getMessage(pageNum){
        	var _obj=new Object();
        	_obj.messageTo=cookieVal;
        	_obj.showCount=10;
        	_obj.currentPage=pageNum;
        	_obj.type=3;
        	_obj.status=0;
        	_obj.equipment=2;
        	var dataJson=$.toJSON(_obj);
        	$.ajax({
        		url:'/message/selectMessageCount',
        		type:'POST',
        		data:dataJson,
        		dataType:"json",
        		contentType: "application/json",
        		success:function(data){
        			if(data.successful){
        				pageSum = data.pagesCount;
        				var str='';
        				var datas=data.data;
        				$.each(datas,function(index,item){
        					str+='<div class="layui-list userMessage">';
        					var messageFromName = (!item.messageFromName?"":item.messageFromName)
        					str += '<p data-from="'+item.messageForm+'" class="userMessageTittle">来自： '+messageFromName+' 的消息 点击查看</p>';
        					str += '<i class="unReadNum">'+item.countNum+'</i>';
    						str += '</div>';
        				})
        				$(".tab-first").html(str);
        				blind();
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
    	function blind(){
    		$(".userMessageTittle").on("click",function(){
    			var messageFrom = $(this).attr("data-from");
    			window.location.href="newsInformInfo.html?messageFrom="+messageFrom
    		});
            $(".copyUsers").on("click",function(){
            	var userId = $(this).attr("userId");
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
                			url:'/message/sendDoctorMessage?doctorId='+doctorId+'&toUserId='+userId+'&context='+goodsDesc,
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
    	}
        function getAllMessage(){
        	$(this).attr("height","auto")
        }
    })
}