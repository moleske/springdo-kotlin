package io.pivotal

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.provisioning.UserDetailsManager

class UserManager : UserDetailsManager{
    @Autowired lateinit var userRepository: UserRepository

    override fun userExists(username: String?): Boolean = userRepository.findByUsername(username) != null

    override fun updateUser(user: UserDetails?) {
        userRepository.save(user as User)
    }

    override fun createUser(user: UserDetails?) {
        userRepository.save(user as User)
    }

    override fun deleteUser(username: String?) {
        val user = userRepository.findByUsername(username)
        if (user != null) {
            userRepository.delete(user)
        }
    }

    override fun changePassword(username: String?, newPassword: String?) {
        val user = userRepository.findByUsername(username)
        if (user != null) {
            user.password = newPassword
        }
    }

    override fun loadUserByUsername(username: String?): UserDetails? = userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
}