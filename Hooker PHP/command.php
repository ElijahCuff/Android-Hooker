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
            'Authorization: key=YOUR FIREBASE SERVER KEY',
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
