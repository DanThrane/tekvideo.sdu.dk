package tekvideo

import dk.sdu.tekvideo.CourseStudent
import dk.sdu.tekvideo.Student
import dk.sdu.tekvideo.Teacher
import dk.sdu.tekvideo.UpdateElearnCommand
import dk.sdu.tekvideo.UpdatePasswordCommand
import dk.sdu.tekvideo.UpdateUserCommand
import dk.sdu.tekvideo.UserRole
import dk.sdu.tekvideo.data.CourseData
import dk.sdu.tekvideo.data.UserData
import spock.lang.Specification
import spock.lang.Unroll

class AccountManagementIntegrationSpec extends Specification {
    def accountManagementService
    def studentService

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

    def "test removing roles from existing student"() {
        given: "a student"
        def student = UserData.buildStudent()
        def studentUser = student.user

        and: "a teacher"
        def teacher = UserData.buildTestTeacher()

        and: "and some courses"
        def course = CourseData.buildTestCourse("Course", teacher)

        and: "a command to remove the student privilege"
        def updateCommand = new UpdateUserCommand(
                username: studentUser.username,
                isCas: studentUser.isCas,
                realName: studentUser.realName,
                email: studentUser.email,
                elearnId: studentUser.elearnId,
                roles: []
        )

        when: "we have created the student"
        then: "the student user has the student role"
        UserRole.findAllByUser(studentUser).role.authority.contains("ROLE_STUDENT")

        when: "we sign up the student for the course"
        def signup = studentService.signupForCourse(student, course)

        then: "the student is added to the course, and is listed correctly on the course list"
        signup.success
        studentService.isInCourse(student, course)

        and: "there is only one student in the course"
        CourseStudent.findAllByCourse(course).size() == 1

        when: "we authenticate as the teacher"
        UserData.authenticateAsUser(teacher.user)

        and: "invoke the user update command"
        def updateResult = accountManagementService.updateUser(updateCommand)

        then: "the update succeeded"
        updateResult.success

        and: "there is no student associated with the student user"
        Student.findByUser(studentUser) == null

        and: "and there are no students signed-up for the course"
        CourseStudent.findAllByCourse(course).size() == 0

        and: "the student user no longer has the student role"
        !UserRole.findAllByUser(studentUser).role.authority.contains("ROLE_STUDENT")
    }

    def "test removing roles from existing teacher"() {
        given: "a teacher"
        def teacher = UserData.buildTestTeacher()
        def teacherUser = teacher.user

        and: "a command to remove the privilege"
        def updateCommand = new UpdateUserCommand(
                username: teacherUser.username,
                isCas: teacherUser.isCas,
                realName: teacherUser.realName,
                email: teacherUser.email,
                elearnId: teacherUser.elearnId,
                roles: []
        )

        when: "we have created the student"
        then: "the student user has the student role"
        UserRole.findAllByUser(teacherUser).role.authority.contains("ROLE_TEACHER")

        when: "we authenticate as the teacher"
        UserData.authenticateAsUser(teacher.user)

        and: "invoke the user update command"
        def updateResult = accountManagementService.updateUser(updateCommand)

        then: "the update succeeded"
        updateResult.success

        and: "there is no teacher associated with the teacher user"
        Teacher.findByUser(teacherUser) == null

        and: "the student user no longer has the teacher role"
        !UserRole.findAllByUser(teacherUser).role.authority.contains("ROLE_TEACHER")
    }

    @Unroll("test adding role to user (Role=#role)")
    def "test adding roles to user"() {
        given: "the user roles"
        UserData.buildRoles()

        and: "a user"
        def user = UserData.buildUser()

        and: "a teacher"
        def teacher = UserData.buildTestTeacher()

        and: "a command to add the privilege"
        def updateCommand = new UpdateUserCommand(
                username: user.username,
                isCas: user.isCas,
                realName: user.realName,
                email: user.email,
                elearnId: user.elearnId,
                roles: [role]
        )

        when: "we haven't invoked the command"
        then: "there does not exists a domain for the underlying role"
        RoleDomain.findByUser(user) == null

        and: "the user does not have the correct role type"
        !UserRole.findAllByUser(user).role.authority.contains(role)

        when: "we authenticate as the teacher"
        UserData.authenticateAsUser(teacher.user)

        and: "invoke the command"
        def updateResult = accountManagementService.updateUser(updateCommand)

        then: "the update is successful"
        updateResult.success

        and: "there now exists a domain for the underlying role"
        RoleDomain.findByUser(user) != null

        and: "the role is added"
        UserRole.findAllByUser(user).role.authority.contains(role)

        where:
        RoleDomain | role
        Teacher    | "ROLE_TEACHER"
        Student    | "ROLE_STUDENT"
    }
}
