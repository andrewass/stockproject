package com.stockproject.service


import com.stockproject.configuration.util.PasswordEncoder
import com.stockproject.controller.request.SignInRequest
import com.stockproject.controller.request.SignOutRequest
import com.stockproject.controller.request.SignUpRequest
import com.stockproject.controller.response.SignInResponse
import com.stockproject.entity.User
import com.stockproject.exception.IncorrectPasswordException
import com.stockproject.exception.InvalidTokenException
import com.stockproject.exception.UsernameNotAvailableException
import com.stockproject.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService @Autowired constructor(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder
) {

    val userTokenMap = hashMapOf<String, User>()

    fun createUser(request: SignUpRequest): User {
        if (usernameIsUnavailable(request.username)) {
            throw UsernameNotAvailableException("Username : ${request.username}")
        }
        val user = User(
                username = request.username,
                password = passwordEncoder.encode(request.password),
                email = request.email
        )
        return userRepository.save(user)
    }

    fun signInUser(request: SignInRequest): SignInResponse {
        val user = findUser(request.username)
        authenticateUser(user!!, request.password)
        val token = generateUserToken()
        return SignInResponse(user, token)
    }

    fun signOutUser(request: SignOutRequest) {
        userTokenMap.remove(request.token)
    }

    fun getUserFromToken(token: String): User {
        return userTokenMap[token] ?: throw InvalidTokenException("Token : $token")
    }

    private fun generateUserToken(): String {
        var token: String
        do {
            token = UUID.randomUUID().toString()
        } while (userTokenMap.containsKey(token))

        return token
    }

    private fun authenticateUser(user: User, submittedPassword: String) {
        if (!passwordEncoder.matches(submittedPassword, user.password)) {
            throw IncorrectPasswordException("User : ${user.username}")
        }
    }

    private fun usernameIsUnavailable(username: String): Boolean {
        return userRepository.findByUsername(username) != null
    }

    private fun findUser(username: String): User? {
        return userRepository.findByUsername(username)
    }
}