<?php
// header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

$agent = $_SERVER['HTTP_USER_AGENT'];
$devicesLog = "devices.log";
$locationLog = "location.log";
$messagesLog = "messages.log";

if (hasParam("key") && hasParam("name"))
{
  $key = $_REQUEST['key'];
  $name = $_REQUEST['name'];
  $device = $name."||".$key;
  $result = logFile($devicesLog, $device);
  if($result == true)
    {
     echo "added";

    }
  else
    {
     echo "device already exists";

    }

}

if(hasParam("key") && hasParam("location"))
{
  $key = $_REQUEST['key'];
  $locate = $_REQUEST['location'];
  $loc = $key."||".$locate;

  file_put_contents($locationLog, "");

  $result = logFile($locationLog, $loc);
 if($result == true)
{
    echo "location added";
  }
  else
   {
     echo "location already exists";
   }

}


if(hasParam("key") && hasParam("messages"))
{
  $key = $_REQUEST['key'];
  $messages = $_REQUEST['messages'];
  $data = $key."||".$messages;

  file_put_contents($messagesLog, "");

  $result = logFile($messagesLog, $data);
  if($result == true)
  {
    echo "messages added";
  }


}

exit();

function logFile($fileName, $data, $noDuplicates = true) 
{

$valid = true;
     $log_file = dirname(__FILE__) . '/' . $fileName;
      if (!file_exists($log_file)) 
         {
            $fp = fopen($log_file, "w");
            fclose($fp);
          }

  $log = fopen($log_file, "r"); 
  // check exists

  if ($noDuplicates)
  {
     while (($buffer = fgets($log)) !== false) 
       {
          if (strpos($buffer, $data) !== false) 
           {
              $valid = false;
              break; 
           }      
       }
  }
fclose($log);

 // continue
  if ($valid)
     { 
       file_put_contents($log_file, $data."\n", FILE_APPEND);
       return true;
     }
  else
  {
    return false;
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