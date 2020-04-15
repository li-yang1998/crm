<div region="west" style="width: 200px" title="导航菜单" split="true">
    <div class="easyui-accordion" data-options="fit:true,border:false">


        <#list modules as module >
            <div title="${(module.moduleName)!}" selected="true" iconCls="${(module.moduleStyle)!}" style="padding: 10px">
                <#list module.subModules as subModule>
                    <a href="javascript:openTab('${(subModule.moduleName)!}','${(subModule.url)!}','${(subModule.moduleStyle)!}')"
                       class="easyui-linkbutton"
                       plain="true" iconCls="${(subModule.moduleName)!}" style="width: 150px">${(subModule.moduleName)!}</a>

                </#list>
            </div>
            <#else>
            暂无权限
        </#list>


    </div>

</div>