layui.use(['element', 'form', 'layer', 'jquery'], function() {
    var $ = layui.jquery,form = layui.form(),element = layui.element();
    var layer = layui.layer;
    var global = {};
    var wxId = window.utils.getRequestParam("wxId")
    var menuId = "";
    // 加载
    function loading(){
    	var obj = new Object();
    	obj.weixinId = wxId
        $.ajax({
            url:'/weixinConfigure/getPageWinxinMenu ',
            type:'POST',
            data: $.toJSON(obj),
            dataType:'json',
            contentType: "application/json",
            success : function(data){
                if(data.status === 200 && data.resultCode.code === 'SUCCESS'){
                    var list = data.items,html = '',num = 0;
                    if(list.length <= 0){
                    	layer.msg("暂无数据!");return;
                    }
                    while(list && (list.length > num)){
                        var item = list[num];
                        html += '<tr>';
                        html += '<td>'+item.menuName+'</td>';
                        html += '<td>'+(!item.parentId?'是':'否')+'</td>';
                        html += '<td>'+(!item.parentId?'':item.menuName)+'</td>';
                        html += '<td>'+item.menuUrl+'</td>';
                        html += '<td>';
                        html += '<button class="layui-btn layui-btn-small edit-node" customId="'+item.id+'">修改节点</button>';
                        html += '<button class="layui-btn layui-btn-danger layui-btn-small del-node" customId="'+item.id+'" customName="'+item.menuName+'">删除节点</button>';
                        html += '</td>';
                        html += '</tr>';
                        if(item.listWeixinMenu && item.listWeixinMenu.length>0){
                            html += eachHTMl(item.menuName,item.listWeixinMenu);
                        }
                        num++;
                    }
                    html && $('#table_body').html(html);
                    global.data = function(){
                        var tempArr = [];
                        if(list){
                        	for(var i=0;i<list.length;i++){
                                var item = list[i];
                                if(item.listWeixinMenu && item.listWeixinMenu.length > 0){
                                    tempArr = tempArr.concat(item.listWeixinMenu);
                                    // delete item.children;
                                }
                                tempArr.push(item);
                            }
                        }
                        return tempArr;
                    }();
                    if(global.data && global.data.length>0){
                        global.selectData = function(){
                            var tempArr = [];
                            for(var i=0;i<global.data.length;i++){
                                var item = global.data[i];
                                if(!item.parentId){
                                    tempArr.push(item);
                                }
                            }
                            return tempArr;
                        }();
                    }
                    bind();
                }else{
                    console.error(data);
                    layer.msg(data.resultCode.message);
                }
            },
            error:function(error){
                layer.closeAll();
                console.error(error)
                layer.msg('查询失败!');
            }
        });
    };

    loading();

    form.verify({
        navName: function(value, item){ //value：表单的值、item：表单的DOM对象
            if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)){return '导航名称不能有特殊字符';}
            if(/(^\_)|(\__)|(\_+$)/.test(value)){return '导航名称首尾不能出现下划线\'_\'';}
            if(/^\d+\d+\d$/.test(value)){return '导航名称不能全为数字';}
            global.data && global.data.some(function(item){
                return (item.menuName == value)?'导航名称已存在，请重新填写':false;
            });
        }
    });     
    //监听提交
    form.on('submit(addNodeform)', function(data) {
        var obj = new Object();
        var url = '';
        if(!menuId){//新增
        	url = '/weixinConfigure/addWeixinMenu'
        }else{//修改
        	url = '/weixinConfigure/modifyWeixinMenu';
        	obj.id = menuId;
        }
        obj.menuName = $("#menuName").val();
        obj.menuUrl = $("#menuUrl").val();
        obj.isParent = $("#parentId").val() == "0"?"1":$("#parentId").val()
        obj.parentId = $("#parentId").val();
        obj.weixinId = wxId
        $.ajax({
            url:url,
            type:'POST',
            data:$.toJSON(obj),
            dataType:'json',
            contentType:'application/json',
            success:function(data){
                if(data.status === 200 && data.resultCode.code === 'SUCCESS'){
                	layer.closeAll();
                    layer.msg('添加成功!');
                    loading();
                    $('button[type="reset"]').click();
                }else{
                    layer.msg(data.resultCode.message);
                }
            },
            error:function(error){
                layer.closeAll();
                layer.msg('添加失败!');
            }
        });
        return false;
    });
    //监听radio
    form.on('radio(isParentRadio)', function(data){
        if(data.value==='1'){
            $("#parentId").find("option").attr("selected",false).find("option[text='#']").attr("selected");
        }
        $('#parentId').attr('disabled',(data.value==='1')?true:false);
        form.render('select');
    });
    // 菜单生效
    $('.use-node').on('click',function(e){
    	$.ajax({
            url:'/api/WeiXin/gen_weixiaoche_menu.action?weixinId='+wxId,
            type:'get',
            dataType:'json',
            success:function(data){
                if(data.status === 200 && data.resultCode.code === 'SUCCESS'){
                    layer.msg('菜单生效成功!');
                }else{
                    layer.msg(data.resultCode.message);
                }
            },
            error:function(error){
                console.error(error);
                layer.closeAll();
                layer.msg('菜单生效失败!');
            }
        });
    })
    // 新增节点
    $('.add-node').on('click',function(e){
    	menuId = "";
    	$("input[name='isParent']").removeAttr("disabled")
        if(global.selectData){
            $('#parentId').html('<option value="0">无</option>');
            for(var i=0;i<global.selectData.length;i++){
                var item = global.selectData[i];
                $('#parentId').append('<option value="'+item.id+'">'+item.menuName+'</option>');
            }
        }
        $('input:radio[name="isParent"]').get(0).checked = true;
        $('#parentId').find("option[value='#']").attr("selected",true);
        $('#parentId').attr('disabled',true);
        layer.open({
            title:'新增导航信息',
            type: 1,
            area: ['650px', '350px'],//宽高
            btnAlign: 'c',
            shade: 0.5,
            content: $('#tipForm_add') 
        });
        e.preventDefault();
        form.render()
    });
    // 生成子节点
    function eachHTMl(parentName,childenArr){
        var html = '';
        var ii = 0;
        var iconHove = '<i class="layui-icon" style="font-size: 24px; color: #1E9FFF;vertical-align: middle;padding-right:10px;">&#xe602;</i>';  
        while(childenArr && (childenArr.length > ii)){
            var item = childenArr[ii];
            html += '<tr>';
            html += '<td>'+iconHove + item.menuName+'</td>';
            html += '<td>'+(!item.parentId?'是':'否')+'</td>';
            html += '<td>'+parentName+'</td>';
            html += '<td>'+item.menuUrl+'</td>';
            html += '<td>';
            html += '<button class="layui-btn layui-btn-small edit-node" customId="'+item.id+'">修改节点</button>';
            html += '<button class="layui-btn layui-btn-danger layui-btn-small del-node" customId="'+item.id+'" customName="'+item.menuName+'">删除节点</button>';
            html += '</td>';
            ii++;
        }
        return html;
    };
    // 绑定事件
    function bind(){
        // 修改节点
        $('.edit-node').on('click',function(){
            var customId = $(this).attr('customId');
            menuId = customId;
            $('#navigation_id').val(customId);
            var editItem = null;
            for(var i=0;i< global.data.length;i++){
                var item = global.data[i];
                if(item.id == customId*1){
                    editItem = item;
                    break ;
                }
            }
            if(editItem){
                // 打开修改页面，将对象赋值，然后修改
                layer.open({
                    title:'修改菜单信息',
                    type: 1,
                    area: ['650px', '350px'],//宽高
                    shadeClose: true,//开启遮罩关闭
                    content: $('#tipForm_add') 
                });
                if(global.selectData){
                    $('#parentId').html('<option value="0">无</option>');
                    for(var i=0;i<global.selectData.length;i++){
                        var item = global.selectData[i];
                        if(item.id != customId){
                            $('#parentId').append('<option value="'+item.id+'">'+item.menuName+'</option>');
                        }
                    }
                }
                $('#menuName').val(editItem.menuName);
                $('#menuUrl').val(editItem.menuUrl);
                $("#parentId").find("option").attr("selected",false)
                $("input:radio[name='isParent']").removeAttr("checked"); 
                if(editItem.parentId || editItem.parentId>0){
                    $('#navigation_isParent0').get(0).checked = true;
                    $("#parentId").find("option[value='"+editItem.parentId+"']").attr("selected",true);
                    $('#parentId').attr('disabled',false);
                }else{
                    $('#navigation_isParent1').get(0).checked = true;
                    $('#parentId').find("option[value='#']").attr("selected",true);
                    $('#parentId').attr('disabled',true);

                }
                $("input:radio[name='isParent']").attr("disabled",true)
                $('#parentId').attr('disabled',true);
                form.render();
            }
        });
        // 删除节点
        $('.del-node').on('click',function(e){
            var customId = $(this).attr('customId');
            var customName = $(this).attr('customName');
            layer.confirm('确定删除当前菜单？', {
                shadeClose: true, //开启遮罩关闭
                btn: ['确定', '取消'] //按钮
            }, function() {
            	$.ajax({
                    url:'/weixinConfigure/removeWeixinMenu?id='+customId,
                    type:'get',
                    dataType:'json',
                    success:function(data){
                        if(data.status === 200 && data.resultCode.code === 'SUCCESS'){
                            loading();
                            layer.msg('删除成功!');
                        }else{
                            console.error(data);
                            layer.msg(data.resultCode.message);
                        }
                    },
                    error:function(error){
                        console.error(error);
                        layer.closeAll();
                        layer.msg('删除失败!');
                    }
                });
            }, function() {
            	layer.closeAll();
            });
        });
    };
});