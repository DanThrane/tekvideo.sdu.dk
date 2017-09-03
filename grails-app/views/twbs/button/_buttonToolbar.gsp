<%@ page import="dk.danthrane.TagLibUtils" %>
<div class="btn-toolbar ${clazz}" ${raw(TagLibUtils.expandAttributes(attrs))}>
    ${raw(body())}
</div>