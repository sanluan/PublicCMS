${"<@_"+entityName?replace('Cms','')?uncap_first+"List"} <#include "../include_condition/paramter.ftl">>
<form id="pagerForm" method="post">
	<#noparse><#include "../include_page/queryParamters.html"/></#noparse>
</form>
<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" method="post">
		<div class="searchBar">
			<ul class="searchContent">
			<#list conditionList as a><#if "Date"=a.type>
				<li class="colspan">
					<label>${a.title}：</label>
					<input type="text" name="queryStart${a.name?cap_first}" size="15" class="date" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-{%d}" value="${r"${queryStart"+a.name?cap_first+"!}"}" />
					<span>-</span>
					<input type="text" name="queryEnd${a.name?cap_first}" size="15" class="date" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-{%d}" value="${r"${queryEnd"+a.name?cap_first+"!}"}" />
				</li>
				<#else>
				<li>
					<label>${a.title}：</label>
					<input type="text" name="query${a.name?cap_first}" value="${r"${query"+a.name?cap_first+"!}"}" />
				</li>
				</#if></#list>
			</ul>
			<#noparse><#include "../include_page/searchBar.html"/></#noparse>
		</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="${entityName?uncap_first}/add.html" target="navtab" rel="${entityName?uncap_first}Add"><span>添加</span></a></li>
			<li><a class="edit" href="${entityName?uncap_first}/add.html?navTabId=${entityName?uncap_first}&id={sid}" target="navtab" rel="${entityName?uncap_first}Add"><span>修改</span></a></li>
			<li><a class="delete" href="${entityName?uncap_first}/delete.do?id={sid}" title="确定要删除该条记录吗?" target="ajaxTodo"><span>删除</span></a></li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="119">
		<thead>
			<tr>
				<#list columnList as a>
				<th<#if a.order> orderField="${a.name}" class="<#noparse><#if orderField??&&</#noparse>'${a.name}'<#noparse>==orderField><#if 'asc'=orderType>asc<#else>desc</#if><#else>order</#if></#noparse>"</#if>>${a.title}</th>
				</#list>
			</tr>
		</thead>
		<tbody>
			<#noparse><#list page.list as a>
			<tr target="sid" rel="${a.id}">
			</#noparse>
				<#list columnList as a>
				<td>${r"${a."+a.name+"!}"}</td>
				</#list>
			<#noparse>
			</tr>
			</#list></#noparse>
		</tbody>
	</table>
	<#noparse><#include "../include_page/page.html"/></#noparse>
</div>
${"</@_"+entityName?replace('Cms','')?uncap_first+"List>"}