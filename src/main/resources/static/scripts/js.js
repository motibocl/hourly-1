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
     if (document.getElementById("enterBtn").src == "http://localhost:63342/noteBook/.idea/src/styles/images/enter-button2.png")
     {
         var time=new Date();
         changeImage.minutes=time.getMinutes();
         changeImage.hours=time.getHours();
         document.getElementById("enterBtn").src = "styles/images/exit-button.png";

     } else if (document.getElementById("enterBtn").src == "http://localhost:63342/noteBook/.idea/src/styles/images/exit-button.png")
     {
         var time2=new Date();
         var minutes1=time2.getMinutes();
         var hours1=time2.getHours();
         //  changeImage.minutes=(minutes1-changeImage.minutes;
         var min=(hours1*60+minutes1)-(changeImage.hours*60+changeImage.minutes);
         changeImage.showTime+=min;
         document.getElementById("workTime").innerHTML= "כמה זמן עבדתי:"+(changeImage.showTime/60).toString().split(".")[0]+"hr"+":" +(changeImage.showTime%60)+"min" ;
         document.getElementById("enterBtn").src = "styles/images/enter-button2.png";
     }
 }

 /*jQuery(document).ready(function($){

 $('.black-button').on({
      'click': function(){
          $('#change-image').attr('src','styles/images/exit-button.png');
      }
  });


  });*/


