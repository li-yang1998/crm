
function openTab(text, url, iconCls){
    if($("#tabs").tabs("exists",text)){
        $("#tabs").tabs("select",text);
    }else{
        var content="<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='" + url + "'></iframe>";
        $("#tabs").tabs("add",{
            title:text,
            iconCls:iconCls,
            closable:true,
            content:content
        });
    }
}


function logout() {
    $.messager.confirm("来自系统信息","确定要退出系统吗?",function (r) {
        if(r){
            $.removeCookie("userIdStr");
            $.removeCookie("userName");
            $.removeCookie("trueName");
            $.messager.alert("来自系统信息","系统将在3秒后自动退出..","info");
            setTimeout(function () {
                    window.location.href = ctx + "/index";
            },3000);
        }

    });
}

//关闭修改框
function closePasswordModifyDialog() {
    $("#dlg").dialog("close");
}

//打开修改框
function openPasswordModifyDialog() {
    $("#dlg").dialog("open").dialog("setTitle","密码修改");
}

function modifyPassword() {
    $("#fm").form("submit",{
        url:ctx+"/user/updatePassword",
        onSubmit:function () {
            return $("#fm").form("validate");
        },
        success:function (data) {
            data =JSON.parse(data);
            if(data.code==200){
                $.messager.alert("来自系统信息","密码修改成功,系统将在5秒后执行退出操作...","info");
                $.removeCookie("userIdStr");
                $.removeCookie("userName");
                $.removeCookie("trueName");
                setTimeout(function () {
                    window.location.href=ctx+"/index";
                },5000)
            }else{
                $.messager.alert("来自系统信息",data.msg,"error");
            }
        }
    })
}