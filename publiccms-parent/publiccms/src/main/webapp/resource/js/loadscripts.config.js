(function () {
    window.editor = {
        base:window.LOADSCRIPTS_BASE + "/resource/plugins/",
        ckeditorResources: ["ckeditor/ckeditor.js"],
        ueditorResources: ["ueditor/ueditor.config.js", "ueditor/ueditor.all.min.js", "ueditor/lang/" + window.UEDITOR_LANGUAGE],
        tinymceResources: ["tinymce/tinymce.config.js","tinymce/tinymce.min.js"],
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
        resources: ["lib/codemirror.css", "addon/hint/show-hint.css", "addon/fold/foldgutter.css", "theme/erlang-dark.css",
            "lib/codemirror.js", "mode/xml/xml.js", "mode/javascript/javascript.js", "mode/css/css.js", "mode/htmlmixed/htmlmixed.js",
            "mode/freemarker/freemarker.js","mode/freemarkermixed/freemarkermixed.js", "mode/nginx/nginx.js", "addon/hint/show-hint.js",
            "addon/hint/javascript-hint.js","addon/hint/html-hint.js", "addon/hint/css-hint.js", "addon/hint/xml-hint.js", 
            "addon/fold/foldcode.js", "addon/fold/foldgutter.js", "addon/fold/xml-fold.js", "addon/fold/indent-fold.js"],
        initd: false,
        initing: false,
        objArray: [],
        index: 0
    };
    window.imageEditor = {
        base:window.LOADSCRIPTS_BASE + "/resource/plugins/filerobot-image-editor/",
        resources: ["filerobot-image-editor.min.js","config.js","lang/" + window.LANGUAGE+".js"],
        initd: false,
        index: 0
    };
    window.jsdiff = {
        resources: [window.LOADSCRIPTS_BASE + "/resource/plugins/jsdiff/diff.min.js"],
        initd: false,
        index: 0
    };
    window.spectrum = {
        base:window.LOADSCRIPTS_BASE + "/resource/plugins/spectrum/",
        resources: ["spectrum.min.css", "spectrum.min.js", "i18n/jquery.spectrum-"+window.SPECTRUM_LANGUAGE+".js"],
        initd: false,
        initing: false,
        objArray: [],
    };
})();