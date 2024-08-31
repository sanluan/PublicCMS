(function () {
    window.TINYMCE_OPTIONS = {
            language: window.TIMYMCE_LANGUAGE,
            base_url: window.TIMYMCE_HOME_URL,
            plugins: 'preview importcss searchreplace autolink autosave save directionality code visualblocks visualchars fullscreen image link media template codesample table charmap pagebreak nonbreaking anchor insertdatetime advlist lists wordcount help charmap quickbars emoticons',
            editimage_cors_hosts: ['picsum.photos'],
            menubar: 'file edit view insert format tools table help',
            toolbar: 'undo redo | bold italic underline strikethrough | fontfamily fontsize blocks | alignleft aligncenter alignright alignjustify | outdent indent |  numlist bullist | forecolor backcolor removeformat | pagebreak | charmap emoticons | fullscreen  preview save print | insertfile image media link anchor codesample template | ltr rtl',
            toolbar_sticky: true,
            toolbar_sticky_offset: 108,
            autosave_ask_before_unload: true,
            autosave_interval: '30s',
            autosave_prefix: '{path}{query}-{id}-',
            autosave_restore_when_empty: false,
            autosave_retention: '2m',
            pagebreak_split_block: true,
            image_advtab: true,
            importcss_append: true,
            images_upload_url: base + '/tinymce/upload',
            images_reuse_filename: true,
            images_upload_base_path: window.TIMYMCE_RESOURCE_PREFIX,
            image_list: base + '/tinymce/imageList',
            image_prepend_url:window.TIMYMCE_RESOURCE_PREFIX,
            file_picker_callback: function (callback, value, meta) {
                var filetype=window.TIMYMCE_FILETYPES;
                switch(meta.filetype){
                    case 'image':
                        filetype='.png,.jpg,.jpeg,.gif,.bmp';
                        break;
                    case 'media':
                        filetype='.mp3, .mp4, .wmv, .mid,.flv,.swf,.mkv,.avi,.rm,.rmvb,.mpeg,.mpg,.ogg,.ogv,.mov';
                        break;
                    case 'file':
                    default:
                }
                var input = document.createElement('input');
                    input.setAttribute('type', 'file');
                    input.setAttribute('accept', filetype);
                input.trigger("click");
                input.onchange = function() {
                    var file = this.files[0];
                    var xhr, formData;
                    xhr = new XMLHttpRequest();
                    xhr.withCredentials = false;
                    xhr.open('POST', base + '/tinymce/upload');
                    xhr.onload = function() {
                        var json;
                        if (xhr.status != 200) {
                            alertMsg.error('HTTP Error: ' + xhr.status);
                            return;
                        }
                        json = JSON.parse(xhr.responseText);
                        if (!json || typeof json.location != 'string') {
                            alertMsg.error('Invalid JSON: ' + xhr.responseText);
                            return;
                        }
                        callback(window.TIMYMCE_RESOURCE_PREFIX+json.location);
                    };
                    formData = new FormData();
                    formData.append('file', file, file.name );
                    xhr.send(formData);
              };
          },
          templates: [
            { title: 'New Table', description: 'creates a new table', content: '<div class="mceTmpl"><table width="98%%"  border="0" cellspacing="0" cellpadding="0"><tr><th scope="col"> </th><th scope="col"> </th></tr><tr><td> </td><td> </td></tr></table></div>' },
            { title: 'Starting my story', description: 'A cure for writers block', content: 'Once upon a time...' },
            { title: 'New list with dates', description: 'New List with dates', content: '<div class="mceTmpl"><span class="cdate">cdate</span><br><span class="mdate">mdate</span><h2>My List</h2><ul><li></li><li></li></ul></div>' }
          ],
          template_cdate_format: '[Date Created (CDATE): %m/%d/%Y : %H:%M:%S]',
          template_mdate_format: '[Date Modified (MDATE): %m/%d/%Y : %H:%M:%S]',
          height: 600,
          image_caption: true,
          quickbars_selection_toolbar: 'bold italic | quicklink h2 h3 blockquote quickimage quicktable',
          noneditable_class: 'mceNonEditable',
          toolbar_mode: 'wrap',
          contextmenu: 'link image table',
          skin: 'oxide',
          content_css: 'default',
        };
})();