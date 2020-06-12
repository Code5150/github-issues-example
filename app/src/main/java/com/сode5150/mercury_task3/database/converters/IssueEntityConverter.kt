package com.сode5150.mercury_task3.database.converters

import com.сode5150.mercury_task3.database.entities.IssueEntity
import com.сode5150.mercury_task3.network.data.Issue
import com.сode5150.mercury_task3.network.data.User

class IssueEntityConverter {
    companion object {
        fun toIssueEntity(issue: Issue): IssueEntity {
            return IssueEntity(
                issue.number,
                issue.createdAt,
                issue.updatedAt,
                issue.closedAt,
                issue.title,
                issue.body,
                issue.user.login
            )
        }

        fun fromIssueEntity(issue: IssueEntity): Issue {
            return Issue(
                issue.number,
                issue.createdAt,
                issue.updatedAt,
                issue.closedAt,
                issue.title,
                issue.body,
                User(issue.login)
            )
        }
    }
}