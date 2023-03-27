/**
 * @author ZhangHuihua@msn.com
 */

/**
 * 普通ajax表单提交
 *
 * @param {Object}
 *            form
 * @param {Object}
 *            callback
 * @param {String}
 *            confirmMsg 提示确认信息
 */
function validateCallback(form, callback, confirmMsg) {
    var $form = $(form);
    $form.trigger(JUI.eventType.editorSync);
    if (!$form.valid() ) {
        return false;
    }
    $("textarea.editor[escape=true],textarea.code[escape=true]", $form).each(function() {
        $(this).val(html2Escape($(this).val()));
    });
    $("input[escape=true]", $form).each(function() {
        if($(this).val()){
            $(this).attr("escape",false);
            $(this).attr("maxlength",128);
            $(this).val(sha512($(this).val()));
        }
    });
    var _submitFn = function() {
        $.ajax({
            type: form.method || "POST", url: $form.attr("action"), data: $form.serializeArray(), dataType: "json", cache: false, success: callback || JUI.ajaxDone ,
            error: JUI.ajaxError
        });
    }
    if (confirmMsg ) {
        alertMsg.confirm(confirmMsg, {
            okCall: _submitFn
        });
    } else {
        _submitFn();
    }
    return false;
}
/**
 * 带文件上传的ajax表单提交
 *
 * @param {Object}
 *            form
 * @param {Object}
 *            callback
 */
function iframeCallback(form, callback) {
    var $form = $(form), $iframe = $("#callbackframe");
    if (!$form.valid() ) {
        return false;
    }
    if ($iframe.length == 0 ) {
        $iframe = $($.parseHTML("<iframe id=\"callbackframe\" name=\"callbackframe\" src=\"about:blank\" style=\"display:none\"></iframe>", document, true)).appendTo("body");
    }
    form.target = "callbackframe";
    _iframeResponse($iframe[0], callback || JUI.ajaxDone);
}
function _iframeResponse(iframe, callback) {
    var $iframe = $(iframe), $document = $(document);
    $document.trigger("ajaxStart");
    $iframe.on("load", null, null, function(event) {
        $iframe.off("load");
        $document.trigger("ajaxStop");

        if (iframe.src == "javascript:\"%3Chtml%3E%3C/html%3E\";" || // For
            // Safari
            iframe.src == "javascript:\"<html></html>\";" ) { // For FF, IE
            return;
        }
        var doc = iframe.contentDocument || iframe.document;

        // fixing Opera 9.26,10.00
        if (doc.readyState && doc.readyState != "complete" ) {
            return;
        }
        // fixing Opera 9.64
        if (doc.body && doc.body.innerHTML == "false" ) {
            return;
        }
        var response;
        if (doc.XMLDocument ) {
            // response is a xml document Internet Explorer property
            response = doc.XMLDocument;
        } else if (doc.body ) {
            try {
                response = $iframe.contents().find("body").text();
                response = JSON.parse(response);
            } catch (e) { // response is html document or plain text
                response = doc.body.innerHTML;
            }
        } else {
            // response is a xml document
            response = doc;
        }
        callback(response);
    });
}

/**
 * navTabAjaxDone是DWZ框架中预定义的表单提交回调函数． 服务器转回navTabId可以把那个navTab标记为reloadFlag=1,
 * 下次切换到那个navTab时会重新载入内容. callbackType如果是closeCurrent就会关闭当前tab
 * 只有callbackType="forward"时需要forwardUrl值
 * navTabAjaxDone这个回调函数基本可以通用了，如果还有特殊需要也可以自定义回调函数. 如果表单提交只提示操作是否成功, 就可以不指定回调函数.
 * 框架会默认调用DWZ.ajaxDone() <form action="/user.do?method=save" onsubmit="return
 * validateCallback(this, navTabAjaxDone)">
 * form提交后返回json数据结构statusCode=JUI.statusCode.ok表示操作成功, 做页面跳转等操作.
 * statusCode=JUI.statusCode.error表示操作失败, 提示错误原因.
 * statusCode=JUI.statusCode.timeout表示session超时，下次点击时跳转到DWZ.loginUrl
 * {"statusCode":"200", "message":"操作成功", "navTabId":"navNewsLi",
 * "forwardUrl":"", "callbackType":"closeCurrent", "rel"."xxxId"}
 * {"statusCode":"300", "message":"操作失败"} {"statusCode":"301", "message":"会话超时"}
 */
