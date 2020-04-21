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
package io.github.reflxction.snowy.command.ticket

import io.github.reflxction.snowy.SnowyBot
import io.github.reflxction.snowy.command.core.BotCommand
import io.github.reflxction.snowy.command.core.CommandCallback
import io.github.reflxction.snowy.command.core.CommandCategory
import io.github.reflxction.snowy.command.core.CommandContext
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member

@BotCommand(
        name = "addmember",
        description = "Add a member to the ticket",
        minimumArguments = 1,
        parameters = "<@member to add>",
        category = CommandCategory.TICKETS
)
object AddMemberToTicketCommand : CommandCallback {

    /**
     * Processes the command
     */
    override fun onProcess(context: CommandContext) {
        val target = context.resolve(context.join(0), Member::class.java)
        val member = context.event.member ?: return
        val ticket = SnowyBot.getTicketsProvider().getTicket(context.event.message)
                ?: throw CommandCallback.CommandException("**This channel is not a ticket!**")
        if (!ticket.canEdit(member)) context.noPermission()
        val channel = context.event.guild.getTextChannelById(ticket.channel) ?: return
        channel.upsertPermissionOverride(target).setAllow(Permission.MESSAGE_READ).queue()
        context.reply("Successfully added **${target.effectiveName}** to the ticket.")
    }

}