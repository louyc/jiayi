/**
 * Created by shiyq on 2017/5/23.
 */
layui.use(['jquery','element','layer'], function(){
    var layer = layui.layer, $ = layui.jquery;
    var element = layui.element(); //������hoverЧ���������˵��ȹ��ܣ���Ҫ����elementģ��
    //�����������
    element.on('nav(test)', function(elem){
        //��IFrame��src��ֵ��������ת
        $("iframe").attr("src",$(this).children("a").attr("data-href")+".html");
        var firstMenu = $(this).parents("dl").prev("a").text();
        var secondMenu = elem.context.innerText;
        $(".layui-breadcrumb").children("a").eq(0).text(firstMenu);
        $(".layui-breadcrumb").children("a").children("cite").text(secondMenu);
        element.init();
    });
});

