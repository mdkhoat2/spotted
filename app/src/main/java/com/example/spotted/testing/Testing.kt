package com.example.spotted.testing

import com.example.spotted.backend.dataServices.*
import com.example.spotted.backend.dataServices.DataService

object Testing {
    fun test() {
        AuthDataService.login("caynhat05062004@gmail.com", "123456") { response ->
            if (response != null) {
                println("Token: $response")
            } else {
                println(DataService.getMsg())
            }
        }
    }

    fun forgotPassword(email: String) {
        AuthDataService.forgotPassword(email) {
            println(DataService.getMsg())
        }
    }

    fun resetPassword(oldPassword: String, newPassword: String) {
        AuthDataService.resetPassword(oldPassword, newPassword) {
            println(DataService.getMsg())
        }
    }

    fun sendMessage(receiverId: String, message: String) {
        MessageDataService.sendMessage("66a39662a7e21e8958e46f3e", "Hello") { message ->
            if (message != null) {
                println("Message: $message")
            } else {
                println(DataService.getMsg())
            }
        }
    }
}