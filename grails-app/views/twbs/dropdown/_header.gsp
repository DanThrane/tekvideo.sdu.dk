<%@ page import="dk.danthrane.TagLibUtils" %>
<li role="presentation" class="dropdown-header ${clazz}" ${raw(TagLibUtils.expandAttributes(attrs))}>${raw(body())}</li>