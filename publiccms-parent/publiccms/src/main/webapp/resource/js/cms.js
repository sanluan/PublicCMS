var cmsSupportVersion = "1.0";
function addTagType(id,name){
    if(name){
        name=name.trim();
    }
    if(id && name){
        $box = $("<span></span>").text(name).append("<a href=\"javascript:;\"><i class=\"icon-remove-sign\"></i></a>").append($("<input type=\"hidden\" name=\"tagTypes[].id\"/>").val(id));
        $(".tagsBox",navTab.getCurrentPanel()).append($box);
    }else if(name){
        $box = $("<span></span>").text(name).append("<a href=\"javascript:;\"><i class=\"icon-remove-sign\"></i></a>").append($("<input type=\"hidden\" name=\"tagTypes[].name\"/>").val(name));
        $(".tagsBox",navTab.getCurrentPanel()).append($box);
    }
    reIndexTagType();
    $("input[name=\"type[].id\"]",navTab.getCurrentPanel()).val("");
    $("input[name=\"type[].name\"]",navTab.getCurrentPanel()).val("");
}
function clickAddTagType(){
    $(".tagTypes .icon-ok",navTab.getCurrentPanel()).parent().click();
}
function reIndexTagType(){
    $(".tagsBox span",navTab.getCurrentPanel()).each(function(tagIndex){
        $("input[name$=\\.id]",this).attr("name","tagTypes["+tagIndex+"].id");
        $("input[name$=\\.name]",this).attr("name","tagTypes["+tagIndex+"].name");
    });
}
function addCategory(id, name, contentId){
    if(name){
        name=name.trim();
    }
    if(id && name && 0==$(".categoryIds input[name=categoryIds][value="+id+"]",navTab.getCurrentPanel()).length){
        $box = $("<span></span>").text(name).append("<a href=\"javascript:;\"><i class=\"icon-remove-sign\"></i></a>");
        if(contentId){
            $box.append($("<input type=\"hidden\" name=\"contentIds\"/>").val(contentId));
        }else{
            $box.append($("<input type=\"hidden\" name=\"categoryIds\"/>").val(id));
        }
        $(".categoryIds",navTab.getCurrentPanel()).append($box);
    }
    $("input[name=\"quoteCategoryId\"]",navTab.getCurrentPanel()).val("");
    $("input[name=\"categoryName\"]",navTab.getCurrentPanel()).val("");
}
function addTag(typeId, id, name){
    if(name){
        name=name.trim();
    }
    if(id && name){
        $box = $("<span></span>").text(name).append("<a href=\"javascript:;\"><i class=\"icon-remove-sign\"></i></a>").append($("<input type=\"hidden\" name=\"tags[].id\"/>").val(id));
        $(".tags_"+typeId,navTab.getCurrentPanel()).append($box);
    }else if(name){
        $box = $("<span></span>").text(name).append("<a href=\"javascript:;\"><i class=\"icon-remove-sign\"></i></a>").append($("<input type=\"hidden\" name=\"tags[].name\"/>").val(name)).append($("<input type=\"hidden\" name=\"tags[].typeId\"/>").val(typeId));
        $(".tags_"+typeId,navTab.getCurrentPanel()).append($box);
    }
    reIndexTag();
    $("input[name=\"tag["+typeId+"].id\"]",navTab.getCurrentPanel()).val("");
    $("input[name=\"tag["+typeId+"].typeId\"]",navTab.getCurrentPanel()).val("");
    $("input[name=\"tag["+typeId+"].name\"]",navTab.getCurrentPanel()).val("");
}
function clickAddTag(){
    $(".tags .icon-ok",navTab.getCurrentPanel()).parent().click();
}
function reIndexTag(){
    var tagIndex=0;
    $(".tags .tagsBox span",navTab.getCurrentPanel()).each(function(){
        $("input[name$=\\.id]",this).attr("name","tags["+tagIndex+"].id");
        $("input[name$=\\.name]",this).attr("name","tags["+tagIndex+"].name");
        $("input[name$=\\.typeId]",this).attr("name","tags["+tagIndex+"].typeId");
        tagIndex++;
    });
}
function checkCategoryModel(){
    $(".categoryModelContent",navTab.getCurrentPanel()).hide();
    $(".categoryModelContentPath",navTab.getCurrentPanel()).hide().find("input[name=contentPath]").removeClass("required");
    $(".categoryModel",navTab.getCurrentPanel()).show();
    $(".categoryModel li",navTab.getCurrentPanel()).each(function(){
        if($(this).find("input[type=checkbox][name$=\\.use]").is(":checked")){
            $(this).find("ul li input[name$=\\.use]").val("true");
            $(".categoryModelContent",navTab.getCurrentPanel()).show();
            if(0 != $(this).find("[name$=categoryModel\\.customContentPath]").not(":checked").length){
                $(".categoryModelContentPath",navTab.getCurrentPanel()).show();
                return ;
            }
        }
    });
}
function checkPageSize(){
    if(parseInt($("input[name=size]",navTab.getCurrentPanel()).val())>0){
        $(".placeExtend",navTab.getCurrentPanel()).show();
        $("textarea[name=content]",navTab.getCurrentPanel()).val($(".placeContent",navTab.getCurrentPanel()).val());
        $(".placeExtend input,.placeExtend textarea",navTab.getCurrentPanel()).removeAttr("disabled");
    } else {
        $(".placeExtend",navTab.getCurrentPanel()).hide();
        $("textarea[name=content]",navTab.getCurrentPanel()).val($(".content",navTab.getCurrentPanel()).val());
        $(".placeExtend input,.placeExtend textarea",navTab.getCurrentPanel()).attr("disabled","disabled");
    }
}
function addUser(id,name){
    if(name){
        name=name.trim();
    }
    if(id && name){
        $box = $("<span></span>").text(name).append("<a href=\"javascript:;\"><i class=\"icon-remove-sign\"></i></a>").append($("<input type=\"hidden\" name=\"adminIds\"/>").val(id));
        $(".adminIds",navTab.getCurrentPanel()).append($box);
    }
    $("input[name=\"userId\"]",navTab.getCurrentPanel()).val("");
    $("input[name=\"nickname\"]",navTab.getCurrentPanel()).val("");
}
var apiCounter=0;
function getApi(base,apisArray,authorizedApis){
    for (var api in apisArray){
        apiRequest(base,api,apisArray,authorizedApis);
    }
}
function apiRequest(base,api,apisArray,authorizedApis){
    $.ajax({
        url:base+apisArray[api],
        dataType: "json",
        success: function (dataList) {
            $(dataList).each(function(index,data){
                if("true"==data.needAppToken){
                    $(".authorizedApis a[data-id="+api+"]",navTab.getCurrentPanel()).next().append("<li><a tname=\"apis\" tvalue=\""+data.name+"\">"+data.name+"</a></li>");
                }
            });
            if(++apiCounter==apisArray.length){
                for(i=0; i<authorizedApis.length; i++){
                    $(".authorizedApis li a[tname=apis][tvalue=\""+authorizedApis[i]+"\"]",navTab.getCurrentPanel()).attr("checked","true");
                }
                $(".authorizedApis", navTab.getCurrentPanel()).addClass("tree").jTree();
                apiCounter=0;
            }
        }
    });
}
function commandParameter(command,parametersName){
    $("input[name=command]",navTab.getCurrentPanel()).val(command);
    $(".commandBox",navTab.getCurrentPanel()).empty();
    if(parametersName) {
        var parameters = parametersName.split(",");
        for(i=0; i<parameters.length; i++){
            $(".commandBox",navTab.getCurrentPanel()).append("<label>"+parameters[i]+":</label><input name=\"parameters\" type=\"text\" class=\"required\"/>").initUI();
        }
    }
}
function bringBackBatchValue(keys){
    var value =$("textarea[name=batchValue]",$.pdialog.getCurrent()).val();
    if(value){
        var list=[];
        var values = value.split("\n");
        $.each(values,function(m,v){
            var vs = v.split(",");
            var obj={};
            $.each(keys,function(n,k){
                if(n < vs.length){
                    obj[k]=vs[n].trim();
                }
            });
            list.push(obj);
        });
        $.batchBringBack(list);
    }
}
var diyMenuTimer,diyButtonTimer,diyTimer;
window.addEventListener("message", function(event) {
    var op = event.data;
    if (op.diyevent) {
        if(op.url) {
            if("load" === op.diyevent) {
                if(diyTimer){
                    clearTimeout(diyTimer);
                    diyTimer=null;
                }
                if(cmsSupportVersion!=op.version){
                    alertMsg.error(JUI.msg("errorSupportVersion"));
                } else if (op.templatePath) {
                    $("input[name=url]",navTab.getCurrentPanel()).val(op.url);
                    $("input[name=templatePath]",navTab.getCurrentPanel()).val(op.templatePath);
                    $("input[name=itemType]",navTab.getCurrentPanel()).val(op.itemType);
                    $("input[name=itemId]",navTab.getCurrentPanel()).val(op.itemId);
                    $("form",navTab.getCurrentPanel()).submit();
                    $("#audioLoadPlay")[0].play();
                }
            } else if("enter" === op.diyevent) {
                if(diyShowMenu(op.itemType, op.itemId, op.noborder)) {
                    moveMenu(op.x, op.y, op.width, op.height);
                }
            } else if("leave" === op.diyevent) {
                diyMenuTimer = setTimeout("diyHideMenu()",100);
            }
        }
        if("hoverItem" === op.diyevent) {
            if(diyShowButton($(".diy-menu",navTab.getCurrentPanel()).data("itemType"), op.itemId)) {
                moveButton(op.x, op.y, op.width, op.height);
            }
        } else if("leaveItem" === op.diyevent) {
            diyButtonTimer = setTimeout("diyHideButton()",100);
        }
        if("scroll" === op.diyevent) {
            moveMenu(op.x, op.y, op.width, op.height);
        }
    }
});
function diyHideMenu(){
    if(!$(".diy-menu",navTab.getCurrentPanel()).is(":hover")&&!$(".diy-button",navTab.getCurrentPanel()).is(":hover")){
        $(".diy-border",navTab.getCurrentPanel()).css("visibility", "hidden");
        diyMenuTimer=null;
    }
    diyHideButton();
}
function diyHideButton(){
    if(!$(".diy-button",navTab.getCurrentPanel()).is(":hover")){
        $(".diy-button",navTab.getCurrentPanel()).hide();
        diyButtonTimer=null;
    }
}
function moveMenu(x,y,width,height){
    var boxWidth = $("iframe", navTab.getCurrentPanel()).width();
    var boxHeight = $("iframe", navTab.getCurrentPanel()).height();
    var menuHeight = $(".diy-menu",navTab.getCurrentPanel()).height();
    if (menuHeight > y && boxHeight - menuHeight < y + height) {
        boxHeight -= menuHeight;
    }
    var bottom = boxHeight < (y + height) ? boxHeight : (y + height);
    var right = boxWidth < (x + width) ? boxWidth : (x + width);
    height = 0 > y ? (boxHeight  < y + height) ? boxHeight  : (y + height) : height;
    width = 0 > x ? (width + x) : width;

    $(".diy-border-top",navTab.getCurrentPanel()).css({
        top: 0 > y ? 0 : y, left: 0 > x ? 0 : x, width: width
    });
    $(".diy-border-bottom", navTab.getCurrentPanel()).css({
        top: bottom - 1 , left: x, width: width
    });
    $(".diy-border-left", navTab.getCurrentPanel()).css({
        top: 0 > y ? 0 : y, left: 0 > x ? 0 : x, height: height
    });
    $(".diy-border-right", navTab.getCurrentPanel()).css({
        top: 0 > y ? 0 : y, left: right - 1, height: height
    });
    if(menuHeight > y) {
        $(".diy-menu",navTab.getCurrentPanel()).appendTo($(".diy-border-bottom",navTab.getCurrentPanel()));
    } else {
        $(".diy-menu",navTab.getCurrentPanel()).appendTo($(".diy-border-top",navTab.getCurrentPanel()));
    }
}
function moveButton(x,y,width,height){
    var boxHeight = $("iframe", navTab.getCurrentPanel()).height();
    var buttonHeight = $(".diy-button",navTab.getCurrentPanel()).height();

    if (buttonHeight > y && boxHeight - buttonHeight < y + height) {
        boxHeight -= buttonHeight;
    }
    var bottom = boxHeight < (y + height) ? boxHeight : (y + height);

    if(buttonHeight > y + height) {
        $(".diy-button",navTab.getCurrentPanel()).css({top: buttonHeight > y ? 0 : y - buttonHeight,left: 0 > x ? width /2 : (width / 2 + x) }).show();
    } else {
        $(".diy-button",navTab.getCurrentPanel()).css({top: bottom , left: 0 > x ? width /2 : (width / 2 + x)}).show();
    }
}
function diyShowButton(itemType,itemId){
    if(itemType) {
        var buttons=$("#buttonBox a[data-diy="+escapeJquery(itemType)+"][data-diy-item]");
        if(buttons.length) {
          if(diyButtonTimer){
              clearTimeout(diyButtonTimer);
              diyButtonTimer=null;
          }
          
          buttons.clone().appendTo($(".diy-button",navTab.getCurrentPanel()).empty()).removeClass("hide").addClass("button").each(function(i,button){
              var value={},$button=$(button);
              value[$button.data("diy-item")]=itemId;
              $button.attr("href",$button.attr("href").replaceTm(value));
          });
          $(".diy-button",navTab.getCurrentPanel()).css({marginLeft:-$(".diy-button",navTab.getCurrentPanel()).width()/2});
          initLink($(".diy-button",navTab.getCurrentPanel()));
          return true;
        }
        return false;
    }
    return false;
}
function diyShowMenu(itemType,itemId,noborder){
    $(".diy-menu",navTab.getCurrentPanel()).data("itemType",itemType);
    var buttons;
    if(itemId) {
        buttons=$("#buttonBox a[data-diy="+escapeJquery(itemType)+"][data-diy-id="+escapeJquery(itemId)+"],#buttonBox a[data-diy="+escapeJquery(itemType)+"]:not([data-diy-id],[data-diy-item])");
    } else {
        buttons=$("#buttonBox a[data-diy="+escapeJquery(itemType)+"]:not([data-diy-id],[data-diy-item])");
    }
    if(buttons.length) {
      if(diyMenuTimer){
          clearTimeout(diyMenuTimer);
          diyMenuTimer=null;
      }
      if(noborder){
          $(".diy-border",navTab.getCurrentPanel()).addClass("noborder");
      } else {
          $(".diy-border.noborder",navTab.getCurrentPanel()).removeClass("noborder");
      }
      $(".diy-menu",navTab.getCurrentPanel()).empty().append(buttons.clone().removeClass("hide").addClass("button"));
      $(".diy-menu",navTab.getCurrentPanel()).css({marginLeft:-$(".diy-menu",navTab.getCurrentPanel()).width()/2});
      initLink($(".diy-menu",navTab.getCurrentPanel()));
      $(".diy-border",navTab.getCurrentPanel()).css("visibility", "visible");
      $("#audioPopPlay")[0].play();
      return true;
    }
    return false;
}
JUI.regPlugins.push(function($p){
    $(".diy-menu",$p).each(function (){
        $(this).hover(function(){
            if(diyMenuTimer){
                clearTimeout(diyMenuTimer);
                diyMenuTimer=null;
            }
        },function(){
            $(".diy-border",navTab.getCurrentPanel()).css("visibility", "hidden");
            diyMenuTimer=null;
        });
    });
});
function diyIframeRefresh(){
    $("iframe",navTab.getCurrentPanel()).attr("src",$("input[name=url]",navTab.getCurrentPanel()).val());
    $("#audioPopPlay")[0].volume = 0.1;
    $("#audioLoadPlay")[0].volume = 0.5;
    if(diyTimer){
        clearTimeout(diyTimer);
    }
    diyTimer = setTimeout("alertMsg.error(JUI.msg(\"errorSupport\"))",8000);
}