<%@ page import="dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" %>
<twbs:pageHeader>
    <h5>Kommentarer</h5>
</twbs:pageHeader>
<sec:ifLoggedIn>
    <twbs:form method="post" action="${createLink(controller: "exercise", action: "postComment", id: exercise.id)}">
        <twbs:textArea labelText="" placeholder="Skriv en kommentar her" rows="5" name="comment"/>
        <twbs:button type="submit" style="${ButtonStyle.PRIMARY}">
            <fa:icon icon="${FaIcon.ENVELOPE}"/>
            Send
        </twbs:button>
    </twbs:form>
</sec:ifLoggedIn>
<sec:ifNotLoggedIn>
    Du skal være logget ind for at skrive en kommentar!
</sec:ifNotLoggedIn>
<hr>
<g:each in="${exercise.comments}" var="comment">
    <div class="comment">
        <twbs:row>
            <twbs:column sm="9">
                <h6>${comment.user.username} <small><date:utilDateFormatter time="${comment.dateCreated}"/></small>
                </h6>

                <p>${comment.contents}</p>
            </twbs:column>
            <twbs:column sm="1" class="pull-right">
                <sec:ifAllGranted roles="ROLE_TEACHER">
                    <twbs:modalButton target="#comment-delete-modal" data-comment-id="${comment.id}"
                                      style="${ButtonStyle.DANGER}" class="subject-delete">
                        <fa:icon icon="${FaIcon.TRASH}"/>
                    </twbs:modalButton>
                </sec:ifAllGranted>
            </twbs:column>
        </twbs:row>
        <hr>
    </div>
</g:each>

<twbs:modal id="comment-delete-modal">
    <twbs:modalHeader>Er du sikker?</twbs:modalHeader>
    Dette vil slette kommentaren fra denne exercise!
    <twbs:modalFooter>
        <twbs:button data-dismiss="modal">Annulér</twbs:button>
        <twbs:button style="${ButtonStyle.DANGER}" id="comment-delete-button">Slet kommentar</twbs:button>
    </twbs:modalFooter>
</twbs:modal>

<script>
    $(function() {
        var commentToDelete = null;

        $("#comment-delete-modal").on("show.bs.modal", function (e) {
            commentToDelete = $(e.relatedTarget).data("comment-id");
        });

        $("#comment-delete-button").click(function () {
            $("[data-comment-id=" + commentToDelete + "]").closest(".comment")[0].remove();
            $("#comment-delete-modal").modal("hide");
            var data = {comment: commentToDelete};
            Util.postJson("${createLink(controller: "exercise", action: "deleteComment")}?id=${exercise.id}&comment=" + commentToDelete, data, {});
        });
    });
</script>