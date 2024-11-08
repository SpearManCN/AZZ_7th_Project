var emailFlag = false;
var addressFlag = false;
var phoneFlag = false;
var passwordFlag = false;

$(function(){
    if($("#error").val()!=null){
        alert("Email (ID) 와 비밀번호를 확인해주세요.");
    }
});

function findAjax(){
    var formData = $('form[name="signUpForm"]').serialize();

    // AJAX 요청
    $.ajax({
        type: "POST",
        url: "/login/findUser",
        data: formData,
        success: function(response) {
            if(response===-1){
                alert("해당하는 Email이 없습니다.")
            }else{
                window.location.href = "/changePw#findUser?no="+response;
            }
        },
        error: function(error) {
            alert("An error occurred.");
            // 에러 처리 로직
        }
    });
}

function changePwAjax(){
    var formData = $('form[name="signUpForm"]').serialize();

    // AJAX 요청
    $.ajax({
        type: "POST",
        url: "/login/changePw",
        data: formData,
        success: function(response) {
            if(response===1){
                alert("비밀번호가 변경되었습니다.");
                window.location.href = "/";
            }else{
                alert("오류가 발생했습니다.");
            }
        },
        error: function(error) {
            alert("An error occurred.");
            // 에러 처리 로직
        }
    });
}

function sendSignInForm(){

    let email = $("#loginEmail").val();
    if( !email.includes("@")   ) {
        alert("@를 포함한 Email을 입력해주세요.");
        return;
    }
    var emailAt = email.indexOf("@");
    var emailLeft = email.substring(0,emailAt);
    var emailRight = email.substring(emailAt+1);
    if(emailLeft.length <1 || emailRight < 1){
        alert("Email 형식을 확인해주세요.");
        return;
    }


    const form = document.getElementById('signInForm');
    form.submit();
}

function formatName(input) {
    // 숫자만 남기기
    let value= input.value;
    // 최대 길이는 8자리까지만
    if (value.length > 10) {
        value = value.slice(0, 10);
    }
    input.value = value;
}


function formatBirthday(input) {
    // 숫자만 남기기
    let value = input.value.replace(/\D/g, '');
    // 최대 길이는 8자리까지만
    if (value.length > 8) {
        value = value.slice(0, 8);
    }
    // YYYY-MM-DD 형식으로 변환
    if (value.length > 4) {
        value = value.slice(0, 4) + '-' + value.slice(4);
    }
    if (value.length > 7) {
        value = value.slice(0, 7) + '-' + value.slice(7);
    }
    // 입력 필드에 값 설정
    input.value = value;
}
function formatCall(input) {
    // 숫자만 남기기
    let value = input.value.replace(/\D/g, '');
    // 최대 길이는 8자리까지만
    if (value.length > 8) {
        value = value.slice(0, 8);
    }
    // YYYY-MM-DD 형식으로 변환
    if (value.length > 4) {
        value = value.slice(0, 4) + '-' + value.slice(4);
    }
    // 입력 필드에 값 설정
    input.value = value;
}

function formatPassword(){
    if($("#pw").val().length>10){
        $("#pw").val($("#pw").val().slice(0,10));
    }
    if($("#pw2").val().length>10){
        $("#pw2").val($("#pw2").val().slice(0,10));
    }
    if($("#pw").val()==$("#pw2").val()){
        $("#passwordCompare").attr("hidden", true);
        passwordFlag=true;
    }else{
        $("#passwordCompare").attr("hidden", false);
        passwordFlag=false;
    }

}

function handleSelectChange() {
    var select = document.getElementById("customSelect");
    var emailInput = document.getElementById("emailInput");

    // 기본 선택값이 아닌 다른 값이 선택되면
    if (select.value !== "") {
        // emailInput의 값 지우기 및 비활성화
        emailInput.value = "";
        emailInput.disabled = true;
        $("#emailRight").val($("#customSelect").val());

    } else {
        // 기본 선택값일 때는 emailInput을 활성화
        emailInput.disabled = false;
        $("#emailRight").val($("#emailInput").val());
    }
}

