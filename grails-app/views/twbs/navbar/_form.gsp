<%@ page import="dk.danthrane.TagLibUtils" %>
<form class="navbar-form ${pull} ${clazz}" ${raw(TagLibUtils.expandAttributes(attrs))}>
    ${raw(body())}
</form>