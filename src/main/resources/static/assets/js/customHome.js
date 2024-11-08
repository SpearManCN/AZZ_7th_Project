function setType(type){
    if(type === 0){
        $("#postTypeSpan").text("/ Record Only");
    }else if(type === 1){
        $("#postTypeSpan").text("/ To all");
    }else if(type === 2){
        $("#postTypeSpan").text("/ To group");
    }
}