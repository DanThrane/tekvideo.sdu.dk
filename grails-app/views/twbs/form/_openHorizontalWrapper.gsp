%{-- Opens wrapping row --}%
<g:if test="${isHorizontal}">
    <g:if test="${showLabel}">
    %{-- TODO Should attempt to develop taglib for conditionally wrapping in other taglibs --}%
        <div class="${horizontalStyle.size.getClassName(12 - horizontalStyle.columns)}">
    </g:if>
    <g:else>
        <div class="${horizontalStyle.size.getClassName(12 - horizontalStyle.columns)} ${horizontalStyle.size.getOffsetName(horizontalStyle.columns)}">
    </g:else>
</g:if>