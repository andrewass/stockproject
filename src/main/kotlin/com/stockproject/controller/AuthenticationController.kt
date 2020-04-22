package com.stockproject.controller

import com.stockproject.controller.request.SignInRequest
import com.stockproject.controller.request.SignOutRequest
import com.stockproject.controller.request.SignUpRequest
import com.stockproject.controller.response.SignInResponse
import com.stockproject.entity.Investment
import com.stockproject.exception.UsernameNotAvailableException
import com.stockproject.service.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController @Autowired constructor(
        private val authenticationService: AuthenticationService
) {

    @PostMapping("/sign-up")
    @CrossOrigin("*")
    fun signUpUser(@RequestBody signUpRequest: SignUpRequest): ResponseEntity<SignInResponse> {
        return try {
            authenticationService.createUser(signUpRequest)
            val signInRequest = SignInRequest(signUpRequest.username, signUpRequest.password)
            val signInResponse = authenticationService.signInUser(signInRequest)
            ResponseEntity(signInResponse, HttpStatus.OK)
        } catch (e: UsernameNotAvailableException) {
            ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }

    @PostMapping("/sign-in")
    @CrossOrigin("*")
    fun signInUser(@RequestBody signInRequest: SignInRequest): ResponseEntity<SignInResponse> {
        val signInResponse = authenticationService.signInUser(signInRequest)
        return ResponseEntity(signInResponse, HttpStatus.OK)
    }

    @PostMapping("/sign-out")
    @CrossOrigin("*")
    fun signOutUser(@RequestBody signOutRequest: SignOutRequest): ResponseEntity<HttpStatus> {
        authenticationService.signOutUser(signOutRequest)
        return ResponseEntity(HttpStatus.OK)
    }
}