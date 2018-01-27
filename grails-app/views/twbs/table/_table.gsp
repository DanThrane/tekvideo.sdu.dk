<%@ page import="dk.danthrane.TagLibUtils" %>
<g:if test="${responsive}">
    <div class="table-responsive">
</g:if>
<table class="table ${extraOptions} ${clazz}" ${raw(TagLibUtils.expandAttributes(attrs))}>
    ${raw(body())}
</table>
<g:if test="${responsive}">
    </div>
</g:if>