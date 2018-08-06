/**
 * @author ZhangHuihua@msn.com
 */
(function($){
    // jQuery validate
    $.extend($.validator.messages, {
        required: "必填字段",
        remote: "请修正该字段",
        email: "请输入正确格式的电子邮件",
        url: "请输入合法的网址",
        date: "请输入合法的日期",
        dateISO: "请输入合法的日期 (ISO).",
        number: "请输入合法的数字",
        digits: "只能输入整数",
        creditcard: "请输入合法的信用卡号",
        equalTo: "请再次输入相同的值",
        accept: "请输入拥有合法后缀名的字符串",
        maxlength: $.validator.format("长度最多是 {0} 的字符串"),
        minlength: $.validator.format("长度最少是 {0} 的字符串"),
        rangelength: $.validator.format("长度介于 {0} 和 {1} 之间的字符串"),
        range: $.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
        max: $.validator.format("请输入一个最大为 {0} 的值"),
        min: $.validator.format("请输入一个最小为 {0} 的值"),

        alphanumeric: "必须是字母、数字、下划线",
        lettersonly: "必须是字母",
        letterstart: "必须是字母、数字、下划线,首字符必须是字母",
        phone: "请输入合法的电话号码",
        domain: "请输入合法的域名"
    });

    // DWZ regional
    $.setRegional("datepicker", {
        dayNames: ['日', '一', '二', '三', '四', '五', '六'],
        monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
    });
    $.setRegional("alertMsg", {
        title:{error:"错误", info:"提示", warn:"警告", correct:"成功", confirm:"确认提示"},
        butMsg:{ok:"确定", yes:"是", no:"否", cancel:"取消"}
    });


    $.setMessage('statusCode_503','服务器当前负载过大或者正在维护!');
    $.setMessage('validateFormError','提交数据不完整，{0}个字段有错误，请改正后再提交!');
    $.setMessage('sessionTimout','会话超时，请重新登录!');
    $.setMessage('alertSelectMsg','请选择一条数据!');
    $.setMessage('forwardConfirmMsg','继续下一步!');
})(jQuery);