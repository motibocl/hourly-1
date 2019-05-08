var indexRow=0;
//var rocounter=0;//for counting rows
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
function rowNum(rowNum) {
    counter=rowNum;
}
function addEmployee() {

    var id=$('input[name=id]').val();
    var name=$('input[name=name]').val();
    var password=$('input[name=password]').val();
    var phone=$('input[name=phone]').val();
    var data='id='
    +encodeURIComponent(id)
    +'&name='
    +encodeURIComponent(name)
    +'&password='
    +encodeURIComponent(password)
    +'&phone='
    +encodeURIComponent(phone);
    $.ajax({
        type: 'POST',
        url: "add-employee",
        data: data,
        success: function (data) {
            console.log('success', data);
            var obj = JSON.parse(data);

            document.getElementById("addEmpTable").innerHTML += "<tr  onclick=\"getRow(this)\">\n" +
                "    <td ><h4 id=\"idEmp\">"+obj.id+"</h4></td>\n" +
                "    <td ><h4 id=\"nameEmp\">"+obj.name+"</h4></td>\n" +
                "    <td ><h4 id=\"phoneEmp\">"+obj.phone+"</h4></td>\n" +
                "    <td ><h4 id=\"passwordEmp\">"+obj.password+"</h4></td>\n" +
                "    <td ><button type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#removeModal\"\ name=\"$i\">x</button></td>\n" +
                "</tr>";
           // document.getElementById("nameEmp").innerHTML = obj.name;
         //   document.getElementById("phoneEmp").innerHTML = obj.phone;
           // document.getElementById("passwordEmp").innerHTML = obj.password;

        },
        error: function (exception) {
            alert('Exception' + exception);
        }
    });


}
function register(){
    window.location.replace("registration");
}

function getRow(x){
    indexRow=x.rowIndex;
}
function removeEmployee() {
    var table=document.getElementById("addEmpTable");
    var Row = table.rows[indexRow];
    var Cells = Row.getElementsByTagName("td");
    var id=Cells[0].innerText;
    var data='id='
        +encodeURIComponent(id);
    $.ajax({
        type: 'POST',
        url: "remove-employee",
        data: data,
        success: function (data) {
            //console.log('success', data);
            document.getElementById("addEmpTable").deleteRow(indexRow);
        },
        error: function (exception) {
            alert('Exception' + exception);
        }
    });

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

    var comment = document.getElementsByName('commentary')[0].value;
    var data = 'comment='
        + encodeURIComponent(comment);
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
    var hours=string.slice(0,2);
    var minutes=string.slice(3,5);
    var check=hours.charAt(0);
    var check2=minutes.charAt(0);
    if (check=="0"&&check2=="0"){
        hours=parseInt(hours.slice(1,2));
        minutes=parseInt(minutes.slice(1,2));
    }
    else if(check=="0"&&check2!="0"){
        hours=parseInt(hours.slice(1,2));
        minutes=parseInt(minutes);
    }
    else if(check!="0"&&check2=="0"){
        hours=parseInt(hours);
        minutes=parseInt(minutes.slice(1,2));
    }
    else {
        hours=parseInt(hours);
        minutes=parseInt(minutes);
    }

    hours=hours*60;
    var total=hours+minutes;
    return total;
}

function confirmAndAdd(i,emplid,enterTime,exitTime,date,reason,day) {
    var enter=convert(document.getElementById("input "+i.toString()).value);
    var exit=convert(document.getElementById("input2 "+i.toString()).value);
    //var index=i;
    //var table=document.getElementById("myTable");
    //var Row=table.rows[index];
    //var Cells=Row.getElementsByTagName("td");
    //var enterTest=Cells[4]
    //var enter=convert(enterTest);
    //var exitTest=Cells[5].innerText;
    //var exit=convert(exitTest);
    /*var obj=document.getElementsByClassName(i);
    var enter = convert(obj.item(0).getAttribute());
    var exit = convert(obj.item(1).value);*/
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
            deleteRow(emplid,enterTime,exitTime,date,reason,i);
        },
        error: function (exception) {
            alert('Exception' + exception);
        }
    });
}

function deleteRow(emplid,enterTime,exitTime,date,reason,i) {
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
        + '&reason='
        + encodeURIComponent(reason);
    $.ajax({
        type: 'POST',
        url: "removeReason",
        data: data,
        success: function (data) {
            console.log('success', data);
            document.getElementById("myTable").deleteRow(i);
        },
        error: function (exception) {
            alert('Exception' + exception);
        }
    });

}
