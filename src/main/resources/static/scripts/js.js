
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
        url: "buttonStatus",
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
        $('#myModal').modal('show')
        $("#enterBtn").attr("src","css/images/enter-button2.png");
        entered = false;


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

//-------------------------------------------------------------///

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