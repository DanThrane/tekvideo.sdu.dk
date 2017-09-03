<%@ page import="dk.danthrane.TagLibUtils" %>
<g:if test="${justified}">
    %{-- Native buttons must be wrapped in a btn-group if justified --}%
    <div class="btn-group">
</g:if>

<button class="${classes}" ${raw(disabledAttribute)} ${raw(TagLibUtils.expandAttributes(attrs))}>
    ${raw(body())}
</button>

<g:if test="${justified}">
    </div>
</g:if>