package com.achelm.helloworld.models

data class CommunityModel (
    var id: String = "",
    var fullname: String = "",
    var age: String = "",
    var profilePic: String = "",
    var nationality: String = "",
    var nativeLanguage: String = "",
    var learningLanguage: String = "",
    val lastMsg: String = "", // this variable to see the last message in the Chat Fragment
    val lastMsgTime: String = ""
)
