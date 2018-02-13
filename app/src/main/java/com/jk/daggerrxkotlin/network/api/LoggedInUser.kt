package com.jk.daggerrxkotlin.network.api

import java.io.Serializable

/**
 * Created by Jitendra on 24/01/2018.
 */
data class LoggedInUser(val phone: String?,val displayName:String?,val email:String?,val photoUrl:String): Serializable

