<?php
ob_start();
session_start();
if(!isset($_GET['id']) || !isset($_SESSION['user']))
{
	header("Location: login.php");
	die();
}
else
{	
	$_SESSION['cid'] = $_GET['id'];
	require_once '\dataprovider\get_complaints.php';
	$cdata = json_decode(ob_get_clean(),true);
	require_once '\dataprovider\get_replies.php';
	$rdata = json_decode(ob_get_clean(),true);
	if($cdata['message'] == "success" && count($cdata['data'])>0)
	{
		$complaint = $cdata['data'][0];
		$replies = $rdata['data'];
		if($complaint['user_id'] == $_SESSION['user'])
		{
			echo '<?xml version="1.0" encoding="utf-8"?>';
			echo '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"';
			echo '"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">';
			echo '<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">';
			echo '<head>';
			echo '	<title>Complaint\'s</title>';
			echo '</head>';
			echo '<body>';
			echo '<button onclick="window.location=\'home.php\'">Back</button>';
			if($complaint['stat'] != "CLOSED")
				echo '<button onclick="window.location = \'dataprovider/close.php\'">Close Complaint</button>';
			echo '<br/><br/>';
			echo "Complaint ID: {$complaint['complaint_id']}</br>";
			echo "Status: {$complaint['stat']}</br>";
			echo "Category: {$complaint['category']}</br>";
			echo "Sub-Category: {$complaint['sub_category']}</br>";
			echo "Subject: {$complaint['subject']}</br></br>";
			
			$date = substr($complaint['tstamp'],0,10);
			$time = substr($complaint['tstamp'],11,8);
			
			echo "{$date} {$time} {$complaint['user_id']} said:</br>";
			echo "{$complaint['message']}</br></br>";
			
			if(count($replies) > 0)
				foreach($replies as $reply)
				{
					$date = substr($reply['tstamp'],0,10);
					$time = substr($reply['tstamp'],11,8);
					echo "{$date} {$time} {$reply['user']} said:</br>";
					echo "{$reply['message']}</br></br>";
				}
			if($complaint['stat'] != "CLOSED")
			{
				echo'Leave a reply:</br>';
				echo '<form action="dataprovider/leave_reply.php" method="post" name="replyform">';
				echo '<textarea name="message" rows="5" cols="50"></textarea>';
				echo '<input type="submit" value="Reply"></form>';
			}
			
			echo '</body>';
			echo '</html>';
		}else{
			echo 'You do not have permission to view this page.';
			header( "refresh:5;url=../home.php" );
			die();
		}
		
	}else
	{
		
	}
}

?>