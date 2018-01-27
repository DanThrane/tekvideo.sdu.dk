<%@ page import="dk.danthrane.TagLibUtils" %>
<div class="${classes}" ${raw(TagLibUtils.expandAttributes(attrs))}>
    ${raw(body())}
</div>