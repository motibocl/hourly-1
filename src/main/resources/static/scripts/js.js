 /*$(function() {
   $('.menulink').click(function(){
     $("#bg").attr('src',"styles/images/exit-button.png");
   });
 });
*/

 var today = new Date ( );//today a date object

 var days = new Array ( );
 days[0] = "יום ראשון,";//in index 0
 days[1] = "יום שני,";//in index 1 becouse 0 place isnt empty
 days[2] = "יום שלישי,";
 days[3] = "יום רביעי,";
 days[4] = "יום חמישי,";
 days[5] = "יום שישי,";
 days[6] = "יום שבת,";
 document.getElementById("date").innerHTML =days[today.getDay()]+"<br />"+today.getDate()+"/"+(today.getMonth()+1)+"/"+today.getFullYear();


 function changeImage() {
     if( typeof changeImage.minutes == 'undefined'  ) {
         changeImage.minutes = 0;
     }
     if( typeof changeImage.hours == 'undefined'  ) {
         changeImage.hours = 0;
     }
     if( typeof changeImage.showTime == 'undefined'  ) {
         changeImage.showTime = 0;
     }
     if (document.getElementById("enterBtn").src == "http://localhost:8666/css/images/enter-button2.png")
     {
         var time=new Date();
         changeImage.minutes=time.getMinutes();
         changeImage.hours=time.getHours();
         document.getElementById("enterBtn").src = "css/images/exit-button.png";

     } else if (document.getElementById("enterBtn").src == "http://localhost:8666/css/images/exit-button.png")
     {
         var time2=new Date();
         var minutes1=time2.getMinutes();
         var hours1=time2.getHours();
         //  changeImage.minutes=(minutes1-changeImage.minutes;
         var min=(hours1*60+minutes1)-(changeImage.hours*60+changeImage.minutes);
         changeImage.showTime+=min;


         document.getElementById("enterBtn").src = "css/images/enter-button2.png";
         var enterTime=changeImage.minutes+(changeImage.hours*60);
         var exitTime=minutes1+(hours1*60);
         var data='enterTime='
         + encodeURIComponent(enterTime)
         +'&exitTime='
         +encodeURIComponent(exitTime);
         $.ajax({
          type: 'POST',
          url:"result",
          data:data ,
          success:function (data) {
              console.log('success',data);

          }   ,
             error: function (exception) {
                 alert('Exception'+exception);
             }
         });
     }
 }
 /*function cookie(){
     var id=makeid(25);

     Cookies.set('flag', id);
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
     Cookies.remove('flag');

     //window.location= "http://localhost:8666/logout";
 }
 function checkCookie(){
     if(Cookies.get('flag')==""){
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
$(document).ready(function ($) {

    $('.black-button').on({
        'click': function () {
            $('#change-image').attr('src', 'styles/images/exit-button.png');
        }
    });


});
 $('#sandbox-container input').datepicker({
     daysOfWeekDisabled: "6",
     todayHighlight: true
 });
/*----------------------------AJAX----------------------------------------------*/

