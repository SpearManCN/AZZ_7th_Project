function setType(type) {
    if (type === 0) {
        $("#filterGroupName").css("visibility","hidden");
        $("#postTypeSpan").text("/ Record Only");
    } else if (type === 1) {
        $("#filterGroupName").css("visibility","visible");
        $("#postTypeSpan").text("/ To group");
    } else if (type === 2) {
        $("#filterGroupName").css("visibility","hidden");
        $("#postTypeSpan").text("/ To all");
    }
}
//1234567890 1234567890 1234567890 12345678
function setGroupName(name){
    $("#groupNameHeader").text(name);
}

function hashTagForm(input){
    let value= input.value;
    if(value.substring(0,1)!=='#'){
        input.value='#'+value;
    }
    if(value.length>16){
        input.value=value.substring(0,16);
    }
}

function contentForm(input){
    let value = input.value;
    if(value.length>59){
        input.value=value.substring(0,60);
    }
}

function confirmUpload(event){
    if($("[name=files]")[0].files.length === 0 ){
        alert("최소 하나의 파일을 선택해 주세요.");
        return;
    }
    if(confirm("이대로 등록하시겠습니까?")){
        $("[name=uploadForm]").submit();
    }else{
        event.preventDefault();
    }
}

function clickUploadThumbNail(){
    $("[name=files]").click();
}