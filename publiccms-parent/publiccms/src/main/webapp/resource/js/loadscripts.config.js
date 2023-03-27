(function () {
    window.editor = {
        ckeditorResources: [window.LOADSCRIPTS_BASE + "/resource/plugins/ckeditor/ckeditor.js"],
        ueditorResources: [window.LOADSCRIPTS_BASE + "/resource/plugins/ueditor/ueditor.cms.config.js",window.LOADSCRIPTS_BASE + "/resource/plugins/ueditor/ueditor.all.min.js",window.LOADSCRIPTS_BASE + "/resource/plugins/ueditor/lang/" + window.UEDITOR_LANGUAGE],
        tinymceResources: [window.LOADSCRIPTS_BASE + "/resource/plugins/tinymce/tinymce.min.js"],
        ckeditorInitd: false,
        ueditorInitd: false,
        tinymceInitd: false,
        ckeditorIniting: false,
        ueditorIniting: false,
        tinymceIniting: false,
        ckeditorArray:[],
        ueditorArray:[],
        tinymceArray:[],
        index: 0
    };
    window.codemirror = {
        base:window.LOADSCRIPTS_BASE + "/resource/plugins/codemirror/",
        resources: ["lib/codemirror.js", "mode/xml/xml.js","mode/javascript/javascript.js","mode/css/css.js",
            "mode/htmlmixed/htmlmixed.js", "mode/freemarker/freemarker.js","mode/freemarkermixed/freemarkermixed.js",
            "mode/nginx/nginx.js", "addon/hint/show-hint.js","addon/hint/javascript-hint.js","addon/hint/html-hint.js",
            "addon/hint/css-hint.js", "addon/hint/xml-hint.js", "addon/fold/foldcode.js", "addon/fold/foldgutter.js", "addon/fold/xml-fold.js", "addon/fold/indent-fold.js"],
        initd: false,
        initing: false,
        array: false,
        index: 0
    };
    window.photoclip = {
        resources: [window.LOADSCRIPTS_BASE + "/resource/plugins/photoclip/hammer.min.js",window.LOADSCRIPTS_BASE + "/resource/plugins/photoclip/iscroll-zoom-min.js",window.LOADSCRIPTS_BASE + "/resource/plugins/photoclip/lrz.all.bundle.js",window.LOADSCRIPTS_BASE + "/resource/plugins/photoclip/PhotoClip.min.js"],
        initd: false,
        index: 0
    };
    window.jsdiff = {
        resources: [window.LOADSCRIPTS_BASE + "/resource/plugins/jsdiff/diff.min.js"],
        initd: false,
        index: 0
    };
})();