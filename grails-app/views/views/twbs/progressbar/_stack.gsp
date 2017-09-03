<%@ page import="dk.danthrane.TagLibUtils" %>
<div class="progress" ${raw(TagLibUtils.expandAttributes(attrs))}>
    ${raw(body())}
</div>