function signUpAjax(){
    if($("[name=name]").val().length==0){
        alert("이름을 입력해주세요.");
        return;
    }
    if($("[name=birth]").val().length!=10){
        alert("생일을 입력해주세요.");
        return;
    }
    if($("#pw").val().length<4){
        alert("비밀번호는 4~10자 입니다.")
        return;
    }
    if($("[name=addressMain]").val().length==0){
        alert("주소를 입력해주세요.");
        return;
    }
    if(!(passwordFlag&&emailFlag&&addressFlag&&phoneFlag)){
        alert("정보를 다시 확인해주세요.");
        return;
    }


    var formData = $('form[name="signUpForm"]').serialize();

    // AJAX 요청
    $.ajax({
        type: "POST",
        url: "/login/signUp",
        data: formData,
        success: function(response) {
            alert(response);
            window.location.href = "/";
            // 서버 응답 처리 로직
        },
        error: function(error) {
            alert("An error occurred.");
            window.location.href = "/";
            // 에러 처리 로직
        }
    });
}


function sample4_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var roadAddr = data.roadAddress; // 도로명 주소 변수
            var extraRoadAddr = ''; // 참고 항목 변수

            // 법정동명이 있을 경우 추가한다. (법정리는 제외)
            // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
            if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                extraRoadAddr += data.bname;
            }
            // 건물명이 있고, 공동주택일 경우 추가한다.
            if(data.buildingName !== '' && data.apartment === 'Y'){
                extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            }
            // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
            if(extraRoadAddr !== ''){
                extraRoadAddr = ' (' + extraRoadAddr + ')';
            }

            $("[name=addressMain]").val(roadAddr);
            addressFlag=true;

        }
    }).open();
}

// 유효성 검사 및 중복 확인

function confirmPhone(){
    if($("[name=phone]").val().length!=9){
        alert("8자리 숫자를 입력해주세요.");
        $("[name=phone]").val("");
        return;
    }
    var formData = $('form[name="signUpForm"]').serialize();

    // AJAX 요청
    $.ajax({
        type: "POST",
        url: "/login/confirmPhone",
        data: formData,
        success: function(response) {
            if(response==1){
                if(confirm("사용 가능한 번호 입니다. 사용 하시겠습니까?")){
                    $("[name=phone]").prop("readonly",true);
                    phoneFlag=true;
                }else{
                    $("[name=phone]").val("");
                }
            }else{
                alert("사용 불가능한 번호 입니다.");
                $("[name=phone]").val("");
            }
            // 서버 응답 처리 로직
        },
        error: function(error) {
            alert("An error occurred.");
            // 에러 처리 로직
        }
    });
}

function confirmEmail(){
    if($("[name=emailLeft]").val().length==0 || $("[name=emailRight]").val().length==0){
        alert("Email을 입력해주세요.");
        return;
    }
    var formData = $('form[name="signUpForm"]').serialize();

    // AJAX 요청
    $.ajax({
        type: "POST",
        url: "/login/confirmEmail",
        data: formData,
        success: function(response) {
            if(response==1){
                if(confirm("사용 가능한 메일 입니다. 사용 하시겠습니까?")){
                    $("[name=emailLeft]").prop("readonly",true);
                    $("[id=customSelect]").prop("disabled",true);
                    $("[id=emailInput]").prop("readonly",true);
                    emailFlag=true;
                }else{
                    $("[name=emailLeft]").val("");
                }
            }else{
                alert("사용 불가능한 메일 입니다.");
                $("[name=emailLeft]").val("");
                $("[name=emailRight]").val("");
            }
            // 서버 응답 처리 로직
        },
        error: function(error) {
            alert("An error occurred.");
            // 에러 처리 로직
        }
    });
}

