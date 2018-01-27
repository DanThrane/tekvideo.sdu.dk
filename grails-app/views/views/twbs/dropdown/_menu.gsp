<%@ page import="dk.danthrane.TagLibUtils" %>
<g:content key="dropdown-menu">
<ul class="dropdown-menu ${clazz}" role="${role}" ${raw(labelledBy)} ${raw(TagLibUtils.expandAttributes(attrs))}>
    ${raw(body())}
</ul>
</g:content>