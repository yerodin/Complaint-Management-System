<?php  
error_reporting(E_ERROR);	
session_start();
unset($_SESSION['cid']);
if(isset($_GET['cid']))
{
	header("Location: view_complaint.php?id=".$_GET['cid']);
	die();
}
else if(isset($_SESSION['user']))
{	
	include 'dataprovider\get_complaints.php';
	$data = json_decode(ob_get_clean(),true);
	if($data['message'] == "success")
	{
		$complaints = $data['data'];
		echo '<?xml version="1.0" encoding="utf-8"?>';
		echo '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"';
		echo '"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">';
		echo '<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">';
		echo '<head>';
		echo '	<title>Your Complaint\'s</title>';
		echo '	<script type = "text/javascript">';
		echo '	function view(id)';
		echo '	{';
		echo '		$loc = "view_complaint.php?id=";';
		echo '		$loc = $loc.concat(id);';
		echo '		window.location= $loc;';
		echo '	}';
		echo '	</script>';
		echo '</head>';
		echo '<body>';
		echo '<button onclick="window.location=\'http://chancellorhall.org\';">Back To Home Page</button><button onclick="window.location=\'logout.php\'">Logout</button><br/><br/>';
		echo 'Your Complaint\'s';
		echo '	<table class = "table1">';
		echo '		<thead>';
		echo '			<tr>';
		echo '			<th class = "col1">ID</th>';
		echo '			<th class = "col2">Category</th>';
		echo '			<th class = "col3">Subject Title</t	h>';
		echo '			<th class = "col4">Status</th>';
		echo '			<th class = "col5">Date</th>';
		echo '			<th class = "col6">Time</th>';
		echo '			<th class = "col7"></th>';
		echo '			</tr>';
		echo '		</thead>';
		echo '		';
		echo '  ';		
		
		foreach ($complaints as $complaint)
		{
			
			$cid = $complaint['complaint_id'];
			$date = substr($complaint['tstamp'],0,10);
			$time = substr($complaint['tstamp'],11,8);
			echo'<tr>';	
			echo"<td class = \"col1\">{$cid}</td>";
			echo"<td class = \"col2\">{$complaint['category']}</td>";
			echo"<td class = \"col3\">{$complaint['subject']}</td>";
			echo"<td class = \"col4\">{$complaint['stat']}</td>";
			echo"<td class = \"col5\">{$date}</td>";
			echo"<td class = \"col6\">{$time}</td>";
			echo'<td class = \"col7\"><button onclick="view('.$cid.')">view</button></td>';
			echo'</tr>';
			
		}
		echo '	</table>';
		echo '</br><button onclick="window.location=\'new_complaint.php\';">New Complaint</button>';
		echo '</body>';
		echo '</html>';
	}else
	{
		echo "{$data['message']}";
	}
}else{
	echo'<script type = "text/javascript">window.location="login.php";</script>';
}
?>

