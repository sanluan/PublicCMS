(function () {
    window.editor = {
        ckeditorResources: [window.LOADSCRIPTS_BASE + "/resource/plugins/ckeditor/ckeditor.js"],
        ueditorResources: [window.LOADSCRIPTS_BASE + "/resource/plugins/ueditor/ueditor.config.js",window.LOADSCRIPTS_BASE + "/resource/plugins/ueditor/ueditor.all.min.js",window.LOADSCRIPTS_BASE + "/resource/plugins/ueditor/lang/" + window.UEDITOR_LANGUAGE],
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
        resources: [window.LOADSCRIPTS_BASE + "/resource/plugins/codemirror/lib/codemirror.js", window.LOADSCRIPTS_BASE + "/resource/plugins/codemirror/mode/xml/xml.js",window.LOADSCRIPTS_BASE + "/resource/plugins/codemirror/mode/javascript/javascript.js",window.LOADSCRIPTS_BASE + "/resource/plugins/codemirror/mode/css/css.js",
            window.LOADSCRIPTS_BASE + "/resource/plugins/codemirror/mode/htmlmixed/htmlmixed.js", window.LOADSCRIPTS_BASE + "/resource/plugins/codemirror/mode/freemarker/freemarker.js",window.LOADSCRIPTS_BASE + "/resource/plugins/codemirror/mode/freemarkermixed/freemarkermixed.js",
            window.LOADSCRIPTS_BASE + "/resource/plugins/codemirror/mode/nginx/nginx.js", window.LOADSCRIPTS_BASE + "/resource/plugins/codemirror/addon/hint/show-hint.js",window.LOADSCRIPTS_BASE + "/resource/plugins/codemirror/addon/hint/javascript-hint.js",window.LOADSCRIPTS_BASE + "/resource/plugins/codemirror/addon/hint/html-hint.js",
            window.LOADSCRIPTS_BASE + "/resource/plugins/codemirror/addon/hint/css-hint.js", window.LOADSCRIPTS_BASE + "/resource/plugins/codemirror/addon/hint/xml-hint.js"],
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