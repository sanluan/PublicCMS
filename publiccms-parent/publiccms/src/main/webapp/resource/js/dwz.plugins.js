DWZ.regPlugins.push(function($p){
    (".searchBar .searchContent", $p).click(function(event){
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