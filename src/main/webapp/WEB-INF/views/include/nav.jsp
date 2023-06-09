<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<sec:authorize access="isAuthenticated()">
    <sec:authentication property="principal" var="principal" />
</sec:authorize>

<nav class="navigation">
	<a href="/"> <img src="/images/1Doc-chat-Logo.jpg" style="width: 182px; height: 44px" />
	</a> <input type="text" placeholder="Search">
	<div class="navigation__links">
		<a href="/image/explore" class="navigation__link"> 
			<i class="fa fa-compass"></i>
		</a> 
		<a href="#" class="navigation__link like_popup"> 
			<i class="fa fa-heart-o"></i>
		</a> 
		<a href="/user/${principal.user.id}" class="navigation__link"> 
			<i class="fa fa-user-o"></i>
		</a>
	</div>
</nav>

<!-- OK Start Modal -->
<div id="modal">
  <div id="like_popup_list">
	  <div class="like_popup_items">
	  
	  </div>
	  <div class="like_popup_close">
	      <button type="button">close</button>
	  </div>
  </div>

</div>
