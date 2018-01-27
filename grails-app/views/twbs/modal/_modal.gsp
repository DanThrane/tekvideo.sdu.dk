<%@ page import="dk.danthrane.TagLibUtils" %>
<div class="modal ${clazz}" role="dialog" aria-labelledby="gridSystemModalLabel" ${raw(TagLibUtils.expandAttributes(attrs))}>
    <div class="modal-dialog ${sizeClass}" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <g:ifContentAvailable key="modal-header">
                    <h4 class="modal-title" id="gridSystemModalLabel">
                        <g:selectContent key="modal-header" />
                    </h4>
                </g:ifContentAvailable>
            </div>
            <div class="modal-body">
                <twbs:container fluid="true">
                    ${raw(body())}
                </twbs:container>
            </div>
            <g:ifContentAvailable key="modal-footer">
                <div class="modal-footer">
                    <g:selectContent key="modal-footer" />
                </div>
            </g:ifContentAvailable>
        </div>
    </div>
</div>