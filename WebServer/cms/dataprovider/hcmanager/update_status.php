<?php
$response = array();
if(isset($_POST['sid']))
{
	session_id($_POST['sid']);
	session_start();
	if(isset($_SESSION['user']))
	{
		if(isset($_POST['cid']) &&  isset($_POST['status']))
		{
			require_once '../db_config.php';
			$db = new PDO("mysql:dbname=test_cms;host=localhost", DB_USER, DB_PASSWORD);
			$date = new DateTime("now", new DateTimeZone('America/Jamaica') );
			$dt = $date->format('Y-m-d H:i:s');	
			
			$stmt = $db->prepare('UPDATE complaints set stat=:stat where complaint_id=:cid;');
			$stmt->bindValue(":cid",$_POST['cid'],PDO::PARAM_INT);
			$stmt->bindValue(":stat",$_POST['status'],PDO::PARAM_STR);
			$stmt->execute();
			
				$response['success'] = 1;	
				$response['flag']=0;	
				$response['data']= "1";
		}
		else{
			$response['success'] = 0;
				$response['flag']=0;
				$response['data']= "cid or status";
		}
	}else{
		$response['success'] = 0;
		$response['flag']=2;
		$response['data'] = "not logged in";
			
	}
}
else{
	$response['success'] = 0;
	$response['flag']=0;
	$response['data'] = "sid";
}
	echo json_encode($response);
	die();
?>