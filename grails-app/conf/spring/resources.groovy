import dk.sdu.tekvideo.UserDetailsService
import dk.sdu.tekvideo.UserPasswordEncoderListener

// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener, ref('hibernateDatastore'))
    userDetailsService(UserDetailsService)
}
