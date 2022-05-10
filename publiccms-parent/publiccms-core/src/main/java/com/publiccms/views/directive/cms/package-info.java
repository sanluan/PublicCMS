/**
 *  Cms directive<p>
 *  CMS指令
 *  指令是一种FreeMarker的扩展，既可以在模板中定义指令又可以用java类实现指令，在PublicCMS中对FreeMarker这种指令扩展方式进行了封装和延申。<p>
 *  这些指令和函数既支持在模板中调用，也支持http方式请求调用,参数规则也保持一致,实现httpEnabled方法返回false可以禁用http方式，实现needAppToken方法返回true可以启用安全验证，只有后台维护-&gt;应用授权中授权的应用通过其token才可请求。
 */
package com.publiccms.views.directive.cms;