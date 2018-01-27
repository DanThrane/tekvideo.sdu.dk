<%@ page import="dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" %>

<sec:ifLoggedIn>
    <twbs:modalButton target="#reportError" style="${ButtonStyle.WARNING}">
        <fa:icon icon="${FaIcon.BUG}"/> Anmeld fejl
    </twbs:modalButton>

    <twbs:modal id="reportError">
        <twbs:modalHeader>Anmeld fejl</twbs:modalHeader>

        <twbs:form method="post" id="errorForm">
            <twbs:textArea name="errorExpected" labelText="Hvad skete der? (*)" required="true" rows="5"/>
            <twbs:textArea name="errorActual" labelText="Hvad skulle der have sket? (*)" required="true" rows="5"/>
        </twbs:form>

        <twbs:modalFooter>
            <twbs:button data-dismiss="modal">Annuller</twbs:button>
            <twbs:button type="submit" form="errorForm" style="${ButtonStyle.SUCCESS}">Indsend</twbs:button>
        </twbs:modalFooter>
    </twbs:modal>

    <script>
        $(function () {
            var modal = $("#reportError");
            var errorForm = $("#errorForm");
            var inputExpected = errorForm.find("[name=errorExpected]");
            var inputActual = errorForm.find("[name=errorActual]");

            errorForm.submit(function (e) {
                e.preventDefault();

                var data = {
                    url: "${request.forwardURI}",
                    expected: inputExpected.val(),
                    actual: inputActual.val()
                };
                Util.postJson("${baseUrl}errorReporting/report", data, {
                    complete: function () {
                        modal.modal("hide");
                        inputExpected.val("");
                        inputActual.val("");
                    }
                });

            });
        });
    </script>
</sec:ifLoggedIn>
