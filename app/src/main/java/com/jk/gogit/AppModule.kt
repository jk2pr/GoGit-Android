package com.jk.gogit

import com.jk.gogit.search.SearchExecutor
import com.jk.gogit.search.SearchViewModel
import com.jk.gogit.feed.services.FeedExecutor
import com.jk.gogit.feed.viewmodel.FeedViewModel
import com.hoppers.networkmodule.network.apolloClient
import com.hoppers.networkmodule.network.ktorHttpClient
import com.jk.gogit.profile.UserProfileViewModel
import com.jk.gogit.profile.services.UserProfileExecutor
import com.jk.gogit.repos.RepoListViewModel
import com.jk.gogit.repos.services.RepoExecutor
import com.jk.gogit.repositorydetails.RepoDetailExecutor
import com.jk.gogit.repositorydetails.RepoDetailViewModel
import com.jk.gogit.users.services.UserListExecutor
import com.jk.gogit.users.viewmodel.UserListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { ktorHttpClient }
    single { apolloClient }
    single<DispatcherProvider> { DefaultDispatchers() }
    single<FeedExecutor> { FeedExecutor(get()) }
    single<UserProfileExecutor> { UserProfileExecutor(get()) }
    single<RepoExecutor> { RepoExecutor(get()) }
    single<SearchExecutor> { SearchExecutor(get()) }
    single<UserListExecutor> { UserListExecutor(get()) }
    single<RepoDetailExecutor> { RepoDetailExecutor(get()) }
    viewModel { params -> FeedViewModel(feedExecutor = get(), dispatchers = get(), login = params.get()) }
    viewModel { params -> UserProfileViewModel(userProfileExecutor = get(), dispatchers = get(), login = params.get()) }
    viewModel { params -> RepoListViewModel(repoExecutor = get(), dispatchers = get(), login = params.get()) }
   // viewModel { params -> OverViewModel(overViewExecutor = get(), dispatchers = get(), login = params.get()) }
    viewModel { params -> UserListViewModel(userListExecutor = get(), dispatchers = get(), login = params.get()) }
    viewModel { params -> SearchViewModel(searchExecutor = get(), dispatchers = get()) }
    viewModel { params -> RepoDetailViewModel(repoDetailExecutor = get(), dispatchers = get(), login = params.get(), repo = params.get(), path = params.get()) }
}