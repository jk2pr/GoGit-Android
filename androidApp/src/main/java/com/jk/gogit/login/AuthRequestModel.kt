package com.jk.gogit.login

class AuthRequestModel {

     val scopes: MutableList<String> = mutableListOf()
     var note: String? = null
     var noteUrl: String? = null
   // @SerializedName("client_id")
     var clientId: String? = null
  //  @SerializedName("client_secret")
     var clientSecret: String? = null

    fun generate(): AuthRequestModel {
        val model = AuthRequestModel()
        model.scopes.addAll(listOf("user","user:follow", "admin:org","admin:repo_hook","admin:org_hook","repo" ,"public_repo", "gist", "notifications"))
      //  model.note = BuildConfig.APPLICATION_ID
       // model.clientId = BuildConfig.client_id
        //model.clientSecret = BuildConfig.client_secret
        //model.noteUrl = "https://gogit-5a346.firebaseapp.com/__/auth/handler"
        return model
    }
}