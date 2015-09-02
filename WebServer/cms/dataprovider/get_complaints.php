<?php
require_once __DIR__ . '/db_config.php';
$db = new PDO("mysql:dbname=test_cms;host=localhost", DB_USER, DB_PASSWORD);

$response = array();
$response['data'] = array();
if(isset($_SESSION['user']) && isset($_SESSION['cid']))
{
	$statement = $db->prepare("SELECT * FROM `complaints` WHERE complaint_id =:id");
	$statement->bindValue(":id",$_GET['id'],PDO::PARAM_STR);
	$statement->execute();
	$rows = $statement->fetchAll(PDO::FETCH_ASSOC);
	$response['data'] = $rows;
	$response['message'] = "success";
}
else if(isset($_SESSION['user']))
{
	$statement = $db->prepare("SELECT * FROM `complaints` WHERE user_id =:id");
	$statement->bindValue(":id",$_SESSION['user'],PDO::PARAM_STR);
	$statement->execute();
	$rows = $statement->fetchAll(PDO::FETCH_ASSOC);
	$response['data'] = $rows;
	$response['message'] = "success";
}
else
{
	$response['message'] = "not logged in.";
}
echo json_encode($response);
	

?>