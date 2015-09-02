<?php
session_start();
require_once __DIR__ . '/db_config.php';
$db = new PDO("mysql:dbname=test_cms;host=localhost", DB_USER, DB_PASSWORD);
if(!isset($_SESSION['user']))
{
	echo 'Please login.';
	header( "refresh:5;url=../login.php" );
	die();
}
else if(isset($_POST['category']) &&  isset($_POST['sub_category'])&&  isset($_POST['subject'])&&  isset($_POST['message']))
{
	$date = new DateTime("now", new DateTimeZone('America/Jamaica') );
	$dt = $date->format('Y-m-d H:i:s');	

	$stmt = $db->prepare('SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = "test_cms" AND   TABLE_NAME   = "complaints"');
	$stmt->execute();
	$rows = $stmt->fetchAll(PDO::FETCH_ASSOC);
	$cid = $rows[0]['AUTO_INCREMENT'];
	
	$stmt = $db->prepare('INSERT INTO complaints(user_id,category,sub_category,subject,message,tstamp,stat) values(:id,:cat,:scat,:sub,:mess,:ts,"OPEN"); insert into complaint_version(id_changed) values(:cid );');
	$stmt->bindValue(":id",$_SESSION['user'],PDO::PARAM_STR);
	$stmt->bindValue(":cat",$_POST['category'],PDO::PARAM_INT);
	$stmt->bindValue(":scat",$_POST['sub_category'],PDO::PARAM_INT);
	$stmt->bindValue(":sub",$_POST['subject'],PDO::PARAM_STR);
	$stmt->bindValue(":mess",$_POST['message'],PDO::PARAM_STR);
	$stmt->bindValue(":ts",$dt,PDO::PARAM_STR);
	$stmt->bindValue(":cid",$cid,PDO::PARAM_INT);
	$stmt->execute();
	if($stmt->rowCount() > 0)
	{
		echo 'Successfully submitted complaint!. Redirecting you...';
		header( "refresh:3;url=../home.php" );
		die();
	}
	else
	{
		echo "error {$stmt->errorCode()}";
		header( "refresh:3;url=../home.php" );
		die();
	}
}
else
{
	echo 'error, try again. Redirecting you...';
	header( "refresh:3;url=../home.php" );
	die();
}
	

?>