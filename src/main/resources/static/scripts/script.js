var indexRow=0;
var idNumber=[];
//var years=[];
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
var searchedFlag=false;
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
function searchEmp(event) {
    if(event.keyCode==13){
        searchedFlag=true;
       var id=document.getElementById("empID").value;
       var data='id='
           + encodeURIComponent(id);
        $.ajax({
            type: 'POST',
            url: "empInf",
            data:data,
            success: function (data) {
                console.log('success', data);
                var obj = JSON.parse(data);
                var table = document.getElementById("addEmpTable");
                if(obj.id!=null&&obj.name!=null&&obj.phone!=null&&obj.password!=null) {
                    for (var i = table.rows.length - 1; i > 1; i--) {
                        table.deleteRow(i);
                    }
                    document.getElementById("addEmpTable").innerHTML += "<tr  >\n" +
                        "    <td ><h4 id=\"idEmp\">" + obj.id + "</h4></td>\n" +
                        "    <td ><h4 id=\"nameEmp\">" + obj.name + "</h4></td>\n" +
                        "    <td ><h4 id=\"phoneEmp\">" + obj.phone + "</h4></td>\n" +
                        "    <td ><h4 id=\"passwordEmp\">" + obj.password + "</h4></td>\n" +
                        "    <td ><button type=\"button\" onclick=\"getRow(this)\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#removeModal\"\ name=\"2\">  <i class=\"fas fa-user-times\"></i></button></td>\n" +
                        "</tr>";
                    document.getElementById("addEmpTable").innerHTML += "<tr  >\n" +
                        "    <td colspan='5'><button  type=\"button\" class=\"btn btn-primary btn-sm\" onclick='searchedFlag=true;location.reload();'> לרשימה המלאה <i class=\"fas fa-undo-alt\"></i></button></td>\n" +
                        "</tr>";
                }
                else {
                    searchedFlag=false;
                    location.reload();
                }
            },
            error: function (exception) {
                alert('Exception' + exception);
            }
        });
    }
}
function dropDown() {
    $.ajax({
        type: 'POST',
        url: "idNumbers",
        success: function (data) {
            console.log('success', data);
            var obj = JSON.parse(data);
            var ids=obj.idArray;
            var dataList = document.getElementById("numberList");//for autocomplete
            for (var i in ids) {
                idNumber.push(ids[i]) ;

            }
            for(var i in idNumber){
                var option = document.createElement('option');
                option.value = idNumber[i];
                dataList.appendChild(option);
            }
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
$('.timepicker').wickedpicker();

function changeImage() {
    var  test=null;
    $.ajax({
        type: 'GET',
        url: "buttonStatus",
        success: function (data) {
            test = data;
            if(test==entered) {

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
                            $("#enterBtn").attr("src", "css/images/exiteBtn.png");
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
                            document.getElementById("hide").innerHTML += data + "<br>";

                        },
                        error: function (exception) {
                            alert('Exception' + exception);
                        }
                    });
                    $('#myModal').modal('show');
                    $("#enterBtn").attr("src", "css/images/enterButtonTest.png");
                    entered = false;
                }

            }else {
                alert("you cant do this function becouse you already entered in onother pc")
            }
        },
        error: function (exception) {
            alert('Exception' + exception);
        }
    });

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

/*
            document.getElementById("addEmpTable").innerHTML += "<tr  style=\"text-align: center\"onclick=\"getRow(this)\">\n" +
                "    <td ><h4 id=\"idEmp\">"+obj.id+"</h4></td>\n" +
                "    <td ><h4 id=\"nameEmp\">"+obj.name+"</h4></td>\n" +
                "    <td ><h4 id=\"phoneEmp\">"+obj.phone+"</h4></td>\n" +
                "    <td ><h4 id=\"passwordEmp\">"+obj.password+"</h4></td>\n" +
                "    <td ><button type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#removeModal\"\ name=\"$i\"><i class=\"fas fa-user-times\"></i></button></td>\n" +
                "</tr>";*/
            var table = document.getElementById( 'addEmpTable' );
            var index=table.rows.length-1;
            var row1 = table.insertRow(table.rows.length-1);
            //row1.onclick="+getRow(this)+";
            var cell1 = row1.insertCell(0);
            var  cell2 = row1.insertCell(1);
            var cell3 = row1.insertCell(2);
            var cell4 = row1.insertCell(3);
            var cell5 = row1.insertCell(4);

            cell1.innerHTML='<h4 id="idEmp">'+obj.id+'</h4></td>';
            cell2.innerHTML="<h4 id=\"nameEmp\">"+obj.name+"</h4>";
            cell3.innerHTML="<h4 id=\"phoneEmp\">"+obj.phone+"</h4>";
            cell4.innerHTML="<h4 id=\"passwordEmp\">"+obj.password+"</h4>";
            cell5.style.textAlign="center";
            cell5.innerHTML="<button type=\"button\" onclick=\"getRow(this)\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#removeModal\"\\ name=\""+index+"\"><i class=\"fas fa-user-times\"></i></button>";
            $('#addModal').modal('hide');

            launch_toastAdd();
           // location.reload();
            // document.getElementById("nameEmp").innerHTML = obj.name;
         //   document.getElementById("phoneEmp").innerHTML = obj.phone;
           // document.getElementById("passwordEmp").innerHTML = obj.password;

        },
        error: function (exception) {
            alert('Exception' + exception);
        }
    });


}
function launch_toastAdd() {
    var x = document.getElementById("toast");
    x.className = "show";
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 5000);

}
function launch_toastRemove() {
    var x = document.getElementById("toastRemove");
    x.className = "show";
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 5000);

}
function register(){
    window.location.replace("registration");
}

