package com.stockproject.controller.response

import com.stockproject.entity.User

class SignInResponse(val user: User, val token: String)