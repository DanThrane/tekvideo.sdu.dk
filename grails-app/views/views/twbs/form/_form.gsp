<%@ page import="dk.danthrane.TagLibUtils" %>
<form class="${classes}" ${raw(TagLibUtils.expandAttributes(attrs))}>
    ${raw(body())}
</form>