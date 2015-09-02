<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<?php
session_start();
if(isset($_SESSION['user']))
{
	header("Location: home.php");
	die();
}
else if(isset($_POST['id']) && isset($_POST['dob']))
{
	$id = $_POST['id'];
	$dob = $_POST['dob'];
	$dob = substr_replace($dob, '-', 4, 0);
	$dob = substr_replace($dob, '-', 7, 0);

	try{
	$db = new PDO("mysql:dbname=test_sms;host=localhost", "root", "");
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	}catch(PDOException $e)
	{
		echo "Connection failed: " . $e->getMessage();
	}
	$statement = $db->prepare("SELECT * FROM `test_stdns` WHERE id_number =:id and dob =:dob");
	$statement->bindValue(":id",$id,PDO::PARAM_STR);
	$statement->bindValue(":dob",$dob,PDO::PARAM_STR);
	$statement->execute();
	$rows = $statement->fetchAll(PDO::FETCH_ASSOC);
	if(count($rows) > 0)
	{
		$_SESSION['user'] = $id;
		$_SESSION['dob'] = $dob;
		header("Location: home.php");
		die();

	}
}
?>

<head>
	<script type="text/javascript">
	function validateForm()
	{
		var valid = true;
		
		if(document.loginform.id.value.match(/^[0-9]+$/) == null || document.loginform.id.value == "")
		{
			valid = false;
			document.getElementById("idstar").style.visibility="visible";
			document.getElementById("label1").style.visibility="visible";
			document.getElementById("label1").innerHTML="please enter a valid UWI ID#.";
		}
		else
		{
			document.getElementById("idstar").style.visibility="hidden";
			document.getElementById("label1").style.visibility="hidden";
		}
		
		if(document.loginform.dob.value.match(/^[0-9]+$/) == null || document.loginform.dob.value == "" || document.loginform.dob.value.length != "8")
		{
			valid = false;
			document.getElementById("dobstar").style.visibility="visible";
			document.getElementById("label2").style.visibility="visible";
			document.getElementById("label2").innerHTML="please enter a valid date of birth(yyymmdd).";
		}
		else
		{
			document.getElementById("dobstar").style.visibility="hidden";
			document.getElementById("label2").style.visibility="hidden";
		}
		if(valid)
		{
			
		}
		
		
		return valid;
	}
	
	
	</script>
</head>
<body>
	<div>
		<p>	
		
			<form action="login.php" method = "post" name="loginform" onsubmit="return(validateForm())" >
			UWI Indetification #:<br/>
			<input type="text" name="id"/><label id ="idstar" style="color:red; visibility:hidden;">* </label>
			<br/>
			Date of Birth:<br/>
			<input type="password" name="dob"/><label id ="dobstar" style="color:red; visibility:hidden;">* </label><br/><br/>
			<input type="submit" value = "Login"><br/><br/>
			<label id ="label1" style="color:red; visibility:hidden;">s </label><br/>
			<label id ="label2" style="color:red; visibility:hidden;" > s</label>
			</form>	
		</p>
	</div>
</body>
</html>
