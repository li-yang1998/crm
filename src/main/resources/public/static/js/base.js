/**
 * 打开对话框
 * @param dlgId
 * @param title
 */
function baseOpenDialog(dlgId,title) {
    $("#"+dlgId).dialog("open").dialog("setTitle",title);
}

/**
 * 关闭对话框
 * @param dlgId
 */
function closeDialog(dlgId) {
    $("#"+dlgId).dialog("close");
}

/**
 * 添加与更新记录
 * @param saveUrl    添加记录后端url 地址
 * @param updateUrl   更新记录后端url 地址
 * @param dlgId     对话框id
 * @param search    多条件搜索方法名
 * @param clearData   清除表单方法名
 */
function saveOrUpdateRecode(saveUrl,updateUrl,dlgId,search,clearData) {
    var url = saveUrl;
    if(!(isEmpty($("input[name='id']").val()))){
        url = updateUrl;
    }
    $("#fm").form("submit",{
        url:url,
        onSubmit:function () {
            return $("#fm").form("validate");
        },
        success:function (data) {
            data =JSON.parse(data);
            if(data.code==200){
                $.messager.alert("来自crm",data.msg,"info");
                closeDialog(dlgId);
                search();
                clearData();
            }else{
                $.messager.alert("来自crm",data.msg,"error");
            }
        }
    })
}


/**
 *
 * @param dataGridId    表格id
 * @param dlgId         对话框id
 * @param fromId        表单id
 * @param title         标题内容
 */
function openModifyDialog(dataGridId,formId,dlgId,title) {
    var rows=$("#"+dataGridId).datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选择待修改的数据!","error");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","暂不支持批量修改!","error");
        return;
    }

    $("#"+formId).form("load",rows[0]);
    baseOpenDialog(dlgId,title);
}


function deleteDialog(dataGridId,deleteUrl,search) {
    var rows = $("#"+dataGridId).datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert('来自rm',"请选择需要删除的记录!","error");
        return;
    }
    if(rows.length>1){
        $.messager.alert('来自rm',"抱歉,暂不能多条删除!","error");
        return;
    }

    $.messager.confirm('确认','您确认想要删除记录吗？',function(r){
        if (r){
            $.ajax({
                type:"post",
                url:deleteUrl,
                data:{
                    id:rows[0].id
                },
                dataType:"json",
                success:function (data) {
                    if(data.code==200){
                        $.messager.alert('来自rm',data.msg);
                        search();
                    }else{
                        $.messager.alert('来自rm',data.msg,"error");
                    }
                }
            })
        }
    });
}