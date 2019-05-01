
var today = new Date();//today a date object
var entered = false;
var days = [];
days[0] = "יום ראשון,";//in index 0
days[1] = "יום שני,";//in index 1 becouse 0 place isnt empty
days[2] = "יום שלישי,";
days[3] = "יום רביעי,";
days[4] = "יום חמישי,";
days[5] = "יום שישי,";
days[6] = "יום שבת,";
document.getElementById("date").innerHTML = days[today.getDay()] + "<br/>" + today.getDate() + "/" + (today.getMonth() + 1) + "/" + today.getFullYear();
document.getElementById("showTime").innerHTML = today.getDate() + "/" + (today.getMonth() + 1) + "/" + today.getFullYear();

function getStatus() {
    $.ajax({
        type: 'POST',
        url: "buttonStatus",
        success: function (data) {
            entered = data;

        },
        error: function (exception) {
            alert('Exception' + exception);
        }
    });
}
function f() {
   /* var obj = new Object();
    obj.id =  document.getElementById("id").value;
    obj.year  =document.getElementById("month").value;
    obj.month = document.getElementById("year").value;
    var jsonString= JSON.stringify(obj);
    $.ajax({
        url:"reports",
        type:"POST",
        contentType: "application/json; charset=utf-8",
        data: jsonString, //Stringified Json Object
        async: false,    //Cross-domain requests and dataType: "jsonp" requests do not support synchronous operation
        cache: false,    //This will force requested pages not to be cached by the browser
        processData:false, //To avoid making query String instead of JSON
        success: function(resposeJsonObject){
            // Success Message Handler
            var obj = JSON.parse(resposeJsonObject);
           alert(obj.name.toString()) ;
        }
    });*/
    var month=document.getElementById("month").value;
    var id=$('input[name=id]').val();
    var year=$('input[name=year]').val();
    var data = 'id='
        + encodeURIComponent(id)
        + '&month='
        + encodeURIComponent(month)
        + '&year='
        + encodeURIComponent(year);
    $.ajax({
        type: 'POST',
        url: "reports",
        data:data,
        success: function (data) {
            console.log('success', data);

        },
        error: function (exception) {
            alert('Exception' + exception);
        }
    });

}
function changeImage() {

    if (!entered) {
        var time = new Date();
        localStorage.setItem("minutes", time.getMinutes().toString());
        localStorage.setItem("hours", time.getHours().toString());
        var enterTime = parseInt(localStorage.getItem("minutes")) + (parseInt(localStorage.getItem("hours")) * 60);
        var num = 1;
        var pressed = 'button='
            + encodeURIComponent(num)
            + '&enterTime='
            + encodeURIComponent(enterTime);
        $.ajax({
            type: 'POST',
            url: "addWorkTime",
            data: pressed,
            success: function (data) {
                $("#enterBtn").attr("src","css/images/exit-button.png");
            },
            error: function (exception) {
                alert('Exception' + exception);
            }
        });
        entered = true;

    } else {

        var time2 = new Date();
        var minutes1 = time2.getMinutes();
        var hours1 = time2.getHours();
        var exitTime = minutes1 + (hours1 * 60);
        var num = 0;
        var data = 'exitTime='
            + encodeURIComponent(exitTime)
            + '&button='
            + encodeURIComponent(num);
        $.ajax({
            type: 'POST',
            url: "updateWorkTime",
            data: data,
            success: function (data) {
                document.getElementById("hide").innerHTML += data+"<br>";

            },
            error: function (exception) {
                alert('Exception' + exception);
            }
        });
        $('#myModal').modal('show');
        $("#enterBtn").attr("src","css/images/enter-button2.png");
        entered = false;


    }
}
//sanding the clicked date and getting array lists of the information.
function dayDetails(date,empId) {
    var data='date='
    +encodeURIComponent(date)
    +'&id='
    +encodeURIComponent(empId);
    $.ajax({
        type: 'POST',
        url: "workTimeDetails",
        data: data,
        success: function (data) {
               //clearing old data .
                $("#daylist").empty();
                $("#hourDetail").empty();
                $("#dateDetail").empty();
                $("#hoursWorkedDetails").empty();
            //getting lists.
            var dayList=data[0];
            for(var i=0;i<dayList.length;i++) {
                document.getElementById("daylist").innerHTML += dayList[i] + "<br>";
            }
            var hourDetail=data[2];
            for(var i=0;i<hourDetail.length;i++) {
                document.getElementById("hourDetail").innerHTML += hourDetail[i] + "<br>";
            }
            var dateDetail=data[3];
            for(var i=0;i<dateDetail.length;i++) {
                document.getElementById("dateDetail").innerHTML += dateDetail[i] + "<br>";
            }
            var hoursWorkedDetails=data[1];
            for(var i=0;i<hoursWorkedDetails.length;i++) {
                document.getElementById("hoursWorkedDetails").innerHTML += hoursWorkedDetails[i] + "<br>";
            }


        },
        error: function (exception) {
            alert('Exception' + exception);
        }
    });
}
function clearCache() {
    history.go(1);
}

function repComment() {
    var commentary = document.getElementsByName('commentary')[0].value;
    var data = 'commentary='
        + encodeURIComponent(commentary);
    $.ajax({
        type: 'POST',
        url: "sendComment",
        data: data,
        success: function (data) {
            console.log('success', data);

        },
        error: function (exception) {
            alert('Exception' + exception);
        }
    });

}
function open() {
    $('#exampleModalLong').modal('show');

}


// /*----------------------------sidebar----------------------------------------------*/

function openNav() {
    document.getElementById("mySidenav").style.width = "250px";

}

function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
}

/*----------------------------------logo-----------------------------------------*/
function returnToMainPage() {
    window.location.replace("main");

}
/*------------------------------------------------------------------*/
function convert(string) {
    var minutes2;
    var hours2=parseInt(string.slice(0,2));
    if(hours2<10){
        minutes2=parseInt(string.slice(2,6));
    }
    else{
        minutes2=parseInt(string.slice(3,6));
    }
    hours2=hours2*60;
    var total1=hours2+minutes2;
    return total1;
}
function confirmAndAdd(emplid,enterTime,exitTime,date,reason,day) {
    var enter = convert(enterTime);
    var exit = convert(exitTime);
    var data = 'emplid='
        + encodeURIComponent(emplid)
        + '&enterTime='
        + encodeURIComponent(enter)
        + '&exitTime='
        + encodeURIComponent(exit)
        + '&date='
        + encodeURIComponent(date)
        + '&day='
        + encodeURIComponent(day)
        + '&reason='
        + encodeURIComponent(reason);
    $.ajax({
        type: 'POST',
        url: "confirmAndAdd",
        data: data,
        success: function (data) {
            console.log('success', data);

        },
        error: function (exception) {
            alert('Exception' + exception);
        }
    });
}