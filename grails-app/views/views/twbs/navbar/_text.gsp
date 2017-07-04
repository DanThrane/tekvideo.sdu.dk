<%@ page import="dk.danthrane.TagLibUtils" %>
<p class="navbar-text ${clazz} ${pull} ${TagLibUtils.expandAttributes(attrs)}">${raw(body())}</p>