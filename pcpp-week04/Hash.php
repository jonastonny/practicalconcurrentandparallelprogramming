<?php 
require_once('/usr/share/php/smarty3/Smarty.class.php');
$smarty = new Smarty();


if(isset($_POST['input'])){
	$input = $_POST['input'];
	$output = [];
	
	$output['md5'] = hash('md5' , $input);
	$output['md4'] = hash('md4' , $input);
	$output['sha1'] = hash('sha1' , $input);
	$output['sha256'] = hash('sha256' , $input);
	$output['sha384'] = hash('sha384' , $input);
	$output['sha512'] = hash('sha512' , $input);
	$output['snefru'] = hash('snefru' , $input);
	$output['gost'] = hash('gost' , $input);

	$smarty->assign('test', $output);
}


?>