package com.ileite.kotlin.stars

import com.ileite.kotlin.stars.data.model.dto.GitResponseDomain
import com.ileite.kotlin.stars.data.model.dto.OwnerDomain
import com.ileite.kotlin.stars.data.model.dto.RepositoriesDomain
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response

object TestUtils {

    fun getResponseMock() = GitResponseDomain(
        getListDomainMock()
    )

    private fun getListDomainMock(): List<RepositoriesDomain> {
        val domainRepositoryList = mutableListOf<RepositoriesDomain>()
        for (i in 1..4) {
            domainRepositoryList.add(
                RepositoriesDomain(
                    id = i,
                    name = "repository$i",
                    description = "description$i",
                    stars = i,
                    forks = i,
                    owner = OwnerDomain(
                        name = "ownerName$i",
                        img = "OwnerImg$i"
                    ))
            )
        }
        return domainRepositoryList
    }

    fun getResponseError(): Response<GitResponseDomain?>? {
        return Response.error(401, ResponseBody.create(MediaType.get("application/json"), ""))
    }
}