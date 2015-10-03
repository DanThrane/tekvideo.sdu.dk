package dk.sdu.tekvideo

class AccountManagementService {
    Map findAccountInformation() {
        [users: User.list()]
    }
}
