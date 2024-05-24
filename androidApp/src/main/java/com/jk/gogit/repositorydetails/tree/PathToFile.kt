package com.jk.gogit.repositorydetails.tree

import com.hoppers.GetRepositoryTreeQuery

data class PathToFile(
    var path: String = "",
    var file: List<GetRepositoryTreeQuery.Entry> = ArrayList()
)
