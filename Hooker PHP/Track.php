<?php
$log_file = dirname(__FILE__) . '/' . "location.log";
$locList = file_get_contents($log_file);
$locations = explode(PHP_EOL,$locList);
$link = "";
$checkKey = urldecode($_REQUEST['key']);

if($locList != null)
{
foreach($locations as $loc)
{
  $bits = explode("||",$loc);
 if(count($bits) > 1)
{
  $key = $bits[0];
  $location = urldecode($bits[1]);
  if($key == $checkKey)
   {
    $link ='https://www.google.com/maps/embed/v1/place?key=AIzaSyA0Dx_boXQiwvdz8sJHoYeZNVTdoWONYkU&q='.$location.'&language=en&zoom=18&maptype=satellite';
   }
}
}
}

if(hasParam("task"))
{
   echo $link;
   file_put_contents($log_file,"");
   exit();
}

function hasParam($param) 
{
   if (array_key_exists($param, $_POST))
    {
       return array_key_exists($param, $_POST);
    } else
    {
      return array_key_exists($param, $_GET);
    }
}
?>


<!DOCTYPE html>
<html >
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="generator" content="Mobirise v4.7.5, mobirise.com">
  <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1">
  <link rel="shortcut icon" href="assets/images/logo.png-128x128.png" type="image/x-icon">
  <meta name="description" content="Web Builder Description">
  <title>Track</title>
  <link rel="stylesheet" href="assets/web/assets/mobirise-icons/mobirise-icons.css">
  <link rel="stylesheet" href="assets/tether/tether.min.css">
  <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="assets/bootstrap/css/bootstrap-grid.min.css">
  <link rel="stylesheet" href="assets/bootstrap/css/bootstrap-reboot.min.css">
  <link rel="stylesheet" href="assets/dropdown/css/style.css">
  <link rel="stylesheet" href="assets/theme/css/style.css">
  <link rel="stylesheet" href="assets/mobirise/css/mbr-additional.css" type="text/css">
  
  
  
</head>
<body class="mbr-section content4 cid-tJdvkoLsC5" >
  <section class="menu cid-tJdoQgnPQm" once="menu" id="menu2-7">

    

    <nav class="navbar navbar-expand beta-menu navbar-dropdown align-items-center navbar-fixed-top navbar-toggleable-sm bg-color transparent">
        <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <div class="hamburger">
                <span></span>
                <span></span>
                <span></span>
                <span></span>
            </div>
        </button>
        <div class="menu-logo">
            <div class="navbar-brand">
                <span class="navbar-logo">
                    <a href="index.php">
                        <img src="assets/images/logo.png-128x128.png" alt="CACS" title="" style="height: 3.8rem;">
                    </a>
                </span>
                <span class="navbar-caption-wrap"><a class="navbar-caption text-white display-4" href="">Hooker<br></a></span>
            </div>
        </div>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav nav-dropdown nav-right" data-app-modern-menu="true"><li class="nav-item">
                    <a class="nav-link link text-white display-4" href="Devices.php">Linked Devices<br></a>
                </li></ul>
            
        </div>
    </nav>
</section>

<section class="mbr-section content4 cid-tJdvkoLsC5" id="content4-9">

    

    <div class="container">
        <div class="media-container-row">
            <div class="title col-12 col-md-8">
                <h2 class="align-center pb-3 mbr-fonts-style display-2">Tracking Device</h2>
                <h3 style="font-size: 13px;" class="mbr-section-subtitle align-center mbr-light mbr-fonts-style display-6"><?php echo $checkKey; ?></h3>
                
            </div>
        </div>
    </div>
</section>

<section class="map1 cid-tJdv9DkoDX" id="map1-8">

     

    <div class="google-map" id="mapbox">



</div>
</section>

<section class="mbr-section content8 cid-tJdvUKX9DD" id="content8-b">
    

    <div class="container">
        <div class="media-container-row title">
            <div class="col-12 col-md-8">
                <div class="mbr-section-btn align-center"><button id="linkBack" class="btn btn-white-outline display-4"  onClick="getLocation()">START TRACKING</button></div>
            </div>
        </div>
    </div>
</section>
  <script src="assets/web/assets/jquery/jquery.min.js"></script>
<script>

 function getLocation(){
var link = document.getElementById("linkBack");
   link.innerHTML = "TRACKING...";
 
$.ajax({
   type: "post",
   url:location.origin + "/command.php",
   data: {
    task: "getLocation",
    key: "<?php echo $checkKey; ?>"
  },
   success: function(response) {
   var back = response;
   setTimeout(function () {
         $.ajax({
                     type: "post",
                     url:location.origin + "/Track.php",
                data: {
                           task: "getUpdate",
                           key: "<?php echo $checkKey; ?>"
                          },
                success: function(response) {
                          var back = response;
                          document.getElementById("mapbox"). innerHTML= "<iframe id='map' frameborder='0' style='border:0' src='"+back+"' allowfullscreen=''></iframe>";
link.innerHTML = "START TRACKING";
                                                                   }   
                     });
                           }, 30000);
   }
});

}


   </script>
  <script src="assets/popper/popper.min.js"></script>
  <script src="assets/tether/tether.min.js"></script>
  <script src="assets/bootstrap/js/bootstrap.min.js"></script>
  <script src="assets/smoothscroll/smooth-scroll.js"></script>
  <script src="assets/dropdown/js/script.min.js"></script>
  <script src="assets/touchswipe/jquery.touch-swipe.min.js"></script>
  <script src="assets/theme/js/script.js"></script>
  
   
  
</body>
</html>