/**
 */
(function($){
    // jQuery validate
    $.extend($.validator.messages, {
        required: "必須フィールド",
        remote: "このフィールドを修正してください",
        email: "正確なメールアドレスを入力してください",
        url: "有効なURLを入力してください",
        date: "正確な期日を入力してください",
        dateISO: "正確な期日を入力してください (ISO).",
        number: "正確な数字を入力してください",
        digits: "整数のみ入力できます",
        creditcard: "正確なクレジットカード番号を入力してください",
        equalTo: "もう一度入力してください",
        accept: "正確な拡張子の文字列を入力してください",
        maxlength: $.validator.format("{0} 以下の文字列"),
        minlength: $.validator.format("{0} 以上の文字列"),
        rangelength: $.validator.format("{0} 和 {1}の間の文字列"),
        range: $.validator.format("{0} 和 {1}の間の文字列を入力してください"),
        max: $.validator.format("{0}以下の値を入力してください"),
        min: $.validator.format("{0}以上の値を入力してください"),

        alphanumeric: "英字、数字、アンダーラインでなければなりません",
        lettersonly: "英字でなければなりません",
        letterstart: "英字、数字、アンダースコアで構成され、最初の文字は英字で始まらなければなりません",
        phone: "正確な電話番号を入力してください",
        domain: "正確なドメイン名を入力しなければなりません"
    });

    // DWZ regional
    $.setRegional("datepicker", {
        dayNames: ['日', '月', '火', '水', '木', '金', '土'],
        monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
    });
    $.setRegional("alertMsg", {
        title:{error:"エラー", info:"プロンプト", warn:"アラート", correct:"成功", confirm:"確認プロンプト"},
        butMsg:{ok:"確認", yes:"はい", no:"いいえ　", cancel:"取り消し"}
    });


    $.setMessage('statusCode_503','サーバーは現在過負荷状態になるか、ただいまメンテナンス中です!');
    $.setMessage('validateFormError','提出データが不完全、{0}フィールドにエラーがあります。修正してから提出してください！');	
    $.setMessage('sessionTimout','タイムアウトしました、再度ログインしてください！');
    $.setMessage('alertSelectMsg','一つのデータを選択してください!');
    $.setMessage('forwardConfirmMsg','次へ!');
})(jQuery);