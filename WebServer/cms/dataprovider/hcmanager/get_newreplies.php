<?php
$response = array();
if(isset($_POST['sid']))
{
	session_id($_POST['sid']);
	session_start();
	if(isset($_SESSION['user']))
	{
		if(isset($_POST['rversion']))
		{
			require_once '../db_config.php';
			$db = new PDO("mysql:dbname=test_cms;host=localhost", DB_USER, DB_PASSWORD);
			
			$statement = $db->prepare("SELECT id_changed FROM reply_version WHERE :rversion < version");
			$statement->bindValue(":rversion",$_POST['rversion'],PDO::PARAM_INT);
			$statement->execute();
			$changed = $statement->fetchAll(PDO::FETCH_ASSOC);
			if(count($changed) > 0)
			{
				$query = "";
				$i = 0;
				foreach($changed as $cchanged)
				{
					if(strlen($query) == 0)
						$query = "SELECT * FROM replies WHERE reply_id=:cchanged0";
					else
						$query = $query." or reply_id=:cchanged".strval($i);
					$i++;
				}
				$statement = $db->prepare($query);
				$i = 0;
				foreach($changed as $cchanged)
				{
					
					$statement->bindValue(":cchanged".$i,$cchanged['id_changed'],PDO::PARAM_INT);
					$i = $i+1;
				}
				$statement->execute();
				$rows = $statement->fetchAll(PDO::FETCH_ASSOC);
				if(count($rows) > 0)
				{
					$response['data']=array();
					$response['success'] = 1;	
					$response['flag']=0;
					$statement = $db->prepare('SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = "test_cms" AND   TABLE_NAME   = "reply_version"');
					$statement->execute();
					$res = $statement->fetchAll(PDO::FETCH_ASSOC);
					$response['rversion'] = $res[0]['AUTO_INCREMENT']-1;
					array_push($response['data'],$rows);	
				}
				else
				{
					$response['success'] = 1;
					$response['flag']=3;
					$response['data']= "DB error";
				}
			}
			else
			{
				$response['success'] = 1;
				$response['flag']=1;
				$response['data']= "no data";
			}		
		}else{
			$response['success'] = 0;
			$response['flag']=0;
			$response['data'] = "rversion";
		}
	}else{
		$response['success'] = 0;
		$response['flag']=2;
		$response['data'] = "not logged in.";
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