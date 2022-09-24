/**
 * @requires jquery.validate.js
 * @author ZhangHuihua@msn.com
 */
( function($) {
    if ($.validator ) {
        $.validator.addMethod("alphanumeric", function(value, element) {
            return this.optional(element) || /^\w+$/i.test(value);
        }, "Letters, numbers or underscores only please");
        $.validator.addMethod("lettersonly", function(value, element) {
            return this.optional(element) || /^[a-z]+$/i.test(value);
        }, "Letters only please");
        $.validator.addMethod("letterstart", function(value, element) {
            return this.optional(element) || /^[a-z]+\w+$/i.test(value);
        }, "Letters, numbers or underscores only  please,The first character must be letter");
        $.validator.addMethod("phone", function(value, element) {
            return this.optional(element) || /^[0-9 \(\)]{7,30}$/.test(value);
        }, "Please specify a valid phone number");
        $.validator.addMethod("postcode", function(value, element) {
            return this.optional(element) || /^[0-9 A-Za-z]{5,20}$/.test(value);
        }, "Please specify a valid postcode");
        $.validator.addMethod("domain", function(value, element) {
            return this.optional(element) || /^((((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|(\[?((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\]?)|(localhost)|(loopback)|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)*)?$/i.test(value);
        }, "Please specify a valid domain");
        $.validator.addMethod("date", function(value, element) {
            value = value.replace(/\s+/g, "");
            if (String.prototype.parseDate ) {
                var $input = $(element);
                var pattern = $input.attr('dateFmt') || 'yyyy-MM-dd';
                return !$input.val() || $input.val().parseDate(pattern);
            } else {
                return this.optional(element) || value.match(/^\d{4}[\/-]\d{1,2}[\/-]\d{1,2}$/);
            }
        }, "Please enter a valid date.");

        /*
         * 自定义js函数验证 <input type="text" name="xxx" customvalid="xxxFn(element)"
         * title="xxx" />
         */
        $.validator.addMethod("customvalid", function(value, element, params) {
            try {
                return eval('(' + params + ')');
            } catch (e) {
                return false;
            }
        }, "Please fix this field.");
        $.validator.addClassRules({
            date: {
                date: true
            }, alphanumeric: {
                alphanumeric: true
            }, lettersonly: {
                lettersonly: true
            }, phone: {
                phone: true
            }, postcode: {
                postcode: true
            }
        });
        $.validator.setDefaults({
            errorElement: "span"
        });
        $.validator.autoCreateRanges = true;
    }

} )(jQuery);