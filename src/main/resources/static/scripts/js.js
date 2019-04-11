/*$(function() {
  $('.menulink').click(function(){
    $("#bg").attr('src',"styles/images/exit-button.png");
  });
});
*/

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
document.getElementById("date").innerHTML = days[today.getDay()] + "<br />" + today.getDate() + "/" + (today.getMonth() + 1) + "/" + today.getFullYear();

function getStatus() {
    $.ajax({
        type: 'POST',
        url: "button",
        success: function (data) {
            entered = data;

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
            + encodeURIComponent(enterTime)
        $.ajax({
            type: 'POST',
            url: "update",
            data: pressed,
            success: function (data) {
                //console.log('success',data);

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
            url: "result",
            data: data,
            success: function (data) {
                document.getElementById("hide").innerHTML += data;

            },
            error: function (exception) {
                alert('Exception' + exception);
            }
        });
        document.getElementById("enterBtn").src = "css/images/enter-button2.png";
        entered = false;

        // location.reload();//reloads the page SA

    }
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

/*function cookie(){
    var id=makeid(25);

    Cookies.set('entered', id);
    'cookie='
    +encodeURIComponent(id);
    $.ajax({
        type: 'POST',
        url:"getAlldata",
        data: id ,
        success:function (data) {
            console.log('success',data);

        }   ,
        error: function (exception) {
            alert('Exception'+exception);
        }
    });
}
function delCookie() {
    Cookies.remove('entered');

    //window.location= "http://localhost:8666/logout";
}
function checkCookie(){
    if(Cookies.get('entered')==""){
        window.location.replace("http://localhost:8666/logout");
    }
}
function redirect(){
     axios.get('http://localhost:8666/logout')
}
function makeid(length) {
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for (var i = 0; i < length; i++)
        text += possible.charAt(Math.floor(Math.random() * possible.length));

    return text;
}

*/
// $(document).ready(function ($) {
//
//     $('.black-button').on({
//         'click': function () {
//             $('#change-image').attr('src', 'styles/images/exit-button.png');
//         }
//     });
//
//
// });
//  $('#sandbox-container input').datepicker({
//      daysOfWeekDisabled: "6",
//      todayHighlight: true
//  });
//  function myFunction() {
//      var x = document.getElementById("myDIV");
//      if (x.style.display === "none") {
//          x.style.display = "block";
//      } else {
//          x.style.display = "none";
//      }
//  }
// /*----------------------------sidebar----------------------------------------------*/

function openNav() {
    document.getElementById("mySidenav").style.width = "250px";

}

function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
}

/*----------------------------------logo-----------------------------------------*/
function main() {
    window.location.replace("main");

}