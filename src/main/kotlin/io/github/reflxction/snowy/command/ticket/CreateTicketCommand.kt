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

@BotCommand(
        name = "ticket",
        description = "Create a ticket",
        category = CommandCategory.TICKETS
)
object CreateTicketCommand : CommandCallback {

    /**
     * Processes the command
     */
    override fun onProcess(context: CommandContext) {
        val ticket = SnowyBot.getTicketsProvider().createTicket(context.event.author, context.event.jda)
        context.reply("Successfully created ticket. Head over to <#${ticket.channel}> and address your issues in detail. Make sure to include any relevant information!")
        context.event.guild.getTextChannelById(ticket.channel)?.sendMessage("**${context.event.member?.effectiveName}**, please describe your issues in detail here.")
                ?.queue()
    }
}