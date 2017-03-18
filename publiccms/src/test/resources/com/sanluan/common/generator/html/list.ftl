<div class="pageHeader">
	<form id="pagerForm" onsubmit="return navTabSearch(this);" method="post">
		<#noparse><#include "../include_page/paramters.html"/></#noparse>
		<div class="searchBar">
			<ul class="searchContent">
			<#list conditionList as a><#if "Date"=a.type>
				<li class="colspan">
					<label>${a.title}：</label>
					<input type="text" name="start${a.name?cap_first}" size="15" class="date" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-{%d}" value="${r"${start"+a.name?cap_first+"!}"}" />
					<span>-</span>
					<input type="text" name="end${a.name?cap_first}" size="15" class="date" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-{%d}" value="${r"${end"+a.name?cap_first+"!}"}" />
				</li>
				<#else>
				<li>
					<label>${a.title}：</label>
					<input type="text" name="${a.name}" value="${r"${"+a.name+"!}"}" />
				</li>
				</#if></#list>
			</ul>
			<div class="subBar">
				<ul>
					<li><button type="submit" class="buttonActive">搜索</button></li>
				</ul>
			</div>
		</div>
	</form>
</div>
<#noparse><@_sysAuthorized roleIds=admin.roles urls='</#noparse>${entityName?uncap_first}/add,${entityName?uncap_first}/delete<#noparse>'><#assign authorizedMap=map/></@_sysAuthorized></#noparse>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
		${'<#'}if authorizedMap['${entityName?uncap_first}/add']>
			<li><a href="${entityName?uncap_first}/add.html" target="navTab" rel="${entityName?uncap_first}/add"><i class="icon-plus-sign-alt icon-large"></i> 添加</a></li>
			<li><a href="${entityName?uncap_first}/add.html?id={sid}" target="navTab" rel="${entityName?uncap_first}/edit"><i class="icon-edit icon-large"></i> 修改</a></li>
		${'</#'}if>
		${'<#'}if authorizedMap['${entityName?uncap_first}/delete']>
			<li><a href="${entityName?uncap_first}/delete.do" title="确定要删除该些记录吗?" target="selectedTodo" rel="ids"><i class="icon-trash icon-large"></i> 批量删除</a></li>
		${'</#'}if>
		</ul>
	</div>
${"<@_"+entityName?replace('Cms','')?uncap_first+"List"} <#include "../include_condition/paramter.ftl">>
	<table class="list" width="100%" layoutH="92">
		<thead>
			<tr>
				<#list columnList as a>
				<th<#if a.order> orderField="${a.name}" class="<#noparse><#if orderField??&&</#noparse>'${a.name}'<#noparse>==orderField><#if 'asc'=orderType>asc<#else>desc</#if><#else>order</#if></#noparse>"</#if>>${a.title}</th>
				</#list>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<#noparse><#list page.list as a>
			<tr target="sid" rel="${a.id}">
			</#noparse>
				<#list columnList as a>
				<td>${r"${a."+a.name+"!}"}</td>
				</#list>
				<td>
				${'<#'}if authorizedMap['${entityName?uncap_first}/delete']>
					<a href="${entityName?uncap_first}/delete.do?ids=<#noparse>${a.id}</#noparse>" title="确定要删除这条记录么?"  target="ajaxTodo">删除</a>
				${'</#'}if>
				</td>
			<#noparse>
			</tr>
			</#list></#noparse>
		</tbody>
	</table>
	<#noparse><#include "../include_page/page.html"/></#noparse>
${"</@_"+entityName?replace('Cms','')?uncap_first+"List>"}
</div>