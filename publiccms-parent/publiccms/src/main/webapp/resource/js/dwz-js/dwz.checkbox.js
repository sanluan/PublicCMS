/**
 * @author ZhangHuihua@msn.com
 */
( function($) {
    $.fn.extend({
        checkboxCtrl: function(parent) {
            return this.each(function() {
                var $trigger = $(this);
                $trigger.click(function() {
                    var group = $trigger.attr("group");
                    if ($trigger.is(":checkbox") ) {
                        var type = $trigger.is(":checked") ? "all": "none";
                        if (group ) {
                            $.checkbox.select(group, type, parent);
                        }
                    } else {
                        if (group ) {
                            $.checkbox.select(group, $trigger.attr("selectType") || "all", parent);
                        }
                    }
                });
            });
        }
    });
    $.checkbox = {
        selectAll: function(_name, _parent) {
            this.select(_name, "all", _parent);
        },
        unSelectAll: function(_name, _parent) {
            this.select(_name, "none", _parent);
        },
        selectInvert: function(_name, _parent) {
            this.select(_name, "invert", _parent);
        },
        select: function(_name, _type, _parent) {
            $parent = $(_parent || document);
            $checkboxLi = $parent.find(":checkbox[name='" + _name + "']");
            switch (_type) {
                case "invert":
                    $checkboxLi.each(function() {
                        $checkbox = $(this);
                        $checkbox.prop('checked', !$checkbox.is(":checked"));
                    });
                    break;
                case "none":
                    $checkboxLi.prop('checked', false);
                    break;
                default :
                    $checkboxLi.prop('checked', true);
                    break;
            }
        }
    };
} )(jQuery);