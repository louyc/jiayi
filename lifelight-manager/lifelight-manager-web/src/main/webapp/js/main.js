/**
 * Created by shiyq on 2017/5/23.
 */
layui.use(['jquery','element','layer'], function(){
    var layer = layui.layer, $ = layui.jquery;
    var element = layui.element(); 
    
    var roleId = window.utils.getCookie("roleId");
    var userName = window.utils.getCookie("userName");
    var managerId = window.utils.getCookie("managerId");
    $("#userName").html("欢迎您，"+userName);
    //跟普通医生暂无首页
    if(roleId ==6){
    	$("iframe").attr("src","");
    	$('#layui-breadcrumb').children("a").children("cite").text("")
    }
    //机构首页统计
    if(roleId ==2){
    	$("iframe").attr("src","./familyDoctors_index_orgin.html");
    	$('#layui-breadcrumb').children("a").children("cite").text("统计")
    }
    if(roleId ==1){
    	//家庭医生的首页
    	if(window.utils.getCookie("doctor_type") == 1){
    		$("iframe").attr("src","/html/familyDoctorHome.html");
    		$(".layui-breadcrumb").children("a").children("cite").text("控制台");
    		/*$("iframe").attr("src","");*/
    		$(".familyDoctorMsg").show()
    		noReadedMsg();
    	}else{
    		$("iframe").attr("src","");
    	}
    }
    /*$("iframe").attr("src","/html/familyDoctorHome.html");*/
    /*$(".familyDoctorMsg").show();*/
    $("#checkMessage").click(function(){
    	 $("iframe").attr("src","/html/console/newsInform.html");
    })
    function noReadedMsg(){
    	var _obj=new Object();
    	_obj.messageTo=managerId;
    	_obj.showCount=1000;
    	_obj.currentPage=1;
    	_obj.type=3;
    	_obj.status=0;
    	_obj.equipment=2;
    	var dataJson=$.toJSON(_obj);
    	$.ajax({
    		url:'/message/getPageMessages',
    		type:'POST',
    		data:dataJson,
    		dataType:"json",
    		contentType: "application/json",
    		success:function(data){
    			if(data.successful && data.items.length>0){
    				$("#checkMessage .layui-badge-dot").show()
    			}else{
    				$("#checkMessage .layui-badge-dot").hide()
    			}
    		}
    	})
    }
	//根据角色显示菜单
	$.ajax({
		url:'/menu/query',
		type:'POST',
		ansyc:false,
		data:{"roleId":roleId},
		dataType:'json',
		success : function(menuList){
			var lists="";
			if(menuList.data.length>0){
				for(var i=0;i<menuList.data.length;i++){
					var list="";
					var ifshow = false;
					if(menuList.data[i].children.length>0){
						for(var j=0;j<menuList.data[i].children.length;j++){
							//判断是否有能显示的二级菜单，有则显示，并显示一级，无则都不显示
					        if(menuList.data[i].children[j].type == "1"){
					        	ifshow = true;
					        	list+='<dd><a href="#" data-href="'+menuList.data[i].children[j].url+'">'+menuList.data[i].children[j].name+'</a></dd>';
					        }
					    }
						if(ifshow){
							list = '<li class="layui-nav-item"><a href="#">'+menuList.data[i].name+'</a><dl class="layui-nav-child">'+list;
							list+='</dl></li>'
						}
					}
					lists+=list;
				}
				$("ul.layui-nav-tree").html(lists);
				element.init();
			}
		},
		error : function(data){
			layui.use('layer', function(){
				var layer = layui.layer;
			    layer.msg('请求菜单失败！')
			});
		}
	});
    element.on('nav(test)', function(elem){
    	
        $("iframe").attr("src",$(this).children("a").attr("data-href"));
        var firstMenu = $(this).parents("dl").prev("a").text();
        var secondMenu = elem.context.innerText;
        $(".layui-breadcrumb").children("a").eq(0).text(firstMenu);
        $(".layui-breadcrumb").children("a").children("cite").text(secondMenu);
        element.init();
    });
    
});

