<?php
$token = $_REQUEST['key'];

$message = $_REQUEST['task'];

$result = sendPush($token,$message);
echo $result;
//$arr = json_decode($result, true);
//if($arr["success"] == 1)
//{
//  echo "Success";
//}
//else
//{
//  echo "Failure";
//}


function sendPush($token, $message) {

    $url = 'https://fcm.googleapis.com/fcm/send';

    $fields = array (
            'to' => $token,
            'data' => array (
                "message" => $message
                )
    );
    $fields = json_encode ( $fields );

    $headers = array (
            'Authorization: key=AAAA8KuzraU:APA91bFW9s6UvGynpjYKiAQiOdIREjK83xKn9sYHA53BXO5feWQY_U5sIZIYesGSVHLJw8m-fEuQU0NH-2EFlnhrz1mTJzLGcG_90bPNMAwFe6dZd8mBUC2tgLy3t8bVdDDTLr1m3P7B',
            'Content-Type: application/json'
    );

    $ch = curl_init ();
    curl_setopt ( $ch, CURLOPT_URL, $url );
    curl_setopt ( $ch, CURLOPT_POST, true );
    curl_setopt ( $ch, CURLOPT_HTTPHEADER, $headers );
    curl_setopt ( $ch, CURLOPT_RETURNTRANSFER, true );
    curl_setopt ( $ch, CURLOPT_POSTFIELDS, $fields );

    $result = curl_exec ( $ch );

    curl_close ( $ch );

    return $result;
}

?>
