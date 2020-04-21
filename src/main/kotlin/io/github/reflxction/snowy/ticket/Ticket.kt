/*
 * * Copyright 2019-2020 github.com/ReflxctionDev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.reflxction.snowy.ticket

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User

/**
 * Represents a ticket.
 */
class Ticket(owner: User, channel: TextChannel) {

    /**
     * The owner ID
     */
    private val owner: String = owner.id

    /**
     * The channel ID
     */
    val channel: String = channel.id

    /**
     * Returns whether can the specified member edit anything in that ticket. Always use that for permission checks.
     */
    fun canEdit(member: Member): Boolean {
        return member.hasPermission(Permission.ADMINISTRATOR) || member.id == owner
    }

}