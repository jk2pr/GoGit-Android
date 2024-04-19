import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hoppers.networkmodule.network.AuthManager
import com.jk.gogit.components.localproviders.LocalNavController
import com.jk.gogit.search.SearchScreen
import com.jk.gogit.feed.screen.FeedScreen
import com.jk.gogit.login.screen.LoginScreen
import com.jk.gogit.navigation.AppScreens
import com.jk.gogit.organisation.OrgListScreen
import com.jk.gogit.orgdetails.OrgDetailsScreen
import com.jk.gogit.profile.UserProfileScreen
import com.jk.gogit.pullrequest.PullRequestListScreen
import com.jk.gogit.repos.RepositoryListScreen
import com.jk.gogit.repositorydetails.RepoDetailScreen
import com.jk.gogit.users.UserListScreen

@Composable
fun Start() {
    NavHost(
        navController = LocalNavController.current,
        startDestination = if (AuthManager.getAccessToken() == null) AppScreens.LOGIN.route else AppScreens.USERPROFILE.route
    ) {
        composable(route = AppScreens.LOGIN.route) {
            LoginScreen()
        }
        composable(route = AppScreens.FEED.route) {
            FeedScreen()
        }
        composable(route = AppScreens.USERPROFILE.route) {
            UserProfileScreen()
        }
        composable(route = AppScreens.USERLIST.route) {
            UserListScreen()
        }
        composable(route = AppScreens.REPOLIST.route) {
            RepositoryListScreen()
        }
        composable(route = AppScreens.SEARCH.route) {
            SearchScreen()
        }
        composable(route = AppScreens.REPODETAIL.route) {
            RepoDetailScreen()
        }
        composable(route = AppScreens.PULLREQUESTS.route) {
            PullRequestListScreen()
        }
        composable(route = AppScreens.ORGLIST.route) {
            OrgListScreen()
        }
        composable(route = AppScreens.ORGDETAIL.route) {
            OrgDetailsScreen()
        }
    }
}