function getRow(x){
    indexRow=$(x).closest('tr').index()+2;////////////////////////giving the -2 row index
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
            document.getElementById("addEmpTable").deleteRow(indexRow)
            launch_toastRemove();
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
function requestEmp(reasonEmp) {
    $("#reason").empty();
    document.getElementById("reason").innerHTML += reasonEmp;

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
function returnToAdminMainPage() {
    window.location.replace("adminMain");

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

function confirmAndAdd(emplid,enterTime,exitTime,date,reason,day,i,row,name) {
    var enter=convert(document.getElementById("input "+i.toString()).value);
    var exit=convert(document.getElementById("input2 "+i.toString()).value);
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
            var reasonEmp=reason;
            var index = row.parentNode.parentNode.rowIndex;
            document.getElementById("myTable").deleteRow(index);
            //

            /* var table = document.getElementById( 'acceptedTable' );
             var row1 = table.insertRow(table.rows.length)
             var cell1 = row1.insertCell(0);
             var  cell2 = row1.insertCell(1);
             var cell3 = row1.insertCell(2);
             var cell4 = row1.insertCell(3);
             var  cell5 = row1.insertCell(4);
             var  cell6 = row1.insertCell(5);
             var  cell7 = row1.insertCell(6);
                 cell1.innerHTML='<p >'+emplid+'</p>';
                 cell2.innerHTML='<p >'+name+'</p>';
                 cell3.innerHTML='<p >'+date+'</p>';
                 cell4.innerHTML='<p >'+day+'</p>';
                 cell5.innerHTML='<p><input type="time" id="input $i" value='+enterTime+' ></p>';
                 cell6.innerHTML='<p><input type="time" id="input $i" value='+exitTime+' ></p>';
                 cell7.innerHTML='<button type="button" class="btn btn-primary  test"  onclick=requestEmp('+reasonEmp+')" data-toggle="modal" data-target="#exampleModalScrollable"> <i class="fa fa-commenting-o" style="font-size:20px"></i></button>';


 */




                /*  var tableRef = document.getElementById('acceptedTable');
                  var newRow   = tableRef.insertRow(tableRef.rows.length);
                  var newCell  = newRow.insertCell(0);
                  var newText  = document.createTextNode('<p >'+emplid+'</p>');
                  newCell.appendChild(newText);
                  newCell  = newRow.insertCell(1);
                  newText  = document.createTextNode('<p >'+name+'</p>');
                  newCell.appendChild(newText);
                  newCell  = newRow.insertCell(2);
                  newText  = document.createTextNode('<p >'+date+'</p>');
                  newCell.appendChild(newText);
                  newCell  = newRow.insertCell(3);
                  newText  = document.createTextNode('<p >'+day+'</p>');
                  newCell.appendChild(newText);
                  newCell  = newRow.insertCell(4);
                  newText  = document.createTextNode('<p><input type="time" id="input $i" value='+enterTime+' ></p>\n');
                  newCell.appendChild(newText);
                  newCell  = newRow.insertCell(5);
                  newText  = document.createTextNode('<p><input type="time" id="input $i" value='+exitTime+' ></p>\n');
                  newCell.appendChild(newText);
                  newCell  = newRow.insertCell(6);
                  newText  = document.createTextNode('<button type="button" class="btn btn-primary  test"  onclick="requestEmp('+reasonEmp+')" data-toggle="modal" data-target="#exampleModalScrollable"> <i class="fa fa-commenting-o" style="font-size:20px"></i></button>');
                  newCell.appendChild(newText);*/
            document.getElementById("acceptedTable").innerHTML +=" <tr class=\"inf\" >\n" +
                 "                                        <td><p >"+emplid+"</p></td>\n" +
                 "                                        <td><p >"+name+"</p></td>\n" +
                 "                                        <td><p >"+date+"</p></td>\n" +
                 "                                        <td><p >"+day+"</p></td>\n" +
                 "                                        <td><p><input type=\"time\" id=\"input $i\" value="+enterTime+" ></p></td>\n" +
                 "                                        <td><p><input type=\"time\" id=\"input2 $i\" value="+exitTime+"></p></td>\n" +
                 "                                        <td><button type=\"button\" class=\"btn btn-primary test\" onclick=\"requestEmp('"+reasonEmp+"')\" data-toggle=\"modal\" data-target=\"#exampleModalScrollable\">\n" +
                 "                                        <i class=\"far fa-comment-dots\" style=\"font-size:20px\"></i>   </button></td>\n" +
                 "                                    </tr>";

            //deleteRow(emplid,enterTime,exitTime,date,reason,i,row);
        },
        error: function (exception) {
            alert('Exception' + exception);
        }
    });
}
function deleteRow(emplid,enterTime,exitTime,date,reason,i,row,name,dayOfTheWeek) {
    var index = row.parentNode.parentNode.rowIndex;
    var enter = convert(document.getElementById("input "+i.toString()).value);
    var exit = convert(document.getElementById("input2 "+i.toString()).value);
    var data = 'emplid='
        + encodeURIComponent(emplid)
        + '&date='
        + encodeURIComponent(date)
        + '&enterTime='
        + encodeURIComponent(enter)
        + '&exitTime='
        + encodeURIComponent(exit)
        + '&reason='
        + encodeURIComponent(reason);
    $.ajax({
        type: 'POST',
        url: "removeReason",
        data: data,
        success: function (data) {
            console.log('success', data);
            var reasonEmp=reason;

            document.getElementById("myTable").deleteRow(index);
           // var lastIndex = document.getElementById("declinedTable").rows.length;
            document.getElementById("declinedTable").innerHTML +=" <tr class=\"inf\" >\n" +
                "                                        <td><p >"+emplid+"</p></td>\n" +
                "                                        <td><p >"+name+"</p></td>\n" +
                "                                        <td><p >"+date+"</p></td>\n" +
                "                                        <td><p >"+dayOfTheWeek+"</p></td>\n" +
                "                                        <td><p><input type=\"time\" id=\"input $i\" value="+enterTime+" ></p></td>\n" +
                "                                        <td><p><input type=\"time\" id=\"input2 $i\" value="+exitTime+"></p></td>\n" +
                "                                        <td><button type=\"button\" class=\"btn btn-primary test\" onclick=\"requestEmp('"+reasonEmp+"')\" data-toggle=\"modal\" data-target=\"#exampleModalScrollable\">\n" +
                "                                        <i class=\"far fa-comment-dots\" style=\"font-size:20px\"></i>   </button></td>\n" +
                "                                    </tr>";
        },
        error: function (exception) {
            alert('Exception' + exception);
        }
    });

}
/*-------------------------------------charts----------------------------------------------------*/
/*
google.charts.load('current', {packages: ['corechart', 'bar']});
google.charts.setOnLoadCallback(drawBasic);

function drawBasic() {



      var data = google.visualization.arrayToDataTable([
         ['Element', 'Density', { role: 'style' }],
         ['January', 8.94, 'blue'],            // RGB value
         ['February', 10.49, 'blue'],            // English color name
         ['March', 19.30, 'blue'],
         ['May', 21.45, 'blue' ], // CSS-style declaration
          ['June', 8.94, 'blue'],            // RGB value
         ['July', 10.49, 'blue'],            // English color name
         ['August', 19.30, 'blue'],
         ['September', 21.45, 'blue' ],
         ['October', 10.49, 'blue'],            // English color name
         ['November', 19.30, 'blue'],
         ['December', 21.45, 'blue' ]
      ]);
      var options = {
        title: 'Motivation Level Throughout the Day',
        hAxis: {
          title: 'Months',
          format: 'h:mm a',
          viewWindow: {
            min: [7, 30, 0],
            max: [17, 30, 0]
          }
        },
        vAxis: {
          title: 'Hours in month'
        }
      };

      var chart = new google.visualization.ColumnChart(
        document.getElementById('chart_div'));

      chart.draw(data, options);
    }*/


function checkDate() {
    var todayYear=today.getFullYear();
    var month=today.getMonth()+1;
    var day=today.getDate();
    if(day<10){
        day='0'+day;
    }
    if(month<10){
        month='0'+month;
    }
    todayYear=todayYear+"-"+month+"-"+day;
    document.getElementById("check").setAttribute("max",todayYear);
}
/*-----------------------------------------checklength-----*/


function checkLength(len,ele){
    var fieldLength = ele.value.length;
    if(fieldLength <= len){
        return true;
    }
    else
    {
        var str = ele.value;
        str = str.substring(0, str.length - 1);
        ele.value = str;
    }
}