DWZ.regPlugins.push(function($p){
    $(".searchBar .searchContent", $p).click(function(event){
        if($(event.target).is('ul')||$(event.target).is('li')){
            $content = $(this).closest(".page").find('.pageContent').find('[layouth]');
            var height = $(this).height();
            $(this).toggleClass('searchContentHover');
            $content.height($content.height()-$(this).height()+height);
        }
    });
});
DWZ.regPlugins.push(function($p){
    $('.tagsBox',navTab.getCurrentPanel()).each(function (){
        var $tagBox = $(this);
        var $callback = $tagBox.attr("callback");
        if ($callback && !$.isFunction($callback) ) {
            $callback = eval('(' + $callback + ')');
        }
        $tagBox.on('click','span>a',function(){
            $(this).parent().remove();
            if ($callback) {
                $callback();
            }
            return false;
        });
    });
    
});
DWZ.regPlugins.push(function($p){
    if($('.cmsVersion',$p).length ) {
        $.getJSON(Base64.decode('Ly9jbXMucHVibGljY21zLmNvbS9hcGkvZGlyZWN0aXZlL3ZlcnNpb24='), function(data) {
            var fullVersion=$('.cmsVersion a',$p).eq(0).text();
            var version=fullVersion.substring(0,fullVersion.lastIndexOf('.'));
            var revision=fullVersion.substring(fullVersion.lastIndexOf('.')+1);
            if(version!==data.cms ) {
                $('.cmsVersion .old',$p).show();
            } else {
                if(revision == data.revision){
                    $('.cmsVersion .new',$p).show();
                } else {
                    $('.cmsVersion .old',$p).css('color','gray').show();
                }
            }
        });
    }
});