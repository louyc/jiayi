/**
 * Created by shiyq on 2017/5/23.
 */
layui.use(['jquery','element','layer'], function(){
    var layer = layui.layer, $ = layui.jquery;
    var element = layui.element(); //导航的hover效果、二级菜单等功能，需要依赖element模块
    //监听导航点击
    element.on('nav(test)', function(elem){
        //给IFrame的src赋值，控制跳转
        $("iframe").attr("src",$(this).children("a").attr("data-href")+".html");
        var firstMenu = $(this).parents("dl").prev("a").text();
        var secondMenu = elem.context.innerText;
        $(".layui-breadcrumb").children("a").eq(0).text(firstMenu);
        $(".layui-breadcrumb").children("a").children("cite").text(secondMenu);
        element.init();
    });
});

