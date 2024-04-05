package com.jk.gogit.navigation

sealed class AppScreens(val route: String) {
    data object LOGIN : AppScreens(ScreenName.LOGIN_SCREEN)
    data object FEED : AppScreens(ScreenName.FEED_SCREEN)
    data object USERPROFILE : AppScreens(ScreenName.USER_PROFILE_SCREEN)
    data object USERLIST : AppScreens(ScreenName.USER_LIST_SCREEN)
    data object REPOLIST : AppScreens(ScreenName.REPO_LIST_SCREEN)
    data object SEARCH : AppScreens(ScreenName.SEARCH_SCREEN)
    data object REPODETAIL : AppScreens(ScreenName.REPO_DETAIL_SCREEN)
    data object  PULLREQUESTS : AppScreens(ScreenName.PULL_REQUESTS_SCREEN)


    class ScreenName {
        companion object {
            const val LOGIN_SCREEN: String = "login_screen"
            const val FEED_SCREEN: String = "feed_screen"
            const val USER_PROFILE_SCREEN: String = "user_profile_screen"
            const val USER_LIST_SCREEN: String = "user_list_screen"
            const val REPO_LIST_SCREEN: String = "repo_list_screen"
            const val SEARCH_SCREEN: String = "search_screen"
            const val REPO_DETAIL_SCREEN: String = "repo_detail_screen"
            const val PULL_REQUESTS_SCREEN: String = "pull_requests_screen"
        }
    }



}
