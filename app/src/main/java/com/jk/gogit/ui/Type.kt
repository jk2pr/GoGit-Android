package com.jk.gogit.ui

import com.google.gson.annotations.SerializedName

enum class Type {
   /**
         * The issue was closed by the actor. When the commit_id is present, it identifies the
         * commit that closed the issue using "closes / fixes #NN" syntax.
         */
        closed,
        /**
         * The issue was reopened by the actor.
         */
        reopened,
        commented,
        @SerializedName("comment_deleted") commentDeleted,


        /**
         * The issue title was changed.
         */
        renamed,
        /**
         * The issue was locked by the actor.
         */
        locked,
        /**
         * The issue was unlocked by the actor.
         */
        unlocked,

        /**
         * The issue was referenced from another issue. The `source` attribute contains the `id`,
         * `actor`, and `url` of the reference's source.
         */
        @SerializedName("cross-referenced") crossReferenced,


        /**
         * The issue was assigned to the actor.
         */
        assigned,
        /**
         * The actor was unassigned from the issue.
         */
        unassigned,
        /**
         * A label was added to the issue.
         */
        labeled,
        /**
         * A label was removed from the issue.
         */
        unlabeled,
        /**
         * The issue was added to a milestone.
         */
        milestoned,
        /**
         * The issue was removed from a milestone.
         */
        demilestoned,
}