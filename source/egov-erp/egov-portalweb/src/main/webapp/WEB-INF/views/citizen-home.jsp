<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		<meta name="description" content="Neon Admin Panel" />
		<meta name="author" content="" />
		
		<title>eGov Urban Portal</title>
		
		<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap.css' context='/egi'/>">
		<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>">
		<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/font-awesome-4.3.0/css/font-awesome.min.css' context='/egi'/>">
		<link rel="stylesheet" href="<c:url value='/resources/global/css/egov/custom.css' context='/egi'/>">
		<link rel="stylesheet" href="<c:url value='/resources/global/css/egov/header-custom.css' context='/egi'/>">
		<script src="<c:url value='/resources/global/js/jquery/jquery.js' context='/egi'/>"></script>
		
		<!--[if lt IE 9]><script src="resources/js/ie8-responsive-file-warning.js"></script><![endif]-->
		
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
			<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
			<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->
		
		
	</head>
	<body class="page-body">
		
		<div class="page-container">
			
			<header><!-- set fixed position by adding class "navbar-fixed-top" -->
				
				<nav class="navbar navbar-default navbar-custom navbar-fixed-top">
					<div class="container-fluid">
						<div class="navbar-header col-lg-4 col-md-4 col-sm-2 col-xs-3">
							<a class="navbar-brand" href="javascript:void(0);">
								<img src="../images/chennai_logo.jpg" height="60">
								<div>
									<span class="title2 hidden-sm hidden-xs">Chennai Municipal Corporation</span>
								</div>
							</a>
						</div>
						<div class="nav-menu col-lg-4 col-md-6 col-sm-7 col-xs-12">
							<ul class="hr-menu text-center">
								<li class="active"><a class="menu-item " href="javascript:void(0);" data-show-screen="#inbox-template"> <span class="title">Inbox</span><span class="badge custom-badge">03</span></a></li>
								<span class="separator">|</span>
								<li><a class="menu-item" href="javascript:void(0);" data-show-screen="#myaccount">My Account</a></li>
								<span class="separator">|</span>
								<li><a class="menu-item" href="javascript:void(0);" data-show-screen="#newreq">New Request</a></li>
							</ul>
						</div><!--/.nav-collapse -->
						
						<div class="col-lg-4 col-md-2 col-sm-3 col-xs-9 nav-right-menu home">
							<ul class="hr-menu text-right">
								
								<li class="ico-menu">
									<a href="javascript:void(0);">
										<i class="fa fa-question-circle"></i>
									</a>
								</li>
								
								<li class="ico-menu">
									<a class="dropdown-toggle" href="javascript:void(0);" data-toggle="dropdown">
										<i class="fa fa-user"></i>
									</a>
									<ul class="right-arrow dropdown-menu" role="menu">
										<li><a href="#"><i class="fa fa-cog"></i> Change Password</a></li>
										<li><a href="#"><i class="fa fa-sign-out"></i> Sign out</a></li>
									</ul>
								</li>
								
								<li class="ico-menu">
									<a href="www.egovernments.org">
										<img src="/egi/resources/global/images/logo@2x.png" title="Powered by eGovernments" height="20px">
									</a>
								</li>
								
							</ul>
						</div>
						
					</div>
				</nav>
				
			</header>
			
			<div class="main-content login-page">
				
				<div class="main-before-footer">
					<div class="citizen-screens" id="inbox-template">
						
						<div class="row padding-tb"><!-- padding-tb -->
							<div class="search-box col-md-9 col-sm-8">
								<i class="fa fa-search"></i>
								<input type="text" class="form-control" placeholder="Search">
							</div>
							<div class="col-md-3 col-sm-4 text-center xs-margin-top-10">
								<div class="btn-group btn-input clearfix" id="sortby_drop">
									<button type="button" class="btn btn-default dropdown-toggle form-control" data-toggle="dropdown">
										<span data-bind="label">Sort by : <b><i class="fa fa-clock-o"></i> Recent Messages</b></span> <span class="caret"></span>
									</button>
									<ul class="dropdown-menu" role="menu">
										<li><a href="#"><i class="fa fa-clock-o"></i> Recent Messages</a></li>
										<li><a href="#"><i class="user-msg fa fa-user"></i> User Messages</a></li>
										<li><a href="#"><i class="fa fa-database"></i> System Notifications</a></li>
									</ul>
								</div>
							</div>
						</div>
						
						<div class="row container-msgs">
							
							<section class="col-lg-12">
								
								<div class="msg" data-toggle="collapse" data-target="#msgcontent0" aria-expanded="true">
								<header>
									
									<div class="row">
										<i class="user-msg fa fa-user col-sm-1 col-xs-2 read-msg"></i><h3 class="col-sm-11 col-xs-10"> Grievance Redressal<span class="msg-status">Redressed</span> </h3> 
									</div>
									
									
									<div class="msg-info">
										<a href="javascript:void(0);">UGH298</a> <span class="dot">&bull;</span> <span class="msg-date"> 13 Jan 2015</span>
									</div>
								</header>
								
								<div id="msgcontent0" class="msg-content collapse in">
									<p> 
										Complaint No. UGH298 regarding Dog Menace at BegumPet was marked as redressed by Mr. Amit Kumar. Please help us to improve our quality of service by giving your feedback on the quality of service by clicking <a href="">here</a>. 
									</p>
								</div>
							</div> 
							
							
						</section>
						
						<section class="col-lg-12">
							
							<div class="msg" data-toggle="collapse" data-target="#msgcontent1" aria-expanded="true">
								<header>
									<div class="row">
										<i class="fa fa-database col-sm-1 col-xs-2 unread-msg"></i> <h3 class="col-sm-11 col-xs-10"> Property Tax Payment</h3>
									</div>
									<div class="msg-info">
										<a href="javascript:void(0);">JGH1837</a> <span class="dot">&bull;</span> <span class="msg-date"> 4 Mar 2015</span>
									</div>
								</header>
								
								<div id="msgcontent1" class="msg-content collapse">
									<p> 
										Payment of 5,000 is due for Property ID 01-01-100 with the address 34, 2nd cross, Indiranagar, Bangalore - 56. 
										<a href="javascript:void(0);">Click here</a> to pay the taxes. Please ignore if already paid.
									</p>
								</div>
							</div> 
							
							
							<div class="msg" data-toggle="collapse" data-target="#msgcontent2" aria-expanded="true">
								<header>
									
									<div class="row">
										<i class="user-msg fa fa-user col-sm-1 col-xs-2 unread-msg"></i> <h3 class="col-sm-11 col-xs-10">Inspection for New Building Construction Permit<span class="msg-status">Pending Inspection</span>  </h3> 
									</div>
									<div class="msg-info">
										<a href="javascript:void(0);">UGH298</a> <span class="dot">&bull;</span> <span class="msg-date"> 13 Jan 2015</span>
									</div>
								</header>
								
								<div id="msgcontent2" class="msg-content collapse">
									<p> 
										Building Plan application No. BA14567 located at No. 14, 5th Main, Indiranagar, Chennai - 400004.
										Mr. Ram Lele, Asst. Engineer Ward 34 will be visiting your premises for Building inspection on 12/01/2015 at 3:00 PM. Please be there for the inspection. Mr. Lele can be reached at +91 99999 99999
									</p>
								</div>
							</div>
							
						</section>
						
						
						<section class="col-lg-12">
							
							<div class="msg" data-toggle="collapse" data-target="#msgcontent3" aria-expanded="true">
								<header>
									<div class="row">
										<i class="fa fa-database col-sm-1 col-xs-2 read-msg"></i> <h3 class="col-sm-11 col-xs-10"> General Information </h3>
									</div>
									<div class="msg-info">
										<a href="javascript:void(0);">UTK135</a> <span class="dot">&bull;</span> <span class="msg-date"> 2 Mar 2015</span>
									</div>
								</header>
								
								<div id="msgcontent3" class="msg-content collapse">
									<p> 
										Lorem ipsum dolor sit amet, consectetur adipisicing elit,
										sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad
										minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
										commodo consequat.
									</p>
								</div>
							</div> 
							
							<div class="msg" data-toggle="collapse" data-target="#msgcontent4" aria-expanded="true">
								<header>
									<div class="row">
										<i class="fa fa-database col-sm-1 col-xs-2 unread-msg"></i> <h3 class="col-sm-11 col-xs-10"> Grievance Recorded </h3>
									</div>
									<div class="msg-info">
										<a href="javascript:void(0);">UTK135</a> <span class="dot">&bull;</span> <span class="msg-date"> 2 Mar 2015</span>
									</div>
								</header>
								
								<div id="msgcontent4" class="msg-content collapse">
									<p> 
										Lorem ipsum dolor sit amet, consectetur adipisicing elit,
										sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad
										minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
										commodo consequat.
									</p>
								</div>
							</div> 
						</section>
					</div>
					
				</div>
				
				<div class="citizen-screens tabs tabs-style-topline myacc display-hide" id="myaccount">
					<nav>
						<ul>
							<li class="tab-current-myacc" data-section="myaccount" data-myaccount-section="#section-myaccount-1">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-book"></i></div>
									<span class="hidden-sm hidden-xs">My Property</span>
								</a>
							</li>
							<li data-section="myaccount" data-myaccount-section="#section-myaccount-2">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-building-o"></i></div>
									<span class="hidden-sm hidden-xs">My Licences</span>
								</a>
							</li>
							<li data-section="myaccount" data-myaccount-section="#section-myaccount-3">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-rupee"></i></div>
									<span class="hidden-sm hidden-xs">My Professional Tax</span>
								</a>
							</li>
							<li data-section="myaccount" data-myaccount-section="#section-myaccount-4">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-money"></i></div>
									<span class="hidden-sm hidden-xs">My Shops</span>
								</a>
							</li>
							<li data-section="myaccount" data-myaccount-section="#section-myaccount-5">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-adn"></i></div>
									<span class="hidden-sm hidden-xs">My Hoardings</span>
								</a>
							</li>
						</ul>
					</nav>
					<div class="content-wrap">
						<section id="section-myaccount-1"  class="content-current-myacc">
							<div class="visible-xs visible-sm">My Property</div>
							<div class="msg" data-toggle="collapse" data-target="#myaccount1content1" aria-expanded="true">
								<header>
									<div class="row">
										<i class="fa fa-book col-sm-1 col-xs-2 unread-msg"></i><h3 class="col-sm-11 col-xs-10">Mohammed Aslam</h3>
									</div>
									<div class="myaccount-actions">
										<i class="fa fa-desktop col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="View property"></i>
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="Pay taxes"></i> 
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="Apply for property modification"></i>
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="Apply for name transfer"></i>
									</div>
									<div class="msg-info">
										<a href="javascript:void(0);">UTK135</a> <span class="dot">&bull;</span> <span class="msg-date"> Pending Tax : <i class="fa fa-rupee"></i>5003 </span>
									</div>
								</header>
								<div id="myaccount1content1" class="msg-content collapse in">
									<p> 
										Lorem ipsum dolor sit amet, consectetur adipisicing elit,
										sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad
										minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
										commodo consequat.
									</p>
								</div>
							</div> 
							<div class="msg" data-toggle="collapse" data-target="#myaccount1content2" aria-expanded="true">
								<header>
									<div class="row">
										<i class="fa fa-book col-sm-1 col-xs-2 unread-msg"></i><h3 class="col-sm-11 col-xs-10">Dinesh Kumar</h3>
									</div>
									<div class="myaccount-actions">
										<i class="fa fa-desktop col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="View property"></i>
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="Pay taxes"></i> 
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="Apply for property modification"></i>
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="Apply for name transfer"></i>
									</div>
									<div class="msg-info">
										<a href="javascript:void(0);">UTK135</a> <span class="dot">&bull;</span> <span class="msg-date"> Pending Tax : <i class="fa fa-rupee"></i>2365</span>
									</div>
								</header>
								<div id="myaccount1content2" class="msg-content collapse">
									<p> 
										Lorem ipsum dolor sit amet, consectetur adipisicing elit,
										sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad
										minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
										commodo consequat.
									</p>
								</div>
							</div>
							<div class="msg" data-toggle="collapse" data-target="#myaccount1content3" aria-expanded="true">
								<header>
									<div class="row">
										<i class="fa fa-book col-sm-1 col-xs-2 unread-msg"></i><h3 class="col-sm-11 col-xs-10">Gopinath</h3>
									</div>
									<div class="myaccount-actions">
										<i class="fa fa-desktop col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="View property"></i>
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="Pay taxes"></i> 
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="Apply for property modification"></i>
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="Apply for name transfer"></i>
									</div>
									<div class="msg-info">
										<a href="javascript:void(0);">UTK135</a> <span class="dot">&bull;</span> <span class="msg-date"> Pending Tax : <i class="fa fa-rupee"></i>0</span>
									</div>
								</header>
								<div id="myaccount1content3" class="msg-content collapse">
									<p> 
										Lorem ipsum dolor sit amet, consectetur adipisicing elit,
										sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad
										minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
										commodo consequat.
									</p>
								</div>
							</div>
						</section>
						<section id="section-myaccount-2">
							<div class="visible-xs visible-sm">My Licences</div>
							<div class="msg" data-toggle="collapse" data-target="#myaccount2content1" aria-expanded="true">
								<header>
									<div class="row">
										<i class="fa fa-book col-sm-1 col-xs-2 unread-msg"></i><h3 class="col-sm-11 col-xs-10">Mohammed Aslam</h3>
									</div>
									<div class="myaccount-actions">
										<i class="fa fa-desktop col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="View Details"></i>
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="Renew Licence"></i> 
									</div>
									<div class="msg-info">
										<span class="msg-date"> Licence No : </span><a href="javascript:void(0);">UTK135</a> 
									</div>
								</header>
								<div id="myaccount2content1" class="msg-content collapse in">
									<p> 
										Lorem ipsum dolor sit amet, consectetur adipisicing elit,
										sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad
										minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
										commodo consequat.
									</p>
								</div>
							</div> 
							<div class="msg" data-toggle="collapse" data-target="#myaccount2content2" aria-expanded="true">
								<header>
									<div class="row">
										<i class="fa fa-book col-sm-1 col-xs-2 unread-msg"></i><h3 class="col-sm-11 col-xs-10">Dinesh Kumar</h3>
									</div>
									<div class="myaccount-actions">
										<i class="fa fa-desktop col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="View Details"></i>
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="Renew Licence"></i>
									</div>
									<div class="msg-info">
										<span class="msg-date"> Licence No : </span><a href="javascript:void(0);">UTK135</a> 
									</div>
								</header>
								<div id="myaccount2content2" class="msg-content collapse">
									<p> 
										Lorem ipsum dolor sit amet, consectetur adipisicing elit,
										sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad
										minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
										commodo consequat.
									</p>
								</div>
							</div>
						</section>
						<section id="section-myaccount-3">
							<div class="visible-xs visible-sm">My Professional Tax</div>
							<div class="msg" data-toggle="collapse" data-target="#myaccount3content1" aria-expanded="true">
								<header>
									<div class="row">
										<i class="fa fa-book col-sm-1 col-xs-2 unread-msg"></i><h3 class="col-sm-11 col-xs-10">Mohammed Aslam</h3>
									</div>
									<div class="myaccount-actions">
										<i class="fa fa-desktop col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="View Details"></i>
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="Pay professional tax"></i> 
									</div>
									<div class="msg-info">
										<span class="msg-date"> Professional Tax : </span><a href="javascript:void(0);">UTK135</a> 
									</div>
								</header>
								<div id="myaccount3content1" class="msg-content collapse in">
									<p> 
										Lorem ipsum dolor sit amet, consectetur adipisicing elit,
										sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad
										minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
										commodo consequat.
									</p>
								</div>
							</div> 
							<div class="msg" data-toggle="collapse" data-target="#myaccount3content2" aria-expanded="true">
								<header>
									<div class="row">
										<i class="fa fa-book col-sm-1 col-xs-2 unread-msg"></i><h3 class="col-sm-11 col-xs-10">Dinesh Kumar</h3>
									</div>
									<div class="myaccount-actions">
										<i class="fa fa-desktop col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="View Details"></i>
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="Pay professional tax"></i>
									</div>
									<div class="msg-info">
										<span class="msg-date"> Professional Tax : </span><a href="javascript:void(0);">UTK135</a> 
									</div>
								</header>
								<div id="myaccount3content2" class="msg-content collapse">
									<p> 
										Lorem ipsum dolor sit amet, consectetur adipisicing elit,
										sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad
										minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
										commodo consequat.
									</p>
								</div>
							</div>
						</section>
						<section id="section-myaccount-4">
							<div class="visible-xs visible-sm">My Shops</div>
							<div class="msg" data-toggle="collapse" data-target="#myaccount4content1" aria-expanded="true">
								<header>
									<div class="row">
										<i class="fa fa-book col-sm-1 col-xs-2 unread-msg"></i><h3 class="col-sm-11 col-xs-10">Mohammed Aslam</h3>
									</div>
									<div class="myaccount-actions">
										<i class="fa fa-desktop col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="View Agreement"></i>
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="Pay Rent/Lease"></i> 
									</div>
									<div class="msg-info">
										<span class="msg-date"> Agreement No : </span><a href="javascript:void(0);">UTK135</a> 
									</div>
								</header>
								<div id="myaccount4content1" class="msg-content collapse in">
									<p> 
										Lorem ipsum dolor sit amet, consectetur adipisicing elit,
										sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad
										minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
										commodo consequat.
									</p>
								</div>
							</div> 
							<div class="msg" data-toggle="collapse" data-target="#myaccount4content2" aria-expanded="true">
								<header>
									<div class="row">
										<i class="fa fa-book col-sm-1 col-xs-2 unread-msg"></i><h3 class="col-sm-11 col-xs-10">Dinesh Kumar</h3>
									</div>
									<div class="myaccount-actions">
										<i class="fa fa-desktop col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="View Agreement"></i>
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="Pay Rent/Lease"></i>
									</div>
									<div class="msg-info">
										<span class="msg-date"> Agreement No : </span><a href="javascript:void(0);">UTK135</a> 
									</div>
								</header>
								<div id="myaccount4content2" class="msg-content collapse">
									<p> 
										Lorem ipsum dolor sit amet, consectetur adipisicing elit,
										sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad
										minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
										commodo consequat.
									</p>
								</div>
							</div>
						</section>
						<section id="section-myaccount-5">
							<div class="visible-xs visible-sm">My Hoardings</div>
							<div class="msg" data-toggle="collapse" data-target="#myaccount5content1" aria-expanded="true">
								<header>
									<div class="row">
										<i class="fa fa-book col-sm-1 col-xs-2 unread-msg"></i><h3 class="col-sm-11 col-xs-10">Mohammed Aslam</h3>
									</div>
									<div class="myaccount-actions">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="Pay Rent/Lease"></i> 
									</div>
									<div class="msg-info">
										<span class="msg-date"> Agreement No : </span><a href="javascript:void(0);">UTK135</a> 
									</div>
								</header>
								<div id="myaccount5content1" class="msg-content collapse in">
									<p> 
										Lorem ipsum dolor sit amet, consectetur adipisicing elit,
										sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad
										minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
										commodo consequat.
									</p>
								</div>
							</div> 
							<div class="msg" data-toggle="collapse" data-target="#myaccount5content2" aria-expanded="true">
								<header>
									<div class="row">
										<i class="fa fa-book col-sm-1 col-xs-2 unread-msg"></i><h3 class="col-sm-11 col-xs-10">Dinesh Kumar</h3>
									</div>
									<div class="myaccount-actions">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg" data-toggle="tooltip" title="Pay Rent/Lease"></i>
									</div>
									<div class="msg-info">
										<span class="msg-date"> Agreement No : </span><a href="javascript:void(0);">UTK135</a> 
									</div>
								</header>
								<div id="myaccount5content2" class="msg-content collapse">
									<p> 
										Lorem ipsum dolor sit amet, consectetur adipisicing elit,
										sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad
										minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
										commodo consequat.
									</p>
								</div>
							</div>
						</section>
					</div>
				</div>
				
				<div class="citizen-screens tabs tabs-style-topline newrequest display-hide" id="newreq">
					<nav>
						<ul>
							<li class="tab-current-newreq" data-section="newrequest" data-newreq-section="#section-newrequest-1">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-book"></i></div>
									<span class="hidden-sm hidden-xs">Grievance Redressal</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-2">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-rupee"></i></div>
									<span class="hidden-sm hidden-xs">Property Tax</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-3">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-building-o"></i></div>
									<span class="hidden-sm hidden-xs">Building Plan Approval</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-4">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-medkit"></i></div>
									<span class="hidden-sm hidden-xs">Birth & Death</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-5">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-text-width"></i></div>
									<span class="hidden-sm hidden-xs">Trade Licence</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-6">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-rupee"></i></div>
									<span class="hidden-sm hidden-xs">Professional Tax</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-7">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-rupee"></i></div>
									<span class="hidden-sm hidden-xs">Company Tax</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-8">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-money"></i></div>
									<span class="hidden-sm hidden-xs">Shops</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-9">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-adn"></i></div>
									<span class="hidden-sm hidden-xs">Advertisement</span>
								</a>
							</li>
							<li data-section="newrequest" data-newreq-section="#section-newrequest-10">
								<a href="javascript:void(0);">
									<div class="text-center"><i class="fa fa-hand-o-right"></i></div>
									<span class="hidden-sm hidden-xs">Others</span>
								</a>
							</li>
						</ul>
					</nav>
					<div class="content-wrap">
						<section id="section-newrequest-1"  class="content-current-newreq">
							<div class="visible-xs visible-sm">Grievance Redressal</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="complaint.html" class="open-popup col-sm-11 col-xs-10">Register Grievance</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-desktop col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">View Grievance</a>
									</div>
								</header>
							</div> 
						</section>
						<section id="section-newrequest-2">
							<div class="visible-xs visible-sm">Property Tax</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">File New Assessment</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Pay Property Tax</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-external-link col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Link Property to My Account</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-search col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Search Property</a>
									</div>
								</header>
							</div>
						</section>
						<section id="section-newrequest-3">
							<div class="visible-xs visible-sm">Building Plan Approval</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Application for Additional Construction</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Application for Demolition and Reconstruction</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Application for Demolition Only</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Application for New Building Permit</a>
									</div>
								</header>
							</div>
						</section>
						<section id="section-newrequest-4">
							<div class="visible-xs visible-sm">Birth & Death</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Application for Birth/Death Certificate</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-search col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Search for Birth/Death records</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Apply for Name Inclusion</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Record Correction</a>
									</div>
								</header>
							</div>
						</section>
						<section id="section-newrequest-5">
							<div class="visible-xs visible-sm">Trade Licence</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Apply For Trade Licence</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Renew Licence</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-external-link col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Link Licence to my Account</a>
									</div>
								</header>
							</div> 
						</section>
						<section id="section-newrequest-6">
							<div class="visible-xs visible-sm">Professional Tax</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">File New Assessment</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Pay Professional Tax</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-external-link col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Link Profession to my Account</a>
									</div>
								</header>
							</div> 
						</section>
						<section id="section-newrequest-7">
							<div class="visible-xs visible-sm">Company Tax</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">File New Assessment</a>
									</div>
								</header>
							</div> 
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Pay Company Tax</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-external-link col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Link Company to my Account</a>
									</div>
								</header>
							</div>
						</section>
						<section id="section-newrequest-8">
							<div class="visible-xs visible-sm">Shops</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Pay Fees</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-external-link col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Link Shop to my Account</a>
									</div>
								</header>
							</div> 
						</section>
						<section id="section-newrequest-9">
							<div class="visible-xs visible-sm">Advertisement</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Pay Fees</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-external-link col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Link Hoarding to my Account</a>
									</div>
								</header>
							</div> 
						</section>
						<section id="section-newrequest-10">
							<div class="visible-xs visible-sm">Others</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-rupee col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Online Payment For Challans</a>
									</div>
								</header>
							</div>
							<div class="msg">
								<header>
									<div class="row">
										<i class="fa fa-edit col-sm-1 col-xs-2 unread-msg"></i><a href="javascript:void(0)" class="col-sm-11 col-xs-10">Apply For Road Cut</a>
									</div>
								</header>
							</div> 
						</section>
					</div>
				</div>
				
			</div>
			

			
		</div>
		
	</div>
	
	<script src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
	
	<script src="<c:url value='/resources/js/app/homepage.js' context='/egi'/>"></script>
	<script src="<c:url value='/resources/js/app/homepagecitizen.js' context='/egi'/>"></script>
	<script src="<c:url value='/resources/global/js/egov/custom.js' context='/egi'/>"></script>
</body>

</html>																																																							