function navTabAjaxDone(json) {
    JUI.ajaxDone(json);
    if (json[JUI.keys.statusCode] == JUI.statusCode.ok ) {
        if (json.navTabId ) { // 把指定navTab页面标记为需要“重新载入”。注意navTabId不能是当前navTab页面的
            if("page" == json.rel){
                navTab.reloadFlag(json.navTabId, 2);
            }else{
                navTab.reloadFlag(json.navTabId);
            }
        } else { // 重新载入当前navTab页面
            var $pagerForm = $(".pagerForm", navTab.getCurrentPanel());
            if(0!=$pagerForm.length){
                $pagerForm.submit();
            }
        }
        if ("closeCurrent" == json.callbackType ) {
            setTimeout(function() {
                navTab.closeCurrentTab(json.navTabId);
            }, 100);
        } else if ("forward" == json.callbackType ) {
            if (json.navTabId ) {
                navTab.reload(json.forwardUrl, {
                    navTabId: json.navTabId
                });
            }else{
                navTab.reload(json.forwardUrl);
            }
        } else if ("forwardConfirm" == json.callbackType ) {
            alertMsg.confirm(json.confirmMsg || JUI.msg("forwardConfirmMsg"), {
                okCall: function() {
                    navTab.reload(json.forwardUrl);
                }, cancelCall: function() {
                    navTab.closeCurrentTab(json.navTabId);
                }
            });
        }
    }
}

/**
 * dialog上的表单提交回调函数 当前navTab页面有pagerForm就重新加载 服务器转回navTabId，可以重新载入指定的navTab.
 * statusCode=JUI.statusCode.ok表示操作成功, 自动关闭当前dialog
 * form提交后返回json数据结构,json格式和navTabAjaxDone一致
 */
function dialogAjaxDone(json) {
    JUI.ajaxDone(json);
    if (json[JUI.keys.statusCode] == JUI.statusCode.ok ) {
        if (json.navTabId ) {
            navTab.reload(json.forwardUrl, {
                navTabId: json.navTabId
            });
        } else {
            var $panel = navTab.getCurrentPanel();
            var $pagerForm = $(".pagerForm", navTab.getCurrentPanel());
            if(0!=$pagerForm.length){
                $pagerForm.submit();
            }
        }
        if ("closeCurrent" == json.callbackType ) {
            $.pdialog.closeCurrent();
        }
    }
}

/**
 * 处理navTab上的查询, 会重新载入当前navTab
 *
 * @param {Object}
 *            form
 */
function navTabSearch(form, navTabId) {
    var $form = $(form);
    navTab.reload($form.attr("action"), {
        data: $form.serializeArray(), navTabId: navTabId
    });
    return false;
}
/**
 * 处理dialog弹出层上的查询, 会重新载入当前dialog
 *
 * @param {Object}
 *            form
 */
function dialogSearch(form) {
    var $form = $(form);
    if (form[JUI.pageInfo.pageNum] ) {
        form[JUI.pageInfo.pageNum].value = 1;
    }
    $.pdialog.reload($form.attr("action"), {
        data: $form.serializeArray()
    });
    return false;
}
/**
 * 处理div上的局部查询, 会重新载入指定div
 *
 * @param {Object}
 *            form
 */
function divSearch(form, rel) {
    var $form = $(form);
    if (form[JUI.pageInfo.pageNum] ) {
        form[JUI.pageInfo.pageNum].value = 1;
    }
    if (rel ) {
        var $box = $("#" + rel);
        $box.ajaxUrl({
            type: "POST", url: $form.attr("action"), data: $form.serializeArray(), callback: function() {
                $box.find("[layoutH]").layoutH();
            }
        });
    }
    return false;
}
/**
 * @param {Object}
 *            args {pageNum:"",numPerPage:"",orderField:"",orderDirection:""}
 * @param String
 *            formId 分页表单选择器，非必填项默认值是 "pagerForm"
 */
function _getPagerForm($parent, args) {
    var form = $(".pagerForm", $parent).get(0);
    if (form ) {
        if (args["pageNum"] ) {
            form[JUI.pageInfo.pageNum].value = args["pageNum"];
        }
        if (args["numPerPage"] ) {
            form[JUI.pageInfo.numPerPage].value = args["numPerPage"];
        }
        if (args["orderField"] ) {
            form[JUI.pageInfo.orderField].value = args["orderField"];
        }
        if (args["orderDirection"] && form[JUI.pageInfo.orderDirection] ) {
            form[JUI.pageInfo.orderDirection].value = args["orderDirection"];
        }
    }
    return form;
}

/**
 * 处理navTab中的分页和排序 targetType: navTab 或 dialog rel: 可选 用于局部刷新div id号 data:
 * pagerForm参数 {pageNum:"n", numPerPage:"n", orderField:"xxx",
 * orderDirection:""} callback: 加载完成回调函数
 */
