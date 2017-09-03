<%@ page import="dk.danthrane.TagLibUtils" %>
<ul class="nav ${classes}" ${raw(TagLibUtils.expandAttributes(attrs))}>
    ${raw(body())}
</ul>