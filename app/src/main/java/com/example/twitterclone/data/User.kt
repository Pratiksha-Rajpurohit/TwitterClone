package com.example.twitterclone.data

data class User(
    val email : String ,
    val userProfileImage: String = "",
    val listOfFollowers : List<String>  = listOf(),
    val listOfTweets : List<String> = listOf(),
    val uid : String = ""
)