function dwzPageBreak(options) {
    var op = $.extend({
        targetType: "navTab", rel: "", data: {
            pageNum: "", numPerPage: "", orderField: "", orderDirection: ""
        }, callback: null
    }, options);
    var $parent = op.targetType == "dialog" ? $.pdialog.getCurrent(): navTab.getCurrentPanel();
    if (op.rel ) {
        var $box = $parent.find("#" + op.rel);
        var form = _getPagerForm($box, op.data);
        if (form ) {
            $box.ajaxUrl({
                type: "POST", url: $(form).attr("action"), data: $(form).serializeArray(), callback: function() {
                    $box.find("[layoutH]").layoutH();
                }
            });
        }
    } else {
        var form = _getPagerForm($parent, op.data);
        var params = $(form).serializeArray();
        if (op.targetType == "dialog" ) {
            if (form ) {
                $.pdialog.reload($(form).attr("action"), {
                    data: params, callback: op.callback
                });
            }
        } else {
            if (form ) {
                navTab.reload($(form).attr("action"), {
                    data: params, callback: op.callback
                });
            }
        }
    }
}
/**
 * 处理navTab中的分页和排序
 *
 * @param args
 *            {pageNum:"n", numPerPage:"n", orderField:"xxx", orderDirection:""}
 * @param rel：
 *            可选 用于局部刷新div id号
 */
function navTabPageBreak(args, rel) {
    dwzPageBreak({
        targetType: "navTab", rel: rel, data: args
    });
}
/**
 * 处理dialog中的分页和排序 参数同 navTabPageBreak
 */
function dialogPageBreak(args, rel) {
    dwzPageBreak({
        targetType: "dialog", rel: rel, data: args
    });
}
function ajaxTodo(url, callback) {
    var $callback = callback || navTabAjaxDone;
    if ("function" !== typeof $callback ) {
        $callback = eval("(" + callback + ")");
    }
    $.ajax({
        type: "POST", url: url, dataType: "json", cache: false, success: $callback, error: JUI.ajaxError
    });
}

/**
 * http://www.uploadify.com/documentation/uploadify/onqueuecomplete/
 */
function uploadifyQueueComplete(queueData) {
    var msg = "The total number of files uploaded: " + queueData.uploadsSuccessful + "<br/>" + "The total number of errors while uploading: " + queueData.uploadsErrored + "<br/>"
            + "The total number of bytes uploaded: " + queueData.queueBytesUploaded + "<br/>" + "The average speed of all uploaded files: " + queueData.averageSpeed;
    if (queueData.uploadsErrored ) {
        alertMsg.error(msg);
    } else {
        alertMsg.correct(msg);
    }
}
/**
 * http://www.uploadify.com/documentation/uploadify/onuploadsuccess/
 */
function uploadifySuccess(file, data, response) {
    alert(data)
}

/**
 * http://www.uploadify.com/documentation/uploadify/onuploaderror/
 */
function uploadifyError(file, errorCode, errorMsg) {
    alertMsg.error(errorCode + ": " + errorMsg);
}

/**
 * http://www.uploadify.com/documentation/
 *
 * @param {Object}
 *            event
 * @param {Object}
 *            queueID
 * @param {Object}
 *            fileObj
 * @param {Object}
 *            errorObj
 */
function uploadifyError(event, queueId, fileObj, errorObj) {
    alert("event:" + event + "\nqueueId:" + queueId + "\nfileObj.name:" + fileObj.name + "\nerrorObj.type:" + errorObj.type + "\nerrorObj.info:" + errorObj.info);
}
$.fn.extend({
    ajaxTodo: function() {
        return this.each(function() {
            var $this = $(this);
            $this.click(function(event) {
                var url = $this.attr("href").replaceTmById($(event.target).parents(".unitBox:first"));
                JUI.debug(url);
                if (!url.isFinishedTm() ) {
                    alertMsg.error($this.attr("warn") || JUI.msg("alertSelectMsg"));
                    return false;
                }
                var title = $this.attr("title");
                if (title ) {
                    alertMsg.confirm(title, {
                        okCall: function() {
                            ajaxTodo(url, $this.attr("callback"));
                        }
                    });
                } else {
                    ajaxTodo(url, $this.attr("callback"));
                }
                event.preventDefault();
            });
        });
    }, dwzExport: function() {
        function _doExport($this) {
            var $p = $this.attr("targetType") == "dialog" ? $.pdialog.getCurrent(): navTab.getCurrentPanel();
            var $form = $(".pagerForm", $p);
            var url = $this.attr("href");
            window.location = url + ( url.indexOf("?") == -1 ? "?": "&" ) + $form.serialize();
        }
        return this.each(function() {
            var $this = $(this);
            $this.click(function(event) {
                var title = $this.attr("title");
                if (title ) {
                    alertMsg.confirm(title, {
                        okCall: function() {
                            _doExport($this);
                        }
                    });
                } else {
                    _doExport($this);
                }
                event.preventDefault();
            });
        });
    }
});