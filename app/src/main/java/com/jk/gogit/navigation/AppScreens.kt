package com.jk.gogit.navigation

sealed class AppScreens(val route: String) {
    data object LOGIN : AppScreens(ScreenName.LOGIN_SCREEN)
    data object HOME : AppScreens(ScreenName.HOME_SCREEN)
    data object USERPROFILE : AppScreens(ScreenName.USER_PROFILE_SCREEN)
    data object USERLIST : AppScreens(ScreenName.USER_LIST_SCREEN)
    data object REPOLIST : AppScreens(ScreenName.REPO_LIST_SCREEN)
    data object SEARCH : AppScreens(ScreenName.SEARCH_SCREEN)
    data object REPODETAIL : AppScreens(ScreenName.REPO_DETAIL_SCREEN)
    data object  PULLREQUESTS : AppScreens(ScreenName.PULL_REQUESTS_SCREEN)
    data object REPOTREESCREEN : AppScreens(ScreenName.REPOTREESCREEN)
    data object ORGLIST : AppScreens(ScreenName.ORGLIST_SCREEN)
    data object ORGDETAIL : AppScreens(ScreenName.ORGDETAIL_SCREEN)
    data object COMMITLIST : AppScreens(ScreenName.COMMIT_LIST_SCREEN)
    data object FILECONTENT : AppScreens(ScreenName.FILE_CONTENT_SCREEN)


    class ScreenName {
        companion object {
            const val LOGIN_SCREEN: String = "login_screen"
            const val HOME_SCREEN: String = "home_screen"
            const val USER_PROFILE_SCREEN: String = "user_profile_screen"
            const val USER_LIST_SCREEN: String = "user_list_screen"
            const val REPO_LIST_SCREEN: String = "repo_list_screen"
            const val SEARCH_SCREEN: String = "search_screen"
            const val REPO_DETAIL_SCREEN: String = "repo_detail_screen"
            const val PULL_REQUESTS_SCREEN: String = "pull_requests_screen"
            const val REPOTREESCREEN:String = "repo_trees_screen"
            const val ORGLIST_SCREEN:String = "org_list_screen"
            const val ORGDETAIL_SCREEN:String = "org_detail_screen"
            const val COMMIT_LIST_SCREEN:String = "commit_list_screen"
            const val FILE_CONTENT_SCREEN:String = "file_content_screen"
        }
    }

}
