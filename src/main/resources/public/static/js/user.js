

function searchUsers() {
    $("#dg").datagrid("load",{
        userName:$("#s_userName").val(),
        phone:$("#s_phone").val(),
        trueName:$("#s_trueName").val()
    });
}



function clearFromDate() {
    $("#userName").val("");
    $("#trueName").val("");
    $("#email").val("");
    $("#phone").val("");
}

function openUserAddDialog() {
    baseOpenDialog("dlg","添加用户");
}

function openUserModifyDialog() {
    var rows=$("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选择待修改的数据!","error");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","暂不支持批量修改!","error");
        return;
    }

    rows[0].roleIds=rows[0].rids.split(",");
    $("#fm").form("load",rows[0]);

    baseOpenDialog("dlg","用户更新");
}


function closeUserDialog() {
    closeDialog("dlg");
}


function saveOrUpdateUser() {
    saveOrUpdateRecode(ctx+"/user/save",ctx+"/user/update","dlg",searchUsers,clearFromDate);
}


function deleteUser() {
    deleteDialog("dg",ctx+"/user/delete",searchUsers);
}