package tekvideo

import dk.sdu.tekvideo.UpdateElearnCommand
import dk.sdu.tekvideo.UpdatePasswordCommand
import dk.sdu.tekvideo.data.UserData
import spock.lang.Specification
import spock.lang.Unroll

class AccountManagementIntegrationSpec extends Specification {
    def accountManagementService

    def "test updating e-learn"() {
        given: "a user"
        def startId = "elearn"
        def user = UserData.buildUser(elearnid: startId)

        and: "an update command"
        def command = new UpdateElearnCommand(elearn: "New ID")

        when: "the user is not authenticated"
        and: "we attempt to update"
        def result = accountManagementService.updateElearn(command)

        then: "the update fails"
        !result.success

        and: "the ID hasn't changed"
        user.elearnId == startId

        when: "the user is authenticated"
        UserData.authenticateAsUser(user)

        and: "we attempt to update"
        result = accountManagementService.updateElearn(command)

        then: "the update succeeds"
        result.success

        and: "the ID is changed"
        user.elearnId == "New ID"
    }

    // We can't break the line because of how annotations work. Sorry.
    @Unroll("Testing update password with (username = #username, password = #password, commandUsername = #commandUsername, currentPassword = #currentPassword, newPassword = #newPassword, newPassword2 = #newPassword2, authenticated = #authenticated)")
    def "test updating password"() {
        given: "a user"
        def user = UserData.buildUser([username: username, password: password])

        and: "an update command"
        def command = new UpdatePasswordCommand(
                username: commandUsername,
                currentPassword: currentPassword,
                newPassword: newPassword,
                newPassword2: newPassword2)

        when: "the user might be authenticated"
        if (authenticated) {
            UserData.authenticateAsUser(user)
        }

        and: "we attempt to make an update"
        def result = accountManagementService.updatePassword(command)

        then: "we might have succeeded"
        result.success == success

        and: "if we succeeded then our password should change"
        if (success) {
            user.password == newPassword
        }

        and: "if we didn't then our password hasn't changed"
        if (!success) {
            user.password == password
        }

        where:
        username | password    | commandUsername | currentPassword | newPassword | newPassword2 | authenticated | success
        "User"   | "12!@vAlId" | "User"          | "12!@vAlId"     | "14!@vAlId" | "14!@vAlId"  | true          | true
        "User"   | "12!@vAlId" | "User"          | "12!@vAlId"     | "14!@vAlId" | "13!@vAlId"  | true          | false
        "User"   | "12!@vAlId" | "User"          | "12!@vAlId"     | "13!@vAlId" | "14!@vAlId"  | true          | false
        "User"   | "12!@vAlId" | "User"          | "13!@vAlId"     | "14!@vAlId" | "14!@vAlId"  | true          | false
        "User"   | "12!@vAlId" | "User2"         | "13!@vAlId"     | "14!@vAlId" | "14!@vAlId"  | true          | false
        "User"   | "12!@vAlId" | "User2"         | "13!@vAlId"     | ""          | ""           | true          | false
        "User"   | "12!@vAlId" | "User"          | "12!@vAlId"     | "14!@vAlId" | "14!@vAlId"  | false         | false
        "User"   | "12!@vAlId" | "User2"         | "13!@vAlId"     | ""          | ""           | false         | false
    }
}
