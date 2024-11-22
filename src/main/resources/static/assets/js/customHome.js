function setType(type) {
    if (type === 0) {
        $("#filterGroupName").css("visibility","hidden");
        $("#postTypeSpan").text("/ Record Only");
        $("[name=post_type]").val("0");
    } else if (type === 1) {
        $("#filterGroupName").css("visibility","visible");
        $("#postTypeSpan").text("/ To group");
        $("[name=post_type]").val("1");
    } else if (type === 2) {
        $("#filterGroupName").css("visibility","hidden");
        $("#postTypeSpan").text("/ To all");
        $("[name=post_type]").val("2");
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
function clickAddPictureBt(){
    $("[name=files]").click();
}

let index = 0;
let images = [];
$("[name=files]").change(function(event){
    $("#previewDiv").empty();
    images = [];
    index = 0;
    const files = event.target.files;
    $("#pictureController").text("<"+(index+1)+"/"+files.length+">");
    for(let i=0; i< files.length; i++){
        const file = files[i];
        const reader = new FileReader();
        reader.onload = function(e){
            // 이미지 배열에 이미지 데이터 추가
            const img = $("<img>").attr("src", e.target.result);
            images.push(img);

            // 첫 번째 이미지만 표시
            if (images.length === 1) {
                $("#previewDiv").append(img);
                img.show();
            }
        }
        reader.readAsDataURL(file);
    }

});

// 좌우 화살표 클릭 시 이미지 슬라이드
$(".arrowLeft").click(function() {
    if (images.length > 0) {
        images[index].hide(); // 현재 이미지 숨김
        index = (index - 1 + images.length) % images.length; // 이전 인덱스로 이동
        $("#previewDiv").append(images[index]); // 새 이미지 추가
        images[index].show(); // 새 이미지 표시
        $("#pictureController").text("<"+(index+1)+"/"+images.length+">");
    }
});

$(".arrowRight").click(function() {
    if (images.length > 0) {
        images[index].hide(); // 현재 이미지 숨김
        index = (index + 1) % images.length; // 다음 인덱스로 이동
        $("#previewDiv").append(images[index]); // 새 이미지 추가
        images[index].show(); // 새 이미지 표시
        $("#pictureController").text("<"+(index+1)+"/"+images.length+">");
    }
});