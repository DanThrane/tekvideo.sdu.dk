package dk.sdu.tekvideo

import org.hibernate.SessionFactory
import org.springframework.http.HttpStatus

import static dk.sdu.tekvideo.ServiceResult.ok
import static dk.sdu.tekvideo.ServiceResult.fail

/**
 * Provides services for changing basic information about a user's account.
 */
class AccountManagementService {
    def springSecurityService
    def userService
    SessionFactory sessionFactory

    /**
     * Updates the user's e-learn ID.
     *
     * @param command The update command
     * @return A service result, indicating success or failure along with an appreciate message.
     */
    ServiceResult<Void> updateElearn(UpdateElearnCommand command) {
        def user = springSecurityService.currentUser as User
        if (user) {
            if (command.validate()) {
                user.elearnId = command.elearn
                user.save(flush: true)
                ok message: "e-learn ID opdateret successfuldt!"
            } else {
                fail message: "e-learn ID kan ikke være blankt!"
            }
        } else {
            fail message: "Ukendt bruger", suggestedHttpStatus: 401
        }
    }

    /**
     * Updates the user's password. This will validate the command, to require that the password is correctly
     * listed twice, and that the new password is valid according to the security module's constraints.
     *
     * @param command The update command
     * @return A service result, indicating success or failure along with an appreciate message.
     */
    ServiceResult<Void> updatePassword(UpdatePasswordCommand command) {
        def user = springSecurityService.currentUser as User
        if (user) {
            command.username = user.username // The password validator expects a username property
            if (command.validate()) {
                if (command.currentPassword != null && checkPassword(user.password, command.currentPassword)) {
                    user.password = command.newPassword
                    user.save(flush: true)
                    springSecurityService.reauthenticate(user.username)
                    ok message: "Dit kodeord er blevet successfuldt opdateret!"
                } else {
                    fail "Forkert kodeord angivet"
                }
            } else {
                if (command.errors.getFieldError("newPassword")) {
                    fail "Kodeordet er ikke gyldtigt. Det skal indeholde tal, bogstaver og special tegn"
                } else {
                    print command.errors
                    fail "De to kodeord matcher ikke"
                }
            }
        } else {
            fail message: "Ukendt bruger", suggestedHttpStatus: 401
        }
    }

    private boolean checkPassword(String current, String newPassword) {
        springSecurityService.passwordEncoder.isPasswordValid(current, newPassword, null)
    }

    ServiceResult<Collection<Map>> retrieveUserData() {
        def teacher = userService.authenticatedTeacher
        if (!teacher) return fail(message: "Not allowed", suggestedHttpStatus: HttpStatus.FORBIDDEN.value())

        def query = """
            SELECT u.id, u.username, u.email, u.is_cas, u.elearn_id, u.real_name, r.authority
            FROM myusers u, user_role ur, role r
            WHERE
                u.id = ur.user_id AND
                ur.role_id = r.id
        """
        List<List> result = sessionFactory.currentSession.createSQLQuery(query).list()
        Map<Long, Map> collected = [:]
        result.each {
            Map entry = collected[it[0]]
            if (entry == null) {
                entry = [
                        username: it[1],
                        email   : it[2],
                        isCas   : it[3],
                        elearnId: it[4],
                        realName: it[5],
                        roles   : []
                ]
            }

            entry.roles.add(it[6])
            collected[it[0]] = entry
        }
        return ok(collected.values())
    }

    ServiceResult<Void> updateUser(UpdateUserCommand command) {
        def teacher = userService.authenticatedTeacher
        if (teacher) {
            if (command.validate()) {
                User user = User.findByUsernameAndIsCas(command.username, command.isCas)
                user.realName = command.realName
                user.email = command.email
                user.elearnId = command.elearnId

                Set<Role> mappedRoles = command.roles.collect { Role.findByAuthority(it) }.toSet()
                boolean hasBadRoles = mappedRoles.any { it == null }
                if (hasBadRoles) {
                    return fail(message: "Bad request (Some roles does not exist)",
                            suggestedHttpStatus: HttpStatus.BAD_REQUEST.value())
                } else {
                    Set<Role> existingRoles = UserRole.findAllByUser(user).role.toSet()
                    Set<Role> toBeRemoved = new HashSet(existingRoles)
                    Set<Role> toBeAdded = new HashSet()

                    for (def role : mappedRoles) {
                        toBeRemoved.remove(role)
                        if (!existingRoles.contains(role)) {
                            toBeAdded.add(role)
                        }
                    }

                    for (def role : toBeRemoved) {
                        removeRoleFromUser(user, role)
                    }

                    for (def role : toBeAdded) {
                        addRoleToUser(user, role)
                    }
                }

                user.save(flush: true)
                return ok()
            } else {
                println command.errors
                return fail(message: "Bad request (Invalid)", suggestedHttpStatus: HttpStatus.BAD_REQUEST.value())
            }
        } else {
            return fail(message: "Forbidden", suggestedHttpStatus: HttpStatus.FORBIDDEN.value())
        }
    }


    private void addRoleToUser(User user, Role role) {
        // TODO This user creation logic might be needed elsewhere. Might want to consider refactoring these into a
        // more general area, such that these rules are properly enforced.
        UserRole.create(user, role)

        switch (role.authority) {
            case "ROLE_STUDENT":
                def student = new Student(user: user)
                student.save(flush: true)
                break
            case "ROLE_TEACHER":
                def teacher = new Teacher(user: user, alias: user.username)
                teacher.save(flush: true)
                break
        }
    }

    private void removeRoleFromUser(User user, Role role) {
        UserRole.remove(user, role)

        switch (role.authority) {
            case "ROLE_STUDENT":
                def student = Student.findByUser(user)
                student?.delete()
                break
            case "ROLE_TEACHER":
                def teacher = Teacher.findByUser(user)
                teacher?.delete()
                break
        }
    }
}
