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

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.*

/**
 * A class which reads all ticket data
 */
class TicketsProvider {

    /**
     * The guild
     */
    private val guild: String = ""

    /**
     * The tickets category
     */
    private val ticketsCategory: String = ""

    /**
     * Represents the tickets size that have ever been opened.
     */
    private var size: Int = 0

    /**
     * A map of all tickets mapped, channel ID to ticket ID
     */
    private val channels: MutableMap<String, Int> = HashMap()

    /**
     * A map of all tickets mapped, ticket ID to ticket object
     */
    private val tickets: MutableMap<Int, Ticket> = HashMap()

    /**
     * Creates a ticket for the specified user
     */
    fun createTicket(owner: User, jda: JDA): Ticket {
        val guild: Guild? = jda.getGuildById(guild)
        val category: Category? = guild?.getCategoryById(ticketsCategory)
        val id = ++size
        val channel: TextChannel? = category?.createTextChannel("ticket-$id")?.complete()
        channels[channel?.id!!] = id
        channel.upsertPermissionOverride(guild.publicRole).deny(Permission.MESSAGE_READ).queue()
        channel.upsertPermissionOverride(guild.getMember(owner)!!).setAllow(Permission.MESSAGE_READ).queue()
        val t = Ticket(owner, channel)
        tickets[id] = t
        return t
    }

    /**
     * Fetches the ticket from the specified message. Returns null if that channel is not
     * a ticket channel.
     */
    fun getTicket(message: Message): Ticket? {
        val id = channels.getOrDefault(message.channel.id, -1)
        if (id == -1) return null
        return tickets[id]
    }

    /**
     * Closes the ticket channel
     */
    fun closeTicket(channel: TextChannel) {
        val id: Int = channels.remove(channel.id) ?: return
        tickets.remove(id)
    }

    /**
     * Returns the amount of tickets that have ever been opened.
     */
    fun getSize(): Int {
        return size
    }

}